package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;


public class IdentificationDocumentExtraDigit extends IdentificationDocumentExtraDigit_Base {
    
    public IdentificationDocumentExtraDigit() {
        super();
    }

    public IdentificationDocumentExtraDigit(final Person person, final String identificationDocumentExtraDigit) {
	setPerson(person);
	if (identificationDocumentExtraDigit == null
		|| identificationDocumentExtraDigit.isEmpty()
		|| identificationDocumentExtraDigit.length() != 1
		|| !StringUtils.isNumeric(identificationDocumentExtraDigit)) {
	    throw new DomainException("label.identificationDocumentExtraDigit.invalid.format");
	}
	setValue(identificationDocumentExtraDigit);
    }
    
}
