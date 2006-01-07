/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class UpdateMembersGroup implements IService {

    public void run(GroupUnion membersGroup,
            Integer[] personIDsToAdd, Integer[] personGroupsIDsToRemove, RoleType roleTypeToUpdate) throws ExcepcaoPersistencia {

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Person creator = AccessControl.getUserView().getPerson();

        Role roleToUpdate = null;
        if (roleTypeToUpdate != null) {
            roleToUpdate = ps.getIPersistentRole().readByRoleType(roleTypeToUpdate);
        }
        
        if (personIDsToAdd != null) {
            for (Integer personID : personIDsToAdd) {
                Person person = (Person) ps.getIPersistentObject().readByOID(Person.class, personID);
                if (!membersGroup.isMember(person)) {
                    DomainFactory.makePersonGroup(creator, person, membersGroup);
                }
                if (roleToUpdate != null && !person.hasRole(roleTypeToUpdate)) {
                    person.addPersonRoles(roleToUpdate);
                }
            }
        }

        if (personGroupsIDsToRemove != null) {
            for (Integer personGroupID : personGroupsIDsToRemove) {
                PersonGroup personGroup = (PersonGroup) ps.getIPersistentObject().readByOID(PersonGroup.class,
                        personGroupID);

                if (roleToUpdate != null) {
                    if (personGroup.getPerson().hasRole(roleTypeToUpdate)) {
                        personGroup.getPerson().removePersonRoles(roleToUpdate);    
                    }
                }
                
                membersGroup.removePart(personGroup);
                
            }
        }

    }

}
