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
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.Substitution;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz </a>
 * 
 *         Created at 2:40:27 PM, Mar 11, 2005
 */
public class ReadStudentExternalInformation {

    @Service
    public static Collection run(String username) throws FenixServiceException {
        Person person = Person.readPersonByUsername(username);

        if (!person.getStudent().hasAnyActiveRegistration()) {
            throw new FenixServiceException("N�o tem nenhuma matr�cula activa");
        }

        if (person.getStudent().getMostSignificantDegreeType().isBolonhaType()) {
            return getResultForBolonha(person);
        } else {
            return getResultForPreBolonha(person);
        }

    }

    private static Collection getResultForBolonha(Person person) {
        Collection result = new ArrayList();
        Collection<CycleCurriculumGroup> studentCycles = getStudentCycles(person.getStudent());

        InfoStudentExternalInformation info = new InfoStudentExternalInformation();
        info.setPerson(buildExternalPersonInfo(person));
        info.setNumber(person.getStudent().getNumber().toString());

        info.setDegree(buildExternalDegreeCurricularPlanInfoBolonha(studentCycles));

        setCoursesInformation(info, studentCycles);

        info.setCurricularYear(getCurriculumYear(studentCycles));

        final double average = getDegreeAverage(studentCycles);
        info.setAverage(average);
        result.add(info);
        return result;
    }

    private static void setCoursesInformation(InfoStudentExternalInformation info, Collection<CycleCurriculumGroup> studentCycles) {
        for (CycleCurriculumGroup cycleCurriculumGroup : studentCycles) {
            for (CurriculumLine curriculumLine : cycleCurriculumGroup.getApprovedCurriculumLines()) {
                if (curriculumLine.getDegreeModule() != null) {
                    addExternalEnrolmentInfoBolonha(info, curriculumLine, cycleCurriculumGroup);
                    if (curriculumLine.isEnrolment()) {
                        Enrolment enrolment = (Enrolment) curriculumLine;
                        if (enrolment.isOptional()) {
                            if (enrolment instanceof OptionalEnrolment) {
                                addCurricularCourse(info.getDegree(), enrolment.getCurricularCourse(), cycleCurriculumGroup);
                            } else {
                                addCurricularCourse(info.getDegree(),
                                        ((OptionalEnrolment) enrolment).getOptionalCurricularCourse(), cycleCurriculumGroup);
                            }
                        } else {
                            addCurricularCourse(info.getDegree(), curriculumLine.getCurricularCourse(), cycleCurriculumGroup);
                        }
                    } else {
                        addCurricularCourse(info.getDegree(), curriculumLine.getCurricularCourse(), cycleCurriculumGroup);
                    }
                }
            }

            for (CurricularCourse curricularCourse : getStudentRemainingDegree(cycleCurriculumGroup)) {
                addCurricularCourse(info.getDegree(), curricularCourse, cycleCurriculumGroup);
                addAvailableRemainingCoursesBolonha(info, curricularCourse, cycleCurriculumGroup);
            }
        }
    }

    private static Set<CurricularCourse> getStudentRemainingDegree(CycleCurriculumGroup cycleCurriculumGroup) {
        return getRemainingCourses(cycleCurriculumGroup.getDegreeModule(), cycleCurriculumGroup);
    }

    private static Set<CurricularCourse> getRemainingCourses(CourseGroup courseGroup, CycleCurriculumGroup cycleCurriculumGroup) {
        if (isClosed(courseGroup, cycleCurriculumGroup)) {
            return Collections.emptySet();
        }

        Set<CurricularCourse> res = getNotAprovedCourses(courseGroup, cycleCurriculumGroup);
        for (CourseGroup group : getChildCourseGroups(courseGroup, cycleCurriculumGroup)) {
            res.addAll(getRemainingCourses(group, cycleCurriculumGroup));
        }
        return res;
    }

