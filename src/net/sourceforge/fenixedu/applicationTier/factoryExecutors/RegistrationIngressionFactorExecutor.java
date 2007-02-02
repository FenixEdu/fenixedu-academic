/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationIngressionFactorExecutor {

    @SuppressWarnings("serial")
    public static class RegistrationIngressionEditor extends IngressionInformationBean implements
	    FactoryExecutor, Serializable {

	private DomainReference<Registration> registration;

	public RegistrationIngressionEditor(Registration registration) {
	    super();
	    this.registration = new DomainReference<Registration>(registration);
	}

	public Object execute() {
	    getRegistration().setIngression(getIngression().name());
	    getRegistration().setEntryPhase(getEntryPhase());
	    return getRegistration();
	}

	public Registration getRegistration() {
	    return this.registration.getObject();
	}
    }
}
