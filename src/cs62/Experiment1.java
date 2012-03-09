package cs62;

public class Experiment1
{
	static public void main(String[] args)
	{
		int a = 1, b = 4, c = 0;
		
		if ((c = (b = a)) != 0)
		{
			System.out.println(String.format("(%d = (%d = %d)) != 0.", c, b, a));
		}
		else
		{
			System.out.println(String.format("(%d = (%d = %d)) == 0.", c, b, a));
		}
	}
}