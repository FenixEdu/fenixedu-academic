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
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PersonFileSourceBean implements PersonFileSource {

    /**
     * Default serial id.
     */
    private static final long serialVersionUID = 1L;

    private Unit unit;
    private int count;

    public PersonFileSourceBean(Unit unit) {
        this.unit = unit;
        this.count = -1;
    }

    @Override
    public MultiLanguageString getName() {
        return getUnit().getNameI18n();
    }

    public Unit getUnit() {
        return this.unit;
    }

    @Override
    public int getCount() {
        if (this.count < 0) {
            this.count = getUnit().getAccessibileFiles(AccessControl.getPerson()).size();
        }

        return this.count;
    }

    @Override
    public List<PersonFileSource> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAllowedToUpload(Person person) {
        return getUnit().isUserAllowedToUploadFiles(person);
    }

}
