/*
 * Created on 10/Fev/2005
 *
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *
 */
public class LEMOptionalPairGroupsEnrollmentRule implements IEnrollmentRule {

    private IStudentCurricularPlan studentCurricularPlan;

    public LEMOptionalPairGroupsEnrollmentRule(IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException {
        List allOptionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan()
                .getAllOptionalCurricularCourseGroups();

        List optionalPairCurricularCourseGroups = (List) CollectionUtils.select(
                allOptionalCurricularCourseGroups, new Predicate() {
                    public boolean evaluate(Object obj) {
                        ICurricularCourseGroup ccg = (ICurricularCourseGroup) obj;
                        return ccg.getName().endsWith("PD") || ccg.getName().endsWith("PR");
                    }
                });

        ICurricularCourseGroup optionalCurricularCourseGroup = null;        
        boolean oneDone = false;

        for (Iterator iter = optionalPairCurricularCourseGroups.iterator(); iter.hasNext();) {
            ICurricularCourseGroup ccg = (ICurricularCourseGroup) iter.next();
            for (Iterator iterCCG = ccg.getCurricularCourses().iterator(); iterCCG.hasNext();) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterCCG.next();
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
            ICurricularCourseGroup ccg = (ICurricularCourseGroup) optionalPairCurricularCourseGroups
                    .get(iter);

            if (oneDone)
                curricularCoursesToEnrollToRemove.addAll(getCurricularCoursesToEnroll(ccg));
        }
        curricularCoursesToBeEnrolledIn.removeAll(curricularCoursesToEnrollToRemove);
        return curricularCoursesToBeEnrolledIn;
    }

    /**
     * @param ccg
     * @return
     */
    private List getCurricularCoursesToEnroll(ICurricularCourseGroup ccg) {
        List curricularCoursesToEnroll = new ArrayList();
        int size = ccg.getCurricularCourses().size();
        for (int iter = 0; iter < size; iter++) {
            ICurricularCourse cc = (ICurricularCourse) ccg.getCurricularCourses().get(iter);
            curricularCoursesToEnroll.add(new CurricularCourse2Enroll(cc, null, null));
        }
        return curricularCoursesToEnroll;
    }

}
