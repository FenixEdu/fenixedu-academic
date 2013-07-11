package net.sourceforge.fenixedu.applicationTier.Servico.library;


import net.sourceforge.fenixedu.domain.library.LibraryCard;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class MarkLibraryCardLetterAsEmited {

    @Checked("RolePredicates.LIBRARY_PREDICATE")
    @Atomic
    public static void run(LibraryCard libraryCard) {
        libraryCard.setLetterGenerationDate(new DateTime());
    }
}