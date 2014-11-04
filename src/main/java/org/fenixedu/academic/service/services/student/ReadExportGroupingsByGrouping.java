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
package org.fenixedu.academic.service.services.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.ExportGrouping;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoExportGrouping;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExportGroupingsByGrouping {

    @Atomic
    public static List<InfoExportGrouping> run(String groupingOID) {
        final Grouping grouping = FenixFramework.getDomainObject(groupingOID);
        final Collection<ExportGrouping> exportGroupings = grouping.getExportGroupingsSet();

        final List<InfoExportGrouping> infoExportGroupings = new ArrayList<InfoExportGrouping>(exportGroupings.size());
        for (final ExportGrouping exportGrouping : exportGroupings) {
            final InfoExportGrouping infoExportGrouping = new InfoExportGrouping();
            infoExportGrouping.setExternalId(exportGrouping.getExternalId());
            infoExportGrouping.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(exportGrouping.getExecutionCourse()));
            infoExportGroupings.add(infoExportGrouping);
        }
        return infoExportGroupings;
    }
}