package ds.made.jdbc.easy.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * To be enhanced ...
 */
public class EasyDataTable
{
    public final EasyDataTableColumn[] columns;
    private final List<Object[]> rows;
    private final ResultSet resultset;
    private Integer rowLimit = null;

    public EasyDataTable(ResultSet resultset) throws AnalysisBroke
    {
        this.resultset = resultset;
        rows = new ArrayList<>();
        columns = analyze();
    }

    public void setRowLimit(Integer rowLimit)
    {
        this.rowLimit = rowLimit;
    }

    public int size()
    {
        return rows.size();
    }

    public Stream<Object[]> getStream()
    {
        return rows.stream();
    }

    public Stream<EasyDataTableColumn> getColumnStream()
    {
        return Arrays.stream(columns);
    }

    public Object[] getRawRow(int row) throws SomethingJustWrong
    {
        if (row >= rows.size())
            throw new SomethingJustWrong("No such row!");
        return rows.get(row);
    }

    public EasyDataRow getRow(int row) throws SomethingJustWrong
    {
        if (row >= rows.size())
            throw new SomethingJustWrong("No such row!");
        return new EasyDataRow(rows.get(row));
    }

    public Object getField(int row, String columnName) throws SomethingJustWrong
    {
        if (row >= rows.size())
            throw new SomethingJustWrong("No such row!");

        for (int idx=0; idx < columns.length; idx++)
        {
            if (columns[idx].name.equalsIgnoreCase(columnName))
                return rows.get(row)[idx];
        }

        throw new SomethingJustWrong("No such column!");
    }

    public BigDecimal getBigDecimal(int row, String columnName) throws SomethingJustWrong
    {
        Object o = getField(row, columnName);
        if (o instanceof BigDecimal)
            return (BigDecimal)o;
        throw new SomethingJustWrong("Field of wrong type!");
    }

    public Integer getInteger(int row, String columnName) throws SomethingJustWrong
    {
        Object o = getField(row, columnName);
        if (o instanceof Integer)
            return (Integer)o;
        throw new SomethingJustWrong("Field of wrong type!");
    }

    public Long getLong(int row, String columnName) throws SomethingJustWrong
    {
        Object o = getField(row, columnName);
        if (o instanceof Long)
            return (Long)o;
        throw new SomethingJustWrong("Field of wrong type!");
    }

    public LocalDate getLocalDate(int row, String columnName) throws SomethingJustWrong
    {
        Object o = getField(row, columnName);
        if (o instanceof LocalDate)
            return (LocalDate)o;
        throw new SomethingJustWrong("Field of wrong type!");
    }

    public LocalDateTime getLocalDateTime(int row, String columnName) throws SomethingJustWrong
    {
        Object o = getField(row, columnName);
        if (o instanceof LocalDateTime)
            return (LocalDateTime)o;
        throw new SomethingJustWrong("Field of wrong type!");
    }

    public String getString(int row, String columnName) throws SomethingJustWrong
    {
        Object o = getField(row, columnName);
        if (o instanceof String)
            return (String)o;
        throw new SomethingJustWrong("Field of wrong type!");
    }

    public void read() throws SQLException, EasyResultSetTooManyRows
    {
        int cnt = 0;
        while (resultset.next())
        {
            cnt++;
            if (rowLimit != null && cnt > rowLimit)
                throw new EasyResultSetTooManyRows("Too many rows!");

            Object[] items = new Object[columns.length];
            rows.add(items);

            for (int idx=0; idx < columns.length; idx++)
            {
                Object o = resultset.getObject(idx+1);
                if (resultset.wasNull())
                    o = null;
                items[idx] = mapSQLTypes(o);
            }
        }
    }

    public void read(Integer offset, Integer count) throws SQLException, EasyResultSetTooManyRows
    {
        int cnt = 0;
        int end = offset + count;
        while (resultset.next())
        {
            cnt++;
            if (cnt < offset)
                continue;
            if (cnt > end)
                break;

            if (rowLimit != null && rows.size() >= rowLimit)
                throw new EasyResultSetTooManyRows("Too many rows!");

            Object[] items = new Object[columns.length];
            rows.add(items);

            for (int idx=0; idx < columns.length; idx++)
            {
                Object o = resultset.getObject(idx+1);
                if (resultset.wasNull())
                    o = null;
                items[idx] = mapSQLTypes(o);
            }
        }
    }

    private EasyDataTableColumn[] analyze() throws AnalysisBroke
    {
        try
        {
            ResultSetMetaData rsmd = resultset.getMetaData();
            EasyDataTableColumn[] cols = new EasyDataTableColumn[rsmd.getColumnCount()];
            for (int idx = 1; idx <= rsmd.getColumnCount(); idx++)
            {
                String name = rsmd.getColumnName(idx);
                int zeroBasedIndex = idx - 1;
                cols[zeroBasedIndex] = new EasyDataTableColumn(zeroBasedIndex, name,rsmd.getColumnType(idx));
            }
            return  cols;
        }
        catch (Exception e)
        {
            throw new AnalysisBroke(e);
        }
    }

    private Object mapSQLTypes(Object o)
    {
        if (o == null)
            return null;

        if (o instanceof java.sql.Timestamp)
            return ((java.sql.Timestamp)o).toLocalDateTime();

        if (o instanceof java.sql.Date)
            return ((java.sql.Date)o).toLocalDate();

        if (o instanceof java.sql.Time)
            return ((java.sql.Time)o).toLocalTime();

        return o;
    }
}
