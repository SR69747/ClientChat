package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public final class UserTableIconRenderer extends DefaultTableCellRenderer {

    UserTableIconRenderer() {
        super();
    }

    public void setValue(Object value) {
        if (value == null) {
            setIcon(Chat.getOfflineIcon());
        } else {
            setIcon((ImageIcon) value);
        }
    }
}
