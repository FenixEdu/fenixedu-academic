package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;

import org.joda.time.DateTime;

public class MarkLibraryCardListAsEmited extends FenixService {

    public void run(List<LibraryCardDTO> libraryCardList) {
	for (LibraryCardDTO libraryCardDTO : libraryCardList) {
	    libraryCardDTO.getLibraryCard().setCardEmitionDate(new DateTime());
	}
    }
}
