package net.sourceforge.fenixedu.domain.person;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class IdDocumentTypeObject extends IdDocumentTypeObject_Base {

    public IdDocumentTypeObject() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public static IdDocumentTypeObject readByIDDocumentType(final IDDocumentType documentType) {
        for (final IdDocumentTypeObject idDocumentType : RootDomainObject.getInstance().getIdDocumentTypesSet()) {
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
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

}
