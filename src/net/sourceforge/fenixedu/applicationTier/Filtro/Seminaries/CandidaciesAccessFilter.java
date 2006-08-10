/*
 * Created on 5/Set/2003, 16:22:14
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.Seminaries;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 5/Set/2003, 16:22:14
 *  
 */
public class CandidaciesAccessFilter extends Filtro {
    public CandidaciesAccessFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType())))
                || (id == null) || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedException();
        }

    }

    private RoleType getRoleType() {
        return RoleType.SEMINARIES_COORDINATOR;
    }
}