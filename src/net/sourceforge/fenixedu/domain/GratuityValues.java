/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 */
public class GratuityValues extends GratuityValues_Base {

	private Date startPayment;

	private Date endPayment;

	private Date when;

	/**
	 * @return Returns the when.
	 */
	public Date getWhen() {
		return when;
	}

	/**
	 * @param when
	 *            The when to set.
	 */
	public void setWhen(Date when) {
		this.when = when;
	}

	/**
	 * @return Returns the endPayment.
	 */
	public Date getEndPayment() {
		return endPayment;
	}

	/**
	 * @param endPayment
	 *            The endPayment to set.
	 */
	public void setEndPayment(Date endPayment) {
		this.endPayment = endPayment;
	}

	/**
	 * @return Returns the startPayment.
	 */
	public Date getStartPayment() {
		return startPayment;
	}

	/**
	 * @param startPayment
	 *            The startPayment to set.
	 */
	public void setStartPayment(Date startPayment) {
		this.startPayment = startPayment;
	}

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

	public boolean equals(Object object) {
		// TODO: to make
		return true;
	}
}