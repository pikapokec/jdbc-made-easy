package ds.made.jdbc.easy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ds.made.jdbc.easy.annotations.EasyRow;
import ds.made.jdbc.easy.annotations.MapEasyAnnotations;
import ds.made.jdbc.easy.annotations.MapResultSet;
import ds.made.jdbc.easy.model.AnalysisBroke;
import ds.made.jdbc.easy.model.EasyColumnClassField;
import ds.made.jdbc.easy.model.EasyColumnFieldData;
import ds.made.jdbc.easy.model.Enums;
import ds.made.jdbc.easy.model.SomethingJustWrong;
import ds.made.jdbc.easy.utility.DBClosingManager;
import ds.made.jdbc.easy.utility.NamedParameterCallableStatement;
import ds.made.jdbc.easy.utility.Parameter;

/**
 * Simple stored procedure (cursor/out parameters) to List<T> mapper.<br/>
 * Can make also direct class instances if the setOtherClassFields configuration is set.<br/>
 * Can be used for procedures / functions with in out / out parameters. Use Object for generic and Object.class for class type. 
 * @author ds
 *
 * @param <T> Any simple {@link EasyRow} / {@link ./annotatons/EasyColumn} annotated class or Object.class.
 */
public class EasyCallForStoredProcedure<T>
{

	private static final Logger MYLOGGER = Logger.getLogger(EasyCallForStoredProcedure.class.getName());
	
	public final String storedName;
	public final Parameter[] parameters;
	public final Enums.STORED_PROCEDURE_TYPE type;
	
	private final Connection connection;
	private final Class<T> clazz;
	
	private String command;
	private Parameter returnParameter;
	
	private EasyColumnClassField[] nonAnnotatedFileds;
	private boolean nonAnnotated = false;
	private Integer rowLimit = null;
	
	/**
	 * No mapping! Use setOtherClassFields method. Made for stored with no cursor output.
	 * @param storedName SQL stored procedure
	 * @param connection Database connection
	 */
	public EasyCallForStoredProcedure(String storedName,Connection connection)
	{
		this.storedName = storedName;
		this.parameters = null;
		this.connection = connection;
		this.clazz = null;
		type = analyzeAndPrepare();
	}

	/**
	 * No mapping! Use setOtherClassFields method. Made for stored with no cursor output.
	 * @param storedName SQL stored procedure
	 * @param connection Database connection
	 * @param parameters Bind variables / procedure parameters
	 */
	public EasyCallForStoredProcedure(String storedName, Connection connection, Parameter... parameters)
	{
		this.storedName = storedName;
		this.parameters = parameters;
		this.connection = connection;
		this.clazz = null;
		type = analyzeAndPrepare();
	}

	/**
	 * Mapping.
	 * @param storedName SQL stored procedure
	 * @param connection Database connection
	 * @param clazz Annotated class
	 * @param parameters Bind variables / procedure parameters
	 */
	public EasyCallForStoredProcedure(String storedName, Connection connection,Class<T> clazz, Parameter... parameters)
	{
		this.storedName = storedName;
		this.parameters = parameters;
		this.clazz = clazz;
		this.connection = connection;
		type = analyzeAndPrepare();
	}
	
	/**
	 * Direct class instances with no annotation usage.
	 * @param fields Manually field to property mapping
	 * @return this instance
	 */
	public EasyCallForStoredProcedure<T> setOtherClassFields(EasyColumnClassField... fields)
	{
		this.nonAnnotatedFileds = fields;
		nonAnnotated = true;
		return this;
	}
	
	/**
	 * Limit the the result.
	 * @param rowLimit
	 */
	public EasyCallForStoredProcedure<T> setRowLimit(Integer rowLimit)
	{
		this.rowLimit = rowLimit;
		return this;
	}
	
	/**
	 * {@link NamedParameterCallableStatement} (CallableStatement) execute.
	 * @return CallableStatement.execute()
	 * @throws SQLException
	 */
	public boolean execute() throws SQLException
	{
		NamedParameterCallableStatement call = null;
		try
		{
			call = new NamedParameterCallableStatement(connection,command);
			return executeInternal(call);
		}
		catch (SQLException e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing!", e);
			throw e;
		}
		finally
		{
			DBClosingManager.closeAndContinue(call);
		}
	}

