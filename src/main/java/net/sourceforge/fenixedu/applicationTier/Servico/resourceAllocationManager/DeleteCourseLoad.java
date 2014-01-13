package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteCourseLoad {

    @Atomic
    public static void run(CourseLoad courseLoad) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        if (courseLoad != null) {
            courseLoad.delete();
        }
    }
}