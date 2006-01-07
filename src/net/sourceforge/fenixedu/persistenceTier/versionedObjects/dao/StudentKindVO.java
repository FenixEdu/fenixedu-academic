package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.StudentKind;
import net.sourceforge.fenixedu.domain.StudentKind;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentKind;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.StudentType;

public class StudentKindVO extends VersionedObjectsBase implements IPersistentStudentKind {

	
    public StudentKind readByStudentType(StudentType studentType) throws ExcepcaoPersistencia {

		Collection<StudentKind> studentKinds = readAll(StudentKind.class);
		
		for (StudentKind studentKind : studentKinds){
			if (studentKind.getStudentType().equals(studentType))
				return studentKind;
		}
		return null;
    }
}
