package networking;

import gui.Chat;
import gui.Login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public final class Receiver implements Runnable {

    private Socket socket;
    private static BufferedReader in;
    private static String messageFromServer;

    Receiver(Socket socket) {
        this.socket = socket;
    }

    /**
     * This block receives server messages and responds to them automatically.
     * At the start of Receiver thread, server's response to our login is checked.
     * If our login is not valid, "Invalid login message" is displayed and message receiving loop is not started.
     * If our login is valid, launch Chat GUI and disable LoginFrame. Note that this thread enters receiving loop afterwards.
     * Receiving loop receives messages and checks whether they are special.
     * If they are, then certain methods are triggered.
     * If not, then messageFromServer is formatted and displayed in chatting GUI.
     */
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if ((messageFromServer = in.readLine()) != null) {
                switch (messageFromServer) {
                    case Protocol.DECLINE_CONNECTION:
                        Login.invalidLoginMessage();
                        break;
                    case Protocol.ACCEPT_CONNECTION:
                        Login.disableLoginFrame();
                        new Thread(new Chat()).start();
                        break;
                }
            }

            while ((messageFromServer = in.readLine()) != null && !messageFromServer.equals(Protocol.DECLINE_CONNECTION)) {
                if (checkServerSpecialMessages()) {
                    System.out.println(messageFromServer);
                    Chat.displayMessageInHTML(messageFromServer, "black", true);
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            closeReceiverResources();
            Sender.closeSenderResources();
            Chat.closeChattingGui();
        }
    }

    /**
     * This block checks server messages.
     * If they are special, then certain methods are triggered.
     *
     * @return boolean showMessageInGui.
     * @throws IOException if message checks catch an exception, then program will be closed.
     */
    private static boolean checkServerSpecialMessages() throws IOException {
        boolean showMessageInGui = true;

        if (messageFromServer.contains(Protocol.IMAGE_STRING)) {
            convertStringToImage();
            showMessageInGui = false;
        }

        switch (messageFromServer) {
            case Protocol.ACCEPT_CONNECTION:
                System.out.println("Connection Accepted\n");
                showMessageInGui = false;
                break;
            case Protocol.DECLINE_CONNECTION:
                System.out.println("Connection Declined");
                showMessageInGui = false;
                break;
            case Protocol.UPDATE_USERS:
                Sender.sendMessageToServer("/online");
                showMessageInGui = false;
                break;
            case Protocol.START_OF_CONNECTED_USERS_STREAM:
                populateOnlineUserTable();
                showMessageInGui = false;
                break;
        }
        return showMessageInGui;
    }

    /**
     * This method decodes Base64 encoded image.
     * Note that this method is triggered when messageFromServer contains IMAGE_STRING.
     */
    private static void convertStringToImage() {
        String stringImage = messageFromServer.split("#")[1];
        if (!stringImage.trim().isEmpty()) {
            //  byte[] bytes = Base64.getMimeDecoder().decode(stringImage.getBytes());
            // ImageIcon pictureImage = new ImageIcon(bytes);
            Chat.displayPictureInHTML(stringImage);
        }
    }

    /**
     * This method populates onlineUserTable in Chat class.
     * The first column is populated with String values.
     * The second column is populated with ImageIcons representing users online status.
     */
    private static void populateOnlineUserTable() {
        Object[] objects = new Object[2];
        int counter = 0;
        try {
            while (!(messageFromServer = in.readLine()).equals(Protocol.END_OF_STREAM)) {
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
            //TODO When a new client registers, table updates in a wrong way. Table should be populated again
            Chat.repaintOnlineUserTableRows();

        } catch (IOException err) {
            System.out.println("Error: Failed to populate online users table\n" + err.getMessage());
        }
    }

    /**
     * This method closes BufferedReader.
     * Note that server socket is not closed in ClientChat program
     * as it is handled by server itself when we send it DECLINE_CONNECTION message.
     */
    private static void closeReceiverResources() {
        try {
            in.close();
            System.out.println("Receiver resources closed");
        } catch (IOException e) {
            System.out.println("Failed to close receiver resources: \n" + e.getMessage());
        }
    }
}