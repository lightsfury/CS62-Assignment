package cs62.rbeam.swingFinal;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

//Extended text field to automatically present placeholder text while the text field is empty
class FadeField extends JTextField implements FocusListener
{
	private static final long serialVersionUID = 1L;
 
	public FadeField()
	{
		super();
		
		init();
	}
	
	public FadeField(int cols)
	{
		super(cols);
		
		init();
	}

	@Override
	public void focusGained(FocusEvent arg0)
	{
		// Are we showing the placeholder text?
		if (showPlaceHolderText)
		{
			showPlaceHolderText = false;
			updateColor();
		}
	}

	@Override
	public void focusLost(FocusEvent arg0)
	{
		// Get the current string
		String text = getText();
		// Do we have an empty string?
		if (text == null || text.length() <= 0)
		{
			showPlaceHolderText = true;
			updateColor();
		}
	}
	
	public void clearText()
	{
		// Because sometimes we just want to nuke the text and show the placeholder
		showPlaceHolderText = true;
		updateColor();
	}
	
	private void init()
	{
		// Begin by showing the placeholder text
		showPlaceHolderText = true;
		// Make sure to get the previous forground color
		defaultTextFieldForeground = getForeground();
		// Set the new color
		updateColor();
		// And add a listener for focus changes
		addFocusListener(this);
	}
	
	private void updateColor()
	{
		// Centralized place to switch out the placeholder text
		if (showPlaceHolderText)
		{
			setForeground(Color.GRAY);
			setText("Filter");
		}
		else
		{
			setForeground(defaultTextFieldForeground);
			setText("");
		}
	}
	
	private boolean showPlaceHolderText;
	protected Color defaultTextFieldForeground;
}
