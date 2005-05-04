package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideEntry extends GuideEntry_Base {

    protected GraduationType graduationType;

    protected DocumentType documentType;

    protected List reimbursementGuideEntries;

    protected IGuide guide;

    public GuideEntry() {
    }

    public GuideEntry(GraduationType graduationType, DocumentType documentType, String description,
            Integer quantity, Double price, IGuide guide) {
        setDescription(description);
        this.guide = guide;
        this.documentType = documentType;
        this.graduationType = graduationType;
        setPrice(price);
        setQuantity(quantity);

    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IGuideEntry) {
            IGuideEntry guideEntry = (IGuideEntry) obj;

            if (((getGuide() == null && guideEntry.getGuide() == null) || (getGuide().equals(guideEntry
                    .getGuide())))
                    && ((getGraduationType() == null && guideEntry.getGraduationType() == null) || (getGraduationType()
                            .equals(guideEntry.getGraduationType())))
                    && ((getDocumentType() == null && guideEntry.getDocumentType() == null) || (getDocumentType()
                            .equals(guideEntry.getDocumentType())))
                    && ((getDescription() == null && guideEntry.getDescription() == null) || (getDescription()
                            .equals(guideEntry.getDescription())))) {
                resultado = true;
            }
        }

        return resultado;
    }

    public String toString() {
        String result = "[GUIDE ENTRY";

        result += ", description=" + getDescription();
        result += ", guide=" + guide;
        result += ", documentType=" + documentType;
        result += ", graduationType=" + graduationType;
        result += ", price=" + getPrice();
        result += ", quantity=" + getQuantity();
        result += "]";
        return result;
    }


    /**
     * @return
     */
    public DocumentType getDocumentType() {
        return documentType;
    }

    /**
     * @return
     */
    public GraduationType getGraduationType() {
        return graduationType;
    }

    /**
     * @return
     */
    public IGuide getGuide() {
        return guide;
    }

    /**
     * @param type
     */
    public void setDocumentType(DocumentType type) {
        documentType = type;
    }

    /**
     * @param type
     */
    public void setGraduationType(GraduationType type) {
        graduationType = type;
    }

    /**
     * @param guide
     */
    public void setGuide(IGuide guide) {
        this.guide = guide;
    }

    /**
     * @return Returns the reimbursementGuideEntries.
     */
    public List getReimbursementGuideEntries() {
        return reimbursementGuideEntries;
    }

    /**
     * @param reimbursementGuideEntries
     *            The reimbursementGuideEntries to set.
     */
    public void setReimbursementGuideEntries(List reimbursementGuideEntries) {
        this.reimbursementGuideEntries = reimbursementGuideEntries;
    }

}