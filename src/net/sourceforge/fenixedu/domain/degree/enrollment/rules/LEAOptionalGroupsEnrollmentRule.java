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

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
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

    private IStudentCurricularPlan studentCurricularPlan;

    private IExecutionPeriod executionPeriod;

    private final static String options1FirstSemester = "Opções 5ºano 1ºsem 1";

    private final static String options1SecondSemester = "Opções 5ºano 2ºsem 1";

    private final static String options2FirstSemester = "Opções 5ºano 1ºsem 2";

    private final static String options2SecondSemester = "Opções 5ºano 2ºsem 2";

    public LEAOptionalGroupsEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionPeriod = executionPeriod;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException {
        List allOptionalGroups = studentCurricularPlan.getDegreeCurricularPlan()
                .getAllOptionalCurricularCourseGroups();
        List<ICurricularCourseGroup> branchOptionalGroups = (List) CollectionUtils.select(
                allOptionalGroups, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) arg0;
                        return curricularCourseGroup.getBranch().equals(
                                studentCurricularPlan.getBranch());
                    }
                });
        List<ICurricularCourseGroup> allOptionalGroups2 = (List) CollectionUtils.select(
                branchOptionalGroups, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) arg0;
                        return curricularCourseGroup.getName().endsWith("2");
                    }
                });
        List<ICurricularCourseGroup> optionalGroupsThisSemester = (List<ICurricularCourseGroup>) CollectionUtils
                .select(branchOptionalGroups, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        ICurricularCourseGroup ccg = (ICurricularCourseGroup) arg0;
                        if ((ccg.getName().equalsIgnoreCase(options1FirstSemester) || ccg.getName()
                                .equalsIgnoreCase(options2FirstSemester))
                                && executionPeriod.getSemester() == 1) {
                            return true;
                        } else if ((ccg.getName().equalsIgnoreCase(options1SecondSemester) || ccg
                                .getName().equalsIgnoreCase(options2SecondSemester))
                                && executionPeriod.getSemester() == 2) {
                            return true;
                        }
                        return false;
                    }
                });

        Collections.sort(optionalGroupsThisSemester, new BeanComparator("name"));

        Set<ICurricularCourseGroup> curricularCourseGroupsToRemove = new HashSet();
        // if the student is enrolled in a group of type 2, it can't enroll in
        // any other course of this groups
        for (ICurricularCourseGroup curricularCourseGroup : allOptionalGroups2) {
            for (Iterator iter = curricularCourseGroup.getCurricularCourses().iterator(); iter.hasNext();) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                if (studentCurricularPlan.isCurricularCourseApproved(curricularCourse)
                        || studentCurricularPlan.isCurricularCourseEnrolled(curricularCourse)) {
                    curricularCourseGroupsToRemove.addAll(allOptionalGroups2);
                    break;
                }
            }
        }
        for (ICurricularCourseGroup curricularCourseGroup : optionalGroupsThisSemester) {
            for (Iterator iter = curricularCourseGroup.getCurricularCourses().iterator(); iter.hasNext();) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
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
    private List getCurricularCoursesToEnrollToRemove(Set<ICurricularCourseGroup> curricularCourseGroupsToRemove) {
        List curricularCoursesToEnroll = new ArrayList();
        for (ICurricularCourseGroup curricularCourseGroup : curricularCourseGroupsToRemove) {
            for (Iterator iter = curricularCourseGroup.getCurricularCourses().iterator(); iter.hasNext();) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                curricularCoursesToEnroll.add(curricularCoursesToEnroll.add(new CurricularCourse2Enroll(
                        curricularCourse, null, null)));
            }
        }
        return curricularCoursesToEnroll;
    }
}
