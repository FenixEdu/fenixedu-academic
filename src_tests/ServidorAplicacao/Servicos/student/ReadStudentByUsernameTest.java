/*
 * ReadStudentTest.java
 * JUnit based test
 *
 * Created on 07th of January de 2003, 23:11
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servicos.*;

public class ReadStudentByUsernameTest extends TestCaseServicos {
    public ReadStudentByUsernameTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ReadStudentByUsernameTest.class);
        
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
    argsReadStudent[0] = new String("45498");
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "ReadStudentByUsername", argsReadStudent);
        fail("testUnauthorizedReadStudentByUsername");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadStudentByUsername", result);
      }
  }

  // read existing student by authorized user
  public void testReadExistingStudent() {
	Object argsReadStudent[] = new Object[1];
	argsReadStudent[0] = new String("nome");

	Object result = null; 
	  try {
		result = _gestor.executar(_userView, "ReadStudentByUsername", argsReadStudent);
		assertNotNull("testReadExistingStudentByUsername", result);
		InfoStudent res = (InfoStudent) result;
		assertEquals("testReadExistingStudentByUsername", _aluno1.getNumber(), res.getNumber());
		assertEquals("testReadExistingStudentByUsername", _aluno1.getState(), res.getState());
		assertEquals("testReadExistingStudentByUsername", _aluno1.getPerson().getNome(),
		                                        res.getInfoPerson().getNome());
	  } catch (Exception ex) {
		fail("testReadExistingStudentByUsername");
	  }
  }

  // read non-existing student by authorized user
  public void testReadUnExistingStudent() {
	Object argsReadStudent[] = new Object[1];
	argsReadStudent[0] = new String("nome2");

	Object result = null; 
	  try {
		result = _gestor.executar(_userView, "ReadStudentByUsername", argsReadStudent);
		assertNull("testReadExistingStudentByUsername", result);
	  } catch (Exception ex) {
		fail("testReadExistingStudentByUsername");
	  }
  }

}
