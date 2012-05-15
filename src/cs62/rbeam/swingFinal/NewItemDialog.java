package cs62.rbeam.swingFinal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cs62.rbeam.swingFinal.DataStorage.DataEntry;

//Dialog used to add and edit items
class NewItemDialog extends JDialog
{
    private static final long serialVersionUID = 1L;
	// Similar to AWT listeners. This one is used to capture the "Submit" button on the dialog
	public interface SubmitListener
	{
		public void dialogSubmit(DataEntry entry);
	}
	
	public NewItemDialog(Frame parentFrame)
	{
		// All good modal dialogs need a parent frame
		super(parentFrame, "Add/Edit item", true);
		// Don't do anything foolish if the wind is closed
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		init();
		
		pack();
	}

	public void addSubmitListener(SubmitListener submitListener)
	{
		// Lazy allocate the listeners
		if (listeners == null)
		{
			listeners = new Vector<SubmitListener>(2);
		}
		
		// And add the listener
		listeners.add(submitListener);
	}

	public void showNew()
	{
		// We don't need to do any magic for a new item
		setVisible(true);
	}
	
	public void showEdit(DataEntry rowData)
	{
		// Load the data into the text fields
		loadData(rowData);

		setVisible(true);
	}

	protected void onCancel()
	{
		// The user clicked the cancel button
		// So hide the dialog
		setVisible(false);
		
		// And dispose of it
		dispose();
	}
	
	protected void onSubmit()
	{
		// The user clicked the submit button
		// So hide the dialog
		setVisible(false);
		
		// Save the data
		saveData();
		
		// And dispatch the listeners
		if (listeners != null)
		{
			for (SubmitListener l : listeners)
			{
				if (l != null)
				{
					l.dialogSubmit(entry);
				}
			}
		}
	}
	
	private void loadData(DataEntry rowData)
	{
		// Load the information from the rowData into the fields
		idField.setText(Integer.toString(rowData.IDNumber));
		nameField.setText(rowData.Name);
		descField.setText(rowData.Description);
		countField.setText(Integer.toString(rowData.Quantity));
	}
	
	private void saveData()
	{
		// Save the field data
		int id, count;
		String name, desc;
		
		// Send a negative ID if the data storage is generating one
		if (generateID.isSelected() == true)
		{
			id = -1;
		}
		else
		{
			// Otherwise decode the text field
			id = Integer.decode(idField.getText());
		}
		
		count = Integer.decode(countField.getText());
		
		// Repeat for name and description
		name = nameField.getText();
		
		desc = descField.getText();
		
		// And put the information in a DataEntry block
		entry = new DataEntry();
		
		entry.IDNumber = id;
		entry.Quantity = count;
		entry.Name = name;
		entry.Description = desc;
	}

	private void init()
	{
		setLayout(new BorderLayout(5, 5));
		
		JPanel pane, pane2;
		JButton button;
		
		// Box-pane
		pane2 = new JPanel();
		
		// Create the ID toggle
		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		generateID = new JCheckBox("Generate ID number");
		generateID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				NewItemDialog.this.toggleGenerateID();
			}
		});
		generateID.setSelected(true);
		pane.add(generateID);
		pane2.add(pane);

		// Create the ID entry field
		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pane.add(new JLabel("ID number:"));
		idField = new JTextField(20);
		idField.setEnabled(false);
		pane.add(idField);
		pane2.add(pane);

		// Create the name entry field
		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pane.add(new JLabel("Item name:"));
		nameField = new JTextField(20);
		pane.add(nameField);
		pane2.add(pane);
		
		// Create the quantity entry field
		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pane.add(new JLabel("Initial quanitity:"));
		countField = new JTextField(20);
		pane.add(countField);
		pane2.add(pane);

		// Create the description entry label
		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pane.add(new JLabel("Description"));
		pane2.add(pane);
		
		pane2.setLayout(new BoxLayout(pane2, BoxLayout.PAGE_AXIS));
		
		add(pane2, BorderLayout.NORTH);
		
		// Add the description entry field to its own Border region
		descField = new JTextArea();
		add(descField, BorderLayout.CENTER);
		
		// Create the submit and cancel buttons
		pane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		button = new JButton("Submit");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				NewItemDialog.this.onSubmit();
			}
		});
		pane.add(button);
		button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				NewItemDialog.this.onCancel();
			}
		});
		pane.add(button);
		
		// Add them to the bottom of the dialog
		add(pane, BorderLayout.SOUTH);
 }
	
	protected void toggleGenerateID()
    {
		idField.setEnabled(generateID.isSelected() == false);
    }

	private JTextField    nameField,
	                      countField,
	                      idField;
	private JTextArea     descField;
	private JCheckBox     generateID;
	private DataEntry     entry;
	private Vector<SubmitListener>
	                      listeners;
}
