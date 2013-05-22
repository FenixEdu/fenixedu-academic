/*
 * Created on 24/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.MasterDegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.Guide;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 */
public class ReadGuide extends FenixService {

    protected InfoGuide run(Integer guideId) throws FenixServiceException {
        Guide guide;
        InfoGuide infoGuide = null;
        guide = rootDomainObject.readGuideByOID(guideId);
        if (guide == null) {
            throw new InvalidArgumentsServiceException();
        }
        infoGuide = InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(guide);

        return infoGuide;
    }

    // Service Invokers migrated from Berserk

    private static final ReadGuide serviceInstance = new ReadGuide();

    @Service
    public static InfoGuide runReadGuide(Integer guideId) throws FenixServiceException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(guideId);
        } catch (NotAuthorizedException ex1) {
            try {
                MasterDegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
                return serviceInstance.run(guideId);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}