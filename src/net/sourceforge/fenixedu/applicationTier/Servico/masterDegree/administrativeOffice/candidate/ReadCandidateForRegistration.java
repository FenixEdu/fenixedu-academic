/*
 * Created on 14/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateForRegistration implements IService {

    /**
     * The actor of this class.
     */
    public ReadCandidateForRegistration() {
    }

    public List run(Integer executionDegreeCode) throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // Get the Actual Execution Year

            IExecutionDegree executionDegree = new ExecutionDegree();
            executionDegree.setIdInternal(executionDegreeCode);

            result = sp.getIPersistentCandidateSituation().readCandidateListforRegistration(
                    executionDegree);
        } catch (ExcepcaoPersistencia ex) {

            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (result == null) {
            throw new NonExistingServiceException();
        }

        List candidateList = new ArrayList();
        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            ICandidateSituation candidateSituation = (ICandidateSituation) iterator.next();
            InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(candidateSituation.getMasterDegreeCandidate());
            infoMasterDegreeCandidate.setInfoCandidateSituation(Cloner
                    .copyICandidateSituation2InfoCandidateSituation(candidateSituation));
            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;

    }
}