package gui;

import javax.swing.table.DefaultTableModel;

public final class CustomTableModel extends DefaultTableModel {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
