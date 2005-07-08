package net.sourceforge.fenixedu.domain;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Price extends Price_Base {

    public Price() {
    }

    public Price(GraduationType graduationType, DocumentType documentType, String description,
            Double price) {
        this.setDescription(description);
        this.setDocumentType(documentType);
        this.setGraduationType(graduationType);
        this.setPrice(price);

    }

    public String toString() {
        String result = "[GUIDE ENTRY";
        result += ", codInt=" + getInternalCode();
        result += ", description=" + getDescription();
        result += ", documentType=" + getDocumentType();
        result += ", graduationType=" + getGraduationType();
        result += ", price=" + getPrice();

        result += "]";
        return result;
    }

}
