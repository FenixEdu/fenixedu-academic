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
package org.fenixedu.academic.domain.student.curriculum;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class ExtraCurricularActivityType extends ExtraCurricularActivityType_Base {
    public ExtraCurricularActivityType() {
        setRootDomainObject(Bennu.getInstance());
    }

    public ExtraCurricularActivityType(MultiLanguageString name) {
        setName(name);
        setRootDomainObject(Bennu.getInstance());
    }

    @Atomic
    public void delete() {
        if (!getExtraCurricularActivitySet().isEmpty()) {
            throw new DomainException("error.extraCurricularActivityTypes.unableToDeleteUsedType", this.getName().getContent());
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public String getNamePt() {
        return getName().getContent(MultiLanguageString.pt);
    }

    public void setNamePt(String name) {
        if (getName() == null) {
            setName(new MultiLanguageString(MultiLanguageString.pt, name));
        } else {
            setName(getName().with(MultiLanguageString.pt, name));
        }
    }

    public String getNameEn() {
        return getName().getContent(MultiLanguageString.en);
    }

    public void setNameEn(String name) {
        if (getName() == null) {
            setName(new MultiLanguageString(MultiLanguageString.en, name));
        } else {
            setName(getName().with(MultiLanguageString.en, name));
        }
    }

}
