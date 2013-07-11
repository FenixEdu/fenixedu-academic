package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ReadCandidatesForSelectionAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.util.SituationName;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidatesForSelection {

    protected List run(String executionDegreeID, List<SituationName> situationNames) throws FenixServiceException {

        // Read the candidates

        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);

        List<CandidateSituation> resultTemp = executionDegree.getCandidateSituationsInSituation(situationNames);

        if (resultTemp.isEmpty()) {
            throw new NonExistingServiceException();
        }

        Iterator candidateIterator = resultTemp.iterator();
        List result = new ArrayList();
        while (candidateIterator.hasNext()) {
            CandidateSituation candidateSituation = (CandidateSituation) candidateIterator.next();
            result.add(InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(candidateSituation.getMasterDegreeCandidate()));
        }

        return result;

    }

    // Service Invokers migrated from Berserk

    private static final ReadCandidatesForSelection serviceInstance = new ReadCandidatesForSelection();

    @Atomic
    public static List runReadCandidatesForSelection(String executionDegreeID, List<SituationName> situationNames)
            throws FenixServiceException, NotAuthorizedException {
        ReadCandidatesForSelectionAuthorizationFilter.instance.execute(executionDegreeID, situationNames);
        return serviceInstance.run(executionDegreeID, situationNames);
    }

}