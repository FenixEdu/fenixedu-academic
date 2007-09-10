package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;

public class MarkLibraryCardListAsEmited extends Service {

    public void run(List<LibraryCardDTO> libraryCardList) {
        for (LibraryCardDTO libraryCardDTO : libraryCardList) {
            libraryCardDTO.getLibraryCard().setIsCardEmited(Boolean.TRUE);   
        }        
    }
}
