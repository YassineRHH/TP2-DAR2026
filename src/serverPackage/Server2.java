package serverPackage;

import commonPackage.Operation;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2 {

    public static void main(String[] args) {

        try {
            // 1. Écouter puis accepter un client
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Je suis un serveur en attente la connexion d'un client");

            Socket socket = serverSocket.accept();
            System.out.println("un client est connecté");

            // 2. Flux de communication : octets
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // 3. Flux de traitement : objets
            // L'ObjectOutputStream est créé (et vidé) avant l'ObjectInputStream
            // pour que l'en-tête de sérialisation parte tout de suite : sinon
            // client et serveur restent bloqués à s'attendre mutuellement.
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(is);

            while (true) {

                // c) Le serveur reçoit l'objet
                Object recu = ois.readObject();

                // "quit" : le client demande la fin de la communication
                if (recu instanceof String && recu.equals("quit")) {
                    break;
                }

                String reponse;

                if (recu instanceof Operation) {

                    // d) Le serveur récupère les données de l'objet reçu
                    Operation operation = (Operation) recu;
                    double a = operation.getOperande1();
                    String operateur = String.valueOf(operation.getOperateur());
                    double b = operation.getOperande2();

                    System.out.println("Objet Operation reçu : "
                            + formatter(a) + " " + operateur + " " + formatter(b));

                    // e) Le serveur effectue le calcul demandé
                    reponse = calculer(a, operateur, b);

                } else {
                    reponse = "Erreur : objet reçu invalide";
                }

                // f) Le serveur renvoie le résultat au client
                oos.writeObject(reponse);
                oos.flush();
                System.out.println("Réponse envoyée : " + reponse);
            }

            System.out.println("Fin de la communication");

            // 4. Fermer
            socket.close();
            serverSocket.close();

        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            // readObject() : le serveur ne possède pas la classe de l'objet reçu
            System.out.println("Classe introuvable côté serveur : " + e.getMessage());
        }
    }

    // Effectue l'opération et renvoie le texte à afficher chez le client
    static String calculer(double a, String operateur, double b) {

        double r;

        switch (operateur) {

            case "+":
                r = a + b;
                break;

            case "-":
                r = a - b;
                break;

            case "*":
                r = a * b;
                break;

            case "/":
                if (b == 0) {
                    return "Erreur : division par zéro impossible";
                }
                r = a / b;
                break;

            default:
                return "Erreur : opérateur inconnu";
        }

        return formatter(a) + " " + operateur + " " + formatter(b) + " = " + formatter(r);
    }

    // 1375.0 -> "1375" ; 2.5 -> "2.5"
    static String formatter(double x) {
        if (x == Math.rint(x) && !Double.isInfinite(x)) {
            return String.valueOf((long) x);
        }
        return String.valueOf(x);
    }
}