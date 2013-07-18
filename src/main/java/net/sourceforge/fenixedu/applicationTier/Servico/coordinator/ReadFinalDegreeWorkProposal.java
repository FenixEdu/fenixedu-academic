/*
 * Created on 2004/03/09
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;


import net.sourceforge.fenixedu.applicationTier.Filtro.ReadFinalDegreeWorkProposalAuthorization;
import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.AccessFinalDegreeWorkProposalAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 * 
 */
public class ReadFinalDegreeWorkProposal {

    protected InfoProposal run(Integer finalDegreeWorkProposalOID) throws FenixServiceException {
        return InfoProposal.newInfoFromDomain(RootDomainObject.getInstance().readProposalByOID(finalDegreeWorkProposalOID));
    }

    // Service Invokers migrated from Berserk

    private static final ReadFinalDegreeWorkProposal serviceInstance = new ReadFinalDegreeWorkProposal();

    @Service
    public static InfoProposal runReadFinalDegreeWorkProposal(Integer DegreeWorkProposalOID) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ReadFinalDegreeWorkProposalAuthorization.instance.execute(DegreeWorkProposalOID);
            return serviceInstance.run(DegreeWorkProposalOID);
        } catch (NotAuthorizedException ex1) {
            try {
                AccessFinalDegreeWorkProposalAuthorizationFilter.instance.execute(DegreeWorkProposalOID);
                return serviceInstance.run(DegreeWorkProposalOID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}