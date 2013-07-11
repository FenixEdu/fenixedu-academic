package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/*
 * 
 * @author Fernanda Quitério 29/Dez/2003
 */
public class AssociateCurricularCoursesToExecutionCourse {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(Integer executionCourseId, List<Integer> curricularCourseIds) throws FenixServiceException {
        if (executionCourseId == null) {
            throw new FenixServiceException("nullExecutionCourseId");
        }

        if (curricularCourseIds != null && !curricularCourseIds.isEmpty()) {
            ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId);

            if (executionCourse == null) {
                throw new DomainException("message.nonExisting.executionCourse");
            }

            Iterator<Integer> iter = curricularCourseIds.iterator();
            while (iter.hasNext()) {
                Integer curricularCourseId = iter.next();

                CurricularCourse curricularCourse =
                        (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseId);
                if (curricularCourse == null) {
                    throw new DomainException("message.nonExistingDegreeCurricularPlan");
                }
                if (!curricularCourse.hasAssociatedExecutionCourses(executionCourse)) {
                    curricularCourse.addAssociatedExecutionCourses(executionCourse);
                }
            }
        } else {
            throw new DomainException("error.selection.noCurricularCourse");
        }
    }
}