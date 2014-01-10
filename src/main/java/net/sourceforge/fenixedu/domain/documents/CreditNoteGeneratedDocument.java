package net.sourceforge.fenixedu.domain.documents;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.ist.fenixframework.Atomic;

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
        setSource(null);
        super.delete();
    }

    @Atomic
    public static void store(CreditNote source, String filename, byte[] content) {
        if (!CoreConfiguration.getConfiguration().developmentMode()) {
            new CreditNoteGeneratedDocument(source, source.getReceipt().getPerson(), AccessControl.getPerson(), filename, content);
        }
    }

    @Deprecated
    public boolean hasSource() {
        return getSource() != null;
    }

}
