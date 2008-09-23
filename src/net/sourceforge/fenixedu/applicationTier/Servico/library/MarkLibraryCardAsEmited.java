package net.sourceforge.fenixedu.applicationTier.Servico.library;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.library.LibraryCard;

import org.joda.time.DateTime;

public class MarkLibraryCardAsEmited extends FenixService {

    public void run(LibraryCard libraryCard) {
	libraryCard.setCardEmitionDate(new DateTime());
    }
}
