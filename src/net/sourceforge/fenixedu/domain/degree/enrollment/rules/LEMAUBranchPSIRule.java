package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class LEMAUBranchPSIRule implements IEnrollmentRule{

	private static final String AUTOMACAO_ROBOTICA_CODE = "330";
	private static final String PROJECTO_SISTEMAS_I_CODE = "A9C";
	private static final String FITH_YEAR_2SEM_OPTIONAL_GROUP = "Opções 5ºAno 2ºSem"; 
	
    private StudentCurricularPlan studentCurricularPlan;
    private ExecutionSemester executionSemester;

    public LEMAUBranchPSIRule(StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionSemester = executionSemester;
    }


	public List apply(List curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException {
		if(executionSemester.getSemester().equals(2) && studentCurricularPlan.getBranch() != null && studentCurricularPlan.getBranch().getCode().equals(AUTOMACAO_ROBOTICA_CODE)) {
			CurricularCourse2Enroll curricularCourse2Enroll = getProjectoSistemasICourse2Enroll(curricularCoursesToBeEnrolledIn);
			if(curricularCourse2Enroll != null) {
				List<CurricularCourse> fifthYear2Sem = new ArrayList<CurricularCourse>();
				CurricularCourseGroup curricularCourseGroup = getCurricularCourseGroup(FITH_YEAR_2SEM_OPTIONAL_GROUP);
				fifthYear2Sem.addAll(curricularCourseGroup.getCurricularCourses());
				fifthYear2Sem.remove(curricularCourse2Enroll.getCurricularCourse());
				
				if(curricularCourse2Enroll.getEnrollmentType().equals(CurricularCourseEnrollmentType.DEFINITIVE)) {
					removeCurricularCourses(curricularCoursesToBeEnrolledIn, fifthYear2Sem);
				} else if(curricularCourse2Enroll.getEnrollmentType().equals(CurricularCourseEnrollmentType.TEMPORARY)) {
					setTemporaryEnrolmentType(curricularCoursesToBeEnrolledIn, fifthYear2Sem);
				}
			}
		}
		
		return curricularCoursesToBeEnrolledIn;
	}


	private void setTemporaryEnrolmentType(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn, List<CurricularCourse> fifthYear2Sem) {
		for (CurricularCourse course : fifthYear2Sem) {
			for (CurricularCourse2Enroll curricularCourse2Enroll : curricularCoursesToBeEnrolledIn) {
				if(curricularCourse2Enroll.getCurricularCourse().equals(course)) {
					curricularCourse2Enroll.setEnrollmentType(CurricularCourseEnrollmentType.TEMPORARY);
					break;
				}
			}
		}
	}


	private void removeCurricularCourses(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn, List<CurricularCourse> fifthYear2Sem) {
		List<CurricularCourse2Enroll> elementsToRemove = new ArrayList<CurricularCourse2Enroll>();
		for (CurricularCourse course : fifthYear2Sem) {
			for (CurricularCourse2Enroll curricularCourse2Enroll : curricularCoursesToBeEnrolledIn) {
				if(curricularCourse2Enroll.getCurricularCourse().equals(course)) {
					elementsToRemove.add(curricularCourse2Enroll);
					break;
				}
			}
		}
		curricularCoursesToBeEnrolledIn.removeAll(elementsToRemove);
	}


	private CurricularCourse2Enroll getProjectoSistemasICourse2Enroll(List curricularCoursesToBeEnrolledIn) {
		for(Iterator iter = curricularCoursesToBeEnrolledIn.iterator(); iter.hasNext();) {
			CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) iter.next();
			if(curricularCourse2Enroll.getCurricularCourse().getCode().equals(PROJECTO_SISTEMAS_I_CODE)) {
				return curricularCourse2Enroll;
			}
		}
		return null;
	}
	
    private CurricularCourseGroup getCurricularCourseGroup(final String optionalGroupName) {
        List optionalCurricularCourseGroups = (List) CollectionUtils.select(studentCurricularPlan.getDegreeCurricularPlan().getAllOptionalCurricularCourseGroups(), new Predicate() {

            public boolean evaluate(Object arg0) {
                CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) arg0;
                return curricularCourseGroup.getName().equalsIgnoreCase(optionalGroupName)
                        && curricularCourseGroup.getBranch().getCode().equals(AUTOMACAO_ROBOTICA_CODE);
            }
        });
        return (CurricularCourseGroup) optionalCurricularCourseGroups.get(0);
    }
	

}
