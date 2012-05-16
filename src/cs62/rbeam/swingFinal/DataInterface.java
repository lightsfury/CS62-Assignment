package cs62.rbeam.swingFinal;

import java.util.*;

public class DataInterface
{
	public static class DataEntry
	{
		public int    IDNumber,
		              Quantity;
		public String Name,
		              Description;
		public byte[] ImageData;
		// Flags to limit to number of database queries
		public boolean IsDirty;
		public boolean IsNew;
	}
	
	public DataInterface()
	{
		dataRows = new Vector<DataEntry>(5);
	}
	
	public void loadFromDatabase()
	{
		
	}
	
	public void sateToDatabase()
	{
		
	}
	
	public void loadFromFile()
	{
		
	}
	
	public void saveToFile()
	{
		
	}

	public int getRowCount()
	{
		return dataRows.size();
	}
	
	public DataEntry getRow(int rowIndex)
	{
		return dataRows.get(rowIndex);
	}
	
	public void addRow(DataEntry entry)
	{
		dataRows.add(entry);
		entry.IsNew = true;
	}
	
	public void setRow(int rowIndex, DataEntry entry)
	{
		dataRows.set(rowIndex, entry);
		entry.IsDirty = true;
	}
	
	public void insertRow(int rowIndex, DataEntry entry)
	{
		dataRows.insertElementAt(entry, rowIndex);
		entry.IsNew = true;
	}
	
	private Vector<DataEntry> dataRows;
}

/*
 * InputStream in = new ByteArrayInputStream(imageInByte);
BufferedImage bImageFromConvert = ImageIO.read(in);
The following example will read an image file named “darksouls.jpg“, convert it into byte array, and then reuse the converted byte array, and convert it back to a new BufferedImage, and save it back into a new name “new-darksouls.jpg“.

package com.mkyong.image;
 
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
 
public class ImageTest {
 
	public static void main(String[] args) {
 
		try {
 
			byte[] imageInByte;
			BufferedImage originalImage = ImageIO.read(new File(
					"c:/darksouls.jpg"));
 
			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
 
			// convert byte array back to BufferedImage
			InputStream in = new ByteArrayInputStream(imageInByte);
			BufferedImage bImageFromConvert = ImageIO.read(in);
 
			ImageIO.write(bImageFromConvert, "jpg", new File(
					"c:/new-darksouls.jpg"));
 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}

// */
