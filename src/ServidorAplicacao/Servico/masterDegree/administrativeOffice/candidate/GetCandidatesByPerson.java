/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCandidateSituation;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetCandidatesByPerson implements IService {

    public List run(Integer personID) throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            result = sp.getIPersistentMasterDegreeCandidate().readByPersonID(personID);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        List candidateList = new ArrayList();
        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) iterator.next();
            InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner
                    .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
            Iterator situationIterator = masterDegreeCandidate.getSituations().iterator();
            List situations = new ArrayList();
            while (situationIterator.hasNext()) {
                InfoCandidateSituation infoCandidateSituation = Cloner
                        .copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) situationIterator
                                .next());
                situations.add(infoCandidateSituation);

                // Check if this is the Active Situation
                if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                    infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
            }
            infoMasterDegreeCandidate.setSituationList(situations);
            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;
    }

}