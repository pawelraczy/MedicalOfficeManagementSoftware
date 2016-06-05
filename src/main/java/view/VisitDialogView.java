package view;

import helper.DateLabelFormatter;
import helper.GBC;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import net.sourceforge.jdatepicker.JDatePicker;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * This is the class, which represents visits panel after using "Add visit"
 * button on view.PanelPatientsListView.
 */

public class VisitDialogView extends JDialog {
	private JDatePickerImpl vDatePicker;
	private JComboBox<String> time1Box;
	private JComboBox<String> time2Box;
	private JTextArea visitReportArea;
	private UtilDateModel vDateModel;
	private JLabel infoLabel;
	private JButton okButton;
	private JButton cancelButton;

	public VisitDialogView() {
		super((Frame) null, "Visit report", true);

		// Panel settings
		JPanel mainPanel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		mainPanel.setLayout(gridBagLayout);

		// JLabels
		JLabel dateLabel = new JLabel("Date:");
		JLabel timeLabel = new JLabel("Time");
		JLabel visitReportLabel = new JLabel("Finished medical treatments:");

		// Date picker
		UtilDateModel vDateModel = new UtilDateModel();
		Calendar today = Calendar.getInstance();
		vDateModel.setDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
				today.get(Calendar.DAY_OF_MONTH));
		vDateModel.setSelected(true);
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl hireDatePanel = new JDatePanelImpl(vDateModel, p);
		JDatePickerImpl vDatePicker = new JDatePickerImpl(hireDatePanel, new DateLabelFormatter());

		// Time fields
		String[] time1Settings = { "08", "09", "10", "11", "12", "13", "14",
				"15" };
		String[] time2Settings = { "00", "30" };

		time1Box = new JComboBox<String>(time1Settings);
		time2Box = new JComboBox<String>(time2Settings);
		time1Box.setBackground(Color.WHITE);
		time1Box.setSelectedItem(null);
		time2Box.setBackground(Color.WHITE);
		time2Box.setSelectedItem(null);

		JPanel timePanel = new JPanel();
		timePanel.add(time1Box);
		timePanel.add(time2Box);

		// Visit report area
		visitReportArea = new JTextArea(10, 1);
		visitReportArea.setBorder(BorderFactory
				.createLineBorder(Color.LIGHT_GRAY));
		visitReportArea.setLineWrap(true);
		JScrollPane visitReportScrollPane = new JScrollPane(visitReportArea);

		// Buttons
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");

		// JPanel with buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);

		// Info JLabel on the bottom
		infoLabel = new JLabel("");
		infoLabel.setForeground(Color.BLUE);
		// infoLabel.setBorder(BorderFactory.createRaisedBevelBorder());

		// Adding components to JPanel
		// row 1
		mainPanel.add(dateLabel, new GBC(0, 0, 1, 1).setInsets(1, 1, 1, 10)
				.setAnchor(GBC.WEST));
		mainPanel.add(vDatePicker, new GBC(0, 1, 1, 1).setWeight(100, 0)
				.setFill(GBC.HORIZONTAL).setInsets(1, 1, 20, 50));
		mainPanel.add(timeLabel, new GBC(1, 0, 1, 1).setInsets(1, 1, 1, 10)
				.setAnchor(GBC.WEST));
		mainPanel.add(timePanel,
				new GBC(1, 1, 1, 1).setWeight(0, 0).setInsets(1, 1, 20, 50)
						.setAnchor(GBC.WEST));
		// row 2
		mainPanel.add(visitReportLabel,
				new GBC(0, 2, 1, 1).setInsets(1, 1, 1, 10).setAnchor(GBC.WEST));
		mainPanel.add(visitReportScrollPane,
				new GBC(0, 3, 2, 1).setWeight(100, 100).setFill(GBC.BOTH)
						.setInsets(1, 1, 20, 50));
		mainPanel.add(buttonsPanel, new GBC(0, 4, 2, 1).setInsets(1, 1, 1, 10)
				.setAnchor(GBC.WEST));
		mainPanel.add(infoLabel, new GBC(0, 5, 2, 1).setInsets(1, 1, 1, 10)
				.setAnchor(GBC.WEST));

		// Add panel to JDialog
		add(mainPanel);
	}

	// Getters
	@SuppressWarnings("deprecation")
	public Calendar getDate() {
		Calendar cal = null;
		if (vDatePicker.getModel().getValue() != null) {
			cal = Calendar.getInstance();
			Date tempCal = (Date) vDatePicker.getModel().getValue();
			if (time1Box.getSelectedItem() != null)
				tempCal.setHours(Integer.parseInt((String) time1Box
						.getSelectedItem()));
			if (time2Box.getSelectedItem() != null)
				tempCal.setMinutes(Integer.parseInt((String) time2Box
						.getSelectedItem()));
			cal.setTime(tempCal);
		}
		return cal;
	}

	public String getTime1() {
		return (String) time1Box.getSelectedItem();
	}

	public String getTime2() {
		return (String) time2Box.getSelectedItem();
	}

	public String getReport() {
		return visitReportArea.getText();
	}

	// Setters
	public void setDate(int year, int month, int day) {
		vDateModel.setDate(year, month, day);
	}

	public void setTime1(String time) {
		time1Box.setSelectedItem(time);
	}

	public void setTime2(String time) {
		time2Box.setSelectedItem(time);
	}

	public void setReport(String report) {
		visitReportArea.setText(report);
	}

	public void setInfoLabel(String message) {
		infoLabel.setText(message);
	}

	// AddActionListener methods
	public void addActionListenerToOkButton(ActionListener listener) {
		okButton.addActionListener(listener);
	}

	public void addActionListenerToCancelButton(ActionListener listener) {
		cancelButton.addActionListener(listener);
	}
}
