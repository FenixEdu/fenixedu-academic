/**
 * 
 */
package net.sourceforge.fenixedu.domain.student;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum StudentStatuteType {

    WORKING_STUDENT(true), PROFESSIONAL_ATHLETE(true), HANDICAPPED(false), SAS_GRANT_OWNER(true), ASSOCIATIVE_LEADER(true);

    private boolean explicitCreation;

    private StudentStatuteType(boolean explicitCreation) {
	this.explicitCreation = explicitCreation;
    }

    public boolean isExplicitCreation() {
	return explicitCreation;
    }

}
