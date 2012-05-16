package cs62.rbeam.swingFinal;

import javax.swing.table.*;

import cs62.rbeam.swingFinal.DataInterface.DataEntry;

public class InventoryTableModel extends AbstractTableModel
{
	public InventoryTableModel(DataInterface data)
	{
		super();
		
		dataInterface = data;
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public int getRowCount()
	{
		return dataInterface.getRowCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		DataEntry entry = dataInterface.getRow(rowIndex);
		
		switch (columnIndex)
		{
		case 0:
			return entry.IDNumber;
		case 1:
			return entry.Quantity;
		case 2:
			return entry.Name;
		case 3:
			return entry.Description;
		case 4:
			return "Click to view the image";
		default:
			return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return Object.class;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		return columnNames[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
		DataEntry entry = dataInterface.getRow(rowIndex);
		
		switch (columnIndex)
		{
		case 0:
			entry.IDNumber = (Integer)(value);
		case 1:
			entry.Quantity = (Integer)(value);
		case 2:
			entry.Name = (String)(value);
		case 3:
			entry.Description = (String)(value);
		default:
		case 4:
			// This is done by the ItemEditDialog onFinished handler
			break;
		}
		
		entry.IsDirty = true;
	}
	
	private DataInterface dataInterface;
	
	static private String[] columnNames = {
		"ID #", "Quantity", "Name", "Description", "Image"
	};
}
