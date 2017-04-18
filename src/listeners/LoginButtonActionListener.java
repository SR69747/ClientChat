package listeners;

import gui.Login;
import networking.Protocol;
import networking.Sender;
import org.jasypt.util.text.StrongTextEncryptor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

public final class LoginButtonActionListener extends AbstractAction {
    private JTextField usernameField;
    private JPasswordField passwordField;
    //TODO Make SECRET generatable and synchronize it with a server.
    private static final transient String SECRET = "LKJoi8d9ausoiJOIOIDU8ouod88ujdsou8rpoe;mg.,srlkjs98";

    /**
     * Using constructor in order to encapsulate our loginFrame usernameField and passwordField.
     * No need to use getters for password fields.
     *
     * @param textField     - String username.
     * @param passwordField - String password.
     */
    public LoginButtonActionListener(JTextField textField, JPasswordField passwordField) {
        this.usernameField = textField;
        this.passwordField = passwordField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
            String passString = new String(this.passwordField.getPassword());
            String loginDetails = String.format("%s,%s", this.usernameField.getText(), passString);
            StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
            textEncryptor.setPassword(SECRET);
            String myEncryptedDetails = textEncryptor.encrypt(loginDetails);

            try {
                Socket socket = new Socket(Protocol.HOST, Protocol.PORT);
                System.out.println("Connected to: " + Protocol.HOST);

                //Starting a new ClientReceiver Thread
                new Thread(new Sender(socket, myEncryptedDetails)).start();

            } catch (IOException e2) {
                System.out.println("Error launching a client: " + e2.getMessage());
            }
        } else {
            Login.invalidLoginMessage();
        }
    }
}
