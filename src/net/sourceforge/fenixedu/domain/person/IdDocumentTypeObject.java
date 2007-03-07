package net.sourceforge.fenixedu.domain.person;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class IdDocumentTypeObject extends IdDocumentTypeObject_Base {
    
    public IdDocumentTypeObject() {
        super();
    }

    public static IdDocumentTypeObject readByIDDocumentType(final IDDocumentType documentType) {
	for (final IdDocumentTypeObject idDocumentType : RootDomainObject.getInstance().getIdDocumentTypesSet()) {
	    if (idDocumentType.getValue() == documentType) {
		return idDocumentType;
	    }
	}
	return null;
    }
    
}
