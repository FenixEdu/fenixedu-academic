package net.sourceforge.fenixedu.applicationTier.Servico.library;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.library.LibraryCard;

import org.joda.time.DateTime;

public class MarkLibraryCardAsEmited extends Service {

    public void run(LibraryCard libraryCard) {
	libraryCard.setCardEmitionDate(new DateTime());
    }
}
