package ds.made.jdbc.easy.test;

import ds.made.jdbc.easy.annotations.EasyColumn;
import ds.made.jdbc.easy.annotations.EasyRow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestEntityNoAnnotation
{

	private String someString;

	private LocalDate justDate;
	
	private Integer someInteger;
	
	private BigDecimal currency;
	
	private EnumTypes.Status status;

	private LocalDateTime pointInTime;

	public String getSomeString()
	{
		return someString;
	}

	public void setSomeString(String someString)
	{
		this.someString = someString;
	}

	public LocalDate getJustDate()
	{
		return justDate;
	}

	public void setJustDate(LocalDate justDate)
	{
		this.justDate = justDate;
	}

	public Integer getSomeInteger()
	{
		return someInteger;
	}

	public void setSomeInteger(Integer someInteger)
	{
		this.someInteger = someInteger;
	}

	public BigDecimal getCurrency()
	{
		return currency;
	}

	public void setCurrency(BigDecimal currency)
	{
		this.currency = currency;
	}

	public EnumTypes.Status getStatus()
	{
		return status;
	}

	public void setStatus(EnumTypes.Status status)
	{
		this.status = status;
	}

	public LocalDateTime getPointInTime()
	{
		return pointInTime;
	}

	public void setPointInTime(LocalDateTime pointInTime)
	{
		this.pointInTime = pointInTime;
	}

	@Override
	public String toString()
	{
		return "TestEntity{" +
				"someString='" + someString + '\'' +
				", justDate=" + justDate +
				", someInteger=" + someInteger +
				", currency=" + currency +
				", status=" + status +
				", pointInTime=" + pointInTime +
				'}';
	}
}
