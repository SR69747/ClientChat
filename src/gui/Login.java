package gui;

import networking.Sender;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

public class Login {

    // Main frame initialisation
    private static JFrame loginFrame = new JFrame("Log in");
    private static JPanel loginPanel = new JPanel();

    // Component initialisation
    private static JTextField usernameField = new JTextField(15);
    private static JPasswordField passwordField = new JPasswordField(15);
    private static JLabel usernameLabel = new JLabel("Username: ");
    private static JLabel passwordLabel = new JLabel("Password: ");
    private static JButton enterButton = new JButton("Enter");
    private static JTextArea loginMessage = new JTextArea();
    private static Font messageFont = new Font("Calibri", Font.PLAIN, 18);

    // Panel initialisation
    private static JPanel messagePanel = new JPanel();
    private static JPanel usernamePanel = new JPanel();
    private static JPanel passwordPanel = new JPanel();
    private static JPanel buttonPanel = new JPanel();

    public static void launchLoginGui() {

        loginFrame.setBounds(300, 300, 300, 300);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
        loginFrame.setAlwaysOnTop(true);
        loginFrame.add(loginPanel, BorderLayout.CENTER);

        loginMessage.setBackground(null);
        loginMessage.setEditable(false);
        loginMessage.setFont(messageFont);

        // Components are split into panels here
        messagePanel.add(loginMessage);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        buttonPanel.add(enterButton);

        // Panels are then added to main frame here
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.add(messagePanel);
        loginPanel.add(usernamePanel);
        loginPanel.add(passwordPanel);
        loginPanel.add(buttonPanel);



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

    public static void enableLoginFrame(){
        loginFrame.setVisible(true);
        usernameField.setText("");
        passwordField.setText("");
    }

    public static void invalidLoginMessage() {
        loginMessage.setText("\nInvalid login, try again");
        usernameField.setText("");
        passwordField.setText("");
    }

    static String getUsername() {
        return usernameField.getText();
    }
}