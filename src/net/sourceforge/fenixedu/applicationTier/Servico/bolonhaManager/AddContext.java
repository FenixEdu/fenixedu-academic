/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.ICourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AddContext implements IService {

    public void run(ICurricularCourse curricularCourse, ICourseGroup courseGroup, Integer year,
            Integer semester) throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        // TODO: check CurricularSemesterID for null value
        final ICurricularSemester curricularSemester = persistentSupport
                .getIPersistentCurricularSemester().readCurricularSemesterBySemesterAndCurricularYear(
                        semester, year);
        if (curricularSemester == null) {
            throw new FenixServiceException("error.noCurricularSemesterGivenYearAndSemester");
        }
        // TODO: this should be modified to receive ExecutionYear, but for now
        // we just read the '2006/2007'
        final IExecutionYear executionYear = persistentSupport.getIPersistentExecutionYear()
                .readExecutionYearByName("2006/2007");
        final IExecutionPeriod beginExecutionPeriod = executionYear
                .getExecutionPeriodForSemester(Integer.valueOf(1));

        curricularCourse.addContext(courseGroup, curricularSemester, beginExecutionPeriod, null);
    }
}
