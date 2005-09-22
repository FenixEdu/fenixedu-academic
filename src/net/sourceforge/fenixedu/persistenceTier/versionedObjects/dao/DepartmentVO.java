package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class DepartmentVO extends VersionedObjectsBase implements IPersistentDepartment {

    public List readAll() {
        return (List) readAll(Department.class);
    }
    
    public IDepartment readByName(String name){
        Collection<IDepartment> departments = readAll(Department.class);
        
        for(IDepartment department : departments){
            if(department.getName().equals(name))
                return department;
        }        
        return null;
    }
}
