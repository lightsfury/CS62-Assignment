package cs62.rbeam.swingFinal;

import java.io.*;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.*;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataInterface
{
	public static class DataEntry implements JSONAware
	{
		public int     IDNumber = 0,
		               Quantity = 0;
		public String  Name = "",
		               Description = "";
		public byte[]  ImageData = null;
		public String  ImageMIME = "image/*"; // If needed
		public boolean GenerateID = true;
		
		@Override
        public String toJSONString()
        {
			// Trickery to encode the class in a predictable manner
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("idNumber", new Integer(IDNumber));
			data.put("quantity", new Integer(Quantity));
			data.put("name", new String(Name));
			data.put("desc", new String(Description));
			if (ImageData != null)
			{
				data.put("imageData", Base64.encodeBase64String(ImageData));
				data.put("imageType", new String(ImageMIME));
			}
			data.put("generatedID", new Boolean(GenerateID));
			return JSONObject.toJSONString(data);
        }
		
		public static DataEntry fromJSONObject(Map<String, Object> data) throws Exception
		{
			DataEntry entry = new DataEntry();
			Number num;
			String str;
			byte[] bytes;
			Boolean bool;
			
			// Check the various possible data entries
			
			if (data.containsKey("idNumber"))
			{
				if ((num = (Number)(data.get("idNumber"))) != null)
				{
					entry.IDNumber = num.intValue();
				}
				else
				{
					throw new Exception("JSON entry contains a non-number field `idNumber`");
				}
			}
			
			if (data.containsKey("quantity"))
			{
				if ((num = (Number)(data.get("quantity"))) != null)
				{
					entry.Quantity = num.intValue();
				}
				else
				{
					throw new Exception("JSON entry contains a non-number field `quantity`");
				}
			}
			
			if (data.containsKey("name"))
			{
				if ((str = (String)(data.get("name"))) != null)
				{
					entry.Name = new String(str); 
				}
				else
				{
					throw new Exception("JSON entry contains a non-string field `name`");
				}
			}
			
			if (data.containsKey("desc"))
			{
				if ((str = (String)(data.get("desc"))) != null)
				{
					entry.Description = new String(str); 
				}
				else
				{
					throw new Exception("JSON entry contains a non-string field `desc`");
				}
			}
			
			if (data.containsKey("imageData"))
			{
				if ((str = (String)(data.get("imageData"))) != null)
				{
					bytes = Base64.decodeBase64(str);
					entry.ImageData = bytes;
					
					if (data.containsKey("imageType") && ((str = (String)(data.get("imageType"))) != null))
					{
						entry.ImageMIME = new String(str);
					}
					else
					{
						// Do we really need to throw if we don't have a MIME-type?
						throw new Exception("JSON entry contains a non-string or missing field `imageType`");
					}
				}
				else
				{
					throw new Exception("JSON entry contains a non-string field `imageData`");
				}
			}
			
			if (data.containsKey("generatedID"))
			{
				if ((bool = (Boolean)(data.get("generatedID"))) != null)
				{
					entry.GenerateID = bool.booleanValue();
				}
				else
				{
					throw new Exception("JSON entry contains a non-boolean field `generatedID`");
				}
			}
			
			return entry;
		}
	}
	
	// Helper class that creates the proper storage types for JSON entries
	protected class JSONContainerFactory implements ContainerFactory
	{
		@Override
        public List creatArrayContainer()
        {
			return new ArrayList<Object>();
        }

		@Override
        public Map createObjectContainer()
        {
			return new HashMap<String, Object>();
        }
	}
	
	// Cannon-Listener pattern for data updates
	public interface DataChangedEventListener
	{
		public void onDataChanged();
	}
	
	public DataInterface()
	{
		dataRows = new Vector<DataEntry>(5);
	}
	
	// Cannon-Listener contract methods
	public void addDataChangedEventListener(DataChangedEventListener l)
	{
		if (listeners == null)
		{
			listeners = new Vector<DataChangedEventListener>(5);
		}
		
		listeners.add(l);
	}
	
	public void removeDataChangedEventListener(DataChangedEventListener l)
	{
		if (listeners != null)
		{
			listeners.remove(l);
		}
	}
	
	protected void fireDataChangedEventListeners()
	{
		if (listeners != null)
		{
			for (DataChangedEventListener l : listeners)
			{
				l.onDataChanged();
			}
		}
	}
	
	public void loadFromFile(String path) throws Exception
	{
		// Create a new parser object
		JSONParser parser = new JSONParser();
		// Pre-declare the variable to it exists beyond the try scope
		Object data;
		try
		{
			// Wrap this in a try block so we can post a more specific console message
    		data = parser.parse(new FileReader(path), new JSONContainerFactory());
		}
		catch (Exception e)
		{
			System.out.println(String.format("DataInterface.loadFromFile: Unable to parse the JSON file.\nMessage: %s.\nTrace:\n", e.getMessage()));
			e.printStackTrace();
			throw new Exception("The file is not of the correct format");
		}
		
		// Eclipse moans about an "unchecked cast" except that we throw if the cast didn't work ...
		List list = (List)(data);
		if (list == null)
		{
			// If we didn't get an array/List, then scream
			throw new Exception("The file is not of the correct format");
		}
		
		// More console message trickery
		try
		{
			// Pre-declare to minimize stack workout
    		Map<String, Object> map;
    		DataEntry entry;
    		for (Object o : list)
    		{
    			// Cast and check
    			map = (Map<String, Object>)(o);
    			if (o != null)
    			{
    				// We have a proper map object, so create an entry from it
    				entry = DataEntry.fromJSONObject(map);
    				// And add it to the vector
    				dataRows.add(entry);
    			}
    		}
		}
		catch (Exception e)
		{
			System.out.println(String.format("DataInterface.loadFromFile: Exception while processing the JSON file.\nMessage: %s.\nTrace:\n", e.getMessage()));
			e.printStackTrace();
			throw new Exception("The file is not of the correct format");
		}
		
		// The data definitely changed, so notify the listeners
		fireDataChangedEventListeners();
	}
	
	public void saveToFile(String path) throws Exception
	{
		try
		{
			// Create a writing stream
			FileWriter file = new FileWriter(path);
			// Pass it and the data list to the JSON encoders
			JSONArray.writeJSONString(dataRows, file);
			// And make sure to close the file
			file.close();
		}
		catch (Exception e)
		{
			System.out.println(String.format("DataInterface.saveToFile: Exception while writing the data to a JSON file.\nMessage: %s.\nTrace:\n", e.getMessage()));
			e.printStackTrace();
			throw new Exception("Could not save the file");
		}
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
		fireDataChangedEventListeners();
	}
	
	public void setRow(int rowIndex, DataEntry entry)
	{
		dataRows.set(rowIndex, entry);
		fireDataChangedEventListeners();
	}
	
	public void insertRow(int rowIndex, DataEntry entry)
	{
		dataRows.insertElementAt(entry, rowIndex);
		fireDataChangedEventListeners();
	}
	
	public int findRow(DataEntry entry)
	{
		// Easy way to ensure the ItemEditDialog uses the correct rowIndex
		return dataRows.indexOf(entry);
	}
	
	private Vector<DataEntry> dataRows;
	private Vector<DataChangedEventListener> listeners;
}
