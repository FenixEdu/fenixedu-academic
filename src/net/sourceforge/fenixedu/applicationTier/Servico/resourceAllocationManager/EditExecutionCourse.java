package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.CourseLoadBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class EditExecutionCourse extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(CourseLoadBean bean) throws FenixServiceException {
	if (bean != null) {
	    ExecutionCourse executionCourse = bean.getExecutionCourse();
	    executionCourse.editCourseLoad(bean.getType(), bean.getUnitQuantity(), bean.getTotalQuantity());
	}
    }
}