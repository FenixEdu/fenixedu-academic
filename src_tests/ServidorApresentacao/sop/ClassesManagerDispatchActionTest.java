/**
 * 
 * Project sop 
 * Package ServidorApresentacao.sop 
 * Created on 15/Jan/2003
 */
package ServidorApresentacao.sop;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.ExecutionYear;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
import Dominio.IPlanoCurricularCurso;
import Dominio.ITurma;
import Dominio.Pessoa;
import Dominio.PlanoCurricularCurso;
import Dominio.Privilegio;
import Dominio.Turma;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

/**
 * @author João Mota
 *
 */
public class ClassesManagerDispatchActionTest extends MockStrutsTestCase {
	protected ISuportePersistente _suportePersistente = null;
	protected ICursoPersistente _cursoPersistente = null;
	protected ICursoExecucaoPersistente _cursoExecucaoPersistente = null;
	protected ITurmaPersistente _turmaPersistente = null;
	protected IPessoaPersistente _pessoaPersistente = null;
	protected IPersistentExecutionYear persistentExecutionYear = null;
	protected IPlanoCurricularCursoPersistente persistentCurricularPlan = null;
	protected ICurso curso1 = null;
	protected ICurso curso2 = null;
	protected ICursoExecucao _cursoExecucao1 = null;
	protected ICursoExecucao _cursoExecucao2 = null;
	protected ITurma _turma1 = null;
	protected ITurma _turma2 = null;
	protected IPessoa _pessoa1 = null;
	/**
	 * Constructor for ClassesManagerDispatchAction.
	 * @param arg0
	 */
	public ClassesManagerDispatchActionTest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ClassesManagerDispatchActionTest.class);
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

		IExecutionYear executionYear = new ExecutionYear("2002/03");
		_suportePersistente.iniciarTransaccao();
		persistentExecutionYear.lockWrite(executionYear);
		_suportePersistente.confirmarTransaccao();
		IPlanoCurricularCurso curricularPlan =
			new PlanoCurricularCurso("plano1", curso1);
		IPlanoCurricularCurso curricularPlan2 =
			new PlanoCurricularCurso("plano2", curso2);
		_suportePersistente.iniciarTransaccao();
		persistentCurricularPlan.lockWrite(curricularPlan);
		_suportePersistente.confirmarTransaccao();
		_suportePersistente.iniciarTransaccao();
		persistentCurricularPlan.lockWrite(curricularPlan2);
		_suportePersistente.confirmarTransaccao();
		_suportePersistente.iniciarTransaccao();
		_cursoPersistente.lockWrite(curso1);
		_suportePersistente.confirmarTransaccao();
		_cursoExecucao1 =
			new CursoExecucao("2002/03", curso1, executionYear, curricularPlan);
		_suportePersistente.iniciarTransaccao();
		_cursoExecucaoPersistente.lockWrite(_cursoExecucao1);
		_suportePersistente.confirmarTransaccao();
		_cursoExecucao2 =
			new CursoExecucao(
				"2002/03",
				curso2,
				executionYear,
				curricularPlan2);
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

	}

	public void testUnAuthorizedListClasses() {
		//set request path
		setRequestPathInfo("sop", "/ClassesManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "listClasses");
		//falta  contexto
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

		// Coloca contexto em sessão
		InfoDegree iL = new InfoDegree("LEIC", "Informatica");
		InfoDegreeCurricularPlan infoPlan =
			new InfoDegreeCurricularPlan("plano1", iL);
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(infoPlan, new InfoExecutionYear("2002/03"));
		CurricularYearAndSemesterAndInfoExecutionDegree aCSiLE =
			new CurricularYearAndSemesterAndInfoExecutionDegree(
				new Integer(5),
				new Integer(1),
				iLE);
		getSession().setAttribute(SessionConstants.CONTEXT_KEY, aCSiLE);

		//action perform
		actionPerform();
		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedListClasses() {
		//set request path
		setRequestPathInfo("sop", "/ClassesManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "listClasses");
		//falta  contexto
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();

		privilegios.add("LerTurmas");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

		// Coloca contexto em sessão
		InfoDegree iL = new InfoDegree("LEIC", "Informatica");
		InfoDegreeCurricularPlan infoPlan =
			new InfoDegreeCurricularPlan("plano1", iL);
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(infoPlan, new InfoExecutionYear("2002/03"));
		CurricularYearAndSemesterAndInfoExecutionDegree aCSiLE =
			new CurricularYearAndSemesterAndInfoExecutionDegree(
				new Integer(5),
				new Integer(1),
				iLE);
		getSession().setAttribute(SessionConstants.CONTEXT_KEY, aCSiLE);

		//action perform
		actionPerform();
		//verify that there are no errors
		verifyNoActionErrors();
		//verify forward
		verifyForward("listClasses");
		//verify that all that should be in session is
		GestorServicos gestor = GestorServicos.manager();
		Object argsLerTurmas[] = { aCSiLE };

		try {
			List classesList =
				(List) gestor.executar(
					SessionUtils.getUserView(getRequest()),
					"LerTurmas",
					argsLerTurmas);
			if (classesList != null && !classesList.isEmpty()) {
				assertEquals(
					classesList,
					getRequest().getAttribute("CLASS_LIST_KEY"));
			}
		} catch (Exception e) {
		}

	}

	/**
	 * Method verifySessionAttributes.
	 * @param httpSession
	 * @param attributesList
	 */
	private void verifySessionAttributes(
		HttpSession session,
		List existingAttributesList,
		List nonExistingAttributesList) {
		Enumeration attNames = session.getAttributeNames();

		verifySessionAttributes(session, existingAttributesList, true);
		verifySessionAttributes(session, nonExistingAttributesList, false);
	}
	/**
	 * Method verifySessionAttributes.
	 * @param session
	 * @param existingAttributesList
	 * @param exists
	 */
	private void verifySessionAttributes(
		HttpSession session,
		List list,
		boolean exists) {
		if (list != null) {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				String attName = (String) iterator.next();
				if (!((session.getAttribute(attName) != null) == exists)) {
					String message = " Session contains attribute ";
					if (exists)
						message = "Session doesn't contains attribute ";
					fail(message + attName + ".");
				}
			}
		}

	}

	protected void startPersistentLayer() {
		try {
			_suportePersistente = SuportePersistenteOJB.getInstance();
			_cursoExecucaoPersistente =
				_suportePersistente.getICursoExecucaoPersistente();
			_cursoPersistente = _suportePersistente.getICursoPersistente();
			_pessoaPersistente = _suportePersistente.getIPessoaPersistente();
			_turmaPersistente = _suportePersistente.getITurmaPersistente();
			persistentExecutionYear =
				_suportePersistente.getIPersistentExecutionYear();
			persistentCurricularPlan =
				_suportePersistente.getIPlanoCurricularCursoPersistente();
				System.out.println("*******correu tudo bem no startPersistentLayer");
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
			persistentCurricularPlan.apagarTodosOsPlanosCurriculares();
			_suportePersistente.confirmarTransaccao();
			_suportePersistente.iniciarTransaccao();
			persistentExecutionYear.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when cleaning data");
		}
	}

}
