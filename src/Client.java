import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class Client extends JPanel implements Runnable, KeyListener {

    private Socket socket;
    private String loginDetails;
    ImageIcon pictureImage;


    Client(Socket socket, String loginDetails) {
        this.socket = socket;
        this.loginDetails = loginDetails;
    }

    //This is main chatting GUI
    private static JFrame mainFrame = new JFrame();
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;

    //Online Users table
    private static JTable onlineUserTable = new JTable();
    private static DefaultTableModel defaultTableModel = new DefaultTableModel();
    private String[] UserColumnNames = {"Username", "Status"};

    //Receiver
    private BufferedReader in;
    private String messageFromServer;
    private static JTextPane textPane = new JTextPane();

    //Special strings, which server can send us
    //They are not shown in a console
    private static final String ACCEPT_CONNECTION = "AC:+";
    private static final String DECLINE_CONNECTION = "EX:0";
    private static final String START_OF_STREAM = "ST:R";
    private static final String END_OF_STREAM = "EN:0";
    private static final String UPDATE_USERS = "UP:A";

    //Sender
    private static BufferedWriter out;
    private static JTextField messageTextField = new JTextField();
    private static JButton sendButton = new JButton("Send");
    private static JPanel sendPanel = new JPanel();

    public void run() {
        try {
            //Sends and receives data
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Takes login details and sends them to server for checks
            out.write(loginDetails + '\n');
            out.flush();

            //This block checks server's response to our login.
            //If our login is valid, then draw GUI
            if ((messageFromServer = in.readLine()) != null) {
                switch (messageFromServer) {
                    case DECLINE_CONNECTION:
                        Main.invalidLoginMessage();
                        break;
                    case ACCEPT_CONNECTION:
                        //Sam closes login GUI here
                        Main.disableLoginFrame();
                        //Launching GUI
                        //Giving properties to our frame
                        launchChattingGui();
                        break;
                }
            }

            //Communication block, receives messages
            //It won't start if our connection is already declined
            while ((messageFromServer = in.readLine()) != null && !messageFromServer.equals(DECLINE_CONNECTION)) {
                //If incoming message is not special, then display it
                if (checkServerSpecialMessages()) {
                    System.out.println(messageFromServer);
                    textPane.setText(textPane.getText() + '\n' +
                            '[' + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + "] " + messageFromServer);
                }
            }

            //Program ends here
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            closeReceiverResources();
            closeSenderResources();
            //Close chatting GUI
            closeChattingGui();
            System.out.println("Client thread completed");
        }
    }

    private static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        return sb.toString();
    }

    private static void closeSenderResources() {
        try {
            out.close();
            System.out.println("Sender resources closed");
        } catch (IOException e) {
            System.out.println("Failed to close sender resources: \n" + e.getMessage());
        }
    }

    private void closeReceiverResources() {
        try {
            in.close();
            System.out.println("Receiver resources closed");
        } catch (IOException e) {
            System.out.println("Failed to close receiver resources: \n" + e.getMessage());
        }
    }

    private boolean checkServerSpecialMessages() throws IOException {
        boolean messageForConsole = true;
        //Special message behaviour
        switch (messageFromServer) {
            case ACCEPT_CONNECTION:
                System.out.println("Connection Accepted\n");
                messageForConsole = false;
                break;
            case DECLINE_CONNECTION:
                System.out.println("Connection Declined");
                messageForConsole = false;
                break;
            case START_OF_STREAM:
                Object[] objects = new Object[2];

                ImageIcon icon = new ImageIcon("C:\\lgim\\code\\java\\Status.png");
                ImageIcon icon2 = new ImageIcon("C:\\lgim\\code\\java\\Status2.png");

                while (!(messageFromServer = in.readLine()).equals(END_OF_STREAM)) {
                    String username = messageFromServer.split(",")[0];
                    String onlineStatus = messageFromServer.split(",")[1];
                    objects[0] = username;
                    switch (onlineStatus) {
                        case "true":
                            objects[1] = icon;
                            break;
                        case "false":
                            objects[1] = icon2;
                            break;
                    }
                    defaultTableModel.addRow(objects);
                }
                onlineUserTable.repaint();
                messageForConsole = false;
                break;
            case UPDATE_USERS:
                out.write("/online\n");
                out.flush();
                messageForConsole = false;
                // Update users method goes here!
                break;

        }
        if (messageFromServer.contains("IM:G")) {
            messageForConsole = false;
            System.out.println(messageFromServer.split("#")[1].trim().replace("=i", "").getBytes());
            byte[] byttes = Base64.getMimeDecoder().decode(messageFromServer.split("#")[1].trim().replace("=i", "").getBytes());
            pictureImage = new ImageIcon(byttes);
            textPane.insertIcon(pictureImage);

        }
        return messageForConsole;
    }

    static class IconRenderer extends DefaultTableCellRenderer {
        private IconRenderer() {
            super();
        }

        public void setValue(Object value) {
            if (value == null) {
                setText("");
            } else {
                setIcon((ImageIcon) value);
            }
        }
    }

    private void launchChattingGui() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Client.this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                mainFrame.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
                mainFrame.setAlwaysOnTop(false);
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainFrame.setContentPane(Client.this);
                mainFrame.setLayout(new BorderLayout(8, 8));
                mainFrame.setVisible(true);
                mainFrame.setTitle("Chatting - " + Main.getUsername());
                textPane.setEditable(false);
                textPane.setAutoscrolls(true);
                JScrollPane userScrollPane = new JScrollPane(onlineUserTable);
                userScrollPane.setPreferredSize(new Dimension(135, 500));
                onlineUserTable.setModel(defaultTableModel);
                onlineUserTable.setAutoCreateRowSorter(true);
                defaultTableModel.setColumnIdentifiers(UserColumnNames);
                messageTextField.addKeyListener(Client.this);
                sendButton.setPreferredSize(new Dimension(135, 25));
                sendPanel.setLayout(new BorderLayout(8, 8));
                sendPanel.add(messageTextField, BorderLayout.CENTER);
                sendPanel.add(sendButton, BorderLayout.EAST);
                mainFrame.add(sendPanel, BorderLayout.SOUTH);
                mainFrame.add(new JScrollPane(textPane), BorderLayout.CENTER);
                mainFrame.add(userScrollPane, BorderLayout.EAST);
                textPane.setText("\n    Welcome to chat\n    _____________\n");
                onlineUserTable.getColumnModel().getColumn(1).setCellRenderer(new IconRenderer());
                sendButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessageToServer();
                    }
                });
            }
        });

    }

    private static void sendMessageToServer() {
        String messageToServer;
        if ((messageToServer = messageTextField.getText()) != null && !messageToServer.trim().isEmpty()) {
            try {
                out.write(messageToServer + '\n');
                out.flush();
                messageTextField.setText("");
                textPane.setText(textPane.getText() + '\n' + "You : " + messageToServer);
            } catch (IOException err) {
                closeSenderResources();
                System.out.println("Error: " + err.getMessage());
            }
        }
    }

    private static void closeChattingGui() {
        //Improve this !
        mainFrame.setVisible(false);
    }

    private static void populateUserTable() {

    }


    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Typed " + e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Pressed " + e.getKeyCode());
        if (e.getKeyCode() == 10) {
            sendMessageToServer();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Released " + e.getKeyCode());
    }
}

