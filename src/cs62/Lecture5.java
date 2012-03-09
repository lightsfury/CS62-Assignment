package cs62;

import java.text.DecimalFormat;
import java.text.ParseException;

public class Lecture5
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ParseException
	{
		// TODO Auto-generated method stub
		DecimalFormat fmt = new DecimalFormat("$#,##0.00");
		System.out.println(fmt.format(123123123123.456));
		System.out.println(fmt.parse("$456,789"));
	}

}
