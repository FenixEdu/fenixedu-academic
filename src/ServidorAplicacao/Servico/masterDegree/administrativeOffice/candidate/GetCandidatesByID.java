/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoCandidateSituation;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetCandidatesByID implements IServico {

    private static GetCandidatesByID servico = new GetCandidatesByID();

    /**
     * The singleton access method of this class.
     */
    public static GetCandidatesByID getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private GetCandidatesByID() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "GetCandidatesByID";
    }

    public InfoMasterDegreeCandidate run(Integer candidateID)
            throws FenixServiceException {

        ISuportePersistente sp = null;
        IMasterDegreeCandidate masterDegreeCandidate = null;

        if (candidateID == null) {
            throw new NonExistingServiceException();
        }

        try {
            sp = SuportePersistenteOJB.getInstance();

            masterDegreeCandidate = (IMasterDegreeCandidate) sp
                    .getIPersistentMasterDegreeCandidate().readByOID(
                            MasterDegreeCandidate.class, candidateID);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error", ex);

            throw newEx;
        }

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner
                .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
        Iterator situationIterator = masterDegreeCandidate.getSituations()
                .iterator();
        List situations = new ArrayList();
        while (situationIterator.hasNext()) {
            InfoCandidateSituation infoCandidateSituation = Cloner
                    .copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) situationIterator
                            .next());
            situations.add(infoCandidateSituation);

            // Check if this is the Active Situation
            if (infoCandidateSituation.getValidation().equals(
                    new State(State.ACTIVE))) {

                infoMasterDegreeCandidate
                        .setInfoCandidateSituation(infoCandidateSituation);
            }

        }
        infoMasterDegreeCandidate.setSituationList(situations);
        return infoMasterDegreeCandidate;
    }
}