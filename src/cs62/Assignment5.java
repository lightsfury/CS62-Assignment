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
		
		smokingRooms = GetInteger("Please enter the number of smoking rooms:");
		
		nonSmokingRooms = GetInteger("Please enter the number of non-smoking rooms:");
		
		System.out.println("Creating Reservations object");
		Reservations r = new Reservations(smokingRooms, nonSmokingRooms);
	}

	private static int GetInteger(String message)
	{
		int ret;
		String temp;
		while (true)
		{
			try
			{
				temp = JOptionPane.showInputDialog(message);
				
				ret = Integer.parseInt(temp);
				
				if (ret <= 0)
				{
					throw new NumberFormatException();
				}
				
				return ret;
			}
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

class Room extends JTextArea
{
	private static final long serialVersionUID = 1L;
	boolean smoking;
	boolean available = true;
	int roomIndex;
	ReservationInfo info;
	
	Room(boolean smoking, int room)
	{
		System.out.println(String.format("Creating Room object %d", room));
		this.smoking = smoking;
		this.roomIndex = room;
		info = new ReservationInfo();
		UpdateText();
	}
	
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

	private void UpdateText()
	{
		setEditable(false);
		// Update the text message and background color
		String text;
		if (!available)
		{
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

class ReservationInfo
{
	public String name;
	public String phoneNumber;
	public int partySize;
}

class Reservations extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Room[] rooms;
	private JPanel gridPanel, infoPanel, submitPanel;
	private JLabel nameLabel, phoneLabel, partyLabel, smokingLabel;
	private JTextField nameField, phoneField;
	private JComboBox partyBox;
	private JRadioButton hiddenRadio, nonSmokingRadio, smokingRadio;
	private ButtonGroup radioGroup;
	private JButton submitButton;
	
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

		init();
		
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void init()
	{
		setLayout(new BorderLayout());
		
		System.out.println("Creating grid panel");
		// Initialize the upper grid panel
		gridPanel = new JPanel(new GridLayout(2, 4, 5, 5));
		
		gridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		for (Room room : rooms)
		{
			room.setPreferredSize(new Dimension(50, 50));
			gridPanel.add(room, room.roomIndex);
		}

		System.out.println("Creating info panel");
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

		System.out.println("Creating submit panel");
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
		System.out.println("Book room clicked");
		if (gridPanel != null)
		{
			String name, phone;
			if ((name = nameField.getText()) == "")
			{
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
			
			int partySize = partyBox.getSelectedIndex() + 8;
			
			BookRoom(name, phone, smokingRadio.isSelected(), partySize);
			
			//System.out.println(String.format("Grid panel bounds: %dx%d", gridPanel.getBounds().height, gridPanel.getBounds().width));
			//System.out.println(String.format("Grid panel # of children: %d", gridPanel.getComponents().length));
		}
		else
		{
			System.out.println("Grid panel is null");
		}
		// TODO Auto-generated method stub
		
	}

	private void BookRoom(String name, String phone, boolean smoking, int partySize)
	{
		for (Room r : rooms)
		{
			if (r.isAvailable() && r.isSmokingRoom() == smoking)
			{
				r.bookRoom(name, phone, partySize);
				break;
			}
		}
	}
}
