/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.CantDeleteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author lmac1
 */
public class DeleteCurricularCourseScope {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer scopeId) throws FenixServiceException {
        CurricularCourseScope scope = AbstractDomainObject.fromExternalId(scopeId);
        if (scope != null) {

            try {
                scope.delete();
            } catch (DomainException e) {
                throw new CantDeleteServiceException();
            }
        }
    }
}