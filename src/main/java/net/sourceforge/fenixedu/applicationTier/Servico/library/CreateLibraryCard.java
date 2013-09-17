package net.sourceforge.fenixedu.applicationTier.Servico.library;


import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateLibraryCard {

    @Atomic
    public static LibraryCard run(LibraryCardDTO libraryCardDTO) {
        check(RolePredicates.LIBRARY_PREDICATE);
        return new LibraryCard(libraryCardDTO);
    }
}