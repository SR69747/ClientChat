package networking;

import gui.Chat;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

import static networking.Protocol.REQUEST_LOGIN;

public final class Sender implements Runnable {

    private static BufferedWriter out;
    private static Socket socket;
    private static String loginDetails;

    private static String selectedUserName = "";

    public Sender(Socket socket, String loginDetails) {
        Sender.socket = socket;
        Sender.loginDetails = loginDetails;
    }

    /**
     * This block sends messages to server.
     * In order to send a message, sendMessageToServer method should be called.
     * When Sender thread is launched, it launches Receiver thread for receiving servers response and
     * sending loginDetails string to the server for checks.
     * Note that when Sender thread ends, BufferedWriter is not closed, as it is used in sendMessageToServer
     * methods.
     * BufferedWriter is closed when receiver thread is completed or when it received SERVER_DECLINE_CONNECTION message from the server.
     */
    public void run() {
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new Thread(new Receiver(socket)).start();

            //Takes login details and sends them to server for checks
            sendMessageToServer(REQUEST_LOGIN + loginDetails);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            closeSenderResources();
        }
    }

    /**
     * This method sends a message to server without formatting it.
     * This method can be used to send special commands to the server.
     *
     * @param messageToServer your message without formatting.
     */
    public static void sendMessageToServer(String messageToServer) {
        if (messageToServer != null && !messageToServer.trim().isEmpty()) {
            //FIXME When an image is sent to a specific person, this method sends a blank String to our server (to all people).
            try {
                out.write(messageToServer + '\n');
                out.flush();
            } catch (IOException err) {
                System.out.println("Error: " + err.getMessage());
                closeSenderResources();
            }
        }
    }

    /**
     * This method gets a message from TextField in Chat class, formats it and sends to the server.
     * Afterwards, this method clears current textField and sets new text in MessageDisplayPane.
     * Note that some message checks are done.
     */
    public static void sendMessageToServer() {
        String messageToServer;
        if ((messageToServer = Chat.getUserInputText()) != null && !messageToServer.trim().isEmpty() && !messageToServer.contains(Protocol.SERVER_STREAM_MISSED_MESSAGES)) {
            try {
                if (!selectedUserName.isEmpty()) {
                    out.write(String.format("%s%s\u0003%s\n", Protocol.SEND_MESSAGE_TO, selectedUserName.trim(), messageToServer));
                    out.flush();
                } else {
                    out.write(messageToServer + '\n');
                    out.flush();
                }
                Chat.emptyUserInputTextField();
                Chat.displayMessageInHTML("You : " + messageToServer, "gray", true);
            } catch (IOException err) {
                System.out.println("Error: " + err.getMessage());
                closeSenderResources();
            }
        }
    }

    /**
     * This method send an image to server.
     *
     * @param file - our file
     */
    public static void sendImageToServer(File file) {
        if (!selectedUserName.isEmpty()) {
            Sender.sendMessageToServer(String.format("%s%s\u0003%s\n", Protocol.SEND_MESSAGE_TO, Sender.selectedUserName, Protocol.SERVER_IMAGE_STRING + encodeFileToBase64Binary(file)));
        } else {
            Sender.sendMessageToServer(Protocol.SERVER_IMAGE_STRING + encodeFileToBase64Binary(file));
        }
    }

    /**
     * This method encodes any file given to String.
     *
     * @param file - our File object.
     * @return Base64 encoded String representation of our file
     */
    private static String encodeFileToBase64Binary(File file) {
        String encodedFile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedFile = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedFile;
    }

    /**
     * This method is used in SelectedUserTableCellListener class.
     * Every time our user selects or deselects a client row in the table, it will rewrite selectedUserName
     * to a new one.
     *
     * @param selectedUserName a username selected in UserTable in Chat class.
     */
    public static void setSelectedUserName(String selectedUserName) {
        Sender.selectedUserName = selectedUserName;
    }

    /**
     * This method closes BufferedWriter.
     * Note that server socket is not closed in ClientChat program
     * as it is handled by server itself when we send it SERVER_DECLINE_CONNECTION message.
     */
    static void closeSenderResources() {
        try {
            out.close();
            System.out.println("Sender resources closed");
        } catch (IOException e) {
            System.out.println("Failed to close sender resources: \n" + e.getMessage());
        }
    }

}
