package Dominio;

import Util.EquivalenceType;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class Equivalence implements IEquivalence {

	private IEnrolment enrolment;
	private IEnrolment equivalentEnrolment;
	private EquivalenceType equivalenceType;

	private Integer internalID;
	private Integer enrolmentKey;
	private Integer equivalentEnrolmentKey;

	public Equivalence() {
		setEnrolment(null);
		setEquivalentEnrolment(null);
		setEquivalenceType(null);

		setInternalID(null);
		setEnrolmentKey(null);
		setEquivalentEnrolmentKey(null);
	}

	public Equivalence(IEnrolment enrolment, IEnrolment equivalentEnrolment, EquivalenceType equivalenceType) {
		setEnrolment(enrolment);
		setEquivalentEnrolment(equivalentEnrolment);
		setEquivalenceType(equivalenceType);

		setInternalID(null);
		setEnrolmentKey(null);
		setEquivalentEnrolmentKey(null);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof IEquivalence) {
			IEquivalence equivalence = (IEquivalence) obj;

			resultado = (this.getEnrolment().equals(equivalence.getEnrolment())) &&
									(this.getEquivalentEnrolment().equals(equivalence.getEquivalentEnrolment()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "enrolment = " + this.enrolment + "; ";
		result += "equivalentEnrolment = " + this.equivalentEnrolment + "; ";
		result += "equivalenceType" + this.equivalenceType + "]\n";
		return result;
	}

	/**
	 * @return IEnrolment
	 */
	public IEnrolment getEnrolment() {
		return enrolment;
	}

	/**
	 * @return Integer
	 */
	public Integer getEnrolmentKey() {
		return enrolmentKey;
	}

	/**
	 * @return EquivalenceType
	 */
	public EquivalenceType getEquivalenceType() {
		return equivalenceType;
	}

	/**
	 * @return IEnrolment
	 */
	public IEnrolment getEquivalentEnrolment() {
		return equivalentEnrolment;
	}

	/**
	 * @return Integer
	 */
	public Integer getEquivalentEnrolmentKey() {
		return equivalentEnrolmentKey;
	}

	/**
	 * @return Integer
	 */
	public Integer getInternalID() {
		return internalID;
	}

	/**
	 * Sets the enrolment.
	 * @param enrolment The enrolment to set
	 */
	public void setEnrolment(IEnrolment enrolment) {
		this.enrolment = enrolment;
	}

	/**
	 * Sets the enrolmentKey.
	 * @param enrolmentKey The enrolmentKey to set
	 */
	public void setEnrolmentKey(Integer enrolmentKey) {
		this.enrolmentKey = enrolmentKey;
	}

	/**
	 * Sets the equivalenceType.
	 * @param equivalenceType The equivalenceType to set
	 */
	public void setEquivalenceType(EquivalenceType equivalenceType) {
		this.equivalenceType = equivalenceType;
	}

	/**
	 * Sets the equivalentEnrolment.
	 * @param equivalentEnrolment The equivalentEnrolment to set
	 */
	public void setEquivalentEnrolment(IEnrolment equivalentEnrolment) {
		this.equivalentEnrolment = equivalentEnrolment;
	}

	/**
	 * Sets the equivalentEnrolmentKey.
	 * @param equivalentEnrolmentKey The equivalentEnrolmentKey to set
	 */
	public void setEquivalentEnrolmentKey(Integer equivalentEnrolmentKey) {
		this.equivalentEnrolmentKey = equivalentEnrolmentKey;
	}

	/**
	 * Sets the internalID.
	 * @param internalID The internalID to set
	 */
	public void setInternalID(Integer internalID) {
		this.internalID = internalID;
	}

}
