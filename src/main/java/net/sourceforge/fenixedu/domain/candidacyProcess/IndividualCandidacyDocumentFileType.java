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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.Locale;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum IndividualCandidacyDocumentFileType {
    DOCUMENT_IDENTIFICATION, PAYMENT_DOCUMENT, HABILITATION_CERTIFICATE_DOCUMENT, FIRST_CYCLE_ACCESS_HABILITATION_DOCUMENT,
    CV_DOCUMENT, REPORT_OR_WORK_DOCUMENT, HANDICAP_PROOF_DOCUMENT, VAT_CARD_DOCUMENT, PHOTO, DEGREE_CERTIFICATE,
    REGISTRATION_CERTIFICATE, FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE, NO_PRESCRIPTION_CERTIFICATE,
    FOREIGN_INSTITUTION_SCALE_CERTIFICATE, GRADES_DOCUMENT, LEARNING_AGREEMENT, TRANSCRIPT_OF_RECORDS,
    APPROVED_LEARNING_AGREEMENT, ENGLISH_LEVEL_DECLARATION;

    public String localizedName(Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, locale, qualifiedName());
    }

    protected String qualifiedName() {
        return this.getClass().getSimpleName() + "." + name();
    }

    protected String localizedName() {
        return localizedName(I18N.getLocale());
    }

    public String getLocalizedName() {
        return localizedName();
    }

}
