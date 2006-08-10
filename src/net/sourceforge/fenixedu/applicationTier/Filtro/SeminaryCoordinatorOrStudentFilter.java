/*
 * Created on 8/Set/2003, 14:55:43
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 8/Set/2003, 14:55:43
 *  
 */
public class SeminaryCoordinatorOrStudentFilter extends Filtro {

    public SeminaryCoordinatorOrStudentFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);

        if (((id != null && id.getRoleTypes() != null
                && !id.hasRoleType(getRoleType1()) && !id.hasRoleType(getRoleType2())))
                || (id == null) || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }

    protected RoleType getRoleType1() {
        return RoleType.STUDENT;
    }

    protected RoleType getRoleType2() {
        return RoleType.SEMINARIES_COORDINATOR;
    }

}