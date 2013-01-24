package net.sourceforge.fenixedu.domain.documents;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Pedro Santos (pmrsa)
 */
public class CreditNoteGeneratedDocument extends CreditNoteGeneratedDocument_Base {
    protected CreditNoteGeneratedDocument(CreditNote source, Party addressee, Person operator, String filename, byte[] content) {
	super();
	setSource(source);
	init(GeneratedDocumentType.CREDIT_NOTE, addressee, operator, filename, content);
    }

    @Override
    protected Group computePermittedGroup() {
	return new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_STUDENT_PAYMENTS);
    }

    @Override
    public void delete() {
	removeSource();
	super.delete();
    }

    @Service
    public static void store(CreditNote source, String filename, byte[] content) {
	if (PropertiesManager.getBooleanProperty(CONFIG_DSPACE_DOCUMENT_STORE)) {
	    new CreditNoteGeneratedDocument(source, source.getReceipt().getPerson(), AccessControl.getPerson(), filename, content);
	}
    }
}
