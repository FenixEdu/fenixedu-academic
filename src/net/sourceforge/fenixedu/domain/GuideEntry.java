package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;



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

    public void delete() {
        if (canBeDeleted()) {
            if (hasPaymentTransaction()) {
                getPaymentTransaction().delete();
            }
            
            Guide guide = getGuide();
            removeGuide();
            guide.updateTotalValue();
            
            removeRootDomainObject();

            deleteDomainObject();
        } else {
            throw new DomainException("guide.entry.cannot.be.deleted");
        }
    }

    public boolean canBeDeleted() {
        return !hasAnyReimbursementGuideEntries();
    }
    
}
