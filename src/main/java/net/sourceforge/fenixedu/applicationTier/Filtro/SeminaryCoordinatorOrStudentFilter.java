/*
 * Created on 8/Set/2003, 14:55:43
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

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
        IUserView id = AccessControl.getUserView();

        if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType1()) && !id.hasRoleType(getRoleType2())))
                || (id == null) || (id.getRoleTypes() == null)) {
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