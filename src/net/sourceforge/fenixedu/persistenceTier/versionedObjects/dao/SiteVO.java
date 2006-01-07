/*
 * Created on May 17, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jdnf
 *  
 */
public class SiteVO extends VersionedObjectsBase implements IPersistentSite {

    public Site readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia {

        final ExecutionCourse executionCourse = (ExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseID);

        return (executionCourse != null) ? executionCourse.getSite() : null;
    }

}
