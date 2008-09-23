package net.sourceforge.fenixedu.applicationTier.Servico.library;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.library.LibraryCard;

import org.joda.time.DateTime;

public class MarkLibraryCardLetterAsEmited extends FenixService {

    public void run(LibraryCard libraryCard) {
	libraryCard.setLetterGenerationDate(new DateTime());
    }
}
