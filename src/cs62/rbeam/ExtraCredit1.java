/*
 * @author	Robert Beam
 * @date 	2-7-12
 * @title	CS62 Extra Credit 1: Income:Debt ratio calculator.
 * @purpose Assess students' ability to follow textual instructions.
 */

package cs62.rbeam;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;

public class ExtraCredit1 {
	public static void main(String[] args) throws HeadlessException
	{
		// Forward variable declarations
		double monthIncome, monthRent, monthCarLoan, monthMiscDebt, debtIncomeRatio;
		String inputData;
		
		// Welcome message
		JOptionPane.showMessageDialog(null, "Welcome to the Debt:Income Ratio calculator");
		
		inputData = JOptionPane.showInputDialog("Please enter the household monthly income");
		// Retrieve and convert the monthly income
		monthIncome = Double.parseDouble(inputData);
		
		inputData = JOptionPane.showInputDialog("Please enter the household monthly rent or mortgage cost:");
		// Retrieve and convert the monthly housing cost
		monthRent = Double.parseDouble(inputData);
		
		inputData = JOptionPane.showInputDialog("Please enter the household monthly auto loan or lease cost:");
		// Retrieve and convert the monthly vehicle cost
		monthCarLoan = Double.parseDouble(inputData);
		
		inputData = JOptionPane.showInputDialog("Please enter the household miscellaneous debt monthly cost (credit cards, student loans, etc):");
		// Retrieve the convert the monthly misc debt
		monthMiscDebt = Double.parseDouble(inputData);
		
		double totalDebt = monthRent + monthCarLoan + monthMiscDebt;
		// debt:income ratio = sum(housing cost, vehicle cost, misc debt) / income
		debtIncomeRatio = totalDebt / monthIncome;
		
		JOptionPane.showMessageDialog(null, String.format("The household debt:income ratio is $%.2f of debt per $1.00 of income.", debtIncomeRatio));
		
	}

}
