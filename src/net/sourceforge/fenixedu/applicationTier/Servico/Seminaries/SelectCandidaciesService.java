/*
 * Created on Aug 24, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.CandidacyDTO;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoClassification;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.SelectCandidaciesDTO;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author João Mota
 * 
 */
public class SelectCandidaciesService implements IService {

	public SelectCandidaciesDTO run(Boolean inEnrollmentPeriod, Integer seminaryID)
			throws FenixServiceException, ExcepcaoPersistencia {
		SelectCandidaciesDTO result = new SelectCandidaciesDTO();
		ISuportePersistente persistenceSupport;

		persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IPersistentObject persistentObject = persistenceSupport.getIPersistentObject();
		IPersistentSeminary persistentSeminary = persistenceSupport.getIPersistentSeminary();
		List seminaries = persistentSeminary.readAll();
		List infoSeminaries = getSeminaries(inEnrollmentPeriod, seminaries);
		result.setSeminaries(infoSeminaries);
		List candidacies = getCandidacies(seminaryID, seminaries, persistentObject);
		Iterator iter = candidacies.iterator();
		List infoCandidacies = new ArrayList();
		while (iter.hasNext()) {
			Candidacy candidacy = (Candidacy) iter.next();
			Student student = candidacy.getStudent();
			StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student);
			List enrollments = studentCurricularPlan.getEnrolments();

			CandidacyDTO candidacyDTO = new CandidacyDTO();
			candidacyDTO.setNumber(student.getNumber());
			candidacyDTO.setName(student.getPerson().getNome());
			candidacyDTO.setUsername(student.getPerson().getUsername());
			candidacyDTO.setEmail(student.getPerson().getNome());
			candidacyDTO.setInfoClassification(getInfoClassification(enrollments));
			candidacyDTO.setCandidacyId(candidacy.getIdInternal());
			if (candidacy.getApproved() != null) {
				candidacyDTO.setApproved(candidacy.getApproved());
			} else {
				candidacyDTO.setApproved(Boolean.FALSE);
			}
			infoCandidacies.add(candidacyDTO);
		}

		result.setCandidacies(infoCandidacies);

		return result;
	}

	/**
	 * @param enrollments
	 * @param infoClassification
	 */
	private InfoClassification getInfoClassification(List enrollments) {
		InfoClassification infoClassification = new InfoClassification();
		int auxInt = 0;
		float acc = 0;
		float grade = 0;
		for (Iterator iter1 = enrollments.iterator(); iter1.hasNext();) {
			Enrolment enrollment = (Enrolment) iter1.next();
			List enrollmentEvaluations = enrollment.getEvaluations();
			EnrolmentEvaluation enrollmentEvaluation = null;
			if (enrollmentEvaluations != null && !enrollmentEvaluations.isEmpty()) {
				enrollmentEvaluation = (EnrolmentEvaluation) Collections.max(enrollmentEvaluations);
			}

			String stringGrade;
			if (enrollmentEvaluation != null) {

				stringGrade = enrollmentEvaluation.getGrade();
			} else {
				stringGrade = "NA";
			}

			if (stringGrade != null && !stringGrade.equals("") && !stringGrade.equals("RE")
					&& !stringGrade.equals("NA") && !stringGrade.equals("AP")) {
				Float gradeObject = new Float(stringGrade);
				grade = gradeObject.floatValue();
				acc += grade;
				auxInt++;
			}

		}
		if (auxInt != 0) {
			String value = new DecimalFormat("#0.0").format(acc / auxInt);
			infoClassification.setAritmeticClassification(value);
		}
		infoClassification.setCompletedCourses(new Integer(auxInt).toString());
		return infoClassification;
	}

	/**
	 * @param student
	 * @return
	 */
	private StudentCurricularPlan getStudentCurricularPlan(Student student) {
		List curricularPlans = student.getStudentCurricularPlans();
		long startDate = Long.MAX_VALUE;
		StudentCurricularPlan selectedSCP = null;
		for (Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
			StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iter.next();
			if (studentCurricularPlan.getStartDate().getTime() < startDate) {
				startDate = studentCurricularPlan.getStartDate().getTime();
				selectedSCP = studentCurricularPlan;
			}
		}
		return selectedSCP;
	}

	/**
	 * @param seminaryID
	 * @param seminaries
	 * @param persistentObject
	 * @return
	 */
	private List getCandidacies(final Integer seminaryID, List seminaries,
			IPersistentObject persistentObject) {
		Seminary seminary = (Seminary) CollectionUtils.find(seminaries, new Predicate() {

			public boolean evaluate(Object arg0) {
				Seminary seminary = (Seminary) arg0;
				return seminary.getIdInternal().equals(seminaryID);
			}
		});

		return seminary.getCandidacies();
	}

	/**
	 * @param persistentSeminary
	 * @param inEnrollmentPeriod
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private List getSeminaries(Boolean inEnrollmentPeriod, List seminaries) {
		List result = new ArrayList();

		for (Iterator iterator = seminaries.iterator(); iterator.hasNext();) {
			InfoSeminaryWithEquivalencies infoSeminary = InfoSeminaryWithEquivalencies
					.newInfoFromDomain((Seminary) iterator.next());

			Calendar now = new GregorianCalendar();
			Calendar endDate = new GregorianCalendar();
			Calendar beginDate = new GregorianCalendar();
			endDate.setTimeInMillis(infoSeminary.getEnrollmentEndTime().getTimeInMillis()
					+ infoSeminary.getEnrollmentEndDate().getTimeInMillis());
			beginDate.setTimeInMillis(infoSeminary.getEnrollmentBeginTime().getTimeInMillis()
					+ infoSeminary.getEnrollmentBeginDate().getTimeInMillis());
			if ((!inEnrollmentPeriod.booleanValue()) || (endDate.after(now) && beginDate.before(now)))
				result.add(infoSeminary);
		}
		return result;
	}

}