/*
 * Created on Sep 19, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.IUnit;
import net.sourceforge.fenixedu.domain.Unit;
import net.sourceforge.fenixedu.persistenceTier.IPersistentUnit;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class UnitVO extends VersionedObjectsBase implements IPersistentUnit{
    
    public IUnit readByName(String unitName){
        Collection<IUnit> allUnits = readAll(Unit.class);
        for(IUnit unit : allUnits){
            if(unit.getName().equals(unitName))
                return unit;
        }              
        return null;
    }
}
