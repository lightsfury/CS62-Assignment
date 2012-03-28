/*
 * @author	Robert Beam
 * @date 	3-6-12
 * @title	CS62 Assignment 5: Room reservation application
 * @purpose Reserve a conference or party room
 */

package cs62;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

public class Assignment5
{
	public static void main(String[] args)
	{
		int smokingRooms, nonSmokingRooms;
		
		// Ask the user for the number of smoking and non-smoking rooms
		smokingRooms = GetInteger("Please enter the number of smoking rooms:");
		nonSmokingRooms = GetInteger("Please enter the number of non-smoking rooms:");
		
		// Create the reservations frame and object
		Reservations r = new Reservations(smokingRooms, nonSmokingRooms);
	}

	private static int GetInteger(String message)
	{
		// Forward declarations
		int ret;
		String temp;
		// Infinite loop
		while (true)
		{
			try
			{
				// Show the input dialog
				temp = JOptionPane.showInputDialog(message);
				
				// Attempt to parse the integer
				ret = Integer.parseInt(temp);
				
				if (ret <= 0)
				{
					// If some non-exception happened, force one
					throw new NumberFormatException();
				}
				
				// If everything looks okay, return the integer
				return ret;
			}
			// Tell the user not to do bad things
			catch (NumberFormatException e)
			{
				JOptionPane.showMessageDialog(null, "Please enter a valid integer.");
			}
			catch (NullPointerException e)
			{
				JOptionPane.showMessageDialog(null, "Please enter a valid integer.");	
			}
		}
	}
}

// Read-only JTextArea serves double duty as a Swing component and data storage
class Room extends JTextArea
{
	private static final long serialVersionUID = 1L;
	// Data storage
	boolean smoking;
	boolean available = true;
	int roomIndex;
	ReservationInfo info;
	
	// Constructor
	Room(boolean smoking, int room)
	{
		this.smoking = smoking;
		this.roomIndex = room;
		info = new ReservationInfo();
		UpdateText();
	}
	
	// Helper methods
	public boolean isSmokingRoom()
	{
		return smoking;
	}
	
	public boolean isAvailable()
	{
		return available;
	}
	
	public boolean bookRoom(String name, String phone, int partySize)
	{
		if (!available)
		{
			return false;
		}
		else
		{
			info.name = name;
			info.phoneNumber = phone;
			info.partySize = partySize;
			available = false;
			UpdateText();
			return true;
		}
	}

	// Helper magic
	private void UpdateText()
	{
		setEditable(false);
		// Update the text message and background color
		String text;
		if (!available)
		{
			// Make a pretty string
			text = String.format("Room %d\n%s(%s) Party size: %d\n%s", roomIndex + 1, info.name, info.phoneNumber, info.partySize, smoking ? "Smoking" : "Non-Smoking");
			setBackground(Color.RED);
		}
		else
		{
			text = String.format("Room %d\n%s", roomIndex + 1, smoking ? "Smoking" : "Non-Smoking");
			setBackground(Color.GREEN);
		}
		setText(text);
	}
}

// Data storage class
class ReservationInfo
{
	public String name;
	public String phoneNumber;
	public int partySize;
}

