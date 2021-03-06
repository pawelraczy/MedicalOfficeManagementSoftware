package view;

import domain.Employee;
import helper.DateLabelFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;


/**
 * This is a view for view.PanelPatientsScheduleView (main view: "view.MenuAndMainView")
 */

public class PanelPatientsScheduleView extends JPanel {
    private JButton addVisitButton;
    private JButton cancelButton;
    private JButton deleteVisitButton;
    private JButton nextDayButton;
    private JButton previousDayButton;
    private JTextField selectedPatientField;
    private JTextField selectedDateField;
    private JTextField selectedTimeField;
    private JComboBox<String> doctorBox;
    private JDatePickerImpl datePicker;
    private JTable table;
    private JTable headerTable;
    private DefaultTableModel model;
    private ListSelectionModel cellSelectionModel;
    private int idNumber = 0;

    public PanelPatientsScheduleView() {
        //JPanel settings
        setLayout(new BorderLayout());

        //JLabels
        JLabel selectedPatientLabel = new JLabel("Selected patient:");
        selectedPatientLabel.setPreferredSize(new Dimension(300, 10));
        JLabel doctorLabel = new JLabel("Doctor:");
        JLabel selectedDateLabel = new JLabel("Selected date:");
        JLabel selectedTimeLabel = new JLabel("Selected time:");

        //Date select component
        UtilDateModel dateModel = new UtilDateModel();
        Calendar today = Calendar.getInstance();
        dateModel.setDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        dateModel.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        //Not-editable JTextFields
        selectedPatientField = new JTextField("Patient is not selected!");
        selectedPatientField.setEditable(false);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        selectedDateField = new JTextField(formatter.format(today.getTime()));
        selectedDateField.setEditable(false);
        selectedTimeField = new JTextField();
        selectedTimeField.setEditable(false);

        //ComboBox
        doctorBox = new JComboBox<String>();
        doctorBox.setBackground(Color.WHITE);
        doctorBox.setSelectedItem(null);

        //Additional JButtons
        addVisitButton = new JButton("Add visit");
        cancelButton = new JButton("Finish adding appointments");
        deleteVisitButton = new JButton("Delete visit");
        nextDayButton = new JButton("Next day");
        previousDayButton = new JButton("Previous day");

        //Creating main JTable
        String col[] = {"Consulting room 1", "Consulting room 2"};
        model = new DefaultTableModel(col, 16) {
            //Turning off editable possibility for JTable
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        table.setCellSelectionEnabled(true);
        table.setBorder(BorderFactory.createRaisedBevelBorder());
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        for (int i = 0; i < table.getRowCount(); i++)
            for (int j = 0; j < table.getColumnCount(); j++)
                table.setValueAt("", i, j);
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) table.getDefaultRenderer(String.class);
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Creating left table header
        DefaultTableModel headerModel = new DefaultTableModel() {
            @Override
            public int getColumnCount() {
                return 1;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public int getRowCount() {
                return table.getRowCount();
            }

            @Override
            public Class<?> getColumnClass(int colNum) {
                switch (colNum) {
                    case 0:
                        return String.class;
                    default:
                        return super.getColumnClass(colNum);
                }
            }
        };
        headerTable = new JTable(headerModel);
        Calendar timeCount = Calendar.getInstance();
        timeCount.set(2015, 1, 1, 8, 0);
        for (int i = 0; i < table.getRowCount(); i++) {
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
            headerTable.setValueAt(timeFormatter.format(timeCount.getTime()), i, 0);
            timeCount.add(Calendar.MINUTE, 30);

        }
        headerTable.setShowGrid(false);
        headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        headerTable.setPreferredScrollableViewportSize(new Dimension(50, 0));
        headerTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        headerTable.setBorder(BorderFactory.createRaisedBevelBorder());
        headerTable.setRowHeight(35);
        headerTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(Color.LIGHT_GRAY);
                setFont(new Font("Arial", Font.BOLD, 13));

                return this;
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(headerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(600, 800));

        //Adding components to JPanel on the left side
        GridLayout leftPanelLayout = new GridLayout(20, 1);
        leftPanelLayout.setVgap(3);
        JPanel leftPanel = new JPanel(leftPanelLayout);
        leftPanel.setBorder(BorderFactory.createEtchedBorder());
        leftPanel.setPreferredSize(new Dimension(250, 0));
        leftPanel.add(selectedPatientLabel);
        leftPanel.add(selectedPatientField);
        leftPanel.add(doctorLabel);
        leftPanel.add(doctorBox);
        leftPanel.add(new JLabel());
        leftPanel.add(selectedDateLabel);
        leftPanel.add(selectedDateField);
        leftPanel.add(selectedTimeLabel);
        leftPanel.add(selectedTimeField);
        leftPanel.add(new JLabel());
        leftPanel.add(addVisitButton);
        leftPanel.add(deleteVisitButton);
        leftPanel.add(new JLabel());
        leftPanel.add(new JLabel());
        leftPanel.add(new JLabel());
        leftPanel.add(new JLabel());
        leftPanel.add(new JLabel());
        leftPanel.add(new JLabel());
        leftPanel.add(new JLabel());
        leftPanel.add(cancelButton);

        //Adding components to the top of rightPanel
        JPanel topOfRightPanel = new JPanel();
        topOfRightPanel.add(previousDayButton);
        topOfRightPanel.add(datePicker);
        topOfRightPanel.add(nextDayButton);

        //Adding components to JPanel on the left side
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(topOfRightPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.setBorder(BorderFactory.createEtchedBorder());

        //Adding components to main JPanel
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    //Listener methods
    public void addActionListenerToPreviousDayButton(ActionListener listener) {
        previousDayButton.addActionListener(listener);
    }

    public void addActionListenerToNextDayButton(ActionListener listener) {
        nextDayButton.addActionListener(listener);
    }

    public void addActionListenerToAddVisitButton(ActionListener listener) {
        addVisitButton.addActionListener(listener);
    }

    public void addActionListenerToCancelButton(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    public void addActionListenerToDeleteVisitButton(ActionListener listener) {
        deleteVisitButton.addActionListener(listener);
    }

    public void addMouseListener(MouseAdapter listener) {
        this.addMouseListener(listener);
    }

    public void addDateChangeListener(ChangeListener listener) {
        datePicker.getModel().addChangeListener(listener);
    }

    public void addActionListenerToCellSelectionModel(ListSelectionListener listener) {
        cellSelectionModel.addListSelectionListener(listener);
    }

    //Getters
    public String getSelectedPatient() {
        return selectedPatientField.getText();
    }

    public String getSelectedDoctor() {
        return (String) doctorBox.getSelectedItem();
    }

    public String getSelectedDate() {
        return selectedDateField.getText();
    }

    public String getSelectedTime() {
        return selectedTimeField.getText();
    }

    public Calendar getDate() {
        Calendar cal = null;
        if (datePicker.getModel().getValue() != null) {
            cal = Calendar.getInstance();
            cal.setTime((Date) datePicker.getModel().getValue());
        }
        return cal;
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public int getSelectedColumn() {
        return table.getSelectedColumn();
    }

    public String getHeaderTitle(int row) {
        return (String) headerTable.getValueAt(row, 0);
    }

    public String getTableValue(int row, int column) {
        return (String) table.getValueAt(row, column);
    }

    public int getTableRowCount() {
        return table.getRowCount();
    }

    public int getTableColumnCount() {
        return table.getColumnCount();
    }

    public int getIdNumber() {
        return idNumber;
    }

    //Setters
    public void setPatientField(String patient) {
        selectedPatientField.setText(patient);
    }

    public void setDateField(String date) {
        selectedDateField.setText(date);
    }

    public void setTimeField(String time) {
        selectedTimeField.setText(time);
    }

    public void setDate(int year, int month, int day) {
        datePicker.getModel().setDate(year, month, day);
    }

    public void setDoctorsList(List<Employee> doctors) {
        doctorBox.removeAllItems();
        for (Employee e : doctors) {
            doctorBox.addItem("Dr. " + e.getFirstName() + " " + e.getLastName());
        }
    }

    public void setTableValue(String value, int row, int column) {
        table.setValueAt(value, row, column);
    }

    public void setIdNumber(int id) {
        idNumber = id;
    }
}