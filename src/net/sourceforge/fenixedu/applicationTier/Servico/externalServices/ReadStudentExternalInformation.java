/*
 * Created on 2:40:27 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.RootCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Substitution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz </a>
 * 
 * Created at 2:40:27 PM, Mar 11, 2005
 */
public class ReadStudentExternalInformation extends Service {

    public Collection run(String username) throws ExcepcaoPersistencia, FenixServiceException {
	Person person = Person.readPersonByUsername(username);

	if (person.getStudent().getMostSignificantDegreeType().isBolonhaType()) {
	    return getResultForBolonha(person);
	} else {
	    return getResultForPreBolonha(person);
	}

    }

    private Collection getResultForBolonha(Person person) {
	Collection result = new ArrayList();
	Collection<CycleCurriculumGroup> studentCycles = getStudentCycles(person.getStudent());

	InfoStudentExternalInformation info = new InfoStudentExternalInformation();
	info.setPerson(this.buildExternalPersonInfo(person));
	info.setNumber(person.getStudent().getNumber().toString());

	info.setDegree(this.buildExternalDegreeCurricularPlanInfoBolonha(studentCycles));

	Set externalEnrollmentsInfoBolonha = new HashSet();
	Set availableRemainingCoursesBolonha = new HashSet();
	for (CycleCurriculumGroup cycleCurriculumGroup : studentCycles) {

	    externalEnrollmentsInfoBolonha.addAll(this.buildExternalEnrollmentsInfoBolonha(cycleCurriculumGroup));
	    availableRemainingCoursesBolonha.addAll(this.buildAvailableRemainingCoursesBolonha(cycleCurriculumGroup));

	}

	info.setCourses(externalEnrollmentsInfoBolonha);
	info.setAvailableRemainingCourses(availableRemainingCoursesBolonha);
	info.setCurricularYear(getCurriculumYear(studentCycles));

	final double average = getDegreeAverage(studentCycles);
	info.setAverage(average);
	result.add(info);
	return result;
    }

    private int getCurriculumYear(Collection<CycleCurriculumGroup> studentCycles) {
	CycleCurriculumGroup cycleCurriculumGroup = (CycleCurriculumGroup) Collections.max(studentCycles, new BeanComparator(
		"cycleType"));
	Integer curricularYear = cycleCurriculumGroup.getCurriculum().getCurricularYear();
	return cycleCurriculumGroup.getCycleType() == CycleType.FIRST_CYCLE ? curricularYear : (curricularYear += 3);
    }

    private double getDegreeAverage(Collection<CycleCurriculumGroup> studentCycles) {
	Iterator<CycleCurriculumGroup> iterator = studentCycles.iterator();
	Curriculum curriculum = iterator.next().getCurriculum();
	while (iterator.hasNext()) {
	    curriculum.add(iterator.next().getCurriculum());
	}
	return curriculum.getAverage().doubleValue();
    }

