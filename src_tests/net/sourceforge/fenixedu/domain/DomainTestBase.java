/*
 * Created on Jun 28, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import junit.framework.TestCase;

public class DomainTestBase extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        DomainObject.turnOffLockMode();
    }

    protected void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

}
