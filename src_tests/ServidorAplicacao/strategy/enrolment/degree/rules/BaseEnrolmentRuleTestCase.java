/*
 * Created on 8/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree.rules;

import junit.framework.TestCase;
import Dominio.ICurricularCourse;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.depercated.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.dbaccess;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author jpvl
 */
abstract public class BaseEnrolmentRuleTestCase extends TestCase {
	private dbaccess dbAcessPoint;
	protected ISuportePersistente sp;

	protected IUserView userView = new UserView("user", null);

	
	protected void setUp() {
		try {
			dbAcessPoint = new dbaccess();
			dbAcessPoint.openConnection();
			dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
			dbAcessPoint.loadDataBase(getDataSetFilePath());
			dbAcessPoint.closeConnection();
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			fail("Setup failed loading database with test data set: " + ex);
		}
		
		try {
			sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Getting persistent layer!");
		}
	}

	/**
	 * @return
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetForRestrictionsTests.xml";
	}
	
	public EnrolmentContext getEnrolmentContext(Integer studentNumber, TipoCurso degreeType){
		EnrolmentContext enrolmentContext = null;
		
		IStudentCurricularPlanPersistente studentCurricularPlanDAO = sp.getIStudentCurricularPlanPersistente();
		
		try {
			sp.iniciarTransaccao();
			IStudentCurricularPlan studentCurricularPlan = studentCurricularPlanDAO.readActiveStudentCurricularPlan(studentNumber, degreeType);
			assertNotNull(studentCurricularPlan);
			
			enrolmentContext = EnrolmentContextManager.initialEnrolmentContext(studentCurricularPlan.getStudent());

			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Getting enrolment context!");
		} catch (OutOfCurricularCourseEnrolmentPeriod e) {
			e.printStackTrace();
			fail("No valid enrolment period!");
		}	
		return enrolmentContext;
	}
	
	public ICurricularCourse getCurricularCourse (String name, String code){
		ICurricularCourse curricularCourse = null;
		IPersistentCurricularCourse curricularCourseDAO = sp.getIPersistentCurricularCourse();
		try {
			sp.iniciarTransaccao();
			curricularCourse = curricularCourseDAO.readCurricularCourseByNameAndCode(name, code);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Reading curricular course! (name:"+name+";code:"+code);
		}
		assertNotNull("Reading curricular course (name:"+name+";code:"+code+"!",curricularCourse);
		return curricularCourse;
	}

	
	protected void autentication() {
		String argsAutenticacao[] = { "user", "pass" , Autenticacao.EXTRANET};
		try {
			userView = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsAutenticacao);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected EnrolmentContext executeService(String serviceName, Object[] serviceArgs) {
		try {
			Object result = null;
			result = ServiceManagerServiceFactory.executeService(userView, serviceName, serviceArgs);
			return EnrolmentContextManager.getEnrolmentContext((InfoEnrolmentContext) result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	
}
