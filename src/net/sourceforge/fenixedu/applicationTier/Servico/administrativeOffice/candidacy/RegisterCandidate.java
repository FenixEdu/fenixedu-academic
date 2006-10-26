/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.RegisterCandidacyBean;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.StateMachine;

import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegisterCandidate extends Service {

    public void run(RegisterCandidacyBean candidacyBean) {

	StateMachine.execute(candidacyBean.getCandidacy().getActiveCandidacySituation(),
		CandidacySituationType.REGISTERED.toString());

	final Registration registration = candidacyBean.getCandidacy().getRegistration();
	registration.setStartDate(candidacyBean.getStartDate() != null ? candidacyBean.getStartDate()
		: new YearMonthDay());
	registration.setEnrolmentModelForCurrentExecutionYear(candidacyBean.getEnrolmentModel());
    }

}