    private static Set<CourseGroup> getChildCourseGroups(CourseGroup courseGroup, CycleCurriculumGroup cycleCurriculumGroup) {
        CurriculumGroup groupFor = cycleCurriculumGroup.findCurriculumGroupFor(courseGroup);

        if (groupFor != null) {
            ICurricularRule degreeModulesRule =
                    courseGroup.getMostRecentActiveCurricularRule(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT, null,
                            ExecutionYear.readCurrentExecutionYear());
            if (degreeModulesRule != null) {
                DegreeModulesSelectionLimit degreeModulesSelectionLimit = (DegreeModulesSelectionLimit) degreeModulesRule;
                if (groupFor.getCurriculumModulesCount() >= degreeModulesSelectionLimit.getMaximumLimit()) {
                    Set<CourseGroup> res = new HashSet<CourseGroup>();
                    for (CurriculumModule curriculumModule : groupFor.getCurriculumModules()) {
                        if (!curriculumModule.isLeaf()) {
                            res.add((CourseGroup) curriculumModule.getDegreeModule());
                        }
                    }
                }
            }
        }

        Set<CourseGroup> res = new HashSet<CourseGroup>();
        for (Context context : courseGroup.getOpenChildContexts(CourseGroup.class, ExecutionYear.readCurrentExecutionYear())) {
            res.add((CourseGroup) context.getChildDegreeModule());
        }
        return res;
    }

    private static boolean isClosed(CourseGroup courseGroup, CycleCurriculumGroup cycleCurriculumGroup) {
        CurriculumGroup groupFor = cycleCurriculumGroup.findCurriculumGroupFor(courseGroup);
        if (groupFor == null) {
            return false;
        }

        ICurricularRule creditsLimitRule =
                courseGroup.getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT, null,
                        ExecutionYear.readCurrentExecutionYear());
        if (creditsLimitRule != null) {
            CreditsLimit creditsLimit = (CreditsLimit) creditsLimitRule;
            if (groupFor.getCreditsConcluded() >= creditsLimit.getMaximumCredits()) {
                return true;
            }
        }

