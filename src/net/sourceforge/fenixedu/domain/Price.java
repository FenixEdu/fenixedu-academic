package net.sourceforge.fenixedu.domain;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Price extends Price_Base {

    public Price() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Price(GraduationType graduationType, DocumentType documentType, String description,
            Double price) {
    	this();
        this.setDescription(description);
        this.setDocumentType(documentType);
        this.setGraduationType(graduationType);
        this.setPrice(price);
    }

}
