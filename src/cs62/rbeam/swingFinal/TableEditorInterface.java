package cs62.rbeam.swingFinal;

import java.awt.Component;
import javax.swing.*;
import cs62.rbeam.swingFinal.DataInterface.DataEntry;
import cs62.rbeam.swingFinal.ItemEditDialog.SubmitListener;

public class TableEditorInterface extends DefaultCellEditor
{
	public TableEditorInterface(ItemEditDialog dialog, DataInterface data)
	{
		super(new JTextField());
		
		editDialog = dialog;
		dataInterface = data;
		
		editDialog.addSubmitListener(new SubmitListener() {
			@Override
			public void dialogSubmit(int rowIndex, DataEntry entry)
			{
				dataInterface.setRow(rowIndex, entry);
				
				fireEditingStopped();
			}
			
			@Override
			public void dialogCancel()
			{
				fireEditingCanceled();
			}
		});
	}
	
	@Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column)
    {
		row = table.convertRowIndexToModel(row);
		
		DataEntry entry = dataInterface.getRow(row);
		editDialog.setDataRow(row, entry);
		editDialog.setActiveTab(column);
		editDialog.setVisible(true);
		
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
	
	@Override
	protected void fireEditingStopped()
	{
		super.fireEditingStopped();
	}
	
	@Override
	protected void fireEditingCanceled()
	{
		super.fireEditingCanceled();
	}
	
	private ItemEditDialog editDialog;
	private DataInterface dataInterface;
}
