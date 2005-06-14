/*
 * Created on 2003/09/06
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAdvisory;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.AdvisoryRecipients;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class CreateAdvisory implements IService {

    private boolean hasRole(final IPerson person, final RoleType roleType) {
        final Collection roles = person.getPersonRoles();
        for (final Iterator iterator = roles.iterator(); iterator.hasNext();) {
            final IRole role = (IRole) iterator.next();
            if (roleType.equals(role.getRoleType())) {
                return true;
            }
        }
        return false;
    }

    public Boolean run(InfoAdvisory infoAdvisory, AdvisoryRecipients advisoryRecipients)
            throws ExcepcaoPersistencia {
        final ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentAdvisory persistentAdvisory = persistenceSupport.getIPersistentAdvisory();
        final IPessoaPersistente persistentPerson = persistenceSupport.getIPessoaPersistente();

        final IAdvisory advisory = Cloner.copyInfoAdvisory2IAdvisory(infoAdvisory);
        persistentAdvisory.simpleLockWrite(advisory);

        final Collection people = persistentPerson.readAll(Person.class);
        for (final Iterator iterator = people.iterator(); iterator.hasNext();) {
            final IPerson person = (IPerson) iterator.next();

            if ((advisoryRecipients.equals(AdvisoryRecipients.STUDENTS) && hasRole(person,
                    RoleType.STUDENT))
                    || (advisoryRecipients.equals(AdvisoryRecipients.TEACHERS) && hasRole(person,
                            RoleType.TEACHER))
                    || (advisoryRecipients.equals(AdvisoryRecipients.EMPLOYEES) && hasRole(person,
                            RoleType.EMPLOYEE))) {
                persistentPerson.simpleLockWrite(person);

                person.getAdvisories().add(advisory);
                advisory.getPeople().add(person);
            }
        }

        return new Boolean(true);
    }

}