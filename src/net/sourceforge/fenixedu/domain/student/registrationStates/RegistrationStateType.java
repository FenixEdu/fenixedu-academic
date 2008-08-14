/**
 * 
 */
package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum RegistrationStateType {

    REGISTERED(true, false), MOBILITY(true, false), CANCELED(false, true), CONCLUDED(false, false), FLUNKED(false, true), INTERRUPTED(
	    false, true), SCHOOLPARTCONCLUDED(true, false), INTERNAL_ABANDON(false, true), EXTERNAL_ABANDON(false, true), TRANSITION(
	    false, false), TRANSITED(false, false);

    private RegistrationStateType(boolean active, boolean deleteActualPeriodInfo) {
	this.active = active;
	this.deleteActualPeriodInfo = deleteActualPeriodInfo;
    }

    private boolean active;
    private boolean deleteActualPeriodInfo;

    public String getName() {
	return name();
    }

    public boolean isActive() {
	return active;
    }

    public boolean isInactive() {
	return !active;
    }

    public boolean deleteActualPeriodInfo() {
	return deleteActualPeriodInfo;
    }

    public String getQualifiedName() {
	return RegistrationStateType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return RegistrationStateType.class.getName() + "." + name();
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(getQualifiedName());
    }

    public boolean canReingress() {
	return this == FLUNKED || this == INTERRUPTED || this == INTERNAL_ABANDON || this == EXTERNAL_ABANDON || this == CANCELED;
    }

}
