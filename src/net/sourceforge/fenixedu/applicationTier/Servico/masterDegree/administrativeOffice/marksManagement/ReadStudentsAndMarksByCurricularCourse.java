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

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;

public class ReadStudentsAndMarksByCurricularCourse extends Service {

    public InfoSiteEnrolmentEvaluation run(Integer curricularCourseCode, String yearString)
	    throws FenixServiceException {

	List infoEnrolmentEvaluations = new ArrayList();
	Date lastEvaluationDate = null;

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);
	final List<Enrolment> enrolments = (yearString != null) ? curricularCourse.getEnrolmentsByYear(yearString) : curricularCourse.getEnrolments();

	final List<EnrolmentEvaluation> enrolmentEvaluations = new ArrayList<EnrolmentEvaluation>();
	for (final Enrolment enrolment : enrolments) {
	    if (enrolment.getDegreeCurricularPlanOfStudent().getDegreeType() == DegreeType.MASTER_DEGREE) {
		enrolmentEvaluations.add(enrolment.getEvaluations().get(enrolment.getEvaluationsCount() - 1));
	    }
	}
	
	InfoTeacher infoTeacher = null;

	if (!enrolmentEvaluations.isEmpty()) {
	    
	    final List<EnrolmentEvaluation> temporaryEnrolmentEvaluations = (List) CollectionUtils.select(enrolmentEvaluations,
		    new Predicate() {
			public boolean evaluate(Object arg0) {
			    EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) arg0;
			    return enrolmentEvaluation.isTemporary();
			}
		    });

	    if (temporaryEnrolmentEvaluations == null || temporaryEnrolmentEvaluations.size() == 0) {
		throw new ExistingServiceException();
	    }

	    final List<EnrolmentEvaluation> enrolmentEvaluationsWithResponsiblePerson = (List) CollectionUtils.select(
		    enrolmentEvaluations, new Predicate() {
			public boolean evaluate(Object arg0) {
			    EnrolmentEvaluation enrolEval = (EnrolmentEvaluation) arg0;
			    return enrolEval.getPersonResponsibleForGrade() != null;
			}
		    });
	    
	    if (enrolmentEvaluationsWithResponsiblePerson.size() > 0) {
		Person person = ((EnrolmentEvaluation) enrolmentEvaluationsWithResponsiblePerson.get(0))
			.getPersonResponsibleForGrade();
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

		infoEnrolmentEvaluation.setInfoEnrolment(InfoEnrolment.newInfoFromDomain(elem
			.getEnrolment()));
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

    private Date getLastEvaluationDate(String yearString, CurricularCourse curricularCourse) {

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