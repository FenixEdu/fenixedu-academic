/*
 * Created on 3/Fev/2004
 *
 */
package Dominio;

/**
 *
 * @author Susana Fernandes
 *
 */
public class TestScope extends DomainObject implements ITestScope
{
	private IDomainObject domainObject;
	private String className;
	private Integer keyClass;

	public TestScope()
	{
	}

	public TestScope(IDomainObject object)
	{
		super();
		this.domainObject = object;
		this.className = object.getClass().getName();
		this.keyClass = object.getIdInternal();
	}

	public TestScope(Integer idInternal)
	{
		super(idInternal);
	}

	public IDomainObject getDomainObject()
	{
		return domainObject;
	}

	public void setDomainObject(IDomainObject object)
	{
		domainObject = object;
	}

	public Integer getKeyClass()
	{
		return keyClass;
	}

	public String getClassName()
	{
		return className;
	}

	public void setKeyClass(Integer integer)
	{
		keyClass = integer;
	}

	public void setClassName(String string)
	{
		className = string;
	}

}
