import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class to create a GUI for an example program to accept form submissions. The program keeps a list of several
 * forms that are being filled out. Each form holds some information about the user, such as their name, email,
 * and signature. The JComboBox at the top of the program is used to swap between forms that are being filled out.
 * Each form can be filled out separately, and information about each form is stored in a FormData object. These
 * objects are stored in a list and selected for editing with the ComboBox.
 *
 * The following is required of your program:
 * (1) Your frame layout should roughly match the layout demonstrated in lab and uploaded to canvas.
 * (2) You should be able to use the text fields to modify a FormData object. Pressing the "Save" button will
 *     attempt to set the values of the currently selected formdata (as corresponding to the currently selected
 *     index from the ComboBox). Use the values in the text fields to set the values of the formdata. Pressing "Reset"
 *     will clear all fields of the formdata. Pressing "New Form" will generate a new formdata for editing.
 * (3) You should be able to export all of your stored forms (i.e. export the datalist object).
 * (4) You should be able to import a set of stored forms (i.e. import a list of FormData into the datalist object).
 * (5) You should not serialize the Social Security Numbers (see the FormData class).
 *
 * Note: the different forms are represented in the ComboBox by the display names.
 *
 * Follow the TODOs to complete your code.
 *
 * @author Stephen
 * @version 2019-04-24
 */
public class DataEntryFrame extends JFrame
{
	/**
	 * Users may fill out multiple forms at once. Only one form can be displayed at once, however.
	 * As such, users may cycle through this list to edit different forms.
	 */
	private ArrayList<FormData> datalist = new ArrayList<FormData>();
	private JComboBox<String> formSelect = new JComboBox<String>();

	/**
	 * Function used for refreshing the combo box contents. Populates the box with the display names.
	 */
	private DefaultComboBoxModel<String> getComboBoxModel(List<FormData> data)
	{
		ArrayList<String> displayNames = new ArrayList<String>();
		for (FormData form : data)
		{
			displayNames.add(form.getDisplayName());
		}
		String[] comboBoxModel = displayNames.toArray(new String[displayNames.size()]);
	    return new DefaultComboBoxModel<>(comboBoxModel);
	}

	/**
	 * Identifying Information:
	 */
	private JLabel firstNameInfo = new JLabel("First Name:");
	private JTextField firstName = new JTextField(15);
	private JLabel middleInitialInfo = new JLabel("Middle Initial:");
	private JTextField middleInitial = new JTextField(1);
	private JLabel lastNameInfo = new JLabel("Last Name:");
	private JTextField lastName = new JTextField(15);
	private JLabel displayNameInfo = new JLabel("Display Name:");
	private JTextField displayName = new JTextField(15);
	private JLabel SSNInfo = new JLabel("Social Security Number:");
	private JTextField SSN = new JTextField(15);

	/**
	 * Contact information:
	 */
	private JLabel phoneInfo = new JLabel("Phone Number:");
	private JTextField phone = new JTextField(15);
	private JLabel emailInfo = new JLabel("Email Address:");
	private JTextField email = new JTextField(15);
	private JLabel addressInfo = new JLabel("Street Address:");
	private JTextField address = new JTextField(15);

	/**
	 * User verification:
	 */
	private JLabel signatureInfo = new JLabel("Signature:");
	private SignaturePanel spanel = new SignaturePanel();

	/**
	 * Translate stored form information into visual update...
	 */
	private void setVisuals(FormData data)
	{
		// Set the text fields and the signature as corresponding to the fields in FormData.
		firstName.setText(data.getFirstName());
		middleInitial.setText(Character.toString(data.getMiddleInitial()));
		lastName.setText(data.getLastName());
		displayName.setText(data.getDisplayName());
		SSN.setText(data.getSSN());
		phone.setText(data.getPhone());
		email.setText(data.getEmail());
		address.setText(data.getAddress());
		
		spanel.setSignature(data.getSignature());
	}

	/**
	 * Error/confirmation message:
	 */
	private JTextField errorField = new JTextField("No Errors");

