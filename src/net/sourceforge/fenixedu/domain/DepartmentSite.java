package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class DepartmentSite extends DepartmentSite_Base {
    
    public DepartmentSite(Department department) {
        super();
        
        Unit unit = department.getDepartmentUnit();
        if (unit.hasSite()) {
            throw new DomainException("site.department.unit.already.has.site");
        }
        
        setUnit(department.getDepartmentUnit());
    }

    public Department getDepartment() {
        return getUnit().getDepartment();
    }
    
    @Override
    public IGroup getOwner() {
        return new RoleGroup(Role.getRoleByRoleType(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE));
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> groups = super.getContextualPermissionGroups();
        
        groups.add(new DepartmentEmployeesGroup(getDepartment()));
        
        return groups;
    }
    
}
