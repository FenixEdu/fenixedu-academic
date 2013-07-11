/*
 * Created on 1/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlan {

    protected InfoDegreeCurricularPlan run(final String externalId) throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(externalId);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
    }

    // Service Invokers migrated from Berserk

    private static final ReadDegreeCurricularPlan serviceInstance = new ReadDegreeCurricularPlan();

    @Atomic
    public static InfoDegreeCurricularPlan runReadDegreeCurricularPlan(String externalId) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(externalId);
        } catch (NotAuthorizedException ex1) {
            try {
                CoordinatorAuthorizationFilter.instance.execute();
                return serviceInstance.run(externalId);
            } catch (NotAuthorizedException ex2) {
                try {
                    GEPAuthorizationFilter.instance.execute();
                    return serviceInstance.run(externalId);
                } catch (NotAuthorizedException ex3) {
                    try {
                        OperatorAuthorizationFilter.instance.execute();
                        return serviceInstance.run(externalId);
                    } catch (NotAuthorizedException ex4) {
                        throw ex4;
                    }
                }
            }
        }
    }

}