package ds.made.jdbc.easy.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

public class DatabaseUtils
{
	private static final Logger MYLOGGER = Logger.getLogger(DatabaseUtils.class.getName());
	
	private OracleConnectionPoolDataSource dataSource;
	
    public void createOracleSource(String user, String password, String url) throws SQLException
    {
        try
        {
            dataSource = new OracleConnectionPoolDataSource();

            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setURL(url);
            dataSource.setImplicitCachingEnabled(true);
            dataSource.setFastConnectionFailoverEnabled(true);

        }
        catch (SQLException e)
        {
            MYLOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }
    
    public Connection newConnection() throws SQLException
    {
        Connection c = dataSource.getConnection();
        c.setAutoCommit(false);
        return c;
    }
    
    public void closeConnection(Connection connection) throws SQLException
    {
    	if (connection != null)
    		connection.close();
    }
}
