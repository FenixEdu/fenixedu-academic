/*
 * LerAulaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 21:24
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
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.KeyLesson;
import DataBeans.RoomKey;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.DiaSemana;

public class LerAulaServicosTest extends TestCaseServicos {
    public LerAulaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerAulaServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

    // read aula by unauthorized user
  public void testUnauthorizedReadAula() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsLerAula[] = new Object[1];
    argsLerAula[0] = new KeyLesson(_diaSemana1, _inicio, _fim, keySala);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "LerAula", argsLerAula);
        fail("testUnauthorizedReadAula");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadAula", result);
      }
  }

    // read new existing aula
  public void testReadExistingAula() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsLerAula[] = new Object[1];
    argsLerAula[0] = new KeyLesson(_diaSemana1, _inicio, _fim, keySala);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAula", argsLerAula);
      	InfoDegree infoLicenciatura = new InfoDegree(_aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla(),
      	                                                         _aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome());
      	InfoExecutionDegree infoLicenciaturaExecucao = new InfoExecutionDegree(_aula1.getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo(),
      	                                                                                 infoLicenciatura);
      	InfoExecutionCourse infoDisciplinaExecucao = new InfoExecutionCourse(_aula1.getDisciplinaExecucao().getNome(),
      	                                                                           _aula1.getDisciplinaExecucao().getSigla(),
      	                                                                           _aula1.getDisciplinaExecucao().getPrograma(),
     	                                                                           infoLicenciaturaExecucao,
     	                                                                           _aula1.getDisciplinaExecucao().getTheoreticalHours(),
     	                                                                           _aula1.getDisciplinaExecucao().getPraticalHours(),
     	                                                                           _aula1.getDisciplinaExecucao().getTheoPratHours(),
     	                                                                           _aula1.getDisciplinaExecucao().getLabHours());
      	InfoRoom infoSala = new InfoRoom(_aula1.getSala().getNome(), _aula1.getSala().getEdificio(),
      	                                 _aula1.getSala().getPiso(), _aula1.getSala().getTipo(),
      	                                 _aula1.getSala().getCapacidadeNormal(),
      	                                 _aula1.getSala().getCapacidadeExame());
      	InfoLesson infoAula = new InfoLesson(_aula1.getDiaSemana(), _aula1.getInicio(), _aula1.getFim(),
      	                                _aula1.getTipo(), infoSala, infoDisciplinaExecucao);
        assertEquals("testLerExistingAula", infoAula, (InfoLesson) result);
      } catch (Exception ex) {
      	fail("testLerExistingAula");
      }
  }

    // read new non-existing aula
  public void testReadNonExistingAula() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsLerAula[] = new Object[1];
    argsLerAula[0] = new KeyLesson(new DiaSemana(DiaSemana.SEXTA_FEIRA), _inicio, _fim, keySala);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "LerAula", argsLerAula);
        assertNull("testReadNonExistingAula", result);
      } catch (Exception ex) {
      	fail("testReadNonExistingAula");
      }
  }
    
}
