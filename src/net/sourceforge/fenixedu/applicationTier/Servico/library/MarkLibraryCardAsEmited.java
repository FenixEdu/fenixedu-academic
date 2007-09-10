package net.sourceforge.fenixedu.applicationTier.Servico.library;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.library.LibraryCard;

public class MarkLibraryCardAsEmited extends Service {

    public void run(LibraryCard libraryCard) {
        libraryCard.setIsCardEmited(Boolean.TRUE);
    }
}
