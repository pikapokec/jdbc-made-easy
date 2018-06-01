package ds.made.jdbc.easy.model;

/**
When you try to obtain too many rows from the database, you'll have a <strong>surprise exception</strong>!
Because you were so smart that you set the right method before mapping all the result set (setRowLimit).

@author ds
 */
public class EasyResultSetTooManyRows extends SomethingJustWrong
{

	private static final long serialVersionUID = 1L;

	public EasyResultSetTooManyRows()
	{
		super();
	}

	public EasyResultSetTooManyRows(String message, Throwable cause)
	{
		super(message, cause);
	}

	public EasyResultSetTooManyRows(String message)
	{
		super(message);
	}

	public EasyResultSetTooManyRows(Throwable cause)
	{
		super(cause);
	}

}
