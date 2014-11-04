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
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;

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
