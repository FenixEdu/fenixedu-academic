package net.sourceforge.fenixedu.applicationTier.Servico.library;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class EditLibraryCard {

    @Atomic
    public static LibraryCard run(LibraryCardDTO libraryCardDTO) {
        check(RolePredicates.LIBRARY_PREDICATE);
        libraryCardDTO.getLibraryCard().edit(libraryCardDTO);
        return libraryCardDTO.getLibraryCard();
    }
}