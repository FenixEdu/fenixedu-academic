/**
 * 
 * Project sop 
 * Package ServidorApresentacao.sop 
 * Created on 16/Jan/2003
 */
package ServidorApresentacao.sop;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IPessoa;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.Pessoa;
import Dominio.Privilegio;
import Dominio.Turma;
import Dominio.TurmaTurno;
import Dominio.Turno;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.ClassShiftManagerDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

/**
 * @author João Mota
 *
 */
public class ClassShiftManagerDispatchActionTest extends MockStrutsTestCase {
	protected ISuportePersistente _suportePersistente = null;
	protected ICursoPersistente _cursoPersistente = null;
	protected ICursoExecucaoPersistente _cursoExecucaoPersistente = null;
	protected ITurmaPersistente _turmaPersistente = null;
	protected IPessoaPersistente _pessoaPersistente = null;
	protected IDisciplinaExecucaoPersistente _disciplinaExecucaoPersistente =
		null;
	protected ITurnoPersistente _turnoPersistente = null;
	protected ICurso curso1 = null;
	protected ICurso curso2 = null;
	protected ICursoExecucao _cursoExecucao1 = null;
	protected ICursoExecucao _cursoExecucao2 = null;
	protected IDisciplinaExecucao _disciplinaExecucao1 = null;
	protected ITurma _turma1 = null;
	protected ITurma _turma2 = null;
	protected ITurno _turno1 = null;
	protected ITurno _turno2 = null;
	protected IPessoa _pessoa1 = null;
	protected ITurmaTurno _turmaTurno1 = null;
	protected ITurmaTurnoPersistente _turmaTurnoPersistente = null;

