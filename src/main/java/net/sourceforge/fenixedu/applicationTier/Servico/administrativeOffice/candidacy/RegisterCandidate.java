/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;


import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.RegisterCandidacyBean;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;
import net.sourceforge.fenixedu.domain.util.workflow.StateMachine;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegisterCandidate {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Atomic
    public static void run(RegisterCandidacyBean candidacyBean) {

        StateMachine.execute(candidacyBean.getCandidacy().getActiveCandidacySituation(), new StateBean(
                CandidacySituationType.REGISTERED.name()));

        final Registration registration = candidacyBean.getCandidacy().getRegistration();
        registration.setStartDate(candidacyBean.getStartDate() != null ? candidacyBean.getStartDate() : new YearMonthDay());
        registration.setEnrolmentModelForCurrentExecutionYear(candidacyBean.getEnrolmentModel());
    }

}