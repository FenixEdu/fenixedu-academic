/*
 * Created on 10/Fev/2005
 *
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *
 */
public class LEMOptionalPairGroupsEnrollmentRule implements IEnrollmentRule {

    private StudentCurricularPlan studentCurricularPlan;
    private ExecutionSemester executionSemester;

    public LEMOptionalPairGroupsEnrollmentRule(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionSemester = executionSemester;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        if(executionSemester.getSemester().equals(1)) {
	    	List allOptionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan()
	                .getAllOptionalCurricularCourseGroups();
	
	        List optionalPairCurricularCourseGroups = (List) CollectionUtils.select(
	                allOptionalCurricularCourseGroups, new Predicate() {
	                    public boolean evaluate(Object obj) {
	                        CurricularCourseGroup ccg = (CurricularCourseGroup) obj;
	                        return ccg.getName().endsWith("PD") || ccg.getName().endsWith("PR");
	                    }
	                });
	
	        CurricularCourseGroup optionalCurricularCourseGroup = null;        
	        boolean oneDone = false;
	
	        for (Iterator iter = optionalPairCurricularCourseGroups.iterator(); iter.hasNext();) {
	            CurricularCourseGroup ccg = (CurricularCourseGroup) iter.next();
	            for (Iterator iterCCG = ccg.getCurricularCourses().iterator(); iterCCG.hasNext();) {
	                CurricularCourse curricularCourse = (CurricularCourse) iterCCG.next();
	                if (studentCurricularPlan.isCurricularCourseEnrolled(curricularCourse)
	                        || studentCurricularPlan.isCurricularCourseApproved(curricularCourse)) {
	                    if (optionalCurricularCourseGroup == null){
	                        optionalCurricularCourseGroup = ccg;
	                        oneDone = true;
	                        break;
	                    }
	                }
	            }
	            if (oneDone) {
	                optionalPairCurricularCourseGroups.remove(optionalCurricularCourseGroup);
	                break;
	            }
	        }
	
	        List curricularCoursesToEnrollToRemove = new ArrayList();
	        for (int iter = 0; iter < optionalPairCurricularCourseGroups.size(); iter++) {
	            CurricularCourseGroup ccg = (CurricularCourseGroup) optionalPairCurricularCourseGroups
	                    .get(iter);
	
	            if (oneDone)
	                curricularCoursesToEnrollToRemove.addAll(getCurricularCoursesToEnroll(ccg));
	        }
	        curricularCoursesToBeEnrolledIn.removeAll(curricularCoursesToEnrollToRemove);
        }
        return curricularCoursesToBeEnrolledIn;
    }

    /**
     * @param ccg
     * @return
     */
    private List getCurricularCoursesToEnroll(CurricularCourseGroup ccg) {
        List curricularCoursesToEnroll = new ArrayList();
        int size = ccg.getCurricularCourses().size();
        for (int iter = 0; iter < size; iter++) {
            CurricularCourse cc = ccg.getCurricularCourses().get(iter);
            curricularCoursesToEnroll.add(new CurricularCourse2Enroll(cc, null, null));
        }
        return curricularCoursesToEnroll;
    }

}
