package ds.made.jdbc.easy.utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import ds.made.jdbc.easy.model.Enums;

/**
Simplified creation of Oracle database parameters. Port from oracle.jdbc.OracleTypes.
@author ds
*/
public class OracleParameterFactory
{
	// From oracle.jdbc.OracleTypes
	public static final int INTEGER = 4;
	public static final int NUMERIC = 2;
	public static final int DECIMAL = 3;
	public static final int VARCHAR = 12;
	public static final int DATE = 91;
	public static final int TIME = 92;
	public static final int TIMESTAMP = 93;
	public static final int TIMESTAMPTZ = -101;
	public static final int TIMESTAMPLTZ = -102;
	public static final int CURSOR = -10;
	public static final int BLOB = 2004;
	public static final int CLOB = 2005;
	public static final int SQLXML = 2009;
	public static final int NULL = 0;
	public static final int NUMBER = 2;
	
	/**
	 * Creates a String parameter.
	 * @param name parameter name
	 * @return Parameter
	 */
	public static Parameter stringParameter(String name)
	{
		return new Parameter(name,VARCHAR);
	}

	/**
	 * Creates a String parameter.
	 * @param name parameter name
	 * @param value parameter value
	 * @return Parameter
	 */
	public static Parameter stringParameter(String name, String value)
	{
		return new Parameter(name,VARCHAR,value);
	}

	/**
	 * Creates a String parameter in out direction.
	 * @param name parameter name
	 * @param value parameter value
	 * @return Parameter
	 */
	public static Parameter stringInOutParameter(String name, String value)
	{
		return new Parameter(name,VARCHAR,Enums.PARAMETER_DIRECTION.IN_OUT,value);
	}

	/**
	 * Creates a String parameter out direction.
	 * @param name parameter name
	 * @return Parameter
	 */
	public static Parameter stringOutParameter(String name)
	{
		return new Parameter(name,VARCHAR,Enums.PARAMETER_DIRECTION.OUT);
	}

	/**
	 * Creates a String function return parameter.
	 * @param name parameter name
	 * @return Parameter
	 */
	public static Parameter stringReturnParameter(String name)
	{
		return new Parameter(name,VARCHAR,Enums.PARAMETER_DIRECTION.RETURN);
	}
	
