package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;

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

}
