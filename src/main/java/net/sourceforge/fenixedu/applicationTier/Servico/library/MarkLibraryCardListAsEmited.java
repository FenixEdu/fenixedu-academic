package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class MarkLibraryCardListAsEmited {

    @Checked("RolePredicates.LIBRARY_PREDICATE")
    @Atomic
    public static void run(List<LibraryCardDTO> libraryCardList) {
        for (LibraryCardDTO libraryCardDTO : libraryCardList) {
            libraryCardDTO.getLibraryCard().setCardEmitionDate(new DateTime());
        }
    }
}