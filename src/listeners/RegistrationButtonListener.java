package listeners;

import gui.Login;
import networking.Protocol;
import org.jasypt.util.text.StrongTextEncryptor;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;


/**
 * This class is a listener for our registration button.
 */
public final class RegistrationButtonListener extends AbstractAction {
    private static final String REQUEST_REGISTRATION = "\u0001\u0003";
    private static final transient String REGISTRATION_SECRET = "MIICXAIBAAKBgH/YVs/NDEnVyVPKOgo0ShTjsg6uCaCYJL7tNcTR8EEMZmUHYy7B";
    private static final String KEY_CHARACTERS = "0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
    private static final int KEY_LENGTH = 40;

    /**
     * This method collects registration details of a user,
     * encrypts them and sends them to a server.
     *
     * @param e - ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (checkDetails()) {
            try {
                String details = String.format("%s,%s,%s", getUsername(), getPassword(), generateKey());
                StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
                textEncryptor.setPassword(REGISTRATION_SECRET);
                String myEncryptedDetails = textEncryptor.encrypt(details);
                try {
                    Socket socket = new Socket(Protocol.HOST, Protocol.PORT);
                    System.out.println("Connected to: " + Protocol.HOST + "\nRegistration attempt");
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.write(REQUEST_REGISTRATION + myEncryptedDetails);
                    out.newLine();
                    out.flush();
                    new Thread(() -> {
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String messageFromServer;
                            while ((messageFromServer = in.readLine()) != null) {
                                System.out.println(messageFromServer);
//                                if (messageFromServer = INSERT SERVER MESSAGE HERE){
//                                    Login.setRegisterCompleteMessage();
//                                } else {
//                                    Login.setFailedRegistrationMessage();
//                                }

                                //TODO React to the server message
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }).start();

                } catch (IOException e2) {
                    System.out.println("Error launching a client: " + e2.getMessage());
                }
            } catch (UnsupportedEncodingException err) {
                System.out.println("Error: Failed to generate a secure key!\n" + err.getMessage());
            }
        } else {
            Login.setRegisterPasswordMatchErrorMessage();
        }
    }

    /**
     * @return boolean - validDetails
     */
    private static boolean checkDetails() {
        if (Login.getRegistrationPassword().equals(Login.getRegistrationRetypePassword())) {
            return true;
        }
        Login.setRegisterPasswordMatchErrorMessage();
        return false;

    }


    /**
     * @return String - username
     */
    private static String getUsername() {
        return Login.getRegistrationUsername();
    }

    /**
     * @return String - password
     */
    private static String getPassword() {
        return Login.getRegistrationPassword();
    }

    /**
     * This method produces a random String
     * by selecting random characters from key characters string and
     * appending using string builder.
     *
     * @return String - generated key
     * @throws UnsupportedEncodingException - exception wee need to catch
     */
    private static String generateKey() throws UnsupportedEncodingException {

        StringBuilder builder = new StringBuilder(KEY_LENGTH);

        for (int i = 0; i < KEY_LENGTH; i++) {
            int random = (int) (Math.random() * KEY_LENGTH);
            builder.append(KEY_CHARACTERS.charAt(random));
            System.out.println(random);
        }

        System.out.println(builder.toString());
        return builder.toString();
    }


}
