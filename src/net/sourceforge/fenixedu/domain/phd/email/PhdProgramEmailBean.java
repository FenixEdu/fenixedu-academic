package net.sourceforge.fenixedu.domain.phd.email;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdProgramEmailBean extends PhdEmailBean {

	private static final long serialVersionUID = 1L;

	private PhdProgram phdProgram;
	private List<PhdIndividualProgramProcess> selectedElements;
	private boolean showProgramsChoice = true;

	public PhdProgramEmailBean() {

	}

	public PhdProgramEmailBean(PhdProgram phdProgram) {
		this.phdProgram = phdProgram;
	}

	public PhdProgramEmailBean(PhdProgramEmail email) {
		this.subject = email.getFormattedSubject().getContent(Language.pt);
		this.message = email.getFormattedBody().getContent(Language.pt);
		this.bccs = email.getBccs();
		this.creationDate = email.getWhenCreated();
		this.creator = email.getPerson();
	}

	public PhdProgram getPhdProgram() {
		return phdProgram;
	}

	public void setPhdProgram(PhdProgram phdProgram) {
		this.phdProgram = phdProgram;
	}

	public List<PhdIndividualProgramProcess> getSelectedElements() {
		if (selectedElements != null) {
			return selectedElements;
		}

		return new ArrayList<PhdIndividualProgramProcess>();
	}

	public void setSelectedElements(List<PhdIndividualProgramProcess> selectedElements) {
		this.selectedElements = selectedElements;
	}

	public AndPredicate<PhdIndividualProgramProcess> getManagedPhdProgramsPredicate() {
		final AndPredicate<PhdIndividualProgramProcess> result = new AndPredicate<PhdIndividualProgramProcess>();
		if (getPhdProgram() != null) {
			result.add(new InlinePredicate<PhdIndividualProgramProcess, PhdProgram>(getPhdProgram()) {

				@Override
				public boolean eval(PhdIndividualProgramProcess toEval) {
					if (toEval.hasPhdProgram()) {
						return getValue().equals(toEval.getPhdProgram());
					} else if (toEval.hasPhdProgramFocusArea()) {
						return !CollectionUtils.intersection(Collections.singleton(getValue()),
								toEval.getPhdProgramFocusArea().getPhdPrograms()).isEmpty();
					} else {
						return false;
					}
				}
			});
		}

		return result;
	}

	public void updateBean() {
		setCreationDate(new DateTime());
		setCreator(AccessControl.getPerson());
	}

	public String getBccsWithSelectedParticipants() {
		String bccs = getBccs() == null ? null : getBccs().replace(" ", "");

		if (!StringUtils.isEmpty(bccs)) {
			bccs += ",";
		}

		for (PhdIndividualProgramProcess process : getSelectedElements()) {
			if (process.getPerson().getEmailForSendingEmails() != null) {
				bccs += process.getPerson().getEmailForSendingEmails();
				bccs += ",";
			}
		}

		if (bccs.endsWith(",")) {
			bccs = bccs.substring(0, bccs.length() - 1);
		}

		return bccs;
	}

	public boolean isShowProgramsChoice() {
		return showProgramsChoice;
	}

	public void setShowProgramsChoice(boolean showProgramsChoice) {
		this.showProgramsChoice = showProgramsChoice;
	}

}
