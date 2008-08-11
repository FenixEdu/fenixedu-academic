package net.sourceforge.fenixedu.domain.thesis;

/**
 * Enumeration to show the library state of a thesis 
 * 
 * @author Pedro Santos (pmrsa)
 */
public enum ThesisLibraryState {
    NOT_DEALT, // services haven't look at it yet
    PENDING_ARCHIVE, // pending for some reason
    ARCHIVE_CANCELED, // validation canceled, no reason, no reference
    ARCHIVE; // validated and with library reference set.
    
    public String getName() {
	return name();
    }
}
