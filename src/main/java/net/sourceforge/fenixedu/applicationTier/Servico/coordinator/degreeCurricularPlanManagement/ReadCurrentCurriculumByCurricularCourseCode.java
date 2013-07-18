package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Fernanda Quitério 13/Nov/2003
 */
public class ReadCurrentCurriculumByCurricularCourseCode {

    @Checked("RolePredicates.COORDINATOR_PREDICATE")
    @Service
    public static InfoCurriculum run(Integer executionDegreeCode, Integer curricularCourseCode) throws FenixServiceException {

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }

        CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }
        // selects active curricular course scopes
        List<CurricularCourseScope> activeCurricularCourseScopes = curricularCourse.getActiveScopes();

        activeCurricularCourseScopes = (List) CollectionUtils.select(activeCurricularCourseScopes, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
                if (curricularCourseScope.isActive().booleanValue()) {
                    return true;
                }
                return false;
            }
        });

        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();

        List<ExecutionCourse> associatedExecutionCourses = new ArrayList<ExecutionCourse>();
        List<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getExecutionPeriod().equals(executionSemester)) {
                associatedExecutionCourses.add(executionCourse);
            }
        }

        Curriculum curriculum = curricularCourse.findLatestCurriculum();
        InfoCurriculum infoCurriculum = null;
        if (curriculum != null) {
            infoCurriculum = InfoCurriculumWithInfoCurricularCourse.newInfoFromDomain(curriculum);
        } else {
            infoCurriculum = new InfoCurriculumWithInfoCurricularCourse();
            infoCurriculum.setIdInternal(Integer.valueOf(0));
            infoCurriculum.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }

        infoCurriculum = createInfoCurriculum(infoCurriculum, activeCurricularCourseScopes, associatedExecutionCourses);
        return infoCurriculum;
    }

    private static InfoCurriculum createInfoCurriculum(InfoCurriculum infoCurriculum, List activeCurricularCourseScopes,
            List associatedExecutionCourses) {

        List scopes = new ArrayList();

        CollectionUtils.collect(activeCurricularCourseScopes, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;

                return InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
            }
        }, scopes);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        Iterator iterExecutionCourses = associatedExecutionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourses.next();
            infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}