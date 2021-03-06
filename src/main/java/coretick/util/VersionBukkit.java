package coretick.util;

import org.bukkit.Bukkit;

public enum VersionBukkit
{
	VU,
	V7,
	V8,
	V9,
	V10,
	V11;
	
	public static boolean tc()
	{
		if(get().equals(V7))
		{
			return false;
		}
		
		return true;
	}
	
	public static VersionBukkit get()
	{
		if(Bukkit.getBukkitVersion().startsWith("1.7"))
		{
			return V7;
		}
		
		if(Bukkit.getBukkitVersion().startsWith("1.8"))
		{
			return V8;
		}
		
		if(Bukkit.getBukkitVersion().startsWith("1.9"))
		{
			return V9;
		}
		
		if(Bukkit.getBukkitVersion().startsWith("1.10"))
		{
			return V10;
		}
		
		if(Bukkit.getBukkitVersion().startsWith("1.11"))
		{
			return V11;
		}
		
		return VU;
	}
}
