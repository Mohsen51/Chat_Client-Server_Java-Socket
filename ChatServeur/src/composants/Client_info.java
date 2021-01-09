package composants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import server.*;

public class Client_info {
	
	public Socket socket;
	public Client_listener client_listener;
	public Client_sender client_sender;
	public String pseudo;
	
	
	public Client_info (Socket soc, Server_dispatcher sd) {
		this.socket = soc;
		this.pseudo = "not_set";
		try {
			this.client_listener = new Client_listener (this, new BufferedReader( new InputStreamReader (soc.getInputStream())), sd);
			this.client_sender = new Client_sender (this, new PrintWriter (new BufferedWriter (new OutputStreamWriter (soc.getOutputStream())), true));
			this.client_listener.setDaemon(true);
			this.client_sender.setDaemon(true);
			this.client_listener.start();
			this.client_sender.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR FLUX : "+ e +"\n\nUn problème est survenu lors de la création du BufferedReader"
					+ " ou du PrintWriter associé à la socket "+ this.socket);
		}
	}
	
	
}
