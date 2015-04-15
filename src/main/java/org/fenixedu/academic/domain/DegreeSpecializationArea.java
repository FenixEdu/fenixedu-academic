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
import org.fenixedu.academic.util.MultiLanguageString;

import pt.ist.fenixframework.Atomic;

public class DegreeSpecializationArea extends DegreeSpecializationArea_Base {

    public DegreeSpecializationArea(DegreeOfficialPublication officialPublication, MultiLanguageString area) {
        super();
        init(officialPublication, area);
    }

    protected void init(DegreeOfficialPublication degreeOfficialPublication, MultiLanguageString area) {
        checkParameters(degreeOfficialPublication, area);
        setOfficialPublication(degreeOfficialPublication);
        setName(area);
    }

    private void checkParameters(DegreeOfficialPublication degreeOfficialPublication, MultiLanguageString area) {
        if (degreeOfficialPublication == null) {
            throw new DomainException(DegreeSpecializationArea.class.getName() + ".degreeOfficialPublication.required");
        }
        if (area == null) {
            throw new DomainException(MultiLanguageString.class.getName() + ".area.required");
        }

        if (area.getAllLocales().isEmpty()) {
            throw new DomainException(DegreeSpecializationArea.class.getName() + ".area.names.required");
        }

        if (!verifyIfSomeContentsAreNotEmpty(area)) {
            throw new DomainException(DegreeSpecializationArea.class.getName() + ".area.names.nameForLanguage.required");
        }
    }

    private boolean verifyIfSomeContentsAreNotEmpty(MultiLanguageString area) {
        for (String language : area.getAllContents()) {
            if (!language.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void delete() {
        setOfficialPublication(null);
        deleteDomainObject();
    }

    @Atomic
    public void setNameEn(String nameEn) {
        setName(getName().with(MultiLanguageString.en, nameEn));
    }

    @Atomic
    public void setNamePt(String namePt) {
        setName(getName().with(MultiLanguageString.pt, namePt));
    }

    public String getNameEn() {
        return this.getName().getContent(MultiLanguageString.en);
    }

    public String getNamePt() {
        return this.getName().getContent(MultiLanguageString.pt);
    }

}