	/**
	 * Creates an Integer parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter integerParameter(String name)
	{
		return new Parameter(name,INTEGER);
	}
	
	/**
	 * Creates an Integer parameter.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter integerParameter(String name, Integer value)
	{
		return new Parameter(name,INTEGER,value);
	}

	/**
	 * Creates an Integer parameter in out direction.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter integerInOutParameter(String name, Integer value)
	{
		return new Parameter(name,INTEGER,Enums.PARAMETER_DIRECTION.IN_OUT,value);
	}
	
	/**
	 * Creates an Integer parameter out direction.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter integerOutParameter(String name)
	{
		return new Parameter(name,INTEGER,Enums.PARAMETER_DIRECTION.OUT);
	}
	
	/**
	 * Creates an Integer function return parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter integerReturnParameter(String name)
	{
		return new Parameter(name,INTEGER,Enums.PARAMETER_DIRECTION.RETURN);
	}

	// -------------------------------------------------------------------------------------------------- //
	// java.util.Date
	// -------------------------------------------------------------------------------------------------- //

	/**
	 * Creates a Date parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter dateParameter(String name)
	{
		return new Parameter(name,TIMESTAMP).setDateType(Enums.PARAMETER_DATE_TYPE.Util);
	}

	/**
	 * Creates a Date parameter.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter dateParameter(String name, Date value)
	{
		return new Parameter(name,TIMESTAMP,value).setDateType(Enums.PARAMETER_DATE_TYPE.Util);
	}
	
	/**
	 * Creates a Date parameter in out direction.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter dateInOutParameter(String name, Date value)
	{
		return new Parameter(name,TIMESTAMP,Enums.PARAMETER_DIRECTION.IN_OUT,value).setDateType(Enums.PARAMETER_DATE_TYPE.Util);
	}
	
	/**
	 * Creates a Date parameter out direction.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter dateOutParameter(String name)
	{
		return new Parameter(name,TIMESTAMP,Enums.PARAMETER_DIRECTION.OUT).setDateType(Enums.PARAMETER_DATE_TYPE.Util);
	}
	
	/**
	 * Creates an Date function return parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter dateReturnParameter(String name)
	{
		return new Parameter(name,TIMESTAMP,Enums.PARAMETER_DIRECTION.RETURN).setDateType(Enums.PARAMETER_DATE_TYPE.Util);
	}

	// -------------------------------------------------------------------------------------------------- //
	// java.time
	// -------------------------------------------------------------------------------------------------- //

	/**
	 * Creates a LocalDate parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter localDateParameter(String name)
	{
		return new Parameter(name,TIMESTAMP).setDateType(Enums.PARAMETER_DATE_TYPE.LocalDate);
	}

	/**
	 * Creates a LocalDate parameter.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter localDateParameter(String name, LocalDate value)
	{
		return new Parameter(name,TIMESTAMP,value).setDateType(Enums.PARAMETER_DATE_TYPE.LocalDate);
	}

	/**
	 * Creates a LocalDate parameter in out direction.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter localDateInOutParameter(String name, LocalDate value)
	{
		return new Parameter(name,TIMESTAMP,Enums.PARAMETER_DIRECTION.IN_OUT,value).setDateType(Enums.PARAMETER_DATE_TYPE.LocalDate);
	}

	/**
	 * Creates a LocalDate parameter out direction.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter localDateOutParameter(String name)
	{
		return new Parameter(name,TIMESTAMP,Enums.PARAMETER_DIRECTION.OUT).setDateType(Enums.PARAMETER_DATE_TYPE.LocalDate);
	}

	/**
	 * Creates an LocalDate function return parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter localDateReturnParameter(String name)
	{
		return new Parameter(name,TIMESTAMP,Enums.PARAMETER_DIRECTION.RETURN).setDateType(Enums.PARAMETER_DATE_TYPE.LocalDate);
	}

	/**
	 * Creates a LocalDateTime parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter localDateTimeParameter(String name)
	{
		return new Parameter(name,TIMESTAMP).setDateType(Enums.PARAMETER_DATE_TYPE.LocalDateTime);
	}

	/**
	 * Creates a LocalDateTime parameter.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter localDateTimeParameter(String name, LocalDateTime value)
	{
		return new Parameter(name,TIMESTAMP,value).setDateType(Enums.PARAMETER_DATE_TYPE.LocalDateTime);
	}

	/**
	 * Creates a LocalDateTime parameter in out direction.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter localDateTimeInOutParameter(String name, LocalDateTime value)
	{
		return new Parameter(name,TIMESTAMP,Enums.PARAMETER_DIRECTION.IN_OUT,value).setDateType(Enums.PARAMETER_DATE_TYPE.LocalDateTime);
	}

	/**
	 * Creates a LocalDateTime parameter out direction.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter localDateTimeOutParameter(String name)
	{
		return new Parameter(name,TIMESTAMP,Enums.PARAMETER_DIRECTION.OUT).setDateType(Enums.PARAMETER_DATE_TYPE.LocalDateTime);
	}

	/**
	 * Creates an LocalDateTime function return parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter localDateTimeReturnParameter(String name)
	{
		return new Parameter(name,TIMESTAMP,Enums.PARAMETER_DIRECTION.RETURN).setDateType(Enums.PARAMETER_DATE_TYPE.LocalDateTime);
	}

	// -------------------------------------------------------------------------------------------------- //

	/**
	 * Creates a Number parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter numberParameter(String name)
	{
		return new Parameter(name,NUMBER);
	}
	
	/**
	 * Creates a Number parameter.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter numberParameter(String name, BigDecimal value)
	{
		return new Parameter(name,NUMBER,value);
	}

	/**
	 * Creates a Number parameter in out direction.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter numberInOutParameter(String name, BigDecimal value)
	{
		return new Parameter(name,NUMBER,Enums.PARAMETER_DIRECTION.IN_OUT,value);
	}
	
	/**
	 * Creates a Number parameter out direction.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter numberOutParameter(String name)
	{
		return new Parameter(name,NUMBER,Enums.PARAMETER_DIRECTION.OUT);
	}
	
	/**
	 * Creates an Number function return parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter numberReturnParameter(String name)
	{
		return new Parameter(name,NUMBER,Enums.PARAMETER_DIRECTION.RETURN);
	}
	
	/**
	 * Creates an Decimal parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter decimalParameter(String name)
	{
		return new Parameter(name,DECIMAL);
	}
	
	/**
	 * Creates an Decimal parameter.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter decimalParameter(String name, BigDecimal value)
	{
		return new Parameter(name,DECIMAL,value);
	}

	/**
	 * Creates an Decimal parameter in out direction.
	 * @param name
	 * @param value
	 * @return Parameter
	 */
	public static Parameter decimalInOutParameter(String name, BigDecimal value)
	{
		return new Parameter(name,DECIMAL,Enums.PARAMETER_DIRECTION.IN_OUT,value);
	}
	
	/**
	 * Creates an Decimal parameter out direction.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter decimalOutParameter(String name)
	{
		return new Parameter(name,DECIMAL,Enums.PARAMETER_DIRECTION.OUT);
	}
	
	/**
	 * Creates an Decimal return value parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter decimalReturnParameter(String name)
	{
		return new Parameter(name,DECIMAL,Enums.PARAMETER_DIRECTION.RETURN);
	}
	
	/**
	 * Creates an Cursor parameter out direction.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter cursorOutParameter(String name)
	{
		return new Parameter(name,CURSOR,Enums.PARAMETER_DIRECTION.OUT);		
	}
	
	/**
	 * Creates an Cursor function return parameter.
	 * @param name
	 * @return Parameter
	 */
	public static Parameter cursorReturnParameter(String name)
	{
		return new Parameter(name,CURSOR,Enums.PARAMETER_DIRECTION.RETURN);		
	}
	
}
