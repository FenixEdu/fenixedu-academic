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
 * Created on 30/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Jo�o Mota
 * 
 *         30/Jun/2003 fenix-branch ServidorAplicacao.Servico.sop
 * 
 */
public class ReadAvailableClassesForShift {

    @Atomic
    public static List run(String shiftOID) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        final Shift shift = FenixFramework.getDomainObject(shiftOID);
        final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
        final ExecutionYear executionYear = executionSemester.getExecutionYear();

        final Set<SchoolClass> availableSchoolClasses = new HashSet<SchoolClass>();

        for (DegreeCurricularPlan degreeCurricularPlan : executionCourse.getAssociatedDegreeCurricularPlans()) {
            for (SchoolClass schoolClass : degreeCurricularPlan.getExecutionDegreeByAcademicInterval(
                    executionCourse.getAcademicInterval()).getSchoolClassesSet()) {
                if (schoolClass.getAcademicInterval().equals(executionCourse.getAcademicInterval())) {
                    if (!shift.getAssociatedClassesSet().contains(schoolClass)) {
                        availableSchoolClasses.add(schoolClass);
                    }
                }
            }
        }

        final List<InfoClass> infoClasses = new ArrayList<InfoClass>(availableSchoolClasses.size());
        for (final SchoolClass schoolClass : availableSchoolClasses) {
            final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
            infoClasses.add(infoClass);
        }

        return infoClasses;
    }
}