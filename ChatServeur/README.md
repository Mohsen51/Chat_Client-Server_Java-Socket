# ChatServeur

## src/server

Contient le main, ainsi que le dispatcher.

- **Le main** permet de récuperer les connexions.
- **Le Dispatcher** permet de gérer les clients, les 2 threads associés à chauqe client, les groupes, ainsi que l'aiguillage des messages.

## src/composants

Contient les composants spécifiques.

- **Client_info** : chaque instance correspond à un client connecter. Chaque client est donc associé de façons unique à une socket, un pseudo, un listener et un sender.
- **Client_listener** : Gére la réception des messages.
- **Client_sender** : Gére l'envoie des messages.
- **Message** : classe spécifique permettant d'associer un "message" (String) au client éxpéditeur.
- **Team** : classe spécifique permettant de stocker une liste de client appartenant à un groupe.