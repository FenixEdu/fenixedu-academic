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
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixframework.Atomic;

public class ReadExecutionDegreesByExecutionYear {

    @Atomic
    public static List run(InfoExecutionYear infoExecutionYear) {

        final List infoExecutionDegreeList = new ArrayList();
        final List<ExecutionDegree> executionDegrees = readExecutionDegrees(infoExecutionYear);

        for (ExecutionDegree executionDegree : executionDegrees) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            infoExecutionDegreeList.add(infoExecutionDegree);
        }
        return infoExecutionDegreeList;
    }

    private static List<ExecutionDegree> readExecutionDegrees(final InfoExecutionYear infoExecutionYear) {
        if (infoExecutionYear == null) {
            return ExecutionDegree.getAllByExecutionYear(ExecutionYear.readCurrentExecutionYear().getYear());
        }
        return ExecutionDegree.getAllByExecutionYear(infoExecutionYear.getYear());
    }

}