	/**
	 * {@link NamedParameterCallableStatement} (CallableStatement) execute; cursor result to List<T> mapping.
	 * @param cursor Cursor parameter
	 * @return List<T> class instances
	 * @throws SomethingJustWrong
	 */
	public List<T> executeAsList(Parameter cursor) throws SomethingJustWrong
	{
		return executeAsList(cursor,null,null);
	}

	/**
	 * {@link NamedParameterCallableStatement} (CallableStatement) execute; cursor result to List<T> mapping.<br/>
	 * Pagination.
	 * @param cursor Cursor parameter
	 * @param offset start row index (zero based)
	 * @param count number of rows
	 * @return List<T> class instances
	 * @throws SomethingJustWrong
	 */
	public List<T> executeAsList(Parameter cursor, Integer offset, Integer count) throws SomethingJustWrong
	{
		if (cursor == null)
			throw new SomethingJustWrong("No cursor to read from!");

		if (clazz == null)
			throw new SomethingJustWrong("No class definition! Use full constructor!");
		
		if (nonAnnotated)
			return executeAsListForNonAnnotatedClass(cursor);
		
		if (!clazz.isAnnotationPresent(EasyRow.class))
			throw new SomethingJustWrong("Class " + clazz.getName() + " must be @EasyRow annotatated!");

		MapEasyAnnotations aMapper = new MapEasyAnnotations(clazz);
		EasyColumnFieldData[] annotations = aMapper.propertiesList();
		if (annotations.length <= 0)
			throw new SomethingJustWrong("Class " + clazz.getName() + " must have @EasyColumn properties!");
		
		NamedParameterCallableStatement call = null;
		ResultSet rs = null;
		MapResultSet<T> mapper = null;
		try
		{
			call = new NamedParameterCallableStatement(connection,command);
			executeInternal(call);
			rs = (ResultSet)cursor.getValue();
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
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading cursor: " + (mapper == null ? "" : mapper.getLastFieldName()), sjw);
			throw sjw;
		}
		catch (SQLException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading cursor: " + (mapper == null ? "" : mapper.getLastFieldName()), se);
			throw new SomethingJustWrong(se);
		}
		catch (Exception e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading cursor!", e);
			throw new SomethingJustWrong(e);
		}
		finally
		{
			DBClosingManager.closeAndContinue(rs);
			DBClosingManager.closeAndContinue(call);
		}
	}
	
	/**
	 * Single row means one object.
	 * @param cursor Cursor parameter
	 * @return T class instance
	 * @throws SomethingJustWrong
	 */
	public T executeSingleResult(Parameter cursor) throws SomethingJustWrong
	{
		List<T> lst = executeAsList(cursor,0,1);
		if (lst == null || lst.size() != 1)
			return null;
		else
			return lst.get(0);
	}
	
