package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;

abstract public class AccessControlFilter extends Filter {

	/**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection getNeededRoles() {
        return new ArrayList();
    }

    /**
     * @param collection
     * @return boolean
     */
    protected boolean containsRole(Collection roles) {
        CollectionUtils.intersection(roles, getNeededRoles());
        return roles.size() != 0;
    }

    abstract public void execute(ServiceRequest request, ServiceResponse response) throws Exception;
    
    public void execute(ServiceRequest request, ServiceResponse response, FilterParameters filterParameters) throws Exception {
        this.execute(request, response);
    }

}