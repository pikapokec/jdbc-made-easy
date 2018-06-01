package ds.made.jdbc.easy.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * This class wraps around a {@link PreparedStatement} and allows the programmer
 * to set parameters by name instead of by index. This eliminates any confusion
 as to which parameter index represents what. This also means that rearranging
 the SQL statement or adding a parameter doesn't involve renumbering your
 indices. Code such as this:

 Connection con=getConnection(); String query="select * from my_table where
 name=? or address=?"; PreparedStatement p=con.prepareStatement(query);
 p.setString(1, "bob"); p.setString(2, "123 terrace ct"); ResultSet
 rs=p.executeQuery();

 can be replaced with:

 Connection con=getConnection();
 String query="select * from my_table where name=:name or address=:address";
 NamedParameterPreparedStatement p=new NamedParameterPreparedStatement(con, query);
 p.setString("name", "bob");
 p.setString("address", "123 terrace ct");
 ResultSet rs=p.executeQuery();
 *
 *
 * @author adam_crume
 */
@SuppressWarnings("rawtypes")
public class NamedParameterPreparedStatement
{

    /**
     * The statement this object is wrapping.
     */
    private final PreparedStatement statement;

    /**
     * Maps parameter names to arrays of ints which are the parameter indices.
     */
	private final Map indexMap;

    /**
     * Creates a NamedParameterStatement. Wraps a call to
     * c.{@link Connection#prepareStatement(java.lang.String) prepareStatement}.
     *
     * @param connection the database connection
     * @param query the parameterized query
     * @throws SQLException if the statement could not be created
     */
    public NamedParameterPreparedStatement(Connection connection, String query) throws
            SQLException
    {
        indexMap = new HashMap();
        String parsedQuery = NamedParameterUtils.parse(query, indexMap);
        statement = connection.prepareStatement(parsedQuery);
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

    /**
     * Returns the underlying statement.
     *
     * @return the statement
     */
    public PreparedStatement getStatement()
    {
        return statement;
    }

    /**
     * Executes the statement.
     *
     * @return true if the first result is a {@link ResultSet}
     * @throws SQLException if an error occurred
     * @see PreparedStatement#execute()
     */
    public boolean execute() throws SQLException
    {
        return statement.execute();
    }

    /**
     * Executes the statement, which must be a query.
     *
     * @return the query results
     * @throws SQLException if an error occurred
     * @see PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException
    {
        return statement.executeQuery();
    }

    /**
     * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE
     * statement; or an SQL statement that returns nothing, such as a DDL
     * statement.
     *
     * @return number of rows affected
     * @throws SQLException if an error occurred
     * @see PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException
    {
        return statement.executeUpdate();
    }

    /**
     * Closes the statement.
     *
     * @throws SQLException if an error occurred
     * @see Statement#close()
     */
    public void close() throws SQLException
    {
        statement.close();
    }

}
