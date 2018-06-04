package ds.made.jdbc.easy.model;

import java.util.List;

public class EasyDataRow
{
    private Object[] data;

    public EasyDataRow()
    {
    }

    public EasyDataRow(Object[] data)
    {
        this.data = data;
    }

    public Object[] getData()
    {
        return data;
    }

    public void setData(Object[] data)
    {
        this.data = data;
    }

}