// The meaty frame object
class Reservations extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	// Data storage
	private Room[] rooms;
	private JPanel gridPanel, infoPanel, submitPanel;
	private JLabel nameLabel, phoneLabel, partyLabel, smokingLabel;
	private JTextField nameField, phoneField;
	private JComboBox partyBox;
	private JRadioButton hiddenRadio, nonSmokingRadio, smokingRadio;
	private ButtonGroup radioGroup;
	private JButton submitButton;
	
	// The constructor
	Reservations(int smokingRooms, int nonSmokingRooms)
	{
		// Initialize the parent type
		super("Reserve a party room");
		// Create an array of size `smokingRooms` + `nonSmokingRooms`
		rooms = new Room[smokingRooms + nonSmokingRooms];
		
		// Initialize each room object with a smoking flag and the capacity
		for (int i = 0; i <  smokingRooms + nonSmokingRooms; i++)
		{
			rooms[i] = new Room(i < smokingRooms, i);
		}

		// Create the visual elements
		init();
		
		// Make sure the frame is visible, adjust its size and make sure the program exits when the frame closes
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// Visual element creation
	public void init()
	{
		// Because the damn thing doesn't play well with a FlowLayout
		setLayout(new BorderLayout());
		
		// Initialize the upper grid panel
		gridPanel = new JPanel(new GridLayout(2, 4, 5, 5));
		
		// Allow space between the text areas
		gridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		for (Room room : rooms)
		{
			// Make sure everything is sufficiently large and add them to the grid
			room.setPreferredSize(new Dimension(50, 50));
			gridPanel.add(room, room.roomIndex);
		}

		// Initialize the information input panel
		infoPanel = new JPanel(new FlowLayout());
		
		// Create the helper labels
		nameLabel = new JLabel("Name:");
		phoneLabel = new JLabel("Phone #:");
		partyLabel = new JLabel("Number in party:");
		smokingLabel = new JLabel("Smoking preference:");
		
		// Create the text input fields
		nameField = new JTextField(20);
		phoneField = new JTextField(12);
		
		// Create a *party* box
		Vector<String> items = new Vector<String>();
		for (int i = 8; i <= 20; i++)
		{
			items.add(String.valueOf(i));
		}
		
		partyBox = new JComboBox(items);
		
		// Create the radio buttons
		hiddenRadio = new JRadioButton("");
		hiddenRadio.setVisible(false);
		hiddenRadio.setSelected(true);
		
		smokingRadio = new JRadioButton("Smoking");
		
		nonSmokingRadio = new JRadioButton("Non-Smoking");
		
		// Create the radio group and add the radio buttons
		radioGroup = new ButtonGroup();
		radioGroup.add(hiddenRadio);
		radioGroup.add(smokingRadio);
		radioGroup.add(nonSmokingRadio);
		
		// Add the objects to the info panel
		infoPanel.add(nameLabel);
		infoPanel.add(nameField);
		infoPanel.add(phoneLabel);
		infoPanel.add(phoneField);
		infoPanel.add(partyLabel);
		infoPanel.add(partyBox);
		infoPanel.add(smokingLabel);
		infoPanel.add(smokingRadio);
		infoPanel.add(nonSmokingRadio);

		// Create the submit panel
		submitPanel = new JPanel();
		
		submitButton = new JButton("Book room");
		submitButton.addActionListener(this);
		
		submitPanel.add(submitButton);
		
		add(gridPanel, BorderLayout.NORTH);
		add(infoPanel, BorderLayout.CENTER);
		add(submitPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		// Pre-creation sanity
		if (gridPanel != null)
		{
			// Variable declaration
			String name, phone;
			// Make sure the fields have values
			if ((name = nameField.getText()) == "")
			{
				// Tell the user if not
				JOptionPane.showMessageDialog(null, "Please enter the client's name");
				return;
			}
			
			if ((phone = phoneField.getText()) == "")
			{
				JOptionPane.showMessageDialog(null, "Please enter the client's phone number");
				return;
			}
			
			if (hiddenRadio.isSelected())
			{
				JOptionPane.showMessageDialog(null, "Please select the client's smoking preference");
				return;
			}
			
			// Calculate the party size
			int partySize = partyBox.getSelectedIndex() + 8;
			
			// Place the reservation
			bookRoom(name, phone, smokingRadio.isSelected(), partySize);
		}
		else
		{
			System.out.println("Grid panel is null");
		}
		
	}

	private void bookRoom(String name, String phone, boolean smoking, int partySize)
	{
		boolean booked = false;
		// Iterate over the rooms
		for (Room r : rooms)
		{
			// To find one that is available and meets the client's smoking preference
			if (r.isAvailable() && (r.isSmokingRoom() == smoking))
			{
				// Then book the room
				r.bookRoom(name, phone, partySize);
				booked = true;
				break;
			}
		}
		if (!booked)
		{
			// Show a message alerting the user with the Reservations frame as the alert's parent or origin frame
			JOptionPane.showMessageDialog(this, "Could not find an available room that meet the client's preferences.");
		}
	}
}
