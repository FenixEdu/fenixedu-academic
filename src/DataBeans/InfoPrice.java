package DataBeans;

import Util.DocumentType;
import Util.GraduationType;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class InfoPrice extends InfoObject {

	protected GraduationType graduationType;
	protected Double price;
	protected String description;
	protected DocumentType documentType;

	public InfoPrice() {
	}


	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoPrice) {
			InfoPrice infoPrice = (InfoPrice) obj;
			resultado = this.getGraduationType().equals(infoPrice.getGraduationType()) && 
						this.getPrice().equals(infoPrice.getPrice()) &&
						this.getDescription().equals(infoPrice.getDescription()) &&
						this.getDocumentType().equals(infoPrice.getDocumentType());
						
		}
		return resultado;
	}

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

}