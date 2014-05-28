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
package net.sourceforge.fenixedu.domain;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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

    private Boolean canDeleteAllEligibleCurricularCourseScopes(final Branch commonBranch) {
        Iterator<CurricularCourseScope> branchCurricularCourseScopesIterator = getScopesSet().iterator();
        while (branchCurricularCourseScopesIterator.hasNext()) {
            CurricularCourseScope scope = branchCurricularCourseScopesIterator.next();
            CurricularCourse curricularCourse = scope.getCurricularCourse();

            // if CurricularCourse already has a common Branch
            if (hasCurricularCourseCommonBranchInAnyCurricularCourseScope(curricularCourse, commonBranch)) {
                // we want to delete this CurricularCourseScope

                if (!scope.canBeDeleted()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean canBeDeleted() {
        if (this.hasAnyAssociatedFinalDegreeWorkProposals()) {
            throw new DomainException("error.branch.cant.delete");
        }

        if (this.representsCommonBranch() && this.hasAnyScopes()) {
            throw new DomainException("error.branch.cant.delete");
        }

        Branch commonBranch = findCommonBranchForSameDegreeCurricularPlan();
        // if (commonBranch == null)
        // throw new DomainException("error.branch.cant.delete");

        if (!canDeleteAllEligibleCurricularCourseScopes(commonBranch)) {
            throw new DomainException("error.branch.cant.delete");
        }

        return true;
    }

    public void delete() throws DomainException {

        if (!this.canBeDeleted()) {
            throw new DomainException("error.branch.cant.delete");
        }

        final Branch commonBranch = findCommonBranchForSameDegreeCurricularPlan();

        this.getStudentCurricularPlans().clear();

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
        for (Branch branch : getDegreeCurricularPlan().getAreas()) {
            if (branch.representsCommonBranch() && branch.getName().equals("")) {
                return branch;
            }
        }
        return null;
    }

    private Boolean hasCurricularCourseCommonBranchInAnyCurricularCourseScope(CurricularCourse curricularCourse,
            final Branch commonBranch) {
        return ((CurricularCourseScope) CollectionUtils.find(curricularCourse.getScopes(), new Predicate() {
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal> getAssociatedFinalDegreeWorkProposals() {
        return getAssociatedFinalDegreeWorkProposalsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedFinalDegreeWorkProposals() {
        return !getAssociatedFinalDegreeWorkProposalsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourseScope> getScopes() {
        return getScopesSet();
    }

    @Deprecated
    public boolean hasAnyScopes() {
        return !getScopesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.StudentCurricularPlan> getStudentCurricularPlans() {
        return getStudentCurricularPlansSet();
    }

    @Deprecated
    public boolean hasAnyStudentCurricularPlans() {
        return !getStudentCurricularPlansSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSecondaryCredits() {
        return getSecondaryCredits() != null;
    }

    @Deprecated
    public boolean hasNameEn() {
        return getNameEn() != null;
    }

    @Deprecated
    public boolean hasSpecializationCredits() {
        return getSpecializationCredits() != null;
    }

    @Deprecated
    public boolean hasAcronym() {
        return getAcronym() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

    @Deprecated
    public boolean hasBranchType() {
        return getBranchType() != null;
    }

}
