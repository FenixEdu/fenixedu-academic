package net.sourceforge.fenixedu.applicationTier.Servico.cardGeneration;

import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import pt.ist.fenixframework.Atomic;

public class DeleteCardGenerationEntryService {

    @Atomic
    public static void run(final CardGenerationEntry cardGenerationEntry) {
        cardGenerationEntry.delete();
    }

}
