package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;

abstract public class AccessControlFilter extends Filter {

	/**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection<RoleType> getNeededRoleTypes() {
        return null;
    }

    protected boolean containsRoleType(Collection<RoleType> roleTypes) {
        final Collection<RoleType> neededRoleTypes = getNeededRoleTypes();
        if (neededRoleTypes == null || neededRoleTypes.isEmpty()) {
            return true;
        }
        if (roleTypes != null) {
            for (final RoleType roleType : roleTypes) {
                if (neededRoleTypes.contains(roleType)) {
                    return true;
                }
            }
        }
        return false;
    }

    abstract public void execute(ServiceRequest request, ServiceResponse response) throws Exception;
    
    public void execute(ServiceRequest request, ServiceResponse response, FilterParameters filterParameters) throws Exception {
        this.execute(request, response);
    }

}