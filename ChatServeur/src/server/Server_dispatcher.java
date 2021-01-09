package server;

import java.util.*;
import composants.*;
import java.net.*;

public class Server_dispatcher extends Thread{
	
	private Vector<Message> message_queue;
	private Vector<Client_info> list_client;
	private HashMap<Integer, Team> list_team;
	private int next_team_id;
	
	public void run() {
		this.list_client = new Vector<Client_info>();	
		this.message_queue = new Vector<Message>();
		this.list_team = new HashMap<Integer, Team>();
		this.next_team_id = 0;
		Message msg;
		
		//Boucle d'aiguillage des messages
		while (true) {
			synchronized (this.message_queue) {
				if (!this.message_queue.isEmpty()) {
					msg = this.message_queue.firstElement();
					this.message_queue.remove(0);
					System.out.println("From "+ msg.client_info.pseudo +" :\n" + msg.message);
					
					String[] tab = msg.message.split(";");
					
					//Cas du choix du pseudo
					if (tab[0].equals("pseudo"))
						this.check_pseudo(msg);
										
					//Cas de gestion
					else if (tab[0].equals("gestion")) {
						if (tab[1].equals("close"))
							this.delete_client(msg.client_info);
						else if (tab[1].equals("list"))
							this.send_list_client(msg);
						else
							msg.client_info.client_sender.add_message(new Message (null, "gestion;infos;"+ msg.client_info.pseudo));
					}
					
					//Cas de création d'un groupe
					else if (tab[0].equals("creategroupe"))
						this.create_group(msg);
					
					//Cas d'un message de groupe
					else if (tab[0].equals("groupe"))
						this.send_groupe(msg);
					
					//Cas d'un message public
					else if (tab[0].equals(""))
						send_all(msg);
					
					//Cas d'un message privé
					else
						this.dispatch_message(msg);
				}
			}	
		}
	}
	
	//Crée un client_info associé à la socket, le rajoute à la liste et lance la boucle de pseudo
	public void add_client (Socket soc) {
		synchronized (this.list_client) {
			Client_info ci = new Client_info (soc, this);
			this.list_client.add(ci);
			ci.client_sender.add_message(new Message(ci, "pseudo")); //On envoie le premier message pour le choix du pseudo
		}
	}
	
	//Vérifie le pseudo
	private void check_pseudo (Message msg) {
		String pseudo = msg.message.split(";")[1];
		Client_info ci = this.get_client(pseudo);
		if (ci==null) {
			msg.client_info.pseudo = pseudo;
			msg.client_info.client_sender.add_message(new Message (msg.client_info, "pseudook"));
		}
		else {
			msg.client_info.client_sender.add_message(new Message (msg.client_info, "pseudo"));
		}
			
	}
	
	//Attends la fin des thread sender et listener associé à ci puis enlève ci de la liste
	public void delete_client (Client_info ci) {
		synchronized (this.list_client) {
			try {
				this.list_client.remove(ci);
				ci.client_listener.close();
				this.wait();
				ci.client_sender.close();
				ci.socket.close();
			}catch (Exception e) {
				System.out.println("ERROR fermeture : "+ e + "Un problem est survenu lors de la fermeture de la socket associé"
						+ " au client "+ ci.pseudo);
			}
		}
		
	}
	
	//Traite le message et l'ajoute à la liste des messages du Client_sender concerné
	private void dispatch_message (Message msg) {
		String[] split_message = msg.message.split(";");
		Client_info ci;
		
		if (split_message[0].equals("private")) {
			ci = this.get_client(split_message[1]);
			if (ci == null) 
				msg.client_info.client_sender.add_message( new Message (null, "gestion;noclientfound"));
			else
				ci.client_sender.add_message( new Message (msg.client_info, "private;"+ msg.client_info.pseudo +";"
			+split_message[split_message.length - 1]));
		} else {
			System.out.println("groupe");
		}
				
	}
	
	//Envoie le message à tous les clients
	private void send_all(Message msg) {	
		String new_msg = msg.client_info.pseudo + msg.message;
		synchronized (this.list_client) {
			for (int i = 0; i<this.list_client.size(); i++)
				this.list_client.elementAt(i).client_sender.add_message(new Message (msg.client_info, new_msg));
		}
	}
	
	//Envoie un message à tout un groupe
	private void send_groupe (Message msg) {
		String[] split_msg = msg.message.split(";");
		String s;
		if (msg.client_info == null)
			s = split_msg[0] +";"+ split_msg[1] +";gestion;"+split_msg[2];
		else			
			s = split_msg[0] +";"+ split_msg[1] +";"+ msg.client_info.pseudo +";"+ split_msg[2];
		Client_info ci;
		synchronized (this.list_team) {
			Team team = this.list_team.get(Integer.parseInt(split_msg[1]));
			for (int i = 0; i < team.list_client.size(); i++) {
				ci = team.list_client.get(i);
				ci.client_sender.add_message(new Message (msg.client_info, s));
			}
		}
	}
	
	//Creation d'un nouveau groupe
	private void create_group (Message msg) {
		String[] split_msg = msg.message.split(";");
		Client_info ci;
		Team team = new Team();
		for (int i = 1; i < split_msg.length-1; i++) {
			ci = this.get_client(split_msg[i]);
			if (ci != null) 
				team.list_client.add(ci);
		}
		synchronized (this.list_team) {
			this.list_team.put(this.next_team_id, team);
		}
		send_groupe (new Message (null, "groupe;"+ this.next_team_id));
		this.next_team_id ++;
	}
	
	//Envoie la liste des clients
	private void send_list_client (Message msg) {
		String s = "gestion;list";
		synchronized (this.list_client) {
			for (int i = 0; i < this.list_client.size(); i++)
				s+=";"+ this.list_client.get(i).pseudo;
		}
		msg.client_info.client_sender.add_message (new Message (null, s));
	}
	
	//Permet aux client_listener d'ajouter un message dans la liste des messages à dispatcher
	public void add_message_to_queue (Message msg) {
		synchronized (this.message_queue) {
			this.message_queue.add(msg);
		}
	}
	
	//Permet de récupérer un client à partir de son pseudo
	private Client_info get_client (String ci) {
		synchronized (this.list_client) {
			for (int i = 0; i<this.list_client.size(); i++) {
				if (this.list_client.elementAt(i).pseudo.equals(ci))
					return this.list_client.elementAt(i);
			}
		}
		return null;
	}
}
