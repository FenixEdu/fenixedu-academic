/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditContextFromCurricularCourse implements IService {

    public void run(CurricularCourse curricularCourse, Context context, CourseGroup courseGroup,
            Integer year, Integer semester) throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        // TODO: check CurricularSemesterID for null value
        final CurricularSemester curricularSemester = persistentSupport
                .getIPersistentCurricularSemester().readCurricularSemesterBySemesterAndCurricularYear(
                        semester, year);
        if (curricularSemester == null) {
            throw new FenixServiceException("error.noCurricularSemesterGivenYearAndSemester");
        }
        curricularCourse.editContext(context, courseGroup, curricularSemester);
    }
}
