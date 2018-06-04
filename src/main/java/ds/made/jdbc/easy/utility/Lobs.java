package ds.made.jdbc.easy.utility;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
Utility for handling Oracle CLOB and BLOB objects.
@author ds
*/
public class Lobs 
{

	private static final Logger logger = Logger.getLogger(Lobs.class.getName());
	private static final int PORTION = 2048;
	
	/**
	 * Converts Oracle BLOB to byte array. All streams are closed in the finally block.
	 * @param blob java.sql.Blob 
	 * @return byte array
	 * @throws IOException
	 * @throws SQLException
	 */
	public static byte[] convertBlobToArray(java.sql.Blob blob) throws IOException, SQLException
	{
		if (blob == null)
			return null;
		
		InputStream is = null;
		ByteArrayOutputStream  buffer = null;
		try
		{
	        is = blob.getBinaryStream();
	        buffer = new ByteArrayOutputStream ();
	        int nRead;
	        byte[] data = new byte[PORTION];
	        while ((nRead = is.read(data, 0, data.length)) != -1) 
	        	buffer.write(data, 0, nRead);
	        buffer.flush();
	        return buffer.toByteArray();
		}
		catch (IOException e)
		{
			throw e;
		} 
		catch (SQLException e) 
		{
			throw e;
		}
		finally
		{
			closeStream(is);
			closeStream(buffer);
		}
	}

	/**
	 * Converts Oracle BLOB to byte array and frees it. All streams are closed in the finally block.
	 * @param parameter Parameter 
	 * @return byte array
	 * @throws IOException
	 * @throws SQLException
	 */
	public static byte[] convertBlobParameterToArrayAndFree(Parameter parameter) throws IOException, SQLException
	{
		if (parameter == null)
			return null;
		
		if (!(parameter.getValue() instanceof java.sql.Blob))
			throw new SQLException("Not a java.sql.Blob instance!");
		
		java.sql.Blob blob = (java.sql.Blob)parameter.getValue();
		byte[] b = convertBlobToArray(blob);
		blob.free();
		return b;
	}
	
	/**
	 * Converts Oracle CLOB to String. All streams are closed in the finally block.
	 * @param clob java.sql.Clob 
	 * @return String
	 * @throws IOException
	 * @throws SQLException
	 */
	public static String convertClobToString(java.sql.Clob clob) throws IOException, SQLException
	{
		if (clob == null)
			return null;
		
		Reader r = null;
		BufferedReader buffer = null;
		StringBuilder sb = new StringBuilder();
		try
		{
	        r = clob.getCharacterStream();
	        buffer = new BufferedReader(r);
	        int b;
	        while(-1 != (b = buffer.read()))
	            sb.append((char)b);
	        return sb.toString();
		}
		catch (IOException e)
		{
			throw e;
		} 
		catch (SQLException e) 
		{
			throw e;
		}
		finally
		{
			closeReader(buffer);
			closeReader(r);
		}
	}
	
	/**
	 * Converts Oracle CLOB to String and frees it. All streams are closed in the finally block.
	 * @param parameter Parameter 
	 * @return String
	 * @throws IOException
	 * @throws SQLException
	 */
	public static String convertClobParameterToStringAndFree(Parameter parameter) throws IOException, SQLException
	{
		if (parameter == null)
			return null;
		
		if (!(parameter.getValue() instanceof java.sql.Clob))
			throw new SQLException("Not a java.sql.Clob instance!");
		
		java.sql.Clob clob = (java.sql.Clob)parameter.getValue();
		String c = convertClobToString(clob);
		clob.free();
		return c;
	}
	
	private static void closeReader(java.io.Reader r)
	{
		try
		{
			if (r != null)
				r.close();
		} 
		catch (IOException e)
		{
			logger.severe("Error while closing the reader " + e.toString());
		}
	}
	
	public static void closeStream(java.io.InputStream is)
	{
		try
		{
			if (is != null)
				is.close();
		} 
		catch (IOException e)
		{
			logger.severe("Error while closing the input stream " + e.toString());
		}
	}
	
	public static void closeStream(java.io.OutputStream os)
	{
		try
		{
			if (os != null)
				os.close();
		} 
		catch (IOException e)
		{
			logger.severe("Error while closing the output stream " + e.toString());
		}
	}
	
	/**
	 * Free java.sql.Blob.
	 * @param blob
	 */
	public static void freeBlob(java.sql.Blob blob)
	{
		try
		{
			if (blob != null)
				blob.free();
		} 
		catch (SQLException e)
		{
			logger.severe("Error while closing the blob " + e.toString());
		}
	}
	
	/**
	 * Free java.sql.Clob.
	 * @param clob
	 */
	public static void freeClob(java.sql.Clob clob)
	{
		try
		{
			if (clob != null)
				clob.free();
		} 
		catch (SQLException e)
		{
			logger.severe("Error while closing the clob " + e.toString());
		}
	}
	
}
