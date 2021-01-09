import java.io.BufferedReader;

public class listener extends Thread{

	private BufferedReader br;
	private String status;
	private sender sen;

	public listener (BufferedReader br, sender sen) {
		this.br = br;
		this.sen = sen;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	//Thread pour écouter la réception un message
		public void run() {
			String s;
			while (true) {
				try {
					s = br.readLine();
					sen.changeStatus(s);
					if (s.equals("pseudo;ok")) {
						while (true) {
							s = br.readLine();
							//Permet de vérifier les réponses serveur via la nomenclature
							String[] message = s.split(";");
							if (message[0].equals("private"))
								System.out.println(message[1] + " vous a envoyé un message : " + message[2]);
							if (message[0].equals("groupe"))
							{
								if(message.length==2)
									System.out.println("Felicitations, votre groupe a été créé et a pour ID : " + message[1]);
								System.out.println(message[2] + " a envoyé un message au groupe " + message[1] + " : " + message[3]);
							}
							if (message[0].equals("gestion"))
							{
								if (message[1].equals("infos"))
									System.out.println("Vous êtes connectés sous le pseudo : "+message[2] + " (entrer pour continuer...)");
								if (message[1].equals("list"))
								{
									System.out.println("Les personnes suivantes sont connéctées sur le serveur : (entrer pour continuer...) ");
									for (int i = 2 ; i<message.length;i++) {
										System.out.println(message[i]);
									}
								}
							}
							else {
								//Affiche les messages public uniquement
								if (message.length==2)
									System.out.println("L'utilisateur " + message[0] +  " a envoyé un message public : " + message[1]);
							}
						}
					}
				}catch (Exception e) {
					System.out.println("ERROR "+e);
					e.printStackTrace();
				}
			}
		}
	}