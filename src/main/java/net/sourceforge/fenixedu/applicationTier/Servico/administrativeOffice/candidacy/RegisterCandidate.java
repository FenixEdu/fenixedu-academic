/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.RegisterCandidacyBean;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;
import net.sourceforge.fenixedu.domain.util.workflow.StateMachine;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegisterCandidate {

    @Atomic
    public static void run(RegisterCandidacyBean candidacyBean) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        StateMachine.execute(candidacyBean.getCandidacy().getActiveCandidacySituation(), new StateBean(
                CandidacySituationType.REGISTERED.name()));

        final Registration registration = candidacyBean.getCandidacy().getRegistration();
        registration.setStartDate(candidacyBean.getStartDate() != null ? candidacyBean.getStartDate() : new YearMonthDay());
        registration.setEnrolmentModelForCurrentExecutionYear(candidacyBean.getEnrolmentModel());
    }

}