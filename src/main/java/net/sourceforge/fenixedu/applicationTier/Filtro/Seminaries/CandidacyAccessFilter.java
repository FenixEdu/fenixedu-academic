/*
 * Created on 26/Ago/2003, 13:35:57
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.Seminaries;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

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
        User id = Authenticate.getUser();

        if ((!this.checkCandidacyOwnership(id, candidacyID)) && (!this.checkCoordinatorRole(id))) {
            throw new NotAuthorizedException();
        }

    }

    boolean checkCoordinatorRole(User id) {
        boolean result = true;
        // Collection roles = id.getRoles();
        // Iterator iter = roles.iterator();
        // while (iter.hasNext())
        // {
        // InfoRole role = (InfoRole) iter.next();
        // }
        if (((id != null && id.getPerson().getPersonRolesSet() != null && !id.getPerson().hasRole(getRoleType())))
                || (id == null) || (id.getPerson().getPersonRolesSet() == null)) {
            result = false;
        }
        return result;
    }

    boolean checkCandidacyOwnership(User id, String candidacyID) {
        boolean result = true;

        Registration registration = Registration.readByUsername(id.getUsername());
        if (registration != null) {
            SeminaryCandidacy candidacy = FenixFramework.getDomainObject(candidacyID);
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