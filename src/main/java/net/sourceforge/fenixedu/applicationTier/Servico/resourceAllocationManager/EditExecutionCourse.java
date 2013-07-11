package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.CourseLoadBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class EditExecutionCourse {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static void run(CourseLoadBean bean) throws FenixServiceException {
        if (bean != null) {
            ExecutionCourse executionCourse = bean.getExecutionCourse();
            executionCourse.editCourseLoad(bean.getType(), bean.getUnitQuantity(), bean.getTotalQuantity());
        }
    }
}