    private Collection getResultForPreBolonha(Person person) {
	Collection result = new ArrayList();
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

		final int curricularYear = registration.getCurricularYear();
		info.setCurricularYear(curricularYear);
		final double average = registration.getAverage().doubleValue();
		info.setAverage(average);

		result.add(info);
	    }
	}

	return result;
    }

    private Collection<CycleCurriculumGroup> getStudentCycles(Student student) {
	Registration activeRegistration = getActiveRegistrationForBolonha(student);
	if (activeRegistration.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)) {
	    return Collections.singleton(activeRegistration.getActiveStudentCurricularPlan().getRoot().getCycleCurriculumGroup(
		    CycleType.FIRST_CYCLE));
	} else if (activeRegistration.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
	    CycleType currentCycleType = activeRegistration.getCurrentCycleType();
	    if (currentCycleType == CycleType.FIRST_CYCLE) {
		return Collections.singleton(activeRegistration.getActiveStudentCurricularPlan().getRoot()
			.getCycleCurriculumGroup(CycleType.FIRST_CYCLE));
	    } else {
		Set<CycleCurriculumGroup> groups = new HashSet<CycleCurriculumGroup>();
		groups.add(activeRegistration.getActiveStudentCurricularPlan().getRoot().getCycleCurriculumGroup(
			CycleType.SECOND_CYCLE));

		CycleCurriculumGroup cycleCurriculumGroup = getConcludedFirstCycle(student);
		if (cycleCurriculumGroup != null) {
		    groups.add(cycleCurriculumGroup);
		}
		return groups;
	    }
	} else {
	    Set<CycleCurriculumGroup> groups = new HashSet<CycleCurriculumGroup>();
	    groups.add(activeRegistration.getActiveStudentCurricularPlan().getRoot().getCycleCurriculumGroup(
		    CycleType.SECOND_CYCLE));

	    CycleCurriculumGroup cycleCurriculumGroup = getConcludedFirstCycle(student);
	    if (cycleCurriculumGroup != null) {
		groups.add(cycleCurriculumGroup);
	    }
	    return groups;
	}
    }

    private CycleCurriculumGroup getConcludedFirstCycle(Student student) {
	for (Registration registration : student.getRegistrationsSet()) {
	    for (CycleCurriculumGroup cycleCurriculumGroup : registration.getLastStudentCurricularPlan().getRoot()
		    .getCycleCurriculumGroups()) {
		if (cycleCurriculumGroup.getCycleType() == CycleType.FIRST_CYCLE && cycleCurriculumGroup.isConcluded()) {
		    return cycleCurriculumGroup;
		}
	    }
	}
	return null;
    }

    private Registration getActiveRegistrationForBolonha(Student student) {
	for (Registration registration : student.getRegistrationsSet()) {
	    if (registration.isActive()
		    && (registration.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)
			    || registration.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) || registration
			    .getDegreeType().equals(DegreeType.BOLONHA_MASTER_DEGREE))) {
		return registration;
	    }
	}
	return null;
    }

    private Collection buildAvailableRemainingCourses(final Registration registration) {
	final Collection<CurricularCourse> allCourses = registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan()
		.getCurricularCourses();
	final Collection<InfoExternalCurricularCourseInfo> availableInfos = new ArrayList<InfoExternalCurricularCourseInfo>();
	for (final CurricularCourse curricularCourse : allCourses) {
	    if (hasActiveScope(curricularCourse) && studentIsNotApprovedInCurricularCourse(registration, curricularCourse)) {
		final InfoExternalCurricularCourseInfo infoExternalCurricularCourseInfo = InfoExternalCurricularCourseInfo
			.newFromDomain(curricularCourse);
		infoExternalCurricularCourseInfo
			.setName("" + curricularCourse.getIdInternal() + " " + curricularCourse.getName());
		availableInfos.add(infoExternalCurricularCourseInfo);
	    }
	}
	return availableInfos;
    }

    private Collection buildAvailableRemainingCoursesBolonha(final CycleCurriculumGroup cycleCurriculumGroup) {
	final Collection<CurricularCourse> allCourses = cycleCurriculumGroup.getDegreeModule().getAllOpenCurricularCourses();
	final Collection<InfoExternalCurricularCourseInfo> availableInfos = new ArrayList<InfoExternalCurricularCourseInfo>();
	for (final CurricularCourse curricularCourse : allCourses) {
	    if (hasActiveScope(curricularCourse) && !cycleCurriculumGroup.isApproved(curricularCourse)) {
		final InfoExternalCurricularCourseInfo infoExternalCurricularCourseInfo = InfoExternalCurricularCourseInfo
			.newFromDomain(curricularCourse);
		infoExternalCurricularCourseInfo
			.setName("" + curricularCourse.getIdInternal() + " " + curricularCourse.getName());
		if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE && cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
		    infoExternalCurricularCourseInfo.setCurricularYear((Integer.valueOf(infoExternalCurricularCourseInfo
			    .getCurricularYear()) + 3)
			    + "");
		}
		availableInfos.add(infoExternalCurricularCourseInfo);
	    }
	}
	return availableInfos;
    }

    private boolean studentIsNotApprovedInCurricularCourse(final Registration registration,
	    final CurricularCourse curricularCourse) {
	for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.isApproved(curricularCourse)) {
		return false;
	    }
	}
	return true;
    }

    private boolean hasActiveScope(final CurricularCourse curricularCourse) {
	for (final DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
	    if (degreeModuleScope.isActive()) {
		return true;
	    }
	}
	return false;
    }

    private Collection buildExternalEnrollmentsInfo(Registration registration) {
	Collection enrollments = new ArrayList();
	Collection curricularPlans = registration.getStudentCurricularPlans();
	for (Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
	    StudentCurricularPlan curricularPlan = (StudentCurricularPlan) iter.next();
	    for (Iterator iterEnrolments = curricularPlan.getEnrolments().iterator(); iterEnrolments.hasNext();) {
		Enrolment enrollment = (Enrolment) iterEnrolments.next();
		if (enrollment.isEnrolmentStateApproved()) {
		    CurricularCourse curricularCourse = enrollment.getCurricularCourse();
		    if (curricularCourse.getEctsCredits().doubleValue() == 0) {
			Collection<DegreeCurricularPlan> degrees = curricularCourse.getDegreeCurricularPlan().getDegree()
				.getDegreeCurricularPlans();
			for (DegreeCurricularPlan degree : degrees) {
			    CurricularCourse curricularCourseWithSameCode = degree.getCurricularCourseByCode(curricularCourse
				    .getCode());
			    if (curricularCourseWithSameCode != null && curricularCourseWithSameCode.getEctsCredits() != null
				    && !(curricularCourseWithSameCode.getEctsCredits().doubleValue() == 0)) {
				curricularCourse = curricularCourseWithSameCode;
				break;
			    }
			}
		    }

		    if (!registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan().getDegree()
			    .getDegreeCurricularPlans().contains(enrollment.getCurricularCourse().getDegreeCurricularPlan())) {
			curricularCourse = null;
			DegreeCurricularPlan studentDegreeCurricularPlan = registration.getActiveStudentCurricularPlan()
				.getDegreeCurricularPlan();
			List<CurricularCourse> curricularCourses = enrollment.getCurricularCourse().getCompetenceCourse()
				.getAssociatedCurricularCourses();
			for (CurricularCourse course : curricularCourses) {
			    if (course.getDegreeCurricularPlan().equals(studentDegreeCurricularPlan)) {
				curricularCourse = course;
				break;
			    }
			}
		    }
		    if (curricularCourse != null) {
			InfoExternalEnrollmentInfo info = InfoExternalEnrollmentInfo.newFromEnrollment(enrollment);

			if (!curricularCourse.equals(enrollment.getCurricularCourse())) {
			    info.setCourse(InfoExternalCurricularCourseInfo.newFromDomain(curricularCourse));
			}

			GetEnrolmentGrade getEnrollmentGrade = new GetEnrolmentGrade();
			InfoEnrolmentEvaluation infoEnrollmentEvaluation = getEnrollmentGrade.run(enrollment);
			if (infoEnrollmentEvaluation != null) {
			    info.setGrade(enrollment.getGrade());
			}
			enrollments.add(info);
		    }
		}
	    }
	}

	return enrollments;
    }

    private Collection buildExternalEnrollmentsInfoBolonha(CycleCurriculumGroup cycleCurriculumGroup) {
	Collection<InfoExternalEnrollmentInfo> curriculumLines = new ArrayList<InfoExternalEnrollmentInfo>();

	for (CurriculumLine curriculumLine : cycleCurriculumGroup.getAllCurriculumLines()) {
	    if (curriculumLine.isApproved()) {
		if (curriculumLine.isEnrolment()) {
		    Enrolment enrolment = (Enrolment) curriculumLine;
		    InfoExternalEnrollmentInfo info = InfoExternalEnrollmentInfo.newFromEnrollment(enrolment);
		    info.setGrade(enrolment.getGrade());
		    if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE && cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
			info.getCourse().setCurricularYear((Integer.valueOf(info.getCourse().getCurricularYear()) + 3) + "");
		    }
		    curriculumLines.add(info);
		} else if (curriculumLine.isDismissal()) {
		    Dismissal dismissal = (Dismissal) curriculumLine;
		    if (dismissal.isCreditsDismissal()) {
			continue;
		    }
		    if (dismissal.getCredits().isEquivalence()) {
			InfoExternalEnrollmentInfo info = InfoExternalEnrollmentInfo.newFromCurricularCourse(dismissal
				.getCurricularCourse(), dismissal.getCredits().getGrade());
			if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE && cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
			    info.getCourse().setCurricularYear((Integer.valueOf(info.getCourse().getCurricularYear()) + 3) + "");
			}
			curriculumLines.add(info);
		    } else if (dismissal.getCredits().isSubstitution()) {
			Grade grade = getAverage((Substitution) dismissal.getCredits());
			InfoExternalEnrollmentInfo info = InfoExternalEnrollmentInfo.newFromCurricularCourse(dismissal
				.getCurricularCourse(), grade);
			if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE && cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
			    info.getCourse().setCurricularYear((Integer.valueOf(info.getCourse().getCurricularYear()) + 3) + "");
			}
			curriculumLines.add(info);
		    }
		}
	    }
	}

	return curriculumLines;
    }

    private Grade getAverage(Substitution substitution) {
	BigDecimal gradeWeigth = BigDecimal.ZERO;
	BigDecimal weigth = BigDecimal.ZERO;
	for (EnrolmentWrapper enrolmentWrapper : substitution.getEnrolments()) {
	    Grade grade = enrolmentWrapper.getIEnrolment().getGrade();
	    if (grade.getGradeScale() == GradeScale.TYPEAP && grade.isApproved()) {
		return grade;
	    }
	    gradeWeigth = gradeWeigth.add(BigDecimal.valueOf(Long.valueOf(grade.getValue())).multiply(
		    BigDecimal.valueOf(enrolmentWrapper.getIEnrolment().getWeigth())));
	    weigth = weigth.add(BigDecimal.valueOf(enrolmentWrapper.getIEnrolment().getWeigth()));
	}

	BigDecimal res = gradeWeigth.divide(weigth);

	return Grade.createGrade(res.setScale(0, RoundingMode.HALF_UP).toString(), GradeScale.TYPE20);

    }

    private InfoExternalDegreeCurricularPlanInfo buildExternalDegreeCurricularPlanInfo(Registration registration) {
	InfoExternalDegreeCurricularPlanInfo info = new InfoExternalDegreeCurricularPlanInfo();
	DegreeCurricularPlan degreeCurricularPlan = registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan();

	final String degreeCode = degreeCurricularPlan.getDegree().getSigla();
	final String name = degreeCode.indexOf('-') > 0 ? StringUtils.substringBefore(degreeCode, "-") : degreeCode;
	info.setName(name);
	info.setCode(degreeCurricularPlan.getDegree().getIdInternal().toString());
	info.setBranch(this.buildExternalDegreeBranchInfo(registration));

	Collection courses = registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan().getCurricularCourses();
	for (Iterator iter = courses.iterator(); iter.hasNext();) {
	    CurricularCourse curricularCourse = (CurricularCourse) iter.next();
	    if (curricularCourse.getEctsCredits().doubleValue() == 0) {
		Collection<DegreeCurricularPlan> degrees = curricularCourse.getDegreeCurricularPlan().getDegree()
			.getDegreeCurricularPlans();
		for (DegreeCurricularPlan degree : degrees) {
		    CurricularCourse curricularCourseWithSameCode = degree.getCurricularCourseByCode(curricularCourse.getCode());
		    if (curricularCourseWithSameCode != null && curricularCourseWithSameCode.getEctsCredits() != null
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

    private InfoExternalDegreeCurricularPlanInfo buildExternalDegreeCurricularPlanInfoBolonha(
	    Collection<CycleCurriculumGroup> studentCycles) {
	InfoExternalDegreeCurricularPlanInfo info = new InfoExternalDegreeCurricularPlanInfo();
	DegreeCurricularPlan degreeCurricularPlan = getCycleDegree(studentCycles);

	final String degreeCode = degreeCurricularPlan.getDegree().getSigla();
	final String name = degreeCode.indexOf('-') > 0 ? StringUtils.substringBefore(degreeCode, "-") : degreeCode;
	info.setName(name);
	info.setCode(degreeCurricularPlan.getDegree().getIdInternal().toString());
	info.setBranch(null);

	for (CycleCurriculumGroup cycleCurriculumGroup : studentCycles) {
	    for (CurricularCourse curricularCourse : cycleCurriculumGroup.getDegreeModule().getAllOpenCurricularCourses()) {
		InfoExternalCurricularCourseInfo infoExternalCurricularCourseInfo = InfoExternalCurricularCourseInfo
			.newFromDomain(curricularCourse);
		if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE && cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
		    infoExternalCurricularCourseInfo.setCurricularYear((Integer.valueOf(infoExternalCurricularCourseInfo
			    .getCurricularYear()) + 3)
			    + "");
		}
		info.addCourse(infoExternalCurricularCourseInfo);
	    }
	}

	return info;
    }

    private DegreeCurricularPlan getCycleDegree(Collection<CycleCurriculumGroup> studentCycles) {
	for (CycleCurriculumGroup cycleCurriculumGroup : studentCycles) {
	    if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE) {
		return cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan();
	    }
	}

	for (CycleCurriculumGroup cycleCurriculumGroup : studentCycles) {
	    if (cycleCurriculumGroup.getCycleType() == CycleType.FIRST_CYCLE) {
		return cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan();
	    }
	}

	return null;
    }

    private InfoExternalDegreeBranchInfo buildExternalDegreeBranchInfo(Registration registration) {
	InfoExternalDegreeBranchInfo info = new InfoExternalDegreeBranchInfo();
	if (registration.getActiveStudentCurricularPlan().getBranch() != null) {
	    info.setName(registration.getActiveStudentCurricularPlan().getBranch().getName());
	    info.setCode(registration.getActiveStudentCurricularPlan().getBranch().getCode());
	}

	return info;
    }

    private InfoExternalPersonInfo buildExternalPersonInfo(Person person) {
	InfoExternalPersonInfo info = new InfoExternalPersonInfo();
	info.setAddress(this.buildInfoExternalAdressInfo(person));
	info.setBirthday(DateFormatUtil.format("yyyy-MM-dd", person.getDateOfBirth()));
	info.setCelularPhone(person.getMobile());
	info.setCitizenship(this.builsExternalCitizenshipInfo(person));
	StringBuilder emails = new StringBuilder();
	Iterator<EmailAddress> iterator = person.getEmailAddresses().iterator();
	while (iterator.hasNext()) {
	    emails.append(iterator.next().getValue());
	    if (iterator.hasNext()) {
		emails.append(",");
	    }
	}
	info.setEmail(emails.toString());
	info.setFiscalNumber(person.getSocialSecurityNumber());
	info.setIdentification(this.buildExternalIdentificationInfo(person));
	info.setName(person.getName());
	info.setNationality(person.getCountry().getCode());
	info.setPhone(person.getPhone());
	info.setSex(person.getGender().toString());
	info.setUsername(person.getUsername());
	return info;
    }

    private InfoExternalIdentificationInfo buildExternalIdentificationInfo(Person person) {
	InfoExternalIdentificationInfo info = new InfoExternalIdentificationInfo();
	info.setDocumentType(person.getIdDocumentType().toString());
	info.setNumber(person.getDocumentIdNumber());
	if (person.getEmissionDateOfDocumentId() != null) {
	    info.setEmitionDate(DateFormatUtil.format("yyyy-MM-dd", person.getEmissionDateOfDocumentId()));
	}
	if (person.getExpirationDateOfDocumentId() != null) {
	    info.setExpiryDate(DateFormatUtil.format("yyyy-MM-dd", person.getExpirationDateOfDocumentId()));
	}
	if (person.getEmissionLocationOfDocumentId() != null) {
	    info.setEmitionLocal(person.getEmissionLocationOfDocumentId());
	}

	return info;
    }

    private InfoExternalCitizenshipInfo builsExternalCitizenshipInfo(Person person) {
	InfoExternalCitizenshipInfo info = new InfoExternalCitizenshipInfo();
	info.setArea(person.getParishOfBirth());
	info.setCounty(person.getDistrictSubdivisionOfBirth());

	return info;
    }

    private InfoExternalAdressInfo buildInfoExternalAdressInfo(Person person) {
	InfoExternalAdressInfo info = new InfoExternalAdressInfo();

	if (person.hasDefaultPhysicalAddress()) {
	    final PhysicalAddress physicalAddress = person.getDefaultPhysicalAddress();
	    info.setPostalCode(physicalAddress.getAreaCode());
	    info.setStreet(physicalAddress.getAddress());
	    info.setTown(physicalAddress.getArea());
	}

	return info;
    }
}