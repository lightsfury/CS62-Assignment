package cs62.rbeam.swingFinal;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

//Extended text field to automatically present placeholder text while the text field is empty
class TextFieldWithPlaceholder extends JTextField implements FocusListener
{
	private static final long serialVersionUID = 1L;
 
	public TextFieldWithPlaceholder()
	{
		super();
		
		init();
	}
	
	public TextFieldWithPlaceholder(int cols)
	{
		super(cols);
		
		init();
	}
	
	public TextFieldWithPlaceholder(String placeholder)
	{
		super();
		setPlaceholderText(placeholder);
	}
	
	public TextFieldWithPlaceholder(String placeholder, int cols)
	{
		super(cols);
		setPlaceholderText(placeholder);
	}

	@Override
	public void focusGained(FocusEvent arg0)
	{
		// Are we showing the placeholder text?
		if (showingPlaceholderText)
		{
			showingPlaceholderText = false;
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
			showingPlaceholderText = true;
			updateColor();
		}
	}
	
	public void setPlaceholderText(String text)
	{
		placeholderText = new String(text);
	}
	
	public String getPlaceholderText()
	{
		return placeholderText;
	}
	
	public void showPlaceholderText()
	{
		showingPlaceholderText = true;
		updateColor();
	}
	
	public String getText()
	{
		if (showingPlaceholderText)
		{
			return "";
		}
		else
		{
			return super.getText();
		}
	}
	
	private void init()
	{
		// Begin by showing the placeholder text
		showingPlaceholderText = true;
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
		if (showingPlaceholderText)
		{
			setForeground(Color.GRAY);
			setText(placeholderText);
		}
		else
		{
			setForeground(defaultTextFieldForeground);
			setText("");
		}
	}
	
	private String placeholderText;
	private boolean showingPlaceholderText;
	protected Color defaultTextFieldForeground;
}
