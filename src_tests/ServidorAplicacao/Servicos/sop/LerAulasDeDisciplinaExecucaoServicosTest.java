/*
 * LerAulasDeDisciplinaExecucaoServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 23:34
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import java.util.List;
import DataBeans.*;
import ServidorAplicacao.Servicos.*;

public class LerAulasDeDisciplinaExecucaoServicosTest extends TestCaseServicos {
    public LerAulasDeDisciplinaExecucaoServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerAulasDeDisciplinaExecucaoServicosTest.class);

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
  	InfoDegree infoLicenciatura = new InfoDegree(_aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla(),
  	                                                         _aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome());
  	InfoExecutionDegree infoLicenciaturaExecucao = new InfoExecutionDegree(_aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo(),
  	                                                                                 infoLicenciatura);
    Object argsLerAulas[] = new Object[1];
    argsLerAulas[0] = new InfoExecutionCourse(_aula1.getDisciplinaExecucao().getNome(),
                                                 _aula1.getDisciplinaExecucao().getSigla(),
                                                 _aula1.getDisciplinaExecucao().getPrograma(),
                                                 infoLicenciaturaExecucao,
                                                 _aula1.getDisciplinaExecucao().getTheoreticalHours(),
                                                 _aula1.getDisciplinaExecucao().getPraticalHours(),
                                                 _aula1.getDisciplinaExecucao().getTheoPratHours(),
                                                 _aula1.getDisciplinaExecucao().getLabHours());
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "LerAulasDeDisciplinaExecucao", argsLerAulas);
        fail("testUnauthorizedReadAulas");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadAulas", result);
      }
  }

  // read new existing aulas
  public void testReadExistingAulas() {
  	InfoDegree infoLicenciatura = new InfoDegree(_aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla(),
  	                                                         _aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome());
  	InfoExecutionDegree infoLicenciaturaExecucao = new InfoExecutionDegree(_aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo(),
  	                                                                                 infoLicenciatura);
    Object argsLerAulas[] = new Object[1];
    argsLerAulas[0] = new InfoExecutionCourse(_aula1.getDisciplinaExecucao().getNome(),
    											 _aula1.getDisciplinaExecucao().getSigla(),
    											 _aula1.getDisciplinaExecucao().getPrograma(),
    											 infoLicenciaturaExecucao,
    											 _aula1.getDisciplinaExecucao().getTheoreticalHours(),
    											 _aula1.getDisciplinaExecucao().getPraticalHours(),
    											 _aula1.getDisciplinaExecucao().getTheoPratHours(),
    											 _aula1.getDisciplinaExecucao().getLabHours());
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAulasDeDisciplinaExecucao", argsLerAulas);
        assertEquals("testLerExistingAulas", 1, ((List) result).size());
      } catch (Exception ex) {
      	fail("testLerExistingAulas");
      }
  }

  // read new non-existing aulas
  public void testReadNonExistingAulas() {
  	InfoDegree infoLicenciatura = new InfoDegree(_aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla(),
  	                                                         _aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome());
  	InfoExecutionDegree infoLicenciaturaExecucao = new InfoExecutionDegree("1850/51",
  	                                                                                 infoLicenciatura);
    Object argsLerAulas[] = new Object[1];
    argsLerAulas[0] = new InfoExecutionCourse(_aula1.getDisciplinaExecucao().getNome(),
    											 _aula1.getDisciplinaExecucao().getSigla(),
    											 _aula1.getDisciplinaExecucao().getPrograma(),
    											 infoLicenciaturaExecucao,
    											 _aula1.getDisciplinaExecucao().getTheoreticalHours(),
    											 _aula1.getDisciplinaExecucao().getPraticalHours(),
    											 _aula1.getDisciplinaExecucao().getTheoPratHours(),
    											 _aula1.getDisciplinaExecucao().getLabHours());
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAulasDeDisciplinaExecucao", argsLerAulas);
        assertTrue("testLerNonExistingAulas", ((List) result).isEmpty());
      } catch (Exception ex) {
      	fail("testLerNonExistingAulas");
      }
  }
    
}
