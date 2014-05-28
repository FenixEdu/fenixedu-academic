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
package net.sourceforge.fenixedu.domain.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.apache.commons.lang.StringUtils;

public class YearDelegate extends YearDelegate_Base {

    public YearDelegate() {
        super();
    }

    public YearDelegate(Registration registration, PersonFunction delegateFunction) {
        this();
        setRegistration(registration);
        setDelegateFunction(delegateFunction);
    }

    public CurricularYear getCurricularYear() {
        return getDelegateFunction().getCurricularYear();
    }

    public Collection<ExecutionCourse> getDelegatedExecutionCourses(final ExecutionSemester executionSemester) {
        return getDegree().getExecutionCourses(getCurricularYear(), executionSemester);
    }

    public boolean hasInquiriesToAnswer(final ExecutionSemester executionSemester) {
        if (getInquiryDelegateAnswersSet().isEmpty()) {
            return true;
        }

        final ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(
                        this.getRegistration().getStudentCurricularPlan(executionSemester).getDegreeCurricularPlan(),
                        executionSemester.getExecutionYear());
        for (ExecutionCourse executionCourse : getExecutionCoursesToInquiries(executionSemester, executionDegree)) {
            if (hasMandatoryCommentsToMake(executionCourse, executionDegree)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMandatoryCommentsToMake(ExecutionCourse executionCourse, ExecutionDegree executionDegree) {
        Collection<InquiryResult> inquiryResults = executionCourse.getInquiryResults();
        for (InquiryResult inquiryResult : inquiryResults) {
            if (inquiryResult.getResultClassification() != null
                    && (inquiryResult.getExecutionDegree() == executionDegree || inquiryResult.getExecutionDegree() == null)) {
                if (inquiryResult.getResultClassification().isMandatoryComment()
                        && !inquiryResult.getInquiryQuestion().isResultQuestion(executionCourse.getExecutionPeriod())) {
                    InquiryResultComment inquiryResultComment =
                            inquiryResult.getInquiryResultComment(getPerson(), ResultPersonCategory.DELEGATE);
                    if (inquiryResultComment == null || StringUtils.isEmpty(inquiryResultComment.getComment())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Set<ExecutionCourse> getExecutionCoursesToInquiries(final ExecutionSemester executionSemester,
            ExecutionDegree executionDegree) {

        final Set<ExecutionCourse> result = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        for (ExecutionCourse executionCourse : getDelegatedExecutionCourses(executionSemester)) {
            if (executionCourse.getAvailableForInquiries() && executionCourse.hasAnyInquiryResults()
                    && executionCourse.hasAnyEnrolment(executionDegree)) {
                result.add(executionCourse);
            }
        }

        addIfNecessaryExecutionCoursesFromOtherYears(executionSemester, executionDegree, result);
        return result;
    }

    private void addIfNecessaryExecutionCoursesFromOtherYears(final ExecutionSemester executionSemester,
            ExecutionDegree executionDegree, final Set<ExecutionCourse> result) {
        final Degree degree = getRegistration().getDegree();
        //final CycleType currentCycleType = getRegistration().getCurrentCycleType(); //TODO to pass EC to degree and master delegates
        final FunctionType functionType = getFunctionType(degree);
        final Student student = getRegistration().getStudent();
        final PersonFunction degreeDelegateFunction =
                degree.getActiveDelegatePersonFunctionByStudentAndFunctionType(student, executionSemester.getExecutionYear(),
                        functionType);

        if (degreeDelegateFunction != null) {
            addExecutionCoursesForOtherYears(executionSemester, executionDegree, degree, student, result);
        }
    }

    private void addExecutionCoursesForOtherYears(ExecutionSemester executionPeriod, ExecutionDegree executionDegree,
            Degree degree, Student student, Set<ExecutionCourse> executionCoursesToInquiries) {
        List<YearDelegate> otherYearDelegates = new ArrayList<YearDelegate>();
        for (Student forStudent : degree.getAllActiveYearDelegates()) {
            if (forStudent != student) {
                YearDelegate otherYearDelegate = null;
                for (Delegate delegate : forStudent.getDelegates()) {
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
                executionCoursesToInquiries.addAll(getExecutionCoursesToInquiries(executionPeriod, executionDegree, iter));
            }
        }
    }

    private YearDelegate getYearDelegate(List<YearDelegate> otherYearDelegates, int year) {
        for (YearDelegate yearDelegate : otherYearDelegates) {
            if (yearDelegate.getCurricularYear().getYear() == year) {
                return yearDelegate;
            }
        }
        return null;
    }

    private FunctionType getFunctionType(Degree degree) {
        switch (degree.getDegreeType()) {
        case BOLONHA_DEGREE:
            return FunctionType.DELEGATE_OF_DEGREE;
        case BOLONHA_MASTER_DEGREE:
            return FunctionType.DELEGATE_OF_MASTER_DEGREE;
        case BOLONHA_INTEGRATED_MASTER_DEGREE:
            //	    degree.getDegreeType().getYears(CycleType.FIRST_CYCLE); //TODO pass to degree delegate
            //	    degree.getDegreeType().getYears(CycleType.SECOND_CYCLE); //TODO pass to master degree delegate
            return FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE;
        default:
            return null;
        }
    }

    private List<ExecutionCourse> getExecutionCoursesToInquiries(final ExecutionSemester executionSemester,
            final ExecutionDegree executionDegree, Integer curricularYear) {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (ExecutionCourse executionCourse : getDegree().getExecutionCourses(curricularYear, executionSemester)) {
            if (executionCourse.getAvailableForInquiries() && executionCourse.hasAnyEnrolment(executionDegree)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public Collection<ExecutionCourse> getAnsweredInquiriesExecutionCourses(final ExecutionSemester executionSemester) {
        final Set<ExecutionCourse> result = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        for (YearDelegateCourseInquiry yearDelegateCourseInquiry : getYearDelegateCourseInquiries()) {
            final ExecutionCourse executionCourse = yearDelegateCourseInquiry.getExecutionCourse();
            if (executionCourse.getExecutionPeriod() == executionSemester) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public Collection<ExecutionCourse> getNotAnsweredInquiriesExecutionCourses(final ExecutionSemester executionSemester) {
        final Set<ExecutionCourse> result = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        final Collection<ExecutionCourse> answeredInquiriesExecutionCourses =
                getAnsweredInquiriesExecutionCourses(executionSemester);
        for (ExecutionCourse executionCourse : getDelegatedExecutionCourses(executionSemester)) {
            if (executionCourse.isAvailableForInquiry() && !answeredInquiriesExecutionCourses.contains(executionCourse)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public Person getPerson() {
        return getRegistration().getPerson();
    }

    public boolean isAfter(YearDelegate yearDelegate) {
        return getDelegateFunction().getEndDate().isAfter(yearDelegate.getDelegateFunction().getEndDate());
    }

    @Override
    public Degree getDegree() {
        return super.getDegree();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry> getYearDelegateCourseInquiries() {
        return getYearDelegateCourseInquiriesSet();
    }

    @Deprecated
    public boolean hasAnyYearDelegateCourseInquiries() {
        return !getYearDelegateCourseInquiriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer> getInquiryDelegateAnswers() {
        return getInquiryDelegateAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryDelegateAnswers() {
        return !getInquiryDelegateAnswersSet().isEmpty();
    }

}
