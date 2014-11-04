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
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerAulasDeTurma {

    @Atomic
    public static List<InfoLessonInstanceAggregation> run(InfoClass infoClass) {
        final SchoolClass schoolClass = FenixFramework.getDomainObject(infoClass.getExternalId());

        final Collection<Shift> shiftList = schoolClass.getAssociatedShiftsSet();

        final List<InfoLessonInstanceAggregation> infoLessonList = new ArrayList<InfoLessonInstanceAggregation>();
        for (final Shift shift : shiftList) {
            infoLessonList.addAll(InfoLessonInstanceAggregation.getAggregations(shift));
        }

        return infoLessonList;
    }

}