/*
 * @author	Robert Beam
 * @date 	2-21-12
 * @title	CS62 Extra Credit 2: Speeding ticket calculator.
 * @purpose Assess students' ability to follow a basic application request without assistance.
 */

package cs62.rbeam;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class ExtraCredit2 extends JApplet implements KeyListener
{
	// Setup the various static and member variables
	private static final long serialVersionUID = 1L;
	
	private JTextField speedLimit, speedCaught, offenceNum;
	
	private JLabel speedLimitText, speedCaughtText, offenceNumText, outputText;
	
	// BTW, tickets are expensive. M'kay
	static private double courtFeeBase = 74.80, courtFeeScale = 25.00, speedDeltaFeeScale = 20.00;
	
	public void init()
	{
		// Initialize the GUI elements
		speedLimit = new JTextField(4);
		speedCaught = new JTextField(4);
		offenceNum = new JTextField(2);
		
		speedLimitText = new JLabel("Speed limit at place of incidence:");
		speedCaughtText = new JLabel("Speed recorded:");
		offenceNumText = new JLabel("Number of prior offences:");
		
		outputText = new JLabel("Please enter values into the above fields");
		
		// Add them to the content pane
		add(speedLimitText);
		add(speedLimit);
		add(speedCaughtText);
		add(speedCaught);
		add(offenceNumText);
		add(offenceNum);
		add(outputText);

		// Make sure the content pane is visible
		setVisible(true);
		
		// Set a built-in layout
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// Set a constant size
		setSize(365, 200);
		
		// Add input listeners
		speedLimit.addKeyListener(this);
		speedCaught.addKeyListener(this);
		offenceNum.addKeyListener(this);
		
		try
		{
			// Try to adopt the OS's look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		}
		catch (Exception e)
		{
		}

	}

	private void CalculateTicketCost()
	{
		try
		{
			int limit, caught, offence;
			// Parse the input texts
			limit = Integer.parseInt(speedLimit.getText());
			caught = Integer.parseInt(speedCaught.getText());
			offence = Integer.parseInt(offenceNum.getText());
			
			// Sanity checks. Don't want to give people money for not speeding.
			if (caught < limit)
			{
				outputText.setText("Speed caught less than speed limit! Please adjust.");
				return;
			}
			
			// Calculate the intermediary values
			int speedDelta = caught - limit;
			double courtCost = courtFeeBase + Math.min(3 * courtFeeScale, courtFeeScale * offence);
			double totalCost = courtCost + (speedDelta * speedDeltaFeeScale);
			
			// Display the cost
			outputText.setText(String.format("Cost of ticket: $%,.2f", totalCost));
		}
		catch (NumberFormatException e)
		{
			outputText.setText("Please enter values to the nearest whole number");
		}
	}

	// Various KeyListener methods to calculate the cost when the user edits the text fields
	@Override
	public void keyPressed(KeyEvent e)
	{
		CalculateTicketCost();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		CalculateTicketCost();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		CalculateTicketCost();
	}

}
