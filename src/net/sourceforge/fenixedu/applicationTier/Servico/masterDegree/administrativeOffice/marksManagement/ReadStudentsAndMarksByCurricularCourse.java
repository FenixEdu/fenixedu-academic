package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluationWithResponsibleForGrade;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Fernanda Quit�rio 01/07/2003
 * 
 */
public class ReadStudentsAndMarksByCurricularCourse extends Service {

	public InfoSiteEnrolmentEvaluation run(Integer curricularCourseCode, String yearString)
			throws FenixServiceException, ExcepcaoPersistencia {

		List infoEnrolmentEvaluations = new ArrayList();
		Date lastEvaluationDate = null;

		CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);

		List enrolments = null;
		if (yearString != null) {
			enrolments = curricularCourse.getEnrolmentsByYear(yearString);
		} else {
			enrolments = curricularCourse.getCurriculumModules();
		}
		List enrolmentEvaluations = new ArrayList();
		Iterator iterEnrolment = enrolments.listIterator();
		while (iterEnrolment.hasNext()) {
			Enrolment enrolment = (Enrolment) iterEnrolment.next();
			if (enrolment.getStudentCurricularPlan().getDegreeCurricularPlan().getDegree()
					.getTipoCurso().equals(DegreeType.MASTER_DEGREE)) {
				List allEnrolmentEvaluations = enrolment.getEvaluations();
				EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) allEnrolmentEvaluations
						.get(allEnrolmentEvaluations.size() - 1);
				enrolmentEvaluations.add(enrolmentEvaluation);

			}
		}

		InfoTeacher infoTeacher = null;

		if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
			// in case we have evaluations they can be submitted only if
			// they are temporary
			List temporaryEnrolmentEvaluations = (List) CollectionUtils.select(enrolmentEvaluations,
					new Predicate() {
						public boolean evaluate(Object arg0) {
							EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) arg0;
							if (enrolmentEvaluation.getEnrolmentEvaluationState().equals(
									EnrolmentEvaluationState.TEMPORARY_OBJ))
								return true;
							return false;
						}
					});

			if (temporaryEnrolmentEvaluations == null || temporaryEnrolmentEvaluations.size() == 0) {
				throw new ExistingServiceException();
			}

			List enrolmentEvaluationsWithResponsiblePerson = (List) CollectionUtils.select(
					enrolmentEvaluations, new Predicate() {
						public boolean evaluate(Object arg0) {
							EnrolmentEvaluation enrolEval = (EnrolmentEvaluation) arg0;
							if (enrolEval.getPersonResponsibleForGrade() != null) {
								return true;
							}
							return false;
						}
					});
			if (enrolmentEvaluationsWithResponsiblePerson.size() > 0) {
				Person person = ((EnrolmentEvaluation) enrolmentEvaluationsWithResponsiblePerson
						.get(0)).getPersonResponsibleForGrade();
				Teacher teacher = Teacher.readTeacherByUsername(person.getUsername());
				infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
			}

			// transform evaluations in databeans
			ListIterator iter = temporaryEnrolmentEvaluations.listIterator();
			while (iter.hasNext()) {
				EnrolmentEvaluation elem = (EnrolmentEvaluation) iter.next();
				InfoEnrolmentEvaluation infoEnrolmentEvaluation = InfoEnrolmentEvaluationWithResponsibleForGrade
						.newInfoFromDomain(elem);
				infoEnrolmentEvaluation.setIdInternal(elem.getIdInternal());

				infoEnrolmentEvaluation.setInfoEnrolment(InfoEnrolment.newInfoFromDomain(elem.getEnrolment()));
				infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
			}
		}

		if (infoEnrolmentEvaluations.size() == 0) {
			throw new NonExistingServiceException();
		}

		// get last evaluation date to show in interface
		if (((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.get(0)).getExamDate() == null) {
			lastEvaluationDate = getLastEvaluationDate(yearString, curricularCourse);
		} else {
			lastEvaluationDate = ((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.get(0))
					.getExamDate();
		}

		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
		infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
		infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
		infoSiteEnrolmentEvaluation.setLastEvaluationDate(lastEvaluationDate);
		return infoSiteEnrolmentEvaluation;
	}

	private Date getLastEvaluationDate(String yearString, CurricularCourse curricularCourse)
	// throws ExcepcaoPersistencia, NonExistingServiceException
	{

		Date lastEvaluationDate = null;
		Iterator iterator = curricularCourse.getAssociatedExecutionCourses().listIterator();
		while (iterator.hasNext()) {
			ExecutionCourse executionCourse = (ExecutionCourse) iterator.next();
			if (executionCourse.getExecutionPeriod().getExecutionYear().getYear().equals(yearString)) {

				if (executionCourse.getAssociatedEvaluations() != null
						&& executionCourse.getAssociatedEvaluations().size() > 0) {
					List evaluationsWithoutFinal = (List) CollectionUtils.select(executionCourse
							.getAssociatedEvaluations(), new Predicate() {
						public boolean evaluate(Object input) {
							// for now returns only exams
							if (input instanceof Exam)
								return true;
							return false;
						}
					});

					ComparatorChain comparatorChain = new ComparatorChain();
					comparatorChain.addComparator(new BeanComparator("day.time"));
					comparatorChain.addComparator(new BeanComparator("beginning.time"));
					Collections.sort(evaluationsWithoutFinal, comparatorChain);

					if (evaluationsWithoutFinal.get(evaluationsWithoutFinal.size() - 1) instanceof Exam) {
						Exam lastEvaluation = (Exam) (evaluationsWithoutFinal
								.get(evaluationsWithoutFinal.size() - 1));
						if (lastEvaluationDate != null) {
							if (lastEvaluationDate.before(lastEvaluation.getDay().getTime())) {
								lastEvaluationDate = lastEvaluation.getDay().getTime();
							}
						} else {
							lastEvaluationDate = lastEvaluation.getDay().getTime();
						}
					}
				}
			}
		}

		if (lastEvaluationDate == null) {
			Calendar calendar = Calendar.getInstance();
			lastEvaluationDate = calendar.getTime();
		}
		return lastEvaluationDate;
	}
}