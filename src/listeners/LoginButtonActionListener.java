package listeners;

import networking.Protocol;
import networking.Sender;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

public final class LoginButtonActionListener extends AbstractAction {
    private JTextField usernameField;
    private JPasswordField passwordField;

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
        String passString = new String(this.passwordField.getPassword());
        String loginDetails = String.format("%s,%s", this.usernameField.getText(), passString);

        try {
            Socket socket = new Socket(Protocol.HOST, Protocol.PORT);
            System.out.println("Connected to: " + Protocol.HOST);

            //Starting a new ClientReceiver Thread
            new Thread(new Sender(socket, loginDetails)).start();

        } catch (IOException e2) {
            System.out.println("Error launching a client: " + e2.getMessage());
        }
    }
}
