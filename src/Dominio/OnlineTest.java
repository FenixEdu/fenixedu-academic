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
public class OnlineTest extends Evaluation implements IOnlineTest {
    private Integer keyDistributedTest;

    private IDistributedTest distributedTest;

    public OnlineTest() {
    }

    public IDistributedTest getDistributedTest() {
        return distributedTest;
    }

    public Integer getKeyDistributedTest() {
        return keyDistributedTest;
    }

    public void setDistributedTest(IDistributedTest test) {
        distributedTest = test;
    }

    public void setKeyDistributedTest(Integer integer) {
        keyDistributedTest = integer;
    }

}