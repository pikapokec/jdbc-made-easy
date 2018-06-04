package ds.made.jdbc.easy.test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import ds.made.jdbc.easy.EasyCallForStoredProcedure;
import ds.made.jdbc.easy.EasyPreparedStatement;
import ds.made.jdbc.easy.model.*;
import ds.made.jdbc.easy.utility.EasyColumnClassFieldFactory;
import ds.made.jdbc.easy.utility.Lobs;
import ds.made.jdbc.easy.utility.OracleParameterFactory;
import ds.made.jdbc.easy.utility.Parameter;

public class OracleMainTestEasy
{

	public static void main(String[] args) throws Exception
	{
		System.out.println("BEGIN");

		try
		{
			test();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("END");
	}

	private static void test() throws SQLException, SomethingJustWrong, EasyStatementNoSingleObject, IOException
	{
		DatabaseUtils u = new DatabaseUtils();
		Connection c = null;
		InputStream stream = null;
		try
		{
			String filePath = "OracleConnectionString.ini";
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			stream = classLoader.getResourceAsStream(filePath);
			Properties prop = new Properties();
			prop.load(stream);

			u.createOracleSource(prop.getProperty("username"), prop.getProperty("password"), prop.getProperty("url"));
			c = u.newConnection();
			OracleMainTestEasy m = new OracleMainTestEasy(c);

			m.functionInOutCursor();
			m.functionInOutCursorLocalDate();
			m.functionInOutCursorLocalDateTime();
			m.functionInOutCursorNoAnnotations();

			m.procedureInOutCursor();

			m.functionInOutCursorNoAnnotationsNestedOnce();
			m.functionInOutCursorNoAnnotationsNestedTwice();
			m.functionInOutCursorNoAnnotationsNestedMixed();

			m.function();
			m.procedure();

			m.testSelect();
			m.testLOBs();

			m.testScalar();

			m.testDataTable();
		}
		finally
		{
			u.closeConnection(c);
			if (stream != null)
				stream.close();
		}
	}
	
	private Connection connection;
	
	public OracleMainTestEasy(Connection connection)
	{
		this.connection = connection;
	}
	
	private void functionInOutCursor() throws SomethingJustWrong
	{
		System.out.println("\n\nfunctionInOutCursor");
		
		/*
		  Function F_RefCursor(
				  inNumber      in Number,
				  inText        in VarChar2,
				  inDate        in Date,
				  inoutNumber   in out Number,
				  inoutText     in out VarChar2,
				  inoutDate     in out Date,
				  outNumber     out Number,
				  outText       out VarChar2,
				  outDate       out Date) RETURN SYS_REFCURSOR;
		 */
		
		Parameter cursor = OracleParameterFactory.cursorReturnParameter("cursor");
		Parameter inoutNumber = OracleParameterFactory.numberInOutParameter("inoutNumber", BigDecimal.ONE);
		Parameter inoutText = OracleParameterFactory.stringInOutParameter("inoutText", "blah blah blah!");
		Parameter inoutDate = OracleParameterFactory.dateInOutParameter("inoutDate", new Date());
		Parameter outNumber = OracleParameterFactory.numberOutParameter("outNumber");
		Parameter outText = OracleParameterFactory.stringOutParameter("outText");
		Parameter outDate = OracleParameterFactory.dateOutParameter("outDate");

		List<TestEntity> lst = new EasyCallForStoredProcedure<>(
				"pckTestEasyJDBC.F_RefCursor",
				connection,
				TestEntity.class,
				OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE),
				OracleParameterFactory.stringParameter("inText","It is a test!"),
				OracleParameterFactory.dateParameter("inDate",new Date()),
				inoutNumber,inoutText,inoutDate,
				outNumber, outText, outDate,
				cursor)
				.executeAsList(cursor);
		
		System.out.println("\tinoutNumber " + inoutNumber.getValue());
		System.out.println("\tinoutText " + inoutText.getValue());
		System.out.println("\tinoutDate " + inoutDate.getValue());
		System.out.println("\toutNumber " + outNumber.getValue());
		System.out.println("\toutText " + outText.getValue());
		System.out.println("\toutDate " + outDate.getValue());
		
		for (TestEntity a : lst)
			System.out.println("\t"+a);
	}

