package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.State;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadCandidateListByPersonAndExecutionDegree {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree, InfoPerson infoPerson, Integer number)
            throws FenixServiceException {

        final Person person = (Person) RootDomainObject.getInstance().readPartyByOID(infoPerson.getIdInternal());
        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());

        final State candidateState = new State(State.ACTIVE);

        final MasterDegreeCandidate masterDegreeCandidate = person.getMasterDegreeCandidateByExecutionDegree(executionDegree);
        InfoMasterDegreeCandidate infoMasterDegreeCandidate =
                InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);

        List<InfoCandidateSituation> infoCandidateSituations = new ArrayList<InfoCandidateSituation>();
        for (final CandidateSituation candidateSituation : masterDegreeCandidate.getSituationsSet()) {
            final InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation.newInfoFromDomain(candidateSituation);
            infoCandidateSituations.add(infoCandidateSituation);

            if (candidateSituation.getValidation().equals(candidateState)) {
                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
            }
        }
        infoMasterDegreeCandidate.setSituationList(infoCandidateSituations);

        return infoMasterDegreeCandidate;
    }
}