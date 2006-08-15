/*
 * Created on 2003/09/06
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.advisoriesManagement;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisoryEditor;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.AdvisoryRecipients;

/**
 * @author Luis Cruz
 */
public class CreateAdvisory extends Service {

    private boolean hasRole(final Person person, final RoleType roleType) {
        final Collection roles = person.getPersonRoles();
        for (final Iterator iterator = roles.iterator(); iterator.hasNext();) {
            final Role role = (Role) iterator.next();
            if (roleType.equals(role.getRoleType())) {
                return true;
            }
        }
        return false;
    }

    public Boolean run(InfoAdvisoryEditor infoAdvisory, AdvisoryRecipients advisoryRecipients)
            throws ExcepcaoPersistencia {
        final Advisory advisory = new Advisory();
        advisory.setCreated(infoAdvisory.getCreated());
        advisory.setSubject(infoAdvisory.getSubject());
        advisory.setExpires(infoAdvisory.getExpires());
        advisory.setMessage(infoAdvisory.getMessage());
        advisory.setSender(infoAdvisory.getSender());

        Role role = Role.getRoleByRoleType(RoleType.PERSON);
        List<Person> people = role.getAssociatedPersons();

        for (Person person : people) {
            if ((advisoryRecipients.equals(AdvisoryRecipients.STUDENTS) && person
                    .hasRole(RoleType.STUDENT))
                    || (advisoryRecipients.equals(AdvisoryRecipients.TEACHERS) && person
                            .hasRole(RoleType.TEACHER))
                    || (advisoryRecipients.equals(AdvisoryRecipients.EMPLOYEES) && person
                            .hasRole(RoleType.EMPLOYEE))) {

                person.addAdvisories(advisory);
            }
        }

        return Boolean.TRUE;
    }

}
