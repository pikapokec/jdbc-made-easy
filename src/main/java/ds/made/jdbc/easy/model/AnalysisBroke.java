package ds.made.jdbc.easy.model;

/**
When analyzing class structure there are no correct annotations!
For internal usage.
@author ds
 */
public class AnalysisBroke extends Exception
{

	private static final long serialVersionUID = 1L;

	public AnalysisBroke()
	{
		super();
	}

	public AnalysisBroke(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AnalysisBroke(String message)
	{
		super(message);
	}

	public AnalysisBroke(Throwable cause)
	{
		super(cause);
	}

}
