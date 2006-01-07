/*
 * Created on 20/Ago/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;

/**
 * @author Susana Fernandes
 */
public class InfoDistributedTestWithTestScope extends InfoDistributedTest {

    public void copyFromDomain(DistributedTest distributedTest) {
        super.copyFromDomain(distributedTest);
        if (distributedTest != null) {
            setInfoTestScope(InfoTestScope.newInfoFromDomain(distributedTest.getTestScope()));
        }
    }

    public static InfoDistributedTest newInfoFromDomain(DistributedTest distributedTest) {
        InfoDistributedTestWithTestScope infoDistributedTest = null;
        if (distributedTest != null) {
            infoDistributedTest = new InfoDistributedTestWithTestScope();
            infoDistributedTest.copyFromDomain(distributedTest);
        }
        return infoDistributedTest;
    }
}