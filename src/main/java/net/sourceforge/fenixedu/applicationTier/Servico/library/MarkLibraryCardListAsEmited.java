package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;

import org.joda.time.DateTime;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class MarkLibraryCardListAsEmited {

    @Atomic
    public static void run(List<LibraryCardDTO> libraryCardList) {
        check(RolePredicates.LIBRARY_PREDICATE);
        for (LibraryCardDTO libraryCardDTO : libraryCardList) {
            libraryCardDTO.getLibraryCard().setCardEmitionDate(new DateTime());
        }
    }
}