package ServidorAplicacao.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import DataBeans.InfoCurriculum;
import DataBeans.InfoCurriculumWithInfoCurricularCourse;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseWithExecutionPeriod;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 17/Nov/2003
 */
public class ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName
        implements IServico {

    private static ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName service = new ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName();

    public static ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName getService() {

        return service;
    }

    private ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName() {

    }

    public final String getNome() {

        return "ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName";
    }

    public InfoCurriculum run(Integer executionDegreeCode,
            Integer curricularCourseCode, String stringExecutionYear)
            throws FenixServiceException {
        InfoCurriculum infoCurriculum = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = sp
                    .getIPersistentCurricularCourse();
            IPersistentExecutionYear persistentExecutionYear = sp
                    .getIPersistentExecutionYear();
            IPersistentCurriculum persistentCurriculum = sp
                    .getIPersistentCurriculum();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp
                    .getIPersistentExecutionPeriod();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                    .getIPersistentCurricularCourseScope();
            IPersistentExecutionCourse persistentExecutionCourse = sp
                    .getIPersistentExecutionCourse();

            if (curricularCourseCode == null) {
                throw new FenixServiceException("nullCurricularCourse");
            }
            if (stringExecutionYear == null
                    || stringExecutionYear.length() == 0) {
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
                    .readCurriculumByCurricularCourseAndExecutionYear(
                            curricularCourse, executionYear);
            if (curriculumExecutionYear != null) {
                List allCurricularCourseScopes = new ArrayList();
                List allExecutionCourses = new ArrayList();
                List executionPeriods = persistentExecutionPeriod
                        .readByExecutionYear(executionYear);
                Iterator iterExecutionPeriods = executionPeriods.iterator();
                while (iterExecutionPeriods.hasNext()) {
                    IExecutionPeriod executionPeriod = (IExecutionPeriod) iterExecutionPeriods
                            .next();
                    List curricularCourseScopes = persistentCurricularCourseScope
                            .readCurricularCourseScopesByCurricularCourseInExecutionPeriod(
                                    curricularCourse, executionPeriod);
                    if (curricularCourseScopes != null) {
                        List disjunctionCurricularCourseScopes = (List) CollectionUtils
                                .disjunction(allCurricularCourseScopes,
                                        curricularCourseScopes);
                        List intersectionCurricularCourseScopes = (List) CollectionUtils
                                .intersection(allCurricularCourseScopes,
                                        curricularCourseScopes);

                        allCurricularCourseScopes = (List) CollectionUtils
                                .union(disjunctionCurricularCourseScopes,
                                        intersectionCurricularCourseScopes);
                    }
                    List associatedExecutionCourses = persistentExecutionCourse
                            .readListbyCurricularCourseAndExecutionPeriod(
                                    curricularCourse, executionPeriod);
                    if (associatedExecutionCourses != null) {
                        allExecutionCourses.addAll(associatedExecutionCourses);
                    }

                }

                infoCurriculum = createInfoCurriculum(curriculumExecutionYear,
                        persistentExecutionCourse, allCurricularCourseScopes,
                        allExecutionCourses);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoCurriculum;
    }

    private InfoCurriculum createInfoCurriculum(ICurriculum curriculum,
            IPersistentExecutionCourse persistentExecutionCourse,
            List allCurricularCourseScopes, List allExecutionCourses)
            throws ExcepcaoPersistencia {
        //CLONER
        //InfoCurriculum infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
        InfoCurriculum infoCurriculum  =InfoCurriculumWithInfoCurricularCourse.newInfoFromDomain(curriculum);

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
            IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses
                    .next();
            //CLONER
            //InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner
            //        .get(executionCourse);
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(executionCourse);
            
            infoExecutionCourse.setHasSite(persistentExecutionCourse
                    .readSite(executionCourse.getIdInternal()));
            infoExecutionCourses.add(infoExecutionCourse);
        }
        infoCurriculum.getInfoCurricularCourse()
                .setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}