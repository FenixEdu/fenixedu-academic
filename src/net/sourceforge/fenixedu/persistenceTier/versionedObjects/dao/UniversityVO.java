package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.University;
import net.sourceforge.fenixedu.domain.University;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentUniversity;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class UniversityVO extends VersionedObjectsBase implements IPersistentUniversity {

	
    public University readByNameAndCode(String name, String code) throws ExcepcaoPersistencia {
			
		Collection<University> universitys = readAll(University.class);
		
		for (University university : universitys){
			if(university.getName().equals(name) && university.getCode().equals(code))
				return university;
		}
		
		return null;
    }
}
