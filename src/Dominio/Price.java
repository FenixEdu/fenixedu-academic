package Dominio;

import Util.DocumentType;
import Util.GraduationType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Price extends DomainObject implements IPrice {
    protected Integer internalCode;

    protected GraduationType graduationType;

    protected DocumentType documentType;

    protected String description;

    protected Double price;

    public Price() {
    }

    public Price(GraduationType graduationType, DocumentType documentType, String description,
            Double price) {
        this.description = description;
        this.documentType = documentType;
        this.graduationType = graduationType;
        this.price = price;

    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IPrice) {
            IPrice guideEntry = (IPrice) obj;

            resultado =

            getGraduationType().equals(guideEntry.getGraduationType())
                    && getDocumentType().equals(guideEntry.getDocumentType())
                    && getDescription().equals(guideEntry.getDescription());
        }

        return resultado;
    }

    public String toString() {
        String result = "[GUIDE ENTRY";
        result += ", codInt=" + internalCode;
        result += ", description=" + description;
        result += ", documentType=" + documentType;
        result += ", graduationType=" + graduationType;
        result += ", price=" + price;

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
    public Integer getInternalCode() {
        return internalCode;
    }

    /**
     * @return
     */
    public Double getPrice() {
        return price;
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
     * @param integer
     */
    public void setInternalCode(Integer integer) {
        internalCode = integer;
    }

    /**
     * @param double1
     */
    public void setPrice(Double double1) {
        price = double1;
    }

}