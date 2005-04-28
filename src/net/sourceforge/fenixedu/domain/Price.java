package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.DocumentType;
import net.sourceforge.fenixedu.util.GraduationType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Price extends Price_Base {

    protected GraduationType graduationType;

    protected DocumentType documentType;

    public Price() {
    }

    public Price(GraduationType graduationType, DocumentType documentType, String description,
            Double price) {
        setDescription(description);
        this.documentType = documentType;
        this.graduationType = graduationType;
        setPrice(price);

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
        result += ", codInt=" + getInternalCode();
        result += ", description=" + getDescription();
        result += ", documentType=" + documentType;
        result += ", graduationType=" + graduationType;
        result += ", price=" + getPrice();

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

}