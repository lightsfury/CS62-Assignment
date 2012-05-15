package cs62.rbeam.swingFinal;

import java.awt.Component;
import java.awt.Frame;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

public class InventoryDataModel
{
	public static class DataEntry
	{
		public int    IDNumber,
		              Quanitity;
		public String Description,
		              Name;
	}

	// Stores an in-memory copy of the item information and exposes it to the JTable
	class DataStorage extends DefaultTableModel implements TableCellRenderer
	{

		public DataStorage(Frame parent)
	    {
			super();
			setColumnIdentifiers(new Object[] {
				"ID Number",
				"Name",
				"Description",
				"Edit",
			});
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
			// Return 0 if the data is empty
			if (data == null)
			{
				return 0;
			}
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
			
			// And make sure there are enough EditButtons for each row of data
			if (data.size() > editButtons.size())
			{
				for (int i = editButtons.size(); i < data.size(); i++)
				{
					editButtons.add(i, new EditButton(parentFrame));
				}
			}
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
	
}
