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
package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.commons.lang.StringUtils;

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
        return hasPerson() ? getPerson().getName() : StringUtils.EMPTY;
    }

    public String getIstUsername() {
        return hasPerson() ? getPerson().getIstUsername() : StringUtils.EMPTY;
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
        if ((person.getIstUsername() != null) && !(person.getIstUsername().isEmpty())) {
            personViewed = person.getIstUsername();
        } else if ((person.getPartyName() != null) && !(person.getPartyName().isEmpty())) {
            personViewed = person.getName();
        } else {
            personViewed = "ID:" + person.getExternalId();
        }
        return personViewed;
    }

    @Deprecated
    public boolean hasPersonViewed() {
        return getPersonViewed() != null;
    }

}
