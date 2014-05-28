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
package net.sourceforge.fenixedu.dataTransferObject.academicAdministration;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class DegreeByExecutionYearBean implements Serializable, Comparable<DegreeByExecutionYearBean> {

    private Degree degree;
    private DegreeType degreeType;
    private ExecutionYear executionYear;
    private Set<DegreeType> administratedDegreeTypes;
    private Set<Degree> administratedDegrees;

    public DegreeByExecutionYearBean() {
    }

    public DegreeByExecutionYearBean(final Degree degree, final ExecutionYear executionYear) {
        setDegree(degree);
        setDegreeType(degree.getDegreeType());
        setExecutionYear(executionYear);
    }

    public DegreeByExecutionYearBean(Set<DegreeType> administratedDegreeTypes, Set<Degree> administratedDegrees) {
        this.administratedDegreeTypes = administratedDegreeTypes;
        this.administratedDegrees = administratedDegrees;
    }

    public Set<DegreeType> getAdministratedDegreeTypes() {
        return administratedDegreeTypes;
    }

    public SortedSet<Degree> getAdministratedDegrees() {
        final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

        for (final Degree degree : administratedDegrees) {
            if (executionYear != null && degree.getExecutionDegreesForExecutionYear(executionYear).isEmpty()) {
                continue;
            }
            if (degreeType != null && !degreeType.equals(degree.getDegreeType())) {
                continue;
            }
            result.add(degree);
        }

        return result;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public String getDegreeName() {
        return getDegree().getNameFor(getExecutionYear().getAcademicInterval()).getContent();
    }

    public String getDegreePresentationName() {
        return getDegree().getPresentationName(getExecutionYear());
    }

    public String getKey() {
        return getDegree().getExternalId() + ":" + getExecutionYear().getExternalId();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DegreeByExecutionYearBean) ? equals((DegreeByExecutionYearBean) obj) : false;
    }

    public boolean equals(DegreeByExecutionYearBean bean) {
        return getDegree() == bean.getDegree();
    }

    @Override
    public int hashCode() {
        Degree degree = getDegree();
        if (degree != null) {
            return getDegree().hashCode();
        } else {
            return 0;
        }
    }

    public Collection<DegreeCurricularPlan> getDegreeCurricularPlans() {
        return getDegree().getDegreeCurricularPlans();
    }

    @Override
    public int compareTo(final DegreeByExecutionYearBean other) {
        return (other == null) ? 1 : Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID.compare(getDegree(), other.getDegree());
    }
}
