package net.sourceforge.fenixedu.domain;


/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideEntry extends GuideEntry_Base {

    public GuideEntry() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public GuideEntry(GraduationType graduationType, DocumentType documentType, String description,
            Integer quantity, Double price, Guide guide) {
    	this();
        this.setDescription(description);
        this.setGuide(guide);
        this.setDocumentType(documentType);
        this.setGraduationType(graduationType);
        this.setPrice(price);
        this.setQuantity(quantity);

    }

}
