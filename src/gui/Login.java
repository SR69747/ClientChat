package gui;

import networking.Sender;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

public class Login {

    private static JFrame loginFrame = new JFrame("Log in");
    private static JTextField usernameField = new JTextField();
    private static JPasswordField passwordField = new JPasswordField();
    private static JLabel usernameLabel = new JLabel("Username: ");
    private static JLabel passwordLabel = new JLabel("Password: ");
    private static JButton enterButton = new JButton("Enter");
    private static JTextArea loginMessage = new JTextArea();

    public static void launchLoginGui() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error: Failed to set look and feel\n" + e.getMessage());
        }

        //Sam, refactor this
        loginFrame.setBounds(300, 300, 300, 300);
        loginFrame.setResizable(false);
        loginFrame.setLayout(null);
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
        loginFrame.setAlwaysOnTop(true);

        usernameField.setBounds(130, 100, 100, 20);
        passwordField.setBounds(130, 150, 100, 20);
        usernameLabel.setBounds(60, 100, 100, 20);
        passwordLabel.setBounds(60, 150, 100, 20);
        enterButton.setBounds(115, 220, 80, 20);
        loginMessage.setBounds(90, 50, 200, 30);
        loginMessage.setBackground(null);
        loginMessage.setEditable(false);

        loginFrame.add(usernameField);
        loginFrame.add(passwordField);
        loginFrame.add(usernameLabel);
        loginFrame.add(passwordLabel);
        loginFrame.add(enterButton);
        loginFrame.add(loginMessage);

        enterButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String passString = new String(passwordField.getPassword());
                String loginDetails = String.format("%s,%s", usernameField.getText(), passString);

                final int port = 8000;
                final String host = "10.201.194.154";
                try {
                    Socket socket = new Socket(host, port);
                    System.out.println("Connected to: " + host);

                    //Starting a new ClientReceiver Thread
                    new Thread(new Sender(socket, loginDetails)).start();

                } catch (IOException e2) {
                    System.out.println("Error launching a client: " + e2.getMessage());
                }
            }
        });
    }

    //Work on this !
    public static void disableLoginFrame() {
        loginFrame.setVisible(false);
//       loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));

    }

    public static void invalidLoginMessage() {
        loginMessage.setText("Invalid Login, try again");
        usernameField.setText("");
        passwordField.setText("");
    }

    static String getUsername() {
        return usernameField.getText();
    }

}
