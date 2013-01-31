package net.sourceforge.fenixedu.applicationTier.Servico.library;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.library.LibraryCard;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class MarkLibraryCardAsEmited extends FenixService {

	@Checked("RolePredicates.LIBRARY_PREDICATE")
	@Service
	public static void run(LibraryCard libraryCard) {
		libraryCard.setCardEmitionDate(new DateTime());
	}
}