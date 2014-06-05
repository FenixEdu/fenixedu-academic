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
package net.sourceforge.fenixedu.domain.phd.access;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum PhdProcessAccessType {

    CANDIDACY_FEEDBACK_DOCUMENTS_DOWNLOAD(

    "CandidacyFeedbackDocumentsDownload",

    PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION),

    CANDIDACY_FEEDBACK_UPLOAD(

    "CandidacyFeedbackUpload",

    PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION),

    JURY_DOCUMENTS_DOWNLOAD(

    "JuryDocumentsDownload",

    PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK),

    JURY_REPORTER_FEEDBACK_UPLOAD(

    "JuryReporterFeedbackUpload",

    PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK),

    JURY_REVIEW_DOCUMENTS_DOWNLOAD(

    "JuryReviewDocumentsDownload",

    PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING);

    private String descriptor;

    /**
     * Indicates accepted process states. Operation will be available if no
     * access type is specified or if process contais any of given process state
     * 
     * 
     * <pre>
     * Example:
     * <code>
     * 	ACCESS_TYPE("name", PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)
     * </code>
     *  If process is not pending for coordination opinion, then ACCESS_TYPE will not be available.
     * </pre>
     */
    private PhdProcessStateType[] acceptedTypes;

    private PhdProcessAccessType(String type, PhdProcessStateType... acceptedTypes) {
        this.descriptor = type;
        this.acceptedTypes = acceptedTypes;
    }

    public String getName() {
        return name();
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return BundleUtil.getString(Bundle.PHD, locale, getQualifiedName());
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public boolean hasAcceptedTypes() {
        return acceptedTypes != null && acceptedTypes.length > 0;
    }

    public List<PhdProcessStateType> getAcceptedTypes() {
        return Arrays.asList(acceptedTypes);
    }

}
