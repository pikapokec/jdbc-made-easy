package ds.made.jdbc.easy.utility;

import java.sql.Statement;

import ds.made.jdbc.easy.model.Enums;

/**
 Parameter class that is mapped to a ? in a JDBC {@link Statement}.<br/>
 To use in {@link EasyCallForStoredProcedure} and in {@link EasyPreparedStatement}.
 For simplified usage please see {@link OracleParameterFactory}.
 @author ds
 */
public class Parameter
{
	public final String name;
	public final int type;
	public Enums.PARAMETER_DIRECTION direction;
	public Object value;

	public Parameter(String name, int type)
	{
		this.name = name;
		this.type = type;
		direction = Enums.PARAMETER_DIRECTION.IN;
	}

	public Parameter(String name, int type, Enums.PARAMETER_DIRECTION direction)
	{
		this.name = name;
		this.type = type;
		this.direction = direction;
	}
	
	public Parameter(String name, int type, Object value)
	{
		this.name = name;
		this.type = type;
		this.value = value;
		direction = Enums.PARAMETER_DIRECTION.IN;
	}

	public Parameter(String name, int type, Enums.PARAMETER_DIRECTION direction, Object value)
	{
		this.name = name;
		this.type = type;
		this.value = value;
		this.direction = direction;
	}
	
	public boolean isNULL()
	{
		return (value == null);
	}
	
	public Parameter setValue(Object value)
	{
		this.value = value;
		if (value != null)
		{
			if (value instanceof java.util.Date)
			{
				java.util.Date d = (java.util.Date)value;
				this.value = DateUtils.toSQLDate(d);
			}
		}
		return this;
	}
	
	public Parameter setDirection(Enums.PARAMETER_DIRECTION direction)
	{
		this.direction = direction;
		return this;
	}

	@Override
	public String toString()
	{
		return "Parameter [name=" + name + ", type=" + type + ", direction=" + direction + ", value=" + value + "]";
	}
	
}
