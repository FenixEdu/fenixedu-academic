package net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.elections;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.NewRoundElectionBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateDelegateVotingPeriod {

    @Atomic
    public static void run(ElectionPeriodBean bean) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);

        try {
            DelegateElection election = bean.getElection();

            if (election != null) {
                election.createVotingPeriod(bean.getStartDate(), bean.getEndDate());
            }

        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage(), ex.getArgs());
        }
    }

    @Atomic
    public static void run(ElectionPeriodBean bean, String degreeOID) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final Degree degree = FenixFramework.getDomainObject(degreeOID);

        DelegateElection election =
                degree.getYearDelegateElectionWithLastCandidacyPeriod(executionYear, bean.getCurricularYear());
        bean.setElection(election);

        run(bean);

    }

    @Atomic
    public static void run(ElectionPeriodBean bean, NewRoundElectionBean newRoundElectionsCandidatesBean,
            NewRoundElectionBean newRoundElectionsNotCandidatesBean) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);

        run(bean);
        // Gets a new period. This period have been created by method "run"
        DelegateElectionVotingPeriod votingPeriod = bean.getElection().getLastVotingPeriod();

        if (newRoundElectionsCandidatesBean == null && newRoundElectionsNotCandidatesBean == null) {
            throw new DomainException("error.election.not.have.candidates");
        }
        if (newRoundElectionsCandidatesBean != null && !newRoundElectionsCandidatesBean.containsCandidates()
                && newRoundElectionsNotCandidatesBean != null && !newRoundElectionsNotCandidatesBean.containsCandidates()) {
            throw new DomainException("error.election.not.have.candidates");
        }
        for (Student candidate : newRoundElectionsCandidatesBean.getCandidates()) {
            votingPeriod.addCandidatesForNewRoundElections(candidate);
        }

        for (Student candidate : newRoundElectionsNotCandidatesBean.getCandidates()) {
            votingPeriod.addCandidatesForNewRoundElections(candidate);
        }

    }
}