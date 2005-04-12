package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public interface IEnrolmentEquivalence extends IDomainObject {

    public IEnrolment getEnrolment();

    public List getEquivalenceRestrictions();

    public void setEnrolment(IEnrolment enrolment);

    public void setEquivalenceRestrictions(List list);

}