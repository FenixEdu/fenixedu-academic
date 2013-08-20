package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.Price;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class InfoPrice extends InfoObject {

    protected GraduationType graduationType;

    protected Double price;

    protected String description;

    protected DocumentType documentType;

    public InfoPrice() {
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoPrice) {
            InfoPrice infoPrice = (InfoPrice) obj;
            resultado =
                    this.getGraduationType().equals(infoPrice.getGraduationType())
                            && this.getPrice().equals(infoPrice.getPrice())
                            && this.getDescription().equals(infoPrice.getDescription())
                            && this.getDocumentType().equals(infoPrice.getDocumentType());

        }
        return resultado;
    }

    @Override
    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "graduationType = " + this.graduationType + "; ";
        result += "price = " + this.price + "]";
        result += "description = " + this.description + "]";
        result += "documentType = " + this.documentType + "]";
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
     * @param double1
     */
    public void setPrice(Double double1) {
        price = double1;
    }

    public static InfoPrice newInfoFromDoaim(Price price2) {
        InfoPrice infoPrice = new InfoPrice();
        infoPrice.setDescription(price2.getDescription());
        infoPrice.setDocumentType(price2.getDocumentType());
        infoPrice.setGraduationType(price2.getGraduationType());
        infoPrice.setExternalId(price2.getExternalId());
        infoPrice.setPrice(price2.getPrice());
        return infoPrice;
    }

}