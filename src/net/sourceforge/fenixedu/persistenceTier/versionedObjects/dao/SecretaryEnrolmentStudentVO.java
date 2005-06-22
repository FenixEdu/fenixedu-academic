package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ISecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.domain.SecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class SecretaryEnrolmentStudentVO extends VersionedObjectsBase implements IPersistentSecretaryEnrolmentStudent {
	
    public ISecretaryEnrolmentStudent readByStudentNumber(Integer studentNumber)
		throws ExcepcaoPersistencia {

		Collection<ISecretaryEnrolmentStudent> secretaryEnrolmentStudents = readAll(SecretaryEnrolmentStudent.class);
		
		for (ISecretaryEnrolmentStudent secretaryEnrolmentStudent : secretaryEnrolmentStudents) {
			if (secretaryEnrolmentStudent.getStudent() == null)
				System.out.println("Ay caramba!");
			
			if (secretaryEnrolmentStudent.getStudent().getNumber().equals(studentNumber))
				return secretaryEnrolmentStudent;
		}
		return null;
	}
}