	/**
	 * When T is just a predefined Java type (String,Integer,BigDecimal,Date,...). T is not annotated.
	 * @param cursor Cursor parameter
	 * @return List<T> class instances
	 * @throws SomethingJustWrong
	 */
	public List<T> executeAsListSingleColumn(Parameter cursor) throws SomethingJustWrong
	{
		if (cursor == null)
			throw new SomethingJustWrong("No cursor to read from!");

		if (clazz == null)
			throw new SomethingJustWrong("No class definition! Use full constructor!");
		
		if (clazz.isAnnotationPresent(EasyRow.class))
			throw new SomethingJustWrong("Class " + clazz.getName() + " must NOT be @EasyRow annotatated! Use executeAsList.");

		NamedParameterCallableStatement call = null;
		ResultSet rs = null;
		MapResultSet<T> mapper = null;
		try
		{
			call = new NamedParameterCallableStatement(connection,command);
			executeInternal(call);
			rs = (ResultSet)cursor.getValue();
			mapper = new MapResultSet<T>(clazz, rs);
			return mapper.mapSingleColumn();
		}
		catch (AnalysisBroke ab)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error analyzing!", ab);
			throw new SomethingJustWrong(ab);
		}
		catch (SomethingJustWrong sjw)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading cursor: " + (mapper == null ? "" : mapper.getLastFieldName()), sjw);
			throw sjw;
		}
		catch (SQLException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading cursor: " + (mapper == null ? "" : mapper.getLastFieldName()), se);
			throw new SomethingJustWrong(se);
		}
		catch (Exception e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading cursor!", e);
			throw new SomethingJustWrong(e);
		}
		finally
		{
			DBClosingManager.closeAndContinue(rs);
			DBClosingManager.closeAndContinue(call);
		}
	}

	private List<T> executeAsListForNonAnnotatedClass(Parameter cursor) throws SomethingJustWrong
	{
		if (nonAnnotatedFileds == null || nonAnnotatedFileds.length <= 0)
			throw new SomethingJustWrong("Definition " + clazz.getName() + " demands setOtherClassFields() method configuration!");
		
		NamedParameterCallableStatement call = null;
		ResultSet rs = null;
		MapResultSet<T> mapper = null;
		try
		{
			call = new NamedParameterCallableStatement(connection,command);
			executeInternal(call);
			rs = (ResultSet)cursor.getValue();
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
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading cursor: " + (mapper == null ? "" : mapper.getLastFieldName()), sjw);
			throw sjw;
		}
		catch (SQLException se)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading cursor: " + (mapper == null ? "" : mapper.getLastFieldName()), se);
			throw new SomethingJustWrong(se);
		}
		catch (Exception e)
		{
			MYLOGGER.log(Level.SEVERE, "Critical error executing or reading cursor!", e);
			throw new SomethingJustWrong(e);
		}
		finally
		{
			DBClosingManager.closeAndContinue(rs);
			DBClosingManager.closeAndContinue(call);
		}
	}
	
	
	private boolean executeInternal(NamedParameterCallableStatement call) throws SQLException
	{
		if (parameters != null)
		{
			for (Parameter p : parameters)
			{
				switch (p.direction)
				{
					case IN:
						call.setObject(p.name, p.getValueForExecute());
						break;

					case OUT:
						call.registerOutParameter(p.name, p.type);
						break;
						
					case IN_OUT:
						call.registerOutParameter(p.name, p.type);
						call.setObject(p.name, p.getValueForExecute());
						break;
						
					case RETURN:
						call.registerOutParameter(p.name, p.type);
						break;
						
				}
			}
		}
		
		boolean b = call.execute();

		if (parameters != null)
		{
			for (Parameter p : parameters)
			{
				switch (p.direction)
				{
					case OUT:
					case IN_OUT:
					case RETURN:
						p.setValueOutDirection(call.getObject(p.name));
						break;
						
					default:
						break;
				}
			}
		}
		
		return b;
	}
	
	private Enums.STORED_PROCEDURE_TYPE analyzeAndPrepare()
	{
		StringBuilder sbParams = new StringBuilder();
		Enums.STORED_PROCEDURE_TYPE t = Enums.STORED_PROCEDURE_TYPE.PROCEDURE;
		returnParameter = null;
		
		if (parameters != null)
		{
			for (Parameter p : parameters)
			{
				if (p.direction == Enums.PARAMETER_DIRECTION.RETURN)
				{
					t = Enums.STORED_PROCEDURE_TYPE.FUNCTION;
					returnParameter = p;
				}
				else
				{
					sbParams.append(",:" + p.name);
				}
			}
			
			sbParams.deleteCharAt(0);
		}
		
		switch (t)
		{
			case FUNCTION:
				command = "{:" + returnParameter.name + " = call " + storedName + "(" + sbParams.toString() + ")}";
				break;

			case PROCEDURE:
				command = "{call " + storedName + "(" + sbParams.toString() + ")}";
				break;
		}
		return t;
	}
	
}
