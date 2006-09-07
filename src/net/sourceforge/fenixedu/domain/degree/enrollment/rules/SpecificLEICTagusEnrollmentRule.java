package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class SpecificLEICTagusEnrollmentRule extends SpecificLEICEnrollmentRule{

    public SpecificLEICTagusEnrollmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    protected List<CurricularCourse> getOptionalCurricularCourses() {
	List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	List<CurricularCourse> tagusCourses = getTagus5Courses();
	List<CurricularCourse> tagusSecCourses = getTagusSecAreaCourses();
	List<CurricularCourse> leicCourses = getLEIC5Courses();
	result.addAll(tagusCourses);
	result.addAll(leicCourses);
	result.addAll(tagusSecCourses);
	return result;
    }

    protected List<CurricularCourse> getTagus5Courses(){
	Set<CurricularCourse> areaCourses = new HashSet<CurricularCourse>();
	List<CurricularCourseGroup> optionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan().getAllOptionalCurricularCourseGroups();
	for (CurricularCourseGroup curricularCourseGroup : optionalCurricularCourseGroups) {
	    List<CurricularCourse> optionalCurricularCourses = curricularCourseGroup.getCurricularCourses();
	    for (CurricularCourse course : optionalCurricularCourses) {
		if(!studentCurricularPlan.isCurricularCourseApproved(course) && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(course, executionPeriod)) {
		    if(isAnyScopeActive(course.getScopes())) {
			areaCourses.add(course);
		    }
		}
		else {
		    if(isAnyScopeActive(course.getScopes())) {
			optionalCourses++;
		    }
		}
	    }
	}
	return new ArrayList<CurricularCourse>(areaCourses);
    }

    protected List<CurricularCourse> getTagusSecAreaCourses(){
	Set<CurricularCourse> areaCourses = new HashSet<CurricularCourse>();
	Set<CurricularCourse> allCurricularCourses = new HashSet<CurricularCourse>();
	List<CurricularCourseGroup> secCurricularCourseGroups = studentCurricularPlan.getSecundaryBranch().getCurricularCourseGroups();
	for (CurricularCourseGroup group : secCurricularCourseGroups) {
	    allCurricularCourses.addAll(group.getCurricularCourses());
	}

	allCurricularCourses.removeAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);
	
	if(!isSecAreaDone) {
	    allCurricularCourses.removeAll(getSecundaryAreaCurricularCourses(studentCurricularPlan));
	}

	for (CurricularCourse curricularCourse : allCurricularCourses) {
	    if(!studentCurricularPlan.isCurricularCourseApproved(curricularCourse) && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, executionPeriod)) {
		if(isAnyScopeActive(curricularCourse.getScopes())) {
		    areaCourses.add(curricularCourse);
		}
	    }
	    else {
		if(isAnyScopeActive(curricularCourse.getScopes())) {
		    optionalCourses++;
		}
	    }
	}

	return new ArrayList<CurricularCourse>(areaCourses);
    }

    protected List<CurricularCourse> getLEIC5Courses(){
	Set<CurricularCourse> areaCourses = new HashSet<CurricularCourse>();
	DegreeCurricularPlan leicDegreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(88);
	List<CurricularCourseGroup> optionalCurricularCourseGroups = leicDegreeCurricularPlan.getAllOptionalCurricularCourseGroups();

	for (CurricularCourseGroup curricularCourseGroup : optionalCurricularCourseGroups) {
	    List<CurricularCourse> optionalCurricularCourses = curricularCourseGroup.getCurricularCourses();
	    for (CurricularCourse course : optionalCurricularCourses) {
		if (!isApproved(course) && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(course, executionPeriod)) {
		    if(!studentCurricularPlan.isCurricularCourseApproved(course) && isAnyScopeActive(course.getScopes())) {
			areaCourses.add(course);
		    }
		}
		else {
		    if(isAnyScopeActive(course.getScopes())) {
			optionalCourses++;
		    }
		}
	    }
	}

	return new ArrayList<CurricularCourse>(areaCourses);
    }

}
