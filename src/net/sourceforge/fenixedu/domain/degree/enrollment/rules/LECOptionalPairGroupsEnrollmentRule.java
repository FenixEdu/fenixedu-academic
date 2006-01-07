/*
 * Created on 7/Fev/2005
 *
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *  
 */
public class LECOptionalPairGroupsEnrollmentRule implements IEnrollmentRule {

    private StudentCurricularPlan studentCurricularPlan;

    public LECOptionalPairGroupsEnrollmentRule(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        List allOptionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan()
                .getAllOptionalCurricularCourseGroups();

        List optionalPairCurricularCourseGroups = (List) CollectionUtils.select(
                allOptionalCurricularCourseGroups, new Predicate() {
                    public boolean evaluate(Object obj) {
                        CurricularCourseGroup ccg = (CurricularCourseGroup) obj;
                        return ccg.getName().endsWith("GRUPO A") || ccg.getName().endsWith("GRUPO B")
                                || ccg.getName().endsWith("GRUPO C")
                                || ccg.getName().endsWith("GRUPO D");

                    }
                });

        CurricularCourseGroup firstOptionalCCurricularCourseGroup = null;
        CurricularCourseGroup secondOptionalCCurricularCourseGroup = null;
        boolean pairDone = false;

        for (Iterator iter = optionalPairCurricularCourseGroups.iterator(); iter.hasNext();) {
            CurricularCourseGroup ccg = (CurricularCourseGroup) iter.next();
            for (Iterator iterCCG = ccg.getCurricularCourses().iterator(); iterCCG.hasNext();) {
                CurricularCourse curricularCourse = (CurricularCourse) iterCCG.next();
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
            CurricularCourseGroup ccg = (CurricularCourseGroup) optionalPairCurricularCourseGroups
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
