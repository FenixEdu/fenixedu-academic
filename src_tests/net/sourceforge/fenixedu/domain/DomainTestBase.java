/*
 * Created on Jun 28, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.stm.Transaction;

import junit.framework.TestCase;

public class DomainTestBase extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
	Transaction.begin();
        DomainObject.turnOffLockMode();
    }

    protected void tearDown() throws Exception {
	Transaction.abort();
        super.tearDown();
    }


    protected void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

}
