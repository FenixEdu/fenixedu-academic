package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.logic.filterManager.IFilter;

//defines a type of filters
abstract public class AccessControlFilter implements IFilter
{
    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection getNeededRoles() {
        List roles = new ArrayList();      

        return roles;
    }

    /**
     * @param collection
     * @return boolean
     */
    protected boolean containsRole(Collection roles) {
        CollectionUtils.intersection(roles, getNeededRoles());
    
        if (roles.size() != 0) {
            return true;
        }
        return false;
    
    }
}
