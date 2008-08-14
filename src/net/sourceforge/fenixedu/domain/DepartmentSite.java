package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DepartmentSite extends DepartmentSite_Base {

    public DepartmentSite(DepartmentUnit unit) {
	super();

	if (unit.hasSite()) {
	    throw new DomainException("site.department.unit.already.has.site");
	}

	setUnit(unit);
    }

    public DepartmentSite(Department department) {
	this(department.getDepartmentUnit());
    }

    public Department getDepartment() {
	return getUnit().getDepartment();
    }

    @Override
    public IGroup getOwner() {
	return new GroupUnion(new RoleGroup(Role.getRoleByRoleType(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)),
		new FixedSetGroup(getManagers()));
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
	List<IGroup> groups = super.getContextualPermissionGroups();

	groups.add(new DepartmentEmployeesGroup(getDepartment()));

	return groups;
    }

    /**
     * The department already has the an internacionalized name.
     * 
     * @see Department#getNameI18n()
     */
    @Override
    public MultiLanguageString getUnitNameWithAcronym() {
	return getDepartment().getNameI18n();
    }

    @Override
    public MultiLanguageString getName() {
	return MultiLanguageString.i18n().add("pt", getUnit().getAcronym()).finish();
    }

    @Override
    public void setNormalizedName(final MultiLanguageString normalizedName) {
	// unable to optimize because we cannot track changes to name correctly.
	// don't call super.setNormalizedName() !
    }

}
