package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.fenixedu.bennu.core.groups.Group;

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
        return singleton(() -> Optional.ofNullable(phdIndividualProgramProcess.getGuidingsAndAssistantsGroup()),
                () -> new PersistentGuidingsAndAssistantsOfPhdGroup(phdIndividualProgramProcess));
    }
}
