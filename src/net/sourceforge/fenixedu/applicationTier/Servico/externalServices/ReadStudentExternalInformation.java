/*
 * Created on 2:40:27 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.GetEnrolmentGrade;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalAdressInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalCitizenshipInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalCurricularCourseInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalDegreeBranchInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalDegreeCurricularPlanInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalEnrollmentInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalIdentificationInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalPersonInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoStudentExternalInformation;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.StringAppender;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz </a>
 * 
 * Created at 2:40:27 PM, Mar 11, 2005
 */
public class ReadStudentExternalInformation extends Service {

    public Collection run(String username) throws ExcepcaoPersistencia, FenixServiceException {
	Collection result = new ArrayList();
	Person person = Person.readPersonByUsername(username);
	Collection students = person.getStudents();
	for (Iterator iter = students.iterator(); iter.hasNext();) {
	    InfoStudentExternalInformation info = new InfoStudentExternalInformation();
	    Registration registration = (Registration) iter.next();
	    if (registration.getActiveState().getStateType() != RegistrationStateType.CANCELED
		    && registration.getActiveStudentCurricularPlan() != null) {

		info.setPerson(this.buildExternalPersonInfo(person));
		info.setDegree(this.buildExternalDegreeCurricularPlanInfo(registration));
		info.setCourses(this.buildExternalEnrollmentsInfo(registration));
		info.setAvailableRemainingCourses(this.buildAvailableRemainingCourses(registration));
		info.setNumber(registration.getNumber().toString());

		result.add(info);
	    }
	}

	return result;
    }

    private Collection buildAvailableRemainingCourses(final Registration registration) {
	final Collection<CurricularCourse> allCourses = registration.getActiveStudentCurricularPlan()
		.getDegreeCurricularPlan().getCurricularCourses();
	final Collection<InfoExternalCurricularCourseInfo> availableInfos = new ArrayList<InfoExternalCurricularCourseInfo>();
	for (final CurricularCourse curricularCourse : allCourses) {
	    if (hasActiveScope(curricularCourse)
		    && studentIsNotApprovedInCurricularCourse(registration, curricularCourse)) {
		final InfoExternalCurricularCourseInfo infoExternalCurricularCourseInfo = InfoExternalCurricularCourseInfo
			.newFromDomain(curricularCourse);
		infoExternalCurricularCourseInfo.setName("" + curricularCourse.getIdInternal() + " "
			+ curricularCourse.getName());
		availableInfos.add(infoExternalCurricularCourseInfo);
	    }
	}
	return availableInfos;
    }

