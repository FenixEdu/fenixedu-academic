package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;

import org.apache.commons.beanutils.BeanComparator;

public class StudentCurriculumGroupBean extends StudentCurriculumModuleBean {

    private static class ComparatorByCurriculumGroupOrder implements
	    Comparator<StudentCurriculumGroupBean> {

	private ExecutionPeriod executionPeriod;

	public ComparatorByCurriculumGroupOrder(ExecutionPeriod executionPeriod) {
	    this.executionPeriod = executionPeriod;
	}

	public int compare(StudentCurriculumGroupBean o1, StudentCurriculumGroupBean o2) {
	    CurriculumGroup c1 = (CurriculumGroup) o1.getCurriculumModule();
	    CurriculumGroup c2 = (CurriculumGroup) o2.getCurriculumModule();
	    return c1.getChildOrder(executionPeriod).compareTo(c2.getChildOrder(executionPeriod));
	}

    }

    // Student Structure
    private List<StudentCurriculumEnrolmentBean> enrolledCurriculumCourses;

    private List<StudentCurriculumGroupBean> enrolledCurriculumGroups;

    // Curriculum Structure
    private List<IDegreeModuleToEvaluate> courseGroupsToEnrol;

    private List<IDegreeModuleToEvaluate> curricularCoursesToEnrol;

    public StudentCurriculumGroupBean(final CurriculumGroup curriculumGroup, final ExecutionPeriod executionPeriod, int[] curricularYears) {
	super(curriculumGroup);
	
	setCourseGroupsToEnrol(buildCourseGroupsToEnrol(curriculumGroup,
		executionPeriod));
	
	if (curricularYears != null) {
	    setCurricularCoursesToEnrol(buildCurricularCoursesToEnrol(curriculumGroup,
		    executionPeriod, curricularYears));
	} else {
	    setCurricularCoursesToEnrol(buildCurricularCoursesToEnrol(curriculumGroup,
		    executionPeriod));
	}
	
	setEnrolledCurriculumGroups(buildCurriculumGroupsEnroled(curriculumGroup,
		executionPeriod, curricularYears));
	setEnrolledCurriculumCourses(buildCurricularCoursesEnroled(curriculumGroup,
		executionPeriod));
	
    }


    protected List<StudentCurriculumGroupBean> buildCurriculumGroupsEnroled(
	    CurriculumGroup parentGroup, ExecutionPeriod executionPeriod, int[] curricularYears) {
	final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>();
	for (final CurriculumGroup curriculumGroup : parentGroup.getCurriculumGroups()) {
	    result.add(new StudentCurriculumGroupBean(curriculumGroup, executionPeriod,
		    curricularYears));
	}

	return result;
    }

    protected List<IDegreeModuleToEvaluate> buildCourseGroupsToEnrol(CurriculumGroup group,
	    ExecutionPeriod executionPeriod) {
	final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
	final List<Context> courseGroupContextsToEnrol = group
		.getCourseGroupContextsToEnrol(executionPeriod);

	for (final Context context : courseGroupContextsToEnrol) {
	    result.add(new DegreeModuleToEnrol(group, context, executionPeriod));

	}

	return result;
    }

    protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group,
	    ExecutionPeriod executionPeriod, int[] curricularYears) {
	final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
	final List<Context> curricularCoursesToEnrol = group
		.getCurricularCourseContextsToEnrol(executionPeriod);

	for (final Context context : curricularCoursesToEnrol) {
	    // NOTE: Temporary solution until first degree completes
	    final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
	    for (final int curricularYear : curricularYears) {
		if (context.containsSemesterAndCurricularYear(executionPeriod.getSemester(),
			curricularYear, curricularCourse.getRegime())) {
		    result.add(new DegreeModuleToEnrol(group, context, executionPeriod));
		    break;
		}
	    }
	}

	return result;
    }

    protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group,
	    ExecutionPeriod executionPeriod) {

	final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
	for (final Context context : group.getCurricularCourseContextsToEnrol(executionPeriod)) {
	    result.add(new DegreeModuleToEnrol(group, context, executionPeriod));
	}

	return result;

    }

    protected List<StudentCurriculumEnrolmentBean> buildCurricularCoursesEnroled(
	    CurriculumGroup group, ExecutionPeriod executionPeriod) {
	final List<StudentCurriculumEnrolmentBean> result = new ArrayList<StudentCurriculumEnrolmentBean>();

	for (final CurriculumLine curriculumLine : group.getCurriculumLines()) {
	    if (curriculumLine.isEnrolment()) {
		Enrolment enrolment = (Enrolment) curriculumLine;
		if (enrolment.getExecutionPeriod().equals(executionPeriod) && enrolment.isEnroled()) {
		    result.add(new StudentCurriculumEnrolmentBean((Enrolment) curriculumLine));
		}
	    }
	}

	return result;
    }

    @Override
    public CurriculumGroup getCurriculumModule() {
	return (CurriculumGroup) super.getCurriculumModule();
    }

    public List<IDegreeModuleToEvaluate> getCourseGroupsToEnrol() {
	return courseGroupsToEnrol;
    }

    public List<IDegreeModuleToEvaluate> getCourseGroupsToEnrolSortedByContext() {
	final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>(courseGroupsToEnrol);
	Collections.sort(result, new BeanComparator("context"));

	return result;
    }

    public void setCourseGroupsToEnrol(List<IDegreeModuleToEvaluate> courseGroupsToEnrol) {
	this.courseGroupsToEnrol = courseGroupsToEnrol;
    }

    public List<IDegreeModuleToEvaluate> getSortedDegreeModulesToEvaluate() {
	final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>(
		curricularCoursesToEnrol);
	Collections.sort(result, new BeanComparator("context"));

	return result;
    }

    public List<IDegreeModuleToEvaluate> getCurricularCoursesToEnrol() {
	return curricularCoursesToEnrol;
    }

    public void setCurricularCoursesToEnrol(List<IDegreeModuleToEvaluate> curricularCoursesToEnrol) {
	this.curricularCoursesToEnrol = curricularCoursesToEnrol;
    }

    public List<StudentCurriculumGroupBean> getEnrolledCurriculumGroups() {
	return enrolledCurriculumGroups;
    }

    public List<StudentCurriculumGroupBean> getEnrolledCurriculumGroupsSortedByOrder(
	    final ExecutionPeriod executionPeriod) {
	final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>(
		enrolledCurriculumGroups);
	Collections.sort(result, new ComparatorByCurriculumGroupOrder(executionPeriod));

	return result;
    }

    public boolean isEnrolledInAnyCurriculumGroups() {
	return !enrolledCurriculumGroups.isEmpty();
    }

    public void setEnrolledCurriculumGroups(List<StudentCurriculumGroupBean> enrolledCurriculumGroups) {
	this.enrolledCurriculumGroups = enrolledCurriculumGroups;
    }

    public List<StudentCurriculumEnrolmentBean> getEnrolledCurriculumCourses() {
	return enrolledCurriculumCourses;
    }

    public boolean isEnrolledInAnyCurriculumCourses() {
	return !enrolledCurriculumCourses.isEmpty();
    }

    public void setEnrolledCurriculumCourses(
	    List<StudentCurriculumEnrolmentBean> enrolledCurriculumCourses) {
	this.enrolledCurriculumCourses = enrolledCurriculumCourses;
    }

    public boolean isRoot() {
	return getCurriculumModule().isRoot();
    }
    
    public boolean isToBeDisabled() {
	return isRoot() || isEnrolledInAnyCurriculumCourses() || isEnrolledInAnyCurriculumGroups();
    }

}
