package ds.made.jdbc.easy.annotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ds.made.jdbc.easy.model.EasyColumnFieldData;

/**
For internal usage.
@author ds
 */
public class MapEasyAnnotations
{

	private final Class<?> clazz;
	
	public MapEasyAnnotations(Class<?> clazz)
	{
		this.clazz = clazz;
	}
	
	public EasyColumnFieldData[] propertiesList()
	{
		Field[] properties = clazz.getDeclaredFields();
		List<EasyColumnFieldData> propList = new ArrayList<EasyColumnFieldData>();
		for (Field p : properties)
		{
			if (p.isAnnotationPresent(EasyColumn.class))
			{
				EasyColumn a = p.getAnnotation(EasyColumn.class);
				propList.add(new EasyColumnFieldData(p,a));
			}
		}
		
		return propList.toArray(new EasyColumnFieldData[propList.size()]);
	}
	
}
