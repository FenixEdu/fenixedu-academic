package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

public enum ThesisPresentationState {
    UNEXISTING, 
    DRAFT, 
    SUBMITTED, 
    REJECTED,
    APPROVED, 
    CONFIRMED, 
    EVALUATED_1ST, // Indicates that the student has a thesis evaluated but he
                   // can reevaluate the Thesis (or a new) in the second semester
    EVALUATED, 
    UNKNOWN
}