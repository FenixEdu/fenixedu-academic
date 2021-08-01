/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum AcademicServiceRequestType {

    DOCUMENT,

    REINGRESSION(true),

    EQUIVALENCE_PLAN(true),

    REVISION_EQUIVALENCE_PLAN(true),

    COURSE_GROUP_CHANGE_REQUEST(true),

    FREE_SOLICITATION_ACADEMIC_REQUEST(true),

    SPECIAL_SEASON_REQUEST(true),

    EXTRAORDINARY_SEASON_REQUEST(true),

    EXTRA_EXAM_REQUEST(true),

    PHOTOCOPY_REQUEST(true),

    PARTIAL_REGIME_REQUEST(true),

    PHD_STUDENT_REINGRESSION(true),

    DUPLICATE_REQUEST(true),

    DIPLOMA_REQUEST(true),

    DIPLOMA_SUPPLEMENT_REQUEST(true),

    REGISTRY_DIPLOMA_REQUEST(true);

    private boolean isServiceRequest;

    AcademicServiceRequestType() {
        this.isServiceRequest = false;
    }

    AcademicServiceRequestType(final boolean isForServiceRequest) {
        this.isServiceRequest = isForServiceRequest;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return AcademicServiceRequestType.class.getName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return AcademicServiceRequestType.class.getName() + "." + name();
    }

    static public List<AcademicServiceRequestType> getServiceRequests() {
        final List<AcademicServiceRequestType> result = new ArrayList<AcademicServiceRequestType>();
        for (final AcademicServiceRequestType type : values()) {
            if (type.isServiceRequest) {
                result.add(type);
            }
        }
        return result;
    }

    public String localizedName(Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, locale, getQualifiedName());
    }

    protected String localizedName() {
        return localizedName(I18N.getLocale());
    }

    public String getLocalizedName() {
        return localizedName();
    }

}
