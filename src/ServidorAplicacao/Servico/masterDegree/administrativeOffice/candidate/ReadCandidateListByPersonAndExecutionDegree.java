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
import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateListByPersonAndExecutionDegree implements IService {

    public InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree, InfoPerson infoPerson,
            Integer number) throws FenixServiceException {

        ISuportePersistente sp = null;
        IMasterDegreeCandidate result = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the candidates

            ICursoExecucao executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
            IPessoa person = Cloner.copyInfoPerson2IPerson(infoPerson);

            result = sp.getIPersistentMasterDegreeCandidate().readByExecutionDegreeAndPersonAndNumber(
                    executionDegree, person, number);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner
                .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(result);
        Iterator situationIterator = result.getSituations().iterator();
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

        return infoMasterDegreeCandidate;

    }

}