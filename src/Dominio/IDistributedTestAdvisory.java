/*
 * Created on 24/Mar/2004
 *
 */

package Dominio;

/**
 * @author Susana Fernandes
 *  
 */

public interface IDistributedTestAdvisory extends IDomainObject
{
	public abstract IAdvisory getAdvisory();

	public abstract void setAdvisory(IAdvisory advisory);

	public abstract IDistributedTest getDistributedTest();

	public abstract void setDistributedTest(IDistributedTest distributedTest);

	public abstract Integer getKeyAdvisory();

	public abstract void setKeyAdvisory(Integer keyAdvisory);

	public abstract Integer getKeyDistributedTest();

	public abstract void setKeyDistributedTest(Integer keyDistributedTest);
}