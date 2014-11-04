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
package org.fenixedu.academic.domain.student;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class RegistrationStateLog extends RegistrationStateLog_Base {

    public RegistrationStateLog() {
        super();
    }

    public RegistrationStateLog(Registration registration, String description) {
        super();
        if (getRegistration() == null) {
            setRegistration(registration);
        }
        setDescription(description);
    }

    public static RegistrationStateLog createRegistrationStateLog(Registration registration, String description) {
        return new RegistrationStateLog(registration, description);
    }

    public static RegistrationStateLog createRegistrationStateLog(Registration registration, String bundle, String key,
            String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createRegistrationStateLog(registration, label);
    }

    protected static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getString(bundle, key, args);
    }

    public String getPersonName() {
        return getPerson() != null ? getPerson().getName() : StringUtils.EMPTY;
    }

    public String getUsername() {
        return getPerson() != null ? getPerson().getUsername() : StringUtils.EMPTY;
    }
}
