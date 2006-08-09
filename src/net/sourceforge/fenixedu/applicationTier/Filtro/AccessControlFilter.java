package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Role;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;

abstract public class AccessControlFilter extends Filter {

	/**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection<Role> getNeededRoles() {
        return null;
    }

    /**
     * @param collection
     * @return boolean
     */
    protected boolean containsRole(Collection<Role> roles) {
    	final Collection<Role> neededRoles = getNeededRoles();
    	if (neededRoles == null || neededRoles.isEmpty()) {
    		return true;
    	}
    	if (roles != null) {
    		for (final Role role : roles) {
    			if (neededRoles.contains(role)) {
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