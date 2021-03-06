package view;

import helper.GBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This is a view for view.PanelPatientsListView (main view: "view.MenuAndMainView" must initiate it)
 */

public class PanelBlacklistView extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private JButton showPatientCard;
    private JButton removeFromBlacklist;
    private JTable blacklistTable;

    public PanelBlacklistView() {
        //JPanel settings
        setBorder(BorderFactory.createTitledBorder("Blacklist"));
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        //Search: JLabel and JTextField
        JLabel searchLabel = new JLabel("Enter one of: First Name/Last Name/Personal identity number");
        searchField = new JTextField();

        //Additional JButtons
        searchButton = new JButton("Search");
        showPatientCard = new JButton("Show patient card");
        removeFromBlacklist = new JButton("Remove patient from blacklist");

        //Creating JScrollPane with JTable
        String col[] = {"ID", "Last name", "First name", "Personal identity number", "Blacklist reason"};
        DefaultTableModel model = new DefaultTableModel(col, 0) {
            //Turning off editable possibility for JTable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        blacklistTable = new JTable(model);

        //Change text color in table to red
        TableCellRenderer renderer = blacklistTable.getDefaultRenderer(Object.class);
        blacklistTable.setDefaultRenderer(Object.class, new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.RED);
                return c;
            }
        });

        //Hiding ID column (first column)
        blacklistTable.removeColumn(blacklistTable.getColumnModel().getColumn(0));
        JScrollPane scrollTable = new JScrollPane(blacklistTable);

        //Adding components to JPanel on the left side
        GridLayout leftPanelLayout = new GridLayout(7, 1);
        leftPanelLayout.setVgap(5);
        JPanel leftPanel = new JPanel(leftPanelLayout);
        leftPanel.add(searchLabel);
        leftPanel.add(searchField);
        leftPanel.add(searchButton);
        leftPanel.add(new JLabel());
        leftPanel.add(showPatientCard);
        leftPanel.add(new JLabel());
        leftPanel.add(removeFromBlacklist);

        //Adding components to main JPanel
        add(leftPanel, new GBC(0, 0, 1, 1).setInsets(1).setFill(GBC.HORIZONTAL));
        add(scrollTable, new GBC(1, 0, 1, 2).setWeight(100, 100).setInsets(1).setFill(GBC.BOTH));
    }

    //AddActionListener methods
    public void addActionListenerToSearchButton(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void addActionListenerToShowPatientCardButton(ActionListener listener) {
        showPatientCard.addActionListener(listener);
    }

    public void addActionListenerToRemoveFromBlacklistButton(ActionListener listener) {
        removeFromBlacklist.addActionListener(listener);
    }

    public String getSearchValue() {
        return searchField.getText();
    }

    public JTable getTable() {
        return blacklistTable;
    }
}



