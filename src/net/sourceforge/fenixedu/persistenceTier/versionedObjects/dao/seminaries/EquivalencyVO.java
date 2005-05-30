/*
 * Created on 4/Ago/2003, 13:08:05
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 4/Ago/2003, 13:08:05
 *  
 */
public class EquivalencyVO extends VersionedObjectsBase implements
        IPersistentSeminaryCurricularCourseEquivalency {

    public List readAll() {
        return (List) readAll(CourseEquivalency.class);
    }

}