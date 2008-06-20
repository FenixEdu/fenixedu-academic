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
    public static class RegistrationIngressionEditor extends IngressionInformationBean implements FactoryExecutor, Serializable {

	private DomainReference<Registration> registration;

	public RegistrationIngressionEditor(Registration registration) {
	    super();
	    setRegistration(registration);

	    if (hasRegistration()) {
		setRegistrationAgreement(getRegistration().getRegistrationAgreement());
		setAgreementInformation(getRegistration().getAgreementInformation());
		setIngression(getRegistration().getIngression());
		setEntryPhase(getRegistration().getEntryPhase());
	    }
	}

	public Object execute() {
	    getRegistration().setRegistrationAgreement(getRegistrationAgreement());
	    getRegistration().setAgreementInformation(getAgreementInformation());
	    getRegistration().setIngression(getIngression());
	    getRegistration().setEntryPhase(getEntryPhase());
	    return getRegistration();
	}

	public Registration getRegistration() {
	    return (this.registration != null) ? this.registration.getObject() : null;
	}

	public boolean hasRegistration() {
	    return getRegistration() != null;
	}

	public void setRegistration(Registration registration) {
	    this.registration = (registration != null) ? new DomainReference<Registration>(registration) : null;
	}
    }
}
