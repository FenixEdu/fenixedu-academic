/*
 * DisciplinaExecucaoOJBTest.java
 * JUnit based test
 *
 * Created on 29 de Novembro de 2002, 18:16
 */

package ServidorPersistente.OJB;

/**
 *
 * @author tfc130
 */

import junit.framework.*;
import ServidorPersistente.*;
import Dominio.*;

public class DisciplinaExecucaoOJBTest extends TestCaseOJB {
    public DisciplinaExecucaoOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(DisciplinaExecucaoOJBTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
//    public IDisciplinaExecucao readBySiglaAndAnoLectivAndSiglaLicenciatura(String sigla, String anoLectivo, String siglaLicenciatura)    
  /** Test of readBySiglaAndAnoLectivAndSiglaLicenciatura method, of class ServidorPersistente.OJB.DisciplinaCurricularDisciplinaExecucaoOJB. */
  public void testReadBySiglaAndAnoLectivAndSiglaLicenciatura() {
    IDisciplinaExecucao disciplinaExecucao = null;
    // read existing DisciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      disciplinaExecucao = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivAndSiglaLicenciatura(_disciplinaExecucao1.getSigla(),
                                                                                                      _disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
                                                                                                      _disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getSigla());
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySiglaAndAnoLectivAndSiglaLicenciatura:fail read existing disciplinaExecucao");
    }

	assertTrue(disciplinaExecucao.getNome().equals("Trabalho Final de Curso I"));
	assertTrue(disciplinaExecucao.getSigla().equals("TFCI"));
	assertTrue(disciplinaExecucao.getPrograma().equals("programa1"));
	assertTrue(disciplinaExecucao.getTheoreticalHours().equals(new Double(0)));
	assertTrue(disciplinaExecucao.getPraticalHours().equals(new Double(0)));
	assertTrue(disciplinaExecucao.getTheoPratHours().equals(new Double(0)));
	assertTrue(disciplinaExecucao.getLabHours().equals(new Double(0)));	
	
	assertNotNull(disciplinaExecucao.getAssociatedCurricularCourses());
	assertTrue(disciplinaExecucao.getAssociatedCurricularCourses().size() == 1);

    // read unexisting DisciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      disciplinaExecucao = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivAndSiglaLicenciatura("SiglaDisciplinaInexistente",
                                                                                                      "2000/01",
                                                                                                      "SiglaLicenciaturaInexistente");
      assertNull(disciplinaExecucao);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByBySiglaAndAnoLectivAndSiglaLicenciatura:fail read unexisting disciplinaExecucao");
    }
  }

}