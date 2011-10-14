package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.predicates.OrPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

public enum PhdInactivePredicateContainer implements PredicateContainer<PhdIndividualProgramProcess> {

    SUSPENDED {
	public Predicate<PhdIndividualProgramProcess> getPredicate() {
	    return new PhdInactivePredicate(PhdIndividualProgramProcessState.SUSPENDED);
	}
    },

    CONCLUDED {
	public Predicate<PhdIndividualProgramProcess> getPredicate() {
	    return new PhdInactivePredicate(PhdIndividualProgramProcessState.CONCLUDED);
	}
    },

    CONCLUDED_THIS_YEAR {
	public Predicate<PhdIndividualProgramProcess> getPredicate() {
	    return new PhdInactivePredicate(PhdIndividualProgramProcessState.CONCLUDED) {
		@Override
		public boolean eval(PhdIndividualProgramProcess process) {
		    return super.eval(process) && process.getConclusionYear().equals(ExecutionYear.readCurrentExecutionYear());
		}
	    };
	}
    },

    ABOLISHED {
	public Predicate<PhdIndividualProgramProcess> getPredicate() {
	    final PhdInactivePredicate cancelledPredicate = new PhdInactivePredicate(PhdIndividualProgramProcessState.CANCELLED);
	    final PhdInactivePredicate flunkedPredicate = new PhdInactivePredicate(PhdIndividualProgramProcessState.FLUNKED);
	    final PhdInactivePredicate notAdmittedPredicate = new PhdInactivePredicate(
		    PhdIndividualProgramProcessState.NOT_ADMITTED);

	    return new OrPredicate<PhdIndividualProgramProcess>() {
		{
		    add(cancelledPredicate);
		    add(flunkedPredicate);
		    add(notAdmittedPredicate);
		}
	    };
	}
    };

    private static class PhdInactivePredicate extends
	    InlinePredicate<PhdIndividualProgramProcess, PhdIndividualProgramProcessState> {
	public PhdInactivePredicate(PhdIndividualProgramProcessState processState) {
	    super(processState);
	}

	@Override
	public boolean eval(PhdIndividualProgramProcess process) {
	    return checkState(process) && checkValue(process);
	}

	private boolean checkState(PhdIndividualProgramProcess process) {
	    return !process.getActiveState().isPhdActive();
	}

	private boolean checkValue(PhdIndividualProgramProcess process) {
	    return process.getActiveState().equals(getValue());
	}
    }
}
