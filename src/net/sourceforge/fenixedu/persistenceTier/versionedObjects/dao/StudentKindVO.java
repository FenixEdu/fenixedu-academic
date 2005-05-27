package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.IStudentKind;
import net.sourceforge.fenixedu.domain.StudentKind;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentKind;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.StudentType;

public class StudentKindVO extends VersionedObjectsBase implements IPersistentStudentKind {

	
    public IStudentKind readByStudentType(StudentType studentType) throws ExcepcaoPersistencia {

		Collection<IStudentKind> studentKinds = readAll(StudentKind.class);
		
		for (IStudentKind studentKind : studentKinds){
			if (studentKind.getStudentType().equals(studentType))
				return studentKind;
		}
		return null;
    }
}
