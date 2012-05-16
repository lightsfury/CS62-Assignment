package cs62.rbeam.swingFinal;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import cs62.rbeam.swingFinal.DataInterface.DataEntry;

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
		
		data = new DataInterface();
		
		init();
		
		setSize(800, 600);
		setVisible(true);
		
		startLoad();
	}
	
	private void startLoad()
	{
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
		textField = new TextFieldWithPlaceholder("Filter", 20);
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SwingFinal.this.onFilterAction();
			}
		});
		pane2.add(textField);
		filterField = (TextFieldWithPlaceholder)(textField);
		
		button = new JButton("Reset filter");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SwingFinal.this.filterField.showPlaceholderText();
				SwingFinal.this.onFilterAction();
			}
		});
		pane2.add(button);
		
		pane.add(pane2, BorderLayout.EAST);
		
		add(pane, BorderLayout.NORTH);

		// Setup the data model
		dataModel = new InventoryTableModel(data);
		
		// Setup the table sorter/filter
		tableSorter = new TableRowSorter<InventoryTableModel>(dataModel);
		
		// Setup the table
		table = new JTable(dataModel);
		table.setFillsViewportHeight(true);
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setRowSorter(tableSorter);
		
		// Setup the table's scroll panel
		JScrollPane pane3 = new JScrollPane(table);
		
		add(pane3, BorderLayout.CENTER);
    }

	protected void onToggleEdit()
    {
		editEnabled = editToggle.isSelected();
		table.setCellSelectionEnabled(editEnabled);
    }

	protected void onFilterAction()
    {
		// Filter the table results
		String text = filterField.getText();
		RowFilter<InventoryTableModel, Object> filter = null;
		
		try
		{
			filter = RowFilter.regexFilter(text, 0);
			
			tableSorter.setRowFilter(filter);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "The filter text appears to be contain an error. Please check the syntax.", "Error", JOptionPane.ERROR_MESSAGE);
		}
    }

	protected void onNewItem()
    {
		DataEntry entry = new DataEntry();
		data.addRow(entry);
		
		
		/*
		// Show the add/edit item dialog
		ItemEditDialog dialog = new ItemEditDialog(this);
		
		// Add a submit listener to know when the user is done
		dialog.addSubmitListener(new SubmitListener() {
			@Override
			public void dialogSubmit(DataEntry entry)
			{
				SwingFinal.this.data.addRow(entry);
			}
		});
		
		// And show the dialog for new items
		dialog.showNew(); // */
    }
	
	private JCheckBox editToggle;
	private boolean editEnabled = false;
	private TextFieldWithPlaceholder filterField;
	private InventoryTableModel dataModel;
	private JTable table;
	private DataInterface data;
	private TableRowSorter<InventoryTableModel> tableSorter;
}
