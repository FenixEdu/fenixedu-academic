/*
 * Created on 29/Jan/2004
 *
 */
package Dominio;

/**
 * 
 * @author Susana Fernandes
 *  
 */
public interface IOnlineTest extends IEvaluation {
    public abstract IDistributedTest getDistributedTest();

    public abstract Integer getKeyDistributedTest();

    public abstract void setDistributedTest(IDistributedTest test);

    public abstract void setKeyDistributedTest(Integer integer);
}