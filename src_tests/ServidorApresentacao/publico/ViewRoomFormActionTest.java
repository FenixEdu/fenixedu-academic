package ServidorApresentacao.publico;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoRoom;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 *
 */
public class ViewRoomFormActionTest extends TestCasePresentation {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ViewRoomFormActionTest.class);

		return suite;
	}

	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuração Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-publico.xml");

	}

	public ViewRoomFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulViewRoom() {
		// define mapping de origem
		setRequestPathInfo("publico", "/viewRoom");

		// Fill in Form
		addRequestParameter("index", "0");

		// Place list of rooms in session.
		try {
			GestorServicos gestor = GestorServicos.manager();
			Object args[] = { new InfoRoom()};
			ArrayList infoRooms =
				(ArrayList) gestor.executar(null, "SelectRooms", args);
			getSession().setAttribute("publico.infoRooms", infoRooms);
		} catch (Exception e) {
			System.out.println("Erro na invocacao do servico " + e);
		}

		//puts execution period in session
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			infoExecutionPeriod);

		// invoca acção
		actionPerform();

		// verifica reencaminhamento
		verifyForward("Sucess");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}

	//  public void testUnsuccessfulSelectRooms() {      
	//	// define mapping de origem
	//	setRequestPathInfo("publico", "/viewRoom");
	//
	//	// Fill in Form
	//	//addRequestParameter("index","0");
	//
	//	// Place list of rooms in session.
	//	getSession().setAttribute("publico.infoRooms", _infoRooms);
	//
	//	// invoca acção
	//	actionPerform();
	//
	//	// verifica reencaminhamento
	//	verifyForwardPath("/viewRoom.jsp");
	//
	//	//verifica ausencia de erros
	//	verifyNoActionErrors();
	//  }

}