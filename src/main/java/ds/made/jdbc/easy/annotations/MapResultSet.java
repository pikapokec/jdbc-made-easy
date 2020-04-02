package ds.made.jdbc.easy.annotations;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import ds.made.jdbc.easy.model.*;
import ds.made.jdbc.easy.utility.DBHelper;
import ds.made.jdbc.easy.utility.Lobs;

/**
Maps JDBC objects to annotated properties.
For internal usage.
@author ds
 */
public class MapResultSet<T>
{
	private final Class<?> clazz;
	private final ResultSet resultset;
	private final Map<String, Method> setters;
	private final List<EasyColumnFieldData> myPropertiesList;
	private final List<EasyColumnClassField> myPropertiesNonAnnotatedList;
	private final EasyColumnFieldData[] properties;
	private final EasyColumnClassField[] nonAnnotatedFileds;
	private boolean nonAnnotated = false;
	private Integer rowLimit = null;
	
	private String lastFieldName = "";

	public MapResultSet(Class<?> clazz, ResultSet resultset) throws AnalysisBroke
	{
		this.clazz = clazz;
		this.resultset = resultset;
		this.properties = null;
		this.nonAnnotatedFileds = null;
		this.myPropertiesNonAnnotatedList = null;
		this.setters = new HashMap<String, Method>();
		this.myPropertiesList = new ArrayList<EasyColumnFieldData>();
		nonAnnotated = false;
	}
	
	public MapResultSet(Class<?> clazz, EasyColumnFieldData[] properties, ResultSet resultset) throws AnalysisBroke
	{
		this.clazz = clazz;
		this.resultset = resultset;
		this.properties = properties;
		this.nonAnnotatedFileds = null;
		this.myPropertiesNonAnnotatedList = null;
		this.setters = new HashMap<String, Method>();
		this.myPropertiesList = new ArrayList<EasyColumnFieldData>();
		nonAnnotated = false;
		analyze();
	}

	public MapResultSet(Class<?> clazz,EasyColumnClassField[] nonAnnotatedFileds, ResultSet resultset) throws AnalysisBroke
	{
		this.clazz = clazz;
		this.resultset = resultset;
		this.properties = null;
		this.nonAnnotatedFileds = nonAnnotatedFileds;
		this.myPropertiesList = null;
		this.setters = new HashMap<String, Method>();
		this.myPropertiesNonAnnotatedList = new ArrayList<EasyColumnClassField>();
		nonAnnotated = true;
		analyzeNonAnnotated();
	}
	
	public void setRowLimit(Integer rowLimit)
	{
		this.rowLimit = rowLimit;
	}
	
