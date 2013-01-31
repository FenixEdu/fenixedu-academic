package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class MarkLibraryCardListLettersAsEmited extends FenixService {

	@Checked("RolePredicates.LIBRARY_PREDICATE")
	@Service
	public static void run(List<LibraryCardDTO> libraryCardDTOList) {
		for (LibraryCardDTO libraryCardDTO : libraryCardDTOList) {
			libraryCardDTO.getLibraryCard().setLetterGenerationDate(new DateTime());
		}
	}
}