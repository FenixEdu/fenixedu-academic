package UtilInsc;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import framework.factory.ServiceManagerServiceFactory;

import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IEnrolmentPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentState;


/**
 * @author David Santos
 */

public class SetEnrolmentState {

	private static IUserView userView = null;
	private static ISuportePersistente persistentSupport =	null;
	private static boolean chooseExecutionPeriodsMode = false;

	public static void main(String[] args) {
		List enrolmentList = null;

		autentication();
//		userView = SetEnrolmentStateIO.interactiveAutentication();

		chooseExecutionPeriodsMode = SetEnrolmentStateIO.selectMode();

		turnOnPersistentSuport();

		enrolmentList = loadPersistentData();
		if( (enrolmentList != null) && (!enrolmentList.isEmpty()) ) {
			SetEnrolmentStateIO.showTemporarilyEnrolmentList(enrolmentList);
			SetEnrolmentStateIO.selectNewStatesForListOfTemporarilyEnrolments(enrolmentList, chooseExecutionPeriodsMode);
			writePersistentData(enrolmentList);
			if(!chooseExecutionPeriodsMode) {
				updateDataBaseForNewEnrolmentPeriod();
			}
		} else {
			System.out.println();
			System.out.println("THERE ARE NO ENROLMENTS IN THE SELECTED EXECUTION PERIOD!");
		}

		turnOffPersistentSuport();
	}

