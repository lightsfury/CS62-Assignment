/*
 * @author	Robert Beam
 * @date 	2-7-12
 * @title	CS62 Assignment 1: Welcome to My Day
 * @purpose Introduce the Swing dialog components of Java.
 */

package cs62.rbeam.assignment3;

import javax.swing.*;

public class Assignment3 {
	public static void main(String[] args)
	{
		// Forward variable declarations
		int heightInch, weightLbs;
		double heightMeter, weightKg;
		String inputData;
		
		// Introductory dialog
		JOptionPane.showMessageDialog(null, "Welcome to the Sun Fitness BMI Calculator");
		
		// Get the client's height in inches
		inputData = JOptionPane.showInputDialog("Enter the client's height to the nearest inch:");
		// Convert the input data to inches
		heightInch = Integer.parseInt(inputData);
		// 1 meter = 39.36 inches
		heightMeter = (double)(heightInch) / 39.36;
		
		// Get the client's weight in lbs
		inputData = JOptionPane.showInputDialog("Enter the client's weight to the nearest pound:");
		// Convert the input data to pounds
		weightLbs = Integer.parseInt(inputData);
		// 1 kg = 2.2 lbs
		weightKg = (double)(weightLbs) / 2.2;
		// bmi = mass / height ^ 2
		double bmi = weightKg / Math.pow(heightMeter, 2);
		
		JOptionPane.showMessageDialog(null, String.format("The client's Body Mass Index is %.0f.", bmi));
	}

}
