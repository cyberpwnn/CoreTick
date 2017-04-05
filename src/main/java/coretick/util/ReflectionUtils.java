package coretick.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils
{
	public static void setAccessible(Field field)
	{
		try
		{
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		}
		
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setStatic(final String name, final Class<?> clazz, final Object val)
	{
		try
		{
			final Field field = clazz.getDeclaredField(name);
			setAccessible(field);
			field.set(null, val);
		}
		
		catch(ReflectiveOperationException ex)
		{
			ex.printStackTrace();
		}
	}
}
