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
package org.fenixedu.academic.domain;

import java.util.stream.Stream;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum ProfessionalSituationConditionType {

    UNKNOWN(false),

    WORKS_FOR_OTHERS(true),

    EMPLOYEER(true),

    INDEPENDENT_WORKER(true),

    WORKS_FOR_FAMILY_WITHOUT_PAYMENT(true),

    RETIRED(true),

    UNEMPLOYED(true),

    HOUSEWIFE(true),

    STUDENT(true),

    INTERN(true),

    GRANT_HOLDER(true),

    MILITARY_SERVICE(false),

    OTHER(true);

    private boolean active;

    private ProfessionalSituationConditionType(boolean active) {
        setActive(active);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return ProfessionalSituationConditionType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return ProfessionalSituationConditionType.class.getName() + "." + name();
    }

    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

    public static Stream<ProfessionalSituationConditionType> getActiveValues() {
        return Stream.of(ProfessionalSituationConditionType.values()).filter(ProfessionalSituationConditionType::isActive);
    }

}
