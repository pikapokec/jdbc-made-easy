package ds.made.jdbc.easy.test;

public class TestSelectNested
{
    private String someString;

    private TestSelectNestedOnce once;

    private TestSelectNestedTwice twice;


    public String getSomeString()
    {
        return someString;
    }

    public void setSomeString(String someString)
    {
        this.someString = someString;
    }

    public TestSelectNestedOnce getOnce()
    {
        return once;
    }

    public void setOnce(TestSelectNestedOnce once)
    {
        this.once = once;
    }

    public TestSelectNestedTwice getTwice()
    {
        return twice;
    }

    public void setTwice(TestSelectNestedTwice twice)
    {
        this.twice = twice;
    }

    @Override
    public String toString()
    {
        return "TestSelectNested{" +
                "someString='" + someString + '\'' +
                ", once=" + once +
                ", twice=" + twice +
                '}';
    }
}
