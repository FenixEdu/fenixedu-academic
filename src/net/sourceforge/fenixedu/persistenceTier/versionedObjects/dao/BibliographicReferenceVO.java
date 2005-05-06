/*
 * Created on May 5, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jdnf
 *  
 */
public class BibliographicReferenceVO extends VersionedObjectsBase implements
        IPersistentBibliographicReference {

    public List readBibliographicReference(Integer executionCourseOID) throws ExcepcaoPersistencia {
        final IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        return (executionCourse != null) ? executionCourse.getAssociatedBibliographicReferences()
                : new ArrayList();
    }

}
