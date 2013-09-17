package net.sourceforge.fenixedu.domain.person;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class IdDocument extends IdDocument_Base {

    public IdDocument(final Person person, final String value, final IdDocumentTypeObject idDocumentType) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPerson(person);
        setIdDocumentType(idDocumentType);
        setValue(value);
    }

    public IdDocument(final Person person, final String value, final IDDocumentType documentType) {
        this(person, value, IdDocumentTypeObject.readByIDDocumentType(documentType));
    }

    public static Collection<IdDocument> find(final String idDocumentValue) {
        final Collection<IdDocument> idDocuments = new ArrayList<IdDocument>();
        for (final IdDocument idDocument : RootDomainObject.getInstance().getIdDocumentsSet()) {
            if (idDocument.getValue().equalsIgnoreCase(idDocumentValue)) {
                idDocuments.add(idDocument);
            }
        }

        return idDocuments;
    }

    public void setIdDocumentType(IDDocumentType documentType) {
        super.setIdDocumentType(IdDocumentTypeObject.readByIDDocumentType(documentType));
    }

    public void delete() {
        setPerson(null);
        super.setIdDocumentType(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasIdDocumentType() {
        return getIdDocumentType() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
