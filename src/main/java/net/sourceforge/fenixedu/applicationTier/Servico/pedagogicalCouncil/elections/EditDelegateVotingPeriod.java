package net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.elections;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditDelegateVotingPeriod {

    @Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
    @Service
    public static void run(ElectionPeriodBean bean) throws FenixServiceException {

        DelegateElection election = bean.getElection();

        try {
            election.editVotingPeriod(bean.getStartDate(), bean.getEndDate(), election.getLastVotingPeriod());
        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage(), ex.getArgs());
        }
    }

    @Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
    @Service
    public static void run(ElectionPeriodBean bean, String degreeOID) throws FenixServiceException {
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final Degree degree = RootDomainObject.getInstance().readDegreeByOID(Integer.parseInt(degreeOID));

        DelegateElection electionToEdit =
                degree.getYearDelegateElectionWithLastVotingPeriod(executionYear, bean.getCurricularYear());

        if (electionToEdit != null) {
            bean.setElection(electionToEdit);
        } else {
            throw new FenixServiceException("error.elections.edit.electionNotFound", new String[] { degree.getSigla(),
                    bean.getCurricularYear().getYear().toString() });
        }

        run(bean);
    }
}