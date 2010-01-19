package net.sourceforge.fenixedu.domain.student;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

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
	final Collection<ExecutionCourse> answeredInquiriesExecutionCourses = getAnsweredInquiriesExecutionCourses(executionSemester);
	for (ExecutionCourse executionCourse : getDelegatedExecutionCourses(executionSemester)) {
	    if (executionCourse.getAvailableForInquiries() && executionCourse.hasAnyAttends()
		    && !answeredInquiriesExecutionCourses.contains(executionCourse)) {
		result.add(executionCourse);
	    }
	}
	return result;

    }

}
