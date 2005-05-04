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

    public void write(final IAdvisory advisory, final List group) throws ExcepcaoPersistencia {
        simpleLockWrite(advisory);

        for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
            final IPerson person = (IPerson) iterator.next();
            simpleLockWrite(person);

            person.getAdvisories().add(advisory);
            advisory.getPeople().add(person);
        }
    }

}