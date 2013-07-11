package net.sourceforge.fenixedu.domain.documents;

import java.util.List;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.library.LibraryDocument;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class LibraryMissingLettersDocument extends LibraryMissingLettersDocument_Base {

    public LibraryMissingLettersDocument() {
        super();
    }

    public LibraryMissingLettersDocument(List<LibraryCard> source, Person operator, String filename, byte[] content,
            boolean forStudents) {
        super();
        for (LibraryCard card : source) {
            addSource(card);
        }
        setLibraryDocument(new LibraryDocument());
        init(forStudents ? GeneratedDocumentType.LIBRARY_MISSING_LETTERS_STUDENTS : GeneratedDocumentType.LIBRARY_MISSING_LETTERS,
                operator, operator, filename, content);

    }

    @Override
    protected Group computePermittedGroup() {
        return new RoleGroup(RoleType.LIBRARY);
    }

    @Atomic
    public static void store(List<LibraryCard> source, Person operator, byte[] content, boolean forStudents) {
        if (PropertiesManager.getBooleanProperty(CONFIG_DSPACE_DOCUMENT_STORE)) {
            DateTime time = new DateTime();
            new LibraryMissingLettersDocument(source, operator, "missing_letters_" + time.toString("yMd_kms") + ".pdf", content,
                    forStudents);
        }
    }

}
