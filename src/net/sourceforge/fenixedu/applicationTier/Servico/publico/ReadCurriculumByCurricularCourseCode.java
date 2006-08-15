package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PeriodState;

public class ReadCurriculumByCurricularCourseCode extends Service {

    public InfoCurriculum run(final Integer curricularCourseCode)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }

        final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        final Curriculum curriculum = curricularCourse.findLatestCurriculum();
        final InfoCurriculum infoCurriculum = (curriculum != null) ? InfoCurriculum.newInfoFromDomain(curriculum) : new InfoCurriculum();
        infoCurriculum.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));

        List infoExecutionCourses = buildExecutionCourses(curricularCourse);
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);

        List activeInfoScopes = buildActiveScopes(curricularCourse);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(activeInfoScopes);

        return infoCurriculum;
    }

    private List buildExecutionCourses(final CurricularCourse curricularCourse) {
        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
            final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            if (executionPeriod.getState().equals(PeriodState.OPEN)
                    || executionPeriod.getState().equals(PeriodState.CURRENT)) {
                final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                infoExecutionCourse.setHasSite(executionCourse.getSite() != null);
                infoExecutionCourses.add(infoExecutionCourse);
            }
        }
        return infoExecutionCourses;
    }

    private List<InfoCurricularCourseScope> buildActiveScopes(final CurricularCourse curricularCourse) {
        final List<InfoCurricularCourseScope> activeInfoCurricularCourseScopes = new ArrayList<InfoCurricularCourseScope>();
        for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
            if (curricularCourseScope.isActive()) {
                activeInfoCurricularCourseScopes.add(InfoCurricularCourseScopeWithBranchAndSemesterAndYear.newInfoFromDomain(curricularCourseScope));
            }
        }
        return activeInfoCurricularCourseScopes;
    }
}