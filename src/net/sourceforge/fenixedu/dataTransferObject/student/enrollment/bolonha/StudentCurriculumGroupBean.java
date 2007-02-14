package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
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
    private List<DegreeModuleToEnrol> courseGroupsToEnrol;

    private List<DegreeModuleToEnrol> curricularCoursesToEnrol;

    private StudentCurriculumGroupBean(final CurriculumGroup curriculumGroup) {
	super(curriculumGroup);
	this.enrolledCurriculumCourses = new ArrayList<StudentCurriculumEnrolmentBean>();
	this.enrolledCurriculumGroups = new ArrayList<StudentCurriculumGroupBean>();
	this.courseGroupsToEnrol = new ArrayList<DegreeModuleToEnrol>();
	this.curricularCoursesToEnrol = new ArrayList<DegreeModuleToEnrol>();
    }

    public static StudentCurriculumGroupBean create(final CurriculumGroup group,
	    final ExecutionPeriod executionPeriod) {
	final StudentCurriculumGroupBean studentCurriculumGroupBean = new StudentCurriculumGroupBean(
		group);

	studentCurriculumGroupBean.setCourseGroupsToEnrol(buildCourseGroupsToEnrol(group,
		executionPeriod));
	studentCurriculumGroupBean.setCurricularCoursesToEnrol(buildCurricularCoursesToEnrol(group,
		executionPeriod));
	studentCurriculumGroupBean.setEnrolledCurriculumGroups(buildCurriculumGroupsEnroled(group,
		executionPeriod));
	studentCurriculumGroupBean.setEnrolledCurriculumCourses(buildCurricularCoursesEnroled(group,
		executionPeriod));

	return studentCurriculumGroupBean;

    }

    private static List<StudentCurriculumGroupBean> buildCurriculumGroupsEnroled(
	    CurriculumGroup parentGroup, ExecutionPeriod executionPeriod) {
	final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>();
	for (final CurriculumGroup curriculumGroup : parentGroup.getCurriculumGroups()) {
	    result.add(StudentCurriculumGroupBean.create(curriculumGroup, executionPeriod));
	}

	return result;
    }

    private static List<DegreeModuleToEnrol> buildCourseGroupsToEnrol(CurriculumGroup group,
	    ExecutionPeriod executionPeriod) {
	final List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>();
	final List<Context> courseGroupContextsToEnrol = group
		.getCourseGroupContextsToEnrol(executionPeriod);

	for (final Context context : courseGroupContextsToEnrol) {
	    result.add(new DegreeModuleToEnrol(group, context));
	}

	return result;
    }

    private static List<DegreeModuleToEnrol> buildCurricularCoursesToEnrol(CurriculumGroup group,
	    ExecutionPeriod executionPeriod) {
	final List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>();
	final List<Context> curricularCoursesToEnrol = group
		.getCurricularCourseContextsToEnrol(executionPeriod);

	for (final Context context : curricularCoursesToEnrol) {
	    result.add(new DegreeModuleToEnrol(group, context));

	}

	return result;
    }

    private static List<StudentCurriculumEnrolmentBean> buildCurricularCoursesEnroled(
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

    public List<DegreeModuleToEnrol> getCourseGroupsToEnrol() {
	return courseGroupsToEnrol;
    }

    public List<DegreeModuleToEnrol> getCourseGroupsToEnrolSortedByContext() {
	final List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>(courseGroupsToEnrol);
	Collections.sort(result, new BeanComparator("context"));

	return result;
    }

    public void setCourseGroupsToEnrol(List<DegreeModuleToEnrol> courseGroupsToEnrol) {
	this.courseGroupsToEnrol = courseGroupsToEnrol;
    }

    public List<DegreeModuleToEnrol> getCurricularCoursesToEnrolSortedByContext() {
	final List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>(
		curricularCoursesToEnrol);
	Collections.sort(result, new BeanComparator("context"));

	return result;
    }

    public List<DegreeModuleToEnrol> getCurricularCoursesToEnrol() {
	return curricularCoursesToEnrol;
    }

    public void setCurricularCoursesToEnrol(List<DegreeModuleToEnrol> curricularCoursesToEnrol) {
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

}
