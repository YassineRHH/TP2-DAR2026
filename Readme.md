# TP2 - Applications réparties

## Présentation

Ce projet met en place une calculatrice répartie en Java. Un client saisit une opération, l'envoie à un serveur via une connexion TCP, puis affiche le résultat calculé par le serveur. Les opérations prises en charge sont l'addition, la soustraction, la multiplication et la division.

## Synthèse 1 : échange de données textuelles

`Client` et `Server` communiquent avec des flux texte (`PrintWriter` et `BufferedReader`). Le client valide la saisie puis transmet l'opération en trois lignes : premier opérande, opérateur et second opérande. Le serveur effectue le calcul et renvoie le résultat sous forme de texte. La commande `quit` met fin à l'échange.

## Synthèse 2 : échange d'objets sérialisés

`Client2` et `Server2` utilisent des flux d'objets Java. Le client encapsule les deux opérandes et l'opérateur dans une instance de `Operation` (qui est sérialisable), puis l'envoie au serveur. Le serveur lit l'objet, calcule le résultat et renvoie une réponse textuelle. Cette version illustre la sérialisation d'objets sur une socket TCP.

## Exécution

Lancer d'abord le serveur, puis le client correspondant. Les deux versions écoutent sur le port `1234` de la machine locale :

| Version | Serveur | Client |
|---|---|---|
| Flux texte | `serverPackage.Server` | `clientPackage.Client` |
| Objets sérialisés | `serverPackage.Server2` | `clientPackage.Client2` |

Saisir une opération comme `55 * 25`. Entrer `quit` pour fermer la communication.

## Captures d'écran

Configuration d'exécution :

![Configuration d'exécution](tp2%2C.jpg)

Résultat affiché côté client :

![Console du client](tp2..jpg)

Réception et calcul côté serveur :

![Console du serveur](tp2.jpg)