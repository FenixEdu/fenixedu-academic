package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério 17/Nov/2003
 */
public class ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName extends Service {

    public InfoCurriculum run(Integer executionDegreeCode, Integer curricularCourseCode,
            String stringExecutionYear) throws FenixServiceException, ExcepcaoPersistencia {
        InfoCurriculum infoCurriculum = null;

        IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
        IPersistentCurriculum persistentCurriculum = persistentSupport.getIPersistentCurriculum();
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSupport
                .getIPersistentCurricularCourseScope();
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }
        if (stringExecutionYear == null || stringExecutionYear.length() == 0) {
            throw new FenixServiceException("nullExecutionYearName");
        }
        CurricularCourse curricularCourse = (CurricularCourse) persistentObject.readByOID(
                CurricularCourse.class, curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("noCurricularCourse");
        }

        ExecutionYear executionYear = persistentExecutionYear
                .readExecutionYearByName(stringExecutionYear);
        if (executionYear == null) {
            throw new NonExistingServiceException("noExecutionYear");
        }

        Curriculum curriculumExecutionYear = persistentCurriculum
                .readCurriculumByCurricularCourseAndExecutionYear(curricularCourse.getIdInternal(),
                        executionYear.getEndDate());
        if (curriculumExecutionYear != null) {
            List allCurricularCourseScopes = new ArrayList();
            List allExecutionCourses = new ArrayList();
            List executionPeriods = persistentExecutionPeriod.readByExecutionYear(executionYear.getIdInternal());
            Iterator iterExecutionPeriods = executionPeriods.iterator();
            while (iterExecutionPeriods.hasNext()) {
                ExecutionPeriod executionPeriod = (ExecutionPeriod) iterExecutionPeriods.next();
                List curricularCourseScopes = persistentCurricularCourseScope
                        .readCurricularCourseScopesByCurricularCourseInExecutionPeriod(curricularCourse.getIdInternal(),
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

            infoCurriculum = createInfoCurriculum(curriculumExecutionYear, persistentExecutionCourse,
                    allCurricularCourseScopes, allExecutionCourses);
        }
        return infoCurriculum;
    }

    private InfoCurriculum createInfoCurriculum(Curriculum curriculum,
            IPersistentExecutionCourse persistentExecutionCourse, List allCurricularCourseScopes,
            List allExecutionCourses) throws ExcepcaoPersistencia {

        InfoCurriculum infoCurriculum = InfoCurriculumWithInfoCurricularCourse
                .newInfoFromDomain(curriculum);

        List scopes = new ArrayList();
        CollectionUtils.collect(allCurricularCourseScopes, new Transformer() {
            public Object transform(Object arg0) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;

                return InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear
                        .newInfoFromDomain(curricularCourseScope);
            }
        }, scopes);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List infoExecutionCourses = new ArrayList();
        Iterator iterExecutionCourses = allExecutionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourses.next();

            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(executionCourse);

            Boolean hasSite;
            if(executionCourse.getSite() != null)
                hasSite = true;
            else
                hasSite = false;
            
            infoExecutionCourse.setHasSite(hasSite);
            infoExecutionCourses.add(infoExecutionCourse);
        }
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}