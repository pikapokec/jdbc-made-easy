package ds.made.jdbc.easy.utility;

import java.math.BigDecimal;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

import ds.made.jdbc.easy.model.Enums;
import ds.made.jdbc.easy.model.ParameterCastException;

/**
 Parameter class that is mapped to a ? in a JDBC {@link Statement}.<br/>
 To use in {@link ../EasyCallForStoredProcedure} and in {@link ../EasyPreparedStatement}.
 For simplified usage please see {@link OracleParameterFactory}.
 @author ds
 */
public class Parameter
{
	public final String name;
	public final int type;
	private Enums.PARAMETER_DIRECTION direction;
	private Enums.PARAMETER_DATE_TYPE dateType = null;
	private Object value;

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

	public Object getValueForExecute()
	{
		if (value != null && dateType != null)
		{
			if (value instanceof java.sql.Timestamp)
				return value;

			switch (dateType)
			{
				case Util:
					return DateUtils.toSQLDate((java.util.Date)value);
				case LocalDate:
					return DateUtils.toSQLDate((LocalDate) value);
				case LocalDateTime:
					return DateUtils.toSQLDate((LocalDateTime)value);
				default:
					break;
			}
		}
		return value;
	}

	public void setValueOutDirection(Object value)
	{
		this.value = value;
		if (value != null && dateType != null)
		{
			if (value instanceof java.sql.Timestamp)
			{
				java.sql.Timestamp ts = (java.sql.Timestamp)value;
				switch (dateType)
				{
					case Util:
						value = DateUtils.toDate(ts);
						break;
					case LocalDate:
						value = DateUtils.toLocalDate(ts);
						break;
					case LocalDateTime:
						value = DateUtils.toLocalDateTime(ts);
						break;
					default:
						break;
				}
			}
		}
	}

	public Parameter setValue(Object value)
	{
		this.value = value;
		if (value != null && dateType != null)
		{
			if (value instanceof java.util.Date)
				this.value = DateUtils.toSQLDate((java.util.Date)value);
			else if (value instanceof LocalDate)
				this.value = DateUtils.toSQLDate((LocalDate)value);
			else if (value instanceof LocalDateTime)
				this.value = DateUtils.toSQLDate((LocalDateTime)value);
		}
		return this;
	}

	public Object getValue()
	{
		if (value != null && dateType != null)
		{
			if (value instanceof java.sql.Timestamp)
			{
				java.sql.Timestamp ts = (java.sql.Timestamp)value;
				switch (dateType)
				{
					case Util:
						value = DateUtils.toDate(ts);
						break;
					case LocalDate:
						value = DateUtils.toLocalDate(ts);
						break;
					case LocalDateTime:
						value = DateUtils.toLocalDateTime(ts);
						break;
					default:
						break;
				}
			}
		}

		return value;
	}

	public String getStringValue() throws ParameterCastException
	{
		if (value == null)
			return null;

		if (value instanceof String)
			return (String)value;

		throw new ParameterCastException("Not a String instance!");
	}

	public Integer getIntegerValue() throws ParameterCastException
	{
		if (value == null)
			return null;

		if (value instanceof Integer)
			return (Integer)value;

		throw new ParameterCastException("Not an Integer instance!");
	}

	public Long getLongValue() throws ParameterCastException
	{
		if (value == null)
			return null;

		if (value instanceof Long)
			return (Long)value;

		throw new ParameterCastException("Not a Long instance!");
	}

	public BigDecimal getBigDecimalValue() throws ParameterCastException
	{
		if (value == null)
			return null;

		if (value instanceof BigDecimal)
			return (BigDecimal)value;

		throw new ParameterCastException("Not a BigDecimal instance!");
	}

	public java.util.Date getUtilDate() throws ParameterCastException
	{
		if (value == null)
			return null;

		if (value != null && dateType != null)
		{
			if (value instanceof java.sql.Timestamp)
			{
				java.sql.Timestamp ts = (java.sql.Timestamp)value;
				if (dateType == Enums.PARAMETER_DATE_TYPE.Util)
					return DateUtils.toDate(ts);
			}
		}

		throw new ParameterCastException("Not a java.util.Date instance!");
	}

	public LocalDate getLocalDate() throws ParameterCastException
	{
		if (value == null)
			return null;

		if (value != null && dateType != null)
		{
			if (value instanceof java.sql.Timestamp)
			{
				java.sql.Timestamp ts = (java.sql.Timestamp)value;
				if (dateType == Enums.PARAMETER_DATE_TYPE.LocalDate)
					return DateUtils.toLocalDate(ts);
			}
		}

		throw new ParameterCastException("Not a LocalDate instance!");
	}

	public LocalDateTime getLocalDateTime() throws ParameterCastException
	{
		if (value == null)
			return null;

		if (value != null && dateType != null)
		{
			if (value instanceof java.sql.Timestamp)
			{
				java.sql.Timestamp ts = (java.sql.Timestamp)value;
				if (dateType == Enums.PARAMETER_DATE_TYPE.LocalDateTime)
					return DateUtils.toLocalDateTime(ts);
			}
		}

		throw new ParameterCastException("Not a LocalDateTime instance!");
	}

	public Enums.PARAMETER_DIRECTION getDirection()
	{
		return direction;
	}

	public Parameter setDirection(Enums.PARAMETER_DIRECTION direction)
	{
		this.direction = direction;
		return this;
	}

	public Enums.PARAMETER_DATE_TYPE getDateType(Enums.PARAMETER_DATE_TYPE dateType)
	{
		return dateType;
	}

	public Parameter setDateType(Enums.PARAMETER_DATE_TYPE dateType)
	{
		this.dateType = dateType;
		return  this;
	}

	@Override
	public String toString()
	{
		return "Parameter [name=" + name + ", type=" + type + ", direction=" + direction + ", value=" + value + "]";
	}
	
}
