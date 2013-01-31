package net.sourceforge.fenixedu.applicationTier.Servico.cardGeneration;

import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteCardGenerationEntryService {

	@Service
	public static void run(final CardGenerationEntry cardGenerationEntry) {
		cardGenerationEntry.delete();
	}

}
