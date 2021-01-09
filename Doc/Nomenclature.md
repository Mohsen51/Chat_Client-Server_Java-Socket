# Nomenclature

## Création du pseudo

[flux de connection et de choix du pseudo](https://github.com/hvovar39/ProjetMutlitache/blob/dev_serveur/ChatServeur/Flux%20connexion%20et%20pseudo.png)

## Création d'un groupe

- From Client to Server : **creategroupe;pseudo1;pseudo2;pseudo3;...;message**
- From Server to Client : **groupe;idgroupe**

## Messages

### Messages publics

- From Client to Server : **;message**
- From Server to Client: **pseudo_sender;message**

### Messages privés

- From Client to Server : **private;pseudo_reciever;message**
- From Server to Client : **private;pseudo_sender;message**

### Messages de groupe

- From Client to Server : **groupe;idgroupe;message**
- From Server to Client : **groupe;idgroupe;pseudo_sender;message**

## Gestions

### Fermeture connection

- From Client to Server : **gestion;close**

### Infos clients

- From Client to Server : **gestion;infos**
- From server to Client : **gestion;infos;pseudo**

### Liste des connectées

- From Client to Server : **gestion;list**
- From Server to Client : **gestion;list;pseudo1;pseudo2;pseudo3;...**