        return false;
    }

    private static Set<CurricularCourse> getNotAprovedCourses(CourseGroup courseGroup, CycleCurriculumGroup cycleCurriculumGroup) {
        Set<CurricularCourse> res = new HashSet<CurricularCourse>();
        for (Context context : courseGroup.getOpenChildContexts(CurricularCourse.class, ExecutionYear.readCurrentExecutionYear())) {
            CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
            if (!cycleCurriculumGroup.isApproved(curricularCourse)) {
                res.add(curricularCourse);
            }
        }
        return res;
    }

    private static CurriculumLine getAprovedCurriculumLine(CycleCurriculumGroup cycleCurriculumGroup,
            CurricularCourse curricularCourse) {
        return cycleCurriculumGroup.getApprovedCurriculumLine(curricularCourse);
    }

    private static void addAvailableRemainingCoursesBolonha(InfoStudentExternalInformation info,
            CurricularCourse curricularCourse, CycleCurriculumGroup cycleCurriculumGroup) {
        if (!curricularCourse.isOptional()) {
            final InfoExternalCurricularCourseInfo infoExternalCurricularCourseInfo =
                    InfoExternalCurricularCourseInfo.newFromDomain(curricularCourse);
            infoExternalCurricularCourseInfo.setName("" + curricularCourse.getIdInternal() + " " + curricularCourse.getName());
            if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE
                    && cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
                infoExternalCurricularCourseInfo.setCurricularYear((Integer.valueOf(infoExternalCurricularCourseInfo
                        .getCurricularYear()) + 3) + "");
            }
            info.getAvailableRemainingCourses().add(infoExternalCurricularCourseInfo);
        }
    }

    private static void addExternalEnrolmentInfoBolonha(InfoStudentExternalInformation infoStudentExternalInformation,
            CurriculumLine curriculumLine, CycleCurriculumGroup cycleCurriculumGroup) {
        if (curriculumLine.isEnrolment()) {
            Enrolment enrolment = (Enrolment) curriculumLine;

            InfoExternalEnrollmentInfo info = null;
            if (enrolment.isOptional()) {
                info = InfoExternalEnrollmentInfo.newFromOptionalEnrollment((OptionalEnrolment) enrolment);
            } else {
                info = InfoExternalEnrollmentInfo.newFromEnrollment(enrolment);
            }

            info.setGrade(enrolment.getGrade());
            if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE
                    && cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
                info.getCourse().setCurricularYear((Integer.valueOf(info.getCourse().getCurricularYear()) + 3) + "");
            }
            infoStudentExternalInformation.getCourses().add(info);
        } else if (curriculumLine.isDismissal()) {
            Dismissal dismissal = (Dismissal) curriculumLine;
            if (dismissal.isCreditsDismissal()) {
                return;
            }
            if (dismissal.getCredits().isEquivalence()) {
                InfoExternalEnrollmentInfo info =
                        InfoExternalEnrollmentInfo.newFromCurricularCourse(dismissal.getCurricularCourse(), dismissal
                                .getCredits().getGrade());
                if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE
                        && cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
                    info.getCourse().setCurricularYear((Integer.valueOf(info.getCourse().getCurricularYear()) + 3) + "");
                }
                infoStudentExternalInformation.getCourses().add(info);
            } else if (dismissal.getCredits().isSubstitution() && !dismissal.getSourceIEnrolments().isEmpty()) {
                Grade grade = getAverage((Substitution) dismissal.getCredits());
                if (!grade.isEmpty()) {
                    InfoExternalEnrollmentInfo info =
                            InfoExternalEnrollmentInfo.newFromCurricularCourse(dismissal.getCurricularCourse(), grade);
                    if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE
                            && cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
                        info.getCourse().setCurricularYear((Integer.valueOf(info.getCourse().getCurricularYear()) + 3) + "");
                    }
                    infoStudentExternalInformation.getCourses().add(info);
                }
            }
        }
    }

    private static void addCurricularCourse(InfoExternalDegreeCurricularPlanInfo info, CurricularCourse curricularCourse,
            CycleCurriculumGroup cycleCurriculumGroup) {
        InfoExternalCurricularCourseInfo infoExternalCurricularCourseInfo =
                InfoExternalCurricularCourseInfo.newFromDomain(curricularCourse);
        if (cycleCurriculumGroup.getCycleType() == CycleType.SECOND_CYCLE
                && cycleCurriculumGroup.getStudentCurricularPlan().getDegreeCurricularPlan().getDegreeType() == DegreeType.BOLONHA_MASTER_DEGREE) {
            final int curricularYear = Integer.parseInt(infoExternalCurricularCourseInfo.getCurricularYear()) + 3;
            infoExternalCurricularCourseInfo.setCurricularYear(Integer.toString(curricularYear));
        }
        info.addCourse(infoExternalCurricularCourseInfo);
    }

    private static int getCurriculumYear(Collection<CycleCurriculumGroup> studentCycles) {
        CycleCurriculumGroup cycleCurriculumGroup = Collections.max(studentCycles, new BeanComparator("cycleType"));
        Integer curricularYear = cycleCurriculumGroup.getCurriculum().getCurricularYear();
        return cycleCurriculumGroup.getCycleType() == CycleType.FIRST_CYCLE ? curricularYear : (curricularYear += 3);
    }

    private static double getDegreeAverage(Collection<CycleCurriculumGroup> studentCycles) {
        Iterator<CycleCurriculumGroup> iterator = studentCycles.iterator();
        Curriculum curriculum = iterator.next().getCurriculum();
        while (iterator.hasNext()) {
            curriculum.add(iterator.next().getCurriculum());
        }
        return curriculum.getAverage().doubleValue();
    }

    private static Collection getResultForPreBolonha(Person person) {
        Collection result = new ArrayList();
        Collection students = person.getStudents();
        for (Iterator iter = students.iterator(); iter.hasNext();) {
            InfoStudentExternalInformation info = new InfoStudentExternalInformation();
            Registration registration = (Registration) iter.next();
            if (registration.getActiveState().getStateType() != RegistrationStateType.CANCELED
                    && registration.getActiveStudentCurricularPlan() != null) {

                info.setPerson(buildExternalPersonInfo(person));

                info.setDegree(buildExternalDegreeCurricularPlanInfo(registration));
                info.setCourses(buildExternalEnrollmentsInfo(registration));
                info.setAvailableRemainingCourses(buildAvailableRemainingCourses(registration));
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

    private static Collection<CycleCurriculumGroup> getStudentCycles(Student student) {
        Registration activeRegistration = getActiveRegistrationForBolonha(student);
        if (activeRegistration.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)) {
            return Collections.singleton(activeRegistration.getActiveStudentCurricularPlan().getRoot()
                    .getCycleCurriculumGroup(CycleType.FIRST_CYCLE));
        } else if (activeRegistration.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
            Set<CycleCurriculumGroup> groups = new HashSet<CycleCurriculumGroup>();
            CycleType currentCycleType = activeRegistration.getCurrentCycleType();
            if (currentCycleType == CycleType.FIRST_CYCLE) {
                groups.add(activeRegistration.getActiveStudentCurricularPlan().getRoot()
                        .getCycleCurriculumGroup(CycleType.FIRST_CYCLE));

                CycleCurriculumGroup cycleCurriculumGroup =
                        activeRegistration.getActiveStudentCurricularPlan().getRoot()
                                .getCycleCurriculumGroup(CycleType.SECOND_CYCLE);

                if (cycleCurriculumGroup != null) {
                    groups.add(cycleCurriculumGroup);
                }

            } else {
                groups.add(activeRegistration.getActiveStudentCurricularPlan().getRoot()
                        .getCycleCurriculumGroup(CycleType.SECOND_CYCLE));

                CycleCurriculumGroup cycleCurriculumGroup =
                        activeRegistration.getActiveStudentCurricularPlan().getRoot()
                                .getCycleCurriculumGroup(CycleType.FIRST_CYCLE);

                if (cycleCurriculumGroup == null) {
                    cycleCurriculumGroup = getConcludedFirstCycle(student);
                }

                if (cycleCurriculumGroup != null) {
                    groups.add(cycleCurriculumGroup);
                }
            }
            return groups;
        } else {
            Set<CycleCurriculumGroup> groups = new HashSet<CycleCurriculumGroup>();
            groups.add(activeRegistration.getActiveStudentCurricularPlan().getRoot()
                    .getCycleCurriculumGroup(CycleType.SECOND_CYCLE));

            CycleCurriculumGroup cycleCurriculumGroup = getConcludedFirstCycle(student);
            if (cycleCurriculumGroup != null) {
                groups.add(cycleCurriculumGroup);
            }
            return groups;
        }
    }

    private static CycleCurriculumGroup getConcludedFirstCycle(Student student) {
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

    private static Registration getActiveRegistrationForBolonha(Student student) {
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

    private static Collection buildAvailableRemainingCourses(final Registration registration) {
        final Collection<CurricularCourse> allCourses =
                registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan().getCurricularCourses();
        final Collection<InfoExternalCurricularCourseInfo> availableInfos = new ArrayList<InfoExternalCurricularCourseInfo>();
        for (final CurricularCourse curricularCourse : allCourses) {
            if (hasActiveScope(curricularCourse) && studentIsNotApprovedInCurricularCourse(registration, curricularCourse)) {
                final InfoExternalCurricularCourseInfo infoExternalCurricularCourseInfo =
                        InfoExternalCurricularCourseInfo.newFromDomain(curricularCourse);
                infoExternalCurricularCourseInfo
                        .setName("" + curricularCourse.getIdInternal() + " " + curricularCourse.getName());
                availableInfos.add(infoExternalCurricularCourseInfo);
            }
        }
        return availableInfos;
    }

    private static boolean studentIsNotApprovedInCurricularCourse(final Registration registration,
            final CurricularCourse curricularCourse) {
        for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.isApproved(curricularCourse)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasActiveScope(final CurricularCourse curricularCourse) {
        for (final DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
            if (degreeModuleScope.isActive()) {
                return true;
            }
        }
        return false;
    }

    private static Collection buildExternalEnrollmentsInfo(Registration registration) {
        Collection enrollments = new ArrayList();
        Collection curricularPlans = registration.getStudentCurricularPlans();
        for (Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
            StudentCurricularPlan curricularPlan = (StudentCurricularPlan) iter.next();
            for (Object element : curricularPlan.getEnrolments()) {
                Enrolment enrollment = (Enrolment) element;
                if (enrollment.isEnrolmentStateApproved()) {
                    CurricularCourse curricularCourse = enrollment.getCurricularCourse();
                    if (curricularCourse.getEctsCredits().doubleValue() == 0) {
                        Collection<DegreeCurricularPlan> degrees =
                                curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeCurricularPlans();
                        for (DegreeCurricularPlan degree : degrees) {
                            CurricularCourse curricularCourseWithSameCode =
                                    degree.getCurricularCourseByCode(curricularCourse.getCode());
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
                        DegreeCurricularPlan studentDegreeCurricularPlan =
                                registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan();
                        List<CurricularCourse> curricularCourses =
                                enrollment.getCurricularCourse().getCompetenceCourse().getAssociatedCurricularCourses();
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

    private static Grade getAverage(Substitution substitution) {
        if (substitution.getEnrolments().size() == 1) {
            return substitution.getEnrolments().get(0).getIEnrolment().getGrade();
        }

        BigDecimal gradeWeigth = BigDecimal.ZERO;
        BigDecimal weigth = BigDecimal.ZERO;
        for (EnrolmentWrapper enrolmentWrapper : substitution.getEnrolments()) {
            Grade grade = enrolmentWrapper.getIEnrolment().getGrade();
            if (grade.getGradeScale() == GradeScale.TYPEAP && grade.isApproved()) {
                return grade;
            }
            gradeWeigth = gradeWeigth.add(enrolmentWrapper.getIEnrolment().getWeigthTimesGrade());
            weigth = weigth.add(enrolmentWrapper.getIEnrolment().getWeigthForCurriculum());
        }

        if (weigth.compareTo(BigDecimal.ZERO) == 0) {
            gradeWeigth = BigDecimal.ZERO;
            for (EnrolmentWrapper enrolmentWrapper : substitution.getEnrolments()) {
                Grade grade = enrolmentWrapper.getIEnrolment().getGrade();
                if (grade.getGradeScale() == GradeScale.TYPEAP && grade.isApproved()) {
                    return grade;
                }
                gradeWeigth =
                        gradeWeigth.add(BigDecimal.valueOf(Double.valueOf(enrolmentWrapper.getIEnrolment().getGradeValue())));
            }
            weigth = new BigDecimal(substitution.getEnrolmentsCount());
        }

        BigDecimal res = gradeWeigth.divide(weigth, 0, RoundingMode.HALF_UP);

        return Grade.createGrade(res.toString(), GradeScale.TYPE20);

    }

    private static InfoExternalDegreeCurricularPlanInfo buildExternalDegreeCurricularPlanInfo(Registration registration) {
        InfoExternalDegreeCurricularPlanInfo info = new InfoExternalDegreeCurricularPlanInfo();
        DegreeCurricularPlan degreeCurricularPlan = registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan();

        info.setName(degreeCurricularPlan.getDegree().getName());
        info.setCode(degreeCurricularPlan.getDegree().getSigla());
        info.setBranch(buildExternalDegreeBranchInfo(registration));

        Collection courses = registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan().getCurricularCourses();
        for (Iterator iter = courses.iterator(); iter.hasNext();) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();
            if (curricularCourse.getEctsCredits().doubleValue() == 0) {
                Collection<DegreeCurricularPlan> degrees =
                        curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeCurricularPlans();
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

    private static InfoExternalDegreeCurricularPlanInfo buildExternalDegreeCurricularPlanInfoBolonha(
            Collection<CycleCurriculumGroup> studentCycles) {
        InfoExternalDegreeCurricularPlanInfo info = new InfoExternalDegreeCurricularPlanInfo();
        DegreeCurricularPlan degreeCurricularPlan = getCycleDegree(studentCycles);

        info.setName(degreeCurricularPlan.getDegree().getName());
        info.setCode(degreeCurricularPlan.getDegree().getSigla());
        info.setBranch(null);

        return info;
    }

    private static DegreeCurricularPlan getCycleDegree(Collection<CycleCurriculumGroup> studentCycles) {
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

    private static InfoExternalDegreeBranchInfo buildExternalDegreeBranchInfo(Registration registration) {
        InfoExternalDegreeBranchInfo info = new InfoExternalDegreeBranchInfo();
        if (registration.getActiveStudentCurricularPlan().getBranch() != null) {
            info.setName(registration.getActiveStudentCurricularPlan().getBranch().getName());
            info.setCode(registration.getActiveStudentCurricularPlan().getBranch().getCode());
        }

        return info;
    }

    private static InfoExternalPersonInfo buildExternalPersonInfo(Person person) {
        InfoExternalPersonInfo info = new InfoExternalPersonInfo();
        info.setAddress(buildInfoExternalAdressInfo(person));
        info.setBirthday(DateFormatUtil.format("yyyy-MM-dd", person.getDateOfBirth()));
        info.setCelularPhone(person.getDefaultMobilePhoneNumber());
        info.setCitizenship(builsExternalCitizenshipInfo(person));
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
        info.setIdentification(buildExternalIdentificationInfo(person));
        info.setName(person.getName());
        info.setNationality(person.getCountry().getCode());
        info.setPhone(person.getDefaultPhoneNumber());
        info.setSex(person.getGender().toString());
        info.setUsername(person.getUsername());
        return info;
    }

    private static InfoExternalIdentificationInfo buildExternalIdentificationInfo(Person person) {
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

    private static InfoExternalCitizenshipInfo builsExternalCitizenshipInfo(Person person) {
        InfoExternalCitizenshipInfo info = new InfoExternalCitizenshipInfo();
        info.setArea(person.getParishOfBirth());
        info.setCounty(person.getDistrictSubdivisionOfBirth());

        return info;
    }

    private static InfoExternalAdressInfo buildInfoExternalAdressInfo(Person person) {
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