package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideEntry extends GuideEntry_Base {

    public GuideEntry() {
    }

    public GuideEntry(GraduationType graduationType, DocumentType documentType, String description,
            Integer quantity, Double price, IGuide guide) {
        this.setDescription(description);
        this.setGuide(guide);
        this.setDocumentType(documentType);
        this.setGraduationType(graduationType);
        this.setPrice(price);
        this.setQuantity(quantity);

    }

    public String toString() {
        String result = "[GUIDE ENTRY";
        result += ", description=" + getDescription();
        result += ", guide=" + getGuide();
        result += ", documentType=" + getDocumentType();
        result += ", graduationType=" + getGraduationType();
        result += ", price=" + getPrice();
        result += ", quantity=" + getQuantity();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IGuideEntry) {
            final IGuideEntry guideEntry = (IGuideEntry) obj;
            return this.getIdInternal().equals(guideEntry.getIdInternal());
        }
        return false;
    }

}
