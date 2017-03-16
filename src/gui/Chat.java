package gui;

import listeners.*;
import networking.Receiver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Chat extends JPanel implements Runnable {

    private static JFrame mainFrame = new JFrame();
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;

    //Initialise Components for Chat gui
    private static JButton sendButton = new JButton("Send");
    private static JPanel sendPanel = new JPanel();
    private static JTextPane messageDisplayPane = new JTextPane();
    private static JTextField messageSendTextField = new JTextField();
    private static String[] UserColumnNames = {"Username", "Status"};
    private static JTable onlineUserTable = new JTable();
    private static JScrollPane userScrollPane = new JScrollPane(onlineUserTable);
    private static DefaultTableModel defaultTableModel = new DefaultTableModel();
    private static JMenuBar menuBar = new JMenuBar();
    private static JMenu settingsMenu = new JMenu("Settings");
    private static JMenuItem signOut = new JMenuItem("Sign Out");
    private static JMenuItem changePassword = new JMenuItem("Change password");
    private static JMenuItem changeTheme = new JMenuItem("Change theme");

    private static HTMLDocument doc;
    private static HTMLEditorKit editorKit;

    private static ImageIcon onlineIcon = new ImageIcon("C:\\lgim\\code\\java\\onlineIcon2.jpg");
    private static ImageIcon offlineIcon = new ImageIcon("C:\\lgim\\code\\java\\offlineIcon2.jpg");

    public void run() {

        SwingUtilities.invokeLater(() -> {

            // set mainframe properties
            Chat.this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            mainFrame.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            mainFrame.setAlwaysOnTop(false);
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setContentPane(Chat.this);
            mainFrame.setLayout(new BorderLayout(8, 8));
            mainFrame.setVisible(true);
            mainFrame.setTitle("Chatting - " + Login.getUsername());

            // set message display properties
            messageDisplayPane.setEditable(false);
            messageDisplayPane.setAutoscrolls(true);

            //FIXME Trying to use html on textPane.
            messageDisplayPane.setContentType("text/html");
            doc = (HTMLDocument) messageDisplayPane.getDocument();
            editorKit = (HTMLEditorKit) messageDisplayPane.getEditorKit();
            displayMessageInHTML("Welcome to chatting GUI", "blue", false);

            messageSendTextField.addKeyListener(new TextFieldKeyListener());
            messageSendTextField.setDropTarget(new DropTarget(messageSendTextField, 0, new DropListener(), true));

            sendButton.setPreferredSize(new Dimension(135, 25));
            sendButton.addActionListener(new SendButtonListener());

            // Menus
            settingsMenu.setMnemonic(KeyEvent.VK_F);
            settingsMenu.add(signOut);
            settingsMenu.add(changePassword);
            settingsMenu.add(changeTheme);
            signOut.setMnemonic(KeyEvent.VK_F);
            signOut.addActionListener(new SettingsSignOutListener());
            menuBar.add(settingsMenu);

            // set user table properties
            //TODO Set onlineUserTable cells unmodifiable.
            userScrollPane.setPreferredSize(new Dimension(135, 500));
            onlineUserTable.setModel(defaultTableModel);
            onlineUserTable.setAutoCreateRowSorter(true);
            onlineUserTable.setRowHeight(25);
            defaultTableModel.setColumnIdentifiers(UserColumnNames);
            onlineUserTable.getColumnModel().getColumn(1).setCellRenderer(new UserTableIconRenderer());
            onlineUserTable.getSelectionModel().addListSelectionListener(new SelectedUserTableCellListener());

            // add components and panel to mainframe
            sendPanel.setLayout(new BorderLayout(8, 8));
            sendPanel.add(messageSendTextField, BorderLayout.CENTER);
            sendPanel.add(sendButton, BorderLayout.EAST);
            mainFrame.setJMenuBar(menuBar);
            mainFrame.add(sendPanel, BorderLayout.SOUTH);
            mainFrame.add(new JScrollPane(messageDisplayPane), BorderLayout.CENTER);
            mainFrame.add(userScrollPane, BorderLayout.EAST);
        });
    }

    public static void closeChattingGui() {
        //TODO Need to actually close chatting gui !
        mainFrame.setVisible(false);
    }

    public static String getTableModelValue(int row) {
        String data = "No Data";
        if (defaultTableModel.getRowCount() != 0) {
            data = (String) defaultTableModel.getValueAt(row, 0);
        }
        return data;
    }

    /**
     * This method displays @param text in our messageDisplayPane.
     *
     * @param text - String message
     */
    public static void displayMessageInHTML(String text, String color, boolean showTimeStamp) {
        try {
            editorKit.insertHTML(doc, doc.getLength(), "<b style=\"color:" + color + "\">" + ((showTimeStamp) ? getCurrentTimeStamp() : "") + text + "</span>", 0, 0, null);
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method displays @param icon in our messageDisplayPane.
     *
     * @param base64 - our Image in base64 String
     */
    public static void displayPictureInHTML(String base64) {
        //FIXME This method is under work.
        try {
            // String e = Base64.getEncoder().encodeToString(base64);
            /*<img src="https://www.gravatar.com/avatar/89ec268d347a422368b2d8cbcc5e059b?s=32&amp;d=identicon&amp;r=PG" alt="" width="32" height="32">*/
            System.out.println(base64);
            editorKit.insertHTML(doc, doc.getLength(), "<img src=\"data:image/png;base64," + base64 + "\" />", 0, 0, null);
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns a current time stamp.
     *
     * @return String in this format [hh:mm:ss].
     */
    private static String getCurrentTimeStamp() {
        return '[' + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + "] ";
    }

    /**
     * This method sets messageSendTextField text to "".
     */
    public static void emptyUserInputTextField() {
        messageSendTextField.setText("");
    }

    /**
     * This method is used in Sender class.
     *
     * @return messageSendTextField text
     */
    public static String getUserInputText() {
        return messageSendTextField.getText();
    }

    public static int getTableModelRowCount() {
        return defaultTableModel.getRowCount();
    }

    public static void addRowToTableModel(Object[] objects) {
        defaultTableModel.addRow(objects);
    }

    public static void setTableStatusIcon(Object object, int rowCount) {
        defaultTableModel.setValueAt(object, rowCount, 1);
    }

    public static void repaintOnlineUserTableRows() {
        onlineUserTable.repaint();
    }

    public static ImageIcon getOnlineIcon() {
        return onlineIcon;
    }

    public static ImageIcon getOfflineIcon() {
        return offlineIcon;
    }

    public static JTable getOnlineUserTable() {
        return onlineUserTable;
    }
}