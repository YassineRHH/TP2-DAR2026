package clientPackage;

import commonPackage.Operation;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client2 {

    // nombre, opérateur (+ - * /), nombre ; espaces facultatifs
    static final Pattern SYNTAX = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+\\-*/])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$");

    public static void main(String[] args) {

        try {
            // 1. Se connecter au serveur
            System.out.println("Je suis un client pas encore connecté");

            Socket socket = new Socket("localhost", 1234);
            System.out.println("Je suis un client connecté");

            // 2. Flux de communication : octets
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            // 3. Flux de traitement : objets
            // ObjectOutputStream d'abord, vidé aussitôt (en-tête de sérialisation),
            // puis ObjectInputStream : même ordre des deux côtés pour éviter l'interblocage.
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(is);

            Scanner sc = new Scanner(System.in);

            while (true) {

                System.out.print("Donnez une opération (ex : 55 * 25) ou quit : ");

                String saisie = sc.nextLine().trim();

                if (saisie.equals("quit")) {
                    break;
                }

                // Validation de la syntaxe AVANT l'envoi
                Matcher m = SYNTAX.matcher(saisie);

                if (!m.matches()) {
                    System.out.println("Syntaxe invalide, rien n'est envoyé.");
                    continue;
                }

                // b) Le client construit l'objet Operation et l'envoie
                Operation operation = new Operation(
                        Double.parseDouble(m.group(1)),   // premier opérande
                        m.group(2).charAt(0),             // opérateur
                        Double.parseDouble(m.group(3)));  // deuxième opérande

                oos.writeObject(operation);
                oos.flush();

                // g) Le client lit et affiche le résultat
                String reponse = (String) ois.readObject();
                System.out.println("Résultat : " + reponse);
            }

            oos.writeObject("quit"); // prévenir le serveur
            oos.flush();
            System.out.println("Fin de la communication");

            // 4. Fermer
            sc.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Classe introuvable côté client : " + e.getMessage());
        }
    }
}