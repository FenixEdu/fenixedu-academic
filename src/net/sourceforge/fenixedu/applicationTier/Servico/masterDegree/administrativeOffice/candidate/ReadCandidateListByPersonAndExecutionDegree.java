/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateListByPersonAndExecutionDegree implements IService {

    public InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree, InfoPerson infoPerson,
            Integer number) throws FenixServiceException {

        ISuportePersistente sp = null;
        IMasterDegreeCandidate result = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // Read the candidates

            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
            IPerson person = Cloner.copyInfoPerson2IPerson(infoPerson);

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