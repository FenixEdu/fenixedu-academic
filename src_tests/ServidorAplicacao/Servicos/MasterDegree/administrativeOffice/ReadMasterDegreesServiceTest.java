/*
 * ReadMasterDegreeCandidateByUsernameTest.java
 *
 * Created on 06 de Dezembro de 2002, 18:51
 *
 * Tests :
 * 
 * - 1 : Read an existing Master Degree Candidate
 * - 2 : Read an non existing Master Degree Candidate
 * 
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice;

import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;

public class ReadMasterDegreesServiceTest extends TestCaseServicos {

    public ReadMasterDegreesServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadMasterDegreesServiceTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();

    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testReadMasterDegreeCandidateExisting() {
        System.out.println("- Test 1 : Read Master Degrees");
        List masterDegrees = null;

        Object args[] = { "2002/2003" };
        try {
            masterDegrees = (List) ServiceManagerServiceFactory.executeService(this
                    .getUserViewToBeTested(), "ReadMasterDegrees", args);
        } catch (Exception ex) {
            System.out.println("Service Not Executed: " + ex);
        }

        assertNotNull(masterDegrees);
        assertTrue(!masterDegrees.isEmpty());
        assertEquals(3, masterDegrees.size());

    }

    private UserView getUserViewToBeTested() {
        UserView userView = new UserView("nmsn", null);
        return userView;
    }

}

