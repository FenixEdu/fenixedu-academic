package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério 17/Nov/2003
 */
public class ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName extends Service {

    public InfoCurriculum run(Integer executionDegreeCode, Integer curricularCourseCode,
            String stringExecutionYear) throws FenixServiceException, ExcepcaoPersistencia {
        InfoCurriculum infoCurriculum = null;

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }
        if (stringExecutionYear == null || stringExecutionYear.length() == 0) {
            throw new FenixServiceException("nullExecutionYearName");
        }
        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("noCurricularCourse");
        }

        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(stringExecutionYear);
        if (executionYear == null) {
            throw new NonExistingServiceException("noExecutionYear");
        }

        Curriculum curriculumExecutionYear = curricularCourse.findLatestCurriculumModifiedBefore(executionYear.getEndDate());
        if (curriculumExecutionYear != null) {
            List allCurricularCourseScopes = new ArrayList();
            List allExecutionCourses = new ArrayList();
            List executionPeriods = executionYear.getExecutionPeriods();
            Iterator iterExecutionPeriods = executionPeriods.iterator();
            while (iterExecutionPeriods.hasNext()) {
                ExecutionPeriod executionPeriod = (ExecutionPeriod) iterExecutionPeriods.next();
                Set<CurricularCourseScope> curricularCourseScopes = curricularCourse.findCurricularCourseScopesIntersectingPeriod(
                        executionPeriod.getBeginDate(),executionPeriod.getEndDate());
                if (curricularCourseScopes != null) {
                    List disjunctionCurricularCourseScopes = (List) CollectionUtils.disjunction(
                            allCurricularCourseScopes, curricularCourseScopes);
                    List intersectionCurricularCourseScopes = (List) CollectionUtils.intersection(
                            allCurricularCourseScopes, curricularCourseScopes);

                    allCurricularCourseScopes = (List) CollectionUtils.union(
                            disjunctionCurricularCourseScopes, intersectionCurricularCourseScopes);
                }
                List associatedExecutionCourses = new ArrayList();
                List<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
                for(ExecutionCourse executionCourse : executionCourses){
                    if(executionCourse.getExecutionPeriod().equals(executionPeriod))
                        associatedExecutionCourses.add(executionCourse);
                }
         
                if (associatedExecutionCourses != null) {
                    allExecutionCourses.addAll(associatedExecutionCourses);
                }

            }

            infoCurriculum = createInfoCurriculum(curriculumExecutionYear, allCurricularCourseScopes, allExecutionCourses);
        }
        return infoCurriculum;
    }

    private InfoCurriculum createInfoCurriculum(Curriculum curriculum, List allCurricularCourseScopes,
            List allExecutionCourses) throws ExcepcaoPersistencia {

        InfoCurriculum infoCurriculum = InfoCurriculumWithInfoCurricularCourse
                .newInfoFromDomain(curriculum);

        List scopes = new ArrayList();
        CollectionUtils.collect(allCurricularCourseScopes, new Transformer() {
            public Object transform(Object arg0) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;

                return InfoCurricularCourseScope
                        .newInfoFromDomain(curricularCourseScope);
            }
        }, scopes);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        Iterator iterExecutionCourses = allExecutionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourses.next();
            infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}