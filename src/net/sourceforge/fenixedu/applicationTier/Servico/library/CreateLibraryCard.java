package net.sourceforge.fenixedu.applicationTier.Servico.library;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.domain.library.LibraryCard;

public class CreateLibraryCard extends FenixService {

    public LibraryCard run(LibraryCardDTO libraryCardDTO) {
	return new LibraryCard(libraryCardDTO);
    }
}
