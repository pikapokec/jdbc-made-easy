package ds.made.jdbc.easy.test;

public class TestSelectNestedOnce
{

	private TestEntityNoAnnotation data;

	public TestEntityNoAnnotation getData()
	{
		return data;
	}

	public void setData(TestEntityNoAnnotation data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		return "TestSelectNestedOnce [data=" + data + "]";
	}
	
}
