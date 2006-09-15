/*
 * Created on Aug 3, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ResidenceCandidacies extends ResidenceCandidacies_Base {

    public ResidenceCandidacies() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDateDateTime(new DateTime());
    }
    
    public ResidenceCandidacies(String observations) {
	this();
	setObservations(observations);
    }

}
