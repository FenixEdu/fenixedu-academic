package Dominio;

import Util.EquivalenceType;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IEquivalence {

	public IEnrolment getEnrolment();
	public EquivalenceType getEquivalenceType();
	public IEnrolment getEquivalentEnrolment();

	public void setEnrolment(IEnrolment enrolment);
	public void setEquivalenceType(EquivalenceType equivalenceType);
	public void setEquivalentEnrolment(IEnrolment equivalentEnrolment);
}