/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCourseGroup extends Service {

    public void run(final Integer courseGroupID, final String name, final String nameEn) throws ExcepcaoPersistencia,
            FenixServiceException {
        final CourseGroup courseGroup = (CourseGroup) persistentObject
                .readByOID(CourseGroup.class, courseGroupID);
        if (courseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        courseGroup.edit(name, nameEn);
    }
}
