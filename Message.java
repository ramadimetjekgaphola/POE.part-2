/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registration;

/**
 *
 * @author METJA MONARE
 */
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Message {

    private static int totalMessagesSent = 0;
    private static ArrayList<String> sentMessages = new ArrayList<>();

    public static void startMessaging(Scanner scanner) {
        System.out.print("How many messages would you like to send? ");
        int numMessages = 2;
        try {
            numMessages = Integer.parseInt(scanner.nextLine().trim());
            if (numMessages < 1) numMessages = 1;
        } catch (Exception e) {
            System.out.println("Invalid input. Defaulting to 2 messages.");
        }

        System.out.println("\nYou can now send " + numMessages + " message(s).\n");

        for (int i = 0; i < numMessages; i++) {
            sendSingleMessage(scanner);
        }

        System.out.println("\nTotal messages sent: " + returnTotalMessages());
        System.out.println("Returning to main menu...\n");
    }

    private static void sendSingleMessage(Scanner scanner) {
        System.out.println("\n--- New Message ---");

        
        String recipient;
        do {
            System.out.print("Enter recipient cell number: ");
            recipient = scanner.nextLine().trim();
            System.out.println(checkRecipientCell(recipient));
        } while (!recipient.startsWith("+27") || recipient.length() > 12);

        
        String messageText;
        do {
            System.out.print("Enter message (max 250 characters): ");
            messageText = scanner.nextLine().trim();
            if (messageText.length() > 250) {
                System.out.println("Message exceeds 250 characters by " + (messageText.length() - 250) + "; please reduce the size.");
            } else {
                System.out.println("Message ready to send.");
            }
        } while (messageText.length() > 250);

        String messageID = generateMessageID();
        String messageHash = createMessageHash(messageID, messageText);

        System.out.println("\nMessage Details:");
        System.out.println("Message ID     : " + messageID);
        System.out.println("Message Hash   : " + messageHash);
        System.out.println("Recipient      : " + recipient);
        System.out.println("Message        : " + messageText);

        
        System.out.println("\n1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");
        System.out.print("Choose option: ");
        String option = scanner.nextLine().trim();

        String result = sentMessage(option, messageID, messageHash, recipient, messageText);
        System.out.println(result);
    }

    private static String generateMessageID() {
        Random rand = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    public static boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() <= 10;
    }

    public static String checkRecipientCell(String cell) {
        if (cell.startsWith("+27") && cell.length() <= 12) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public static String createMessageHash(String messageID, String message) {
        String firstTwoID = messageID.length() >= 2 ? messageID.substring(0, 2) : "00";
        String[] words = message.trim().split("\\s+");
        int wordCount = words.length;
        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();
        return firstTwoID + ":" + wordCount + ":" + firstWord + lastWord;
    }

    
    public static String sentMessage(String option, String messageID, String messageHash, String recipient, String messageText) {
        
        switch (option) {
            case "1":  
                totalMessagesSent++;
                String entry = "Message ID: " + messageID +
                        " | Hash: " + messageHash +
                        " | To: " + recipient +
                        " | Message: " + messageText;
                sentMessages.add(entry);
                return "Message successfully sent.";

            case "2":  
                return "Press 0 to delete the message.";

            case "3":  
                saveMessageToJson(messageID, messageHash, recipient, messageText, "Stored");
                return "Message successfully stored.";

            default:
                totalMessagesSent++;
                sentMessages.add("Message ID: " + messageID + " | Hash: " + messageHash + " | To: " + recipient + " | Message: " + messageText);
                return "Message successfully sent.";
        }
    }

   
    public static void saveMessageToJson(String messageID, String messageHash, String recipient, String messageText, String status) {
        
        try {
            FileWriter writer = new FileWriter("messages.json", true);  
            
            writer.write("{\n");
            writer.write("\"messageID\":\"" + messageID + "\",\n");
            writer.write("\"messageHash\":\"" + messageHash + "\",\n");
            writer.write("\"recipient\":\"" + recipient + "\",\n");
            writer.write("\"message\":\"" + messageText + "\",\n");
            writer.write("\"status\":\"" + status + "\"\n");
            writer.write("},\n");
            
            writer.close();
            System.out.println("Message successfully saved to messages.json");
            
        } catch (IOException e) {
            System.out.println("Error saving message.");
        }
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentMessages.size(); i++) {
            sb.append("Message ").append(i + 1).append(": ").append(sentMessages.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public static void resetForTesting() {
        totalMessagesSent = 0;
        sentMessages.clear();
    }
}