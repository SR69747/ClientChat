package Networking;

import GUI.Chat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Sender implements Runnable {

    private static BufferedWriter out;
    private Socket socket;
    private String loginDetails;

    public Sender(Socket socket, String loginDetails) {
        this.socket = socket;
        this.loginDetails = loginDetails;
    }

    public void run() {
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //Takes login details and sends them to server for checks
            out.write(loginDetails + '\n');
            out.flush();

            new Thread(new Receiver(socket)).start();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            closeSenderResources();
        }
    }

    public static void sendMessageToServer(String messageToServer) {
        if (messageToServer != null && !messageToServer.trim().isEmpty()) {
            try {
                out.write(messageToServer + '\n');
                out.flush();
            } catch (IOException err) {
                System.out.println("Error: " + err.getMessage());
                closeSenderResources();
            }
        }
    }

    public static void sendMessageToServer() {
        String messageToServer;
        if ((messageToServer = Chat.getMessageSendTextField().getText()) != null && !messageToServer.trim().isEmpty()) {
            try {
                out.write(messageToServer + '\n');
                out.flush();
                Chat.getMessageSendTextField().setText("");
                Chat.getMessageDisplayPane().setText(Chat.getMessageDisplayPane().getText() + '\n' + "You : " + messageToServer);
            } catch (IOException err) {
                System.out.println("Error: " + err.getMessage());
                closeSenderResources();
            }
        }
    }

    static void closeSenderResources() {
        try {
            out.close();
            System.out.println("Sender resources closed");
        } catch (IOException e) {
            System.out.println("Failed to close sender resources: \n" + e.getMessage());
        }
    }
}
