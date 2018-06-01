package ds.made.jdbc.easy.utility;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 This class wraps around a {@link CallableStatement} and allows the programmer
 to set parameters by name instead of by index. This eliminates any confusion
 as to which parameter index represents what. This also means that rearranging
 the SQL call statement or adding a parameter doesn't involve renumbering your
 indices.
<br/><br/>
Made from the NamedParameterPreparedStatement template.
 *
 *
 * @author ds
 */

@SuppressWarnings("rawtypes")
public class NamedParameterCallableStatement
{

    /**
     * The statement this object is wrapping.
     */
    private final CallableStatement statement;
    
    private final Connection connection;
    
    /**
     * Maps parameter names to arrays of ints which are the parameter indices.
     */
    private final Map indexMap;

    private List<java.sql.Clob> clobs;
    private List<java.sql.Blob> blobs;
    
    /**
     * Creates a NamedParameterStatement. Wraps a call to
     * c.{@link Connection#prepareStatement(java.lang.String) prepareStatement}.
     *
     * @param connection the database connection
     * @param query the parameterized query
     * @throws SQLException if the statement could not be created
     */
    public NamedParameterCallableStatement(Connection connection, String command) throws
            SQLException
    {
    	this.connection = connection;
    	
        indexMap = new HashMap();
        String parsedQuery = NamedParameterUtils.parse(command, indexMap);
        statement = connection.prepareCall(parsedQuery);
    }

