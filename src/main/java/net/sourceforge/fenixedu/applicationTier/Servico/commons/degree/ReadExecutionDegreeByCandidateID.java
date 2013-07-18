package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;


import net.sourceforge.fenixedu.applicationTier.Filtro.ReadExecutionDegreeByCandidateIDAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByCandidateID {

    protected InfoExecutionDegree run(Integer candidateID) throws NonExistingServiceException {

        MasterDegreeCandidate masterDegreeCandidate = RootDomainObject.getInstance().readMasterDegreeCandidateByOID(candidateID);

        ExecutionDegree executionDegree =
                RootDomainObject.getInstance().readExecutionDegreeByOID(masterDegreeCandidate.getExecutionDegree().getIdInternal());

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

    // Service Invokers migrated from Berserk

    private static final ReadExecutionDegreeByCandidateID serviceInstance = new ReadExecutionDegreeByCandidateID();

    @Service
    public static InfoExecutionDegree runReadExecutionDegreeByCandidateID(Integer candidateID) throws NonExistingServiceException  , NotAuthorizedException {
        ReadExecutionDegreeByCandidateIDAuthorizationFilter.instance.execute(candidateID);
        return serviceInstance.run(candidateID);
    }

}