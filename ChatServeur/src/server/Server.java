package server;

import java.net.*;

public class Server {
	
	public static int listening_port;

	public static void main(String[] args) {
		Server_dispatcher dispatcher = new Server_dispatcher();
		dispatcher.setDaemon(true);
		Socket soc;
		
		try {
			ServerSocket serv_soc = new ServerSocket (8080);
			dispatcher.start();
			System.out.println("\n===================================================\nServer is ON"
					+ "\n===================================================\n");

			//Boucle pour la récupération d'utilisateur
			while (true) {
				soc = serv_soc.accept();
				dispatcher.add_client(soc);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR port binding "+ e +"\nUn probleme est survenu lors de l'ouverture du port"+ listening_port);
		}
	}

}