	/**
	 * Constructor for ClassShiftManagerDispatchActionTest.
	 * @param arg0
	 */
	public ClassShiftManagerDispatchActionTest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(ClassShiftManagerDispatchActionTest.class);
		return suite;
	}

	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");

		startPersistentLayer();
		cleanData();

		_pessoa1 = new Pessoa();
		_pessoa1.setNumeroDocumentoIdentificacao("0123456789");
		_pessoa1.setCodigoFiscal("9876543210");
		_pessoa1.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		_pessoa1.setUsername("nome");
		_pessoa1.setPassword("pass");
		HashSet privilegios = new HashSet();
		privilegios.add(new Privilegio(_pessoa1, new String("LerTurmas")));
		privilegios.add(new Privilegio(_pessoa1, new String("CriarTurma")));
		privilegios.add(new Privilegio(_pessoa1, new String("EditarTurma")));
		privilegios.add(new Privilegio(_pessoa1, new String("ApagarTurma")));
		_pessoa1.setPrivilegios(privilegios);
		_suportePersistente.iniciarTransaccao();
		_pessoaPersistente.escreverPessoa(_pessoa1);
		_suportePersistente.confirmarTransaccao();

		curso1 =
			new Curso(
				"LEIC",
				"Curso de Engenharia Informatica e de Computadores",
				new TipoCurso(TipoCurso.LICENCIATURA));
		_suportePersistente.iniciarTransaccao();
		_cursoPersistente.lockWrite(curso1);
		_suportePersistente.confirmarTransaccao();
		curso2 =
			new Curso(
				"LEEC",
				"Curso de Engenharia Electrotecnica e de Computadores",
				new TipoCurso(TipoCurso.LICENCIATURA));
		_suportePersistente.iniciarTransaccao();
		_cursoPersistente.lockWrite(curso1);
		_suportePersistente.confirmarTransaccao();
		_cursoExecucao1 = new CursoExecucao("2002/03", curso1);
		_suportePersistente.iniciarTransaccao();
		_cursoExecucaoPersistente.lockWrite(_cursoExecucao1);
		_suportePersistente.confirmarTransaccao();
		_cursoExecucao2 = new CursoExecucao("2002/03", curso2);
		_suportePersistente.iniciarTransaccao();
		_cursoExecucaoPersistente.lockWrite(_cursoExecucao1);
		_suportePersistente.confirmarTransaccao();

		_disciplinaExecucao1 =
			new DisciplinaExecucao(
				"disciplinaExecucao1",
				"DE1",
				"programa1",
				_cursoExecucao1,
				new Double(2),
				new Double(2),
				new Double(2),
				new Double(2));

		_suportePersistente.iniciarTransaccao();
		_disciplinaExecucaoPersistente.escreverDisciplinaExecucao(
			_disciplinaExecucao1);
		_suportePersistente.confirmarTransaccao();

		_suportePersistente.iniciarTransaccao();
		_cursoExecucaoPersistente.lockWrite(_cursoExecucao1);
		_suportePersistente.confirmarTransaccao();
		_turma1 = new Turma("10501", new Integer(1), new Integer(1), curso1);
		_suportePersistente.iniciarTransaccao();
		_turmaPersistente.lockWrite(_turma1);
		_suportePersistente.confirmarTransaccao();
		_turma2 = new Turma("14501", new Integer(1), new Integer(1), curso2);
		_suportePersistente.iniciarTransaccao();
		_turmaPersistente.lockWrite(_turma2);
		_suportePersistente.confirmarTransaccao();

		_turno1 =
			new Turno(
				"turno1",
				new TipoAula(new Integer(1)),
				new Integer(100),
				_disciplinaExecucao1);

		_suportePersistente.iniciarTransaccao();
		_turnoPersistente.lockWrite(_turno1);
		_suportePersistente.confirmarTransaccao();

		_turno2 =
			new Turno(
				"turno2",
				new TipoAula(new Integer(1)),
				new Integer(100),
				_disciplinaExecucao1);

		_suportePersistente.iniciarTransaccao();
		_turnoPersistente.lockWrite(_turno2);
		_suportePersistente.confirmarTransaccao();

		_turmaTurno1 = new TurmaTurno(_turma1, _turno1);
		_suportePersistente.iniciarTransaccao();
		_turmaTurnoPersistente.lockWrite(_turmaTurno1);
		_suportePersistente.confirmarTransaccao();

	}

	protected void startPersistentLayer() {
		try {
			_suportePersistente = SuportePersistenteOJB.getInstance();
			_cursoExecucaoPersistente =
				_suportePersistente.getICursoExecucaoPersistente();
			_cursoPersistente = _suportePersistente.getICursoPersistente();
			_disciplinaExecucaoPersistente =
				_suportePersistente.getIDisciplinaExecucaoPersistente();
			_turnoPersistente = _suportePersistente.getITurnoPersistente();
			_pessoaPersistente = _suportePersistente.getIPessoaPersistente();
			_turmaPersistente = _suportePersistente.getITurmaPersistente();
			_turmaTurnoPersistente =
				_suportePersistente.getITurmaTurnoPersistente();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when opening database");
		}

	}

	protected void cleanData() {
		try {
			_suportePersistente.iniciarTransaccao();
			_cursoExecucaoPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
			_suportePersistente.iniciarTransaccao();
			_cursoPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
			_suportePersistente.iniciarTransaccao();
			_pessoaPersistente.apagarTodasAsPessoas();
			_suportePersistente.confirmarTransaccao();
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
			_suportePersistente.iniciarTransaccao();
			_disciplinaExecucaoPersistente.apagarTodasAsDisciplinasExecucao();
			_suportePersistente.confirmarTransaccao();
			_suportePersistente.iniciarTransaccao();
			_turnoPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
			_suportePersistente.iniciarTransaccao();
			_turmaTurnoPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();

		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when cleaning data");
		}
	}

	public void testUnAuthorizedAddClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "addClassShift");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//fills the form
		addRequestParameter("shiftName", _turno1.getNome());

		//coloca a class_view na sessão

		InfoDegree infoDegree =
			new InfoDegree(
				_turma1.getLicenciatura().getSigla(),
				_turma1.getLicenciatura().getNome());
		InfoClass classView =
			new InfoClass(
				_turma1.getNome(),
				_turma1.getSemestre(),
				_turma1.getAnoCurricular(),
				infoDegree);
		getSession().setAttribute(SessionConstants.CLASS_VIEW, classView);

		//action perform
		actionPerform();

		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedAddClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "addClassShift");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("AdicionarTurno");
		privilegios.add("LerTurnosDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//fills the form
		addRequestParameter("shiftName", _turno2.getNome());

		//coloca a class_view na sessão

		InfoDegree infoDegree =
			new InfoDegree(
				_turma1.getLicenciatura().getSigla(),
				_turma1.getLicenciatura().getNome());
		InfoClass classView =
			new InfoClass(
				_turma1.getNome(),
				_turma1.getSemestre(),
				_turma1.getAnoCurricular(),
				infoDegree);
		getSession().setAttribute(SessionConstants.CLASS_VIEW, classView);

		//action perform
		actionPerform();

		//verify Forward
		verifyForward("viewClassShiftList");

		//verify that there are errors
		verifyNoActionErrors();

		//verifies correctness of session/request
		assertNotNull(
			getRequest().getAttribute(
				ClassShiftManagerDispatchAction.SHIFT_LIST_ATT));
		assertNotNull(getSession().getAttribute(SessionConstants.CLASS_VIEW));

	}

	public void testUnAuthorizedRemoveClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "removeClassShift");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//fills the form
		addRequestParameter("shiftName", _turno1.getNome());

		//coloca a class_view na sessão

		InfoDegree infoDegree =
			new InfoDegree(
				_turma1.getLicenciatura().getSigla(),
				_turma1.getLicenciatura().getNome());
		InfoClass classView =
			new InfoClass(
				_turma1.getNome(),
				_turma1.getSemestre(),
				_turma1.getAnoCurricular(),
				infoDegree);
		getSession().setAttribute(SessionConstants.CLASS_VIEW, classView);

		//action perform
		actionPerform();

		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedRemoveClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "removeClassShift");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("RemoverTurno");
		privilegios.add("LerTurnosDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//fills the form
		addRequestParameter("shiftName", _turno1.getNome());

		//coloca a class_view na sessão

		InfoDegree infoDegree =
			new InfoDegree(
				_turma1.getLicenciatura().getSigla(),
				_turma1.getLicenciatura().getNome());
		InfoClass classView =
			new InfoClass(
				_turma1.getNome(),
				_turma1.getSemestre(),
				_turma1.getAnoCurricular(),
				infoDegree);
		getSession().setAttribute(SessionConstants.CLASS_VIEW, classView);

		//action perform
		actionPerform();

		//verify Forward
		verifyForward("viewClassShiftList");

		//verify that there are errors
		verifyNoActionErrors();

		//verifies correctness of session/request

		assertNotNull(getSession().getAttribute(SessionConstants.CLASS_VIEW));

	}

	public void testUnAuthorizedViewClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "viewClassShiftList");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//coloca a class_view na sessão

		InfoDegree infoDegree =
			new InfoDegree(
				_turma1.getLicenciatura().getSigla(),
				_turma1.getLicenciatura().getNome());
		InfoClass classView =
			new InfoClass(
				_turma1.getNome(),
				_turma1.getSemestre(),
				_turma1.getAnoCurricular(),
				infoDegree);
		getSession().setAttribute(SessionConstants.CLASS_VIEW, classView);

		//action perform
		actionPerform();

		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedViewClassShift() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "viewClassShiftList");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("RemoverTurno");
		privilegios.add("LerTurnosDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//coloca a class_view na sessão

		InfoDegree infoDegree =
			new InfoDegree(
				_turma1.getLicenciatura().getSigla(),
				_turma1.getLicenciatura().getNome());
		InfoClass classView =
			new InfoClass(
				_turma1.getNome(),
				_turma1.getSemestre(),
				_turma1.getAnoCurricular(),
				infoDegree);
		getSession().setAttribute(SessionConstants.CLASS_VIEW, classView);

		//action perform
		actionPerform();

		//verify Forward
		verifyForward("viewClassShiftList");

		//verify that there are errors
		verifyNoActionErrors();

		//verifies correctness of session/request
		assertNotNull(
			getRequest().getAttribute(
				ClassShiftManagerDispatchAction.SHIFT_LIST_ATT));
		assertNotNull(getSession().getAttribute(SessionConstants.CLASS_VIEW));

	}

	public void testUnAuthorizedListAvailableShifts() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "listAvailableShifts");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//coloca a class_view na sessão

		InfoDegree infoDegree =
			new InfoDegree(
				_turma1.getLicenciatura().getSigla(),
				_turma1.getLicenciatura().getNome());
		InfoClass classView =
			new InfoClass(
				_turma1.getNome(),
				_turma1.getSemestre(),
				_turma1.getAnoCurricular(),
				infoDegree);
		getSession().setAttribute(SessionConstants.CLASS_VIEW, classView);

		//action perform
		actionPerform();

		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedListAvailableShifts() {
		//set request path
		setRequestPathInfo("sop", "/ClassShiftManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "listAvailableShifts");

		//coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("LerTurnosDeDisciplinaExecucao");
		privilegios.add("LerTurnosDeTurma");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);

		//coloca a class_view na sessão

		InfoDegree infoDegree =
			new InfoDegree(
				_turma1.getLicenciatura().getSigla(),
				_turma1.getLicenciatura().getNome());
		InfoClass classView =
			new InfoClass(
				_turma1.getNome(),
				_turma1.getSemestre(),
				_turma1.getAnoCurricular(),
				infoDegree);
		getSession().setAttribute(SessionConstants.CLASS_VIEW, classView);

		//coloca a Execution_course_key em sessão
		InfoExecutionCourse exeCourse =
			new InfoExecutionCourse(
				_disciplinaExecucao1.getNome(),
				_disciplinaExecucao1.getSigla(),
				_disciplinaExecucao1.getPrograma(),
				new InfoExecutionDegree(
					_disciplinaExecucao1
						.getLicenciaturaExecucao()
						.getAnoLectivo(),
					new InfoDegree(
						_disciplinaExecucao1
							.getLicenciaturaExecucao()
							.getCurso()
							.getSigla(),
						_disciplinaExecucao1
							.getLicenciaturaExecucao()
							.getCurso()
							.getNome())),
				_disciplinaExecucao1.getTheoreticalHours(),
				_disciplinaExecucao1.getPraticalHours(),
				_disciplinaExecucao1.getTheoPratHours(),
				_disciplinaExecucao1.getLabHours());
		getSession().setAttribute(
			SessionConstants.EXECUTION_COURSE_KEY,
			exeCourse);

		//action perform
		actionPerform();

		//verify Forward
		verifyForward("viewClassShiftList");

		//verify that there are errors
		verifyNoActionErrors();

		//verifies correctness of session/request
		assertNotNull(
			getRequest().getAttribute(
				ClassShiftManagerDispatchAction.SHIFT_LIST_ATT));
		assertNotNull(
			getRequest().getAttribute(
				ClassShiftManagerDispatchAction.AVAILABLE_LIST));

	}
}
