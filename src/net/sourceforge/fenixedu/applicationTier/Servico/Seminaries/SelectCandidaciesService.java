package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.CandidacyDTO;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoClassification;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminary;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.SelectCandidaciesDTO;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class SelectCandidaciesService extends Service {

	public SelectCandidaciesDTO run(Boolean inEnrollmentPeriod, Integer seminaryID)
			throws FenixServiceException, ExcepcaoPersistencia {
		SelectCandidaciesDTO result = new SelectCandidaciesDTO();

		List<Seminary> seminaries = rootDomainObject.getSeminarys();
		List infoSeminaries = getSeminaries(inEnrollmentPeriod, seminaries);
		result.setSeminaries(infoSeminaries);

		List<CandidacyDTO> infoCandidacies = new ArrayList<CandidacyDTO>();
		for (SeminaryCandidacy candidacy : getCandidacies(seminaryID, seminaries)) {
			Registration registration = candidacy.getStudent();
			StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(registration);

			CandidacyDTO candidacyDTO = new CandidacyDTO();
			candidacyDTO.setNumber(registration.getNumber());
			candidacyDTO.setName(registration.getPerson().getName());
			candidacyDTO.setUsername(registration.getPerson().getUsername());
			candidacyDTO.setEmail(registration.getPerson().getName());
			candidacyDTO.setInfoClassification(getInfoClassification(studentCurricularPlan.getEnrolments()));
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

	private InfoClassification getInfoClassification(List<Enrolment> enrollments) {
		InfoClassification infoClassification = new InfoClassification();
		int auxInt = 0;
		float acc = 0;
		float grade = 0;
		for (Enrolment enrollment : enrollments) {
			List<EnrolmentEvaluation> enrollmentEvaluations = enrollment.getEvaluations();
			EnrolmentEvaluation enrollmentEvaluation = null;
			if (!enrollmentEvaluations.isEmpty()) {
				enrollmentEvaluation = Collections.max(enrollmentEvaluations);
			}

			String stringGrade;
			if (enrollmentEvaluation != null) {
				stringGrade = enrollmentEvaluation.getGrade();
			} else {
				stringGrade = GradeScale.NA;
			}

			if (stringGrade != null 
                    && !stringGrade.equals("") 
                    && !stringGrade.equals(GradeScale.RE)
					&& !stringGrade.equals(GradeScale.NA) 
                    && !stringGrade.equals(GradeScale.AP)) {
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
		infoClassification.setCompletedCourses(Integer.valueOf(auxInt).toString());
		return infoClassification;
	}

	private StudentCurricularPlan getStudentCurricularPlan(Registration registration) {
		long startDate = Long.MAX_VALUE;
		StudentCurricularPlan selectedSCP = null;
		for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {
			if (studentCurricularPlan.getStartDate().getTime() < startDate) {
				startDate = studentCurricularPlan.getStartDate().getTime();
				selectedSCP = studentCurricularPlan;
			}
		}
		return selectedSCP;
	}

	private List<SeminaryCandidacy> getCandidacies(final Integer seminaryID, List seminaries) {
		Seminary seminary = (Seminary) CollectionUtils.find(seminaries, new Predicate() {

			public boolean evaluate(Object arg0) {
				Seminary seminary = (Seminary) arg0;
				return seminary.getIdInternal().equals(seminaryID);
			}
		});

		return seminary.getCandidacies();
	}

	private List<InfoSeminary> getSeminaries(Boolean inEnrollmentPeriod, List<Seminary> seminaries) {
		List<InfoSeminary> result = new ArrayList<InfoSeminary>();

		for (Seminary seminary : seminaries) {
			Calendar now = new GregorianCalendar();
			
			Calendar beginDate = new GregorianCalendar();
            beginDate.setTimeInMillis(seminary.getEnrollmentBeginTime().getTimeInMillis()
                    + seminary.getEnrollmentBeginDate().getTimeInMillis());
            
            Calendar endDate = new GregorianCalendar();
            endDate.setTimeInMillis(seminary.getEnrollmentEndTime().getTimeInMillis()
					+ seminary.getEnrollmentEndDate().getTimeInMillis());
			
			if (!inEnrollmentPeriod || (endDate.after(now) && beginDate.before(now)))
				result.add(InfoSeminaryWithEquivalencies.newInfoFromDomain(seminary));
		}
		return result;
	}

}
