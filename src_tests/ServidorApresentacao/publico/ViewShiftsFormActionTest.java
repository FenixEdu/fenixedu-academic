package ServidorApresentacao.publico;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

/**
 * @author João Mota
 */
public class ViewShiftsFormActionTest extends MockStrutsTestCase {
	protected ISuportePersistente sp = null;
	protected IDisciplinaExecucaoPersistente executionCourseDAO =
		null;
	protected ITurnoPersistente shiftDAO = null;
	private IPersistentExecutionPeriod executionPeriodDAO;
	private IPersistentExecutionYear executionYearDAO;
	protected ITurno shift = null;

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ViewShiftsFormActionTest.class);

		return suite;
	}

	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuração Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-publico.xml");
		//preenche a DB
		sp = SuportePersistenteOJB.getInstance();
		shiftDAO = sp.getITurnoPersistente();
		executionCourseDAO =
			sp.getIDisciplinaExecucaoPersistente();
			
		executionYearDAO.deleteAll();
		
		
		sp.iniciarTransaccao();
		sp.getICursoExecucaoPersistente().deleteAll();
		sp.getICursoPersistente().deleteAll();
		
		shiftDAO.deleteAll();
		
		
		executionCourseDAO.apagarTodasAsDisciplinasExecucao();
		sp.confirmarTransaccao();
			
		IExecutionPeriod executionPeriod = new ExecutionPeriod();
		executionPeriod.setName("1");
		executionPeriod.setExecutionYear(new ExecutionYear("2002/2003"));
		IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
		
		executionCourse.setSigla("whatever");
		
		executionCourse.setExecutionPeriod(executionPeriod);

		TipoAula tipo = new TipoAula(new Integer(1));
		
		shift = new Turno("blabla", tipo, new Integer(1), executionCourse);
		
		sp.iniciarTransaccao();
		
		shiftDAO.lockWrite(shift);
		
		sp.confirmarTransaccao();
		
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public ViewShiftsFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulViewShiftsFormAction() {
		// define mapping de origem
		setRequestPathInfo("publico", "/viewShifts");

		// preenche o form
		addRequestParameter("courseInitials", "whatever");

		// invoca acção
		actionPerform();

		// verifica reencaminhamento
		verifyForward("Sucess");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}

}