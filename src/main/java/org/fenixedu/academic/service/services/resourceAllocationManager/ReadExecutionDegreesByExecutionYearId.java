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
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExecutionDegreesByExecutionYearId {

    @Atomic
    public static List run(String executionYearId) {

        List<InfoExecutionDegree> infoExecutionDegreeList = null;

        ExecutionYear executionYear = null;
        if (StringUtils.isEmpty(executionYearId)) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = FenixFramework.getDomainObject(executionYearId);
        }

        List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByExecutionYear(executionYear.getYear());

        if (executionDegrees != null && executionDegrees.size() > 0) {
            Iterator iterator = executionDegrees.iterator();
            infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();

            while (iterator.hasNext()) {
                ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                infoExecutionDegreeList.add(infoExecutionDegree);
            }
        }

        return infoExecutionDegreeList;
    }

}