package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByCandidateID extends Service {

    public InfoExecutionDegree run(Integer candidateID) throws NonExistingServiceException,
            ExcepcaoPersistencia {

        ExecutionDegree executionDegree = null;

        MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(
                        candidateID);

        executionDegree = rootDomainObject.readExecutionDegreeByOID(masterDegreeCandidate.getExecutionDegree().getIdInternal());

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        return InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan.newInfoFromDomain(executionDegree);
    }

}