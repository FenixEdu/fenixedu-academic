package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class SpecificLEFTEnrolmentRule implements IEnrollmentRule {

	private IStudentCurricularPlan studentCurricularPlan = null;
	private IExecutionPeriod executionPeriod = null;
	
	private ICurricularCourse fgesCourse = null;
	private ICurricularCourse projectoCourse = null;
	private boolean isFGESaproved = false;
	private boolean projectoEnrolmentAllowed = false;
	
	public SpecificLEFTEnrolmentRule(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod) {
		setExecutionPeriod(executionPeriod);
		setStudentCurricularPlan(studentCurricularPlan);
	}
	
	
	public List apply(List curricularCoursesToBeEnrolledIn)
			throws EnrolmentRuleDomainException {
		
		List<ICurricularCourse> result = null;
		
		if(studentCurricularPlan.getBranch() != null) {
			fgesCourse = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode("AX2");
			projectoCourse = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode("XH");
			if(studentCurricularPlan.isCurricularCourseApproved(fgesCourse) || studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(fgesCourse, getExecutionPeriod())) {
				isFGESaproved = true;
			}
			
			if(studentCurricularPlan.getBranch().getCode().equals("710")) {
				result = getEngenhariaOptionalCourses();
			}
			else {
				result = getFisicaOptionalCourses();
			}
			
			return filter(result, curricularCoursesToBeEnrolledIn);
		}
		else {
			return removeOptionalCourses(curricularCoursesToBeEnrolledIn);
		}
		
	}


	private List filter(final List<ICurricularCourse> curricularCoursesFromOptionalGroups,
			List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
		final List<ICurricularCourse> commonCurricularCourses = getCommonCurricularCourses();

		List result = (List) CollectionUtils.select(
				curricularCoursesToBeEnrolledIn, new Predicate() {
					public boolean evaluate(Object obj) {
						CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
						return commonCurricularCourses
								.contains(curricularCourse2Enroll
										.getCurricularCourse());
					}
				});
		
        if(!projectoEnrolmentAllowed) {
        	CollectionUtils.filter(result, new Predicate() {

				public boolean evaluate(Object arg0) {
					CurricularCourse2Enroll course2Enroll = (CurricularCourse2Enroll) arg0;
					if(course2Enroll.getCurricularCourse().equals(projectoCourse))
						return false;
					return true;
				}
        		
        	});
        }
		
        List curricularCoursesFromOtherAreasToMantain = (List) CollectionUtils.select(
                curricularCoursesToBeEnrolledIn, new Predicate() {
                    public boolean evaluate(Object obj) {
                        CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                        return curricularCoursesFromOptionalGroups
                                .contains(curricularCourse2Enroll.getCurricularCourse());
                    }
                });
        
        result.addAll(curricularCoursesFromOtherAreasToMantain);
        
        return result;

	}


	private List<ICurricularCourse> getCommonCurricularCourses() {
		List<IBranch> commonAreas = studentCurricularPlan.getDegreeCurricularPlan().getCommonAreas();
		List<ICurricularCourse> curricularCourses = new ArrayList<ICurricularCourse>();
		for (IBranch branch : commonAreas) {
			curricularCourses.addAll(studentCurricularPlan.getDegreeCurricularPlan().getCurricularCoursesFromArea(branch, AreaType.BASE));
		}
		
		List<ICurricularCourse> result = new ArrayList<ICurricularCourse>();
		for (ICurricularCourse course : curricularCourses) {
			List<ICurricularCourseScope> scopes = course.getScopes();
			for (ICurricularCourseScope curricularCourseScope : scopes) {
				if(curricularCourseScope.getBranch().getBranchType().equals(BranchType.COMNBR) && curricularCourseScope.isActive(executionPeriod.getBeginDate())) {
					result.add(course);
					break;
				}
			}
		}	
		return result;
	}



	private List<ICurricularCourse> getGroupBCurricularCourses() {
		ICurricularCourseGroup curricularCourseGroup = getOptionalCurricularCoursesGroupBySemesterAndCode(executionPeriod.getSemester(), "B");
		return curricularCourseGroup.getCurricularCourses();
	}
	
	private List<ICurricularCourse> getGroupACurricularCourses() {
		ICurricularCourseGroup curricularCourseGroup = getOptionalCurricularCoursesGroupBySemesterAndCode(executionPeriod.getSemester(), "A");
		return curricularCourseGroup.getCurricularCourses();
	}

	private Integer countGroupAAprovedOrEnroledCourses() {
		Integer count = 0;
		List<ICurricularCourse> curricularCourses = getGroupACurricularCourses();
		for (ICurricularCourse curricularCourse : curricularCourses) {
			if(studentCurricularPlan.isCurricularCourseApproved(curricularCourse) || studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, getExecutionPeriod())) {
				count++;
			}
		}
		return count;
	}
	
	private Integer countGroupBAprovedOrEnroledCourses() {
		Integer count = 0;
		List<ICurricularCourse> curricularCourses = getGroupBCurricularCourses();
		for (ICurricularCourse curricularCourse : curricularCourses) {
			if(studentCurricularPlan.isCurricularCourseApproved(curricularCourse) || studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, getExecutionPeriod())) {
				count++;
			}
		}
		return count;
	}


	private List<ICurricularCourse> getFisicaOptionalCourses() {
		//FIXME : só funca para o 1º Semestre
		List<ICurricularCourse> result = new ArrayList<ICurricularCourse>();
		
		Integer groupA = countGroupAAprovedOrEnroledCourses();
		Integer groupB = countGroupBAprovedOrEnroledCourses();
		
		if(groupA.equals(0)) {
			result.addAll(getGroupACurricularCourses());
			if(groupB < 3) {
				result.addAll(getGroupBCurricularCourses());
			}
		}
		else if(groupA.equals(1) && groupB < 4) {
			if(groupB < 3) {
				result.addAll(getGroupACurricularCourses());
				result.addAll(getGroupBCurricularCourses());
			}
			if(isFGESaproved){
				result.addAll(getGroupBCurricularCourses());
				projectoEnrolmentAllowed = true;
			}
		}
		else if(groupA.equals(2) && groupB < 3) {
			if(groupB < 2) {
				result.addAll(getGroupBCurricularCourses());
			}
			if(isFGESaproved) {
				result.addAll(getGroupBCurricularCourses());
				projectoEnrolmentAllowed = true;
			}			
		}
		return result;
	}
	
	private List<ICurricularCourse> getEngenhariaOptionalCourses() {
		//FIXME : só funca para o 1º Semestre
		List<ICurricularCourse> result = new ArrayList<ICurricularCourse>();
		
		Integer groupA = countGroupAAprovedOrEnroledCourses();
		Integer groupB = countGroupBAprovedOrEnroledCourses();
		
		if(groupB.equals(0)) {
			result.addAll(getGroupBCurricularCourses());
			if(groupA < 3) {
				result.addAll(getGroupACurricularCourses());
			}
		}
		else if(groupB.equals(1) && groupA < 4) {
			if(groupA < 3) {
				result.addAll(getGroupACurricularCourses());
				result.addAll(getGroupBCurricularCourses());
			}
			if(isFGESaproved){
				result.addAll(getGroupACurricularCourses());
				projectoEnrolmentAllowed = true;
			}
		}
		else if(groupB.equals(2) && groupA < 3) {
			if(groupA < 2) {
				result.addAll(getGroupACurricularCourses());
			}
			if(isFGESaproved) {
				result.addAll(getGroupACurricularCourses());
				projectoEnrolmentAllowed = true;
			}			
		}
		
		return result;
	}


	private List removeOptionalCourses(List curricularCoursesToBeEnrolledIn) {
		final List<ICurricularCourse> curricularCourses = getAllOptionalCurricularCoursesBySemester(executionPeriod.getSemester());
        List result = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn, new Predicate() {

            public boolean evaluate(Object arg0) {
                CurricularCourse2Enroll course2Enroll = (CurricularCourse2Enroll) arg0;
                return !curricularCourses.contains(course2Enroll.getCurricularCourse());
            }
            
        });
        return result;
	}


	private List<ICurricularCourse> getAllOptionalCurricularCoursesBySemester(Integer semester) {
		Set<ICurricularCourse> result = new HashSet<ICurricularCourse>();
		List<ICurricularCourseGroup> allOptionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan().getAllOptionalCurricularCourseGroups();
		for (ICurricularCourseGroup curricularCourseGroup : allOptionalCurricularCourseGroups) {
			if(curricularCourseGroup.getName().indexOf(semester.toString()) != -1) {
				List<ICurricularCourse> curricularCourses = curricularCourseGroup.getCurricularCourses();
				result.addAll(curricularCourses);
			}
		}
		return null;
	}
	
	private ICurricularCourseGroup getOptionalCurricularCoursesGroupBySemesterAndCode(Integer semester, String code) {
		List<ICurricularCourseGroup> allOptionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan().getAllOptionalCurricularCourseGroups();
		for (ICurricularCourseGroup curricularCourseGroup : allOptionalCurricularCourseGroups) {
			if((curricularCourseGroup.getName().indexOf(semester.toString()) != -1) && (curricularCourseGroup.getName().indexOf(code) != -1)) {
				return curricularCourseGroup;
			}
		}
		return null;
	}


	private IExecutionPeriod getExecutionPeriod() {
		return executionPeriod;
	}


	private void setExecutionPeriod(IExecutionPeriod executionPeriod) {
		this.executionPeriod = executionPeriod;
	}


	private IStudentCurricularPlan getStudentCurricularPlan() {
		return studentCurricularPlan;
	}


	private void setStudentCurricularPlan(
			IStudentCurricularPlan studentCurricularPlan) {
		this.studentCurricularPlan = studentCurricularPlan;
	}

}