    /**
     * Returns the indexes for a parameter.
     *
     * @param name parameter name
     * @return parameter indexes
     * @throws IllegalArgumentException if the parameter does not exist
     */
    private int[] getIndexes(String name)
    {
        int[] indexes = (int[]) indexMap.get(name);
        if (indexes == null)
        {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        return indexes;
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(String name, Object value) throws SQLException
    {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++)
        {
            statement.setObject(indexes[i], value);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(String name, String value) throws SQLException
    {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++)
        {
            statement.setString(indexes[i], value);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     */
    public void setInt(String name, Integer value) throws SQLException
    {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++)
        {
            if (value == null)
                statement.setNull(indexes[i], Types.INTEGER);
            else
                statement.setInt(indexes[i], value);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     */
    public void setLong(String name, Long value) throws SQLException
    {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++)
        {
            if (value == null)
                statement.setNull(indexes[i], Types.INTEGER);
            else
                statement.setLong(indexes[i], value);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(String name, Timestamp value) throws SQLException
    {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++)
        {
            statement.setTimestamp(indexes[i], value);
        }
    }

    public void setDate(String name, java.util.Date date) throws SQLException
    {
        java.sql.Timestamp ts = null;
        if (date != null)
        {
            ts = new Timestamp(date.getTime());
            setTimestamp(name, ts);
        }
        setTimestamp(name, ts);
    }

    public void setBigDecimal(String name, BigDecimal value) throws SQLException
    {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++)
        {
            statement.setBigDecimal(indexes[i], value);
        }
    }

    public void setBooleanString(String name, Boolean value, String trueValue, String falseValue) throws SQLException
    {
        if (value == null)
            setString(name, falseValue);
        else if (value)
            setString(name, trueValue);
        else
            setString(name, falseValue);
    }

    /**
     * Easy call for setting BLOB value.<br/>
     * <b>WARNING! Closing on statement.close() or (in a loop) explicitly with closeAllBlobsAndClobs()!</b>
     * @param name parameter name
     * @param value blob value
     * @throws SQLException
     */
    public void setBlob(String name, byte[] value) throws SQLException
    {
        int[] indexes = getIndexes(name);
        if (value == null)
        {
	        for (int i = 0; i < indexes.length; i++)
	            statement.setObject(indexes[i], null);
        }
        else
        {
        	java.sql.Blob blob = connection.createBlob();
	    	blob.setBytes(1, value);
	        for (int i = 0; i < indexes.length; i++)
	            statement.setBlob(indexes[i], blob);

	        addBlobToFreeList(blob);
        }
    }

    /**
     * Easy call for setting CLOB value.<br/>
     * <b>WARNING! Closing on statement.close() or (in a loop) explicitly with closeAllBlobsAndClobs()!</b>
     * @param parameter name
     * @param value clob value
     * @throws SQLException
     */
	public void setClob(String name, String value) throws SQLException
    {
        int[] indexes = getIndexes(name);
        
        if (value == null)
        {
	        for (int i = 0; i < indexes.length; i++)
	            statement.setObject(indexes[i], null);
        }
        else
        {
        	java.sql.Clob clob = connection.createClob();
	    	clob.setString(1, value);
	        for (int i = 0; i < indexes.length; i++)
	            statement.setClob(indexes[i], clob);
	        
	        addClobToFreeList(clob);
        }
    }
    
	public Object getObject(String name) throws SQLException
    {
        int[] indexes = getIndexes(name);
        if (indexes.length > 0)
            return statement.getObject(indexes[0]);
        else
            return null;
    }

    public Integer getInt(String name) throws SQLException
    {
        int[] indexes = getIndexes(name);
        if (indexes.length > 0)
            return statement.getInt(indexes[0]);
        else
            return null;
    }

    public Long getLong(String name) throws SQLException
    {
        int[] indexes = getIndexes(name);
        if (indexes.length > 0)
            return statement.getLong(indexes[0]);
        else
            return null;
    }

    public String getString(String name) throws SQLException
    {
        int[] indexes = getIndexes(name);
        if (indexes.length > 0)
            return statement.getString(indexes[0]);
        else
            return null;
    }

    public BigDecimal getBigDecimal(String name) throws SQLException
    {
        int[] indexes = getIndexes(name);
        if (indexes.length > 0)
            return statement.getBigDecimal(indexes[0]);
        else
            return null;
    }

    public java.util.Date getDate(String name) throws SQLException
    {
        int[] indexes = getIndexes(name);
        if (indexes.length > 0)
        	return DateUtils.toDate(statement.getTimestamp(indexes[0]));
        else
            return null;
    }
    
    public void registerOutParameter(String name, int type) throws SQLException
    {
        int[] indexes = getIndexes(name);
        if (indexes.length == 1)
            statement.registerOutParameter(indexes[0], type);
        else
            throw new SQLException("Ne najdem ene pojavitve parametra " + name + "!");
    }

    /**
     * Returns the underlying statement.
     *
     * @return the statement
     */
    public CallableStatement getStatement()
    {
        return statement;
    }

    /**
     * Executes the statement.
     *
     * @return true if CallableStatement execute is true.
     * @throws SQLException if an error occurred
     * @see CallableStatement#execute()
     */
    public boolean execute() throws SQLException
    {
        return statement.execute();
    }

    /**
     * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE
     * statement; or an SQL statement that returns nothing, such as a DDL
     * statement.
     *
     * @return number of rows affected
     * @throws SQLException if an error occurred
     * @see CallableStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException
    {
        return statement.executeUpdate();
    }

    /**
     * Closes the statement.<br/><b>Closes all Blobs and Clobs!</b>
     *
     * @throws SQLException if an error occurred
     * @see Statement#close()
     */
    public void close() throws SQLException
    {
    	closeAllBlobsAndClobs();
        statement.close();
    }

    public void closeAllBlobsAndClobs()
	{
		if (clobs != null)
		{
			for (java.sql.Clob clob : clobs)
				Lobs.freeClob(clob);
		}
		clobs = null;
		
		if (blobs != null)
		{
			for (java.sql.Blob blob : blobs)
				Lobs.freeBlob(blob);
		}
		blobs = null;
	}

	private void addBlobToFreeList(Blob blob)
	{
		if (blobs == null)
			blobs = new ArrayList<Blob>();
		blobs.add(blob);
	}
	
    private void addClobToFreeList(Clob clob)
	{
		if (clobs == null)
			clobs = new ArrayList<Clob>();
		clobs.add(clob);
	}

    
}
