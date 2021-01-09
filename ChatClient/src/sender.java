import java.io.PrintWriter;
import java.util.Scanner;

public class sender extends Thread {

	private PrintWriter pw;
	private Scanner sc;
	protected String statusPseudo;

	public sender (PrintWriter pw, Scanner sc) {
		this.pw = pw;
		this.sc = sc;
		this.statusPseudo = "pseudo;start";
	}

	//Thread pour envoyer un message
	public void run () {
		String s;
		try {
			String pseudo;
			do {
				System.out.println("Veuillez entrer un pseudo pour vous connecter\n");
				pseudo = sc.nextLine();
				s = "pseudo;" + pseudo;
				pw.println(s);
				System.out.println("Bienvenue " + pseudo + ", entrez pour continuer...");
				sc.nextLine();
				if (this.statusPseudo.equals("pseudo;err"))
					System.out.println("Oops le pseudo est déjà utilisé, veuillez en entrer un nouveau");
			} while (!this.statusPseudo.equals("pseudo;ok"));
			while (true) {
				String choice;
				do {
					System.out.println("Voulez vous envoyer des messages ou gérer votre session ?\n(Veuillez entrer message ou gestion)\nEntrez 'quit!' pour quitter\n ");
					choice = sc.nextLine();
					if (choice.equals("quit!"))
						pw.println("gestion;close");
				} while (!choice.equals("message") && !choice.equals("gestion"));
				if (choice.equals("message")) {
					String mess;
					System.out.println("Bienvenue dans le chat\n--------------------------------------");
					System.out.println("Entrez action :\nmessage privé : entrez private\nmessage public : entrez public\nmessage de groupe : entrez groupe\n");
					String stat = sc.nextLine();
					switch (stat) {
						case "private":
							System.out.println("Veuillez entrer le pseudo du destinataire");
							s = "private;" + sc.nextLine() + ";";
							while (true) {
								System.out.println("Veuillez entrer le message (entrez 'quit!' pour changer d'option)");
								mess = sc.nextLine();
								if (mess.equals("quit!"))
									break;
								s = s + mess;
								pw.println(s);
							}
							break;
						case "public":
							while (true) {
								System.out.println("Veuillez entrer le message (entrez 'quit!' pour changer d'option)");
								mess = sc.nextLine();
								if (mess.equals("quit!"))
									break;
								s = ";" + mess;
								pw.println(s);
							}
							break;
						case "groupe":
							String newGroupe;
							do {
								System.out.println("Voulez vous créer un groupe ou vous en possédez déjà un ? (entrez oui ou non) ");
								newGroupe = sc.nextLine();
							} while (!newGroupe.equals("oui") && !newGroupe.equals("non"));
							if (newGroupe.equals("oui")) {
								System.out.println("Veuillez entrer le nombre de personnes que vous voulez ajouter à votre groupe ");
								int n = sc.nextInt();
								sc.nextLine();
								s = "creategroupe;";
								for (int i = 0; i < n; i++) {
									System.out.println("Veuillez entrer le pseudo d'une personne que vous voulez ajouter au groupe ");
									s = s + sc.nextLine() + ";";
								}
								pw.println(s);
								System.out.println("Groupe créé");
							}
							System.out.println("Veuillez entrer l'ID du groupe sur lequel vous voulez envoyer un message ");
							s = "groupe;" + sc.nextLine() + ";";
							while (true) {
								System.out.println("Veuillez entrer le message (entrez 'quit!' pour changer d'option)");
								mess = sc.nextLine();
								if (mess.equals("quit!"))
									break;
								s = s + mess;
								pw.println(s);
							}
							break;
						default:
							System.out.println("Cette option n'existe pas");
							System.out.println("Si vous voulez quiter la session, entrez close\nPour continuer 'entrer'");
							s = sc.nextLine();
							if (s.equals("close"))
								pw.println("gestion;close");
					}
				}
				else {
					String stat;
					do {
						System.out.println("Bienvenue dans l'onglet de gestion de session\n--------------------------------------");
						System.out.println("Entrez action :\nPour obtenir vos infos de session : entrez infos\nPour obtenir la liste des utilisateurs connéctés : entrez list\nEntrez close pour vous déconnecter\n");
						stat = sc.nextLine();
					} while (!stat.equals("close") && !stat.equals("infos") && !stat.equals("list"));
					s = "gestion;" + stat;
					pw.println(s);
					sc.nextLine();
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR "+e);
			e.printStackTrace();
		}
	}

	//Fonction pour le changement du statut du pseudo (pseudo;err si pseudo dejà utilisé)
	public void changeStatus(String newStat) {
		this.statusPseudo = newStat;
	}
}