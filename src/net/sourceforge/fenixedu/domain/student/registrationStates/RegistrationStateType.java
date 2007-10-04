/**
 * 
 */
package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.LanguageUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum RegistrationStateType {

    REGISTERED(true), MOBILITY(true), CANCELED(false), CONCLUDED(false), FLUNKED(false), INTERRUPTED(false), SCHOOLPARTCONCLUDED(
	    true), INTERNAL_ABANDON(false), EXTERNAL_ABANDON(false), TRANSITION(false), TRANSITED(false);

    private RegistrationStateType(boolean active) {
	this.active = active;
    }

    private boolean active;

    public String getName() {
	return name();
    }

    public boolean isActive() {
	return active;
    }

    public boolean isInactive() {
	return !active;
    }

    public String getQualifiedName() {
	return RegistrationStateType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return RegistrationStateType.class.getName() + "." + name();
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale())
		.getString(getQualifiedName());
    }

    public boolean canReingress() {
	return this == FLUNKED || this == INTERRUPTED || this == INTERNAL_ABANDON || this == EXTERNAL_ABANDON || this == CANCELED;
    }

}
