/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.CollectionUtils;

import Dominio.CurricularCourseDoneRestriction;
import Dominio.ICurricularCourse;
import Dominio.IPrecedence;
import Dominio.IRestriction;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.NumberOfCurricularCourseDoneRestriction;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContextManager;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.IPersistentRestriction;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class RestrictionOJBTest extends BaseEnrolmentRestrictionOJBTest {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(RestrictionOJBTest.class);
        
		return suite;
	}

	/**
	 * @param testName
	 */
	public RestrictionOJBTest(String testName) {
		super(testName);
	}
	
	public void testReadByCurricularCourse(){
		List precedenceList = null;
		
		IPersistentPrecedence precedenceDAO = sp.getIPersistentPrecedence();
		IPersistentCurricularCourse curricularCourseDAO = sp.getIPersistentCurricularCourse();
		ICurricularCourse curricularCourse = null;
		
		IPersistentRestriction restrictionDAO = sp.getIPersistentRestriction();
		
		try {
			sp.iniciarTransaccao();
			curricularCourse = curricularCourseDAO.readCurricularCourseByNameAndCode("Analise Matematica II","AMII");
			assertNotNull(curricularCourse);
			precedenceList = precedenceDAO.readByCurricularCourse(curricularCourse);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Getting precedenceList!");
		}
		assertNotNull(precedenceList);
		List restrictionList = null;		
		
		try {
			sp.iniciarTransaccao();
			restrictionList = restrictionDAO.readByCurricularCourse(curricularCourse);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace(System.out);
			fail("Reading restriction list!");			
		}
		
		assertNotNull(restrictionList);
		
		List expectedRestrictionList = new ArrayList();
		
		for (int i = 0; i < precedenceList.size(); i++){
			IPrecedence precedence = (IPrecedence) precedenceList.get(0);
			expectedRestrictionList.addAll(precedence.getRestrictions());	
		}
		
		assertEquals(expectedRestrictionList.size(), restrictionList.size());
		
		Collection subtractList = CollectionUtils.subtract(expectedRestrictionList, restrictionList);
		assertEquals(0, subtractList.size());
	}
	
	public void testEvaluateCurricularCourseDoneRestriction(){
		IPersistentPrecedence precedenceDAO = sp.getIPersistentPrecedence();
		IPersistentCurricularCourse curricularCourseDAO = sp.getIPersistentCurricularCourse();
		ICurricularCourse curricularCourse = null;
		try {
			sp.iniciarTransaccao();
			curricularCourse = curricularCourseDAO.readCurricularCourseByNameAndCode("Analise Matematica II","AMII");
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Reading curricularCourse!");
		}
		List precedenceList = null;
		try {
			sp.iniciarTransaccao();
			precedenceList = precedenceDAO.readByCurricularCourse(curricularCourse);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace(System.out);
			fail("Reading precedenceList!");
		}
		
		assertNotNull(precedenceList);
		assertEquals(1, precedenceList.size());
		IPrecedence precedence = (IPrecedence) precedenceList.get(0);
		
		assertEquals(1, precedence.getRestrictions().size());
		
		IRestriction restriction = (IRestriction) precedence.getRestrictions().get(0);
		assertEquals(CurricularCourseDoneRestriction.class, restriction.getClass());
		assertEquals("Evaluating AMII precedence!",true,restriction.evaluate(getEnrolmentContext()));
	}

	public void testEvaluateNumberOfCurricularCourseDoneRestriction(){
		IPersistentPrecedence precedenceDAO = sp.getIPersistentPrecedence();
		IPersistentCurricularCourse curricularCourseDAO = sp.getIPersistentCurricularCourse();
		ICurricularCourse curricularCourse = null;
		try {
			sp.iniciarTransaccao();
			curricularCourse = curricularCourseDAO.readCurricularCourseByNameAndCode("Analise Matematica I","AMI");
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Reading curricularCourse!");
		}
		List precedenceList = null;
		try {
			sp.iniciarTransaccao();
			precedenceList = precedenceDAO.readByCurricularCourse(curricularCourse);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace(System.out);
			fail("Reading precedenceList!");
		}
		
		assertNotNull(precedenceList);
		assertEquals(1, precedenceList.size());
		IPrecedence precedence = (IPrecedence) precedenceList.get(0);
		
		assertEquals(1, precedence.getRestrictions().size());
		
		IRestriction restriction = (IRestriction) precedence.getRestrictions().get(0);
		assertEquals(NumberOfCurricularCourseDoneRestriction.class, restriction.getClass());
		assertEquals(false,restriction.evaluate(getEnrolmentContext()));
	}
	
	public EnrolmentContext getEnrolmentContext(){
		EnrolmentContext enrolmentContext = null;
		
		IStudentCurricularPlanPersistente studentCurricularPlanDAO = sp.getIStudentCurricularPlanPersistente();
		
		
		IStudent student = null;

		try {
			sp.iniciarTransaccao();
			IStudentCurricularPlan studentCurricularPlan = studentCurricularPlanDAO.readActiveStudentCurricularPlan(new Integer(1), new TipoCurso(TipoCurso.LICENCIATURA));

			assertNotNull(studentCurricularPlan);

			student = studentCurricularPlan.getStudent();
			assertNotNull(student);

			enrolmentContext = EnrolmentContextManager.initialEnrolmentContext(student);

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
}
