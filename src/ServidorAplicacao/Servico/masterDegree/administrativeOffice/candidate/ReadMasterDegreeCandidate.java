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
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoPerson;
import DataBeans.util.Cloner;
import Dominio.ICandidateSituation;
import Dominio.IExecutionDegree;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPerson;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Specialization;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadMasterDegreeCandidate implements IService {

    public InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree,
            Integer candidateNumber, Specialization degreeType) throws FenixServiceException {

        ISuportePersistente sp = null;
        IMasterDegreeCandidate masterDegreeCandidate = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);

            // Read the candidates

            masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                    .readByNumberAndExecutionDegreeAndSpecialization(candidateNumber, executionDegree,
                            degreeType);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (masterDegreeCandidate == null)
            return null;
        Iterator iterator = masterDegreeCandidate.getSituations().iterator();
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner
                .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
        List situations = new ArrayList();
        while (iterator.hasNext()) {
            InfoCandidateSituation infoCandidateSituation = Cloner
                    .copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) iterator
                            .next());
            situations.add(infoCandidateSituation);

            // Check if this is the Active Situation
            if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
        }
        infoMasterDegreeCandidate.setSituationList(situations);

        return infoMasterDegreeCandidate;
    }

    public InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree, InfoPerson infoPerson)
            throws FenixServiceException {

        ISuportePersistente sp = null;
        IMasterDegreeCandidate masterDegreeCandidate = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
            IPerson person = Cloner.copyInfoPerson2IPerson(infoPerson);

            // Read the candidates

            masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                    .readByExecutionDegreeAndPerson(executionDegree, person);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (masterDegreeCandidate == null)
            return null;
        Iterator iterator = masterDegreeCandidate.getSituations().iterator();
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner
                .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
        List situations = new ArrayList();
        while (iterator.hasNext()) {
            InfoCandidateSituation infoCandidateSituation = Cloner
                    .copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) iterator
                            .next());
            situations.add(infoCandidateSituation);

            // Check if this is the Active Situation
            if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
        }
        infoMasterDegreeCandidate.setSituationList(situations);

        return infoMasterDegreeCandidate;
    }

}