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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;

public class InfoGroupingWithAttends extends InfoGrouping {

    @Override
    public void copyFromDomain(Grouping grouping) {
        super.copyFromDomain(grouping);
        if (grouping != null) {
            final Collection<Attends> attendsList = grouping.getAttends();
            final List<InfoFrequenta> infoAttendsList = new ArrayList<InfoFrequenta>(attendsList.size());
            for (final Attends attends : attendsList) {
                infoAttendsList.add(InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment.newInfoFromDomain(attends));
            }
            setInfoAttends(infoAttendsList);
        }
    }

    public static InfoGroupingWithAttends newInfoFromDomain(Grouping groupProperties) {
        InfoGroupingWithAttends infoGroupProperties = null;
        if (groupProperties != null) {
            infoGroupProperties = new InfoGroupingWithAttends();
            infoGroupProperties.copyFromDomain(groupProperties);
        }

        return infoGroupProperties;
    }

}
