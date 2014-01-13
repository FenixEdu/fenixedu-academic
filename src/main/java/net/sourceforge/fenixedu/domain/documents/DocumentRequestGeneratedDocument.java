package net.sourceforge.fenixedu.domain.documents;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.ist.fenixframework.Atomic;

/**
 * @author Pedro Santos (pmrsa)
 */
public class DocumentRequestGeneratedDocument extends DocumentRequestGeneratedDocument_Base {
    protected DocumentRequestGeneratedDocument(IDocumentRequest source, Party addressee, Person operator, String filename,
            byte[] content) {
        super();
        setSource((AcademicServiceRequest) source);
        init(GeneratedDocumentType.determineType(source.getDocumentRequestType()), addressee, operator, filename, content);
    }

    @Override
    public boolean isPersonAllowedToAccess(Person person) {
        if (person != null && person.hasRole(RoleType.RECTORATE) && getSource().hasRegistryCode()) {
            return true;
        }
        return super.isPersonAllowedToAccess(person);
    }

    @Override
    protected Group computePermittedGroup() {
        return new AcademicAuthorizationGroup(AcademicOperationType.SERVICE_REQUESTS);
    }

    @Override
    public void delete() {
        setSource(null);
        super.delete();
    }

    @Atomic
    public static void store(IDocumentRequest source, String filename, byte[] content) {
        if (!CoreConfiguration.getConfiguration().developmentMode()) {
            new DocumentRequestGeneratedDocument(source, source.getPerson(), AccessControl.getPerson(), filename, content);
        }
    }

    @Deprecated
    public boolean hasSource() {
        return getSource() != null;
    }

}
