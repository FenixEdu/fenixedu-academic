package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class SpecificLEICTagusEnrollmentRule extends SpecificLEICEnrollmentRule{
	
    public SpecificLEICTagusEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        super(studentCurricularPlan, executionPeriod);
    }
	
	protected List<ICurricularCourse> getOptionalCurricularCourses() {
		List<ICurricularCourse> result = new ArrayList<ICurricularCourse>();
		List<ICurricularCourse> tagusCourses = getTagus5Courses();
		List<ICurricularCourse> tagusSecCourses = getTagusSecAreaCourses();
		List<ICurricularCourse> leicCourses = getLEIC5Courses();
		result.addAll(tagusCourses);
		result.addAll(leicCourses);
		result.addAll(tagusSecCourses);
		return result;
	}
	
	protected List<ICurricularCourse> getTagus5Courses(){
		Set<ICurricularCourse> areaCourses = new HashSet<ICurricularCourse>();
        List<ICurricularCourseGroup> optionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan().getAllOptionalCurricularCourseGroups();
        for (ICurricularCourseGroup curricularCourseGroup : optionalCurricularCourseGroups) {
			List<ICurricularCourse> optionalCurricularCourses = curricularCourseGroup.getCurricularCourses();
			for (ICurricularCourse course : optionalCurricularCourses) {
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
        return new ArrayList<ICurricularCourse>(areaCourses);
	}
	
	protected List<ICurricularCourse> getTagusSecAreaCourses(){
		Set<ICurricularCourse> areaCourses = new HashSet<ICurricularCourse>();
		Set<ICurricularCourse> allCurricularCourses = new HashSet<ICurricularCourse>();
		List<ICurricularCourseGroup> secCurricularCourseGroups = studentCurricularPlan.getSecundaryBranch().getCurricularCourseGroups();
		for (ICurricularCourseGroup group : secCurricularCourseGroups) {
			allCurricularCourses.addAll(group.getCurricularCourses());
		}

		allCurricularCourses.removeAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);
		
		for (ICurricularCourse curricularCourse : allCurricularCourses) {
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
		
		return new ArrayList<ICurricularCourse>(areaCourses);
	}

	protected List<ICurricularCourse> getLEIC5Courses(){
		try {
			Set<ICurricularCourse> areaCourses = new HashSet<ICurricularCourse>();
			IDegreeCurricularPlan leicDegreeCurricularPlan = (IDegreeCurricularPlan) PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class, 88);
			List<ICurricularCourseGroup> optionalCurricularCourseGroups = leicDegreeCurricularPlan.getAllOptionalCurricularCourseGroups();
	        for (ICurricularCourseGroup curricularCourseGroup : optionalCurricularCourseGroups) {
				List<ICurricularCourse> optionalCurricularCourses = curricularCourseGroup.getCurricularCourses();
				for (ICurricularCourse course : optionalCurricularCourses) {
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
	        return new ArrayList<ICurricularCourse>(areaCourses);
		} catch (ExcepcaoPersistencia e) {
			throw new RuntimeException(e);
		}
		
	}

}
