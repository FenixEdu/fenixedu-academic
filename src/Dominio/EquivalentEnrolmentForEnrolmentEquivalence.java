/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

/**
 * @author jpvl
 */
public class EquivalentEnrolmentForEnrolmentEquivalence extends DomainObject implements IEquivalentEnrolmentForEnrolmentEquivalence {
	private IEnrolmentEquivalence enrolmentEquivalence;
	private IEnrolment equivalentEnrolment;

	private Integer enrolmentEquivalenceKey;
	private Integer equivalentEnrolmentKey;

	/**
	 * 
	 */
	public EquivalentEnrolmentForEnrolmentEquivalence() {
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof IEquivalentEnrolmentForEnrolmentEquivalence) {
			IEquivalentEnrolmentForEnrolmentEquivalence equivalence = (IEquivalentEnrolmentForEnrolmentEquivalence) obj;

			resultado = (this.getEnrolmentEquivalence().equals(equivalence.getEnrolmentEquivalence())) && (this.getEquivalentEnrolment().equals(equivalence.getEquivalentEnrolment()));
		}
		return resultado;
	}

	public IEnrolmentEquivalence getEnrolmentEquivalence() {
		return enrolmentEquivalence;
	}

	public Integer getEnrolmentEquivalenceKey() {
		return enrolmentEquivalenceKey;
	}

	public IEnrolment getEquivalentEnrolment() {
		return equivalentEnrolment;
	}

	public Integer getEquivalentEnrolmentKey() {
		return equivalentEnrolmentKey;
	}

	public void setEnrolmentEquivalence(IEnrolmentEquivalence equivalence) {
		enrolmentEquivalence = equivalence;
	}

	public void setEnrolmentEquivalenceKey(Integer integer) {
		enrolmentEquivalenceKey = integer;
	}

	public void setEquivalentEnrolment(IEnrolment enrolment) {
		equivalentEnrolment = enrolment;
	}

	public void setEquivalentEnrolmentKey(Integer integer) {
		equivalentEnrolmentKey = integer;
	}

}
