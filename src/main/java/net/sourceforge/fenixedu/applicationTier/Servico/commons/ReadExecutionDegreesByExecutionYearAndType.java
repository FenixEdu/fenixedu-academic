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
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class ReadExecutionDegreesByExecutionYearAndType {

    @Atomic
    public static List run(String executionYearOID, Set<DegreeType> degreeTypes) {
        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearOID);

        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
        for (final DegreeType degreeType : degreeTypes) {
            executionDegrees.addAll(ExecutionDegree.getAllByExecutionYearAndDegreeType(executionYear.getYear(), degreeType));
        }
        return getInfoExecutionDegrees(executionDegrees);
    }

    @Atomic
    public static List run(final DegreeType typeOfCourse) {

        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final Collection<ExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        final List<InfoExecutionDegree> infoExecutionDegrees = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            final Degree degree = executionDegree.getDegreeCurricularPlan().getDegree();
            if (degree.getDegreeType().equals(typeOfCourse)) {
                infoExecutionDegrees.add(getInfoExecutionDegree(executionDegree));
            }
        }
        return infoExecutionDegrees;
    }

    @Atomic
    public static List run(Degree degree, ExecutionYear executionYear, String tmp) {
        final List<ExecutionDegree> executionDegrees =
                ExecutionDegree.getAllByDegreeAndExecutionYear(degree, executionYear.getYear());
        return getInfoExecutionDegrees(executionDegrees);
    }

    private static List getInfoExecutionDegrees(final List executionDegrees) {
        final List<InfoExecutionDegree> infoExecutionDegrees = new ArrayList<InfoExecutionDegree>(executionDegrees.size());
        for (int i = 0; i < executionDegrees.size(); i++) {
            final ExecutionDegree executionDegree = (ExecutionDegree) executionDegrees.get(i);
            final InfoExecutionDegree infoExecutionDegree = getInfoExecutionDegree(executionDegree);
            infoExecutionDegrees.add(infoExecutionDegree);
        }
        return infoExecutionDegrees;
    }

    private static InfoExecutionDegree getInfoExecutionDegree(final ExecutionDegree executionDegree) {
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}