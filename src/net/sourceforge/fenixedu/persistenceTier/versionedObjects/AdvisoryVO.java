package net.sourceforge.fenixedu.persistenceTier.versionedObjects;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAdvisory;
import net.sourceforge.fenixedu.util.AdvisoryRecipients;
import net.sourceforge.fenixedu.util.StringAppender;

/**
 * 
 * @author Luis Cruz
 */
public class AdvisoryVO extends VersionedObjectsBase implements IPersistentAdvisory {

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

    public void write(final IAdvisory advisory, final IPerson person) {
        simpleLockWrite(advisory);
        simpleLockWrite(person);

        person.getAdvisories().add(advisory);
        advisory.getPeople().add(person);
    }

    public void write(final IAdvisory advisory, final List group) {
        for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
            final IPerson person = (IPerson) iterator.next();
            write(advisory, person);
        }
    }

    public void write(final IAdvisory advisory, final AdvisoryRecipients advisoryRecipients)
            throws ExcepcaoPersistencia {

        final Collection people = readAll(Person.class);
        for (final Iterator iterator = people.iterator(); iterator.hasNext();) {
            final IPerson person = (IPerson) iterator.next();

            if ((advisoryRecipients.equals(AdvisoryRecipients.STUDENTS) && hasRole(person,
                    RoleType.STUDENT))
                    || (advisoryRecipients.equals(AdvisoryRecipients.TEACHERS) && hasRole(person,
                            RoleType.TEACHER))
                    || (advisoryRecipients.equals(AdvisoryRecipients.EMPLOYEES) && hasRole(person,
                            RoleType.EMPLOYEE))) {
                write(advisory, person);
            }
        }
    }

    public void deleteByOID(final Class classToQuery, final Integer oid) throws ExcepcaoPersistencia {
        if (!classToQuery.equals(Advisory.class)) {
            throw new IllegalArgumentException(StringAppender.append(
                    "Attempted to delete an object of class ", classToQuery.getName(), " with the ",
                    AdvisoryVO.class.getName() + " DAO."));
        }

        final IAdvisory advisory = (IAdvisory) readByOID(classToQuery, oid);
        final List people = advisory.getPeople();
        for (final Iterator iterator = people.iterator(); iterator.hasNext();) {
            final IPerson person = (IPerson) iterator.next();
            person.getAdvisories().remove(advisory);
        }

        people.clear();
    }

}