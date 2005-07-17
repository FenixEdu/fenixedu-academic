package net.sourceforge.fenixedu.domain;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class Branch extends Branch_Base {

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + getIdInternal() + "; ";
        result += "name = " + this.getName() + "; ";
        result += "code = " + this.getCode() + "; ";
        result += "acronym = " + this.getAcronym() + "]\n";
        return result;
    }

    public Boolean representsCommonBranch() {
        if (this.getBranchType().equals(BranchType.COMNBR)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public List getAreaCurricularCourseGroups(final AreaType areaType) {

        return (List) CollectionUtils.select(getAreaCurricularCourseGroups(), new Predicate() {

            public boolean evaluate(Object arg0) {
                ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) arg0;
                return curricularCourseGroup.getAreaType().equals(areaType);
            }
        });
    }
	
	private Boolean canDeleteAllEligibleCurricularCourseScopes (final IBranch commonBranch) {
		Iterator branchCurricularCourseScopesIterator = getScopesIterator();
		while (branchCurricularCourseScopesIterator.hasNext()) {
			ICurricularCourseScope scope = (ICurricularCourseScope)branchCurricularCourseScopesIterator.next();
			ICurricularCourse curricularCourse = scope.getCurricularCourse();

			// if CurricularCourse already has a common Branch
			if (hasCurricularCourseCommonBranchInAnyCurricularCourseScope(curricularCourse,commonBranch)) {
				// we want to delete this CurricularCourseScope
				
				if (!scope.canBeDeleted())
					return false;
			}
		}
		return true;
	}	
	
	public Boolean canBeDeleted() {
		if (this.hasAnyAssociatedFinalDegreeWorkProposals())
			return false;
		
		if (this.representsCommonBranch() && 
				(this.hasAnyCurricularCourseGroups() || this.hasAnyScopes() || this.hasAnyOptionalCurricularCourseGroups() || this.hasAnyAreaCurricularCourseGroups()))
			return false;
		
		IBranch commonBranch = findCommonBranchForSameDegreeCurricularPlan();
		if (commonBranch == null)
			return false;
		
		if (!canDeleteAllEligibleCurricularCourseScopes(commonBranch))
			return false;
		
		return true;
	}
	
	public void delete() throws DomainException {
		
		if (!this.canBeDeleted())
			throw new DomainException(this.getClass().getName(),"ola mundo");
		
		IBranch commonBranch = findCommonBranchForSameDegreeCurricularPlan();

		this.getStudentCurricularPlans().clear();
		this.getSecundaryStudentCurricularPlansLEIC().clear();
		this.getSecundaryStudentCurricularPlansLEEC().clear();

		Iterator curricularCourseGroupsIterator = getCurricularCourseGroupsIterator();
		while (curricularCourseGroupsIterator.hasNext()) {
			ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) curricularCourseGroupsIterator.next();
			curricularCourseGroupsIterator.remove();
			curricularCourseGroup.setBranch(commonBranch);
		}

		Iterator optionalCurricularCourseGroupsIterator = getOptionalCurricularCourseGroupsIterator();
		while (optionalCurricularCourseGroupsIterator.hasNext()) {
			IOptionalCurricularCourseGroup curricularCourseGroup = (IOptionalCurricularCourseGroup) optionalCurricularCourseGroupsIterator.next();
			optionalCurricularCourseGroupsIterator.remove();
			curricularCourseGroup.setBranchForWhichThisIsOptionalCurricularCourseGroup(commonBranch);
		}
		
		Iterator areaCurricularCourseGroupsIterator = getAreaCurricularCourseGroupsIterator();
		while (areaCurricularCourseGroupsIterator.hasNext()) {
			IAreaCurricularCourseGroup curricularCourseGroup = (IAreaCurricularCourseGroup) areaCurricularCourseGroupsIterator.next();
			areaCurricularCourseGroupsIterator.remove();
			curricularCourseGroup.setBranchForWhichThisIsAreaCurricularCourseGroup(commonBranch);
		}
		
		removeCurricularCourseScopes(commonBranch);
		
		removeDegreeCurricularPlan();
		
		deleteDomainObject();
	}
	
	private void removeCurricularCourseScopes(final IBranch commonBranch) throws DomainException {
		Iterator branchCurricularCourseScopesIterator = getScopesIterator();
		while (branchCurricularCourseScopesIterator.hasNext()) {
			ICurricularCourseScope scope = (ICurricularCourseScope)branchCurricularCourseScopesIterator.next();
			ICurricularCourse curricularCourse = scope.getCurricularCourse();
	
			// if CurricularCourse already has a common Branch
			if (hasCurricularCourseCommonBranchInAnyCurricularCourseScope(curricularCourse,commonBranch)) {
				// delete the CurricularCourseScope
				branchCurricularCourseScopesIterator.remove();
				scope.removeBranch();
				scope.delete();
				
			} else {
				// set the Branch in the CurricularCourseScope to commonBranch
				branchCurricularCourseScopesIterator.remove();
				scope.setBranch(commonBranch);
			}
		}		
	}

	private IBranch findCommonBranchForSameDegreeCurricularPlan() {
		for (IBranch branch : getDegreeCurricularPlan().getAreas()) {
			if (branch.representsCommonBranch() && branch.getName().equals("")) {
				return branch;
			}
		}
		return null;
	}
	
	private Boolean hasCurricularCourseCommonBranchInAnyCurricularCourseScope (ICurricularCourse curricularCourse, final IBranch commonBranch) {
		return ((ICurricularCourseScope) CollectionUtils.find(curricularCourse.getScopes(),new Predicate() {
			public boolean evaluate(Object o) {
				ICurricularCourseScope ccs = (ICurricularCourseScope) o;
				return ccs.getBranch().equals(commonBranch);
			}}) != null);
	}
}
