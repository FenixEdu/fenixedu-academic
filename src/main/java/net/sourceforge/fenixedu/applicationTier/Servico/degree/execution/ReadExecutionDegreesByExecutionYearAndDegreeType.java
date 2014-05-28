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
/*
 * Created on Dec 11, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.execution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixframework.Atomic;

/**
 * @author jpvl
 */
public class ReadExecutionDegreesByExecutionYearAndDegreeType {

    @Atomic
    public static List run(String executionYear, DegreeType... degreeType) {

        final List<ExecutionDegree> executionDegrees =
                degreeType == null ? ExecutionDegree.getAllByExecutionYear(executionYear) : ExecutionDegree
                        .getAllByExecutionYearAndDegreeType(executionYear, degreeType);

        final List infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            infoExecutionDegreeList.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));
        }
        return infoExecutionDegreeList;
    }
}