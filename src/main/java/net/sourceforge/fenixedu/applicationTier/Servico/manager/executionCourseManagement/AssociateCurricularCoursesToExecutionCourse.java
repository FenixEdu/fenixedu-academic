package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/*
 * 
 * @author Fernanda Quitério 29/Dez/2003
 */
public class AssociateCurricularCoursesToExecutionCourse {

    @Service
    public static void run(String executionCourseId, List<String> curricularCourseIds) throws FenixServiceException {
        if (executionCourseId == null) {
            throw new FenixServiceException("nullExecutionCourseId");
        }

        if (curricularCourseIds != null && !curricularCourseIds.isEmpty()) {
            ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);

            if (executionCourse == null) {
                throw new DomainException("message.nonExisting.executionCourse");
            }

            Iterator<String> iter = curricularCourseIds.iterator();
            while (iter.hasNext()) {
                String curricularCourseId = iter.next();

                CurricularCourse curricularCourse = AbstractDomainObject.fromExternalId(curricularCourseId);
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