/*
 * @author	Robert Beam
 * @date 	5-1-12
 * @title	CS62 Assignment 7: Bibliography application
 * @purpose Automate the creation of a bibliography with proper formatting
 */

package cs62.rbeam.assignment7;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.*;

public class Assignment7 extends JFrame
{
	JTabbedPane tabs;
	JPanel previewTab, editTab;
	JTextPane previewDocument;
	JButton editSubmit;
	JTextField authorInput, titleInput, publisherInput, yearInput, pagesInput;
	
	ArrayList<BookDetails> data;
	
	// Storage helper class
	private class BookDetails
	{
		public String Author, Title, Publisher, Year, Pages;
	}
	
	public Assignment7()
	{
		// Allow our own close handler
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		data = new ArrayList<BookDetails>(5);
		
		init();
		
		// Setup the window size
		setSize(800, 600);
		setVisible(true);
	}
	
	protected void init()
	{
		// Set a layout for the main window
		setLayout(new BorderLayout());
		
		// Create a window with tabs for the two panels
		tabs = new JTabbedPane();
		add(tabs, BorderLayout.CENTER);
		
		// Setup the viewing panel
		previewTab = new JPanel(new BorderLayout());
		previewTab.add(new JLabel("Add books, resources and references on the Edit tab."), BorderLayout.NORTH);
		previewDocument = new JTextPane();
		previewDocument.setEditable(false);
		previewTab.add(previewDocument, BorderLayout.CENTER);
		
		tabs.add(previewTab, "Preview");
		
		// Setup the edit tab and the various elements
		// So much layout hackery that its no longer funny
		editTab = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel editPanel = new JPanel();
		editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.PAGE_AXIS));
		editTab.add(editPanel);
		// Use throwaway variable for things we don't need to keep track of
		JLabel text;
		JPanel pane;
		
		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		text = new JLabel("Author:");
		authorInput = new JTextField(10);
		
		pane.add(text);
		pane.add(authorInput);
		
		editPanel.add(pane);

		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		text = new JLabel("Title:");
		titleInput = new JTextField(10);
		
		pane.add(text);
		pane.add(titleInput);
		
		editPanel.add(pane);

		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		text = new JLabel("Publisher:");
		publisherInput = new JTextField(10);
		
		pane.add(text);
		pane.add(publisherInput);
		
		editPanel.add(pane);

		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		text = new JLabel("Year of publication:");
		yearInput = new JTextField(10);
		
		pane.add(text);
		pane.add(yearInput);
		
		editPanel.add(pane);

		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		text = new JLabel("Pages:");
		pagesInput = new JTextField(10);
		
		pane.add(text);
		pane.add(pagesInput);
		
		editPanel.add(pane);
		
		editSubmit = new JButton("Add entry");
		editSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Assignment7.this.onEditSubmit();
			}
		});
		editPanel.add(editSubmit);
		
		tabs.add(editTab, "Edit");
		
		// Add a custom window closing handler
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				Assignment7.this.onClose();
			}
		});
		
		setupPreviewDocument();
	}
	
	protected void onEditSubmit()
	{
		BookDetails book = new BookDetails();
		book.Author = authorInput.getText();
		book.Title = titleInput.getText();
		book.Publisher = publisherInput.getText();
		book.Year = yearInput.getText();
		book.Pages = pagesInput.getText();

		authorInput.setText("");
		titleInput.setText("");
		publisherInput.setText("");
		yearInput.setText("");
		pagesInput.setText("");
		
		data.add(book);
		
		updatePreview();
	}

	private void setupPreviewDocument()
	{
		// Setup how tabs look
		TabStop[] tabStops = {
				new TabStop(200, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE),
				new TabStop(350, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE)
		};
		TabSet tabSet = new TabSet(tabStops);
		
		// Define how tabs look
		StyleContext tabStyle = StyleContext.getDefaultStyleContext();
		AttributeSet attrSet = tabStyle.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabSet);
		previewDocument.setParagraphAttributes(attrSet, false);
		
		Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		
		Style normal = previewDocument.addStyle("normal", defaultStyle);
		
		Style s = previewDocument.addStyle("italic", normal);
		StyleConstants.setItalic(s, true);
		
		s = previewDocument.addStyle("underline", normal);
		StyleConstants.setUnderline(s, true);
		
		s = previewDocument.addStyle("header", normal);
		StyleConstants.setFontSize(s, 16);
		
		updatePreview();
	}

	protected void updatePreview()
	{
		try
		{
			// Get the formatted document and clear it
			Document doc = previewDocument.getDocument();
			doc.remove(0, doc.getLength());
			
			// Add the header
			doc.insertString(0, "MLA format\n\n", previewDocument.getStyle("header"));
			
			// Iterate over the books and add each one in MLA format
			for (BookDetails book : data)
			{
				doc.insertString(doc.getLength(),
						String.format("%s. \"%s.\" %s. %s. %s.\n", book.Author, book.Title, book.Publisher, book.Year, book.Pages),
						previewDocument.getStyle("normal"));
			}
			
			// If there aren't any books, tell the user
			if (data.size() == 0)
			{
				doc.insertString(doc.getLength(), "You do not have any books listed.", previewDocument.getStyle("normal"));
			}
		}
		catch (BadLocationException e)
		{
			// Fan, tell the user
			System.out.println("Error with the previewDocument");
			e.printStackTrace();
		}
	}

	protected void onClose()
	{
		int opt = JOptionPane.showConfirmDialog(this, "This work is not saved. Do you want to save it before exiting?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
		if (opt != JOptionPane.CANCEL_OPTION)
		{
			if (opt == JOptionPane.YES_OPTION)
			{
				//! @todo Save the data here
			}
			
			System.exit(0);
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
			// Use the OS native graphics theme
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new Assignment7();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