	@SuppressWarnings("unchecked")
	public DataEntryFrame()
	{
		this.setLayout(new GridLayout(7,1));

		// Add initial form:
		datalist.add(new FormData());
		datalist.get(0).setValues("fn", 'm', "ln", "dn", "111111111", "1234567890",
				"test@ou.edu", "111 first st", new ArrayList<Point>());
		this.setVisuals(datalist.get(0));

		// Add in the form selector:
		DefaultComboBoxModel<String> comboBoxModel = getComboBoxModel(datalist);
		formSelect.setModel(comboBoxModel);
		formSelect.setSelectedIndex(0);
		formSelect.addActionListener((e) -> {
			int select = formSelect.getSelectedIndex();
			this.setVisuals(datalist.get(select));
		});
		this.add(formSelect);

		// Add in all form-fillable components:
		GridLayout layout = new GridLayout(8,2);
		JPanel formFill = new JPanel(layout);
		
		formFill.add(firstNameInfo);
		formFill.add(firstName);
		formFill.add(middleInitialInfo);
		formFill.add(middleInitial);
		formFill.add(lastNameInfo);
		formFill.add(lastName);
		formFill.add(displayNameInfo);
		formFill.add(displayName);
		
		formFill.add(SSNInfo);
		formFill.add(SSN);
		formFill.add(phoneInfo);
		formFill.add(phone);
		formFill.add(emailInfo);
		formFill.add(email);
		formFill.add(addressInfo);
		formFill.add(address);
		


		
		// TODO: add to panel...
		this.add(formFill);

		// Add in the signature panel:
		spanel.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseMoved(MouseEvent e) {}

			@Override
			public void mouseDragged(MouseEvent e)
			{
				// Add a point to the panel on drag and repaint.
				spanel.addPoint(e.getPoint());
				spanel.repaint();
			}
		});
		this.add(signatureInfo);
		this.add(spanel);

		// Add in the form create, save, and reset panel:
		JPanel formHandling = new JPanel(new GridLayout(1, 3));
		JButton createForm = new JButton("New Form");
		createForm.addActionListener((e) -> {
			FormData newData = new FormData();
			newData.setValues("fn", 'm', "ln", "dn", "111111111", "1234567890",
					"test@ou.edu", "111 first st", new ArrayList<Point>());
			datalist.add(newData);
			int select = datalist.size() - 1;
			DefaultComboBoxModel<String> newComboBoxModel = getComboBoxModel(datalist);
			formSelect.setModel(newComboBoxModel);
			formSelect.setSelectedIndex(select);
			this.setVisuals(datalist.get(select));
		});

		
		JButton saveForm = new JButton("Save");
		saveForm.addActionListener((e) -> {
			int select = formSelect.getSelectedIndex();

			// TODO: use the JTextFields and the signature panel to set the values
			// of the selected FormData object.
			
			boolean success = datalist.get(select).setValues(firstName.getText(), middleInitial.getText().charAt(0), lastName.getText(), 
					displayName.getText(), SSN.getText(), phone.getText(), email.getText(), address.getText(), 
					spanel.getSignature());

			this.setVisuals(datalist.get(select));
			formSelect.addItem(datalist.get(select).getDisplayName());
			
			sortList(); // Sorts datalist
			DefaultComboBoxModel<String> newComboBoxModel = getComboBoxModel(datalist);
			formSelect.setModel(newComboBoxModel);
			formSelect.setSelectedIndex(select);

			// TODO: display an error message if setting the values failed. Else, display a success message.w
			if(!success)
			{
				// Error message
				errorField.setText("Input info does not match require format.");
			}
			else
			{
				// Success message
				errorField.setText("Form information successfully updated");
			}
		});

		JButton resetForm = new JButton("Reset");
		resetForm.addActionListener((e) -> {
			int select = formSelect.getSelectedIndex();
			datalist.get(select).setValues("fn", 'm', "ln", "dn", "111111111", "1234567890",
					"test@ou.edu", "111 first st", new ArrayList<Point>());
			this.setVisuals(datalist.get(select));
		});

		// Add buttons to panel and add to frame
		formHandling.add(createForm);
		formHandling.add(saveForm);
		formHandling.add(resetForm);
		this.add(formHandling);

		// Add in the error message field:
		this.errorField.setEditable(false);
		// TODO: add error field to frame
		this.add(errorField);

		// Add in the import/export panel:
		JButton importButton = new JButton("Import");
		JButton exportButton = new JButton("Export");
		JPanel importExportPanel = new JPanel(new GridLayout(1,2));
		

		// TODO: Import from a file: you will import a list of FormData objects and should use this to replace
		// the data in datalist.
		importButton.addActionListener((e) -> {

			// TODO: Choose a file (hint, use JFileChooser):
			// TODO: extract object from a file (hint, use file.getAbsolutePath()):
			//		 You will use the file to replace the datalist object. I.e. you will be loading in a new
			//		 list of formdata.
			// TODO: display error message on fail, else display success message

			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("./Forms"));
			fc.setDialogTitle("Choose a file");
			
			if(fc.showOpenDialog(importButton) == JFileChooser.APPROVE_OPTION)
			{
				//
			}
			
			File file = fc.getSelectedFile();
			
			try {
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
				datalist = (ArrayList<FormData>) is.readObject();
				
				for(int i = 0; i < datalist.size(); ++i)
				{
					formSelect.addItem(datalist.get(i).getDisplayName());
				}
				
				is.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
        	
            int select = 0;
			DefaultComboBoxModel<String> newComboBoxModel = getComboBoxModel(datalist);
			formSelect.setModel(newComboBoxModel);
			formSelect.setSelectedIndex(select);
			this.setVisuals(datalist.get(select));
			
		});

		exportButton.addActionListener((e) -> {
			
			// TODO: Choose a file (hint, use JFileChooser):
			// TODO: export datalist from a file (hint, use file.getAbsolutePath()):
			// TODO: display error message on fail, else display success message
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("./Forms"));
			
			if(fc.showOpenDialog(exportButton) == JFileChooser.APPROVE_OPTION)
			{
				//
			}
			
			File file = fc.getSelectedFile();
			
			try {
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
				os.writeObject(datalist);
				os.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		});

		importExportPanel.add(importButton);
		importExportPanel.add(exportButton);
		this.add(importExportPanel);

		// JFrame basics:
		this.setTitle("Example Form Fillout");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 900);
		this.setVisible(true);
	}
	
	public void sortList()
	{
		for(int i = 0; i < datalist.size(); ++i)
		{
			String baseName = datalist.get(i).getDisplayName();
			
			for(int j = i; j< datalist.size(); ++j)
			{
				String testName = datalist.get(j).getDisplayName();
				
				if(baseName.compareTo(testName) > 0)
				{
					FormData temp = datalist.get(j);
					datalist.set(j, datalist.get(i));
					datalist.set(i, temp);
				}
					
			}
		}
	}

	public static void main(String[] args)
	{
		new DataEntryFrame();
	}
}
