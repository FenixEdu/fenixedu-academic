/*
 * CriarTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 18:53
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoShift;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.TipoAula;

public class CriarTurnoServicosTest extends TestCaseServicos {
    public CriarTurnoServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(CriarTurnoServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // write new existing turno
  public void testUnauthorizedCreateTurno() {
    Object argsCriarTurno[] = new Object[1];
    InfoDegree infoLicenciatura = new InfoDegree(_disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getSigla(),
                                                             _disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getNome());
    InfoExecutionDegree infoLicenciaturaExecucao = new InfoExecutionDegree(_disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
                                                                                     infoLicenciatura);
    InfoExecutionCourse iDE = new InfoExecutionCourse(_disciplinaExecucao1.getNome(),
                                                        _disciplinaExecucao1.getSigla(),
                                                        _disciplinaExecucao1.getPrograma(),
                                                        infoLicenciaturaExecucao,
                                                        _disciplinaExecucao1.getTheoreticalHours(),
                                                        _disciplinaExecucao1.getPraticalHours(),
                                                        _disciplinaExecucao1.getTheoPratHours(),
                                                        _disciplinaExecucao1.getLabHours());
    argsCriarTurno[0] = new InfoShift(new String("turno1"), new TipoAula(TipoAula.TEORICA), new Integer(100), iDE);

    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "CriarTurno", argsCriarTurno);
        fail("testUnauthorizedCreateTurno");
      } catch (Exception ex) {
        assertNull("testUnauthorizedCreateTurno", result);
      }
  }

    // write new existing turno
  public void testCreateExistingTurno() {
    Object argsCriarTurno[] = new Object[1];
    InfoDegree infoLicenciatura = new InfoDegree(_disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getSigla(),
                                                             _disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getNome());
    InfoExecutionDegree infoLicenciaturaExecucao = new InfoExecutionDegree(_disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
                                                                                     infoLicenciatura);
	InfoExecutionCourse iDE = new InfoExecutionCourse(_disciplinaExecucao1.getNome(),
 														_disciplinaExecucao1.getSigla(),
 														_disciplinaExecucao1.getPrograma(),
 														infoLicenciaturaExecucao,
 														_disciplinaExecucao1.getTheoreticalHours(),
 														_disciplinaExecucao1.getPraticalHours(),
 														_disciplinaExecucao1.getTheoPratHours(),
 														_disciplinaExecucao1.getLabHours());
    argsCriarTurno[0] = new InfoShift(new String("turno1"), new TipoAula(TipoAula.TEORICA), new Integer(100), iDE);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "CriarTurno", argsCriarTurno);
        fail("testCreateExistingTurno");
      } catch (Exception ex) {
      	assertNull("testCreateExistingTurno", result);
      }
  }

    // write new non-existing turno
  public void testCreateNonExistingTurno() {
    Object argsCriarTurno[] = new Object[1];
    InfoDegree infoLicenciatura = new InfoDegree(_disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getSigla(),
                                                             _disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getNome());
    InfoExecutionDegree infoLicenciaturaExecucao = new InfoExecutionDegree(_disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
                                                                                     infoLicenciatura);
	InfoExecutionCourse iDE = new InfoExecutionCourse(_disciplinaExecucao1.getNome(),
														_disciplinaExecucao1.getSigla(),
														_disciplinaExecucao1.getPrograma(),
														infoLicenciaturaExecucao,
														_disciplinaExecucao1.getTheoreticalHours(),
														_disciplinaExecucao1.getPraticalHours(),
														_disciplinaExecucao1.getTheoPratHours(),
														_disciplinaExecucao1.getLabHours());
    argsCriarTurno[0] = new InfoShift(new String("turno3"), new TipoAula(TipoAula.TEORICA), new Integer(100), iDE);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "CriarTurno", argsCriarTurno);
        assertEquals("testCreateNonExistingTurno", Boolean.TRUE.booleanValue(),
                                                   ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testCreateNonExistingTurno");
      }
  }
    
}
