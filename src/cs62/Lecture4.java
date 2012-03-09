package cs62;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Lecture4 extends Applet implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	double inch, lbs, meters, kg, index;
	Label companyLabel, heightLabel, weightLabel, outputLabel;
	TextField heightField, weightField;
	Button calcButton;
	
	public void init()
	{
		companyLabel = new Label("The Sun Fitness Center body mass index calculator");
		heightLabel = new Label("Enter the client's height: ");
		weightLabel = new Label("Enter the client's weight: ");
		outputLabel = new Label("Click the Calculate button to calculate the client's body mass index");
		heightField = new TextField(10);
		weightField = new TextField(10);
		calcButton = new Button("Calculate");

		setForeground(Color.red);
		add(companyLabel);
		add(heightLabel);
		add(heightField);
		add(weightLabel);
		add(weightField);
		add(calcButton);
		calcButton.addActionListener(this);
		add(outputLabel);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		inch = Double.parseDouble(heightField.getText());
		lbs = Double.parseDouble(weightField.getText());
		meters = inch / 39.36;
		kg = lbs / 2.2;
		double bmi = kg / Math.pow(meters, 2);
		outputLabel.setText(String.format("The client's BMI is %.1f", bmi));
	}
}
