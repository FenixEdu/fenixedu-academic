/*
 * ReadStudentTest.java
 * JUnit based test
 *
 * Created on 16th of December de 2002, 3:05
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servicos.*;

public class ReadStudentTest extends TestCaseServicos {
    public ReadStudentTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ReadStudentTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  // read student by unauthorized user
  public void testUnauthorizedReadStudent() {
    Object argsReadStudent[] = new Object[1];
    argsReadStudent[0] = new Integer(45498);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "ReadStudent", argsReadStudent);
        fail("testUnauthorizedReadStudent");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadStudent", result);
      }
  }

  // read existing student by authorized user
  public void testReadExistingStudent() {
	Object argsReadStudent[] = new Object[1];
	argsReadStudent[0] = new Integer(600);

	Object result = null; 
	  try {
		result = _gestor.executar(_userView, "ReadStudent", argsReadStudent);
		assertNotNull("testReadExistingStudent", result);
		InfoStudent res = (InfoStudent) result;
		assertEquals("testReadExistingStudent", _aluno1.getNumber(), res.getNumber());
		assertEquals("testReadExistingStudent", _aluno1.getState(), res.getState());
		assertEquals("testReadExistingStudent", _aluno1.getPerson().getNome(),
		                                        res.getInfoPerson().getNome());
	  } catch (Exception ex) {
		fail("testReadExistingStudent");
	  }
  }

  // read non-existing student by authorized user
  public void testReadUnExistingStudent() {
	Object argsReadStudent[] = new Object[1];
	argsReadStudent[0] = new Integer(601);

	Object result = null; 
	  try {
		result = _gestor.executar(_userView, "ReadStudent", argsReadStudent);
		assertNull("testReadExistingStudent", result);
	  } catch (Exception ex) {
		fail("testReadExistingStudent");
	  }
  }

}
