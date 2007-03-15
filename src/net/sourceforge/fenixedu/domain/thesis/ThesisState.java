package net.sourceforge.fenixedu.domain.thesis;

/**
 * Enumeration used to control the current state of a student thesis.
 * 
 * @see Thesis
 * 
 * @author cfgi
 */
public enum ThesisState {
    DRAFT,
    SUBMITTED,
    APPROVED,
    CONFIRMED,
    REVISION, // The discussion occured but the student can submit information
    EVALUATED
}
