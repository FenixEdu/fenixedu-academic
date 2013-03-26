package net.sourceforge.fenixedu.presentationTier.Action.phd;

import static net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType.JURY_VALIDATED;
import static net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION;
import static net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType.NEW;
import static net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType.THESIS_DISCUSSION_DATE_SCHECULED;
import static net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType.WAITING_FOR_JURY_CONSTITUTION;
import static net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK;
import static net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING;
import static net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType.WAITING_FOR_THESIS_MEETING_SCHEDULING;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.predicates.OrPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

public enum PhdThesisPredicateContainer implements PredicateContainer<PhdIndividualProgramProcess> {

    PROVISIONAL_THESIS_DELIVERED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {

            return new OrPredicate<PhdIndividualProgramProcess>() {
                {

                    // can remove some states if we want to distinguish each
                    // situation
                    add(new PhdThesisPredicate(NEW));
                    add(new PhdThesisPredicate(WAITING_FOR_JURY_CONSTITUTION));
                    add(new PhdThesisPredicate(JURY_WAITING_FOR_VALIDATION));
                    add(new PhdThesisPredicate(JURY_VALIDATED));

                    add(new PhdThesisPredicate(WAITING_FOR_JURY_REPORTER_FEEDBACK));
                    add(new PhdThesisPredicate(WAITING_FOR_THESIS_MEETING_SCHEDULING));
                    add(new PhdThesisPredicate(WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING));
                }
            };
        }
    },

    DISCUSSION_SCHEDULED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {
            return new PhdThesisPredicate(THESIS_DISCUSSION_DATE_SCHECULED);
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
