package composants;

import server.*;
import java.io.*;

public class Client_listener extends Thread{
	
	private Server_dispatcher server_dispatcher;
	private Client_info client_info;
	private BufferedReader in;
	private boolean running;
	
	public Client_listener (Client_info ci, BufferedReader br, Server_dispatcher sd) {
		this.client_info = ci;
		this.in = br;
		this.server_dispatcher = sd;
		this.running = true;
	}
	
	public void run () {
		String msg;
		while (this.running) {
			try {
				msg = this.in.readLine();
				this.server_dispatcher.add_message_to_queue(new Message(this.client_info, msg));
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("ERROR INPUT : "+ e +"\nUn problème est survenu lors de la réception"
					+ " d'un message du client "+ this.client_info.pseudo);
				break;
			}
		}
	}
	
	public void close() {
		try {
			synchronized (this) {
				this.wait();
				this.running = false;	
				this.notify();
				this.in.close();
				this.server_dispatcher.notify();
			}
		}catch (Exception e) {
			System.out.println("ERROR fermeture : "+ e +"Un problement est survenu lors de la fermeture du Reader de "
					+ this.client_info.pseudo);
			e.printStackTrace();
		}
	}
}
