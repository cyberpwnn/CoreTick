package coretick;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import coretick.i.CoreInstance;
import coretick.i102.Core102Instance;
import coretick.i112.Core112Instance;
import coretick.i94.Core94Instance;
import coretick.util.DataController;
import coretick.util.GList;
import coretick.util.ParallelPoolManager;
import coretick.util.TICK;
import coretick.util.VersionBukkit;

public class CoreTick extends JavaPlugin
{
	private CoreInstance ci;
	private ParallelPoolManager poolManager;
	private static CoreTick instance;
	
	public void onLoad()
	{
		instance = this;
		destroyOldThreads();
		readCurrentTick();
	}
	
	public void onEnable()
	{
		ci = newCoreInstance();
		
		if(ci == null)
		{
			System.out.println("No CoreInstance supports " + VersionBukkit.get().toString());
			return;
		}
		
		setupTicker();
		tryHack();
	}
	
	private void tryHack()
	{
		try
		{
			ci.inject();
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void destroyOldThreads()
	{
		boolean k = false;
		
		for(Thread i : new GList<Thread>(Thread.getAllStackTraces().keySet()))
		{
			if(i.getName().startsWith("CT Parallel Tick Thread "))
			{
				k = true;
				
				try
				{
					System.out.println("WAITING FOR OLD THREAD TO DIE: " + i.getName());
					i.interrupt();
					i.join(100);
				}
				
				catch(InterruptedException e)
				{
					
				}
				
				catch(Throwable e)
				{
					e.printStackTrace();
				}
				
				if(i.isAlive())
				{
					try
					{
						System.out.println("FORCE KILLING");
						i.stop();
					}
					
					catch(Throwable e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		if(k)
		{
			System.out.println("Killed off stale threads from pre-reload");
		}
	}
	
	private void readCurrentTick()
	{
		long ms = System.currentTimeMillis();
		File prop = new File("server.properties");
		TICK.tick = (ms - prop.lastModified()) / 50;
		System.out.println("Setting Tick to " + TICK.tick);
		poolManager = new ParallelPoolManager(4);
	}
	
	private void setupTicker()
	{
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				TICK.tick++;
				
				if(TICK.tick % 20 == 0)
				{
					ci.searchMods();
				}
			}
		}, 0, 0);
		
		poolManager.start();
	}
	
	private CoreInstance newCoreInstance()
	{
		switch(VersionBukkit.get())
		{
			case V10:
				return new Core102Instance();
			case V11:
				return new Core112Instance();
			case V9:
				return new Core94Instance();
			default:
				return null;
		}
	}
	
	public static DataController getDataController()
	{
		return instance.ci.getDc();
	}
	
	public void onDisable()
	{
		poolManager.shutdown();
	}
	
	public ParallelPoolManager getPoolManager()
	{
		return poolManager;
	}
	
	public static CoreTick instance()
	{
		return instance;
	}
}
