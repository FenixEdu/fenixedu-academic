package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideEntry extends GuideEntry_Base {

	protected GraduationType graduationType;

	protected DocumentType documentType;

	public GuideEntry() {
	}

	public GuideEntry(GraduationType graduationType, DocumentType documentType,
			String description, Integer quantity, Double price, IGuide guide) {
		setDescription(description);
		setGuide(guide);
		this.documentType = documentType;
		this.graduationType = graduationType;
		setPrice(price);
		setQuantity(quantity);

	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IGuideEntry) {
			IGuideEntry guideEntry = (IGuideEntry) obj;

			if (((getGuide() == null && guideEntry.getGuide() == null) || (getGuide()
					.equals(guideEntry.getGuide())))
					&& ((getGraduationType() == null && guideEntry
							.getGraduationType() == null) || (getGraduationType()
							.equals(guideEntry.getGraduationType())))
					&& ((getDocumentType() == null && guideEntry
							.getDocumentType() == null) || (getDocumentType()
							.equals(guideEntry.getDocumentType())))
					&& ((getDescription() == null && guideEntry
							.getDescription() == null) || (getDescription()
							.equals(guideEntry.getDescription())))) {
				resultado = true;
			}
		}

		return resultado;
	}

	public String toString() {
		String result = "[GUIDE ENTRY";

		result += ", description=" + getDescription();
		result += ", guide=" + getGuide();
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