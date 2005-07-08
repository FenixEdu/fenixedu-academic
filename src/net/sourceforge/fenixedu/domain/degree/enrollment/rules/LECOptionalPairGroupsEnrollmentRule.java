/*
 * Created on 7/Fev/2005
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *  
 */
public class LECOptionalPairGroupsEnrollmentRule implements IEnrollmentRule {

    private IStudentCurricularPlan studentCurricularPlan;

    public LECOptionalPairGroupsEnrollmentRule(IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        List allOptionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan()
                .getAllOptionalCurricularCourseGroups();

        List optionalPairCurricularCourseGroups = (List) CollectionUtils.select(
                allOptionalCurricularCourseGroups, new Predicate() {
                    public boolean evaluate(Object obj) {
                        ICurricularCourseGroup ccg = (ICurricularCourseGroup) obj;
                        return ccg.getName().endsWith("GRUPO A") || ccg.getName().endsWith("GRUPO B")
                                || ccg.getName().endsWith("GRUPO C")
                                || ccg.getName().endsWith("GRUPO D");

                    }
                });

        ICurricularCourseGroup firstOptionalCCurricularCourseGroup = null;
        ICurricularCourseGroup secondOptionalCCurricularCourseGroup = null;
        boolean pairDone = false;

        for (Iterator iter = optionalPairCurricularCourseGroups.iterator(); iter.hasNext();) {
            ICurricularCourseGroup ccg = (ICurricularCourseGroup) iter.next();
            for (Iterator iterCCG = ccg.getCurricularCourses().iterator(); iterCCG.hasNext();) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterCCG.next();
                if (studentCurricularPlan.isCurricularCourseEnrolled(curricularCourse)
                        || studentCurricularPlan.isCurricularCourseApproved(curricularCourse)) {
                    if (firstOptionalCCurricularCourseGroup == null)
                        firstOptionalCCurricularCourseGroup = ccg;
                    else if (ccg != firstOptionalCCurricularCourseGroup) {
                        secondOptionalCCurricularCourseGroup = ccg;
                        pairDone = true;
                        break;
                    }
                }
            }
            if (pairDone) {
                optionalPairCurricularCourseGroups.remove(firstOptionalCCurricularCourseGroup);
                optionalPairCurricularCourseGroups.remove(secondOptionalCCurricularCourseGroup);
                break;
            }
        }

        List curricularCoursesToEnrollToRemove = new ArrayList();
        for (int iter = 0; iter < optionalPairCurricularCourseGroups.size(); iter++) {
            ICurricularCourseGroup ccg = (ICurricularCourseGroup) optionalPairCurricularCourseGroups
                    .get(iter);

            if (pairDone)
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
            ICurricularCourse cc = ccg.getCurricularCourses().get(iter);
            curricularCoursesToEnroll.add(new CurricularCourse2Enroll(cc, null, null));
        }
        return curricularCoursesToEnroll;
    }
}
