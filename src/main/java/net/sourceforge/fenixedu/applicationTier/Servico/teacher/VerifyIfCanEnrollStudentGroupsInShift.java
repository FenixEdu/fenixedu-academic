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
 * Created on 13/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author joaosa & rmalo
 * 
 */
public class VerifyIfCanEnrollStudentGroupsInShift {

    protected Boolean run(String executionCourseCode, String groupPropertiesCode, String shiftCode) throws FenixServiceException {
        final Grouping grouping = FenixFramework.getDomainObject(groupPropertiesCode);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final Shift shift = FenixFramework.getDomainObject(shiftCode);

        if (!shift.containsType(grouping.getShiftType())) {
            return false;
        }

        final Collection studentGroups = grouping.getStudentGroupsSet();
        List studentGroupsAux = getStudentGroupsByShift(grouping, shift);

        if (studentGroups.size() == studentGroupsAux.size()) {
            return false;
        }

        return true;
    }

    private List getStudentGroupsByShift(Grouping grouping, Shift shift) {
        List result = new ArrayList();
        List<StudentGroup> studentGroups = grouping.getStudentGroupsWithShift();
        for (final StudentGroup studentGroup : studentGroups) {
            if (studentGroup.getShift().equals(shift)) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final VerifyIfCanEnrollStudentGroupsInShift serviceInstance = new VerifyIfCanEnrollStudentGroupsInShift();

    @Atomic
    public static Boolean runVerifyIfCanEnrollStudentGroupsInShift(String executionCourseCode, String groupPropertiesCode,
            String shiftCode) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, groupPropertiesCode, shiftCode);
    }

}