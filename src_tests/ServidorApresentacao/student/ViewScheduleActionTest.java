package ServidorApresentacao.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.broker.PersistenceBrokerFactory;

import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoRole;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.dbaccess;
import Util.RoleType;
import Util.TipoCurso;


/**
 * @author tfc130
 *
 */
public class ViewScheduleActionTest extends MockStrutsTestCase {

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ViewScheduleActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configuração a utilizar
    setServletConfigFile("/WEB-INF/web.xml");
	dbaccess dbAccess = new dbaccess();
		
	dbAccess.openConnection();
	dbAccess.loadDataBase("etc/testDataSetForStudent.xml");
	dbAccess.closeConnection();
	PersistenceBrokerFactory.defaultPersistenceBroker().clearCache();
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public ViewScheduleActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulViewEnrolment() {      
    // define mapping de origem
    setRequestPathInfo("/student", "/viewSchedule");

    // coloca credenciais na sessão
	Collection roles = new ArrayList();

	InfoRole infoRole = new InfoRole();
	infoRole.setRoleType(RoleType.STUDENT);
	roles.add(infoRole);
	
	IUserView userView = new UserView("pessoa3", roles);
	
    // colocar outras informações na sessão
	getSession().setAttribute("infoStudent", getInfoStudent(new Integer(3)));
	getSession().setAttribute(SessionConstants.U_VIEW, userView);
    
    // invoca acção
    actionPerform();

    // verifica reencaminhamento
    verifyForward("sucess");

    //verifica ausencia de erros
    verifyNoActionErrors();

    //verifica UserView guardado na sessão
    List lessons = (List) getSession().getAttribute(SessionConstants.LESSON_LIST_ATT);
	assertNotNull("Student lessons not present", lessons);
	assertEquals("Student lessons not present", 2, lessons.size());
  }
  
  private InfoStudent getInfoStudent(Integer number) {
	  InfoStudent infoStudent = null;

	  IStudent student = null;

	  try {
		  ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		  IPersistentStudent studentDAO = sp.getIPersistentStudent();
		  sp.iniciarTransaccao();
		  student =
			  studentDAO.readByNumero(
				  number,
				  new TipoCurso(TipoCurso.LICENCIATURA));
		  sp.confirmarTransaccao();
	  } catch (ExcepcaoPersistencia e) {
		  e.printStackTrace(System.out);
		  fail("Reading student number " + number.intValue());
	  }
	  assertNotNull("Student number "+number,student);
	  infoStudent = Cloner.copyIStudent2InfoStudent(student);
	  return infoStudent;
  }
}