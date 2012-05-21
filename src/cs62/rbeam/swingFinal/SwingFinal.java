package cs62.rbeam.swingFinal;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
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
		
		setSize(800, 600);
		
		init();
		
		setVisible(true);
		
		startLoad();
	}
	
	private void startLoad()
	{
		int loadOption;
		LoadOptions[] loadOptions = new LoadOptions[] {
				new LoadOptions(LoadOptions.Values.Empty, "Create new data set"),
				new LoadOptions(LoadOptions.Values.File, "Load from file"),
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
		}
	}

	private void loadFromFile()
    {
		// Create and show a file chooser dialog
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
    		// Get the absolute path to the file
    		String path = chooser.getSelectedFile().getAbsolutePath();
    		
    		// Have the data interface load the data
    		try
            {
	            data.loadFromFile(path);
            }
            catch (Exception e)
            {
            	JOptionPane.showMessageDialog(this, String.format("Could not load the data: %s.", e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
            }
		}
    }
	
	private void saveToFile()
	{
		JFileChooser chooser = new JFileChooser();
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			String path = chooser.getSelectedFile().getAbsolutePath();
			
			try
            {
	            data.saveToFile(path);
            }
            catch (Exception e)
            {
            	JOptionPane.showMessageDialog(this, String.format("Could not save the data: %s.", e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
            }
		}
	}

	private void init()
    {
		setLayout(new BorderLayout(10, 10));
		
		// Pre-declare throw away variables
		JPanel pane, pane2;
		JButton button;
		
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
		pane.add(pane2, BorderLayout.WEST);
		
		// The NE pane
		pane2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		filterField = new TextFieldWithPlaceholder("Filter", 20);
		filterField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SwingFinal.this.onFilterAction();
			}
		});
		pane2.add(filterField);
		
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
		
		TableEditorInterface editInterface = new TableEditorInterface(new ItemEditDialog(this), data);
		
		for (int i = 0; i < table.getColumnCount(); i++)
		{
			table.getColumn(table.getColumnName(i)).setCellEditor(editInterface);
		}
		
		// Setup the table's scroll panel
		JScrollPane pane3 = new JScrollPane(table);
		
		add(pane3, BorderLayout.CENTER);
		
		createMenu();
    }
	
	private void createMenu()
	{
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// The File menu
		menu = new JMenu("File", true);
		menuBar.add(menu);
		
		menuItem = new JMenuItem("Open");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				loadFromFile();
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Save");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveToFile();
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Exit");
		menuItem.setActionCommand("Exit");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		menu.add(menuItem);
		
		// The help menu
		menu = new JMenu("Help");
		menuBar.add(menu);
		
		menuItem = new JMenuItem("About");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				AboutDialog.showModal(SwingFinal.this);
			}
		});
		menu.add(menuItem);
	}

	protected void onFilterAction()
    {
		// Filter the table results
		String text = filterField.getText();
		RowFilter<InventoryTableModel, Object> filter = null;
		
		try
		{
			filter = RowFilter.regexFilter(text);
			
			tableSorter.setRowFilter(filter);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "The filter text appears to contain an error. Please check the syntax.", "Error", JOptionPane.ERROR_MESSAGE);
		}
    }

	protected void onNewItem()
    {
		// Create a new object and add it to the data list
		DataEntry entry = new DataEntry();
		data.addRow(entry);
		// Get its index so we can edit the correct one
		int row = data.findRow(entry);
		// Ask the table to edit the new row
		table.editCellAt(row, 0);
    }
	
	private TextFieldWithPlaceholder filterField;
	private InventoryTableModel dataModel;
	private JTable table;
	private DataInterface data;
	private TableRowSorter<InventoryTableModel> tableSorter;
}
