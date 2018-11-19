package ds.made.jdbc.easy;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ds.made.jdbc.easy.annotations.EasyColumn;
import ds.made.jdbc.easy.annotations.EasyRow;
import ds.made.jdbc.easy.annotations.MapEasyAnnotations;
import ds.made.jdbc.easy.annotations.MapResultSet;
import ds.made.jdbc.easy.annotations.MapScalar;
import ds.made.jdbc.easy.model.*;
import ds.made.jdbc.easy.utility.DBClosingManager;
import ds.made.jdbc.easy.utility.NamedParameterPreparedStatement;
import ds.made.jdbc.easy.utility.Parameter;

/**
 * Simple Select to List<T> mapper.<br/>
 * Can make also direct class instances if the setOtherClassFields configuration is set.
 * @author ds
 *
 * @param <T> Any simple {@link EasyRow} / {@link EasyColumn} annotated class.
 */
public class EasyPreparedStatement<T>
{
	private static final Logger MYLOGGER = Logger.getLogger(EasyPreparedStatement.class.getName());

	private final Connection connection;
	private final Class<T> clazz;
	private final String command;
	public final Parameter[] parameters;
	private EasyColumnClassField[] nonAnnotatedFileds;
	private boolean nonAnnotated = false;
	private Integer rowLimit = null;
	
	/**
	 * 
	 * @param command SQL command (Select)
	 * @param connection Database connection
	 * @param clazz Annotated class
	 */
	public EasyPreparedStatement(String command, Connection connection, Class<T> clazz)
	{
		this.connection = connection;
		this.clazz = clazz;
		this.command = command;
		this.parameters = null;
	}
	
	/**
	 * 
	 * @param command SQL command (Select)
	 * @param connection Database connection
	 * @param clazz Annotated class
	 * @param parameters Bind variables
	 */
	public EasyPreparedStatement(String command, Connection connection, Class<T> clazz, Parameter... parameters)
	{
		this.connection = connection;
		this.clazz = clazz;
		this.command = command;
		this.parameters = parameters;
	}
	
	/**
	 * Direct class instances with no annotation usage.
	 * @param fields Manually field to property mapping
	 * @return this instance
	 */
	public EasyPreparedStatement<T> setOtherClassFields(EasyColumnClassField... fields)
	{
		this.nonAnnotatedFileds = fields;
		nonAnnotated = true;
		return this;
	}
	
	/**
	 * Limit the the result.
	 * @param rowLimit
	 */
	public EasyPreparedStatement<T> setRowLimit(Integer rowLimit)
	{
		this.rowLimit = rowLimit;
		return this;
	}
	
