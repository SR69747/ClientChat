package networking;

import gui.Chat;
import gui.Login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.regex.Pattern;

public final class Receiver implements Runnable {

    private static final Pattern TEXT_SEPARATOR = Pattern.compile("\\u0003");
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
                    case Protocol.SERVER_DECLINE_CONNECTION:
                        Login.invalidLoginMessage();
                        break;
                    case Protocol.SERVER_ACCEPT_CONNECTION:
                        Login.disableLoginFrame();
                        new Thread(new Chat()).start();
                        getUsersOnlineAndMissedMessages();
                        break;
                }
            }

            while ((messageFromServer = in.readLine()) != null && !messageFromServer.equals(Protocol.SERVER_DECLINE_CONNECTION)) {
                if (checkServerSpecialMessages()) {
                    System.out.println(messageFromServer);
                    Chat.displayMessageInHTML(messageFromServer, "black", true);
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            Chat.disposeChattingGui();
            Login.disposeLoginFrame();
            closeReceiverResources();
            Sender.closeSenderResources();
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

        if (messageFromServer.contains(Protocol.SERVER_IMAGE_STRING)) {
            convertStringToImage();
            showMessageInGui = false;
        }

        switch (messageFromServer) {
            case Protocol.SERVER_ACKNOWLEDGE_DATABASE_UPDATE:  //When our server notifies us about online update, we request users online.
                Sender.sendMessageToServer(Protocol.REQUEST_USERS_ONLINE);
                showMessageInGui = false;
                break;
            case Protocol.SERVER_STREAM_USERS_ONLINE:
                populateOnlineUserTable();
                showMessageInGui = false;
                break;
            case Protocol.SERVER_STREAM_MISSED_MESSAGES:
                printOutMissedMessagesStream();
                showMessageInGui = false;
                break;
        }
        return showMessageInGui;
    }

    /**
     * This method decodes Base64 encoded image.
     * Note that this method is triggered when messageFromServer contains IMAGE_STRING.
     */
    private static void convertStringToImage() throws IOException {
        String stringImage = TEXT_SEPARATOR.split(messageFromServer)[1];
        String clientName = TEXT_SEPARATOR.split(messageFromServer)[2];
        Chat.displayPictureInHTML(stringImage, clientName);
    }

    /**
     * This method requests all users online and missed messages with a delay.
     * This is done because our receiver block is not running when we send requests,
     * so we need to send our requests a bit later.
     */
    private static void getUsersOnlineAndMissedMessages() {
        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Sender.sendMessageToServer(Protocol.REQUEST_USERS_ONLINE);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Sender.sendMessageToServer(Protocol.REQUEST_MISSED_MESSAGES);
        }).start();
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
            while (!(messageFromServer = in.readLine()).equals(Protocol.SERVER_END_OF_STREAM) && !messageFromServer.equals(Protocol.SERVER_DECLINE_CONNECTION)) {
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
     * This method prints out all missed messages our server sends us.
     *
     * @throws IOException - possible exception
     */
    private static void printOutMissedMessagesStream() throws IOException {
        while (!(messageFromServer = in.readLine()).equals(Protocol.SERVER_END_OF_STREAM) && !messageFromServer.equals(Protocol.SERVER_DECLINE_CONNECTION)) {
            if (messageFromServer.contains(Protocol.SERVER_IMAGE_STRING)) {
                convertStringToImage();
            } else {
                Chat.displayMessageInHTML(messageFromServer, "green", false);
            }
        }
    }

    /**
     * This method closes BufferedReader.
     * Note that server socket is not closed in ClientChat program
     * as it is handled by server itself when we send it SERVER_DECLINE_CONNECTION message.
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