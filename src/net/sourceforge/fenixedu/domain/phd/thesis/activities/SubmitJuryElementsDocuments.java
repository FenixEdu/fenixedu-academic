/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

public class SubmitJuryElementsDocuments extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	if (process.isJuryValidated()) {
	    throw new PreConditionNotValidException();
	}

	if (PhdThesisProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    return;
	}

	if (userView.getPerson() != null
		&& process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
	    return;
	}

	throw new PreConditionNotValidException();
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

	boolean anyDocumentSubmitted = false;

	for (final PhdProgramDocumentUploadBean each : bean.getDocuments()) {
	    if (each.hasAnyInformation()) {

		process.addDocument(each, userView.getPerson());
		alertIfNecessary(process, each, userView.getPerson());

		anyDocumentSubmitted = true;
	    }
	}

	if (anyDocumentSubmitted) {
	    if (!process.hasState(PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION)) {
		process.createState(PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION, userView.getPerson(), bean
			.getRemarks());
	    }
	}

	return process;
    }

    private void alertIfNecessary(PhdThesisProcess process, PhdProgramDocumentUploadBean each, Person person) {

	switch (each.getType()) {
	case JURY_PRESIDENT_ELEMENT:
	    AlertService.alertCoordinator(process.getIndividualProgramProcess(),
		    "message.phd.alert.request.jury.president.subject", "message.phd.alert.request.jury.president.body");
	    break;

	case JURY_ELEMENTS:
	    if (process.getIndividualProgramProcess().isCoordinatorForPhdProgram(person)) {
		AlertService.alertAcademicOffice(process.getIndividualProgramProcess(), getThesisProcessPermission(),
			"message.phd.alert.jury.elements.submitted.subject", "message.phd.alert.jury.elements.submitted.body");
	    }
	    break;
	}
    }
}