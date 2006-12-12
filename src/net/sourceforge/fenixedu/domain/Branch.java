package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
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

    public Branch() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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

    public List getAreaCurricularCourseGroups(final AreaType areaType) {

        return (List) CollectionUtils.select(getCurricularCourseGroups(), new Predicate() {

            public boolean evaluate(Object arg0) {
                CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) arg0;
                return curricularCourseGroup.getAreaType().equals(areaType);
            }
        });
    }

    private Boolean canDeleteAllEligibleCurricularCourseScopes(final Branch commonBranch) {
        Iterator branchCurricularCourseScopesIterator = getScopesIterator();
        while (branchCurricularCourseScopesIterator.hasNext()) {
			CurricularCourseScope scope = (CurricularCourseScope)branchCurricularCourseScopesIterator.next();
            CurricularCourse curricularCourse = scope.getCurricularCourse();

            // if CurricularCourse already has a common Branch
            if (hasCurricularCourseCommonBranchInAnyCurricularCourseScope(curricularCourse, commonBranch)) {
                // we want to delete this CurricularCourseScope

                if (!scope.canBeDeleted())
                    return false;
            }
        }
        return true;
    }

    public Boolean canBeDeleted() {
        if (this.hasAnyAssociatedFinalDegreeWorkProposals())
            throw new DomainException("error.branch.cant.delete");

		if (this.representsCommonBranch() && 
				(this.hasAnyCurricularCourseGroups() || this.hasAnyScopes()))
		    throw new DomainException("error.branch.cant.delete");

        Branch commonBranch = findCommonBranchForSameDegreeCurricularPlan();
//        if (commonBranch == null)
//            throw new DomainException("error.branch.cant.delete");

        if (!canDeleteAllEligibleCurricularCourseScopes(commonBranch))
            throw new DomainException("error.branch.cant.delete");

        return true;
    }

    public void delete() throws DomainException {

        if (!this.canBeDeleted())
            throw new DomainException("error.branch.cant.delete");

        Branch commonBranch = findCommonBranchForSameDegreeCurricularPlan();

        this.getStudentCurricularPlans().clear();
        this.getSecundaryStudentCurricularPlansLEIC().clear();
        this.getSecundaryStudentCurricularPlansLEEC().clear();

        Iterator curricularCourseGroupsIterator = getCurricularCourseGroupsIterator();
        while (curricularCourseGroupsIterator.hasNext()) {
			CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) curricularCourseGroupsIterator.next();
            curricularCourseGroupsIterator.remove();
            curricularCourseGroup.setBranch(commonBranch);
        }

        removeCurricularCourseScopes(commonBranch);

        removeDegreeCurricularPlan();

        removeRootDomainObject();
        deleteDomainObject();
    }

    private void removeCurricularCourseScopes(final Branch commonBranch) throws DomainException {
        Iterator branchCurricularCourseScopesIterator = getScopesIterator();
        while (branchCurricularCourseScopesIterator.hasNext()) {
			CurricularCourseScope scope = (CurricularCourseScope)branchCurricularCourseScopesIterator.next();
            CurricularCourse curricularCourse = scope.getCurricularCourse();

            // if CurricularCourse already has a common Branch
            if (hasCurricularCourseCommonBranchInAnyCurricularCourseScope(curricularCourse, commonBranch)) {
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

    private Branch findCommonBranchForSameDegreeCurricularPlan() {
        for (Branch branch : getDegreeCurricularPlan().getAreas()) {
            if (branch.representsCommonBranch() && branch.getName().equals("")) {
                return branch;
            }
        }
        return null;
    }

	private Boolean hasCurricularCourseCommonBranchInAnyCurricularCourseScope (CurricularCourse curricularCourse, final Branch commonBranch) {
		return ((CurricularCourseScope) CollectionUtils.find(curricularCourse.getScopes(),new Predicate() {
                    public boolean evaluate(Object o) {
                        CurricularCourseScope ccs = (CurricularCourseScope) o;
                        return ccs.getBranch().equals(commonBranch);
			}}) != null);
                    }

    public List<CurricularCourseGroup> readCurricularCourseGroupsByAreaType(AreaType areaType) {
        List<CurricularCourseGroup> result = new ArrayList<CurricularCourseGroup>();
        for (CurricularCourseGroup curricularCourseGroup : this.getCurricularCourseGroups()) {
			if((curricularCourseGroup instanceof AreaCurricularCourseGroup) && curricularCourseGroup.getAreaType().equals(areaType)) {
                result.add(curricularCourseGroup);
            }
        }
        return result;
    }

	public CurricularCourseGroup readCurricularCourseGroupByCurricularCourseAndAreaType(CurricularCourse curricularCourse, AreaType areaType){
        List<CurricularCourseGroup> areaCurricularCourseGroups = readCurricularCourseGroupsByAreaType(areaType);
        for (CurricularCourseGroup group : areaCurricularCourseGroups) {
            for (CurricularCourse course : group.getCurricularCourses()) {
                if (course.equals(curricularCourse)) {
                    return group;
                }
            }
        }
        return null;
    }

	public CurricularCourseGroup readCurricularCourseGroupByScientificAreaAndAreaType(ScientificArea scientificArea, AreaType areaType){
        List<CurricularCourseGroup> areaCurricularCourseGroups = readCurricularCourseGroupsByAreaType(areaType);
        for (CurricularCourseGroup group : areaCurricularCourseGroups) {
            for (ScientificArea area : group.getScientificAreas()) {
                if (area.equals(scientificArea)) {
                    return group;
                }
            }
        }
        return null;
    }

    public List<CurricularCourseGroup> getOptionalCurricularCourseGroups() {
        List<CurricularCourseGroup> result = new ArrayList<CurricularCourseGroup>();
        for (CurricularCourseGroup curricularCourseGroup : this.getCurricularCourseGroups()) {
            if (curricularCourseGroup instanceof OptionalCurricularCourseGroup) {
                result.add(curricularCourseGroup);
            }
        }
        return result;
    }

	public CurricularCourseGroup getOptionalCurricularCourseGroupByName(final String name) {
	    for (CurricularCourseGroup curricularCourseGroup : this.getCurricularCourseGroups()) {
		if(( curricularCourseGroup instanceof OptionalCurricularCourseGroup ) && curricularCourseGroup.getName().equals(name)) {
		    return curricularCourseGroup;
		}
	    }
	    return null;
	}
    	
 	// Static methods
	public static Branch readByBranchType(final BranchType branchType) {
        for (final Branch branch : RootDomainObject.getInstance().getBranchsSet()) {
            if (branch.getBranchType() == branchType) {
                return branch;
            }
        }
        return null;
    }
    
}