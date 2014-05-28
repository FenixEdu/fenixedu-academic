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
package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
        if (hasAnyExtraCurricularActivity()) {
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivity> getExtraCurricularActivity() {
        return getExtraCurricularActivitySet();
    }

    @Deprecated
    public boolean hasAnyExtraCurricularActivity() {
        return !getExtraCurricularActivitySet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
