package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.CourseLoadBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class EditExecutionCourse {

    @Atomic
    public static void run(CourseLoadBean bean) throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        if (bean != null) {
            ExecutionCourse executionCourse = bean.getExecutionCourse();
            executionCourse.editCourseLoad(bean.getType(), bean.getUnitQuantity(), bean.getTotalQuantity());
        }
    }
}