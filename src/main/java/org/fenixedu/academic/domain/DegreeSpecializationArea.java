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

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class DegreeSpecializationArea extends DegreeSpecializationArea_Base {

    public DegreeSpecializationArea(DegreeOfficialPublication officialPublication, LocalizedString area) {
        super();
        init(officialPublication, area);
    }

    protected void init(DegreeOfficialPublication degreeOfficialPublication, LocalizedString area) {
        checkParameters(degreeOfficialPublication, area);
        setOfficialPublication(degreeOfficialPublication);
        setName(area);
    }

    private void checkParameters(DegreeOfficialPublication degreeOfficialPublication, LocalizedString area) {
        if (degreeOfficialPublication == null) {
            throw new DomainException(DegreeSpecializationArea.class.getName() + ".degreeOfficialPublication.required");
        }
        if (area == null) {
            throw new DomainException(LocalizedString.class.getName() + ".area.required");
        }

        if (area.getLocales().isEmpty()) {
            throw new DomainException(DegreeSpecializationArea.class.getName() + ".area.names.required");
        }

        if (!verifyIfSomeContentsAreNotEmpty(area)) {
            throw new DomainException(DegreeSpecializationArea.class.getName() + ".area.names.nameForLanguage.required");
        }
    }

    private boolean verifyIfSomeContentsAreNotEmpty(LocalizedString area) {
        return area.anyMatch(s -> !s.isEmpty());
    }

    public void delete() {
        setOfficialPublication(null);
        deleteDomainObject();
    }

    @Atomic
    public void setNameEn(String nameEn) {
        setName(getName().with(org.fenixedu.academic.util.LocaleUtils.EN, nameEn));
    }

    @Atomic
    public void setNamePt(String namePt) {
        setName(getName().with(org.fenixedu.academic.util.LocaleUtils.PT, namePt));
    }

    public String getNameEn() {
        return this.getName().getContent(org.fenixedu.academic.util.LocaleUtils.EN);
    }

    public String getNamePt() {
        return this.getName().getContent(org.fenixedu.academic.util.LocaleUtils.PT);
    }

}
