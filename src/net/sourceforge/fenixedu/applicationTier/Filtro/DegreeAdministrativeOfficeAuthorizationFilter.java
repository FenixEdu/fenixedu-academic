package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author David Santos
 *  
 */

public class DegreeAdministrativeOfficeAuthorizationFilter extends AuthorizationByRoleFilter {

    public final static DegreeAdministrativeOfficeAuthorizationFilter instance = new DegreeAdministrativeOfficeAuthorizationFilter();

    /**
     * The singleton access method of this class.
     * 
     * @return Returns the instance of this class responsible for the
     *         authorization access to services.
     */
    public static Filtro getInstance() {
        return instance;
    }

    protected RoleType getRoleType() {
        return RoleType.DEGREE_ADMINISTRATIVE_OFFICE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {
        IUserView userView = (IUserView) request.getRequester();
        if (!userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
            super.execute(request, response);
        }

    }

}