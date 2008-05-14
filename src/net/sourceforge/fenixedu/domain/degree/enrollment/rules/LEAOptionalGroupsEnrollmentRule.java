/**
 * 
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class LEAOptionalGroupsEnrollmentRule implements IEnrollmentRule {

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionSemester executionSemester;

    private final static String options1FirstSemester = "Opções 5ºano 1ºsem 1";

    private final static String options1SecondSemester = "Opções 5ºano 2ºsem 1";

    private final static String options2FirstSemester = "Opções 5ºano 1ºsem 2";

    private final static String options2SecondSemester = "Opções 5ºano 2ºsem 2";

    public LEAOptionalGroupsEnrollmentRule(StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionSemester = executionSemester;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException {
        List allOptionalGroups = studentCurricularPlan.getDegreeCurricularPlan()
                .getAllOptionalCurricularCourseGroups();
        List<CurricularCourseGroup> branchOptionalGroups = (List) CollectionUtils.select(
                allOptionalGroups, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) arg0;
                        return curricularCourseGroup.getBranch().equals(
                                studentCurricularPlan.getBranch());
                    }
                });
        List<CurricularCourseGroup> allOptionalGroups2 = (List) CollectionUtils.select(
                branchOptionalGroups, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) arg0;
                        return curricularCourseGroup.getName().endsWith("2");
                    }
                });
        List<CurricularCourseGroup> optionalGroupsThisSemester = (List<CurricularCourseGroup>) CollectionUtils
                .select(branchOptionalGroups, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        CurricularCourseGroup ccg = (CurricularCourseGroup) arg0;
                        if ((ccg.getName().equalsIgnoreCase(options1FirstSemester) || ccg.getName()
                                .equalsIgnoreCase(options2FirstSemester))
                                && executionSemester.getSemester() == 1) {
                            return true;
                        } else if ((ccg.getName().equalsIgnoreCase(options1SecondSemester) || ccg
                                .getName().equalsIgnoreCase(options2SecondSemester))
                                && executionSemester.getSemester() == 2) {
                            return true;
                        }
                        return false;
                    }
                });

        Collections.sort(optionalGroupsThisSemester, new BeanComparator("name"));

        Set<CurricularCourseGroup> curricularCourseGroupsToRemove = new HashSet();
        // if the student is enrolled in a group of type 2, it can't enroll in
        // any other course of this groups
        for (CurricularCourseGroup curricularCourseGroup : allOptionalGroups2) {
            for (Iterator iter = curricularCourseGroup.getCurricularCourses().iterator(); iter.hasNext();) {
                CurricularCourse curricularCourse = (CurricularCourse) iter.next();
                if (studentCurricularPlan.isCurricularCourseApproved(curricularCourse)
                        || studentCurricularPlan.isCurricularCourseEnrolled(curricularCourse)) {
                    curricularCourseGroupsToRemove.addAll(allOptionalGroups2);
                    break;
                }
            }
        }
        for (CurricularCourseGroup curricularCourseGroup : optionalGroupsThisSemester) {
            for (Iterator iter = curricularCourseGroup.getCurricularCourses().iterator(); iter.hasNext();) {
                CurricularCourse curricularCourse = (CurricularCourse) iter.next();
                if (studentCurricularPlan.isCurricularCourseApproved(curricularCourse)
                        || studentCurricularPlan.isCurricularCourseEnrolled(curricularCourse)) {
                    if (curricularCourseGroup.getName().endsWith("1")) {
                        curricularCourseGroupsToRemove.add(optionalGroupsThisSemester.get(1));
                    } else {
                        curricularCourseGroupsToRemove.add(optionalGroupsThisSemester.get(0));
                    }
                    break;
                }
            }
        }
        List curricularCoursesToEnrollToRemove = getCurricularCoursesToEnrollToRemove(curricularCourseGroupsToRemove);
        curricularCoursesToBeEnrolledIn.removeAll(curricularCoursesToEnrollToRemove);
        return curricularCoursesToBeEnrolledIn;
    }

    /**
     * @param curricularCourseGroupsToRemove
     * @return
     */
    private List getCurricularCoursesToEnrollToRemove(Set<CurricularCourseGroup> curricularCourseGroupsToRemove) {
        List curricularCoursesToEnroll = new ArrayList();
        for (CurricularCourseGroup curricularCourseGroup : curricularCourseGroupsToRemove) {
            for (Iterator iter = curricularCourseGroup.getCurricularCourses().iterator(); iter.hasNext();) {
                CurricularCourse curricularCourse = (CurricularCourse) iter.next();
                curricularCoursesToEnroll.add(curricularCoursesToEnroll.add(new CurricularCourse2Enroll(
                        curricularCourse, null, null)));
            }
        }
        return curricularCoursesToEnroll;
    }
}
