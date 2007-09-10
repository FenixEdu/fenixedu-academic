package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.library.LibraryCard;

public class MarkLibraryCardListLettersAsEmited extends Service {

    public void run(List<LibraryCard> libraryCardList) {
        for (LibraryCard libraryCard : libraryCardList) {
            libraryCard.setIsLetterGenerated(Boolean.TRUE);   
        }        
    }
}
