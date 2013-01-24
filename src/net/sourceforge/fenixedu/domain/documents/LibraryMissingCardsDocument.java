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

import pt.ist.fenixWebFramework.services.Service;

public class LibraryMissingCardsDocument extends LibraryMissingCardsDocument_Base {

    public LibraryMissingCardsDocument(List<LibraryCard> source, Person operator, String filename, byte[] content) {
	super();
	for (LibraryCard card : source)
	    addSource(card);
	setLibraryDocument(new LibraryDocument());
	init(GeneratedDocumentType.LIBRARY_MISSING_CARDS, operator, operator, filename, content);
    }

    @Override
    protected Group computePermittedGroup() {
	return new RoleGroup(RoleType.LIBRARY);
    }

    @Service
    public static void store(List<LibraryCard> source, Person operator, byte[] content) {
	if (PropertiesManager.getBooleanProperty(CONFIG_DSPACE_DOCUMENT_STORE)) {
	    DateTime time = new DateTime();
	    new LibraryMissingCardsDocument(source, operator, "missing_cards_" + time.toString("yMd_kms") + ".pdf", content);
	}

    }

}
