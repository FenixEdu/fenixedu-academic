/*
 * Created on Jun 28, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import junit.framework.TestCase;
import net.sourceforge.fenixedu.stm.Transaction;

public class DomainTestBase extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        try {
        	Transaction.abort();
        	Transaction.commit();
        }catch (Exception e) {
			
		}
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
