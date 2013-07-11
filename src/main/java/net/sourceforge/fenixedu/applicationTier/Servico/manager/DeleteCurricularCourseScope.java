/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.CantDeleteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */
public class DeleteCurricularCourseScope {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(String scopeId) throws FenixServiceException {
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