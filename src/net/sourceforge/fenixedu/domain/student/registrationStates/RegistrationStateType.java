/**
 * 
 */
package net.sourceforge.fenixedu.domain.student.registrationStates;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum RegistrationStateType {

    REGISTERED(true), MOBILITY(true), CANCELED(false), CONCLUDED(false), FLUNKED(false), INTERRUPTED(
	    false), SCHOOLPARTCONCLUDED(true), INTERNAL_ABANDON(false), EXTERNAL_ABANDON(false);

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

}
