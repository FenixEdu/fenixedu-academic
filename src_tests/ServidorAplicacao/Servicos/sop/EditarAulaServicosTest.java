/*
 * EditarAulaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 19:17
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import Util.*;
import ServidorAplicacao.Servicos.*;
import DataBeans.*;


public class EditarAulaServicosTest extends TestCaseServicos {
    public EditarAulaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(EditarAulaServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // edit aula by unauthorized user
  public void testUnauthorizedEditAula() {
  	InfoRoom infoSala = new InfoRoom(_sala1.getNome(), _sala1.getEdificio(), _sala1.getPiso(),
  	                                 _sala1.getTipo(), _sala1.getCapacidadeNormal(),
  	                                 _sala1.getCapacidadeExame());
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

	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsEditarAula[] = new Object[2];
    argsEditarAula[0] = new KeyLesson(_diaSemana1, _inicio, _fim, keySala);
    argsEditarAula[1] = new InfoLesson(_diaSemana1, _inicio, _fim, new TipoAula(TipoAula.PRATICA),
                                    infoSala, iDE);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "EditarAula", argsEditarAula);
        fail("testUnauthorizedEditarAula");
      } catch (Exception ex) {
        assertNull("testUnauthorizedEditarAula", result);
      }
  }

    // edit new existing aula
  public void testEditExistingAula() {
  	InfoRoom infoSala = new InfoRoom(_sala1.getNome(), _sala1.getEdificio(), _sala1.getPiso(),
  	                                 _sala1.getTipo(), _sala1.getCapacidadeNormal(),
  	                                 _sala1.getCapacidadeExame());
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

	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsEditarAula[] = new Object[2];
    argsEditarAula[0] = new KeyLesson(_diaSemana1, _inicio, _fim, keySala);
    argsEditarAula[1] = new InfoLesson(_diaSemana1, _inicio, _fim, new TipoAula(TipoAula.PRATICA),
                                    infoSala, iDE);

    Object result = null; 
      try {
        result = _gestor.executar(_userView, "EditarAula", argsEditarAula);
        assertTrue("testEditNonExistingAula", ((InfoLessonServiceResult) result).isSUCESS());
      } catch (Exception ex) {
      	fail("testEditNonExistingAula");
      }
  }

    // edit new non-existing aula
  public void testEditarNonExistingAula() {
  	InfoRoom infoSala = new InfoRoom(_sala1.getNome(), _sala1.getEdificio(), _sala1.getPiso(),
  	                                 _sala1.getTipo(), _sala1.getCapacidadeNormal(),
  	                                 _sala1.getCapacidadeExame());
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
	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsEditarAula[] = new Object[2];
    argsEditarAula[0] = new KeyLesson(new DiaSemana(DiaSemana.SEXTA_FEIRA), _inicio, _fim, keySala);
    argsEditarAula[1] = new InfoLesson(new DiaSemana(DiaSemana.SEXTA_FEIRA), _inicio, _fim, new TipoAula(TipoAula.PRATICA),
                                    infoSala, iDE);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "EditarAula", argsEditarAula);
        assertNull("testEditNonExistingAula", result);
      } catch (Exception ex) {
      	fail("testEditNonExistingAula");
      }
  }
    
}
