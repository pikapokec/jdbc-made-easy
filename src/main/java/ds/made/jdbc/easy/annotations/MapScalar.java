package ds.made.jdbc.easy.annotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import ds.made.jdbc.easy.model.EasyStatementNoSingleObject;
import ds.made.jdbc.easy.model.SomethingJustWrong;
import ds.made.jdbc.easy.utility.DBHelper;
import ds.made.jdbc.easy.utility.Lobs;

/**
Maps JDBC objects to annotated properties.
For internal usage.
@author ds
 */
public class MapScalar<T>
{

	private final Class<?> clazz;
	private final ResultSet resultset;
	
	public MapScalar(Class<?> clazz, ResultSet resultset)
	{
		this.clazz = clazz;
		this.resultset = resultset;
	}
	
	@SuppressWarnings("unchecked")
	public T map() throws SomethingJustWrong, EasyStatementNoSingleObject, SQLException
	{
		if (resultset.next()) 
		{
			Object o = null;
			if (clazz.equals(String.class))
				o = DBHelper.toString(1, resultset);
			else if (clazz.equals(java.util.Date.class))
				o = DBHelper.toDate(1, resultset);
			else if (clazz.equals(LocalDate.class))
				o = DBHelper.toLocalDate(1, resultset);
			else if (clazz.equals(LocalDateTime.class))
				o = DBHelper.toLocalDateTime(1, resultset);
			else if (clazz.equals(BigDecimal.class))
				o = DBHelper.toBigDecimal(1, resultset);
			else if (clazz.equals(Integer.class))
				o = DBHelper.toInteger(1, resultset);
			else if (clazz.equals(Long.class))
				o = DBHelper.toLong(1, resultset);
			else if (clazz.equals(Short.class))
				o = DBHelper.toShort(1, resultset);
			else
				throw new SomethingJustWrong("What is this type " + clazz.getName() + " ?");
			
			return (T)o;
		} 
		else 
		{
	        throw new EasyStatementNoSingleObject("Cannot read any results!");
	    }			
	}
	
	public byte[] mapBLOB() throws SomethingJustWrong, EasyStatementNoSingleObject, SQLException, IOException
	{
		if (resultset.next()) 
			return Lobs.convertBlobToArray(resultset.getBlob(1));
		else 
	        throw new EasyStatementNoSingleObject("Cannot read any results!");
	}
	
	public String mapCLOB() throws SomethingJustWrong, EasyStatementNoSingleObject, SQLException, IOException
	{
		if (resultset.next()) 
			return Lobs.convertClobToString(resultset.getClob(1));
		else 
	        throw new EasyStatementNoSingleObject("Cannot read any results!");
	}
	
}
