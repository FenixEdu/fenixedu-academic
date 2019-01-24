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
package org.fenixedu.academic.domain;

import org.fenixedu.academic.domain.branch.BranchType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author dcs-rjao
 * 
 *         19/Mar/2003
 */

public class Branch extends Branch_Base {

    public Branch() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Branch(String name, String nameEn, String code, DegreeCurricularPlan degreeCurricularPlan) {
        this();
        setName(name);
        setNameEn(nameEn);
        setCode(code);
        setDegreeCurricularPlan(degreeCurricularPlan);
    }

    public Boolean representsCommonBranch() {
        if (getBranchType() != null && getBranchType().equals(BranchType.COMNBR)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void edit(String name, String nameEn, String code) {
        setName(name);
        setNameEn(nameEn);
        setCode(code);
    }

    public void delete() throws DomainException {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        final Branch commonBranch = findCommonBranchForSameDegreeCurricularPlan();

        this.getStudentCurricularPlansSet().clear();

        setDegreeCurricularPlan(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private Branch findCommonBranchForSameDegreeCurricularPlan() {
        for (Branch branch : getDegreeCurricularPlan().getAreasSet()) {
            if (branch.representsCommonBranch() && branch.getName().equals("")) {
                return branch;
            }
        }
        return null;
    }

    // Static methods
    public static Branch readByBranchType(final BranchType branchType) {
        for (final Branch branch : Bennu.getInstance().getBranchsSet()) {
            if (branch.getBranchType() == branchType) {
                return branch;
            }
        }
        return null;
    }

    public boolean isCommonBranch() {
        return getBranchType() == BranchType.COMNBR;
    }

    public boolean isSpecializationBranch() {
        return getBranchType() == BranchType.SPECBR;
    }

    public boolean isSecondaryBranch() {
        return getBranchType() == BranchType.SECNBR;
    }

}
