/*
 * CriarAulaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 15:51
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

public class CriarAulaServicosTest extends TestCaseServicos {
    public CriarAulaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(CriarAulaServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // write aula by unauthorized user
  public void testUnauthorizedCreateAula() {
  	InfoRoom infoSala = new InfoRoom(_sala1.getNome(), _sala1.getEdificio(),
  	                                 _sala1.getPiso(), _sala1.getTipo(),
  	                                 _sala1.getCapacidadeNormal(),
  	                                 _sala1.getCapacidadeExame());
	InfoDegree infoLicenciatura =
		new InfoDegree(
			_disciplinaExecucao1
				.getLicenciaturaExecucao()
				.getCurso()
				.getSigla(),
			_disciplinaExecucao1
				.getLicenciaturaExecucao()
				.getCurso()
				.getNome());
	InfoExecutionDegree infoLicenciaturaExecucao =
		new InfoExecutionDegree(
			_disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
			infoLicenciatura);
	InfoExecutionCourse infoDisciplinaExecucao =
		new InfoExecutionCourse(
			_disciplinaExecucao1.getNome(),
			_disciplinaExecucao1.getSigla(),
			_disciplinaExecucao1.getPrograma(),
			infoLicenciaturaExecucao,
			_disciplinaExecucao1.getTheoreticalHours(),
			_disciplinaExecucao1.getPraticalHours(),
			_disciplinaExecucao1.getTheoPratHours(),
			_disciplinaExecucao1.getLabHours());
    Object argsCriarAula[] = new Object[1];
    argsCriarAula[0] = new InfoLesson(_diaSemana1, _inicio, _fim, _tipoAula,
                                    infoSala, infoDisciplinaExecucao);

    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "CriarAula", argsCriarAula);
        fail("testUnauthorizedCreateAula");
      } catch (Exception ex) {
        // all is ok
        assertNull("testUnauthorizedCreateAula", result);
      }
  }

    // write new existing aula
  public void testCreateExistingAula() {
  	InfoRoom infoSala = new InfoRoom(_sala1.getNome(), _sala1.getEdificio(),
  	                                 _sala1.getPiso(), _sala1.getTipo(),
  	                                 _sala1.getCapacidadeNormal(),
  	                                 _sala1.getCapacidadeExame());
	InfoDegree infoLicenciatura =
		new InfoDegree(
			_disciplinaExecucao1
				.getLicenciaturaExecucao()
				.getCurso()
				.getSigla(),
			_disciplinaExecucao1
				.getLicenciaturaExecucao()
				.getCurso()
				.getNome());
	InfoExecutionDegree infoLicenciaturaExecucao =
		new InfoExecutionDegree(
			_disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
			infoLicenciatura);
	InfoExecutionCourse infoDisciplinaExecucao =
		new InfoExecutionCourse(
			_disciplinaExecucao1.getNome(),
			_disciplinaExecucao1.getSigla(),
			_disciplinaExecucao1.getPrograma(),
			infoLicenciaturaExecucao,
			_disciplinaExecucao1.getTheoreticalHours(),
			_disciplinaExecucao1.getPraticalHours(),
			_disciplinaExecucao1.getTheoPratHours(),
			_disciplinaExecucao1.getLabHours());
    Object argsCriarAula[] = new Object[1];
    argsCriarAula[0] = new InfoLesson(_diaSemana1, _inicio, _fim, _tipoAula,
                                    infoSala, infoDisciplinaExecucao);

    Object result = null; 
    try {	
      result = _gestor.executar(_userView, "CriarAula", argsCriarAula);
      fail("testCreateExistingAula");
    } catch (Exception ex) {
      assertNull("testCreateExistingAula", result);
    }
  }

    // write new non-existing aula
  public void testCreateNonExistingAula() {
  	InfoRoom infoSala = new InfoRoom(_sala1.getNome(), _sala1.getEdificio(),
  	                                 _sala1.getPiso(), _sala1.getTipo(),
  	                                 _sala1.getCapacidadeNormal(),
  	                                 _sala1.getCapacidadeExame());
	InfoDegree infoLicenciatura =
		new InfoDegree(
			_disciplinaExecucao1
				.getLicenciaturaExecucao()
				.getCurso()
				.getSigla(),
			_disciplinaExecucao1
				.getLicenciaturaExecucao()
				.getCurso()
				.getNome());
	InfoExecutionDegree infoLicenciaturaExecucao =
		new InfoExecutionDegree(
			_disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
			infoLicenciatura);
	InfoExecutionCourse infoDisciplinaExecucao =
		new InfoExecutionCourse(
			_disciplinaExecucao1.getNome(),
			_disciplinaExecucao1.getSigla(),
			_disciplinaExecucao1.getPrograma(),
			infoLicenciaturaExecucao,
			_disciplinaExecucao1.getTheoreticalHours(),
			_disciplinaExecucao1.getPraticalHours(),
			_disciplinaExecucao1.getTheoPratHours(),
			_disciplinaExecucao1.getLabHours());

    Object argsCriarAula[] = new Object[1];
    argsCriarAula[0] = new InfoLesson(new DiaSemana(DiaSemana.SEXTA_FEIRA),
                                    _inicio, _fim, _tipoAula, infoSala,
                                    infoDisciplinaExecucao);

    Object result = null; 
      try {
        result = _gestor.executar(_userView, "CriarAula", argsCriarAula);
        assertTrue("testCreateNonExistingAula", ((InfoLessonServiceResult) result).isSUCESS());
      } catch (Exception ex) {
        fail("testCreateNonExistingAula");
      }
  }
    
}
