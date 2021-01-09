import java.net.*;
import java.io.*;
import java.util.*;
import GUI.Main;

public class Client {

	public static void main(String[] args) {
	 	try {

			//Thread du lancement du GUI (non utilisé mais prévu)
			Thread gui = new Thread(){
				public void run() {
					Main.main(new String[]{});
				}
			};

	 		//Initialisation
	 	 	Scanner sc = new Scanner(System.in);
			Socket client = new Socket("localhost",8080);

			// Flux qui permet de recevoir
			BufferedReader br = new BufferedReader( new InputStreamReader (client.getInputStream()));
			//Flux qui permet d'envoyer
		 	PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);

		 	//On initialise les threads en créant les objets thread sender et listener
			sender sen = new sender(pw, sc);
			//On associe sender à listener pour pouvoir avertir en cas de création d'un pseudo déjà utilisé
			listener lis = new listener (br, sen);

			//On lance les threads ici
			lis.start();
			sen.start();

			//Lancement du GUI (non utilisé)
			//gui.start();

			System.out.println("Bienvenue sur le serveur de chat\n----------------------------------------\n");


	 	} catch (Exception e) {System.out.println("PB client"+ e);}
	}
}
