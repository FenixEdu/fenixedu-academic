package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.predicates.OrPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

public enum PhdThesisPredicateContainer implements PredicateContainer<PhdIndividualProgramProcess> {

    PROVISIONAL_THESIS_DELIVERED {
	public Predicate<PhdIndividualProgramProcess> getPredicate() {

	    final PhdThesisPredicate waitingProvisionalVersionFilter = new PhdThesisPredicate(
		    PhdThesisProcessStateType.WAITING_FOR_THESIS_PROVISIONAL_VERSION_APPROVAL);
	    final PhdThesisPredicate waitingDiscussionDateFilter = new PhdThesisPredicate(
		    PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING);

	    return new OrPredicate<PhdIndividualProgramProcess>() {
		{
		    add(waitingProvisionalVersionFilter);
		    add(waitingDiscussionDateFilter);
		}
	    };
	}
    },

    DISCUSSION_SCHEDULED {
	public Predicate<PhdIndividualProgramProcess> getPredicate() {
	    return new PhdThesisPredicate(PhdThesisProcessStateType.THESIS_DISCUSSION_DATE_SCHECULED);
	}
    };

    private static class PhdThesisPredicate extends InlinePredicate<PhdIndividualProgramProcess, PhdThesisProcessStateType> {
	public PhdThesisPredicate(PhdThesisProcessStateType thesisState) {
	    super(thesisState);
	}

	@Override
	public boolean eval(PhdIndividualProgramProcess process) {
	    return checkState(process) && checkValue(process);
	}

	private boolean checkState(PhdIndividualProgramProcess process) {
	    return process.getActiveState().isActive() && process.hasThesisProcess();
	}

	private boolean checkValue(PhdIndividualProgramProcess process) {
	    return process.getThesisProcess().getActiveState().equals(getValue());
	}
    }
}
