/*
 * Created on 5/Set/2003, 16:22:14
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.Seminaries;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 5/Set/2003, 16:22:14
 * 
 */
public class CandidaciesAccessFilter {

    public static final CandidaciesAccessFilter instance = new CandidaciesAccessFilter();

    public CandidaciesAccessFilter() {
    }

    public void execute() throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType()))) || (id == null)
                || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedException();
        }

    }

    private RoleType getRoleType() {
        return RoleType.SEMINARIES_COORDINATOR;
    }
}