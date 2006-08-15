package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.State;

public class GetCandidatesByPerson extends Service {

    public List<InfoMasterDegreeCandidate> run(Integer personID) throws FenixServiceException,
            ExcepcaoPersistencia {

        final Person person = (Person) rootDomainObject.readPartyByOID(personID);
        final State activeCandidateSituationState = new State(State.ACTIVE);

        final List<InfoMasterDegreeCandidate> result = new ArrayList<InfoMasterDegreeCandidate>();

        for (final MasterDegreeCandidate masterDegreeCandidate : person.getMasterDegreeCandidatesSet()) {

            final InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(masterDegreeCandidate);
            infoMasterDegreeCandidate
                    .setInfoExecutionDegree(InfoExecutionDegree
                            .newInfoFromDomain(masterDegreeCandidate.getExecutionDegree()));

            final List<InfoCandidateSituation> infoCandidateSituations = new ArrayList<InfoCandidateSituation>();
            for (final CandidateSituation candidateSituation : masterDegreeCandidate.getSituationsSet()) {
                final InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
                        .newInfoFromDomain(candidateSituation);
                infoCandidateSituations.add(infoCandidateSituation);

                if (candidateSituation.getValidation().equals(activeCandidateSituationState)) {
                    infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
                }
            }
            infoMasterDegreeCandidate.setSituationList(infoCandidateSituations);
            result.add(infoMasterDegreeCandidate);
        }
        return result;
    }

}