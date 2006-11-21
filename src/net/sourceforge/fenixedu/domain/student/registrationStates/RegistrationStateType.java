/**
 * 
 */
package net.sourceforge.fenixedu.domain.student.registrationStates;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum RegistrationStateType {

    REGISTERED, MOBILITY, CANCELED, CONCLUDED, FLUNKED, INTERRUPTED, SCHOOLPARTCONCLUDED, ABANDONED;

    public String getName() {
	return name();
    }

}
