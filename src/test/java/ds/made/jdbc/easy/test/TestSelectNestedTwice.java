package ds.made.jdbc.easy.test;

public class TestSelectNestedTwice
{

	private TestSelectNestedOnce data;

	public TestSelectNestedOnce getData()
	{
		return data;
	}

	public void setData(TestSelectNestedOnce data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		return "TestSelectNestedTwice [data=" + data + "]";
	}
	
}
