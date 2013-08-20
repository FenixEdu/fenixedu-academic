/*
 * Created on 26/Ago/2003, 13:35:57
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.Seminaries;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 26/Ago/2003, 13:35:57
 * 
 */
public class CandidacyAccessFilter {

    public static final CandidacyAccessFilter instance = new CandidacyAccessFilter();

    public CandidacyAccessFilter() {
    }

    public void execute(String candidacyID) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();

        if ((!this.checkCandidacyOwnership(id, candidacyID)) && (!this.checkCoordinatorRole(id))) {
            throw new NotAuthorizedException();
        }

    }

    boolean checkCoordinatorRole(IUserView id) {
        boolean result = true;
        // Collection roles = id.getRoles();
        // Iterator iter = roles.iterator();
        // while (iter.hasNext())
        // {
        // InfoRole role = (InfoRole) iter.next();
        // }
        if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType()))) || (id == null)
                || (id.getRoleTypes() == null)) {
            result = false;
        }
        return result;
    }

    boolean checkCandidacyOwnership(IUserView id, String candidacyID) {
        boolean result = true;

        Registration registration = Registration.readByUsername(id.getUtilizador());
        if (registration != null) {
            SeminaryCandidacy candidacy = AbstractDomainObject.fromExternalId(candidacyID);
            //
            if ((candidacy != null) && !(candidacy.getStudent().getExternalId().equals(registration.getExternalId()))) {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }

    private RoleType getRoleType() {
        return RoleType.SEMINARIES_COORDINATOR;
    }
}