package ds.made.jdbc.easy.model;

/**
<strong>Something went just wrong!</strong>
@author ds
 */
public class SomethingJustWrong extends Exception
{

	private static final long serialVersionUID = 1L;

	public SomethingJustWrong()
	{
		super();
	}

	public SomethingJustWrong(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SomethingJustWrong(String message)
	{
		super(message);
	}

	public SomethingJustWrong(Throwable cause)
	{
		super(cause);
	}

}
