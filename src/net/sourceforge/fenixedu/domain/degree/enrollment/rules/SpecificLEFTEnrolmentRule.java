package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class SpecificLEFTEnrolmentRule implements IEnrollmentRule {

	private StudentCurricularPlan studentCurricularPlan = null;
	private ExecutionSemester executionSemester = null;
	
	private CurricularCourse fgesCourse = null;
	private CurricularCourse projectoCourse = null;
	private boolean isFGESaproved = false;
	private boolean projectoEnrolmentAllowed = false;
	
	public SpecificLEFTEnrolmentRule(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
		setExecutionPeriod(executionSemester);
		setStudentCurricularPlan(studentCurricularPlan);
	}
	
	
	public List apply(List curricularCoursesToBeEnrolledIn)
			throws EnrolmentRuleDomainException {
		
		List<CurricularCourse> result = null;
		
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


	private List filter(final List<CurricularCourse> curricularCoursesFromOptionalGroups,
			List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
		final List<CurricularCourse> commonCurricularCourses = getCommonCurricularCourses();

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


	private List<CurricularCourse> getCommonCurricularCourses() {
		List<Branch> commonAreas = studentCurricularPlan.getDegreeCurricularPlan().getCommonAreas();
		List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
		for (Branch branch : commonAreas) {
			curricularCourses.addAll(studentCurricularPlan.getDegreeCurricularPlan().getCurricularCoursesFromArea(branch, AreaType.BASE));
		}
		
		List<CurricularCourse> result = new ArrayList<CurricularCourse>();
		for (CurricularCourse course : curricularCourses) {
			List<CurricularCourseScope> scopes = course.getScopes();
			for (CurricularCourseScope curricularCourseScope : scopes) {
				if(curricularCourseScope.getBranch().getBranchType().equals(BranchType.COMNBR) && curricularCourseScope.isActive(executionSemester.getBeginDate())) {
					result.add(course);
					break;
				}
			}
		}	
		return result;
	}



	private List<CurricularCourse> getGroupBCurricularCourses() {
		CurricularCourseGroup curricularCourseGroup = getOptionalCurricularCoursesGroupBySemesterAndCode(executionSemester.getSemester(), "B");
		return curricularCourseGroup.getCurricularCourses();
	}
	
	private List<CurricularCourse> getGroupACurricularCourses() {
		CurricularCourseGroup curricularCourseGroup = getOptionalCurricularCoursesGroupBySemesterAndCode(executionSemester.getSemester(), "A");
		return curricularCourseGroup.getCurricularCourses();
	}

	private Integer countGroupAAprovedOrEnroledCourses() {
		Integer count = 0;
		List<CurricularCourse> curricularCourses = getGroupACurricularCourses();
		for (CurricularCourse curricularCourse : curricularCourses) {
			if(studentCurricularPlan.isCurricularCourseApproved(curricularCourse) || studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, getExecutionPeriod())) {
				count++;
			}
		}
		return count;
	}
	
	private Integer countGroupBAprovedOrEnroledCourses() {
		Integer count = 0;
		List<CurricularCourse> curricularCourses = getGroupBCurricularCourses();
		for (CurricularCourse curricularCourse : curricularCourses) {
			if(studentCurricularPlan.isCurricularCourseApproved(curricularCourse) || studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, getExecutionPeriod())) {
				count++;
			}
		}
		return count;
	}


	private List<CurricularCourse> getFisicaOptionalCourses() {
		//FIXME : só funca para o 1º Semestre
		List<CurricularCourse> result = new ArrayList<CurricularCourse>();
		
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
	
	private List<CurricularCourse> getEngenhariaOptionalCourses() {
		//FIXME : só funca para o 1º Semestre
		List<CurricularCourse> result = new ArrayList<CurricularCourse>();
		
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
		final List<CurricularCourse> curricularCourses = getAllOptionalCurricularCoursesBySemester(executionSemester.getSemester());
        List result = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn, new Predicate() {

            public boolean evaluate(Object arg0) {
                CurricularCourse2Enroll course2Enroll = (CurricularCourse2Enroll) arg0;
                return !curricularCourses.contains(course2Enroll.getCurricularCourse());
            }
            
        });
        return result;
	}


	private List<CurricularCourse> getAllOptionalCurricularCoursesBySemester(Integer semester) {
		Set<CurricularCourse> result = new HashSet<CurricularCourse>();
		List<CurricularCourseGroup> allOptionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan().getAllOptionalCurricularCourseGroups();
		for (CurricularCourseGroup curricularCourseGroup : allOptionalCurricularCourseGroups) {
			if(curricularCourseGroup.getName().indexOf(semester.toString()) != -1) {
				List<CurricularCourse> curricularCourses = curricularCourseGroup.getCurricularCourses();
				result.addAll(curricularCourses);
			}
		}
		return null;
	}
	
	private CurricularCourseGroup getOptionalCurricularCoursesGroupBySemesterAndCode(Integer semester, String code) {
		List<CurricularCourseGroup> allOptionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan().getAllOptionalCurricularCourseGroups();
		for (CurricularCourseGroup curricularCourseGroup : allOptionalCurricularCourseGroups) {
			if((curricularCourseGroup.getName().indexOf(semester.toString()) != -1) && (curricularCourseGroup.getName().indexOf(code) != -1)) {
				return curricularCourseGroup;
			}
		}
		return null;
	}


	private ExecutionSemester getExecutionPeriod() {
		return executionSemester;
	}


	private void setExecutionPeriod(ExecutionSemester executionSemester) {
		this.executionSemester = executionSemester;
	}


	private void setStudentCurricularPlan(
			StudentCurricularPlan studentCurricularPlan) {
		this.studentCurricularPlan = studentCurricularPlan;
	}

}
