/*
 * Created on 2003/09/06
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.advisoriesManagement;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisory;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.AdvisoryRecipients;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class CreateAdvisory implements IService {

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

    public Boolean run(InfoAdvisory infoAdvisory, AdvisoryRecipients advisoryRecipients)
            throws ExcepcaoPersistencia {
        
        final ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();       

        final Advisory advisory = DomainFactory.makeAdvisory();
        advisory.setCreated(infoAdvisory.getCreated());
        advisory.setSubject(infoAdvisory.getSubject());
        advisory.setExpires(infoAdvisory.getExpires());
        advisory.setMessage(infoAdvisory.getMessage());
        advisory.setSender(infoAdvisory.getSender());
     
        Role role = persistenceSupport.getIPersistentRole().readByRoleType(RoleType.PERSON);
        List<Person> people = role.getAssociatedPersons();
              
        for (Person person : people) {
            if ((advisoryRecipients.equals(AdvisoryRecipients.STUDENTS) && hasRole(person,
                    RoleType.STUDENT))
                    || (advisoryRecipients.equals(AdvisoryRecipients.TEACHERS) && hasRole(person,
                            RoleType.TEACHER))
                    || (advisoryRecipients.equals(AdvisoryRecipients.EMPLOYEES) && hasRole(person,
                            RoleType.EMPLOYEE))) {
          
                person.getAdvisories().add(advisory);
                advisory.getPeople().add(person);
            }
        }
        
        return Boolean.TRUE;
    }

}
