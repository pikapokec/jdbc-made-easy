package ds.made.jdbc.easy.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
Utility for easy closing standard Statement connections.<br/>
This manager closes silently (...andContinue pattern), logs if errors and returns Exceptions if any.<br/>
Usage simplifies try / finally blocks.
Of course it is a mere of taste.

@author ds
*/
public class DBClosingManager
{
	
	private static final Logger MYLOGGER = Logger.getLogger(DBClosingManager.class.getName());

	public static void closeConnection(Connection connection) throws SQLException
	{
		if (connection != null)
			connection.close();
	}

	public static Exception closeAndContinue(ResultSet set)
	{
		try
		{
			if (set != null)
				set.close();
			return null;
		}
		catch (Exception e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error closing ResultSet!", e);
			return e;
		}
	}

	public static Exception closeAndContinue(Statement statement)
	{
		try
		{
			if (statement != null)
				statement.close();
			return null;
		}
		catch (Exception e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error closing Statement!", e);
			return e;
		}
	}

	public static Exception closeAndContinue(NamedParameterCallableStatement statement)
	{
		try
		{
			if (statement != null)
				statement.close();
			return null;
		}
		catch (Exception e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error closing NamedParameterCallableStatement!", e);
			return e;
		}
	}

	public static Exception closeAndContinue(NamedParameterPreparedStatement statement)
	{
		try
		{
			if (statement != null)
				statement.close();
			return null;
		}
		catch (Exception e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error closing NamedParameterPreparedStatement!", e);
			return e;
		}
	}
	
	public static Exception close(ResultSet set, Statement statement, Connection connection) throws SQLException
	{
		Exception e1 = closeAndContinue(set);
		Exception e2 = closeAndContinue(statement);
		if (connection != null)
			connection.close();
		
		if (e1 == null && e2 == null)
			return null;
		else if (e1 != null && e2 == null)
			return e1;
		else if (e1 == null && e2 != null)
			return e2;
		else
			return new Exception(e1.getMessage() + "\n" + e2.getMessage());
	}
	
}
