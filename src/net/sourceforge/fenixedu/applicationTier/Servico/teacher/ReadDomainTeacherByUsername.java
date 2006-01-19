/**
* Dec 13, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadDomainTeacherByUsername extends Service {

    public Teacher run(final String username) throws ExcepcaoPersistencia{
        
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<Role> roles = persistentSupport.getIPersistentRole().readAll();
        Role teacherRole = (Role) CollectionUtils.find(roles,new Predicate(){
            public boolean evaluate(Object object) {
                Role role = (Role) object;
                return role.getRoleType().equals(RoleType.TEACHER);
            }});
        
        Person person = (Person) CollectionUtils.find(teacherRole.getAssociatedPersons(), new Predicate(){
            public boolean evaluate(Object object) {
                Person tempPerson = (Person) object;
                return username.equals(tempPerson.getUsername());
            }});
        return person.getTeacher();
    }
}


