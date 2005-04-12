package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByCandidateID implements IService {

    public InfoExecutionDegree run(Integer candidateID) throws NonExistingServiceException {

        IExecutionDegree executionDegree = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                    .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                            candidateID);

            executionDegree = (IExecutionDegree) sp.getIPersistentExecutionDegree().readByOID(
                    ExecutionDegree.class, masterDegreeCandidate.getExecutionDegree().getIdInternal());

        } catch (ExcepcaoPersistencia ex) {
            throw new RuntimeException(ex);
        }

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}