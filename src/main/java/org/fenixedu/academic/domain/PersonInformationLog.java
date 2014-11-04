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

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class PersonInformationLog extends PersonInformationLog_Base {

    public PersonInformationLog() {
        super();
    }

    public PersonInformationLog(Person personViewed, String description) {
        super();
        if (getPersonViewed() == null) {
            setPersonViewed(personViewed);
        }
        setDescription(description);
    }

    public String getPersonName() {
        return getPerson() != null ? getPerson().getName() : StringUtils.EMPTY;
    }

    public String getUsername() {
        return getPerson() != null ? getPerson().getUsername() : StringUtils.EMPTY;
    }

    private static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getString(bundle, key, args).trim();
    }

    public static PersonInformationLog createLog(Person personViewed, String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return new PersonInformationLog(personViewed, label);
    }

    public static String getPersonNameForLogDescription(Person person) {
        String personViewed;
        if ((person.getUsername() != null) && !(person.getUsername().isEmpty())) {
            personViewed = person.getUsername();
        } else if ((person.getPartyName() != null) && !(person.getPartyName().isEmpty())) {
            personViewed = person.getName();
        } else {
            personViewed = "ID:" + person.getExternalId();
        }
        return personViewed;
    }

}
