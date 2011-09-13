package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessStateType;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.predicates.OrPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

public enum PhdSeminarPredicateContainer implements PredicateContainer<PhdIndividualProgramProcess> {

    SEMINAR_PROCESS_STARTED {
	public Predicate<PhdIndividualProgramProcess> getPredicate() {
	    final PhdSeminarPredicate waitingComissionConstitutionPredicate = new PhdSeminarPredicate(
		    PublicPresentationSeminarProcessStateType.WAITING_FOR_COMMISSION_CONSTITUTION);
	    final PhdSeminarPredicate waitingComissionValidationPredicate = new PhdSeminarPredicate(
		    PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION);
	    final PhdSeminarPredicate comissionvalidatedPredicate = new PhdSeminarPredicate(
		    PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED);
	    final PhdSeminarPredicate presentationScheduledPredicate = new PhdSeminarPredicate(
		    PublicPresentationSeminarProcessStateType.PUBLIC_PRESENTATION_DATE_SCHEDULED);

	    return new OrPredicate<PhdIndividualProgramProcess>() {
		{
		    add(waitingComissionConstitutionPredicate);
		    add(waitingComissionValidationPredicate);
		    add(comissionvalidatedPredicate);
		    add(presentationScheduledPredicate);
		}
	    };
	}
    },

    AFTER_FIRST_SEMINAR_REUNION {
	public Predicate<PhdIndividualProgramProcess> getPredicate() {

	    return new PhdSeminarPredicate(PublicPresentationSeminarProcessStateType.REPORT_WAITING_FOR_VALIDATION) {
		@Override
		public boolean eval(PhdIndividualProgramProcess process) {
		    return !process.hasThesisProcess() && super.checkState(process)
			    && process.getSeminarProcess().hasState(getValue());
		}
	    };
	}
    };

    private static class PhdSeminarPredicate extends
	    InlinePredicate<PhdIndividualProgramProcess, PublicPresentationSeminarProcessStateType> {
	public PhdSeminarPredicate(PublicPresentationSeminarProcessStateType seminarState) {
	    super(seminarState);
	}

	@Override
	public boolean eval(PhdIndividualProgramProcess process) {
	    return checkState(process) && checkValue(process);
	}

	private boolean checkState(PhdIndividualProgramProcess process) {
	    return process.getActiveState().isActive() && process.hasSeminarProcess();
	}

	private boolean checkValue(PhdIndividualProgramProcess process) {
	    return process.getSeminarProcess().getActiveState().equals(getValue());
	}
    }
}
