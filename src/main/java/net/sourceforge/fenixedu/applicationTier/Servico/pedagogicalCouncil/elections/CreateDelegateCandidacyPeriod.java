package net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.elections;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateDelegateCandidacyPeriod {

    @Atomic
    public static void run(ElectionPeriodBean bean) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        final String degreeOID = bean.getDegree().getExternalId();

        run(bean, degreeOID);
    }

    @Atomic
    public static void run(ElectionPeriodBean bean, String degreeOID) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final Degree degree = FenixFramework.getDomainObject(degreeOID);

        try {
            YearDelegateElection.createDelegateElectionWithCandidacyPeriod(degree, executionYear, bean.getStartDate(),
                    bean.getEndDate(), bean.getCurricularYear());

        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage(), ex.getArgs());
        }
    }
}