/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 24/Jul/2003
 *
 */
package org.fenixedu.academic.service.strategy.groupEnrolment.strategys;

import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentGroup;

/**
 * @author asnr and scpo
 * 
 */

public class IndividualGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {

    public IndividualGroupEnrolmentStrategy() {

    }

    @Override
    public Integer enrolmentPolicyNewGroup(Grouping groupProperties, int numberOfStudentsToEnrole, Shift shift) {

        if (checkNumberOfGroups(groupProperties, shift)) {
            return Integer.valueOf(1);
        }

        return Integer.valueOf(-1);
    }

    @Override
    public boolean checkNumberOfGroupElements(Grouping groupProperties, StudentGroup studentGroup) {
        return true;
    }
}
