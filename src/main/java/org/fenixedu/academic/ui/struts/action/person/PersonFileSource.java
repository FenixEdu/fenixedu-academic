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

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public interface PersonFileSource extends Serializable {

    public static Comparator<PersonFileSource> COMPARATOR = new Comparator<PersonFileSource>() {

        @Override
        public int compare(PersonFileSource o1, PersonFileSource o2) {
            int c = o1.getName().compareTo(o2.getName());
            if (c != 0) {
                return c;
            } else {
                int o1Count = o1.getCount();
                int o2Count = o2.getCount();

                if (o1Count < o2Count) {
                    return -1;
                } else if (o1Count > o2Count) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }

    };

    public MultiLanguageString getName();

    public List<PersonFileSource> getChildren();

    public int getCount();

    public boolean isAllowedToUpload(Person person);
}
