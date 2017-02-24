package listeners;

import gui.Chat;
import networking.Sender;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SelectedUserTableCellListener implements ListSelectionListener {

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow;
        String selectedUsername;
        if ((selectedRow = Chat.getOnlineUserTable().getSelectedRow()) > -1) {
            selectedUsername = Chat.getOnlineUserTable().getValueAt(selectedRow, 0).toString();
            Sender.setSelectedUserName(selectedUsername);
        }
        if (selectedRow == -1) {
            Sender.setSelectedUserName("");
        }
    }
}
