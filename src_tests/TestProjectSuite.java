/*
 * TestProjectSuite.java
 *
 * Corre Todos os testes do projecto
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

public class TestProjectSuite extends TestCase {
    
    public TestProjectSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        //--JUNIT:
        //This block was automatically generated and can be regenerated again.
        //Do NOT change lines enclosed by the --JUNIT: and :JUNIT-- tags.
        
        TestSuite suite = new TestSuite("TestProject");
        suite.addTest(ServidorAplicacao.Servicos.ServicosSuite.suite());
        
        //:JUNIT--
        //This value MUST ALWAYS be returned from this function.
        return suite;
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
    
}
