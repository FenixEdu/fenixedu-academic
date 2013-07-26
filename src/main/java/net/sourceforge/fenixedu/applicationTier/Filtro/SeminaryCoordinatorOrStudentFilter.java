/*
 * Created on 8/Set/2003, 14:55:43
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 8/Set/2003, 14:55:43
 * 
 */
public class SeminaryCoordinatorOrStudentFilter {

    public static final SeminaryCoordinatorOrStudentFilter instance = new SeminaryCoordinatorOrStudentFilter();

    public SeminaryCoordinatorOrStudentFilter() {
    }

    public void execute() throws NotAuthorizedException {
        User id = Authenticate.getUser();

        if (((id != null && id.getPerson().getPersonRolesSet() != null && !id.getPerson().hasRole(getRoleType1()) && !id
                .getPerson().hasRole(getRoleType2()))) || (id == null) || (id.getPerson().getPersonRolesSet() == null)) {
            throw new NotAuthorizedException();
        }
    }

    protected RoleType getRoleType1() {
        return RoleType.STUDENT;
    }

    protected RoleType getRoleType2() {
        return RoleType.SEMINARIES_COORDINATOR;
    }

}