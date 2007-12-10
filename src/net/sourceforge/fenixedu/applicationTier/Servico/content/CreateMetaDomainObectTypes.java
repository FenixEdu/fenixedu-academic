package net.sourceforge.fenixedu.applicationTier.Servico.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.MetaDomainObject;
import dml.DomainClass;

public class CreateMetaDomainObectTypes extends Service {

    public void run() throws FenixServiceException {
	List<DomainClass> domainClasses = new ArrayList<DomainClass>(MetadataManager.getDomainModel().getDomainClasses());
	Set<MetaDomainObject> metaDomainObjectsSet = new HashSet<MetaDomainObject>(rootDomainObject.getMetaDomainObjectsSet());
	
	for (DomainClass domainClass : domainClasses) {
	    MetaDomainObject metaDomainObject = getMetaDomainObject(metaDomainObjectsSet, domainClass.getFullName());
	    
	    if(metaDomainObject == null) {
		try {
		    new MetaDomainObject(domainClass.getFullName());
		} catch (ClassNotFoundException e) {
		    throw new FenixServiceException(e);
		}
		System.out.println("Created MetaDomainObject -> " + domainClass.getFullName());
	    } else {
		metaDomainObjectsSet.remove(metaDomainObject);
	    }
	}

	for (MetaDomainObject metaDomainObject : metaDomainObjectsSet) {
	    if(metaDomainObject.canBeDeleted()) {
		System.out.println("Deleted MetaDomainObject-> " + metaDomainObject.getType());
		metaDomainObject.delete();
	    }
	}
    }

    private MetaDomainObject getMetaDomainObject(Collection<MetaDomainObject> metaDomainObjects, String fullName) {
	for (MetaDomainObject metaDomainObject : metaDomainObjects) {
	    if(metaDomainObject.getType().equals(fullName)) {
		return metaDomainObject;
	    }
	}
	return null;
    }
}
