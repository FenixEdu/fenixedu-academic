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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class CandidacyProcessSelectDegreesBean implements Serializable {

    private static final long serialVersionUID = -3289144446453054775L;

    private List<Degree> degrees = new ArrayList<Degree>();

    public CandidacyProcessSelectDegreesBean() {
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

    public Collection<Degree> getFirstCycleDegrees() {
        return filterDegrees(Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
    }

    public Collection<Degree> getSecondCycleDegrees() {
        return filterDegrees(Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE,
                DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
    }

    protected Collection<Degree> filterDegrees(Collection<Degree> degrees) {
        final Set<AcademicProgram> programs =
                AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_CANDIDACY_PROCESSES);
        return Collections2.filter(degrees, new Predicate<Degree>() {
            @Override
            public boolean apply(Degree degree) {
                return programs.contains(degree);
            }
        });
    }

}
