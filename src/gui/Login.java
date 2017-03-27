package gui;

import listeners.LoginButtonActionListener;

import javax.swing.*;
import java.awt.*;

public class Login {

    // Main frame initialisation
    private static JFrame loginFrame = new JFrame("Log in");
    private static JPanel loginPanel = new JPanel();

    // Component initialisation
    private static JTextField usernameField = new JTextField(15);
    private static JPasswordField passwordField = new JPasswordField(15);
    private static JLabel usernameLabel = new JLabel("Username: ");
    private static JLabel passwordLabel = new JLabel("Password: ");
    private static JButton loginButton = new JButton("Enter");
    private static JTextArea loginMessage = new JTextArea();
    private static Font messageFont = new Font("Arial", Font.PLAIN, 18);

    // Panel initialisation
    private static JPanel messagePanel = new JPanel();
    private static JPanel usernamePanel = new JPanel();
    private static JPanel passwordPanel = new JPanel();
    private static JPanel buttonPanel = new JPanel();
    private static Icon loginMenuLogo = new ImageIcon("src/resources/lgim.png");
    private static JLabel imageLabel = new JLabel();

    public static void launchLoginGui() {

        loginFrame.setLayout(new BorderLayout());
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
        loginFrame.setAlwaysOnTop(true);

        loginMessage.setBackground(null);
        loginMessage.setEditable(false);
        loginMessage.setFont(messageFont);

        // Components are split into panels here
        messagePanel.add(loginMessage);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        buttonPanel.add(loginButton);
        imageLabel.setIcon(loginMenuLogo);
        buttonPanel.add(imageLabel);


        // Panels are then added to main frame here
        loginPanel.setBounds(300, 300, 300, 300);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.add(messagePanel);
        loginPanel.add(usernamePanel);
        loginPanel.add(passwordPanel);
        loginPanel.add(buttonPanel);
        messagePanel.setBackground(Color.WHITE);
        usernamePanel.setBackground(Color.WHITE);
        passwordPanel.setBackground(Color.WHITE);
        buttonPanel.setBackground(Color.WHITE);


        loginFrame.add(loginPanel, BorderLayout.CENTER);
        loginFrame.pack();

        loginButton.addActionListener(new LoginButtonActionListener(usernameField, passwordField));
    }

    //Work on this !
    public static void disableLoginFrame() {
        //TODO Need to actually close loginFrame, when we terminate our application.
        loginFrame.setVisible(false);
//       loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void enableLoginFrame() {
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