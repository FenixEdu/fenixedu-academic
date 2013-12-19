/*
 * Created on 22/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */
public class ReadCurricularCourseScope {

    /**
     * Executes the service. Returns the current InfoCurricularCourseScope.
     * 
     * @throws ExcepcaoPersistencia
     */
    @Atomic
    public static InfoCurricularCourseScope run(String externalId) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        CurricularCourseScope curricularCourseScope;

        curricularCourseScope = FenixFramework.getDomainObject(externalId);

        if (curricularCourseScope == null) {
            throw new NonExistingServiceException();
        }

        return InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
    }
}