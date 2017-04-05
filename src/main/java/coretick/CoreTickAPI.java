package coretick;

import coretick.util.Execution;
import coretick.util.ParallelThread;

public class CoreTickAPI
{
	private static CoreTick i()
	{
		return CoreTick.instance();
	}
	
	public static void queue(Execution e)
	{
		i().getPoolManager().queue(e);
	}
	
	public static double getTPS(int thread)
	{
		return getThreads()[thread].getInfo().getTicksPerSecondAverage();
	}
	
	public static int getThreadCount()
	{
		return i().getPoolManager().getSize();
	}
	
	public static ParallelThread[] getThreads()
	{
		return i().getPoolManager().getThreads();
	}
}
