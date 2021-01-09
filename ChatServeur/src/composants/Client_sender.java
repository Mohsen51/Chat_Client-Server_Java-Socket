package composants;

import java.util.*;
import java.io.*;

public class Client_sender extends Thread{
	
	private Vector<Message> message_queue;
	private Client_info client_info;
	private PrintWriter out;
	private boolean running;
	
	public Client_sender (Client_info ci, PrintWriter pw) {
		this.client_info = ci;
		this.out = pw;
		this.message_queue = new Vector<Message>();
		this.running = true;
	}
	
	public void run () {
		int flag_pseudo = 0;
		Message msg;
		
		//Boulve d'écoute
		while (this.running) {
			synchronized (this.message_queue) {
				if (!this.message_queue.isEmpty()) {
					System.out.println(this.message_queue);
					msg = this.message_queue.firstElement();
					this.message_queue.remove(0);
					
					//Cas du choix du pseudo
					if (msg.message.equals("pseudo")) {
						this.get_pseudo(flag_pseudo);
						flag_pseudo --;
					}
					else if (msg.message.equals("pseudook")) {
						flag_pseudo = 1;
						this.get_pseudo(flag_pseudo);
					}
					
					//Cas général
					else if (!msg.message.equals("")) 
						send_message (msg);
					
				}
			}
		}
			
	}
	
	//Ajoute le message msg à queue_message
	public void add_message (Message msg) {
		synchronized (this.message_queue){
			this.message_queue.add(msg);
		}
	}
	
	/*Envoie les message pour la boucle pseudo. flag<0 pour commencer, flag>0 si pseudo ok (et fin de la boucle), 
	flag == 0 si pseudo déjà utilisé*/
	public void get_pseudo (int flag) {
		if (flag==0) {
			this.out.println("pseudo;start");
		}
		else if (flag<0) {
			this.out.println("pseudo;err");
		}
		else
			this.out.println("pseudo;ok");
	}

	private void send_message (Message s) {
		this.out.println(s.message);
	}
	
	public void close() {
		try {
			this.wait();
			this.running = false;
			this.notify();
			this.out.close();
		}catch (Exception e) {
			System.out.println("ERROR fermeture : "+ e +"Un problement est survenu lors de la fermeture du Writer de "
					+ this.client_info.pseudo);
		}
	}
	
}
