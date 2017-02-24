package networking;

import gui.Chat;
import gui.Login;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class Receiver implements Runnable {

    //Special strings, which server can send us
    //They are not shown in a console
    private static final String ACCEPT_CONNECTION = "AC:+";
    private static final String DECLINE_CONNECTION = "EX:0";
    private static final String START_OF_CONNECTED_USERS_STREAM = "ST:R";
    private static final String END_OF_STREAM = "EN:0";
    private static final String UPDATE_USERS = "UP:A";
    private static final String IMAGE_STRING = "IM:G";

    private Socket socket;
    private static BufferedReader in;
    private static String messageFromServer;

    Receiver(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //This block checks server's response to our login.
            //If our login is valid, then draw Chat GUI
            if ((messageFromServer = in.readLine()) != null) {
                switch (messageFromServer) {
                    case DECLINE_CONNECTION:
                        Login.invalidLoginMessage();
                        break;
                    case ACCEPT_CONNECTION:
                        Login.disableLoginFrame();
                        //Start gui
                        new Thread(new Chat()).start();
                        break;
                }
            }

            //Communication block, receives messages
            while ((messageFromServer = in.readLine()) != null && !messageFromServer.equals(DECLINE_CONNECTION)) {
                //If incoming message is not special, then display it
                if (checkServerSpecialMessages()) {
                    System.out.println(messageFromServer);
                    Chat.getMessageDisplayPane().setText(Chat.getMessageDisplayPane().getText() + '\n' +
                            '[' + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + "] " + messageFromServer);
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // End of the whole program
            closeReceiverResources();
            Sender.closeSenderResources();
            Chat.closeChattingGui();
            //Implement socket.close()
        }
    }

    private static boolean checkServerSpecialMessages() throws IOException {
        boolean showMessageInGui = true;

        if (messageFromServer.contains(IMAGE_STRING)) {
            convertStringToImage();
            showMessageInGui = false;
        }

        switch (messageFromServer) {
            case ACCEPT_CONNECTION:
                System.out.println("Connection Accepted\n");
                showMessageInGui = false;
                break;
            case DECLINE_CONNECTION:
                System.out.println("Connection Declined");
                showMessageInGui = false;
                break;
            case UPDATE_USERS:
                Sender.sendMessageToServer("/online");
                showMessageInGui = false;
                break;
            case START_OF_CONNECTED_USERS_STREAM:
                populateOnlineUserTable();
                showMessageInGui = false;
                break;
        }
        return showMessageInGui;
    }

    private static void convertStringToImage() {
        String stringImage = messageFromServer.split("#")[1];
        byte[] bytes = Base64.getMimeDecoder().decode(stringImage.getBytes());
        ImageIcon pictureImage = new ImageIcon(bytes);
    }

    private static void populateOnlineUserTable() {
        Object[] objects = new Object[2];
        int counter = 0;
        try {
            while (!(messageFromServer = in.readLine()).equals(END_OF_STREAM)) {
                String username = messageFromServer.split(",")[0];
                String onlineStatus = messageFromServer.split(",")[1];
                objects[0] = username;

                switch (onlineStatus) {
                    case "true":
                        objects[1] = Chat.getOnlineIcon();
                        break;
                    case "false":
                        objects[1] = Chat.getOfflineIcon();
                        break;
                }
                if (Chat.getTableModelRowCount() == counter) {
                    Chat.addRowToTableModel(objects);
                }
                if (Chat.getTableModelRowCount() != 0 && Chat.getTableModelValue(counter).equals(username)) {
                    Chat.setTableStatusIcon(objects[1], counter);
                }
                ++counter;
            }
            //Need to work on this. When a new client registers, table updates in a wrong way.
            //Table should be populated again
            Chat.repaintOnlineUserTableRows();

        } catch (IOException err) {
            System.out.println("Error: Failed to populate online users table\n" + err.getMessage());
        }
    }

    private static void closeReceiverResources() {
        try {
            in.close();
            System.out.println("Receiver resources closed");
        } catch (IOException e) {
            System.out.println("Failed to close receiver resources: \n" + e.getMessage());
        }
    }
}