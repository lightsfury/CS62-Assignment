package cs62;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.*;

import cs62.DataStorage.DataEntry;
import cs62.ItemDialog.SubmitListener;

public class SwingFinal extends JFrame
{
    private static final long serialVersionUID = 1L;
    
	public static void main(String[] args)
	{
		try
		{
			// Try to adopt the OS's look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			// We don't really care if this fails
		}
		
		// And create the window
		new SwingFinal();
	}
	
	public SwingFinal()
	{
		super("Inventory management");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		init();
		
		setSize(800, 600);
		setVisible(true);
	}

	private void init()
    {
		setLayout(new BorderLayout(10, 10));
		
		// Pre-declare throw away variables
		JPanel pane, pane2;
		JButton button;
		JTextField textField;
		
		// The northern pane
		pane = new JPanel(new BorderLayout(10, 10));
		
		// The NW pane
		pane2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		button = new JButton("New item");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				SwingFinal.this.onNewItem();
			}
		});
		pane2.add(button);
		pane.add(pane2, BorderLayout.WEST);
		
		// The NE pane
		pane2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		textField = new FadeField(20);
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SwingFinal.this.onFilterAction();
			}
		});
		pane2.add(textField);
		filterField = (FadeField)(textField);
		
		button = new JButton("Reset filter");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SwingFinal.this.filterField.clearText();
			}
		});
		pane2.add(button);
		
		pane.add(pane2, BorderLayout.EAST);
		
		add(pane, BorderLayout.NORTH);

		// The table pane with scroll action
		dataStorage = new DataStorage(this);
		JTable table = new JTable(dataStorage);
		table.setFillsViewportHeight(true);
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setAutoCreateRowSorter(true);
		
		JScrollPane pane3 = new JScrollPane(table);
		
		add(pane3, BorderLayout.CENTER);
    }

	protected void onFilterAction()
    {
		// Filter the table results
    }

	protected void onNewItem()
    {
		// Show the add/edit item dialog
		ItemDialog dialog = new ItemDialog(this);
		
		// Add a submit listener to know when the user is done
		dialog.addSubmitListener(new ItemDialog.SubmitListener() {
			@Override
			public void dialogSubmit(DataEntry entry)
			{
				SwingFinal.this.dataStorage.addRowData(entry);
			}
		});
		
		// And show the dialog for new items
		dialog.showNew();
    }
	
	private FadeField filterField;
	private DataStorage dataStorage;
}

// Extended text field to automatically present placeholder text while the text field is empty
class FadeField extends JTextField implements FocusListener
{
    private static final long serialVersionUID = 1L;
    
	public FadeField()
	{
		super();
		
		init();
	}
	
	public FadeField(int cols)
	{
		super(cols);
		
		init();
	}

	@Override
    public void focusGained(FocusEvent arg0)
    {
		// Are we showing the placeholder text?
		if (showPlaceHolderText)
		{
			showPlaceHolderText = false;
			updateColor();
		}
    }

	@Override
    public void focusLost(FocusEvent arg0)
    {
		// Get the current string
		String text = getText();
		// Do we have an empty string?
		if (text == null || text.length() <= 0)
		{
			showPlaceHolderText = true;
			updateColor();
		}
    }
	
	public void clearText()
	{
		// Because sometimes we just want to nuke the text and show the placeholder
		showPlaceHolderText = true;
		updateColor();
	}
	
	private void init()
    {
		// Begin by showing the placeholder text
		showPlaceHolderText = true;
		// Make sure to get the previous forground color
		defaultTextFieldForeground = getForeground();
		// Set the new color
		updateColor();
		// And add a listener for focus changes
		addFocusListener(this);
    }
	
	private void updateColor()
	{
		// Centralized place to switch out the placeholder text
		if (showPlaceHolderText)
		{
			setForeground(Color.GRAY);
			setText("Filter");
		}
		else
		{
			setForeground(defaultTextFieldForeground);
			setText("");
		}
	}
	
	private boolean showPlaceHolderText;
	protected Color defaultTextFieldForeground;
}

// Dialog used to add and edit items
class ItemDialog extends JDialog
{
	// Similar to AWT listeners. This one is used to capture the "Submit" button on the dialog
	public interface SubmitListener
	{
		public void dialogSubmit(DataEntry entry);
	}
	
	public ItemDialog(Frame parentFrame)
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
    }
	
	private void saveData()
	{
		// Save the field data
		int id;
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
		
		// Repeat for name and description
		name = nameField.getText();
		
		desc = descField.getText();
		
		// And put the information in a DataEntry block
		entry = new DataEntry();
		
		entry.IDNumber = id;
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
				ItemDialog.this.idField.setEnabled(ItemDialog.this.generateID.isSelected() == false);
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
				ItemDialog.this.onSubmit();
			}
		});
		pane.add(button);
		button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ItemDialog.this.onCancel();
			}
		});
		pane.add(button);
		
		// Add them to the bottom of the dialog
		add(pane, BorderLayout.SOUTH);
    }
	
	private JTextField    nameField,
	                      idField;
	private JTextArea     descField;
	private JCheckBox     generateID;
	private DataEntry     entry;
	private Vector<SubmitListener>
	                      listeners;
}