    private boolean studentIsNotApprovedInCurricularCourse(final Registration registration, final CurricularCourse curricularCourse) {
	for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.isCurricularCourseApproved(curricularCourse)) {
		return false;
	    }
	}
	return true;
    }

    private boolean hasActiveScope(final CurricularCourse curricularCourse) {
	for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopesSet()) {
	    if (curricularCourseScope.isActive().booleanValue()) {
		return true;
	    }
	}
	return false;
    }

    /**
         * @param registration
         * @return
         * @throws FenixServiceException
         * @throws ExcepcaoPersistencia
         */
    private Collection buildExternalEnrollmentsInfo(Registration registration)
	    throws FenixServiceException, ExcepcaoPersistencia {
	Collection enrollments = new ArrayList();
	Collection curricularPlans = registration.getStudentCurricularPlans();
	for (Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
	    StudentCurricularPlan curricularPlan = (StudentCurricularPlan) iter.next();
	    for (Iterator iterEnrolments = curricularPlan.getEnrolments().iterator(); iterEnrolments
		    .hasNext();) {
		Enrolment enrollment = (Enrolment) iterEnrolments.next();
		if (enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
		    CurricularCourse curricularCourse = enrollment.getCurricularCourse();
		    if (curricularCourse.getEctsCredits().doubleValue() == 0) {
			Collection<DegreeCurricularPlan> degrees = curricularCourse
				.getDegreeCurricularPlan().getDegree().getDegreeCurricularPlans();
			for (DegreeCurricularPlan degree : degrees) {
			    CurricularCourse curricularCourseWithSameCode = degree
				    .getCurricularCourseByCode(curricularCourse.getCode());
			    if (curricularCourseWithSameCode != null
				    && curricularCourseWithSameCode.getEctsCredits() != null
				    && !(curricularCourseWithSameCode.getEctsCredits().doubleValue() == 0)) {
				curricularCourse = curricularCourseWithSameCode;
				break;
			    }
			}
		    }

		    if (!registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan()
			    .getDegree().getDegreeCurricularPlans().contains(
				    enrollment.getCurricularCourse().getDegreeCurricularPlan())) {
			curricularCourse = null;
			DegreeCurricularPlan studentDegreeCurricularPlan = registration
				.getActiveStudentCurricularPlan().getDegreeCurricularPlan();
			List<CurricularCourse> curricularCourses = enrollment.getCurricularCourse()
				.getCompetenceCourse().getAssociatedCurricularCourses();
			for (CurricularCourse course : curricularCourses) {
			    if (course.getDegreeCurricularPlan().equals(studentDegreeCurricularPlan)) {
				curricularCourse = course;
				break;
			    }
			}
		    }
		    if (curricularCourse != null) {
			InfoExternalEnrollmentInfo info = InfoExternalEnrollmentInfo
				.newFromEnrollment(enrollment);

			if (!curricularCourse.equals(enrollment.getCurricularCourse())) {
			    info.setCourse(InfoExternalCurricularCourseInfo
				    .newFromDomain(curricularCourse));
			}

			GetEnrolmentGrade getEnrollmentGrade = new GetEnrolmentGrade();
			InfoEnrolmentEvaluation infoEnrollmentEvaluation = getEnrollmentGrade
				.run(enrollment);
			info.setFinalGrade(infoEnrollmentEvaluation.getGrade());
			enrollments.add(info);
			System.out.println(StringAppender.append("Adding: ", info.getCourse().getName(),
				" ", info.getFinalGrade(), " ", info.getCourse().getCode(), " ", info
					.getCourse().getCredits(), " ", info.getCourse()
					.getECTSCredits(), " ", info.getCourse().getWeigth()));
		    }
		}
	    }
	}

	return enrollments;
    }

    /**
         * @param registration
         * @return
         */
    private InfoExternalDegreeCurricularPlanInfo buildExternalDegreeCurricularPlanInfo(
	    Registration registration) {
	InfoExternalDegreeCurricularPlanInfo info = new InfoExternalDegreeCurricularPlanInfo();
	DegreeCurricularPlan degreeCurricularPlan = registration.getActiveStudentCurricularPlan()
		.getDegreeCurricularPlan();

	info.setName(degreeCurricularPlan.getName());
	info.setCode(degreeCurricularPlan.getDegree().getIdInternal().toString());
	info.setBranch(this.buildExternalDegreeBranchInfo(registration));

	Collection courses = registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan()
		.getCurricularCourses();
	for (Iterator iter = courses.iterator(); iter.hasNext();) {
	    CurricularCourse curricularCourse = (CurricularCourse) iter.next();
	    if (curricularCourse.getEctsCredits().doubleValue() == 0) {
		Collection<DegreeCurricularPlan> degrees = curricularCourse.getDegreeCurricularPlan()
			.getDegree().getDegreeCurricularPlans();
		for (DegreeCurricularPlan degree : degrees) {
		    CurricularCourse curricularCourseWithSameCode = degree
			    .getCurricularCourseByCode(curricularCourse.getCode());
		    if (curricularCourseWithSameCode != null
			    && curricularCourseWithSameCode.getEctsCredits() != null
			    && !(curricularCourseWithSameCode.getEctsCredits().doubleValue() == 0)) {
			curricularCourse = curricularCourseWithSameCode;
			break;
		    }
		}
	    }

	    info.addCourse(InfoExternalCurricularCourseInfo.newFromDomain(curricularCourse));
	}

	return info;
    }

    /**
         * @param registration
         * @return
         */
    private InfoExternalDegreeBranchInfo buildExternalDegreeBranchInfo(Registration registration) {
	InfoExternalDegreeBranchInfo info = new InfoExternalDegreeBranchInfo();
	if (registration.getActiveStudentCurricularPlan().getBranch() != null) {
	    info.setName(registration.getActiveStudentCurricularPlan().getBranch().getName());
	    info.setCode(registration.getActiveStudentCurricularPlan().getBranch().getCode());
	}

	return info;
    }

    /**
         * @param infoPerson
         * @return
         */
    private InfoExternalPersonInfo buildExternalPersonInfo(Person person) {
	InfoExternalPersonInfo info = new InfoExternalPersonInfo();
	info.setAddress(this.buildInfoExternalAdressInfo(person));
	info.setBirthday(DateFormatUtil.format("yyyy-MM-dd", person.getDateOfBirth()));
	info.setCelularPhone(person.getMobile());
	info.setCitizenship(this.builsExternalCitizenshipInfo(person));
	info.setEmail(person.getEmail());
	info.setFiscalNumber(person.getSocialSecurityNumber());
	info.setIdentification(this.buildExternalIdentificationInfo(person));
	info.setName(person.getNome());
	info.setNationality(person.getNacionalidade());
	info.setPhone(person.getPhone());
	info.setSex(person.getGender().toString());

	return info;
    }

    /**
         * @param infoPerson
         * @return
         */
    private InfoExternalIdentificationInfo buildExternalIdentificationInfo(Person person) {
	InfoExternalIdentificationInfo info = new InfoExternalIdentificationInfo();
	info.setDocumentType(person.getIdDocumentType().toString());
	info.setNumber(person.getDocumentIdNumber());
	if (person.getEmissionDateOfDocumentId() != null) {
	    info.setEmitionDate(DateFormatUtil
		    .format("yyyy-MM-dd", person.getEmissionDateOfDocumentId()));
	}
	if (person.getExpirationDateOfDocumentId() != null) {
	    info.setExpiryDate(DateFormatUtil.format("yyyy-MM-dd", person
		    .getExpirationDateOfDocumentId()));
	}
	if (person.getEmissionLocationOfDocumentId() != null) {
	    info.setEmitionLocal(person.getEmissionLocationOfDocumentId());
	}

	return info;
    }

    /**
         * @param infoPerson
         * @return
         */
    private InfoExternalCitizenshipInfo builsExternalCitizenshipInfo(Person person) {
	InfoExternalCitizenshipInfo info = new InfoExternalCitizenshipInfo();
	info.setArea(person.getParishOfBirth());
	info.setCounty(person.getDistrictSubdivisionOfBirth());

	return info;
    }

    /**
         * @param infoPerson
         * @return
         */
    private InfoExternalAdressInfo buildInfoExternalAdressInfo(Person person) {
	InfoExternalAdressInfo info = new InfoExternalAdressInfo();
	info.setPostalCode(person.getAreaCode());
	info.setStreet(person.getAddress());
	info.setTown(person.getArea());

	return info;
    }
}