	private void functionInOutCursorLocalDate() throws SomethingJustWrong
	{
		System.out.println("\n\nfunctionInOutCursorLocalDate");

		Parameter cursor = OracleParameterFactory.cursorReturnParameter("cursor");
		Parameter inoutNumber = OracleParameterFactory.numberInOutParameter("inoutNumber", BigDecimal.ONE);
		Parameter inoutText = OracleParameterFactory.stringInOutParameter("inoutText", "blah blah blah!");
		Parameter inoutDate = OracleParameterFactory.localDateInOutParameter("inoutDate", LocalDate.now());
		Parameter outNumber = OracleParameterFactory.numberOutParameter("outNumber");
		Parameter outText = OracleParameterFactory.stringOutParameter("outText");
		Parameter outDate = OracleParameterFactory.localDateOutParameter("outDate");

		List<TestEntity> lst = new EasyCallForStoredProcedure<>(
				"pckTestEasyJDBC.F_RefCursor",
				connection,
				TestEntity.class,
				OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE),
				OracleParameterFactory.stringParameter("inText","It is a test!"),
				OracleParameterFactory.localDateParameter("inDate", LocalDate.now()),
				inoutNumber,inoutText,inoutDate,
				outNumber, outText, outDate,
				cursor)
				.executeAsList(cursor);

		System.out.println("\tinoutNumber " + inoutNumber.getValue());
		System.out.println("\tinoutText " + inoutText.getValue());
		System.out.println("\tinoutDate " + inoutDate.getValue() + " [" + inoutDate.getValue().getClass() + "]");
		System.out.println("\toutNumber " + outNumber.getValue());
		System.out.println("\toutText " + outText.getValue());
		System.out.println("\toutDate " + outDate.getValue() + " [" + outDate.getValue().getClass() + "]");

