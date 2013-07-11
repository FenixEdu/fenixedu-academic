package net.sourceforge.fenixedu.applicationTier.Servico.library;


import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class EditLibraryCard {

    @Checked("RolePredicates.LIBRARY_PREDICATE")
    @Atomic
    public static LibraryCard run(LibraryCardDTO libraryCardDTO) {
        libraryCardDTO.getLibraryCard().edit(libraryCardDTO);
        return libraryCardDTO.getLibraryCard();
    }
}