package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;


import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseSearchBean;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class SelectAllExecutionCoursesForInquiries {

    @Atomic
    public static void run(final ExecutionCourseSearchBean executionCourseSearchBean) {
        check(RolePredicates.GEP_PREDICATE);
        for (final ExecutionCourse executionCourse : executionCourseSearchBean.search()) {
            executionCourse.setAvailableForInquiries(Boolean.TRUE);
        }
    }

    @Atomic
    public static void unselectAll(final ExecutionCourseSearchBean executionCourseSearchBean) {
        check(RolePredicates.GEP_PREDICATE);
        for (final ExecutionCourse executionCourse : executionCourseSearchBean.search()) {
            executionCourse.setAvailableForInquiries(Boolean.FALSE);
        }
    }

}