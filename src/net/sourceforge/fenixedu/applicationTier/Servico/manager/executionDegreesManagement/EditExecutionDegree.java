package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionDegreesManagement;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class EditExecutionDegree extends Service {

    public void run(Integer executionDegreeID, Integer executionYearID, Integer campusID,
            Boolean temporaryExamMap, Date periodLessonsFirstSemesterBegin,
            Date periodLessonsFirstSemesterEnd, Date periodExamsFirstSemesterBegin,
            Date periodExamsFirstSemesterEnd, Date periodLessonsSecondSemesterBegin,
            Date periodLessonsSecondSemesterEnd, Date periodExamsSecondSemesterBegin,
            Date periodExamsSecondSemesterEnd) throws FenixServiceException {

        final ExecutionDegree executionDegree = rootDomainObject
                .readExecutionDegreeByOID(executionDegreeID);
        if (executionDegree == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }

        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
        if (executionYear == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }

        final Campus campus = rootDomainObject.readCampusByOID(campusID);
        if (campus == null) {
            throw new FenixServiceException("error.noCampus");
        }

        executionDegree.edit(executionYear, campus, temporaryExamMap, periodLessonsFirstSemesterBegin,
                periodLessonsFirstSemesterEnd, periodExamsFirstSemesterBegin, periodExamsFirstSemesterEnd,
                periodLessonsSecondSemesterBegin, periodLessonsSecondSemesterEnd, periodExamsSecondSemesterBegin,
                periodExamsSecondSemesterEnd);
    }
}
