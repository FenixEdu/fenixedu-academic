package ServidorApresentacao.publico;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;
import Util.TipoCurso;

/**
 * @author João Mota
 */
public class ViewShiftsFormActionTest extends MockStrutsTestCase {
	protected ISuportePersistente _suportePersistente = null;
	protected IDisciplinaExecucaoPersistente _disciplinaExecucaoPersistente =
		null;
	protected ITurnoPersistente _turnoPersistente = null;
	protected ITurno _turno = null;

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
		_suportePersistente = SuportePersistenteOJB.getInstance();
		_turnoPersistente = _suportePersistente.getITurnoPersistente();
		_disciplinaExecucaoPersistente =
			_suportePersistente.getIDisciplinaExecucaoPersistente();
		_suportePersistente.iniciarTransaccao();
		_suportePersistente.getICursoExecucaoPersistente().deleteAll();
		_suportePersistente.getICursoPersistente().deleteAll();
		_turnoPersistente.deleteAll();
		_disciplinaExecucaoPersistente.apagarTodasAsDisciplinasExecucao();
		_suportePersistente.confirmarTransaccao();
		System.out.println("***************apagueitudo da bd");

		DisciplinaExecucao exeCourse = new DisciplinaExecucao();
		exeCourse.setSigla("whatever");
		exeCourse.setChaveLicenciaturaExecucao(new Integer(1));
		exeCourse.setChaveResponsavel(new Integer(1));

		exeCourse.setLicenciaturaExecucao(
			new CursoExecucao(
				"2002/03",
				new Curso("sigla", "nome", new TipoCurso(new Integer(1)))));

		TipoAula tipo = new TipoAula(new Integer(1));
		_turno = new Turno("blabla", tipo, new Integer(1), exeCourse);
		_suportePersistente.iniciarTransaccao();
		_turnoPersistente.lockWrite(_turno);
		_suportePersistente.confirmarTransaccao();
		System.out.println("***************escrevi tudo na bd");
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