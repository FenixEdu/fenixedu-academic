package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseSearchBean;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class SelectAllExecutionCoursesForInquiries extends FenixService {

    @Checked("RolePredicates.GEP_PREDICATE")
    @Service
    public static void run(final ExecutionCourseSearchBean executionCourseSearchBean) {
        for (final ExecutionCourse executionCourse : executionCourseSearchBean.search()) {
            executionCourse.setAvailableForInquiries(Boolean.TRUE);
        }
    }

    @Checked("RolePredicates.GEP_PREDICATE")
    @Service
    public static void unselectAll(final ExecutionCourseSearchBean executionCourseSearchBean) {
        for (final ExecutionCourse executionCourse : executionCourseSearchBean.search()) {
            executionCourse.setAvailableForInquiries(Boolean.FALSE);
        }
    }

}