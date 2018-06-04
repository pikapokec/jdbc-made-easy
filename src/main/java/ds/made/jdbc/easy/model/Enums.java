package ds.made.jdbc.easy.model;

public class Enums
{

	public enum STORED_PROCEDURE_TYPE { FUNCTION, PROCEDURE };
	
	public enum PARAMETER_DIRECTION { IN, OUT, IN_OUT, RETURN };

	public enum PARAMETER_DATE_TYPE { Util, LocalDate, LocalDateTime };
}
