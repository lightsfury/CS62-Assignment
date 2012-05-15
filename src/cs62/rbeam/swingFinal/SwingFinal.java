package cs62.rbeam.swingFinal;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import cs62.rbeam.swingFinal.NewItemDialog.SubmitListener;

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
		
		int loadOption;
		LoadOptions[] loadOptions = new LoadOptions[] {
				new LoadOptions(LoadOptions.Values.Empty, "Create new data set"),
				new LoadOptions(LoadOptions.Values.File, "Load from file"),
				new LoadOptions(LoadOptions.Values.Database, "Load from database"),
		};
		
		loadOption = JOptionPane.showOptionDialog(this, "Welcome to Inventory Management\nPlease choose a load option:", "Inventory Management",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, loadOptions, loadOptions[0]);
		
		switch (loadOptions[loadOption].getValue())
		{
		default:
		case Empty:
			break;
		case File:
			loadFromFile();
			break;
		case Database:
			loadFromDatabase();
			break;
		}
	}

	private void loadFromDatabase()
    {
		JOptionPane.showMessageDialog(this, "Loading from a database is not supported yet.");
    }

	private void loadFromFile()
    {
		//! @todo
    }

	private void init()
    {
		setLayout(new BorderLayout(10, 10));
		
		// Pre-declare throw away variables
		JPanel pane, pane2;
		JButton button;
		JCheckBox toggle;
		JTextField textField;
		
		// The northern pane
		pane = new JPanel(new BorderLayout(10, 10));
		
		// The NW pane
		pane2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		button = new JButton("New item", UIManager.getIcon("Tree.collapsedIcon"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				SwingFinal.this.onNewItem();
			}
		});
		pane2.add(button);
		toggle = new JCheckBox("Edit data", false);
		toggle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SwingFinal.this.onToggleEdit();
			}
		});
		editToggle = toggle;
		pane2.add(toggle);
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
		tableModel = new DefaultTableModel();
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setAutoCreateRowSorter(true);
		
		JScrollPane pane3 = new JScrollPane(table);
		
		add(pane3, BorderLayout.CENTER);
    }

	protected void onToggleEdit()
    {
		editEnabled = editToggle.isSelected();
    }

	protected void onFilterAction()
    {
		// Filter the table results
    }

	protected void onNewItem()
    {
		// Show the add/edit item dialog
		NewItemDialog dialog = new NewItemDialog(this);
		
		// Add a submit listener to know when the user is done
		dialog.addSubmitListener(new NewItemDialog.SubmitListener() {
			@Override
			public void dialogSubmit(DataEntry entry)
			{
				SwingFinal.this.dataStorage.addRowData(entry);
			}
		});
		
		// And show the dialog for new items
		dialog.showNew();
    }
	
	private JCheckBox editToggle;
	private boolean editEnabled = false;
	private FadeField filterField;
	private TableModel tableModel;
	private JTable table;
	
	static private Object[] columnNames = {
		"ID #", "Quantity", "Name", "Description"
	};
}

//! @todo Replace with an interface to link a JFrame to a modal dialog
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
		NewItemDialog dialog = new NewItemDialog(parentFrame);
		
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
		
		button = new JButton();
		button.setOpaque(true);
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int coloumn)
	{
		if (isSelected)
		{
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		}
		else
		{
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
		}
		label = (value == null) ? "" : value.toString();
		button.setText(label);
		active = true;
		return button;
	}
	
	public Object getCellEditorValue()
	{
		if (active)
		{
			// fireButtonListeners
		}
		
		active = false;
		return new String(label);
	}
	
	public boolean stopCellEditing()
	{
		active = false;
		return super.stopCellEditing();
	}
	
	public void fireEditingStopped()
	{
		super.fireEditingStopped();
	}
	
	private JButton button;
	private String label;
	private boolean active;
}