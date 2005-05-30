/*
 * Created on 28/Jul/2003, 11:01:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Jul/2003, 11:01:39
 *  
 */
public class SeminaryVO extends VersionedObjectsBase implements IPersistentSeminary {

    public List readAll() {
        return (List) readAll(Seminary.class);
    }

}