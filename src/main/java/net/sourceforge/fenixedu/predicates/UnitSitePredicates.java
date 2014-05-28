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
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteLink;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class UnitSitePredicates {

    public static final AccessControlPredicate<UnitSite> managers = new AccessControlPredicate<UnitSite>() {

        @Override
        public boolean evaluate(UnitSite site) {
            Person person = AccessControl.getPerson();
            return person != null
                    && (person.hasRole(RoleType.MANAGER) || person.hasRole(RoleType.SCIENTIFIC_COUNCIL) || site.getManagersSet()
                            .contains(person));
        }

    };

    public static final AccessControlPredicate<UnitSiteLink> linkSiteManagers = new AccessControlPredicate<UnitSiteLink>() {

        @Override
        public boolean evaluate(UnitSiteLink link) {
            UnitSite site = nullOr(link.getTopUnitSite(), link.getFooterUnitSite());
            return site == null ? true : managers.evaluate(site);
        }

    };

    private static <T> T nullOr(T... args) {
        for (T value : args) {
            if (value != null) {
                return value;
            }
        }

        return null;
    }
}
