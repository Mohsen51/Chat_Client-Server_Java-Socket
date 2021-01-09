# ChatClient

## src/

Contient le client, ainsi que les threads Sender et Listener.

- **Le client (client.java)** permet de lancer le serveur socket, ainsi que les threads dont il initialise les objets BufferReader pour lire les entrées seveur ainsi que PrintWriter pour envoyer des messages au serveur.
- **Le Listener (listener.java)** permet d'écouter les sorties serveur et d'informer l'utilisateur de ce qu'il reçoit.
- **Le Sender (sender.java)** permet d'écouter les entrées client et d'informer le serveur des entrées utilisateur. <br/>Une grande partie de ses fonctions réside dans le guidage de l'utilisateur dans la partie envoie au serveur.

## src/GUI

Un projet d'interface graphique a été envisagé par l'équipe mais le temps n'était pas suffisant pour l'achever malheureusment.