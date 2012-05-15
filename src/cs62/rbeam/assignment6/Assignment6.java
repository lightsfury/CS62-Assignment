/*
 * @author	Robert Beam
 * @date 	4-3-12
 * @title	CS62 Assignment 6: Telephone keypad
 * @purpose Enter and dial a telephone number
 */

package cs62.rbeam.assignment6;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;

public class Assignment6
{
	public static void main(String[] args)
	{
		// Create the frame object
		new TelephonePad();
	}
}

// Helper class for Button clicks -> character value mapping
class TelephonePadButton extends JButton
{
	// Variable declarations
	private static final long serialVersionUID = 1L;
	String value;
	
	// Button constructor
	TelephonePadButton(String value)
	{
		super(value);
		this.value = value;
	}
	
	// Helper function used to get the button's value
	String getValue()
	{
		return value;
	}
}

class TelephonePad extends JFrame implements ActionListener
{
	// Variable declarations
	private static final long serialVersionUID = 1L;
	JPanel buttonGrid;
	JTextField phoneNumberField;
	Vector<TelephonePadButton> phonePadButtons;
	JLabel statusLabel;
	
	// Constructor
	TelephonePad()
	{
		super("Telephone");
		
		// Create the visual elements
		init();
		
		// Make sure the frame is visible, set its size and exit the program when the frame is closed
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	void init()
	{
		// Use a border layout with 5 pixel spacing
		setLayout(new BorderLayout(5, 5));
		
		// Create the phone number field
		phoneNumberField = new JTextField();
		phoneNumberField.setEditable(false);
		add(phoneNumberField, BorderLayout.NORTH);
		
		// Create the phone button grid
		buttonGrid = new JPanel(new GridLayout(4, 3, 5, 5));
		add(buttonGrid, BorderLayout.CENTER);
		
		// Create the individual buttons
		phonePadButtons = new Vector<TelephonePadButton>();
		for (String s : ("123456789*0#").split(""))
		{
			// Sanity? check. .split includes a leading empty string that DOES NOT == "" but DOES have a .length == 0
			if (s != null && s.length() > 0)
			{
				// Give it the proper text, set an action listener and add it to the grid
				TelephonePadButton button = new TelephonePadButton(s);
				button.addActionListener(this);
				phonePadButtons.add(button);
				buttonGrid.add(button);
			}
		}
		
		// Add a bit of text for the user
		statusLabel = new JLabel("Click each button above to dial your number");
		add(statusLabel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// Get the source and try to cast it to our button type
		TelephonePadButton button = (TelephonePadButton)(e.getSource());
		if (button != null)
		{
			// If it works, get the button's value and add it to the text field
			String s = button.getValue();
			String number = phoneNumberField.getText();
			phoneNumberField.setText(number + s); // Why doesn't JTextField have append or insert?
		}
		else
		{
			// Sanity? popup
			JOptionPane.showMessageDialog(this, String.format("How did you click this button?\n%s", e.getActionCommand()));
		}
	}
}