		for (TestEntity a : lst)
			System.out.println("\t"+a);
	}

	private void functionInOutCursorLocalDateTime() throws SomethingJustWrong
	{
		System.out.println("\n\nfunctionInOutCursorLocalDateTime");

		Parameter cursor = OracleParameterFactory.cursorReturnParameter("cursor");
		Parameter inoutNumber = OracleParameterFactory.numberInOutParameter("inoutNumber", BigDecimal.ONE);
		Parameter inoutText = OracleParameterFactory.stringInOutParameter("inoutText", "blah blah blah!");
		Parameter inoutDate = OracleParameterFactory.localDateTimeInOutParameter("inoutDate", LocalDateTime.now());
		Parameter outNumber = OracleParameterFactory.numberOutParameter("outNumber");
		Parameter outText = OracleParameterFactory.stringOutParameter("outText");
		Parameter outDate = OracleParameterFactory.localDateOutParameter("outDate");

		List<TestEntity> lst = new EasyCallForStoredProcedure<>(
				"pckTestEasyJDBC.F_RefCursor",
				connection,
				TestEntity.class,
				OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE),
				OracleParameterFactory.stringParameter("inText","It is a test!"),
				OracleParameterFactory.localDateTimeParameter("inDate", LocalDateTime.now()),
				inoutNumber,inoutText,inoutDate,
				outNumber, outText, outDate,
				cursor)
				.executeAsList(cursor);

		System.out.println("\tinoutNumber " + inoutNumber.getValue());
		System.out.println("\tinoutText " + inoutText.getValue());
		System.out.println("\tinoutDate " + inoutDate.getValue() + " [" + inoutDate.getValue().getClass() + "]");
		System.out.println("\toutNumber " + outNumber.getValue());
		System.out.println("\toutText " + outText.getValue());
		System.out.println("\toutDate " + outDate.getValue() + " [" + outDate.getValue().getClass() + "]");

		for (TestEntity a : lst)
			System.out.println("\t"+a);
	}

	private void procedureInOutCursor() throws SomethingJustWrong
	{
		System.out.println("\n\nprocedureInOutCursor");

		/*
		  Procedure P_RefCursor(
				  inNumber      in Number,
				  inText        in VarChar2,
				  inDate        in Date,
				  inoutNumber   in out Number,
				  inoutText     in out VarChar2,
				  inoutDate     in out Date,
				  outNumber     out Number,
				  outText       out VarChar2,
				  outDate       out Date,
				  outCursor     out SYS_REFCURSOR);
		 */

		Parameter inoutNumber = OracleParameterFactory.numberInOutParameter("inoutNumber", BigDecimal.ONE);
		Parameter inoutText = OracleParameterFactory.stringInOutParameter("inoutText", "blah blah blah!");
		Parameter inoutDate = OracleParameterFactory.dateInOutParameter("inoutDate", new Date());
		Parameter outNumber = OracleParameterFactory.numberOutParameter("outNumber");
		Parameter outText = OracleParameterFactory.stringOutParameter("outText");
		Parameter outDate = OracleParameterFactory.dateOutParameter("outDate");
		Parameter cursor = OracleParameterFactory.cursorOutParameter("outCursor");

		List<TestEntity> lst = new EasyCallForStoredProcedure<>(
				"pckTestEasyJDBC.P_RefCursor",
				connection,
				TestEntity.class,
				OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE),
				OracleParameterFactory.stringParameter("inText","It is a test!"),
				OracleParameterFactory.dateParameter("inDate",new Date()),
				inoutNumber,inoutText,inoutDate,
				outNumber, outText, outDate,
				cursor)
				.executeAsList(cursor);

		System.out.println("\tinoutNumber " + inoutNumber.getValue());
		System.out.println("\tinoutText " + inoutText.getValue());
		System.out.println("\tinoutDate " + inoutDate.getValue());
		System.out.println("\toutNumber " + outNumber.getValue());
		System.out.println("\toutText " + outText.getValue());
		System.out.println("\toutDate " + outDate.getValue());

		for (TestEntity a : lst)
			System.out.println("\t"+a);
		
	}
	
	private void functionInOutCursorNoAnnotations() throws SomethingJustWrong
	{
		System.out.println("\n\nfunctionInOutCursorNoAnnotations");

		Parameter cursor = OracleParameterFactory.cursorReturnParameter("cursor");
		Parameter inoutNumber = OracleParameterFactory.numberInOutParameter("inoutNumber", BigDecimal.ONE);
		Parameter inoutText = OracleParameterFactory.stringInOutParameter("inoutText", "blah blah blah!");
		Parameter inoutDate = OracleParameterFactory.dateInOutParameter("inoutDate", new Date());
		Parameter outNumber = OracleParameterFactory.numberOutParameter("outNumber");
		Parameter outText = OracleParameterFactory.stringOutParameter("outText");
		Parameter outDate = OracleParameterFactory.dateOutParameter("outDate");

		List<TestEntity> lst = new EasyCallForStoredProcedure<>(
				"pckTestEasyJDBC.F_RefCursor",
				connection,
				TestEntity.class,
				OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE),
				OracleParameterFactory.stringParameter("inText","It is a test!"),
				OracleParameterFactory.dateParameter("inDate",new Date()),
				inoutNumber,inoutText,inoutDate,
				outNumber, outText, outDate,
				cursor)
				.setOtherClassFields(
						EasyColumnClassFieldFactory.standard("SOME_STRING","someString"),
						EasyColumnClassFieldFactory.standard("JUST_DATE","justDate"),
						EasyColumnClassFieldFactory.standard("SOME_INTEGER","someInteger"),
						EasyColumnClassFieldFactory.standard("CURRENCY","currency"),
						EasyColumnClassFieldFactory.enumeration("STATUS","status",EnumTypes.class,"statusFactory"),
						EasyColumnClassFieldFactory.standard("TS","pointInTime")
				)
				.executeAsList(cursor);

		System.out.println("\tinoutNumber " + inoutNumber.getValue());
		System.out.println("\tinoutText " + inoutText.getValue());
		System.out.println("\tinoutDate " + inoutDate.getValue());
		System.out.println("\toutNumber " + outNumber.getValue());
		System.out.println("\toutText " + outText.getValue());
		System.out.println("\toutDate " + outDate.getValue());

		for (TestEntity a : lst)
			System.out.println("\t"+a);
	}

	private void functionInOutCursorNoAnnotationsNestedOnce() throws SomethingJustWrong
	{
		System.out.println("\n\nfunctionInOutCursorNoAnnotations - nested once");

		Parameter cursor = OracleParameterFactory.cursorReturnParameter("cursor");
		Parameter inoutNumber = OracleParameterFactory.numberInOutParameter("inoutNumber", BigDecimal.ONE);
		Parameter inoutText = OracleParameterFactory.stringInOutParameter("inoutText", "blah blah blah!");
		Parameter inoutDate = OracleParameterFactory.dateInOutParameter("inoutDate", new Date());
		Parameter outNumber = OracleParameterFactory.numberOutParameter("outNumber");
		Parameter outText = OracleParameterFactory.stringOutParameter("outText");
		Parameter outDate = OracleParameterFactory.dateOutParameter("outDate");

		List<TestSelectNestedOnce> lst = new EasyCallForStoredProcedure<>(
				"pckTestEasyJDBC.F_RefCursor",
				connection,
				TestSelectNestedOnce.class,
				OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE),
				OracleParameterFactory.stringParameter("inText","It is a test!"),
				OracleParameterFactory.dateParameter("inDate",new Date()),
				inoutNumber,inoutText,inoutDate,
				outNumber, outText, outDate,
				cursor)
				.setOtherClassFields(
						EasyColumnClassFieldFactory.nested("SOME_STRING","data.someString"),
						EasyColumnClassFieldFactory.nested("JUST_DATE","data.justDate"),
						EasyColumnClassFieldFactory.nested("SOME_INTEGER","data.someInteger"),
						EasyColumnClassFieldFactory.nested("CURRENCY","data.currency"),
						EasyColumnClassFieldFactory.nestedEnumeration("STATUS","data.status",EnumTypes.class,"statusFactory"),
						EasyColumnClassFieldFactory.nested("TS","data.pointInTime")
				)
				.executeAsList(cursor);

		System.out.println("\tinoutNumber " + inoutNumber.getValue());
		System.out.println("\tinoutText " + inoutText.getValue());
		System.out.println("\tinoutDate " + inoutDate.getValue());
		System.out.println("\toutNumber " + outNumber.getValue());
		System.out.println("\toutText " + outText.getValue());
		System.out.println("\toutDate " + outDate.getValue());

		for (TestSelectNestedOnce a : lst)
			System.out.println("\t"+a);
	}

	private void functionInOutCursorNoAnnotationsNestedTwice() throws SomethingJustWrong
	{
		System.out.println("\n\nfunctionInOutCursorNoAnnotations - nested twice");

		Parameter cursor = OracleParameterFactory.cursorReturnParameter("cursor");
		Parameter inoutNumber = OracleParameterFactory.numberInOutParameter("inoutNumber", BigDecimal.ONE);
		Parameter inoutText = OracleParameterFactory.stringInOutParameter("inoutText", "blah blah blah!");
		Parameter inoutDate = OracleParameterFactory.dateInOutParameter("inoutDate", new Date());
		Parameter outNumber = OracleParameterFactory.numberOutParameter("outNumber");
		Parameter outText = OracleParameterFactory.stringOutParameter("outText");
		Parameter outDate = OracleParameterFactory.dateOutParameter("outDate");

		List<TestSelectNestedTwice> lst = new EasyCallForStoredProcedure<>(
				"pckTestEasyJDBC.F_RefCursor",
				connection,
				TestSelectNestedTwice.class,
				OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE),
				OracleParameterFactory.stringParameter("inText","It is a test!"),
				OracleParameterFactory.dateParameter("inDate",new Date()),
				inoutNumber,inoutText,inoutDate,
				outNumber, outText, outDate,
				cursor)
				.setOtherClassFields(
						EasyColumnClassFieldFactory.nested("SOME_STRING","data.data.someString"),
						EasyColumnClassFieldFactory.nested("JUST_DATE","data.data.justDate"),
						EasyColumnClassFieldFactory.nested("SOME_INTEGER","data.data.someInteger"),
						EasyColumnClassFieldFactory.nested("CURRENCY","data.data.currency"),
						EasyColumnClassFieldFactory.nestedEnumeration("STATUS","data.data.status",EnumTypes.class,"statusFactory"),
						EasyColumnClassFieldFactory.nested("TS","data.data.pointInTime")
				)
				.executeAsList(cursor);

		System.out.println("\tinoutNumber " + inoutNumber.getValue());
		System.out.println("\tinoutText " + inoutText.getValue());
		System.out.println("\tinoutDate " + inoutDate.getValue());
		System.out.println("\toutNumber " + outNumber.getValue());
		System.out.println("\toutText " + outText.getValue());
		System.out.println("\toutDate " + outDate.getValue());

		for (TestSelectNestedTwice a : lst)
			System.out.println("\t"+a);
	}

	private void functionInOutCursorNoAnnotationsNestedMixed() throws SomethingJustWrong
	{
		System.out.println("\n\nfunctionInOutCursorNoAnnotations - nested mixed");

		Parameter cursor = OracleParameterFactory.cursorReturnParameter("cursor");
		Parameter inoutNumber = OracleParameterFactory.numberInOutParameter("inoutNumber", BigDecimal.ONE);
		Parameter inoutText = OracleParameterFactory.stringInOutParameter("inoutText", "blah blah blah!");
		Parameter inoutDate = OracleParameterFactory.dateInOutParameter("inoutDate", new Date());
		Parameter outNumber = OracleParameterFactory.numberOutParameter("outNumber");
		Parameter outText = OracleParameterFactory.stringOutParameter("outText");
		Parameter outDate = OracleParameterFactory.dateOutParameter("outDate");

		List<TestSelectNested> lst = new EasyCallForStoredProcedure<>(
				"pckTestEasyJDBC.F_RefCursor",
				connection,
				TestSelectNested.class,
				OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE),
				OracleParameterFactory.stringParameter("inText","It is a test!"),
				OracleParameterFactory.dateParameter("inDate",new Date()),
				inoutNumber,inoutText,inoutDate,
				outNumber, outText, outDate,
				cursor)
				.setOtherClassFields(
						EasyColumnClassFieldFactory.nested("SOME_STRING","someString"),
						EasyColumnClassFieldFactory.nested("JUST_DATE","twice.data.data.justDate"),
						EasyColumnClassFieldFactory.nested("SOME_INTEGER","twice.data.data.someInteger"),
						EasyColumnClassFieldFactory.nested("CURRENCY","once.data.currency"),
						EasyColumnClassFieldFactory.nestedEnumeration("STATUS","once.data.status",EnumTypes.class,"statusFactory"),
						EasyColumnClassFieldFactory.nested("TS","once.data.pointInTime")
				)
				.executeAsList(cursor);

		System.out.println("\tinoutNumber " + inoutNumber.getValue());
		System.out.println("\tinoutText " + inoutText.getValue());
		System.out.println("\tinoutDate " + inoutDate.getValue());
		System.out.println("\toutNumber " + outNumber.getValue());
		System.out.println("\toutText " + outText.getValue());
		System.out.println("\toutDate " + outDate.getValue());

		for (TestSelectNested a : lst)
			System.out.println("\t"+a);
	}

	private void function() throws SQLException
	{
		System.out.println("\n\nfunction");

		/*
		  Function F_Function(
				  inNumber      in Number,
				  inText        in VarChar2,
				  inDate        in Date,
				  inoutNumber   in out Number,
				  inoutText     in out VarChar2,
				  inoutDate     in out Date,
				  outNumber     out Number,
				  outText       out VarChar2,
				  outDate       out Date) RETURN VarChar2;
		 */
		// Inputs
		Parameter inNumber = OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE);
		Parameter inText = OracleParameterFactory.stringParameter("inText","It is a test!");
		Parameter inDate = OracleParameterFactory.dateParameter("inDate",new Date());

		// Inputs Outputs
		Parameter inoutNumber = OracleParameterFactory.numberInOutParameter("inoutNumber", BigDecimal.ONE);
		Parameter inoutText = OracleParameterFactory.stringInOutParameter("inoutText", "bla bla bla!");
		Parameter inoutDate = OracleParameterFactory.dateInOutParameter("inoutDate", new Date());
		
		// Outputs
		Parameter outNumber = OracleParameterFactory.numberOutParameter("outNumber");
		Parameter outText = OracleParameterFactory.stringOutParameter("outText");
		Parameter outDate = OracleParameterFactory.dateOutParameter("outDate");
		Parameter result = OracleParameterFactory.stringReturnParameter("result");
		
		new EasyCallForStoredProcedure<Object>(
				"pckTestEasyJDBC.F_Function",
				connection,
				Object.class,
				inNumber, inText, inDate,
				inoutNumber,inoutText,inoutDate,
				outNumber, outText, outDate,
				result)
			.execute();

		System.out.println("\tinoutNumber " + inoutNumber.getValue());
		System.out.println("\tinoutText " + inoutText.getValue());
		System.out.println("\tinoutDate " + inoutDate.getValue());
		System.out.println("\toutNumber " + outNumber.getValue());
		System.out.println("\toutText " + outText.getValue());
		System.out.println("\toutDate " + outDate.getValue());
		System.out.println("\tresult " + result.getValue());
	}

	private void procedure() throws SQLException
	{
		System.out.println("\n\nprocedure");

		/*
		  Procedure P_Procedure(
				  inNumber      in Number,
				  inText        in VarChar2,
				  inDate        in Date,
				  inoutNumber   in out Number,
				  inoutText     in out VarChar2,
				  inoutDate     in out Date,
				  outNumber     out Number,
				  outText       out VarChar2,
				  outDate       out Date,
				  result        out VarChar2);
		 */
		// Vhodi
		Parameter inNumber = OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE);
		Parameter inText = OracleParameterFactory.stringParameter("inText","It is a test!");
		Parameter inDate = OracleParameterFactory.dateParameter("inDate",new Date());

		// Vhodi izhodi
		Parameter inoutNumber = OracleParameterFactory.numberInOutParameter("inoutNumber", BigDecimal.ONE);
		Parameter inoutText = OracleParameterFactory.stringInOutParameter("inoutText", "bla bla bla!");
		Parameter inoutDate = OracleParameterFactory.dateInOutParameter("inoutDate", new Date());

		// Izhodi
		Parameter outNumber = OracleParameterFactory.numberOutParameter("outNumber");
		Parameter outText = OracleParameterFactory.stringOutParameter("outText");
		Parameter outDate = OracleParameterFactory.dateOutParameter("outDate");
		Parameter result = OracleParameterFactory.stringOutParameter("result");

		new EasyCallForStoredProcedure<Object>(
				"pckTestEasyJDBC.P_Procedure",
				connection,
				Object.class,
				inNumber, inText, inDate,
				inoutNumber,inoutText,inoutDate,
				outNumber, outText, outDate,
				result)
				.execute();

		System.out.println("\tinoutNumber " + inoutNumber.getValue());
		System.out.println("\tinoutText " + inoutText.getValue());
		System.out.println("\tinoutDate " + inoutDate.getValue());
		System.out.println("\toutNumber " + outNumber.getValue());
		System.out.println("\toutText " + outText.getValue());
		System.out.println("\toutDate " + outDate.getValue());
		System.out.println("\tresult " + result.getValue());
	}
	
	private void testSelect() throws SomethingJustWrong
	{
		System.out.println("\n\nTest Select - filter");
		List<TestEntity> lst = new EasyPreparedStatement<>(
				"Select * From TABLE(pckTestEasyJDBC.GetTestForSelect(:string,:date,:integer))",
				connection,
				TestEntity.class,
				OracleParameterFactory.stringParameter("string").setValue("A"),
				OracleParameterFactory.dateParameter("date").setValue(new Date()),
				OracleParameterFactory.decimalParameter("integer").setValue(BigDecimal.ZERO)
				).executeAsList();
		for (TestEntity a : lst)
			System.out.println(a);

		//

		System.out.println("\nAll");
		lst = new EasyPreparedStatement<TestEntity>(
				"Select * From TEST_4_SELECT",
				connection,
				TestEntity.class
				).executeAsList();
		for (TestEntity a : lst)
			System.out.println(a);

		//

		System.out.println("\nAll to non annotated class");
		List<TestEntityNoAnnotation> lstNo = new EasyPreparedStatement<>(
				"Select * From TEST_4_SELECT",
				connection,
				TestEntityNoAnnotation.class
				)
				.setOtherClassFields(
						EasyColumnClassFieldFactory.standard("SOME_STRING","someString"),
						EasyColumnClassFieldFactory.standard("JUST_DATE","justDate"),
						EasyColumnClassFieldFactory.standard("SOME_INTEGER","someInteger"),
						EasyColumnClassFieldFactory.standard("CURRENCY","currency"),
						EasyColumnClassFieldFactory.enumeration("STATUS","status",EnumTypes.class,"statusFactory"),
						EasyColumnClassFieldFactory.standard("TS","pointInTime")
				)
				.executeAsList();
		for (TestEntity a : lst)
			System.out.println(a);

	}

	private void testLOBs() throws EasyStatementNoSingleObject, SomethingJustWrong, SQLException, IOException
	{
		System.out.println("\n\nLOBs throught select");

		byte[] blob = new EasyPreparedStatement<>(
				"Select RAW_DATA From TEST_CLOB_BLOB Where ID=:id",
				connection,
				byte[].class,
				OracleParameterFactory.integerParameter("id",1))
				.blobScalar();
		System.out.println("BLOB Length: " + blob.length);

		String clob = new EasyPreparedStatement<>(
				"Select TEXT_DATA From TEST_CLOB_BLOB Where ID=:id",
				connection,
				String.class,
				OracleParameterFactory.integerParameter("id",1))
				.clobScalar();
		System.out.println("CLOB Length: " + clob.length());

		System.out.println("\n\nLOBs throught procedure");
		/*
		  Procedure P_Lob_Procedure(
				  inId          in Integer,
				  outBLOB       out BLOB,
				  outCLOB       out CLOB);
		 */
		Parameter outBLOB = new Parameter("outBLOB", OracleParameterFactory.BLOB, Enums.PARAMETER_DIRECTION.OUT);
		Parameter outCLOB = new Parameter("outCLOB", OracleParameterFactory.CLOB, Enums.PARAMETER_DIRECTION.OUT);
		new EasyCallForStoredProcedure<Object>(
				"pckTestEasyJDBC.P_Lob_Procedure",
				connection,
				Object.class,
				OracleParameterFactory.numberParameter("inNumber",BigDecimal.ONE),
				outBLOB,outCLOB)
				.execute();
		blob = Lobs.convertBlobParameterToArrayAndFree(outBLOB);
		clob = Lobs.convertClobParameterToStringAndFree(outCLOB);
		System.out.println("BLOB Length: " + blob.length);
		System.out.println("CLOB Length: " + clob.length());
	}

	private void testScalar() throws EasyStatementNoSingleObject, SomethingJustWrong
	{
		System.out.println("\n\ntestScalar");

		java.util.Date d = new EasyPreparedStatement<java.util.Date>(
				"Select SysDate From DUAL",
				connection,
				java.util.Date.class).
				executeAsScalar();
		System.out.println("Date: " + d);

		LocalDate ld = new EasyPreparedStatement<LocalDate>(
				"Select SysDate From DUAL",
				connection,
				LocalDate.class).
				executeAsScalar();
		System.out.println("LocalDate: " + ld);

		LocalDateTime ldt = new EasyPreparedStatement<LocalDateTime>(
				"Select SysDate From DUAL",
				connection,
				LocalDateTime.class).
				executeAsScalar();
		System.out.println("LocalDateTime: " + ldt);

		BigDecimal bd = new EasyPreparedStatement<BigDecimal>(
				"Select -1/3 From DUAL",
				connection,
				BigDecimal.class).
				executeAsScalar();
		System.out.println("BigDecimal: " + bd);

		Integer i = new EasyPreparedStatement<Integer>(
				"Select 1234 From DUAL",
				connection,
				Integer.class).
				executeAsScalar();
		System.out.println("Integer: " + i);

		String s = new EasyPreparedStatement<String>(
				"Select 'This is it!' From DUAL",
				connection,
				String.class).
				executeAsScalar();
		System.out.println("String: " + s);
	}

	private void testDataTable() throws SomethingJustWrong
	{
		System.out.println("\n\ntestDataTable");
		EasyDataTable table = new EasyPreparedStatement<EasyDataTable>(
				"SELECT SOME_STRING, JUST_DATE, SOME_INTEGER, CURRENCY, STATUS, TS\n" +
						"From TEST_4_SELECT \n" +
						"Where (SOME_STRING=:inString or JUST_DATE=:inDate or SOME_INTEGER=:inInteger) \n" +
						"Order By 1",
				connection,
				EasyDataTable.class,
				OracleParameterFactory.stringParameter("inString").setValue("A"),
				OracleParameterFactory.dateParameter("inDate").setValue(LocalDate.now()),
				OracleParameterFactory.decimalParameter("inInteger").setValue(BigDecimal.ZERO)
		).dataTable();

        System.out.println("Data:");
		for (int row = 0; row < table.size(); row++)
		{
			Object[] record = table.getRawRow(row);
			for (int idx = 0; idx < record.length; idx++)
				System.out.print("\t" + table.columns[idx].name + "=" + record[idx] + " [" + table.columns[idx].clazz.getSimpleName() + "];");
			System.out.println();
		}

		System.out.println("Easy Data Row:");
		for (int row = 0; row < table.size(); row++)
		{
			EasyDataRow record = table.getRow(row);
			for (int idx = 0; idx < record.getData().length; idx++)
				System.out.print("\t" + table.columns[idx].name + "=" + record.getData()[idx] + " [" + table.columns[idx].clazz.getSimpleName() + "];");
			System.out.println();
		}

        System.out.println("Stream:");
		table.getStream().forEach(e ->
                {
					System.out.print("\tstream  ");
                	table.getColumnStream().forEach(c -> System.out.print("\t" + c.name + "=" + e[c.position] + " [" + c.clazz.getSimpleName() + "];"));
					System.out.println();

					System.out.print("\tclassic ");
                    for (int idx = 0; idx < e.length; idx++)
                        System.out.print("\t" + table.columns[idx].name + "=" + e[idx] + " [" + table.columns[idx].clazz.getSimpleName() + "];");
                    System.out.println();
                }
        );

		Object o = table.getField(0,"CURRENCY");
		System.out.println("Record 0, field CURRENCY: " + o);

        BigDecimal bd = table.getBigDecimal(0,"CURRENCY");
		System.out.println("Record 0, field CURRENCY: " + bd);
	}

}
