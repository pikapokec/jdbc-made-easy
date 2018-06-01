package ds.made.jdbc.easy.model;

import java.util.Arrays;

/**
For usage see {@link EasyColumnClassFieldFactory}.
@author ds
 */
public class EasyColumnClassField
{

	public final String dbFieldName;
	public final String classPropertyName;
	public final Class<?> enumFactoryClass;
	public final String staticEnumMethod;
	public final boolean blob;
	public final boolean clob;
	public final boolean isEnum;
	public final String[] nestedPath;
	public final boolean nested;
	
	public EasyColumnClassField(String dbFieldName, String classPropertyName, boolean nested, Class<?> enumFactoryClass, String staticEnumMethod, boolean blob,
			boolean clob, boolean isEnum)
	{
		this.dbFieldName = dbFieldName;
		this.enumFactoryClass = enumFactoryClass;
		this.staticEnumMethod = staticEnumMethod;
		this.blob = blob;
		this.clob = clob;
		this.isEnum = isEnum;

		this.nested = nested;
		if (nested)
		{
			String[] fields = classPropertyName.split("\\.");
			this.nestedPath = Arrays.copyOf(fields, fields.length-1);
			this.classPropertyName = fields[fields.length-1];
		}
		else
		{
			this.nestedPath = null;
			this.classPropertyName = classPropertyName;
		}
	}

	public EasyColumnClassField(String dbFieldName, String classPropertyName)
	{
		this.dbFieldName = dbFieldName;
		this.classPropertyName = classPropertyName;
		
		this.enumFactoryClass = null;
		this.staticEnumMethod = null;
		this.blob = false;
		this.clob = false;
		this.nested = false;
		
		isEnum = false;
		nestedPath = null;
	}

	public EasyColumnClassField(String dbFieldName, String classPropertyName, boolean nested)
	{
		this.dbFieldName = dbFieldName;
		this.nested = nested;
		
		this.enumFactoryClass = null;
		this.staticEnumMethod = null;
		this.blob = false;
		this.clob = false;
		
		isEnum = false;
		if (nested)
		{
			String[] fields = classPropertyName.split("\\.");
			nestedPath = Arrays.copyOf(fields, fields.length-1);
			this.classPropertyName = fields[fields.length-1];
		}
		else
		{
			nestedPath = null;
			this.classPropertyName = classPropertyName;
		}
	}
	
	public EasyColumnClassField(String dbFieldName, String classPropertyName, Class<?> enumFactoryClass, String staticEnumMethod)
	{
		this.dbFieldName = dbFieldName;
		this.classPropertyName = classPropertyName;
		this.enumFactoryClass = enumFactoryClass;
		this.staticEnumMethod = staticEnumMethod;
		isEnum = true;
		
		this.nested = false;
		this.blob = false;
		this.clob = false;
		nestedPath = null;
	}
	
	public EasyColumnClassField(String dbFieldName, String classPropertyName, boolean blob, boolean clob)
	{
		this.dbFieldName = dbFieldName;
		this.classPropertyName = classPropertyName;
		this.blob = blob;
		this.clob = clob;

		this.enumFactoryClass = null;
		this.staticEnumMethod = null;
		
		this.nested = false;
		isEnum = false;
		nestedPath = null;
	}
	
}
