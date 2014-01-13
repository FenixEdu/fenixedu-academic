package net.sourceforge.fenixedu.domain.person;

import org.fenixedu.bennu.core.domain.Bennu;

public class IdDocumentTypeObject extends IdDocumentTypeObject_Base {

    public IdDocumentTypeObject() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public static IdDocumentTypeObject readByIDDocumentType(final IDDocumentType documentType) {
        for (final IdDocumentTypeObject idDocumentType : Bennu.getInstance().getIdDocumentTypesSet()) {
            if (idDocumentType.getValue() == documentType) {
                return idDocumentType;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.person.IdDocument> getIdDocuments() {
        return getIdDocumentsSet();
    }

    @Deprecated
    public boolean hasAnyIdDocuments() {
        return !getIdDocumentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
