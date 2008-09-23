package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;

import org.joda.time.DateTime;

public class MarkLibraryCardListLettersAsEmited extends FenixService {

    public void run(List<LibraryCardDTO> libraryCardDTOList) {
	for (LibraryCardDTO libraryCardDTO : libraryCardDTOList) {
	    libraryCardDTO.getLibraryCard().setLetterGenerationDate(new DateTime());
	}
    }
}
