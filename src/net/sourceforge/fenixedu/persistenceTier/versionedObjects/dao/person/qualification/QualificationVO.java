/*
 * Created on 11/Set/2005 - 15:30:32
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.person.qualification;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQualification;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class QualificationVO extends VersionedObjectsBase implements
		IPersistentQualification {

	public List readQualificationsByPersonId(Integer personId)
			throws ExcepcaoPersistencia {
		
		Person person = (Person) readByOID(Person.class, personId);	
		List<Qualification> res = person.getAssociatedQualifications();
		return res;
		
	}

}
