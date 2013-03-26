package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;

public class DeleteEquivalencePlanEntry extends FenixService {

    public void run(final EquivalencePlanEntry EquivalencePlanEntry) {
        if (EquivalencePlanEntry != null) {
            EquivalencePlanEntry.delete();
        }
    }

}
