package ds.made.jdbc.easy.test;

public class EnumTypes
{

	public static Status statusFactory(String code)
	{
		if (code == null)
			return null;
		else if (code.equals("0"))
			return Status.OK;
		else if (code.equals("1"))
			return Status.DELETED;
		else if (code.equals("2"))
			return Status.WRONG;
		else
			return Status.UNKNOWN;
	}

	public enum Status 
	{ 
		OK("0","ok"), DELETED("1","deleted"), WRONG("2","wrong"), UNKNOWN("?","don't know") ;

        public String code;
        public String description;

        private Status(String code, String description)
        {
            this.code = code;
            this.description = description;
        }

        @Override
        public String toString()
        {
            return  code + " - " + description;
        }
	};

}