	/**
	 * Just one single object; e.g. Select F(?) From DUAL
	 * @return T class instance
	 * @throws SomethingJustWrong
	 * @throws EasyStatementNoSingleObject
	 */
	public T executeAsScalar() throws SomethingJustWrong, EasyStatementNoSingleObject
	{
		NamedParameterPreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			statement = new NamedParameterPreparedStatement(connection,command);
			fillParameters(statement);
			rs = statement.executeQuery();
			MapScalar<T> map = new MapScalar<T>(clazz,rs);
			return map.map();
		}
		catch (SQLException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing scalar query!", se);
			throw new SomethingJustWrong(se);
		}
		finally
		{
			DBClosingManager.closeAndContinue(rs);
			DBClosingManager.closeAndContinue(statement);
		}
	}
	
	/**
	 * Maps BLOB to byte array.
	 * @return byte array
	 * @throws SomethingJustWrong
	 * @throws EasyStatementNoSingleObject
	 */
	public byte[] blobScalar() throws SomethingJustWrong, EasyStatementNoSingleObject
	{
		NamedParameterPreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			statement = new NamedParameterPreparedStatement(connection,command);
			fillParameters(statement);
			rs = statement.executeQuery();
			MapScalar<T> map = new MapScalar<T>(clazz,rs);
			return map.mapBLOB();
		}
		catch (SQLException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing scalar query!", se);
			throw new SomethingJustWrong(se);
		}
		catch (IOException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing scalar query - reading BLOB!", se);
			throw new SomethingJustWrong(se);
		}
		finally
		{
			DBClosingManager.closeAndContinue(rs);
			DBClosingManager.closeAndContinue(statement);
		}
	}
	
	/**
	 * Maps CLOB to String.
	 * @return String
	 * @throws SomethingJustWrong
	 * @throws EasyStatementNoSingleObject
	 */
	public String clobScalar() throws SomethingJustWrong, EasyStatementNoSingleObject
	{
		NamedParameterPreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			statement = new NamedParameterPreparedStatement(connection,command);
			fillParameters(statement);
			rs = statement.executeQuery();
			MapScalar<T> map = new MapScalar<T>(clazz,rs);
			return map.mapCLOB();
		}
		catch (SQLException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing scalar query!", se);
			throw new SomethingJustWrong(se);
		}
		catch (IOException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing scalar query - reading BLOB!", se);
			throw new SomethingJustWrong(se);
		}
		finally
		{
			DBClosingManager.closeAndContinue(rs);
			DBClosingManager.closeAndContinue(statement);
		}
	}
	
	/**
	 * Single row means one object.
	 * @return T class instance
	 * @throws SomethingJustWrong
	 */
	public T executeSingleResult() throws SomethingJustWrong
	{
		List<T> lst = executeAsList(0,1);
		if (lst == null || lst.size() != 1)
			return null;
		else
			return lst.get(0);
	}
	
	/**
	 * Single row means one object.
	 * @return T class instance
	 * @throws SomethingJustWrong
	 * @throws EasyStatementNoSingleObject .. when there is no database row!
	 */
	public T executeSingleResultThrow() throws SomethingJustWrong, EasyStatementNoSingleObject
	{
		List<T> lst = executeAsList(0,1);
		if (lst == null || lst.size() != 1)
			throw new EasyStatementNoSingleObject("No data or too many rows!");
		else
			return lst.get(0);
	}
	
	/**
	 * List of mapped rows.
	 * @return List<T> class instances
	 * @throws SomethingJustWrong
	 */
	public List<T> executeAsList() throws SomethingJustWrong
	{
		return executeAsList(null,null);
	}
	
	/**
	 * List of mapped rows. Pagination.
	 * @param offset start row index (zero based)
	 * @param count number of rows
	 * @return List<T> class instances
	 * @throws SomethingJustWrong
	 */
	public List<T> executeAsList(Integer offset, Integer count) throws SomethingJustWrong
	{
		if (nonAnnotated)
			return executeAsListForNonAnnotatedClass();
		
		if (!clazz.isAnnotationPresent(EasyRow.class))
			throw new SomethingJustWrong("Class " + clazz.getName() + " must be @EasyRow annotatated!");

		MapEasyAnnotations aMapper = new MapEasyAnnotations(clazz);
		EasyColumnFieldData[] annotations = aMapper.propertiesList();
		if (annotations.length <= 0)
			throw new SomethingJustWrong("Class " + clazz.getName() + " must have @EasyColumn properties!");

		NamedParameterPreparedStatement statement = null;
		ResultSet rs = null;
		MapResultSet<T> mapper = null;
		try
		{
			statement = new NamedParameterPreparedStatement(connection,command);
			fillParameters(statement);
			rs = statement.executeQuery();
			mapper = new MapResultSet<T>(clazz, annotations, rs);
			mapper.setRowLimit(rowLimit);
			if (offset != null && count != null)
				return mapper.map(offset, count);
			else
				return mapper.map();
		}
		catch (AnalysisBroke ab)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error analyzing!", ab);
			throw new SomethingJustWrong(ab);
		}
		catch (SomethingJustWrong sjw)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading query: " + (mapper == null ? "" : mapper.getLastFieldName()), sjw);
			throw sjw;
		}
		catch (SQLException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading query: " + (mapper == null ? "" : mapper.getLastFieldName()), se);
			throw new SomethingJustWrong(se);
		}
		catch (Exception e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading query!", e);
			throw new SomethingJustWrong(e);
		}
		finally
		{
			DBClosingManager.closeAndContinue(rs);
			DBClosingManager.closeAndContinue(statement);
		}
	}	
	
	/**
	 * List of mapped rows.
	 * @return List<T> class instances
	 * @throws SomethingJustWrong
	 */
	private List<T> executeAsListForNonAnnotatedClass() throws SomethingJustWrong
	{
		if (nonAnnotatedFileds == null || nonAnnotatedFileds.length <= 0)
			throw new SomethingJustWrong("Definition " + clazz.getName() + " demands setOtherClassFields() method configuration!");

		NamedParameterPreparedStatement statement = null;
		ResultSet rs = null;
		MapResultSet<T> mapper = null;
		try
		{
			statement = new NamedParameterPreparedStatement(connection,command);
			fillParameters(statement);
			rs = statement.executeQuery();
			mapper = new MapResultSet<T>(clazz, nonAnnotatedFileds, rs);
			mapper.setRowLimit(rowLimit);
			return mapper.map();
		}
		catch (AnalysisBroke ab)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error analyzing!", ab);
			throw new SomethingJustWrong(ab);
		}
		catch (SomethingJustWrong sjw)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading query: " + (mapper == null ? "" : mapper.getLastFieldName()), sjw);
			throw sjw;
		}
		catch (SQLException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading query: " + (mapper == null ? "" : mapper.getLastFieldName()), se);
			throw new SomethingJustWrong(se);
		}
		catch (Exception e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading query!", e);
			throw new SomethingJustWrong(e);
		}
		finally
		{
			DBClosingManager.closeAndContinue(rs);
			DBClosingManager.closeAndContinue(statement);
		}
	}

	public EasyDataTable dataTable() throws SomethingJustWrong
	{
		return dataTable(null,null);
	}

	public EasyDataTable dataTable(Integer offset, Integer count) throws SomethingJustWrong
	{
		NamedParameterPreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			statement = new NamedParameterPreparedStatement(connection,command);
			fillParameters(statement);
			rs = statement.executeQuery();
			EasyDataTable dt = new EasyDataTable(rs);
			dt.setRowLimit(rowLimit);
			if (offset != null && count != null)
				dt.read(offset, count);
			else
				dt.read();
			return dt;
		}
		catch (AnalysisBroke ab)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error analyzing!", ab);
			throw new SomethingJustWrong(ab);
		}
		catch (SQLException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading query!", se);
			throw new SomethingJustWrong(se);
		}
		catch (Exception e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading query!", e);
			throw new SomethingJustWrong(e);
		}
		finally
		{
			DBClosingManager.closeAndContinue(rs);
			DBClosingManager.closeAndContinue(statement);
		}
	}

	private void fillParameters(NamedParameterPreparedStatement statement) throws SQLException, SomethingJustWrong
	{
		if (parameters != null)
		{
			for (Parameter p : parameters)
			{
				switch (p.getDirection())
				{
					case IN:
						statement.setObject(p.name, p.getValueForExecute());
						break;
						
					default:
						throw new SomethingJustWrong("Wrong parameter direction : " + p.name + " !");

				}
			}
		}
	}
}
