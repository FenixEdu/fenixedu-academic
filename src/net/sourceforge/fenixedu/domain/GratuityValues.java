/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;


/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 */
public class GratuityValues extends GratuityValues_Base {

	public String toString() {
		StringBuffer result = new StringBuffer();
		result = result.append("[GratuityValues: \n").append("idInternal= ")
				.append(getIdInternal()).append("\nanualValue= ").append(
						getAnualValue()).append("\nscholarShipPart= ").append(
						getScholarShipValue()).append("\nfinalProofValue= ")
				.append(getFinalProofValue()).append("\ncourseValue= ").append(
						getCourseValue()).append("\ncreditValue= ").append(
						getCreditValue()).append("\nproofRequestPayment= ")
				.append(getProofRequestPayment()).append("\nstartPayment= ")
				.append(getStartPayment()).append("\nendPayment= ").append(
						getEndPayment()).append("]");
		return result.toString();
	}

}