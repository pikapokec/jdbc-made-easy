package ds.made.jdbc.easy.model;

import ds.made.jdbc.easy.utility.DBHelper;

public class EasyDataTableColumn
{
    public final String name;
    public final int sqlType;
    public final Class<?> clazz;

    public EasyDataTableColumn(String name, int sqlType)
    {
        this.name = name;
        this.clazz = DBHelper.toClass(sqlType);
        this.sqlType = sqlType;
    }
}
