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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixframework.Atomic;

public class ReadNonMasterExecutionDegreesByExecutionYear {

    @Atomic
    public static List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

        final String yearToSearch;
        if (infoExecutionYear.getYear() == null || infoExecutionYear.getYear().length() == 0) {
            yearToSearch = ExecutionYear.readCurrentExecutionYear().getYear();
        } else {
            yearToSearch = infoExecutionYear.getYear();
        }

        // remove ExecutionDegree DAO
        List<ExecutionDegree> executionDegrees =
                ExecutionDegree.getAllByExecutionYearAndDegreeType(yearToSearch, DegreeType.DEGREE);

        final List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            infoExecutionDegreeList.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));
        }

        return infoExecutionDegreeList;
    }

}