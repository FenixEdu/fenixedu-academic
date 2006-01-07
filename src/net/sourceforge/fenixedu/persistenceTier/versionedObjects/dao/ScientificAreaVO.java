package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.ScientificArea;
import net.sourceforge.fenixedu.domain.ScientificArea;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentScientificArea;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class ScientificAreaVO extends VersionedObjectsBase implements IPersistentScientificArea	{

	
	public ScientificArea readByName(String name) throws ExcepcaoPersistencia {
		Collection scientificAreas = readAll(ScientificArea.class);
		
		for (Iterator iterator = scientificAreas.iterator(); iterator.hasNext() ;){
			ScientificArea scientificArea = (ScientificArea)iterator.next();
			if (scientificArea.getName().equals(name))
				return scientificArea;
		}
		return null;
			
	}
}
