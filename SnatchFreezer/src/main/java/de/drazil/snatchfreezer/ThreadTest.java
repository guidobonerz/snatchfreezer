package de.drazil.snatchfreezer;

public class ThreadTest
{
	public static void main(String argv[])
	{
		Thread t = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				System.out.println("sdfsdfsdf");
				try
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();

	}
}
