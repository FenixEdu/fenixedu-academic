package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accessControl.UnitGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

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
    public Group getOwner() {
        return RoleGroup.get(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE).or(UserGroup.of(Person.convertToUsers(getManagers())));
    }

    @Override
    public List<Group> getContextualPermissionGroups() {
        List<Group> groups = super.getContextualPermissionGroups();

        groups.add(UnitGroup.recursiveWorkers(getDepartment().getDepartmentUnit()));

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
        return new MultiLanguageString().with(MultiLanguageString.pt, getUnit().getAcronym());
    }

    @Override
    public String getReversePath() {
        return super.getReversePath() + "/" + getUnit().getAcronym().toLowerCase();
    }

}
