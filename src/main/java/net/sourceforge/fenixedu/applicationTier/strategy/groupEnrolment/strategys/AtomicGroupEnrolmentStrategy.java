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
 * Created on 24/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author asnr and scpo
 * 
 */

public class AtomicGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {

    public AtomicGroupEnrolmentStrategy() {
    }

    @Override
    public Integer enrolmentPolicyNewGroup(Grouping grouping, int numberOfStudentsToEnrole, Shift shift) {

        if (checkNumberOfGroups(grouping, shift)) {
            Integer maximumCapacity = grouping.getMaximumCapacity();
            Integer minimumCapacity = grouping.getMinimumCapacity();
            Integer nrStudents = Integer.valueOf(numberOfStudentsToEnrole);

            if (maximumCapacity == null && minimumCapacity == null) {
                return Integer.valueOf(1);
            }
            if (minimumCapacity != null) {
                if (nrStudents.compareTo(minimumCapacity) < 0) {
                    return Integer.valueOf(-2);
                }
            }
            if (maximumCapacity != null) {
                if (nrStudents.compareTo(maximumCapacity) > 0) {
                    return Integer.valueOf(-3);
                }
            }
        } else {
            return Integer.valueOf(-1);
        }

        return Integer.valueOf(1);
    }

    @Override
    public boolean checkNumberOfGroupElements(Grouping grouping, StudentGroup studentGroup) {

        boolean result = false;
        final Integer minimumCapacity = grouping.getMinimumCapacity();

        if (minimumCapacity == null) {
            result = true;
        } else {
            final int numberOfGroupElements = studentGroup.getAttends().size();
            if (numberOfGroupElements > minimumCapacity.intValue()) {
                result = true;
            }
        }
        return result;
    }
}