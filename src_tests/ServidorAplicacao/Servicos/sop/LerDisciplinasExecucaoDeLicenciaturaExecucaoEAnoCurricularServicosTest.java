/*
 * LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricularServicosTest.java
 * JUnit based test
 *
 * Created on 04 de Janeiro de 2003, 12:35
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.*;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.Servicos.*;

public class LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricularServicosTest extends TestCaseServicos {
    public LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricularServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricularServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  // read disciplinas by unauthorized user
  public void testUnauthorizedReadDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular() {
	InfoExecutionDegree iED = new InfoExecutionDegree(_cursoExecucao1.getAnoLectivo(), new InfoDegree(_curso1.getSigla(),_curso1.getNome()));
  	Object argsLerDisciplinas[] = new Object[1];
  	argsLerDisciplinas[0] = new CurricularYearAndSemesterAndInfoExecutionDegree(_disciplinaCurricular1.getCurricularYear(), _disciplinaCurricular1.getSemester(),iED);

  	Object result = null;    
      try {
        result = _gestor.executar(_userView2, "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular", argsLerDisciplinas);
        fail("testUnauthorizedReadDisciplinas");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadDisciplinas", result);
      }
  }

  // read new existing disciplinas
  public void testReadExistingDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular() {
  	InfoExecutionDegree iED = new InfoExecutionDegree(_cursoExecucao1.getAnoLectivo(), new InfoDegree(_curso1.getSigla(),_curso1.getNome()));
  	Object argsLerDisciplinas[] = new Object[1];
  	argsLerDisciplinas[0] = new CurricularYearAndSemesterAndInfoExecutionDegree(_disciplinaCurricular1.getCurricularYear(),_disciplinaCurricular1.getSemester(),iED);

  	Object result = null;
      try {
        result = _gestor.executar(_userView, "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular", argsLerDisciplinas);
        assertEquals("testLerExistingDisciplinas", ((List) result).size(), 1);
      } catch (Exception ex) {
      	fail("testLerExistingDisciplinas");
      }
  }

  // read new non-existing disciplinas
  public void testReadNonExistingDisciplinas() {
  	InfoExecutionDegree iED = new InfoExecutionDegree("1900/1901", new InfoDegree(_curso1.getSigla(),_curso1.getNome()));
  	Object argsLerDisciplinas[] = new Object[1];
  	argsLerDisciplinas[0] = new CurricularYearAndSemesterAndInfoExecutionDegree(_disciplinaCurricular1.getCurricularYear(), _disciplinaCurricular1.getSemester(),iED);

  	Object result = null;
      try {
        result = _gestor.executar(_userView, "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular", argsLerDisciplinas);
        assertTrue("testLerNonExistingDisciplinas", ((List) result).isEmpty());
      } catch (Exception ex) {
      	fail("testLerNonExistingDisciplinas");
      }
  }
    
}
