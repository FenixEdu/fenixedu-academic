/*
 * Created on 19/Ago/2003
 *
 */

package net.sourceforge.fenixedu.domain;

/**
 * @author Susana Fernandes
 */

public class DistributedTestAdvisory extends DomainObject implements IDistributedTestAdvisory {

    private IDistributedTest distributedTest;

    private Integer keyDistributedTest;

    private IAdvisory advisory;

    private Integer keyAdvisory;

    public DistributedTestAdvisory() {
    }

    public DistributedTestAdvisory(Integer distributedTestId) {
        setIdInternal(distributedTestId);
    }

    public IAdvisory getAdvisory() {
        return advisory;
    }

    public void setAdvisory(IAdvisory advisory) {
        this.advisory = advisory;
    }

    public IDistributedTest getDistributedTest() {
        return distributedTest;
    }

    public void setDistributedTest(IDistributedTest distributedTest) {
        this.distributedTest = distributedTest;
    }

    public Integer getKeyAdvisory() {
        return keyAdvisory;
    }

    public void setKeyAdvisory(Integer keyAdvisory) {
        this.keyAdvisory = keyAdvisory;
    }

    public Integer getKeyDistributedTest() {
        return keyDistributedTest;
    }

    public void setKeyDistributedTest(Integer keyDistributedTest) {
        this.keyDistributedTest = keyDistributedTest;
    }

}