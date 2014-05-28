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
package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Absence extends Absence_Base {

    public Absence(final String giafId, final MultiLanguageString name) {
        super();
        String[] args1 = {};
        if (giafId == null || giafId.isEmpty()) {
            throw new DomainException("", args1);
        }
        String[] args = {};
        if (name == null) {
            throw new DomainException("", args);
        }
        setRootDomainObject(Bennu.getInstance());
        setGiafId(giafId);
        setName(name);
        setImportAbsence(false);
        setIsSabaticalOrEquivalent(false);
        setHasMandatoryCredits(true);
        setGiveCredits(false);
    }

    @Atomic
    public void edit(final MultiLanguageString name) {
        String[] args = {};
        if (name == null) {
            throw new DomainException("", args);
        }
        setName(name);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonAbsence> getPersonAbsences() {
        return getPersonAbsencesSet();
    }

    @Deprecated
    public boolean hasAnyPersonAbsences() {
        return !getPersonAbsencesSet().isEmpty();
    }

    @Deprecated
    public boolean hasIsSabaticalOrEquivalent() {
        return getIsSabaticalOrEquivalent() != null;
    }

    @Deprecated
    public boolean hasGiveCredits() {
        return getGiveCredits() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasHasMandatoryCredits() {
        return getHasMandatoryCredits() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasImportAbsence() {
        return getImportAbsence() != null;
    }

    @Deprecated
    public boolean hasGiafId() {
        return getGiafId() != null;
    }

}
