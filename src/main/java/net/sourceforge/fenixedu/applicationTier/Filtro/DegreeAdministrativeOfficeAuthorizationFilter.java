package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author David Santos
 * 
 */

public class DegreeAdministrativeOfficeAuthorizationFilter extends AuthorizationByRoleFilter {

    public final static DegreeAdministrativeOfficeAuthorizationFilter instance =
            new DegreeAdministrativeOfficeAuthorizationFilter();

    /**
     * The singleton access method of this class.
     * 
     * @return Returns the instance of this class responsible for the
     *         authorization access to services.
     */
    public static Filtro getInstance() {
        return instance;
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.DEGREE_ADMINISTRATIVE_OFFICE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk
     * .ServiceRequest, pt.utl.ist.berserk.ServiceResponse)
     */
    @Override
    public void execute(Object[] parameters) throws FilterException, Exception {
        IUserView userView = AccessControl.getUserView();
        if (!userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
            super.execute(parameters);
        }

    }

}