	private void analyze() throws AnalysisBroke
	{
		try
		{
			// Returns descriptors for all properties of the bean.
			for(PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors())
				// Gets the method that should be used to write the property value.
				setters.put(pd.getName(), pd.getWriteMethod());
			
			ResultSetMetaData rsmd = resultset.getMetaData();
			for (int idx = 1; idx <= rsmd.getColumnCount(); idx++)
			{
				// Database name
				String name = rsmd.getColumnName(idx);
				// Properties from constructor ... annotated fields
				for (EasyColumnFieldData f : properties)
				{
					// If annotated name equals column name regardless of capitalising, then they are equal!
					if (f.column.name().equalsIgnoreCase(name))
					{
						// Add this field to my property list
						myPropertiesList.add(f);
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new AnalysisBroke(e);
		}
	}

	private void analyzeNonAnnotated() throws AnalysisBroke
	{
		try
		{
			for(PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors())
				setters.put(pd.getName(), pd.getWriteMethod());
			
			ResultSetMetaData rsmd = resultset.getMetaData();
			for (int idx = 1; idx <= rsmd.getColumnCount(); idx++)
			{
				String name = rsmd.getColumnName(idx);
				for (EasyColumnClassField n : nonAnnotatedFileds)
				{
					if (n.dbFieldName.equalsIgnoreCase(name))
					{
						myPropertiesNonAnnotatedList.add(n);
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new AnalysisBroke(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<T> map() throws SomethingJustWrong, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SQLException, SecurityException, NoSuchMethodException, InstantiationException, IOException, DatatypeConfigurationException, IntrospectionException
	{
		if (nonAnnotated)
			return mapNonAnnotated();

		int cnt = 0;
		List<T> lst = new ArrayList<T>();
		while (resultset.next())
		{
			cnt++;
			if (rowLimit != null && cnt > rowLimit)
				throw new EasyResultSetTooManyRows("Too many rows!");
			
			T t = (T) clazz.newInstance();
			lst.add(t);
			
			for (EasyColumnFieldData f : myPropertiesList)
			{
				lastFieldName = f.field.getName();
				Method m = setters.get(lastFieldName);
				if (m == null)
					throw new SomethingJustWrong("Method for " + lastFieldName + " not present!");
				
				invoke(t, m, f.column.name(), f.isEnum, f.column.enumFactoryClass(), f.column.staticEnumMethod(), f.column.blob(), f.column.clob());
			}
		}
		return lst;
	}

	@SuppressWarnings("unchecked")
	public List<T> map(Integer offset, Integer count) throws SomethingJustWrong, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SQLException, SecurityException, NoSuchMethodException, InstantiationException, IOException, DatatypeConfigurationException, IntrospectionException
	{
		if (nonAnnotated)
			return mapNonAnnotated();
		
		int cnt = 0;
		int end = offset + count;
		List<T> lst = new ArrayList<T>();
		while (resultset.next())
		{
			cnt++;
			if (cnt < offset)
				continue;
			if (cnt > end)
				break;
			
			if (rowLimit != null && lst.size() >= rowLimit)
				throw new EasyResultSetTooManyRows("Too many rows!");
			
			T t = (T) clazz.newInstance();
			lst.add(t);
			
			for (EasyColumnFieldData f : myPropertiesList)
			{
				lastFieldName = f.field.getName();
				Method m = setters.get(lastFieldName);
				if (m == null)
					throw new SomethingJustWrong("Method for " + lastFieldName + " not present!");
				
				invoke(t, m, f.column.name(), f.isEnum, f.column.enumFactoryClass(), f.column.staticEnumMethod(), f.column.blob(), f.column.clob());
			}
		}
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> mapSingleColumn() throws SomethingJustWrong, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SQLException, SecurityException, NoSuchMethodException, InstantiationException, IOException, DatatypeConfigurationException
	{
		List<T> lst = new ArrayList<T>();
		while (resultset.next())
		{
			Object o = DBHelper.toObject(1, resultset);
			T t = (T)o;
			lst.add(t);
		}
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> mapNonAnnotated() throws SomethingJustWrong, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SQLException, SecurityException, NoSuchMethodException, InstantiationException, IOException, DatatypeConfigurationException, IntrospectionException
	{
		int cnt = 0;
		List<T> lst = new ArrayList<T>();
		while (resultset.next())
		{
			cnt++;
			if (rowLimit != null && cnt > rowLimit)
				throw new EasyResultSetTooManyRows("Too many rows!");
			
			T t = (T) clazz.newInstance();
			lst.add(t);
			
			for (EasyColumnClassField n : myPropertiesNonAnnotatedList)
			{
				if (n.nested)
				{
					lastFieldName = n.classPropertyName;
					mapNonAnnotatedNested(n,t);
				}
				else
				{
					lastFieldName = n.classPropertyName;
					Method m = setters.get(lastFieldName);
					if (m == null)
						throw new SomethingJustWrong("Method for " + lastFieldName + " not present!");
					
					invoke(t, m, n.dbFieldName, n.isEnum, n.enumFactoryClass, n.staticEnumMethod, n.blob, n.clob);
				}
			}
		}
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	private void mapNonAnnotatedNested(EasyColumnClassField n, T t) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, SomethingJustWrong, SecurityException, IOException, SQLException, NoSuchMethodException, DatatypeConfigurationException
	{
		
		Class<?> beanClass = t.getClass();
		Object beanInstance = t;
		for (int i = 0; i < n.nestedPath.length; i++)
		{
			String propertyName = n.nestedPath[i];
			Method read = new PropertyDescriptor(propertyName, beanClass).getReadMethod();
			if (read == null)
				throw new SomethingJustWrong("Get method for " + propertyName + " not present!");
			Object getter = read.invoke(beanInstance);
			if (getter == null)
			{
				PropertyDescriptor pd = new PropertyDescriptor(propertyName, beanClass);
				Method write = pd.getWriteMethod();
				if (write == null)
					throw new SomethingJustWrong("Write method for " + propertyName + " not present!");
				beanClass = pd.getPropertyType();
				Object newValue = beanClass.newInstance();
				write.invoke(beanInstance, newValue);
				beanInstance = newValue;
			}
			else
			{
				beanInstance = getter;
				beanClass = getter.getClass();
			}
		}
		
		PropertyDescriptor pd = new PropertyDescriptor(n.classPropertyName, beanClass);
		Method write = pd.getWriteMethod();
		if (write == null)
			throw new SomethingJustWrong("Write method for " + n.classPropertyName + " not present!");
		
		invoke((T)beanInstance, write, n.dbFieldName, n.isEnum, n.enumFactoryClass, n.staticEnumMethod, n.blob, n.clob);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void invoke(T t, Method m, String columnName, boolean isEnum, Class<?> enumFactoryClass, String staticEnumMethod, boolean isBlob, boolean isClob)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, SQLException, SomethingJustWrong, SecurityException, NoSuchMethodException, DatatypeConfigurationException
	{
		Class<?> cParameter = m.getParameterTypes()[0];
		if (cParameter.equals(String.class))
		{
			if (isClob)
			{
				m.invoke(t, new Object[] {Lobs.convertClobToString(resultset.getClob(columnName))});
			}
			else
			{
				m.invoke(t, new Object[] {DBHelper.toString(columnName, resultset)});
			}
		}
		else if (cParameter.equals(LocalDate.class))
		{
			m.invoke(t, new Object[] {DBHelper.toLocalDate(columnName, resultset)});
		}
		else if (cParameter.equals(LocalDateTime.class))
		{
			m.invoke(t, new Object[] {DBHelper.toLocalDateTime(columnName, resultset)});
		}
		else if (cParameter.equals(java.util.Date.class))
		{
			m.invoke(t, new Object[] {DBHelper.toDate(columnName, resultset)});
		}
		else if (cParameter.equals(java.sql.Date.class))
		{
			m.invoke(t, new Object[] {DBHelper.toDate(columnName, resultset)});
		}
		else if (cParameter.equals(java.sql.Timestamp.class))
		{
			m.invoke(t, new Object[] {DBHelper.toDate(columnName, resultset)});
		}
		else if (cParameter.equals(javax.xml.datatype.XMLGregorianCalendar.class))
		{
			m.invoke(t, new Object[] {DBHelper.toXMLGregorianCalendar(columnName, resultset)});
		}
		else if (cParameter.equals(BigDecimal.class))
		{
			m.invoke(t, new Object[] {DBHelper.toBigDecimal(columnName, resultset)});
		}
		else if (cParameter.equals(BigInteger.class))
		{
			m.invoke(t, new Object[] {DBHelper.toBigInteger(columnName, resultset)});
		}
		else if (cParameter.equals(Integer.class))
		{
			m.invoke(t, new Object[] {DBHelper.toInteger(columnName, resultset)});
		}
		else if (cParameter.equals(Long.class))
		{
			m.invoke(t, new Object[] {DBHelper.toLong(columnName, resultset)});
		}
		else if (cParameter.equals(Short.class))
		{
			m.invoke(t, new Object[] {DBHelper.toShort(columnName, resultset)});
		}
		else if (isEnum)
		{
			String v = DBHelper.toString(columnName, resultset);
			Method method = enumFactoryClass.getMethod(staticEnumMethod, String.class);
			m.invoke(t, new Object[] {method.invoke(null, v)});
		}
		else if (isBlob)
		{
			m.invoke(t, new Object[] {Lobs.convertBlobToArray(resultset.getBlob(columnName))});
		}
		else
			throw new SomethingJustWrong("What is this type " + cParameter.getName() + " ?");
	}
	
	public String getLastFieldName()
	{
		return lastFieldName;
	}
	
	
}
