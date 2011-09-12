package net.sourceforge.fenixedu.domain;

public class IdentificationDocumentSeriesNumber extends IdentificationDocumentSeriesNumber_Base {
    
    public IdentificationDocumentSeriesNumber() {
        super();
    }

    public IdentificationDocumentSeriesNumber(final Person person, final String identificationDocumentSeriesNumber) {
	setPerson(person);
	setValue(identificationDocumentSeriesNumber);
    }
    
}
