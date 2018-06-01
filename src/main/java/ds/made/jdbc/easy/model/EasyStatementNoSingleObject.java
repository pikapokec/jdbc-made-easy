package ds.made.jdbc.easy.model;

/**
When you tried to obtain just a single record from the database, <strong>you have got a surprise exception</strong>!<br/>
There is no single result, obviously.
@author ds
 */
public class EasyStatementNoSingleObject extends Exception
{

	private static final long serialVersionUID = 1L;

	public EasyStatementNoSingleObject()
	{
		super();
	}

	public EasyStatementNoSingleObject(String message, Throwable cause)
	{
		super(message, cause);
	}

	public EasyStatementNoSingleObject(String message)
	{
		super(message);
	}

	public EasyStatementNoSingleObject(Throwable cause)
	{
		super(cause);
	}

}
