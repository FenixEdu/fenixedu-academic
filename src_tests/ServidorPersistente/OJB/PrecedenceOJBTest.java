/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;

import Dominio.CurricularCourse;
import Dominio.CurricularCourseDoneRestriction;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseDoneRestriction;
import Dominio.IPrecedence;
import Dominio.IStudentCurricularPlan;
import Dominio.Precedence;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import Util.PrecedenceScopeToApply;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class PrecedenceOJBTest extends TestCaseOJB {
	private IStudentCurricularPlan studentCurricularPlan;
	
	private IPersistentPrecedence precedenceDAO;
	/**
	 * @param testName
	 */
	public PrecedenceOJBTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.OJB.TestCaseOJB#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testEnrolmentDataSet.xml";
	}

	public void testReadByDegreeCurricularPlan() {
		List precedenceList = null;

		try {
			sp.iniciarTransaccao();
			precedenceList = precedenceDAO.readByDegreeCurricularPlan(studentCurricularPlan.getDegreeCurricularPlan(), PrecedenceScopeToApply.TO_APPLY_TO_SPAN);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Accessing DAO");
		}
		assertNotNull("Precedence List is null!", precedenceList);
		
		assertEquals("Precedence list size:",3, precedenceList.size());
	}

	public void testReadByCurricularCourse() {
		List precedenceList = null;
		ICurricularCourse curricularCourse = null;
		IPersistentCurricularCourse curricularCourseDAO = sp.getIPersistentCurricularCourse();
		try {
			sp.iniciarTransaccao();
			curricularCourse = curricularCourseDAO.readCurricularCourseByNameAndCode("Analise Matematica II","AMII");
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail ("reading curricular course!");
		}
		assertNotNull("Curricular course is null!",curricularCourse);		
		
		try {
			sp.iniciarTransaccao();
			precedenceList = precedenceDAO.readByCurricularCourse(curricularCourse, PrecedenceScopeToApply.TO_APPLY_TO_SPAN);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Accessing precedenceDAO");
		}
		assertNotNull("Precedence List is null!", precedenceList);
		
		assertEquals("precedence list size:",1, precedenceList.size());
		IPrecedence precedence = (IPrecedence) precedenceList.get(0);
		assertEquals("restrictions list size",1, precedence.getRestrictions().size());
		
		ICurricularCourse curricularCoursePrecedent = new CurricularCourse();
		curricularCoursePrecedent.setCode("AMI");
		curricularCoursePrecedent.setName("Analise Matematica I");
		curricularCoursePrecedent.setDegreeCurricularPlan(studentCurricularPlan.getDegreeCurricularPlan());
		
		IPrecedence expectedPrecedence = new Precedence();
		expectedPrecedence.setCurricularCourse(curricularCourse);
		
		
		ICurricularCourseDoneRestriction curricularCourseDoneRestriction = new CurricularCourseDoneRestriction();
		curricularCourseDoneRestriction.setPrecedentCurricularCourse(curricularCoursePrecedent);
		curricularCourseDoneRestriction.setPrecedence(expectedPrecedence);

		List expectedRestrictionList = new ArrayList();
		expectedRestrictionList.add(curricularCourseDoneRestriction);
		expectedPrecedence.setRestrictions(expectedRestrictionList);

		assertEquals("precedence:",expectedPrecedence, precedence);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() {
		super.setUp();
		precedenceDAO = sp.getIPersistentPrecedence();
		IStudentCurricularPlanPersistente studentCurricularPlanDAO = sp.getIStudentCurricularPlanPersistente();
		
		try {
			sp.iniciarTransaccao();
			studentCurricularPlan = studentCurricularPlanDAO.readActiveStudentCurricularPlan(new Integer(1), new TipoCurso(TipoCurso.LICENCIATURA));
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Reading active curricular plan of student number 1!");
		}
	}

}
