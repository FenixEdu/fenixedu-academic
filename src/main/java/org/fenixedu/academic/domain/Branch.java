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

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.branch.BranchType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

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

    private Boolean canDeleteAllEligibleCurricularCourseScopes(final Branch commonBranch) {
        Iterator<CurricularCourseScope> branchCurricularCourseScopesIterator = getScopesSet().iterator();
        while (branchCurricularCourseScopesIterator.hasNext()) {
            CurricularCourseScope scope = branchCurricularCourseScopesIterator.next();
            CurricularCourse curricularCourse = scope.getCurricularCourse();

            // if CurricularCourse already has a common Branch
            if (hasCurricularCourseCommonBranchInAnyCurricularCourseScope(curricularCourse, commonBranch)) {
                // we want to delete this CurricularCourseScope

                if (!scope.isDeletable()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);

        if (this.representsCommonBranch() && !this.getScopesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.branch.cant.delete"));
        }

        if (!canDeleteAllEligibleCurricularCourseScopes(findCommonBranchForSameDegreeCurricularPlan())) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.branch.cant.delete"));
        }
    }

    public void delete() throws DomainException {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        final Branch commonBranch = findCommonBranchForSameDegreeCurricularPlan();

        this.getStudentCurricularPlansSet().clear();

        removeCurricularCourseScopes(commonBranch);
        setDegreeCurricularPlan(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private void removeCurricularCourseScopes(final Branch commonBranch) throws DomainException {
        Iterator<CurricularCourseScope> branchCurricularCourseScopesIterator = getScopesSet().iterator();
        while (branchCurricularCourseScopesIterator.hasNext()) {
            CurricularCourseScope scope = branchCurricularCourseScopesIterator.next();
            CurricularCourse curricularCourse = scope.getCurricularCourse();

            // if CurricularCourse already has a common Branch
            if (hasCurricularCourseCommonBranchInAnyCurricularCourseScope(curricularCourse, commonBranch)) {
                // delete the CurricularCourseScope
                branchCurricularCourseScopesIterator.remove();
                scope.setBranch(null);
                scope.delete();

            } else {
                // set the Branch in the CurricularCourseScope to commonBranch
                branchCurricularCourseScopesIterator.remove();
                scope.setBranch(commonBranch);
            }
        }
    }

    private Branch findCommonBranchForSameDegreeCurricularPlan() {
        for (Branch branch : getDegreeCurricularPlan().getAreasSet()) {
            if (branch.representsCommonBranch() && branch.getName().equals("")) {
                return branch;
            }
        }
        return null;
    }

    private Boolean hasCurricularCourseCommonBranchInAnyCurricularCourseScope(CurricularCourse curricularCourse,
            final Branch commonBranch) {
        return ((CurricularCourseScope) CollectionUtils.find(curricularCourse.getScopesSet(), new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                CurricularCourseScope ccs = (CurricularCourseScope) o;
                return ccs.getBranch().equals(commonBranch);
            }
        }) != null);
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
