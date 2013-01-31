package net.sourceforge.fenixedu.domain.cardGeneration;

import pt.ist.fenixWebFramework.services.Service;

public class CardGenerationServices {

	@Service
	public static void clearConstructionFlag(final CardGenerationBatch cardGenerationBatch) {
		cardGenerationBatch.setPeopleForEntryCreation(null);
	}

}
