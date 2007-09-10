package net.sourceforge.fenixedu.applicationTier.Servico.library;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.library.LibraryCard;

public class MarkLibraryCardLetterAsEmited extends Service {

    public void run(LibraryCard libraryCard) {
        libraryCard.setIsLetterGenerated(Boolean.TRUE);
    }
}
