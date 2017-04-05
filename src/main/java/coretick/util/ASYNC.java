package coretick.util;

import coretick.CoreTickAPI;

public abstract class ASYNC
{
	public ASYNC()
	{
		CoreTickAPI.queue(new Execution()
		{
			@Override
			public void run()
			{
				async();
			}
		});
	}
	
	public abstract void async();
}
