package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;
import pt.utl.ist.berserk.logic.filterManager.IFilter;

abstract public class Filtro extends AccessControlFilter {

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

    abstract public void execute(Object[] parameters) throws Exception;

    @Override
    public void execute(ServiceRequest request, ServiceResponse response, FilterParameters parameters) throws Exception {
        execute(request.getServiceParameters().parametersArray());
    }

}
