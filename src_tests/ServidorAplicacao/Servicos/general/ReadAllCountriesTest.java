/*
 * ReadAllCountriesTest.java
 *
 * Created on 04 of March 2003, 11:00
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

package net.sourceforge.fenixedu.applicationTier.Servicos.general;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;

public class ReadAllCountriesTest extends TestCaseReadServices {

    public ReadAllCountriesTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadAllCountriesTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadAllCountries";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 2;
    }

    protected Object getObjectToCompare() {
        return null;
    }

    protected boolean needsAuthorization() {
        return true;
    }
}

