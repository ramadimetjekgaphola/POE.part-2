/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registration;

/**
 *
 * @author METJA MONARE
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class MessageStorage {

    public static void storeMessage(String messageID, String messageHash, String recipient, String messageText) {
        
        String fileName = "messages.json";
        File file = new File(fileName);
        
        String jsonEntry = String.format(
            "{\"messageID\":\"%s\",\"messageHash\":\"%s\",\"recipient\":\"%s\",\"message\":\"%s\"}",
            messageID, 
            messageHash, 
            recipient, 
            messageText.replace("\"", "\\\"")
        );

        try {
            if (!file.exists()) {
               
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[\n" + jsonEntry + "\n]");
                    System.out.println("Message successfully stored in messages.json");
                }
            } else {
               
                try (FileWriter writer = new FileWriter(file, true)) {
                    writer.write(",\n" + jsonEntry);
                    System.out.println("Message successfully stored in messages.json");
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }
}
