package Dominio;

import java.util.List;


/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IEnrolmentEquivalence extends IDomainObject{

	public IEnrollment getEnrolment();
	public List getEquivalenceRestrictions();
	public void setEnrolment(IEnrollment enrolment);
	public void setEquivalenceRestrictions(List list);

}