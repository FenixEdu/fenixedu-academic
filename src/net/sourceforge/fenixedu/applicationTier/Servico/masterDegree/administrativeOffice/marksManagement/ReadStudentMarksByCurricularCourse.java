package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluationWithResponsibleForGrade;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
public class ReadStudentMarksByCurricularCourse extends Service {

    public List run(Integer curricularCourseID, Integer studentNumber, String executionYear)
	    throws FenixServiceException {

	List enrolmentEvaluations = null;
	InfoTeacher infoTeacher = null;
	List<InfoSiteEnrolmentEvaluation> infoSiteEnrolmentEvaluations = new ArrayList<InfoSiteEnrolmentEvaluation>();

	CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(curricularCourseID);

	final CurricularCourse curricularCourseTemp = curricularCourse;

	// get student curricular Plan
	// in case student has school part concluded his curricular plan is
	// not in active state

	List<StudentCurricularPlan> studentCurricularPlans = null;
	Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber,
		DegreeType.MASTER_DEGREE);
	if (registration == null) {
	    throw new ExistingServiceException();
	}

	studentCurricularPlans = registration.getStudentCurricularPlans();

	StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) CollectionUtils.find(
		studentCurricularPlans, new Predicate() {
		    public boolean evaluate(Object object) {
			StudentCurricularPlan studentCurricularPlanElem = (StudentCurricularPlan) object;
			if (studentCurricularPlanElem.getDegreeCurricularPlan().equals(
				curricularCourseTemp.getDegreeCurricularPlan())) {
			    return true;
			}
			return false;
		    }
		});
	if (studentCurricularPlan == null) {

	    studentCurricularPlan = (StudentCurricularPlan) CollectionUtils.find(studentCurricularPlans,
		    new Predicate() {
			public boolean evaluate(Object object) {
			    StudentCurricularPlan studentCurricularPlanElem = (StudentCurricularPlan) object;
			    if (studentCurricularPlanElem.getDegreeCurricularPlan().getDegree().equals(
				    curricularCourseTemp.getDegreeCurricularPlan().getDegree())) {
				return true;
			    }
			    return false;
			}
		    });

	    if (studentCurricularPlan == null) {
		throw new ExistingServiceException();
	    }

	}
	// }
	Enrolment enrolment = null;
	if (executionYear != null) {
	    enrolment = curricularCourse.getEnrolmentByStudentAndYear(
		    studentCurricularPlan.getRegistration(), executionYear);

	} else {
	    // TODO: Não se sabe se este comportamento está correcto!
	    List<Enrolment> enrollments = studentCurricularPlan.getEnrolments(curricularCourse);

	    if (enrollments.isEmpty()) {
		throw new ExistingServiceException();
	    }
	    enrolment = (Enrolment) enrollments.get(0);
	}

	if (enrolment != null) {

	    EnrolmentEvaluationState enrolmentEvaluationState = EnrolmentEvaluationState.FINAL_OBJ;
	    enrolmentEvaluations = enrolment
		    .getEnrolmentEvaluationsByEnrolmentEvaluationState(enrolmentEvaluationState);

	    if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
		Person person = ((EnrolmentEvaluation) enrolmentEvaluations.get(0))
			.getPersonResponsibleForGrade();
		if (person != null) {
		    Teacher teacher = Teacher.readTeacherByUsername(person.getUsername());
		    infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
		}
	    }

	    List<InfoEnrolmentEvaluation> infoEnrolmentEvaluations = new ArrayList<InfoEnrolmentEvaluation>();
	    if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
		ListIterator iter = enrolmentEvaluations.listIterator();
		while (iter.hasNext()) {
		    EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) iter.next();
		    InfoEnrolmentEvaluation infoEnrolmentEvaluation = InfoEnrolmentEvaluationWithResponsibleForGrade
			    .newInfoFromDomain(enrolmentEvaluation);
		    InfoEnrolment infoEnrolment = InfoEnrolment.newInfoFromDomain(enrolmentEvaluation.getEnrolment());
		    infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);

		    if (enrolmentEvaluation != null && enrolmentEvaluation.hasEmployee()) {
			infoEnrolmentEvaluation.setInfoEmployee(InfoPerson
				.newInfoFromDomain(enrolmentEvaluation.getEmployee().getPerson()));

		    }
		    infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
		}

	    }
	    InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
	    infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
	    infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
	    infoSiteEnrolmentEvaluations.add(infoSiteEnrolmentEvaluation);

	}

	return infoSiteEnrolmentEvaluations;
    }
}
