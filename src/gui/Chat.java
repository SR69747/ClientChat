package gui;

import listeners.SelectedUserTableCellListener;
import listeners.SendButtonListener;
import listeners.TextFieldKeyListener;
import networking.Sender;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Chat extends JPanel implements Runnable {

    private static JFrame mainFrame = new JFrame();
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;

    //Components for Chat gui
    private static JButton sendButton = new JButton("Send");
    private static JPanel sendPanel = new JPanel();

    private String[] UserColumnNames = {"Username", "Status"};
    private static JTable onlineUserTable = new JTable();
    private static DefaultTableModel defaultTableModel = new DefaultTableModel();

    private static JTextPane messageDisplayPane = new JTextPane();
    private static JTextField messageSendTextField = new JTextField();
    private static ImageIcon onlineIcon = new ImageIcon("C:\\lgim\\code\\java\\onlineIcon2.jpg");
    private static ImageIcon offlineIcon = new ImageIcon("C:\\lgim\\code\\java\\offlineIcon2.jpg");

    public void run() {

        //Sam, refactor this
        SwingUtilities.invokeLater(() -> {
            Chat.this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            mainFrame.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            mainFrame.setAlwaysOnTop(false);
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setContentPane(Chat.this);
            mainFrame.setLayout(new BorderLayout(8, 8));
            mainFrame.setVisible(true);
            mainFrame.setTitle("Chatting - " + Login.getUsername());
            messageDisplayPane.setEditable(false);
            messageDisplayPane.setAutoscrolls(true);
            JScrollPane userScrollPane = new JScrollPane(onlineUserTable);
            userScrollPane.setPreferredSize(new Dimension(135, 500));
            onlineUserTable.setModel(defaultTableModel);
            onlineUserTable.setAutoCreateRowSorter(true);
            onlineUserTable.setRowHeight(25);
            defaultTableModel.setColumnIdentifiers(UserColumnNames);
            messageSendTextField.addKeyListener(new TextFieldKeyListener());
            sendButton.setPreferredSize(new Dimension(135, 25));
            sendPanel.setLayout(new BorderLayout(8, 8));
            sendPanel.add(messageSendTextField, BorderLayout.CENTER);
            sendPanel.add(sendButton, BorderLayout.EAST);
            mainFrame.add(sendPanel, BorderLayout.SOUTH);
            mainFrame.add(new JScrollPane(messageDisplayPane), BorderLayout.CENTER);
            mainFrame.add(userScrollPane, BorderLayout.EAST);
            messageDisplayPane.setText("\n    Welcome to chat\n    _____________\n");
            onlineUserTable.getColumnModel().getColumn(1).setCellRenderer(new UserTableIconRenderer());
            onlineUserTable.getSelectionModel().addListSelectionListener(new SelectedUserTableCellListener());
            sendButton.addActionListener(new SendButtonListener());

            //Getting list of users online
            Sender.sendMessageToServer("/online");
        });
    }

    //Improve this !
    public static void closeChattingGui() {
        mainFrame.setVisible(false);
    }

    //Methods which are used in Receiver class
    public static String getTableModelValue(int row) {
        String data = "No Data";
        if (defaultTableModel.getRowCount() != 0) {
            data = (String) defaultTableModel.getValueAt(row, 0);
        }
        return data;
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

    public static JTextPane getMessageDisplayPane() {
        return messageDisplayPane;
    }

    public static JTextField getMessageSendTextField() {
        return messageSendTextField;
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