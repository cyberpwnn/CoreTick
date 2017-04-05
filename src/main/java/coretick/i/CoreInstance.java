package coretick.i;

import java.io.File;
import coretick.util.BlockInjection;
import coretick.util.Configurable;
import coretick.util.DataController;
import coretick.util.GList;
import coretick.util.GMap;

public class CoreInstance
{
	protected GMap<String, Class<?>> swap;
	protected GMap<String, Integer> ids;
	protected GMap<String, String> enumKeys;
	protected GList<Configurable> configs;
	protected GMap<Configurable, Long> mods;
	protected DataController dc;
	
	public CoreInstance()
	{
		dc = new DataController();
		swap = new GMap<String, Class<?>>();
		enumKeys = new GMap<String, String>();
		ids = new GMap<String, Integer>();
		configs = new GList<Configurable>();
		mods = new GMap<Configurable, Long>();
	}
	
	public void searchMods()
	{
		File root = new File(dc.getDataFolder(), "blocks");
		
		for(File i : root.listFiles())
		{
			Configurable c = getFile(i);
			
			if(c != null)
			{
				long mod = i.lastModified();
				
				if(!mods.containsKey(c))
				{
					mods.put(c, mod);
					continue;
				}
				
				if(mods.get(c).longValue() != mod)
				{
					mods.remove(c);
					dc.load("blocks", c);
					System.out.println("Reloaded Config: " + c.getCodeName());
				}
			}
		}
	}
	
	public Configurable getFile(File f)
	{
		for(Configurable i : configs)
		{
			if((i.getCodeName() + ".yml").equals(f.getName()) && f.getParentFile().getName().equals("blocks"))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public void putSwap(String swap, String mcKey, int id, Class<? extends BlockInjection> clazz)
	{
		this.swap.put(swap, clazz);
		this.enumKeys.put(swap, mcKey);
		this.ids.put(swap, id);
	}
	
	public void reloadConfigs()
	{
		for(Configurable i : configs)
		{
			dc.load("blocks", i);
		}
	}
	
	public void inject()
	{
		
	}

	public GMap<String, Class<?>> getSwap()
	{
		return swap;
	}

	public GMap<String, Integer> getIds()
	{
		return ids;
	}

	public GMap<String, String> getEnumKeys()
	{
		return enumKeys;
	}

	public GList<Configurable> getConfigs()
	{
		return configs;
	}

	public DataController getDc()
	{
		return dc;
	}
}
