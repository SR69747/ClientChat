package listeners;

import networking.Sender;
import org.jasypt.util.text.StrongTextEncryptor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * This class is a listener for our registration button.
 */
public final class RegistrationButtonListener extends AbstractAction {
    private static final String REQUEST_REGISTRATION = "\u0001\u0003";
    private static final transient String REGISTRATION_SECRET = "MIICXAIBAAKBgH/YVs/NDEnVyVPKOgo0ShTjsg6uCaCYJL7tNcTR8EEMZmUHYy7B";

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
                Sender.sendMessageToServer(REQUEST_REGISTRATION + myEncryptedDetails);
            } catch (UnsupportedEncodingException err) {
                System.out.println("Error: Failed to generate a secure key!\n" + err.getMessage());
            }
        } else {
            //TODO Implement
        }
    }

    /**
     * @return boolean - validDetails
     */
    private static boolean checkDetails() {
        //TODO Implement
        return false;
    }

    /**
     * @return String - username
     */
    private static String getUsername() {
        //TODO Implement
        return null;
    }

    /**
     * @return String - password
     */
    private static String getPassword() {
        //TODO Implement
        return null;
    }

    /**
     * This method produces a random String
     * by populating byte[] with random values.
     *
     * @return String - generated key
     * @throws UnsupportedEncodingException - exception wee need to catch
     */
    private static String generateKey() throws UnsupportedEncodingException {
        byte[] b = new byte[254];
        new Random().nextBytes(b);
        return new String(b, "UTF-8");
    }

}
