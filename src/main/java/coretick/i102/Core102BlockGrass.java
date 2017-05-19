package coretick.i102;

import java.util.Random;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import coretick.util.BlockInjection;
import coretick.util.ClusterConfig;
import net.minecraft.server.v1_10_R1.BlockDirt;
import net.minecraft.server.v1_10_R1.BlockGrass;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.Blocks;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.World;

public class Core102BlockGrass extends BlockGrass implements BlockInjection
{
	private ClusterConfig cc;
	private boolean inject;
	private boolean eventSpread;
	private boolean eventFade;
	private boolean tick;
	private int speed;
	
	public Core102BlockGrass()
	{
		cc = new ClusterConfig();
	}
	
	public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
	{
		if(!world.isClientSide)
		{
			if(!tick)
			{
				return;
			}
			
			int lightLevel = -1;
			
			if(world.getType(blockposition.up()).c() > 2 && (lightLevel = world.getLightLevel(blockposition.up())) < 4)
			{
				org.bukkit.World bworld = world.getWorld();
				BlockState blockState = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()).getState();
				blockState.setType(CraftMagicNumbers.getMaterial(Blocks.DIRT));
				
				if(eventFade)
				{
					BlockFadeEvent event = new BlockFadeEvent(blockState.getBlock(), blockState);
					world.getServer().getPluginManager().callEvent(event);
					
					if(!event.isCancelled())
					{
						blockState.update(true);
					}
				}
			}
			
			else
			{
				if(lightLevel == -1)
				{
					lightLevel = world.getLightLevel(blockposition.up());
					
					if(lightLevel >= 9)
					{
						for(int i = 0; i < 4 * speed; ++i)
						{
							BlockPosition blockposition1 = blockposition.a(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
							
							IBlockData iblockdata2 = world.getType(blockposition1);
							if(iblockdata2 == null)
							{
								return;
							}
							
							IBlockData iblockdata1 = world.getType(blockposition1.up());
							
							if(iblockdata2.getBlock() == Blocks.DIRT && iblockdata2.get(BlockDirt.VARIANT) == BlockDirt.EnumDirtVariant.DIRT && iblockdata1.c() <= 2 && world.getLightLevel(blockposition1.up()) > 4)
							{
								org.bukkit.World bworld = world.getWorld();
								BlockState blockState = bworld.getBlockAt(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ()).getState();
								blockState.setType(CraftMagicNumbers.getMaterial(Blocks.GRASS));
								
								if(eventSpread)
								{
									BlockSpreadEvent event = new BlockSpreadEvent(blockState.getBlock(), bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), blockState);
									world.getServer().getPluginManager().callEvent(event);
									
									if(!event.isCancelled())
									{
										blockState.update(true);
									}
								}
							}
						}
					}
					
				}
			}
		}
	}
	
	@Override
	public void onNewConfig(ClusterConfig cc)
	{
		cc.set(getCodeName() + ".inject", true, "Should this CoreTick Block be injected? (replacing the minecraft server implementation)");
		cc.set(getCodeName() + ".events.block-spread-event", true, "Toggle the event BlockFadeEvent. \nTurning this off will prevent plugins from seeing this event.");
		cc.set(getCodeName() + ".events.block-fade-event", true, "Toggle the event BlockFadeEvent. \nTurning this off will prevent plugins from seeing this event.");
		cc.set(getCodeName() + ".functionality.speed", 1, "Change the growth speed, higher values grow faster, but consume higher tick times\nKeep this value at or above 1.");
		cc.set(getCodeName() + ".functionality.tick", true, "Should grass blocks be ticked?");
	}
	
	@Override
	public void onReadConfig()
	{
		tick = cc.getBoolean(getCodeName() + ".functionality.tick");
		inject = cc.getBoolean(getCodeName() + ".inject");
		eventSpread = cc.getBoolean(getCodeName() + ".events.block-spread-event");
		eventFade = cc.getBoolean(getCodeName() + ".events.block-fade-event");
		speed = cc.getInt(getCodeName() + ".functionality.speed");
		
		if(speed < 1)
		{
			speed = 1;
		}
	}
	
	@Override
	public ClusterConfig getConfiguration()
	{
		return cc;
	}
	
	@Override
	public String getCodeName()
	{
		return "grass";
	}

	public boolean isInject()
	{
		return inject;
	}

	public boolean isEventSpread()
	{
		return eventSpread;
	}

	public boolean isEventFade()
	{
		return eventFade;
	}
	
	public int getSpeed()
	{
		return speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public void setInject(boolean inject)
	{
		this.inject = inject;
	}

	public void setEventSpread(boolean eventSpread)
	{
		this.eventSpread = eventSpread;
	}

	public void setEventFade(boolean eventFade)
	{
		this.eventFade = eventFade;
	}
}
