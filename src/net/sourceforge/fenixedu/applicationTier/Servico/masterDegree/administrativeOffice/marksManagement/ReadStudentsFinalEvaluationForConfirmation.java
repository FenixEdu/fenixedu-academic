package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluationWithResponsibleForGrade;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Fernanda Quit�rio 10/07/2003
 * 
 */
public class ReadStudentsFinalEvaluationForConfirmation extends Service {

	public InfoSiteEnrolmentEvaluation run(Integer curricularCourseCode, String yearString)
			throws FenixServiceException, ExcepcaoPersistencia {

		List infoEnrolmentEvaluations = new ArrayList();
		InfoTeacher infoTeacher = null;
	
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
			List allEnrolmentEvaluations = enrolment.getEvaluations();
			EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) allEnrolmentEvaluations
					.get(allEnrolmentEvaluations.size() - 1);
			enrolmentEvaluations.add(enrolmentEvaluation);
		}

		if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {

			List temporaryEnrolmentEvaluations = null;
			// try
			// {
			temporaryEnrolmentEvaluations = checkForInvalidSituations(enrolmentEvaluations);
			//
			// }
			// catch (ExistingServiceException e)
			// {
			// throw new ExistingServiceException();
			// }
			// catch (InvalidSituationServiceException e)
			// {
			// throw new InvalidSituationServiceException();
			// }

			// get teacher responsible for final evaluation - he is
			// responsible for all evaluations
			// for this
			// curricularCourseScope
			Person person = ((EnrolmentEvaluation) temporaryEnrolmentEvaluations.get(0))
					.getPersonResponsibleForGrade();
			Teacher teacher = Teacher.readTeacherByUsername(person.getUsername());
			infoTeacher = InfoTeacher.newInfoFromDomain(teacher);

			// transform evaluations in databeans
			ListIterator iter = temporaryEnrolmentEvaluations.listIterator();
			while (iter.hasNext()) {
				EnrolmentEvaluation elem = (EnrolmentEvaluation) iter.next();
				InfoEnrolmentEvaluation infoEnrolmentEvaluation = InfoEnrolmentEvaluationWithResponsibleForGrade
						.newInfoFromDomain(elem);

				InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
						.newInfoFromDomain(elem.getEnrolment());
				infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);
				infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
			}
		}
		if (infoEnrolmentEvaluations.size() == 0) {
			throw new NonExistingServiceException();
		}
		final ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
		InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);

		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
		infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
		infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
		Date evaluationDate = ((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.get(0))
				.getGradeAvailableDate();
		infoSiteEnrolmentEvaluation.setLastEvaluationDate(evaluationDate);
		infoSiteEnrolmentEvaluation.setInfoExecutionPeriod(infoExecutionPeriod);

		return infoSiteEnrolmentEvaluation;
	}

	private List checkForInvalidSituations(List enrolmentEvaluations) throws ExistingServiceException,
			InvalidSituationServiceException {
		// evaluations can only be confirmated if they are not already
		// confirmated
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

		List enrolmentEvaluationsWithoutGrade = (List) CollectionUtils.select(
				temporaryEnrolmentEvaluations, new Predicate() {
					public boolean evaluate(Object input) {
						// see if there are evaluations without grade
						EnrolmentEvaluation enrolmentEvaluationInput = (EnrolmentEvaluation) input;
						if (enrolmentEvaluationInput.getGrade() == null
								|| enrolmentEvaluationInput.getGrade().length() == 0)
							return true;
						return false;
					}
				});
		if (enrolmentEvaluationsWithoutGrade != null) {
			if (enrolmentEvaluationsWithoutGrade.size() == temporaryEnrolmentEvaluations.size()) {
				throw new InvalidSituationServiceException();
			}
			temporaryEnrolmentEvaluations = (List) CollectionUtils.subtract(
					temporaryEnrolmentEvaluations, enrolmentEvaluationsWithoutGrade);
		}

		return temporaryEnrolmentEvaluations;
	}
}