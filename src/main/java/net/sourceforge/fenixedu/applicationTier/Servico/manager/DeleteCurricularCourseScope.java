/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.CantDeleteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */
public class DeleteCurricularCourseScope {

    @Atomic
    public static void run(String scopeId) throws FenixServiceException {
        check(RolePredicates.MANAGER_PREDICATE);
        CurricularCourseScope scope = FenixFramework.getDomainObject(scopeId);
        if (scope != null) {

            try {
                scope.delete();
            } catch (DomainException e) {
                throw new CantDeleteServiceException();
            }
        }
    }
}