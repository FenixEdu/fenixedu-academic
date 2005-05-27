package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.IUniversity;
import net.sourceforge.fenixedu.domain.University;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentUniversity;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class UniversityVO extends VersionedObjectsBase implements IPersistentUniversity {

	
    public IUniversity readByNameAndCode(String name, String code) throws ExcepcaoPersistencia {
			
		Collection<IUniversity> universitys = readAll(University.class);
		
		for (IUniversity university : universitys){
			if(university.getName().equals(name) && university.getCode().equals(code))
				return university;
		}
		
		return null;
    }
}
