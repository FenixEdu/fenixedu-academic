/*
 * Created on 14/Mar/2003
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.ExecutionDegree;
import Dominio.ICandidateSituation;
import Dominio.IExecutionDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            sp = SuportePersistenteOJB.getInstance();

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
            InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner
                    .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(candidateSituation
                            .getMasterDegreeCandidate());
            infoMasterDegreeCandidate.setInfoCandidateSituation(Cloner
                    .copyICandidateSituation2InfoCandidateSituation(candidateSituation));
            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;

    }
}