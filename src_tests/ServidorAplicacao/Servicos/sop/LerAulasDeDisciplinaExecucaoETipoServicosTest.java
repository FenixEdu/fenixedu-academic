/*
 * LerAulasDeDisciplinaExecucaoETipoServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 18:36
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.ExecutionCourseKeyAndLessonType;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.TipoAula;

public class LerAulasDeDisciplinaExecucaoETipoServicosTest extends TestCaseServicos {
    public LerAulasDeDisciplinaExecucaoETipoServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerAulasDeDisciplinaExecucaoETipoServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // read aulas by unauthorized user
  public void testUnauthorizedReadAulas() {
    Object argsLerAulas[] = new Object[1];
    argsLerAulas[0] = new ExecutionCourseKeyAndLessonType(new TipoAula(TipoAula.TEORICA), _disciplinaExecucao1.getSigla());
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "LerAulasDeDisciplinaExecucaoETipo", argsLerAulas);
        fail("testUnauthorizedReadAulas");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadAulas", result);
      }
  }

    // read new existing aulas
  public void testReadExistingAulas() {
    Object argsLerAulas[] = new Object[1];
    argsLerAulas[0] = new ExecutionCourseKeyAndLessonType(new TipoAula(TipoAula.TEORICA), _disciplinaExecucao1.getSigla());
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAulasDeDisciplinaExecucaoETipo", argsLerAulas);
        assertEquals("testLerExistingAulas", 1, ((List) result).size());
      } catch (Exception ex) {
      	fail("testLerExistingAulas");
      }
  }

    // read new non-existing aulas
  public void testReadNonExistingAulas() {
    Object argsLerAulas[] = new Object[1];
    argsLerAulas[0] = new ExecutionCourseKeyAndLessonType(new TipoAula(TipoAula.PRATICA), _disciplinaExecucao2.getSigla());

    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAulasDeDisciplinaExecucaoETipo", argsLerAulas);
        assertTrue("testLerNonExistingAulas", ((List) result).isEmpty());
      } catch (Exception ex) {
      	fail("testLerNonExistingAulas");
      }
  }
    
}
