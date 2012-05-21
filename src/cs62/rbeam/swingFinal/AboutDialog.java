package cs62.rbeam.swingFinal;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AboutDialog extends JDialog
{
	public AboutDialog(Frame parent)
	{
		super(parent);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		init();
		
		pack();
		setVisible(true);
	}
	
	private void init()
	{
		setLayout(new FlowLayout());
		add(new JLabel("Author: Robert Beam"));
		add(new JLabel("Class: CIS 62"));
		add(new JLabel("Date: 5/18/12"));
		add(new JLabel("Purpose: Create an inventory management system"));
		JButton button = new JButton("Close");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(false);
				dispose();
			}
		});
		add(button);
	}
	
	public static void showModal(Frame parent)
	{
		new AboutDialog(parent);
	}
}