	private static void autentication() {
		String argsAutenticacao[] = {"l44406", "1", Autenticacao.EXTRANET};
		try {
			userView = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsAutenticacao);
		} catch (FenixServiceException e) {
			e.printStackTrace(System.out);
		}
	}

	private static void turnOnPersistentSuport() {
		try {
			persistentSupport =	SuportePersistenteOJB.getInstance();
			persistentSupport.iniciarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
		}
	}

	private static void turnOffPersistentSuport() {
		try {
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
		}
	}

	private static List loadPersistentData() {
		List enrolmentList = null;

		try {
//			IPersistentEnrolmentPeriod persistentEnrolmentPeriod = persistentSupport.getIPersistentEnrolmentPeriod();
			IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();
			IPersistentStudent persistentStudent =	persistentSupport.getIPersistentStudent();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
			
			IStudent student = persistentStudent.readByUsername(((UserView) userView).getUtilizador());
			IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
//			IEnrolmentPeriod enrolmentPeriod = getEnrolmentPeriod(persistentEnrolmentPeriod, studentActiveCurricularPlan);
			
//			IEnrolment enrolmentCriteria = new Enrolment();
//			enrolmentCriteria.setExecutionPeriod(enrolmentPeriod.getExecutionPeriod());
//			enrolmentCriteria.setState(EnrolmentState.TEMPORARILY_ENROLED_OJB);
//			enrolmentCriteria.setStudentCurricularPlan(studentActiveCurricularPlan);
//			
//			enrolmentList = persistentEnrolment.readByCriteria(enrolmentCriteria);
			
			if(chooseExecutionPeriodsMode) {
				enrolmentList = readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(persistentEnrolment, studentActiveCurricularPlan);
			} else {
				enrolmentList = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentActiveCurricularPlan, EnrolmentState.TEMPORARILY_ENROLED);
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
		}
		return enrolmentList;
	}

	private static void writePersistentData(List enrolmentList) {
		IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

		System.out.println();
		System.out.println("UPDATING ENROLMENT STATES FOR THE FOLLOWING CURRICULAR COURSES TO DATA BASE:");
		Iterator iterator = enrolmentList.iterator();
		while(iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if(enrolment instanceof IEnrolmentInOptionalCurricularCourse) {
				IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) enrolment;
				System.out.println("CURRICULAR COURSE: [" + enrolmentInOptionalCurricularCourse.getCurricularCourse().getName() + " -> " + enrolmentInOptionalCurricularCourse.getCurricularCourseForOption().getName() + "] STATE: [" + SetEnrolmentStateIO.showEnrolmentState(enrolmentInOptionalCurricularCourse.getEnrolmentState()) + "]");
			} else {
				System.out.println("CURRICULAR COURSE: [" + enrolment.getCurricularCourse().getName() + "] STATE: [" + SetEnrolmentStateIO.showEnrolmentState(enrolment.getEnrolmentState()) + "]");
			}
			try {
				persistentEnrolment.lockWrite(enrolment);
			} catch (ExistingPersistentException e) {
				e.printStackTrace(System.out);
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace(System.out);
			}
		}
		System.out.println("NEW ENROLMENT STATES WERE UPDATED SUCCESSFULY IN DATA BASE!");
	}

	private static void updateDataBaseForNewEnrolmentPeriod() {
		IPersistentEnrolmentPeriod persistentEnrolmentPeriod = persistentSupport.getIPersistentEnrolmentPeriod();
		IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
		IPersistentStudent persistentStudent =	persistentSupport.getIPersistentStudent();
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
			
		try {
			IStudent student = persistentStudent.readByUsername(((UserView) userView).getUtilizador());
			IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
			IEnrolmentPeriod enrolmentPeriod = getEnrolmentPeriod(persistentEnrolmentPeriod, studentActiveCurricularPlan);
			
			int x = 0;
			if(enrolmentPeriod.getExecutionPeriod().getIdInternal().intValue() < 100001){
				x = enrolmentPeriod.getExecutionPeriod().getIdInternal().intValue() + 100000;	
			}else{
				x = enrolmentPeriod.getExecutionPeriod().getIdInternal().intValue() + 1;
			}
			
			final Integer i = new Integer(x);
			List executionPeriodList = persistentExecutionPeriod.readAllExecutionPeriod();
			List finalExecutionPeriodList = (List) CollectionUtils.select(executionPeriodList, new Predicate() {
				public boolean evaluate(Object obj) {
					IExecutionPeriod executionPeriod = (IExecutionPeriod) obj;
					return executionPeriod.getIdInternal().equals(i);
				}
			});
			IExecutionPeriod executionPeriod = (IExecutionPeriod) finalExecutionPeriodList.get(0);
			persistentEnrolmentPeriod.lockWrite(enrolmentPeriod);
			enrolmentPeriod.setExecutionPeriod(executionPeriod);
			String executionPeriodName = executionPeriod.getExecutionYear().getYear() + " - " + executionPeriod.getName();
			System.out.println();
			System.out.println("SETING NEW ENROLMENT PERIOD FOR: [" + executionPeriodName + "]");
			System.out.println("START DATE: [" + enrolmentPeriod.getStartDate().toString() + "] END DATE: [" + enrolmentPeriod.getEndDate().toString() + "]");

		} catch (OutOfCurricularCourseEnrolmentPeriod e) {
			e.printStackTrace(System.out);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
		}
	}

	private static IEnrolmentPeriod getEnrolmentPeriod(IPersistentEnrolmentPeriod enrolmentPeriodDAO, IStudentCurricularPlan studentActiveCurricularPlan) throws ExcepcaoPersistencia, OutOfCurricularCourseEnrolmentPeriod {
		IEnrolmentPeriod enrolmentPeriod = enrolmentPeriodDAO.readActualEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan.getDegreeCurricularPlan());
		if (enrolmentPeriod == null){
			IEnrolmentPeriod nextEnrolmentPeriod = enrolmentPeriodDAO.readNextEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan.getDegreeCurricularPlan());
			Date startDate = null;
			Date endDate = null;
			if (nextEnrolmentPeriod != null){
				startDate = nextEnrolmentPeriod.getStartDate();
				endDate = nextEnrolmentPeriod.getEndDate();
			}
			throw new OutOfCurricularCourseEnrolmentPeriod(startDate, endDate);
		}
		return enrolmentPeriod;
	}

	private static List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(IPersistentEnrolment persistentEnrolment, IStudentCurricularPlan studentActiveCurricularPlan) {
		IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
		List enrolmentList = null;
		try {
			List executionPeriodList = persistentExecutionPeriod.readAllExecutionPeriod();
			SetEnrolmentStateIO.showExecutionPeriods(executionPeriodList);
			IExecutionPeriod executionPeriod = SetEnrolmentStateIO.selectExecutionPeriod(executionPeriodList);
			enrolmentList = persistentEnrolment.readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(studentActiveCurricularPlan, executionPeriod);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
		return enrolmentList;
	}
}