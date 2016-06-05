package view;

import helper.DateLabelFormatter;
import helper.GBC;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;

/**
 * This is a view for view.PanelAddEmployeeView (main view:
 * "view.MenuAndMainView")
 * 
 * @param <JDatePicker>
 * @param <dateModel>
 * @param <dateModel>
 * @param <UtilDateModel>
 */

public class PanelAddEmployeeView<JDatePicker, dateModel, UtilDateModel>
		extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton clearFields;
	private JButton confirm;
	private JButton cancel;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JComboBox<String> genderBox;
	private JDatePicker dateOfBirthPicker;
	private JTextField cityField;
	private JTextField postCodeField1;
	private JTextField postCodeField2;
	private JTextField streetField;
	private JTextField apartmentNumberField;
	private JTextField phoneNumberField1;
	private JTextField phoneNumberField2;
	private JTextField eMailField;
	private JTextField personalIdentityNumberField;
	private JTextField bankAccountNumberField;
	private JDatePicker hireDatePicker;
	private UtilDateModel dateModel, hireDateModel;
	private JDatePanelImpl datePanel;
	private JDatePickerImpl datePicker;
	private JComboBox<String> jobBox;
	private int idNumber = 0;

	@SuppressWarnings("hiding")
	public <dateModel> PanelAddEmployeeView() {
		// JPanel settings
		setBorder(BorderFactory.createTitledBorder("Add a new employee"));
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);

		// JLabels (titles)
		JLabel firstNameLabel = new JLabel("First name:");
		JLabel lastNameLabel = new JLabel("Last name:");
		JLabel genderLabel = new JLabel("Gender:");
		JLabel dateOfBirthLabel = new JLabel("Date of birth:");
		JLabel cityLabel = new JLabel("City:");
		JLabel postCodeLabel = new JLabel("Post code [xx-xxx]:");
		JLabel streetLabel = new JLabel("Street:");
		JLabel houseApartmentNumberLabel = new JLabel("Apartment number:");
		JLabel phoneNumberLabel = new JLabel("Phone number:");
		JLabel eMailLabel = new JLabel("E-mail:");
		JLabel personalIdentityNumberLabel = new JLabel(
				"Personal identity number:");
		JLabel bankAccountNumberLabel = new JLabel("Bank account number");
		JLabel hireDateLabel = new JLabel("Hire date:");
		JLabel jobLabel = new JLabel("Job:");

		// Fields for user (select or fill up)
		firstNameField = new JTextField();
		firstNameField.setPreferredSize(new Dimension(100, 20));
		lastNameField = new JTextField();
		lastNameField.setPreferredSize(new Dimension(100, 20));

		String[] genderSettings = { "Male", "Female" };
		genderBox = new JComboBox<String>(genderSettings);
		genderBox.setBackground(Color.WHITE);
		genderBox.setSelectedItem(null);
		
		JPanel panel = new JPanel();
		UtilDateModel dateModel = new UtilDateModel();
		datePanel = new JDatePanelImpl(dateModel);
		datePicker = new JDatePickerImpl(datePanel, null);
		panel.add(datePicker);
		dateModel.setDate(1980, 0, 1);
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		dateOfBirthPicker = new JDatePicker(datePanel, new DateLabelFormatter());

		cityField = new JTextField();

		JPanel postCodePanel = new JPanel();
		postCodeField1 = new JTextField();
		postCodeField1.setPreferredSize(new Dimension(22, 20));
		postCodeField2 = new JTextField();
		postCodeField2.setPreferredSize(new Dimension(30, 20));
		postCodePanel.add(postCodeField1);
		postCodePanel.add(new JLabel("-"));
		postCodePanel.add(postCodeField2);

		streetField = new JTextField();
		apartmentNumberField = new JTextField();
		apartmentNumberField.setPreferredSize(new Dimension(100, 20));

		JPanel phoneNumberPanel = new JPanel();
		phoneNumberField1 = new JTextField();
		phoneNumberField1.setPreferredSize(new Dimension(24, 20));
		phoneNumberField2 = new JTextField();
		phoneNumberField2.setPreferredSize(new Dimension(72, 20));
		phoneNumberPanel.add(new JLabel("+("));
		phoneNumberPanel.add(phoneNumberField1);
		phoneNumberPanel.add(new JLabel(")"));
		phoneNumberPanel.add(phoneNumberField2);

		eMailField = new JTextField();

		personalIdentityNumberField = new JTextField();
		personalIdentityNumberField.setPreferredSize(new Dimension(100, 20));

		bankAccountNumberField = new JTextField();
		bankAccountNumberField.setPreferredSize(new Dimension(100, 20));

		hireDateModel = new UtilDateModel();
		Calendar today = Calendar.getInstance();
		hireDateModel.setDate(today.get(Calendar.YEAR),
				today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
		((AbstractButton) hireDateModel).setSelected(true);
		JPanel hireDatePanel = new JPanel();
		hireDatePicker = new JDatePicker(hireDatePanel,
				new DateLabelFormatter());

		String[] jobSettings = { "doctor", "receptionist", "assistant",
				"charwoman", "manager" };
		jobBox = new JComboBox<String>(jobSettings);
		jobBox.setBackground(Color.WHITE);
		jobBox.setSelectedItem(null);

		// Buttons on left JPanel
		clearFields = new JButton("Clear fields");
		confirm = new JButton("Confirm");
		cancel = new JButton("Cancel");

		// Adding components to JPanel on the left side
		GridLayout leftPanelLayout = new GridLayout(4, 1);
		leftPanelLayout.setVgap(5);
		JPanel leftPanel = new JPanel(leftPanelLayout);
		leftPanel.add(clearFields);
		leftPanel.add(new JLabel());
		leftPanel.add(confirm);
		leftPanel.add(cancel);

		// Adding components to JPanel on the right side
		JPanel rightPanel = new JPanel(gridBagLayout);
		// row 1
		rightPanel.add(firstNameLabel,
				new GBC(0, 0, 1, 1).setInsets(1, 1, 1, 10).setAnchor(GBC.WEST));
		rightPanel.add(firstNameField, new GBC(0, 1, 1, 1).setWeight(100, 0)
				.setFill(GBC.HORIZONTAL).setInsets(1, 1, 20, 50));
		rightPanel.add(lastNameLabel, new GBC(1, 0, 1, 1)
				.setInsets(1, 1, 1, 10).setAnchor(GBC.WEST));
		rightPanel.add(lastNameField, new GBC(1, 1, 1, 1).setWeight(100, 0)
				.setFill(GBC.HORIZONTAL).setInsets(1, 1, 20, 50));
		rightPanel.add(genderLabel, new GBC(2, 0, 1, 1).setInsets(1, 1, 1, 10)
				.setAnchor(GBC.WEST));
		rightPanel.add(genderBox, new GBC(2, 1, 1, 1).setWeight(0, 0)
				.setInsets(1, 1, 20, 50).setAnchor(GBC.WEST));
		rightPanel.add(dateOfBirthLabel,
				new GBC(3, 0, 1, 1).setInsets(1, 1, 1, 10).setAnchor(GBC.WEST));
		rightPanel.add((Component) dateOfBirthPicker,
				new GBC(3, 1, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL)
						.setInsets(1, 1, 20, 50));
		// row 2
		rightPanel.add(cityLabel, new GBC(0, 2, 1, 1).setInsets(1, 1, 1, 10)
				.setAnchor(GBC.WEST));
		rightPanel.add(cityField, new GBC(0, 3, 1, 1).setWeight(100, 0)
				.setFill(GBC.HORIZONTAL).setInsets(1, 1, 20, 50));
		rightPanel.add(postCodeLabel, new GBC(1, 2, 1, 1)
				.setInsets(1, 1, 1, 10).setAnchor(GBC.WEST));
		rightPanel.add(postCodePanel,
				new GBC(1, 3, 1, 1).setInsets(1, 1, 20, 50).setWeight(100, 0)
						.setAnchor(GBC.WEST));
		rightPanel.add(streetLabel, new GBC(2, 2, 1, 1).setInsets(1, 1, 1, 10)
				.setAnchor(GBC.WEST));
		rightPanel.add(streetField, new GBC(2, 3, 1, 1).setWeight(100, 0)
				.setFill(GBC.HORIZONTAL).setInsets(1, 1, 20, 50));
		rightPanel.add(houseApartmentNumberLabel, new GBC(3, 2, 1, 1)
				.setInsets(1, 1, 1, 10).setAnchor(GBC.WEST));
		rightPanel.add(apartmentNumberField,
				new GBC(3, 3, 1, 1).setInsets(1, 1, 20, 50).setWeight(100, 0)
						.setFill(GBC.HORIZONTAL));
		// row 3
		rightPanel.add(phoneNumberLabel,
				new GBC(0, 4, 1, 1).setInsets(1, 1, 1, 10).setAnchor(GBC.WEST));
		rightPanel.add(phoneNumberPanel, new GBC(0, 5, 1, 1).setWeight(100, 0)
				.setInsets(1, 1, 20, 50).setAnchor(GBC.WEST));
		rightPanel.add(eMailLabel, new GBC(1, 4, 1, 1).setInsets(1, 1, 1, 10)
				.setAnchor(GBC.WEST));
		rightPanel.add(eMailField, new GBC(1, 5, 1, 1).setWeight(100, 0)
				.setFill(GBC.HORIZONTAL).setInsets(1, 1, 20, 50));
		rightPanel.add(personalIdentityNumberLabel, new GBC(2, 4, 1, 1)
				.setInsets(1, 1, 1, 10).setAnchor(GBC.WEST));
		rightPanel.add(personalIdentityNumberField, new GBC(2, 5, 1, 1)
				.setInsets(1, 1, 20, 50).setAnchor(GBC.WEST));
		// row 4
		rightPanel.add(bankAccountNumberLabel,
				new GBC(0, 6, 1, 1).setInsets(1, 1, 1, 10).setAnchor(GBC.WEST));
		rightPanel.add(bankAccountNumberField,
				new GBC(0, 7, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL)
						.setInsets(1, 1, 20, 50));
		rightPanel.add(hireDateLabel, new GBC(1, 6, 1, 1)
				.setInsets(1, 1, 1, 10).setAnchor(GBC.WEST));
		rightPanel.add((Component) hireDatePicker,
				new GBC(1, 7, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL)
						.setInsets(1, 1, 20, 50));
		rightPanel.add(jobLabel, new GBC(2, 6, 1, 1).setInsets(1, 1, 1, 10)
				.setAnchor(GBC.WEST));
		rightPanel.add(jobBox,
				new GBC(2, 7, 1, 1).setWeight(0, 0).setInsets(1, 1, 20, 50)
						.setAnchor(GBC.WEST));

		// Adding components to main JPanel
		add(leftPanel, new GBC(0, 0, 1, 1).setInsets(1).setFill(GBC.HORIZONTAL)
				.setInsets(1, 1, 1, 15));
		add(rightPanel, new GBC(1, 0, 1, 2).setWeight(100, 100).setInsets(1)
				.setFill(GBC.HORIZONTAL).setAnchor(GBC.NORTH));
	}

	// AddActionListener methods
	public void addActionListenerToConfirmButton(ActionListener listener) {
		confirm.addActionListener(listener);
	}

	public void addActionListenerToCancelButton(ActionListener listener) {
		cancel.addActionListener(listener);
	}

	public void addActionListenerToClearButton(ActionListener listener) {
		clearFields.addActionListener(listener);
	}

	// Getters
	public String getFirstName() {
		return firstNameField.getText();
	}

	public String getLastName() {
		return lastNameField.getText();
	}

	public String getGender() {
		return (String) genderBox.getSelectedItem();
	}

	public Calendar getDateOfBirth() {
		Calendar cal = null;
		if (((AbstractButton) dateOfBirthPicker).getModel() != null) {
			cal = Calendar.getInstance();
			cal.setTime((Date) ((AbstractButton) dateOfBirthPicker).getModel());
		}
		return cal;
	}

	public String getCity() {
		return cityField.getText();
	}

	public String getPostCode1() {
		return postCodeField1.getText();
	}

	public String getPostCode2() {
		return postCodeField2.getText();
	}

	public String getStreet() {
		return streetField.getText();
	}

	public String getApartmentNumber() {
		return apartmentNumberField.getText();
	}

	public String getPhoneNumber1() {
		return phoneNumberField1.getText();
	}

	public String getPhoneNumber2() {
		return phoneNumberField2.getText();
	}

	public String getEMail() {
		return eMailField.getText();
	}

	public String getPersonalIdentityNumber() {
		return personalIdentityNumberField.getText();
	}

	public String getBankAccountNumber() {
		return bankAccountNumberField.getText();
	}

	public Calendar getHireDate() {
		Calendar cal = null;
		if (((Object) ((AbstractButton) hireDatePicker).getModel()) != null) {
			cal = Calendar.getInstance();
			cal.setTime((Date) ((Object) ((AbstractButton) hireDatePicker)
					.getModel()));
		}
		return cal;
	}

	public String getJob() {
		return (String) jobBox.getSelectedItem();
	}

	// Setters
	public void setFirstName(String firstName) {
		firstNameField.setText(firstName);
	}

	public void setLastName(String lastName) {
		lastNameField.setText(lastName);
	}

	public void setGender(String gender) {
		genderBox.setSelectedItem(gender);
	}

	public void setDateOfBirth(int year, int month, int day) {
		dateModel.setDateOfBirth(year, month, day);
	}

	public void setCity(String city) {
		cityField.setText(city);
	}

	public void setPostCode1(String postCode1) {
		postCodeField1.setText(postCode1);
	}

	public void setPostCode2(String postCode2) {
		postCodeField2.setText(postCode2);
	}

	public void setStreet(String street) {
		streetField.setText(street);
	}

	public void setApartmentNumber(String apartmentNumber) {
		apartmentNumberField.setText(apartmentNumber);
	}

	public void setPhoneNumber1(String phoneNumber1) {
		phoneNumberField1.setText(phoneNumber1);
	}

	public void setPhoneNumber2(String phoneNumber2) {
		phoneNumberField2.setText(phoneNumber2);
	}

	public void setEMail(String eMail) {
		eMailField.setText(eMail);
	}

	public void setPersonalIdentityNumber(String personalIdentityNumber) {
		personalIdentityNumberField.setText(personalIdentityNumber);
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		bankAccountNumberField.setText(bankAccountNumber);
	}

	public void setHireDate(int year, int month, int day) {
		hireDateModel.setDate(year, month, day);
	}

	public void setJob(String job) {
		jobBox.setSelectedItem(job);
	}

	public void setDateModelSelected(boolean value) {
		((AbstractButton) dateModel).setSelected(value);
	}

	public void setHireDateModelSelected(boolean value) {
		((AbstractButton) hireDateModel).setSelected(value);
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int id) {
		idNumber = id;
	}

	/**
	 * Turn on/off Clear fields button
	 *
	 * @param value
	 *            true (button enabled) or false (button disabled)
	 */
	public void setClearFieldsButtonVisibility(boolean value) {
		if (value == false)
			clearFields.setEnabled(false);
		else
			clearFields.setEnabled(true);
	}
}