// Extends JButton to easily show an ItemDialog
class EditButton extends JButton implements ActionListener
{
    private static final long serialVersionUID = 1L;

	public EditButton(Frame parent)
	{
		super("Edit");
		parentFrame = parent;
	}

	public void setIndex(int rowIndex)
    {
		currentRowIndex = rowIndex;
    }

	@Override
    public void actionPerformed(ActionEvent e)
    {
		ItemDialog dialog = new ItemDialog(parentFrame);
		
		dialog.addSubmitListener(new SubmitListener() {
			@Override
			public void dialogSubmit(DataEntry entry)
			{
				EditButton.this.dataStorage.updateRowData(currentRowIndex, entry);
			}
		});
		
		dialog.showEdit(dataStorage.getRowData(currentRowIndex));
    }
	
	private DataStorage dataStorage;
	private int currentRowIndex;
	private Frame parentFrame;
}

class EditButtonCellEditor extends DefaultCellEditor
{
	private static final long serialVersionUID = 1L;

	public EditButtonCellEditor(JCheckBox checkBox)
	{
		super(checkBox);
		// TODO Auto-generated constructor stub
	}
	
}

// Stores an in-memory copy of the item information and exposes it to the JTable
class DataStorage extends AbstractTableModel implements TableCellRenderer
{
    private static final long serialVersionUID = 1L;
    
    // The item information storage block
	public static class DataEntry
	{
		public int    IDNumber;
		public String Description,
		              Name;
	}

	public DataStorage(Frame parent)
    {
		// Save the parent frame for the item dialogs
		parentFrame = parent;
		// Pre-allocate the data and editButtons vectors
		data = new Vector<DataEntry>(5);
		editButtons = new Vector<EditButton>(5);
    }

	@Override
    public int getColumnCount()
    {
		// Currently only 4 columns
		return 4;
    }

	@Override
    public int getRowCount()
    {
		// 1 row for each entry
		return data.size();
    }

	@Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
		DataEntry entry = data.get(rowIndex);
		
		switch (columnIndex)
		{
		case 0:
			return entry.IDNumber;
		case 1:
			return entry.Name;
		case 2:
			return entry.Description;
		case 3:
			// Get an EditButton for this row
			EditButton button = editButtons.get(rowIndex);
			// Make sure it has the proper rowIndex
			button.setIndex(rowIndex);
			return button;
		default:
			return null;
		}
    }
	
	public String getColumnName(int column)
	{
		switch (column)
		{
		case 0:
			return "ID Number";
		case 1:
			return "Name";
		case 2:
			return "Item description";
		case 3:
			return "Edit";
		default:
			return null;
		}
	}
	
	public Class<?> getColumnClass(int column)
	{
		// Vain attempts to get JTable to render the data properly
		switch (column)
		{
		case 0:
			return Integer.class;
		case 1:
		case 2:
			return String.class;
		case 3:
			return EditButton.class;
		default:
			return null;
		}
	}
	
	public DataEntry getRowData(int rowIndex)
	{
		DataEntry entry = data.get(rowIndex);
		
		return entry;
	}
	
	public void updateRowData(int rowIndex, DataEntry entry)
	{
		// Update the data vector with the new information
		data.set(rowIndex, entry);
	}
	
	public void addRowData(DataEntry entry)
	{
		// Add the information to the back of the vector
		data.add(entry);
		
		System.out.println(data.size());
		
		// And make sure there are enough EditButtons for each row of data
		if (data.size() > editButtons.size())
		{
			for (int i = editButtons.size(); i <= data.size(); i++)
			{
				editButtons.add(i, new EditButton(parentFrame));
			}
		}
		
		System.out.println(editButtons.size());
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex)
	{
		String text = (String)(value);
		EditButton button = (EditButton)(value);
		
		if (text != null)
		{
			return new JLabel(text);
		}
		else if (button != null)
		{
			button.setIndex(rowIndex);
			return button;
		}
		else
		{
			switch (columnIndex)
			{
			case 0:
			case 1:
			case 2:
			default:
				return new JLabel(value.toString());
			case 3:
				button = new EditButton(parentFrame);
				button.setIndex(rowIndex);
				return button;
			}
		}
	}
	
	private Vector<DataEntry> data;
	private Vector<EditButton> editButtons;
	private Frame parentFrame;
}