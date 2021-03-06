package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientCardDialogView extends JDialog {
    DefaultTableModel patientModel;
    JTable table;

    public PatientCardDialogView() {
        super((Frame) null, "domain.Patient card", true);
        //Creating JTable
        String col[] = {"Data", "Value"};
        patientModel = new DefaultTableModel(col, 0) {
            //Turning off editable possibility for JTable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(patientModel);

        //Clear JTable
        patientModel.setRowCount(0);

        //Add JTable to JDialog
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(null);
        getContentPane().add(scrollTable, BorderLayout.CENTER);
    }

    public void addRowToTable(String[] row) {
        patientModel.addRow(row);
    }

    public void setRowHeight(int row, int height) {
        table.setRowHeight(row, height);
    }

}
