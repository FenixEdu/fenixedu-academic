/*
 * OJBSuite.java
 * JUnit based test
 *
 * Created on 2 de Dezembro de 2002, 17:18
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.Candidate;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

public class ServicosCandidatoSuite extends TestCase {

    public ServicosCandidatoSuite(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        //--JUNIT:
        //This block was automatically generated and can be regenerated again.
        //Do NOT change lines enclosed by the --JUNIT: and :JUNIT-- tags.

        TestSuite suite = new TestSuite("ServicosCandidatoSuite");
        suite.addTest(ServidorAplicacao.Servicos.MasterDegree.Candidate.ChangeApplicationInfoTest
                .suite());

        //:JUNIT--
        //This value MUST ALWAYS be returned from this function.
        return suite;
    }

    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}

}