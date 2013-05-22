/*
 * Created on 1/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlan extends FenixService {

    protected InfoDegreeCurricularPlan run(final Integer idInternal) throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(idInternal);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
    }

    // Service Invokers migrated from Berserk

    private static final ReadDegreeCurricularPlan serviceInstance = new ReadDegreeCurricularPlan();

    @Service
    public static InfoDegreeCurricularPlan runReadDegreeCurricularPlan(Integer idInternal) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(idInternal);
        } catch (NotAuthorizedException ex1) {
            try {
                CoordinatorAuthorizationFilter.instance.execute();
                return serviceInstance.run(idInternal);
            } catch (NotAuthorizedException ex2) {
                try {
                    GEPAuthorizationFilter.instance.execute();
                    return serviceInstance.run(idInternal);
                } catch (NotAuthorizedException ex3) {
                    try {
                        OperatorAuthorizationFilter.instance.execute();
                        return serviceInstance.run(idInternal);
                    } catch (NotAuthorizedException ex4) {
                        throw ex4;
                    }
                }
            }
        }
    }

}