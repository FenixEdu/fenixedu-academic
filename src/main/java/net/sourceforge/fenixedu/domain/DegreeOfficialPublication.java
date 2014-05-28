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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DegreeOfficialPublication extends DegreeOfficialPublication_Base {
    public DegreeOfficialPublication(Degree degree, LocalDate date) {
        if (degree == null) {
            throw new DomainException("error.degree.officialpublication.unlinked");
        }
        if (date == null) {
            throw new DomainException("error.degree.officialpublication.undated");
        }
        setDegree(degree);
        setPublication(date);
    }

    @Atomic
    public DegreeSpecializationArea createSpecializationArea(String nameEn, String namePt) {

        MultiLanguageString area = new MultiLanguageString(MultiLanguageString.en, nameEn).with(MultiLanguageString.pt, namePt);

        return new DegreeSpecializationArea(this, area);
    }

    @Atomic
    public void changeOfficialreference(String officialReference, final LocalDate publication) {
        this.setOfficialReference(officialReference);
        this.setPublication(publication);
    }

    @Atomic
    public void delete() {

        setDegree(null);

        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeSpecializationArea> getSpecializationArea() {
        return getSpecializationAreaSet();
    }

    @Deprecated
    public boolean hasAnySpecializationArea() {
        return !getSpecializationAreaSet().isEmpty();
    }

    @Deprecated
    public boolean hasOfficialReference() {
        return getOfficialReference() != null;
    }

    @Deprecated
    public boolean hasUniversityReference() {
        return getUniversityReference() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasPublication() {
        return getPublication() != null;
    }

    @Deprecated
    public boolean hasDgesReference() {
        return getDgesReference() != null;
    }

}
