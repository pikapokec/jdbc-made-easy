# jdbc-made-easy
Lightweight JDBC library for Java.
This library tries to simplify classic JDBC API. 
It features
* named parameters
* annotations for field mapping
* mapping to nested properties
* scalar queries
* auto closing for ResultSet / PreparedStatement / CallableStatement
* LOB handling
* java.time API
* fluent syntax.

## Purpose
Simplified JDBC usage (developed and tested on Oracle database).

## Usage
Examples given in src/test/OracleMainTestEasy.java. See wiki for more examples.

### Simple example

POJO with annotations
```java
@EasyRow
public class TestEntity
{

	@EasyColumn(name="SOME_STRING")
	private String someString;

	@EasyColumn(name="JUST_DATE")
	private LocalDate justDate;
	
	@EasyColumn(name="SOME_INTEGER")
	private Integer someInteger;
	
	@EasyColumn(name="CURRENCY")
	private BigDecimal currency;
	
	@EasyColumn(name="STATUS",enumFactoryClass=EnumTypes.class,staticEnumMethod="statusFactory")
	private EnumTypes.Status status;

	@EasyColumn(name="TS")
	private LocalDateTime pointInTime;

    ...
    getters / setters
}
```

Invoke select:

```java
    List<TestEntity> lst = new EasyPreparedStatement<>(
            "Select * From TABLE(pckTestEasyJDBC.GetTestForSelect(:string,:date,:integer))",
            connection,
            TestEntity.class,
            OracleParameterFactory.stringParameter("string").setValue("A"),
            OracleParameterFactory.dateParameter("date").setValue(new Date()),
            OracleParameterFactory.decimalParameter("integer").setValue(BigDecimal.ZERO)
            ).executeAsList();

```

### BLOB / CLOB

```java
        return new EasyPreparedStatement<>(
                "select BLOB_COL from MY_TABLE where ID=:pID",
                connection,
                byte[].class,
                OracleParameterFactory.longParameter("pID", template.getId())
        ).blobScalar();

        return new EasyPreparedStatement<>(
                "select CLOB_COL from MY_TABLE where ID=:pID",
                connection,
                String.class,
                OracleParameterFactory.longParameter("pID", template.getId())
        ).clobScalar();

```

### Export to Excel

Use Apache POI.

```java
    public EasyDataTable executeQuery(String query) throws SomethingJustWrong
    {
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection())
        {
            return new EasyPreparedStatement<>(
                    query,
                    connection,
                    EasyDataTable.class
            ).dataTable();
        }
        catch (Exception ex)
        {
            throw new SomethingJustWrong(ex);
        }
    }

    public byte[] excel(EasyDataTable dataTable) throws Exception
    {
        var stream = new ByteArrayOutputStream();

        try (var workbook = new XSSFWorkbook())
        {
            // Create a sheet
            var sheet = workbook.createSheet("My data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int colIdx = 0; colIdx < dataTable.columns.length; colIdx++)
            {
                EasyDataTableColumn column = dataTable.columns[colIdx];
                headerRow.createCell(colIdx).setCellValue(column.name);
            }

            for (int rowIdx = 0; rowIdx < dataTable.size(); rowIdx++)
            {
                // Add some data rows
                Row excelRow = sheet.createRow(rowIdx+1);

                var row = dataTable.getRow(rowIdx);
                var rawData = row.getData();

                for (int colIdx = 0; colIdx < dataTable.columns.length; colIdx++)
                {
                    var cell = excelRow.createCell(colIdx);

                    EasyDataTableColumn column = dataTable.columns[colIdx];
                    var obj = rawData[colIdx];
                    if (obj == null)
                    {
                        cell.setCellValue("");
                        continue;
                    }

                    switch (column.sqlType)
                    {
                        case Types.VARCHAR:
                            var s = (String) rawData[colIdx];
                            cell.setCellValue(s);
                            break;

                        case Types.NUMERIC:
                            var bd = (BigDecimal) rawData[colIdx];
                            cell.setCellValue(bd.doubleValue());
                            break;

                        case Types.DATE:
                            var ld = (LocalDate) rawData[colIdx];
                            cell.setCellValue(ld);
                            break;

                        case Types.TIMESTAMP:
                            var ldt = (LocalDateTime) rawData[colIdx];
                            cell.setCellValue(ldt);
                            break;

                        default:
                            var sobj = obj.toString();
                            cell.setCellValue(sobj);
                            break;
                    }
                }
            }

            // Autosize columns
            for (int colIdx = 0; colIdx < dataTable.columns.length; colIdx++)
            {
                sheet.autoSizeColumn(colIdx);
            }

            workbook.write(stream);
        }

        return stream.toByteArray();
    }
```
