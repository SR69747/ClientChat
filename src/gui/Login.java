package gui;

import listeners.CancelListener;
import listeners.LoginButtonActionListener;
import listeners.RegistrationButtonListener;
import listeners.RegistrationPageButtonListener;

import javax.swing.*;
import java.awt.*;

public final class Login {

    // Main frame initialisation
    private static JFrame loginFrame = new JFrame("Log in");
    private static JPanel loginPanel = new JPanel();
    private static JPanel registrationPanel = new JPanel();

    // Card Layout initialisation
    private static JPanel cardPanel = new JPanel(new CardLayout());
    private static CardLayout cardLayout = (CardLayout) cardPanel.getLayout();


    // Login Component initialisation
    private static JTextField usernameField = new JTextField(10);
    private static JPasswordField passwordField = new JPasswordField(10);
    private static JLabel usernameLabel = new JLabel("Username: ");
    private static JLabel passwordLabel = new JLabel("Password:  ");
    private static JButton loginButton = new JButton("Login");
    private static JButton registrationButton = new JButton("Register");
    private static JTextArea loginMessage = new JTextArea();
    private static Font messageFont = new Font("Calibri", Font.PLAIN, 18);

    // Login Panel initialisation
    private static JPanel messagePanel = new JPanel();
    private static JPanel usernamePanel = new JPanel();
    private static JPanel passwordPanel = new JPanel();
    private static JPanel buttonPanel = new JPanel();
    private static Icon loginMenuLogo = new ImageIcon("src/resources/lgim.png");
    private static JLabel imageLabel = new JLabel();

    // Registration Component initialisation
    private static JLabel registerMessageLabel = new JLabel("");
    private static JLabel registerUsernameLabel = new JLabel("Username:  ");
    private static JLabel registerPasswordLabel = new JLabel("Password:  ");
    private static JLabel retypePasswordLabel = new JLabel("Re-type Password: ");
    private static JPasswordField registerPasswordField = new JPasswordField(10);
    private static JPasswordField registerRetypePasswordField = new JPasswordField(10);
    private static JTextField registrationUsernameField = new JTextField(10);
    private static JButton cancelButton = new JButton("Back");
    private static JButton submitRegisterButton = new JButton("Submit");

    // Registration Panel initialisation
    private static JPanel registrationMessagePanel = new JPanel();
    private static JPanel registrationUsernamePanel = new JPanel();
    private static JPanel registrationPasswordPanel = new JPanel();
    private static JPanel registrationButtonPanel = new JPanel();
    private static JPanel registrationRetypePanel = new JPanel();


    public static void launchLoginGui() {

        loginFrame.setLayout(new BorderLayout());
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
        loginFrame.setAlwaysOnTop(true);

        loginMessage.setBackground(null);
        loginMessage.setEditable(false);
        loginMessage.setFont(messageFont);


        // Registration Components split into panels here
        registrationMessagePanel.setBackground(Color.WHITE);
        registrationMessagePanel.add(registerMessageLabel);
        registrationUsernamePanel.add(Box.createHorizontalStrut(35));
        registrationUsernamePanel.setBackground(Color.WHITE);
        registrationUsernamePanel.add(registerUsernameLabel);
        registrationUsernamePanel.add(registrationUsernameField);
        registrationPasswordPanel.add(Box.createHorizontalStrut(37));
        registrationPasswordPanel.setBackground(Color.WHITE);
        registrationPasswordPanel.add(registerPasswordLabel);
        registrationPasswordPanel.add(registerPasswordField);
        registrationRetypePanel.setBackground(Color.WHITE);
        registrationRetypePanel.add(retypePasswordLabel);
        registrationRetypePanel.add(registerRetypePasswordField);
        registrationButtonPanel.setBackground(Color.WHITE);
        registrationButtonPanel.add(cancelButton);
        registrationButtonPanel.add(Box.createHorizontalStrut(25));
        registrationButtonPanel.add(submitRegisterButton);

        // initialise registration panel and add panels to main panel
        registrationPanel.setVisible(true);
        registrationPanel.setLayout(new BoxLayout(registrationPanel, BoxLayout.Y_AXIS));
        registrationPanel.add(registrationMessagePanel);
        registrationPanel.add(registrationUsernamePanel);
        registrationPanel.add(registrationPasswordPanel);
        registrationPanel.add(registrationRetypePanel);
        registrationPanel.add(registrationButtonPanel);


        // Login Components are split into panels here
        messagePanel.add(loginMessage);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        buttonPanel.add(loginButton);
        imageLabel.setIcon(loginMenuLogo);
        buttonPanel.add(imageLabel);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(registrationButton);

        // Login Panels are then added to main login panel here
        loginPanel.setBounds(300, 300, 300, 200);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.add(messagePanel);
        loginPanel.add(usernamePanel);
        loginPanel.add(passwordPanel);
        loginPanel.add(buttonPanel);
        messagePanel.setBackground(Color.WHITE);
        usernamePanel.setBackground(Color.WHITE);
        passwordPanel.setBackground(Color.WHITE);
        buttonPanel.setBackground(Color.WHITE);

        // add login and registration to the card panel
        cardPanel.add(loginPanel);
        cardPanel.add(registrationPanel);

        loginFrame.add(cardPanel, BorderLayout.CENTER);
        loginFrame.setPreferredSize(new Dimension(300, 220));
        loginFrame.pack();

        loginButton.addActionListener(new LoginButtonActionListener(usernameField, passwordField));

        // Change panel to registration panel
        registrationButton.addActionListener(new RegistrationPageButtonListener());

        // Change panel to login panel
        cancelButton.addActionListener(new CancelListener());

        // Submits registration details
        submitRegisterButton.addActionListener(new RegistrationButtonListener());
    }

    //Work on this !
    public static void disableLoginFrame() {
        loginFrame.setVisible(false);
//       loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void disposeLoginFrame() {
        loginFrame.dispose();
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

    public static void openRegistrationPage() {
        resetLoginMessage();
        cardLayout.next(cardPanel);
        loginFrame.setTitle("Registration");
    }

    public static void openLoginPage() {
        cardLayout.next(cardPanel);
        loginFrame.setTitle("Login");
        resetRegistrationPage();

    }

    public static String getRegistrationUsername() {
        return registrationUsernameField.getText();
    }

    public static String getRegistrationPassword() {
        return String.valueOf(registerPasswordField.getPassword());
    }

    public static String getRegistrationRetypePassword() {
        return String.valueOf(registerRetypePasswordField.getPassword());
    }

    public static void setRegisterPasswordMatchErrorMessage() {
        resetRegistrationPage();
        registerMessageLabel.setText("Passwords do not match!");
    }

    public static void setRegisterCompleteMessage() {
        resetRegistrationPage();
        openLoginPage();
        JOptionPane.showMessageDialog(loginFrame, ("Registration Complete!"));
    }

    public static void setFailedRegistrationMessage() {
        resetRegistrationPage();
        JOptionPane.showMessageDialog(loginFrame, ("Registration Failed!"));
    }

    private static void resetRegistrationPage() {
        registerMessageLabel.setText("");
        registrationUsernameField.setText("");
        registerPasswordField.setText("");
        registerRetypePasswordField.setText("");
    }

    private static void resetLoginMessage(){
        loginMessage.setText("");
    }


}