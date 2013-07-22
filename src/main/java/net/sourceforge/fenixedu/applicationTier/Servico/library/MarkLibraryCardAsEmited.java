package net.sourceforge.fenixedu.applicationTier.Servico.library;


import net.sourceforge.fenixedu.domain.library.LibraryCard;

import org.joda.time.DateTime;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class MarkLibraryCardAsEmited {

    @Atomic
    public static void run(LibraryCard libraryCard) {
        check(RolePredicates.LIBRARY_PREDICATE);
        libraryCard.setCardEmitionDate(new DateTime());
    }
}