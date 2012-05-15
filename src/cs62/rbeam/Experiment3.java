package cs62.rbeam;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Experiment3
{
	public static void main(String[] args)
	{
		try
		{
			for (LookAndFeelInfo look : UIManager.getInstalledLookAndFeels())
			{
				System.out.println(String.format("%s %s", look.getName(), look.getClassName()));
			}
			System.out.println(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
