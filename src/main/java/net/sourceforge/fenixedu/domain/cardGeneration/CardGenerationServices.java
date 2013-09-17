package net.sourceforge.fenixedu.domain.cardGeneration;

import pt.ist.fenixframework.Atomic;

public class CardGenerationServices {

    @Atomic
    public static void clearConstructionFlag(final CardGenerationBatch cardGenerationBatch) {
        cardGenerationBatch.setPeopleForEntryCreation(null);
    }

}
