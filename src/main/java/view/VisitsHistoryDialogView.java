package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This is the class, which represents Visit history dialog when using "Show visit history" button on view.PanelPatientsListView.
 */

public class VisitsHistoryDialogView extends JDialog {
    DefaultTableModel visitsModel;
    JTable table;
    JButton editButton;
    JButton removeButton;
    JButton OkButton;

    public VisitsHistoryDialogView() {
        super((Frame) null, "Visits history", true);
        //Creating JTable
        String col[] = {"Date", "Medical treatments"};
        visitsModel = new DefaultTableModel(col, 0) {
            //Turning off editable possibility for JTable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(visitsModel);
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(null);

        //Clear JTable
        visitsModel.setRowCount(0);

        //Buttons
        editButton = new JButton("  Edit  ");
        removeButton = new JButton("Remove");

        //Button JPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        //Creating JPanel and add items to it
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollTable, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        //Add panel to JDialog
        add(panel);
    }

    //AddActionListener methods
    public void addActionListenerToEditButton(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    public void addActionListenerToRemoveButton(ActionListener listener) {
        removeButton.addActionListener(listener);
    }

    public void addRowToTable(String[] row) {
        visitsModel.addRow(row);
    }

    public JTable getTable() {
        return table;
    }

    public void setRowHeight(int row, int height) {
        table.setRowHeight(row, height);
    }
}	
