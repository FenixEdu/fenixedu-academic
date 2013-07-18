package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.ist.fenixWebFramework.services.Service;

public class ReadCurriculumByCurricularCourseCode {

    @Service
    public static InfoCurriculum run(final Integer curricularCourseCode) throws FenixServiceException {

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }

        final CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        final Curriculum curriculum = curricularCourse.findLatestCurriculum();
        final InfoCurriculum infoCurriculum =
                (curriculum != null) ? InfoCurriculum.newInfoFromDomain(curriculum) : new InfoCurriculum();
        infoCurriculum.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));

        List infoExecutionCourses = buildExecutionCourses(curricularCourse);
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);

        List activeInfoScopes = buildActiveScopes(curricularCourse);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(activeInfoScopes);

        return infoCurriculum;
    }

    private static List buildExecutionCourses(final CurricularCourse curricularCourse) {
        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
            if (executionSemester.getState().equals(PeriodState.OPEN) || executionSemester.getState().equals(PeriodState.CURRENT)) {
                infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            }
        }
        return infoExecutionCourses;
    }

    private static List<InfoCurricularCourseScope> buildActiveScopes(final CurricularCourse curricularCourse) {
        final List<InfoCurricularCourseScope> activeInfoCurricularCourseScopes = new ArrayList<InfoCurricularCourseScope>();
        for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
            if (curricularCourseScope.isActive()) {
                activeInfoCurricularCourseScopes.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
            }
        }
        return activeInfoCurricularCourseScopes;
    }
}