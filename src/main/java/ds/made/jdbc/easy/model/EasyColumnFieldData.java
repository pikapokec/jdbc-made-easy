package ds.made.jdbc.easy.model;

import java.lang.reflect.Field;

import ds.made.jdbc.easy.annotations.EasyColumn;

/**
For internal usage.
@author ds
 */
public class EasyColumnFieldData
{

	public final Field field;
	public final EasyColumn column;
	public final boolean isEnum;
	
	public EasyColumnFieldData(Field field, EasyColumn column)
	{
		this.field = field;
		this.column = column;
		
		if (column != null)
		{
			if (column.staticEnumMethod() == null || column.staticEnumMethod().trim().equals(""))
				isEnum = false;
			else
				isEnum = true;
		}
		else
		{
			isEnum = false;
		}
	}
	
}
