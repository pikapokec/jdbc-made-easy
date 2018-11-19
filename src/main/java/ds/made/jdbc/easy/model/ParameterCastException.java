package ds.made.jdbc.easy.model;

/**
 <strong>Parameter value cast is impossible!</strong>
 @author ds
 */
public class ParameterCastException extends Exception
{

    private static final long serialVersionUID = 1L;

    public ParameterCastException()
    {
        super();
    }

    public ParameterCastException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ParameterCastException(String message)
    {
        super(message);
    }

    public ParameterCastException(Throwable cause)
    {
        super(cause);
    }

}


