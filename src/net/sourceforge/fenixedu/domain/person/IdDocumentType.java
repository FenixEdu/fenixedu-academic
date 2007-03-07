package net.sourceforge.fenixedu.domain.person;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class IdDocumentType extends IdDocumentType_Base {
    
    public IdDocumentType() {
        super();
    }

    public static IdDocumentType readByIDDocumentType(final IDDocumentType documentType) {
	for (final IdDocumentType idDocumentType : RootDomainObject.getInstance().getIdDocumentTypesSet()) {
	    if (idDocumentType.getValue() == documentType) {
		return idDocumentType;
	    }
	}
	return null;
    }
    
}
