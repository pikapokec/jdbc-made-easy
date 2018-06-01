package ds.made.jdbc.easy.utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.datatype.DatatypeConfigurationException;

/**
Utility for reading data from {@link ResultSet}.
@author ds
*/
public class DBHelper
{
    public static Object toObject(String fieldName, ResultSet rs) throws SQLException
    {
        Object o = rs.getObject(fieldName);
        if (rs.wasNull())
            return null;
        return o;
    }

    public static Object toObject(int fieldPosition, ResultSet rs) throws SQLException
    {
        Object o = rs.getObject(1);
        if (rs.wasNull())
            return null;
        return o;
    }
	
    public static javax.xml.datatype.XMLGregorianCalendar toXMLGregorianCalendar(String fieldName, ResultSet rs) throws SQLException, DatatypeConfigurationException
    {
        java.sql.Timestamp ts = rs.getTimestamp(fieldName);
        if (rs.wasNull())
            return null;
        return DateUtils.toXMLGregorianCalendar(ts);
    }
    
    public static javax.xml.datatype.XMLGregorianCalendar toXMLGregorianCalendar(int fieldPosition, ResultSet rs) throws SQLException, DatatypeConfigurationException
    {
        java.sql.Timestamp ts = rs.getTimestamp(fieldPosition);
        if (rs.wasNull())
            return null;
        return DateUtils.toXMLGregorianCalendar(ts);
    }

	public static java.util.Date toDate(String fieldName, ResultSet rs) throws SQLException
    {
        java.sql.Timestamp ts = rs.getTimestamp(fieldName);
        if (rs.wasNull())
            return null;
        return DateUtils.toDate(ts);
    }
    
    public static java.util.Date toDate(int fieldPosition, ResultSet rs) throws SQLException
    {
        java.sql.Timestamp ts = rs.getTimestamp(fieldPosition);
        if (rs.wasNull())
            return null;
        return DateUtils.toDate(ts);
    }

    public static LocalDate toLocalDate(String fieldName, ResultSet rs) throws SQLException
    {
        java.sql.Timestamp ts = rs.getTimestamp(fieldName);
        if (rs.wasNull())
            return null;
        return DateUtils.toLocalDate(ts);
    }

    public static LocalDate toLocalDate(int fieldPosition, ResultSet rs) throws SQLException
    {
        java.sql.Timestamp ts = rs.getTimestamp(fieldPosition);
        if (rs.wasNull())
            return null;
        return DateUtils.toLocalDate(ts);
    }

    public static LocalDateTime toLocalDateTime(String fieldName, ResultSet rs) throws SQLException
    {
        java.sql.Timestamp ts = rs.getTimestamp(fieldName);
        if (rs.wasNull())
            return null;
        return DateUtils.toLocalDateTime(ts);
    }

    public static LocalDateTime toLocalDateTime(int fieldPosition, ResultSet rs) throws SQLException
    {
        java.sql.Timestamp ts = rs.getTimestamp(fieldPosition);
        if (rs.wasNull())
            return null;
        return DateUtils.toLocalDateTime(ts);
    }

    public static Short toShort(String fieldName, ResultSet rs) throws SQLException
    {
    	Short s = rs.getShort(fieldName);
        if (rs.wasNull())
            return null;
        return s;
    }
    
    public static Short toShort(int fieldPosition, ResultSet rs) throws SQLException
    {
    	Short s = rs.getShort(fieldPosition);
        if (rs.wasNull())
            return null;
        return s;
    }

    public static Long toLong(String fieldName, ResultSet rs) throws SQLException
    {
        Long l = rs.getLong(fieldName);
        if (rs.wasNull())
            return null;
        return l;
    }

    public static Long toLong(int fieldPosition, ResultSet rs) throws SQLException
    {
        Long l = rs.getLong(fieldPosition);
        if (rs.wasNull())
            return null;
        return l;
    }

    public static Integer toInteger(String fieldName, ResultSet rs) throws SQLException
    {
        Integer i = rs.getInt(fieldName);
        if (rs.wasNull())
            return null;
        return i;
    }

    public static Integer toInteger(int fieldPosition, ResultSet rs) throws SQLException
    {
        Integer i = rs.getInt(fieldPosition);
        if (rs.wasNull())
            return null;
        return i;
    }
    
    public static String toString(String fieldName, ResultSet rs) throws SQLException
    {
        String s = rs.getString(fieldName);
        if (rs.wasNull())
            return null;
        return s;
    }

    public static String toString(int fieldPosition, ResultSet rs) throws SQLException
    {
        String s = rs.getString(fieldPosition);
        if (rs.wasNull())
            return null;
        return s;
    }
    
    public static BigDecimal toBigDecimal(String fieldName, ResultSet rs) throws SQLException
    {
        BigDecimal b = rs.getBigDecimal(fieldName);
        if (rs.wasNull())
            return null;
        return b;
    }

    public static BigDecimal toBigDecimal(int fieldPosition, ResultSet rs) throws SQLException
    {
        BigDecimal b = rs.getBigDecimal(fieldPosition);
        if (rs.wasNull())
            return null;
        return b;
    }

    public static BigInteger toBigInteger(String fieldName, ResultSet rs) throws SQLException
    {
        BigDecimal b = rs.getBigDecimal(fieldName);
        if (rs.wasNull())
            return null;
        return b.toBigInteger();
    }

    public static BigInteger toBigInteger(int fieldPosition, ResultSet rs) throws SQLException
    {
        BigDecimal b = rs.getBigDecimal(fieldPosition);
        if (rs.wasNull())
            return null;
        return b.toBigInteger();
    }
    
    public static byte[] toByteArray(String fieldName, ResultSet rs) throws SQLException
    {
        Blob blob = rs.getBlob(fieldName);
        if (blob == null)
            return null;

        int blobLength = (int) blob.length();
        byte[] blobAsBytes = blob.getBytes(1, blobLength);
        blob.free();
        return blobAsBytes;
    }

    public static Class<?> toClass(int type)
    {
        Class<?> result = Object.class;

        switch (type) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                result = String.class;
                break;

            case Types.NUMERIC:
            case Types.DECIMAL:
                result = java.math.BigDecimal.class;
                break;

            case Types.BIT:
                result = Boolean.class;
                break;

            case Types.TINYINT:
                result = Byte.class;
                break;

            case Types.SMALLINT:
                result = Short.class;
                break;

            case Types.INTEGER:
                result = Integer.class;
                break;

            case Types.BIGINT:
                result = Long.class;
                break;

            case Types.REAL:
            case Types.FLOAT:
                result = Float.class;
                break;

            case Types.DOUBLE:
                result = Double.class;
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                result = Byte[].class;
                break;

            case Types.DATE:
                result = LocalDate.class;
                break;

            case Types.TIME:
                result = LocalDateTime.class;
                break;

            case Types.TIMESTAMP:
                result = LocalDateTime.class;
                break;
        }

        return result;
    }
}
