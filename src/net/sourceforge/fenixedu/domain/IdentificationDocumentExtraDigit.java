package net.sourceforge.fenixedu.domain;


public class IdentificationDocumentExtraDigit extends IdentificationDocumentExtraDigit_Base {
    
    public IdentificationDocumentExtraDigit() {
        super();
    }

    public IdentificationDocumentExtraDigit(final Person person, final String identificationDocumentExtraDigit) {
	setPerson(person);
	setValue(identificationDocumentExtraDigit);
    }
    
}
