package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.List;

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
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.State;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadMasterDegreeCandidate {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree, Integer candidateNumber,
            Specialization degreeType) {

        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());

        MasterDegreeCandidate masterDegreeCandidate =
                executionDegree.getMasterDegreeCandidateBySpecializationAndCandidateNumber(degreeType, candidateNumber);

        return getInfoMasterDegreeCandidate(masterDegreeCandidate);
    }

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree, InfoPerson infoPerson) {

        final Person person = (Person) RootDomainObject.getInstance().readPartyByOID(infoPerson.getIdInternal());
        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());

        final MasterDegreeCandidate masterDegreeCandidate = person.getMasterDegreeCandidateByExecutionDegree(executionDegree);

        return getInfoMasterDegreeCandidate(masterDegreeCandidate);
    }

    private static InfoMasterDegreeCandidate getInfoMasterDegreeCandidate(final MasterDegreeCandidate masterDegreeCandidate) {

        final State candidateState = new State(State.ACTIVE);

        final InfoMasterDegreeCandidate infoMasterDegreeCandidate =
                InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);

        final List<InfoCandidateSituation> candidateSituations = new ArrayList<InfoCandidateSituation>();
        for (final CandidateSituation candidateSituation : masterDegreeCandidate.getSituationsSet()) {
            InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation.newInfoFromDomain(candidateSituation);
            candidateSituations.add(infoCandidateSituation);
            if (candidateSituation.getValidation().equals(candidateState)) {
                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
            }
        }
        infoMasterDegreeCandidate.setSituationList(candidateSituations);

        return infoMasterDegreeCandidate;
    }

}