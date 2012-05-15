package cs62.rbeam;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Experiment2
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
			
			for (LookAndFeelInfo look : looks)
			{
				System.out.println(look.getClassName());
				//System.out.println(look.getName());
			}
			
			//System.out.println(UIManager.getSystemLookAndFeelClassName());
			
			System.out.println(UIManager.getLookAndFeel().getName());

		}
		catch (Exception e)
		{
		}

	}

}
