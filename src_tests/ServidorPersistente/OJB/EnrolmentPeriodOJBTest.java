/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package ServidorPersistente.OJB;



import java.util.Date;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import Dominio.Curso;
import Dominio.EnrolmentPeriod;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolmentPeriod;
import Dominio.IExecutionPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class EnrolmentPeriodOJBTest extends TestCaseOJB {

	public void testReadActualExecutionPeriodForDegreeCurricularPlan() {

		IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = sp.getIPersistentDegreeCurricularPlan();
		IDegreeCurricularPlan degreeCurricularPlan = null;
		ICurso degree = new Curso();
		degree.setSigla("LEIC");
		degree.setNome("Licenciatura em Engenharia Informática e de Computadores");
		degree.setTipoCurso(new TipoCurso(TipoCurso.LICENCIATURA));
		
		try {
			sp.iniciarTransaccao();
			degreeCurricularPlan = degreeCurricularPlanDAO.readByNameAndDegree("plano 1", degree);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e4) {
			e4.printStackTrace();
			fail ("Reading degreeCurricularPlan!");
		}
		assertNotNull("Degree curricular plan must be not null!", degreeCurricularPlan);
		// Reading null
		IEnrolmentPeriod enrolmentPeriod = null;
		IPersistentEnrolmentPeriod enrolmentPeriodDAO =
			sp.getIPersistentEnrolmentPeriod();
		try {
			sp.iniciarTransaccao();
			enrolmentPeriod =
				enrolmentPeriodDAO
					.readActualEnrolmentPeriodForDegreeCurricularPlan(
					degreeCurricularPlan);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Getting enrolment period!");
		}
		assertNull(enrolmentPeriod);

		//Reading actual
		IPersistentExecutionPeriod executionPeriodDAO =
			sp.getIPersistentExecutionPeriod();

		IExecutionPeriod executionPeriod = null;
		try {
			sp.iniciarTransaccao();
			executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Reading an expected null enrolmentPeriod!");
		}
		assertNull("Enrolment period was expected to be null!", enrolmentPeriod);
		
		enrolmentPeriod = new EnrolmentPeriod();

		enrolmentPeriod.setDegreeCurricularPlan(degreeCurricularPlan);
		enrolmentPeriod.setExecutionPeriod(executionPeriod);
		
		Date startDate = new Date();
		Date endDate = new Date(startDate.getTime() + 24*60*60*1000);
		
		enrolmentPeriod.setStartDate(startDate);
		enrolmentPeriod.setEndDate (endDate);
		
		try {
			sp.iniciarTransaccao();
			enrolmentPeriodDAO.lockWrite(enrolmentPeriod);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e2) {
			e2.printStackTrace();
			fail ("saving enrolment periond!");
		}
		// clear cache
		PersistenceBroker pb = PersistenceBrokerFactory.defaultPersistenceBroker();
		pb.clearCache();
		
		try {
			sp.iniciarTransaccao();
			enrolmentPeriod = enrolmentPeriodDAO.readActualEnrolmentPeriodForDegreeCurricularPlan(degreeCurricularPlan);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e3) {
			e3.printStackTrace();
			fail("reading enrolment period!");
		}
		assertNotNull("Enrolment period must be not null!",enrolmentPeriod);
	}

	/**
	 * @param testName
	 */
	public EnrolmentPeriodOJBTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.OJB.TestCaseOJB#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetEnrolmentPeriodOJBTest.xml";
	}

}
