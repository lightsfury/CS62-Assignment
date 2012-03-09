package cs62;

import java.awt.*;
import javax.swing.*;

public class Lecture3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws HeadlessException
	{
		String name = JOptionPane.showInputDialog("What is your name?");
		JOptionPane.showMessageDialog(null, String.format("Hello, %s!", name));
	}

}
