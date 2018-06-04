package ds.made.jdbc.easy.utility;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
Utility for convertion java.sql.Date to java.util.Date and vice versa.

@author ds
*/
public class DateUtils
{
	public static final String FULL_FORMAT = "dd.MM.yyyy HH:mm:ss.SSS";
	public static final String DATE_FORMAT = "dd.MM.yyyy";
	public static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
	
    public static java.util.Date toDate(java.sql.Date date)
    {
        if (date == null)
            return null;

        java.util.Date d = new java.util.Date();
        d.setTime(date.getTime());
        return d;
    }

    public static LocalDate toLocalDate(java.sql.Timestamp timestamp)
    {
        if (timestamp == null)
            return null;

        return timestamp.toLocalDateTime().toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(java.sql.Timestamp timestamp)
    {
        if (timestamp == null)
            return null;

        return timestamp.toLocalDateTime();
    }

    public static java.util.Date toDate(java.sql.Timestamp timestamp)
    {
        if (timestamp == null)
            return null;

        java.util.Date d = new java.util.Date();
        d.setTime(timestamp.getTime());
        return d;
    }

    public static java.util.Date toDate(javax.xml.datatype.XMLGregorianCalendar date)
    {
        if (date == null)
            return null;

        return date.toGregorianCalendar().getTime();
    }
    
	public static java.sql.Date toSqlDate(XMLGregorianCalendar date) 
	{
		if(date == null)
			return null;
		
		return new java.sql.Date(date.toGregorianCalendar().getTime().getTime());
	}
    
    public static javax.xml.datatype.XMLGregorianCalendar toXMLGregorianCalendar(java.sql.Timestamp timestamp) throws DatatypeConfigurationException
    {
        if (timestamp == null)
            return null;

        java.util.Date d = new java.util.Date();
        d.setTime(timestamp.getTime());
        
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(d);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);        
    }

    public static javax.xml.datatype.XMLGregorianCalendar toXMLGregorianCalendar(java.util.Date date) throws DatatypeConfigurationException
    {
        if (date == null)
            return null;

        java.util.Date d = new java.util.Date();
        d.setTime(date.getTime());
        
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(d);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);        
    }
    
    public static javax.xml.datatype.XMLGregorianCalendar toXMLGregorianCalendar(java.sql.Date date) throws DatatypeConfigurationException
    {
		if(date == null)
			return null;
		
		Date utilDate = new Date(date.getTime());
		
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(utilDate);
		
		return javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);		
    }

    public static java.sql.Timestamp toSQLDate(java.util.Date date)
    {
        if (date == null)
            return null;

        java.sql.Timestamp sqlTime = new java.sql.Timestamp(date.getTime());
        return sqlTime;
    }

    public static java.sql.Timestamp toSQLDate(LocalDate localDate)
    {
        if (localDate == null)
            return null;

        java.sql.Timestamp sqlTime = Timestamp.valueOf(localDate.atStartOfDay());
        return sqlTime;
    }

    public static java.sql.Timestamp toSQLDate(LocalDateTime localDateTime)
    {
        if (localDateTime == null)
            return null;

        java.sql.Timestamp sqlTime = Timestamp.valueOf(localDateTime);
        return sqlTime;
    }

	public static String formatDate(Date date, String format, String emptyDate) 
	{
        if (date == null)
            return emptyDate;
		
		return new SimpleDateFormat(format).format(date);
	}
    
}
