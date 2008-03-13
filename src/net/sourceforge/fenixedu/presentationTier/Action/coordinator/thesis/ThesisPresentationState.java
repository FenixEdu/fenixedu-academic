package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import net.sourceforge.fenixedu.domain.thesis.Thesis;

public enum ThesisPresentationState {
    UNEXISTING,
    DRAFT,
    SUBMITTED,
    REJECTED,
    APPROVED,
    DOCUMENTS_SUBMITTED,
    DOCUMENTS_CONFIRMED,
    CONFIRMED,
    EVALUATED_1ST, // Indicates that the student has a thesis evaluated but he
                   // can reevaluate the Thesis (or a new) in the second semester
    EVALUATED,
    UNKNOWN;

    public static ThesisPresentationState getThesisPresentationState(final Thesis thesis) {
        if (thesis == null) {
            return ThesisPresentationState.UNEXISTING;
        }
	if (thesis.isRejected()) {
	    return ThesisPresentationState.REJECTED;
	}
        if (thesis.isDraft()) {
            return ThesisPresentationState.DRAFT;
	}
	if (thesis.isSubmitted()) {
	    return ThesisPresentationState.SUBMITTED;
	}
	if (thesis.isWaitingConfirmation()) {
	    if (thesis.getConfirmmedDocuments() == null) {
		return areAllDocumentsSubmitted(thesis) ?
			ThesisPresentationState.DOCUMENTS_SUBMITTED : ThesisPresentationState.APPROVED;
	    } else {
		return ThesisPresentationState.DOCUMENTS_CONFIRMED;
	    }
	}
	if (thesis.isConfirmed()) {
	    return ThesisPresentationState.CONFIRMED;
	}
	if (thesis.isEvaluated()) {
	    return thesis.isFinalThesis() ? ThesisPresentationState.EVALUATED : ThesisPresentationState.EVALUATED_1ST;
	}

	return ThesisPresentationState.UNKNOWN;
    }

    private static boolean areAllDocumentsSubmitted(final Thesis thesis) {
	return thesis.isThesisAbstractInBothLanguages() && thesis.isKeywordsInBothLanguages()
			&& thesis.hasExtendedAbstract() && thesis.hasDissertation() && thesis.getDiscussed() != null;
    }

    public String getName() {
	return name();
    }

    public boolean isUnexisting() {
	return this == UNEXISTING;
    }

    public boolean isDraft() {
	return this == DRAFT;
    }

    public boolean isSubmitted() {
	return this == SUBMITTED;
    }

    public boolean isRejected() {
	return this == REJECTED;
    }

    public boolean isApproved() {
	return this == APPROVED;
    }

    public boolean isDocumentsSubmitted() {
	return this == DOCUMENTS_SUBMITTED;
    }

    public boolean isDocumentsConfirmed() {
	return this == DOCUMENTS_CONFIRMED;
    }

    public boolean isConfirmed() {
	return this == CONFIRMED;
    }

    public boolean isEvaluated1st() {
	return this == EVALUATED_1ST;
    }

    public boolean isEvaluated() {
	return this == EVALUATED;
    }

    public boolean isUnknown() {
	return this == UNKNOWN;
    }

}