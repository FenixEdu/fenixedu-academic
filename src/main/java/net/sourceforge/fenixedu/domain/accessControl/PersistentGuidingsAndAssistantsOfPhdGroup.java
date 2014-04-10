package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class PersistentGuidingsAndAssistantsOfPhdGroup extends PersistentGuidingsAndAssistantsOfPhdGroup_Base {
    protected PersistentGuidingsAndAssistantsOfPhdGroup(PhdIndividualProgramProcess phdIndividualProgramProcess) {
        super();
        setPhdIndividualProgramProcess(phdIndividualProgramProcess);
    }

    @Override
    public Group toGroup() {
        return GuidingsAndAssistantsOfPhdGroup.get(getPhdIndividualProgramProcess());
    }

    @Override
    protected void gc() {
        setPhdIndividualProgramProcess(null);
        super.gc();
    }

    public static PersistentGuidingsAndAssistantsOfPhdGroup getInstance(
            final PhdIndividualProgramProcess phdIndividualProgramProcess) {
        PersistentGuidingsAndAssistantsOfPhdGroup instance = phdIndividualProgramProcess.getGuidingsAndAssistantsGroup();
        return instance != null ? instance : create(phdIndividualProgramProcess);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentGuidingsAndAssistantsOfPhdGroup create(final PhdIndividualProgramProcess phdIndividualProgramProcess) {
        PersistentGuidingsAndAssistantsOfPhdGroup instance = phdIndividualProgramProcess.getGuidingsAndAssistantsGroup();
        return instance != null ? instance : new PersistentGuidingsAndAssistantsOfPhdGroup(phdIndividualProgramProcess);
    }
}
