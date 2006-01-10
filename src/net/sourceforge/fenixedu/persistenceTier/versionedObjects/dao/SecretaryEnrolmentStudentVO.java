package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.SecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class SecretaryEnrolmentStudentVO extends VersionedObjectsBase implements IPersistentSecretaryEnrolmentStudent {
	
    public SecretaryEnrolmentStudent readByStudentNumber(Integer studentNumber)
		throws ExcepcaoPersistencia {

		Collection<SecretaryEnrolmentStudent> secretaryEnrolmentStudents = readAll(SecretaryEnrolmentStudent.class);
		
		for (SecretaryEnrolmentStudent secretaryEnrolmentStudent : secretaryEnrolmentStudents) {
			if (secretaryEnrolmentStudent.getStudent() == null)
				System.out.println("Ay caramba!");
			
			if (secretaryEnrolmentStudent.getStudent().getNumber().equals(studentNumber))
				return secretaryEnrolmentStudent;
		}
		return null;
	}
}
