package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAdvisory;
import net.sourceforge.fenixedu.util.AdvisoryRecipients;

import org.apache.ojb.broker.query.Criteria;

/**
 * Created on 2003/09/06
 * 
 * @author Luis Cruz Package ServidorPersistente.OJB
 */
public class AdvisoryOJB extends PersistentObjectOJB implements IPersistentAdvisory {

    /**
     * Constructor for ExecutionYearOJB.
     */
    public AdvisoryOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentAdvisory#write(Dominio.IAdvisory,
     *      Util.AdvisoryRecipients)
     */
    public void write(IAdvisory advisory, AdvisoryRecipients advisoryRecipients)
            throws ExcepcaoPersistencia {
        simpleLockWrite(advisory);

        Criteria criteria = new Criteria();
        if (advisoryRecipients.equals(AdvisoryRecipients.STUDENTS)) {
            criteria.addEqualTo("personRoles.roleType", RoleType.STUDENT);
        }
        if (advisoryRecipients.equals(AdvisoryRecipients.TEACHERS)) {
            criteria.addEqualTo("personRoles.roleType", RoleType.TEACHER);
        }
        if (advisoryRecipients.equals(AdvisoryRecipients.EMPLOYEES)) {
            criteria.addEqualTo("personRoles.roleType", RoleType.EMPLOYEE);
            criteria.addNotEqualTo("personRoles.roleType", RoleType.TEACHER);
        }

        int numberOfRecipients = count(Person.class, criteria);
        int numberOfElementsInSpan = 500;
        for (int i = 0; ((i - 1) * numberOfElementsInSpan) < numberOfRecipients; i++) {
            List people = readSpan(Person.class, criteria, new Integer(numberOfElementsInSpan),
                    new Integer(i));

            if (!people.isEmpty()) {
                for (int j = 0; j < people.size(); j++) {
                    IPerson person = (IPerson) people.get(j);
                    write(advisory, person);
                }
            }
        }
    }

    public void write(IAdvisory advisory, List group) throws ExcepcaoPersistencia {
        simpleLockWrite(advisory);

        Iterator it = group.iterator();
        while (it.hasNext()) {
            IPerson person = (IPerson) it.next();
            simpleLockWrite(person);
            person.getAdvisories().add(advisory);
        }
    }

    public void write(IAdvisory advisory, IPerson person) throws ExcepcaoPersistencia {
        simpleLockWrite(person);
        person.getAdvisories().add(advisory);
    }

}