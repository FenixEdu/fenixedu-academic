package net.sourceforge.fenixedu.domain.documents;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Pedro Santos (pmrsa)
 */
public class GeneratedDocumentWithoutSource extends GeneratedDocumentWithoutSource_Base {
    public GeneratedDocumentWithoutSource(GeneratedDocumentType type, Party addressee, Person operator, String filename,
	    byte[] content) {
	super();
	init(type, addressee, operator, filename, content);
    }

    @Override
    protected Group computePermittedGroup() {
	return new RoleGroup(RoleType.MANAGER);
    }

    @Service
    public static void createDocument(GeneratedDocumentType type, Party addressee, Person operator, String filename,
	    byte[] content) {
	new GeneratedDocumentWithoutSource(type, addressee, operator, filename, content);
    }
}
