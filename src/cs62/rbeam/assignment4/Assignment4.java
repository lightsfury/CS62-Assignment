/*
 * @author	Robert Beam
 * @date 	2-21-12
 * @title	CS62 Assignment 4: Sale commission calculator
 * @purpose Introduce students to the use of methods in Java.
 */


package cs62.rbeam.assignment4;

import java.awt.HeadlessException;
import java.security.InvalidParameterException;
import javax.swing.JOptionPane;
import java.util.regex.*;

public class Assignment4
{
	enum LocationCode
	{
		Telephone,
		InStore,
		Outside
	}
	
	public static double GetCommissionRate(LocationCode locationCode) throws InvalidParameterException
	{
		// Switch statement on the LocationCode enumeration
		switch(locationCode)
		{
		case Telephone:
			return 0.10;
		case InStore:
			return 0.14;
		case Outside:
			return 0.18;
		default:
			throw new InvalidParameterException();
		}
	}
	
	public static LocationCode GetSaleLocation() throws HeadlessException
	{
		// Forward declaration of variables
		boolean done = false;
		LocationCode selectedCode = LocationCode.Outside;
		// Use an array to pass options to the choice dialog
		LocationCode[] codes = new LocationCode[] {LocationCode.Telephone, LocationCode.InStore, LocationCode.Outside};
		// Loop this block
		while (!done)
		{
			// Show a choice dialog
			int choice = JOptionPane.showOptionDialog(null, "Please select the sales location", "Sales location",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, codes, codes[0]);
			// Validate the choice
			if (choice >= 0 && choice < codes.length)
			{
				// Signal the loop to end
				done = true;
				selectedCode = codes[choice];
			}
			else
			{
				// Display an error message
				JOptionPane.showMessageDialog(null, "Please select a valid sales location.");
			}
		}
		return selectedCode;
	}
	
	public static double GetSalesAmount()
	{
		// Forward variable declarations
		boolean done = false;
		double sales = 0.0;
		String temp;
		// DecimalFormat is too limiting. Use a RegEx instead
		/*
		 * ^: Match the beginning of the string
		 * \\$?: Match an optional Dollar sign
		 * ([\\d]+: Match a series of 1 or more digits
		 *     (\\.\\d{2})?: Match an optional decimal and 2 digit sequence 
		 * ): Grouping for the entire numeric portion
		 */
		Pattern pattern = Pattern.compile("^\\$?([\\d]+(\\.\\d{2})?)");
		Matcher matcher;
		// Loop the block
		while (!done)
		{
			// Show an input dialog
			temp = JOptionPane.showInputDialog("Please enter the sales amount:");
			// Remove any commas from the input text
			temp = temp.replaceAll(",", "");
			// Run the input through the RegEx engine
			matcher = pattern.matcher(temp);
			if (matcher.find())
			{
				// Grab the 1st grouping and parse the value
				temp = matcher.group(1);
				sales = Double.parseDouble(temp);
				done = true;
			}
			else
			{
				// Notify the user of an invalid input
				JOptionPane.showMessageDialog(null, "Please enter a valid sales amount.");
			}
		}
		return sales;
	}

	public static double CalculateCommission(double sales, LocationCode saleLocation)
	{
		// Get the commission rate and calculate the commission
		double commissionRate = GetCommissionRate(saleLocation);
		return sales * commissionRate;
	}

	public static void AlertCommission(double sales, double commission)
	{
		// Show the alert message using comma delimited decimal numbers with 2 trailing digits
		JOptionPane.showMessageDialog(null, String.format("The commission on $%,.2f is $%,.2f.", sales, commission));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Forward variable declaration
		double sales, commission;
		LocationCode saleLocation;
		
		// Call static methods
		sales = GetSalesAmount();
		saleLocation = GetSaleLocation();
		commission = CalculateCommission(sales, saleLocation);
		AlertCommission(sales, commission);
	}

}
