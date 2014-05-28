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
 * Created on 2004/04/15
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkStudentGroupByUsername {

    @Atomic
    public static InfoGroup run(final Person personUser, final ExecutionDegree executionDegree) {
        check(RolePredicates.STUDENT_PREDICATE);
        final FinalDegreeWorkGroup finalDegreeWorkGroup = findFinalDegreeWorkGroup(personUser, executionDegree);
        return InfoGroup.newInfoFromDomain(finalDegreeWorkGroup);
    }

    private static FinalDegreeWorkGroup findFinalDegreeWorkGroup(final Person personUser, final ExecutionDegree executionDegree) {
        FinalDegreeWorkGroup finalDegreeWorkGroup = find(personUser, executionDegree, DegreeType.BOLONHA_MASTER_DEGREE);
        if (finalDegreeWorkGroup == null) {
            finalDegreeWorkGroup = find(personUser, executionDegree, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
        }
        if (finalDegreeWorkGroup == null) {
            finalDegreeWorkGroup = find(personUser, executionDegree, DegreeType.BOLONHA_DEGREE);
        }
        if (finalDegreeWorkGroup == null) {
            finalDegreeWorkGroup = find(personUser, executionDegree, DegreeType.DEGREE);
        }
        return finalDegreeWorkGroup;
    }

    private static FinalDegreeWorkGroup find(final Person personUser, final ExecutionDegree executionYear,
            final DegreeType degreeType) {
        for (final Registration registration : personUser.getStudent().getRegistrationsSet()) {
            if (registration.getDegreeType() == degreeType) {
                final FinalDegreeWorkGroup finalDegreeWorkGroup =
                        registration.findFinalDegreeWorkGroupForExecutionYear(executionYear);
                if (finalDegreeWorkGroup != null) {
                    return finalDegreeWorkGroup;
                }
            }
        }
        return null;
    }

}