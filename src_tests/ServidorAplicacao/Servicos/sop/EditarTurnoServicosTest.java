/*
 * EditarTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 21:05
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
import DataBeans.ShiftKey;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.TipoAula;

public class EditarTurnoServicosTest extends TestCaseServicos {
    public EditarTurnoServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(EditarTurnoServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // edit turno by unauthorized user
  public void testUnauthorizedEditTurno() {
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
    Object argsEditarTurno[] = new Object[2];
    argsEditarTurno[0] = new ShiftKey("turno1", iDE);
    argsEditarTurno[1] = new InfoShift("turno1", new TipoAula(TipoAula.PRATICA), new Integer(50), iDE);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "EditarTurno", argsEditarTurno);
        fail("testUnauthorizedEditarTurno");
      } catch (Exception ex) {
        assertNull("testUnauthorizedEditarTurno", result);
      }
  }

    // edit new existing turno
  public void testEditExistingTurno() {
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
    Object argsEditarTurno[] = new Object[2];
    argsEditarTurno[0] = new ShiftKey("turno1", iDE);    
    argsEditarTurno[1] = new InfoShift("turno1", new TipoAula(TipoAula.PRATICA), new Integer(50), iDE);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "EditarTurno", argsEditarTurno);
        assertEquals("testEditNonExistingTurno", Boolean.TRUE.booleanValue(),
                                                 ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testEditNonExistingTurno");
      }
  }

    // edit new non-existing turno
  public void testEditarNonExistingTurno() {
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
    Object argsEditarTurno[] = new Object[2];
    argsEditarTurno[0] = new ShiftKey("turno3", iDE);
    argsEditarTurno[1] = new InfoShift("turno3", new TipoAula(TipoAula.PRATICA), new Integer(50), iDE);

    Object result = null; 
      try {
        result = _gestor.executar(_userView, "EditarTurno", argsEditarTurno);
        assertEquals("testEditNonExistingTurno", Boolean.FALSE.booleanValue(),
                                                 ((Boolean) result).booleanValue());
      } catch (Exception ex) {
      	fail("testEditNonExistingTurno");
      }
  }
    
}
