package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 17/Nov/2003
 */
public class ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName implements IService {

    public InfoCurriculum run(Integer executionDegreeCode, Integer curricularCourseCode,
            String stringExecutionYear) throws FenixServiceException {
        InfoCurriculum infoCurriculum = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                    .getIPersistentCurricularCourseScope();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            if (curricularCourseCode == null) {
                throw new FenixServiceException("nullCurricularCourse");
            }
            if (stringExecutionYear == null || stringExecutionYear.length() == 0) {
                throw new FenixServiceException("nullExecutionYearName");
            }
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseCode);
            if (curricularCourse == null) {
                throw new NonExistingServiceException("noCurricularCourse");
            }

            IExecutionYear executionYear = persistentExecutionYear
                    .readExecutionYearByName(stringExecutionYear);
            if (executionYear == null) {
                throw new NonExistingServiceException("noExecutionYear");
            }

            ICurriculum curriculumExecutionYear = persistentCurriculum
                    .readCurriculumByCurricularCourseAndExecutionYear(curricularCourse, executionYear);
            if (curriculumExecutionYear != null) {
                List allCurricularCourseScopes = new ArrayList();
                List allExecutionCourses = new ArrayList();
                List executionPeriods = persistentExecutionPeriod.readByExecutionYear(executionYear);
                Iterator iterExecutionPeriods = executionPeriods.iterator();
                while (iterExecutionPeriods.hasNext()) {
                    IExecutionPeriod executionPeriod = (IExecutionPeriod) iterExecutionPeriods.next();
                    List curricularCourseScopes = persistentCurricularCourseScope
                            .readCurricularCourseScopesByCurricularCourseInExecutionPeriod(
                                    curricularCourse, executionPeriod);
                    if (curricularCourseScopes != null) {
                        List disjunctionCurricularCourseScopes = (List) CollectionUtils.disjunction(
                                allCurricularCourseScopes, curricularCourseScopes);
                        List intersectionCurricularCourseScopes = (List) CollectionUtils.intersection(
                                allCurricularCourseScopes, curricularCourseScopes);

                        allCurricularCourseScopes = (List) CollectionUtils.union(
                                disjunctionCurricularCourseScopes, intersectionCurricularCourseScopes);
                    }
                    List associatedExecutionCourses = persistentExecutionCourse
                            .readListbyCurricularCourseAndExecutionPeriod(curricularCourse,
                                    executionPeriod);
                    if (associatedExecutionCourses != null) {
                        allExecutionCourses.addAll(associatedExecutionCourses);
                    }

                }

                infoCurriculum = createInfoCurriculum(curriculumExecutionYear,
                        persistentExecutionCourse, allCurricularCourseScopes, allExecutionCourses);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoCurriculum;
    }

    private InfoCurriculum createInfoCurriculum(ICurriculum curriculum,
            IPersistentExecutionCourse persistentExecutionCourse, List allCurricularCourseScopes,
            List allExecutionCourses) throws ExcepcaoPersistencia {
        //CLONER
        //InfoCurriculum infoCurriculum =
        // Cloner.copyICurriculum2InfoCurriculum(curriculum);
        InfoCurriculum infoCurriculum = InfoCurriculumWithInfoCurricularCourse
                .newInfoFromDomain(curriculum);

        List scopes = new ArrayList();
        CollectionUtils.collect(allCurricularCourseScopes, new Transformer() {
            public Object transform(Object arg0) {
                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                //CLONER
                //return Cloner
                //        .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
                return InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear
                        .newInfoFromDomain(curricularCourseScope);
            }
        }, scopes);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List infoExecutionCourses = new ArrayList();
        Iterator iterExecutionCourses = allExecutionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses.next();
            //CLONER
            //InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse)
            // Cloner
            //        .get(executionCourse);
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(executionCourse);

            infoExecutionCourse.setHasSite(persistentExecutionCourse.readSite(executionCourse
                    .getIdInternal()));
            infoExecutionCourses.add(infoExecutionCourse);
        }
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}