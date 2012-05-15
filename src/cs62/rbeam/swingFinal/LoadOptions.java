package cs62.rbeam.swingFinal;

class LoadOptions
{
	enum Values
	{
		Empty,
		File,
		Database,
	}
	
	public LoadOptions(Values value, String label)
	{
		this.value = value;
		this.label = new String(label);
	}
	
	public Values getValue()
	{
		return value;
	}
	
	public String toString()
	{
		return label;
	}
	
	private Values value;
	private String label;
}
