package ds.made.jdbc.easy.utility;

import ds.made.jdbc.easy.model.EasyColumnClassField;

/**
Utility for creating {@link EasyColumnClassField} fields.
@author ds
*/
public class EasyColumnClassFieldFactory
{

	/**
	 * Standard column class field mapper.
	 * @param dbFieldName Database field name
	 * @param classPropertyName Java class property exposed as setter
	 * @return standard EasyColumnClassField
	 */
	public static EasyColumnClassField standard(String dbFieldName, String classPropertyName)
	{
		return new EasyColumnClassField(dbFieldName, classPropertyName);
	}

	/**
	 * Nested column class field mapper.
	 * @param dbFieldName Database field name
	 * @param nestedPropertyName Java nested class property exposed as setter
	 * @return standard EasyColumnClassField
	 */
	public static EasyColumnClassField nested(String dbFieldName, String nestedPropertyName)
	{
		return new EasyColumnClassField(dbFieldName, nestedPropertyName, true);
	}
	
	/**
	 * BLOB column class field mapper.
	 * @param dbFieldName Database field name
	 * @param classPropertyName Java class property exposed as setter
	 * @return BLOB to byte[] EasyColumnClassField
	 */
	public static EasyColumnClassField blob(String dbFieldName, String classPropertyName)
	{
		return new EasyColumnClassField(dbFieldName, classPropertyName, true, false);
	}

	/**
	 * Nested BLOB column class field mapper.
	 * @param dbFieldName Database field name
	 * @param nestedPropertyName Java nested class property exposed as setter
	 * @return BLOB to byte[] EasyColumnClassField
	 */
	public static EasyColumnClassField nestedBlob(String dbFieldName, String nestedPropertyName)
	{
		return new EasyColumnClassField(dbFieldName, nestedPropertyName, true, null, null, true, false, false);
	}
	
	/**
	 * CLOB column class field mapper.
	 * @param dbFieldName Database field name
	 * @param classPropertyName Java class property exposed as setter
	 * @return CLOB to String EasyColumnClassField
	 */
	public static EasyColumnClassField clob(String dbFieldName, String classPropertyName)
	{
		return new EasyColumnClassField(dbFieldName, classPropertyName, false, true);
	}
	
	/**
	 * Nested CLOB column class field mapper.
	 * @param dbFieldName Database field name
	 * @param nestedPropertyName Java nested class property exposed as setter
	 * @return CLOB to String EasyColumnClassField
	 */
	public static EasyColumnClassField nestedClob(String dbFieldName, String nestedPropertyName)
	{
		return new EasyColumnClassField(dbFieldName, nestedPropertyName, true, null, null, false, true, false);
	}
	
	/**
	 * Enum column class field mapper.
	 * @param dbFieldName Database field name
	 * @param classPropertyName Java class enum property exposed as setter
	 * @param enumFactoryClass Enum class
	 * @param staticEnumMethod Static factory method that gives us an enumFactoryClass instance
	 * @return whatever to Enum EasyColumnClassField
	 */
	public static EasyColumnClassField enumeration(String dbFieldName, String classPropertyName, Class<?> enumFactoryClass, String staticEnumMethod)
	{
		return new EasyColumnClassField(dbFieldName, classPropertyName, enumFactoryClass, staticEnumMethod);
	}

	/**
	 * Nested Enum column class field mapper.
	 * @param dbFieldName Database field name
	 * @param nestedPropertyName Java nested class property exposed as setter
	 * @param enumFactoryClass Enum class
	 * @param staticEnumMethod Static factory method that gives us an enumFactoryClass instance
	 * @return whatever to Enum EasyColumnClassField
	 */
	public static EasyColumnClassField nestedEnumeration(String dbFieldName, String nestedPropertyName, Class<?> enumFactoryClass, String staticEnumMethod)
	{
		return new EasyColumnClassField(dbFieldName, nestedPropertyName, true, enumFactoryClass, staticEnumMethod, false, false, true);
	}
	
}
