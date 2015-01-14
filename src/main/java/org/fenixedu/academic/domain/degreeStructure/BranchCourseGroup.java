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
package org.fenixedu.academic.domain.degreeStructure;

import org.fenixedu.academic.domain.exceptions.DomainException;

@Deprecated
//deliberately removed public constructors, this subclass should not be instanciated anymore
//only exists until data migration of all instances is concluded
public class BranchCourseGroup extends BranchCourseGroup_Base {

    protected BranchCourseGroup() {
        super();
    }

    @Override
    public void setBranchType(BranchType branchType) {
        if (branchType == null) {
            throw new DomainException("error.degreeStructure.BranchCourseGroup.branch.type.cannot.be.null");
        }

        super.setBranchType(branchType);
    }

}
