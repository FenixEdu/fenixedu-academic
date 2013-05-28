package net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.elections;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateDelegateCandidacyPeriod {

    @Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
    @Service
    public static void run(ElectionPeriodBean bean) throws FenixServiceException {
        final Integer degreeOID = bean.getDegree().getExternalId();

        run(bean, degreeOID.toString());
    }

    @Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
    @Service
    public static void run(ElectionPeriodBean bean, String degreeOID) throws FenixServiceException {
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final Degree degree = RootDomainObject.getInstance().readDegreeByOID(Integer.parseInt(degreeOID));

        try {
            YearDelegateElection.createDelegateElectionWithCandidacyPeriod(degree, executionYear, bean.getStartDate(),
                    bean.getEndDate(), bean.getCurricularYear());

        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage(), ex.getArgs());
        }
    }
}