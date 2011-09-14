package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class IdentificationDocumentSeriesNumber extends IdentificationDocumentSeriesNumber_Base {
    
    public IdentificationDocumentSeriesNumber() {
        super();
    }

    public IdentificationDocumentSeriesNumber(final Person person, final String identificationDocumentSeriesNumber) {
	setPerson(person);
	if (identificationDocumentSeriesNumber != null && !identificationDocumentSeriesNumber.isEmpty()) {
	    final String trimmedValue = identificationDocumentSeriesNumber.trim().replace(" ", "");
	    if (trimmedValue.length() == 4
		    && Character.isDigit(trimmedValue.charAt(0))
		    && Character.isLetter(trimmedValue.charAt(1))
		    && Character.isLetter(trimmedValue.charAt(2))
		    && Character.isDigit(trimmedValue.charAt(3))) {
		setValue(trimmedValue);
	    } else {
		throw new DomainException("label.identificationDocumentSeriesNumber.invalid.format");
	    }
	} else {
	    throw new DomainException("label.identificationDocumentSeriesNumber.invalid.format");
	}
    }
    
}
