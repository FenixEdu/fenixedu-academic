/*
 * Created on 21/Mar/2003
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.util.DocumentType;
import net.sourceforge.fenixedu.util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoGuideEntry extends InfoObject {

    protected GraduationType graduationType;

    protected DocumentType documentType;

    protected String description;

    protected Double price;

    protected Integer quantity;

    protected InfoGuide infoGuide;

    public InfoGuideEntry() {
    }

    public InfoGuideEntry(GraduationType graduationType, DocumentType documentType, String description,
            Double price, Integer quantity, InfoGuide infoGuide) {
        this.description = description;
        this.documentType = documentType;
        this.graduationType = graduationType;
        this.infoGuide = infoGuide;
        this.price = price;
        this.quantity = quantity;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoGuideEntry) {
            InfoGuideEntry infoGuideEntry = (InfoGuideEntry) obj;

            resultado = getInfoGuide().equals(infoGuideEntry.getInfoGuide())
                    && getGraduationType().equals(infoGuideEntry.getGraduationType())
                    && getDocumentType().equals(infoGuideEntry.getDocumentType())
                    && getDescription().equals(infoGuideEntry.getDescription());
        }
        return resultado;
    }

    public String toString() {
        String result = "[GUIDE ENTRY";
        result += ", description=" + description;
        result += ", infoGuide=" + infoGuide;
        result += ", documentType=" + documentType;
        result += ", graduationType=" + graduationType;
        result += ", price=" + price;
        result += ", quantity=" + quantity;
        result += "]";
        return result;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
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
    public InfoGuide getInfoGuide() {
        return infoGuide;
    }

    /**
     * @return
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @return
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
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
    public void setInfoGuide(InfoGuide guide) {
        infoGuide = guide;
    }

    /**
     * @param double1
     */
    public void setPrice(Double double1) {
        price = double1;
    }

    /**
     * @param integer
     */
    public void setQuantity(Integer integer) {
        quantity = integer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IGuideEntry guideEntry) {
        super.copyFromDomain(guideEntry);
        if (guideEntry != null) {
            setDescription(guideEntry.getDescription());
            setDocumentType(guideEntry.getDocumentType());
            setGraduationType(guideEntry.getGraduationType());
            setPrice(guideEntry.getPrice());
            setQuantity(guideEntry.getQuantity());
        }
    }

    public static InfoGuideEntry newInfoFromDomain(IGuideEntry guideEntry) {
        InfoGuideEntry infoGuideEntry = null;
        if (guideEntry != null) {
            infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.copyFromDomain(guideEntry);
        }

        return infoGuideEntry;
    }
}