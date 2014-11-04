/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.student.Delegate;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.YearDelegate;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class DelegateInquiryTemplate extends DelegateInquiryTemplate_Base {

    public DelegateInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static DelegateInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof DelegateInquiryTemplate && inquiryTemplate.isOpen()) {
                return (DelegateInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static boolean hasYearDelegateInquiriesToAnswer(Student student) {
        DelegateInquiryTemplate currentTemplate = getCurrentTemplate();
        if (currentTemplate == null) {
            return false;
        }
        final ExecutionSemester executionSemester = currentTemplate.getExecutionPeriod();

        for (Delegate delegate : Delegate.getDelegates(student)) {
            if (delegate instanceof YearDelegate) {
                if (delegate.isActiveForFirstExecutionYear(executionSemester.getExecutionYear())) {
                    PersonFunction lastYearDelegatePersonFunction =
                            YearDelegate.getLastYearDelegatePersonFunctionByExecutionYearAndCurricularYear(delegate.getDegree()
                                    .getUnit(), executionSemester.getExecutionYear(), ((YearDelegate) delegate)
                                    .getCurricularYear());
                    if (lastYearDelegatePersonFunction.getDelegate() == delegate) {
                        if (hasInquiriesToAnswer(((YearDelegate) delegate), executionSemester)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasMandatoryCommentsToMake(YearDelegate yearDelegate, ExecutionCourse executionCourse,
            ExecutionDegree executionDegree) {
        Collection<InquiryResult> inquiryResults = executionCourse.getInquiryResultsSet();
        for (InquiryResult inquiryResult : inquiryResults) {
            if (inquiryResult.getResultClassification() != null
                    && (inquiryResult.getExecutionDegree() == executionDegree || inquiryResult.getExecutionDegree() == null)) {
                if (inquiryResult.getResultClassification().isMandatoryComment()
                        && !inquiryResult.getInquiryQuestion().isResultQuestion(executionCourse.getExecutionPeriod())) {
                    InquiryResultComment inquiryResultComment =
                            inquiryResult.getInquiryResultComment(yearDelegate.getPerson(), ResultPersonCategory.DELEGATE);
                    if (inquiryResultComment == null || StringUtils.isEmpty(inquiryResultComment.getComment())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasInquiriesToAnswer(YearDelegate yearDelegate, ExecutionSemester executionSemester) {
        if (yearDelegate.getInquiryDelegateAnswersSet().isEmpty()) {
            return true;
        }

        final ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(yearDelegate.getRegistration()
                        .getStudentCurricularPlan(executionSemester).getDegreeCurricularPlan(),
                        executionSemester.getExecutionYear());
        for (ExecutionCourse executionCourse : getExecutionCoursesToInquiries(yearDelegate, executionSemester, executionDegree)) {
            if (hasMandatoryCommentsToMake(yearDelegate, executionCourse, executionDegree)) {
                return true;
            }
        }
        return false;
    }

    public static DelegateInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof DelegateInquiryTemplate && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (DelegateInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static Set<ExecutionCourse> getExecutionCoursesToInquiries(YearDelegate yearDelegate,
            ExecutionSemester executionSemester, ExecutionDegree executionDegree) {

        final Set<ExecutionCourse> result = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        for (ExecutionCourse executionCourse : getDelegatedExecutionCourses(yearDelegate, executionSemester)) {
            if (InquiriesRoot.getAvailableForInquiries(executionCourse) && !executionCourse.getInquiryResultsSet().isEmpty()
                    && executionCourse.hasAnyEnrolment(executionDegree)) {
                result.add(executionCourse);
            }
        }

        addIfNecessaryExecutionCoursesFromOtherYears(yearDelegate, executionSemester, executionDegree, result);
        return result;
    }

    private static void addIfNecessaryExecutionCoursesFromOtherYears(YearDelegate yearDelegate,
            final ExecutionSemester executionSemester, ExecutionDegree executionDegree, final Set<ExecutionCourse> result) {
        final Degree degree = yearDelegate.getRegistration().getDegree();
        //final CycleType currentCycleType = getRegistration().getCurrentCycleType(); //TODO to pass EC to degree and master delegates
        final FunctionType functionType = getFunctionType(degree);
        final Student student = yearDelegate.getRegistration().getStudent();
        final PersonFunction degreeDelegateFunction =
                Delegate.getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student,
                        executionSemester.getExecutionYear(), functionType);

        if (degreeDelegateFunction != null) {
            addExecutionCoursesForOtherYears(yearDelegate, executionSemester, executionDegree, degree, student, result);
        }
    }

    private static void addExecutionCoursesForOtherYears(YearDelegate yearDelegate, ExecutionSemester executionPeriod,
            ExecutionDegree executionDegree, Degree degree, Student student, Set<ExecutionCourse> executionCoursesToInquiries) {
        List<YearDelegate> otherYearDelegates = new ArrayList<YearDelegate>();
        for (Student forStudent : Delegate.getAllActiveDelegatesByFunctionType(degree, FunctionType.DELEGATE_OF_YEAR, null)) {
            if (forStudent != student) {
                YearDelegate otherYearDelegate = null;
                for (Delegate delegate : Delegate.getDelegates(forStudent)) {
                    if (delegate instanceof YearDelegate) {
                        if (delegate.isActiveForFirstExecutionYear(executionPeriod.getExecutionYear())) {
                            if (otherYearDelegate == null
                                    || delegate.getDelegateFunction().getEndDate()
                                            .isAfter(otherYearDelegate.getDelegateFunction().getEndDate())) {
                                otherYearDelegate = (YearDelegate) delegate;
                            }
                        }
                    }
                }
                if (otherYearDelegate != null) {
                    otherYearDelegates.add(otherYearDelegate);
                }
            }
        }
        for (int iter = 1; iter <= degree.getDegreeType().getYears(); iter++) {
            YearDelegate yearDelegateForYear = getYearDelegate(otherYearDelegates, iter);
            if (yearDelegateForYear == null) {
                executionCoursesToInquiries.addAll(getExecutionCoursesToInquiries(yearDelegate, executionPeriod, executionDegree,
                        iter));
            }
        }
    }

    private static YearDelegate getYearDelegate(List<YearDelegate> otherYearDelegates, int year) {
        for (YearDelegate yearDelegate : otherYearDelegates) {
            if (yearDelegate.getCurricularYear().getYear() == year) {
                return yearDelegate;
            }
        }
        return null;
    }

    private static List<ExecutionCourse> getExecutionCoursesToInquiries(YearDelegate yearDelegate,
            final ExecutionSemester executionSemester, final ExecutionDegree executionDegree, Integer curricularYear) {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (ExecutionCourse executionCourse : yearDelegate.getDegree().getExecutionCourses(curricularYear, executionSemester)) {
            if (InquiriesRoot.getAvailableForInquiries(executionCourse) && executionCourse.hasAnyEnrolment(executionDegree)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    private static FunctionType getFunctionType(Degree degree) {
        switch (degree.getDegreeType()) {
        case BOLONHA_DEGREE:
            return FunctionType.DELEGATE_OF_DEGREE;
        case BOLONHA_MASTER_DEGREE:
            return FunctionType.DELEGATE_OF_MASTER_DEGREE;
        case BOLONHA_INTEGRATED_MASTER_DEGREE:
            //      degree.getDegreeType().getYears(CycleType.FIRST_CYCLE); //TODO pass to degree delegate
            //      degree.getDegreeType().getYears(CycleType.SECOND_CYCLE); //TODO pass to master degree delegate
            return FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE;
        default:
            return null;
        }
    }

    private static Collection<ExecutionCourse> getDelegatedExecutionCourses(YearDelegate yearDelegate,
            ExecutionSemester executionSemester) {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final DegreeCurricularPlan degreeCurricularPlan : yearDelegate.getDegree().getDegreeCurricularPlansSet()) {
            for (final CurricularCourse course : degreeCurricularPlan.getCurricularCoursesSet()) {
                for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
                    if (executionSemester == executionCourse.getExecutionPeriod()) {
                        for (final DegreeModuleScope scope : course.getDegreeModuleScopes()) {
                            if (scope.isActiveForExecutionPeriod(executionSemester)
                                    && scope.getCurricularYear() == yearDelegate.getCurricularYear().getYear()) {
                                if (scope.getCurricularSemester() == executionSemester.getSemester()) {
                                    result.add(executionCourse);
                                    break;
                                } else
                                //even if it hasn't an active scope in one of the curricular semesters, 
                                //it must appear to the delegate since it's an annual course 
                                if (course.isAnual(executionSemester.getExecutionYear())) {
                                    result.add(executionCourse);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
