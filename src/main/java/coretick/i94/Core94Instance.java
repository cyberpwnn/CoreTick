package coretick.i94;

import com.google.common.collect.UnmodifiableIterator;
import coretick.i.CoreInstance;
import coretick.util.Configurable;
import coretick.util.ReflectionUtils;
import net.minecraft.server.v1_9_R2.Block;
import net.minecraft.server.v1_9_R2.Blocks;
import net.minecraft.server.v1_9_R2.IBlockData;
import net.minecraft.server.v1_9_R2.MinecraftKey;

public class Core94Instance extends CoreInstance
{
	public Core94Instance()
	{
		putSwap("GRASS", "grass", 2, Core94BlockGrass.class);
	}
	
	public Block get(String s)
	{
		return Block.REGISTRY.get(new MinecraftKey(s));
	}
	
	public void add(int index, String name, Block block)
	{
		Block.REGISTRY.a(index, new MinecraftKey(name), block);
		
		for(UnmodifiableIterator<IBlockData> localUnmodifiableIterator = block.t().a().iterator(); localUnmodifiableIterator.hasNext();)
		{
			IBlockData iblockdata = (IBlockData) localUnmodifiableIterator.next();
			int k = Block.getId(block) << 4 | block.toLegacyData(iblockdata);
			Block.REGISTRY_ID.a(iblockdata, k);
		}
	}
	
	public void inject()
	{
		for(String i : swap.k())
		{
			try
			{
				String mcKey = enumKeys.get(i);
				Object o = swap.get(i).getConstructor().newInstance();
				
				if(o instanceof Configurable)
				{
					Configurable c = (Configurable) o;
					System.out.println("Loading Block Configuration: " + c.getCodeName());
					getDc().load("blocks", c);
					configs.add(c);
					
					if(c.getConfiguration().contains(c.getCodeName() + ".inject") && c.getConfiguration().getBoolean(c.getCodeName() + ".inject"))
					{
						int id = ids.get(i);
						add(id, mcKey, (Block) o);
						ReflectionUtils.setStatic(i, Blocks.class, get(mcKey));
						System.out.println("Injected Block: " + o.getClass().getSimpleName());
					}
				}
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
