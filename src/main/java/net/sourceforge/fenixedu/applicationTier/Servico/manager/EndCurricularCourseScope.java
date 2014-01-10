package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério 28/10/2003
 * 
 */
public class EndCurricularCourseScope {

    @Atomic
    public static void run(InfoCurricularCourseScopeEditor newInfoCurricularCourseScope) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        if (!newInfoCurricularCourseScope.getEndDate().after(newInfoCurricularCourseScope.getBeginDate())) {
            throw new InvalidArgumentsServiceException();
        }

        CurricularCourseScope oldCurricularCourseScope =
                FenixFramework.getDomainObject(newInfoCurricularCourseScope.getExternalId());
        if (oldCurricularCourseScope == null) {
            throw new NonExistingServiceException("message.non.existing.curricular.course.scope", null);
        }

        oldCurricularCourseScope.setEndDate(newInfoCurricularCourseScope.getEndDate());
    }

}