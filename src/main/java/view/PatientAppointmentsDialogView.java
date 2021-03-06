package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientAppointmentsDialogView extends JDialog {
    DefaultTableModel appointmentModel;
    JTable table;
    JLabel label;

    public PatientAppointmentsDialogView() {
        super((Frame) null, "domain.Patient appointments", true);

        //Creating JTable
        String col[] = {"Visit date", "Doctor"};
        appointmentModel = new DefaultTableModel(col, 0) {
            //Turning off editable possibility for JTable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(appointmentModel);

        //Creating label
        label = new JLabel("domain.Patient name");

        //Clear JTable
        appointmentModel.setRowCount(0);

        //Add JTable to JDialog
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(null);
        getContentPane().add(label, BorderLayout.NORTH);
        getContentPane().add(scrollTable, BorderLayout.CENTER);
    }

    public void addRowToTable(String[] row) {
        appointmentModel.addRow(row);
    }

    public void setPatientName(String name) {
        label.setText(name);
    }
}
