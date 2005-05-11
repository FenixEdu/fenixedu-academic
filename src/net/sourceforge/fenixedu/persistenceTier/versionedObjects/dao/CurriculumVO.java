/*
 * Created on May 11, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jdnf
 *  
 */
public class CurriculumVO extends VersionedObjectsBase implements IPersistentCurriculum {

    public ICurriculum readCurriculumByCurricularCourse(Integer curricularCourseOID)
            throws ExcepcaoPersistencia {

        ICurriculum lastCurriculum = null;
        final ICurricularCourse curricularCourse = (ICurricularCourse) readByOID(CurricularCourse.class,
                curricularCourseOID);

        if (curricularCourse != null) {
            final List associatedCurriculums = curricularCourse.getAssociatedCurriculums();
            for (Iterator iter = associatedCurriculums.iterator(); iter.hasNext();) {
                final ICurriculum curriculum = (ICurriculum) iter.next();
                if (lastCurriculum == null
                        || curriculum.getLastModificationDate().after(
                                lastCurriculum.getLastModificationDate()))
                    lastCurriculum = curriculum;
            }
        }

        return lastCurriculum;
    }

    public ICurriculum readCurriculumByCurricularCourseAndExecutionYear(Integer curricularCourseOID,
            Date executionYearEndDate) throws ExcepcaoPersistencia {

        ICurriculum lastCurriculum = null;
        final ICurricularCourse curricularCourse = (ICurricularCourse) readByOID(CurricularCourse.class,
                curricularCourseOID);

        if (curricularCourse != null) {
            final List associatedCurriculums = curricularCourse.getAssociatedCurriculums();
            for (Iterator iter = associatedCurriculums.iterator(); iter.hasNext();) {
                final ICurriculum curriculum = (ICurriculum) iter.next();
                if ((curriculum.getLastModificationDate().before(executionYearEndDate) || curriculum
                        .getLastModificationDate().equals(executionYearEndDate))
                        && (lastCurriculum == null || curriculum.getLastModificationDate().after(
                                lastCurriculum.getLastModificationDate())))
                    lastCurriculum = curriculum;
            }
        }

        return lastCurriculum;
    }

}
