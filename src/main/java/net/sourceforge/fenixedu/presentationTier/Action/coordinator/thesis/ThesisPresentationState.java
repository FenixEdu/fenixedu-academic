/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public enum ThesisPresentationState {
    UNEXISTING, DRAFT, SUBMITTED, REJECTED, APPROVED, DOCUMENTS_SUBMITTED, DOCUMENTS_CONFIRMED, CONFIRMED, EVALUATED_1ST, // Indicates
    // that
    // the
    // student
    // has
    // a
    // thesis
    // evaluated
    // but
    // he
    // can reevaluate the Thesis (or a new) in the second semester
    EVALUATED, UNKNOWN;

    public static ThesisPresentationState getThesisPresentationState(final Enrolment enrolment) {
        return getThesisPresentationState(enrolment.getThesis());
    }

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
                return areAllDocumentsSubmitted(thesis) ? ThesisPresentationState.DOCUMENTS_SUBMITTED : ThesisPresentationState.APPROVED;
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

    public static boolean areDocumentsSubmitted(Thesis thesis) {
        ThesisPresentationState state = getThesisPresentationState(thesis);

        return state == DOCUMENTS_SUBMITTED || state == DOCUMENTS_CONFIRMED || state == CONFIRMED || state == EVALUATED_1ST
                || state == EVALUATED;
    }

    private static boolean areAllDocumentsSubmitted(final Thesis thesis) {
        return thesis.isThesisAbstractInBothLanguages() && thesis.isKeywordsInBothLanguages() && thesis.hasExtendedAbstract()
                && thesis.hasDissertation() && thesis.getDiscussed() != null;
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

    public String getBundleLabel() {
        return this.getClass().getSimpleName() + "." + name() + ".label";
    }

    public String getBundleSimpleLabel() {
        return this.getClass().getSimpleName() + "." + name() + ".simple";
    }

    public String getLabel() {
        final String simpleLabel = RenderUtils.getResourceString("ENUMERATION_RESOURCES", getBundleSimpleLabel());
        final String label = RenderUtils.getResourceString("APPLICATION_RESOURCES", getBundleLabel());
        return String.format("%s - %s", simpleLabel, label);
    }
}