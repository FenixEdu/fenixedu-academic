package ServidorAplicacao.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import DataBeans.InfoCurriculum;
import DataBeans.InfoCurriculumWithInfoCurricularCourse;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseWithExecutionPeriod;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 13/Nov/2003
 */
public class ReadCurrentCurriculumByCurricularCourseCode implements IServico {

    private static ReadCurrentCurriculumByCurricularCourseCode service = new ReadCurrentCurriculumByCurricularCourseCode();

    public static ReadCurrentCurriculumByCurricularCourseCode getService() {

        return service;
    }

    private ReadCurrentCurriculumByCurricularCourseCode() {

    }

    public final String getNome() {

        return "ReadCurrentCurriculumByCurricularCourseCode";
    }

    public InfoCurriculum run(Integer executionDegreeCode,
            Integer curricularCourseCode) throws FenixServiceException {

        InfoCurriculum infoCurriculum = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = sp
                    .getIPersistentCurricularCourse();
            IPersistentCurriculum persistentCurriculum = sp
                    .getIPersistentCurriculum();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                    .getIPersistentCurricularCourseScope();
            IPersistentExecutionCourse persistentExecutionCourse = sp
                    .getIPersistentExecutionCourse();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp
                    .getIPersistentExecutionPeriod();

            if (curricularCourseCode == null) {
                throw new FenixServiceException("nullCurricularCourse");
            }

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseCode);
            if (curricularCourse == null) {
                throw new NonExistingServiceException();
            }
            //selects active curricular course scopes
            List activeCurricularCourseScopes = persistentCurricularCourseScope
                    .readActiveCurricularCourseScopesByCurricularCourse(curricularCourse);

            activeCurricularCourseScopes = (List) CollectionUtils.select(
                    activeCurricularCourseScopes, new Predicate() {
                        public boolean evaluate(Object arg0) {
                            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                            if (curricularCourseScope.isActive().booleanValue()) {
                                return true;
                            }
                            return false;
                        }
                    });

            //selects execution courses for current execution period
            final IExecutionPeriod executionPeriod = persistentExecutionPeriod
                    .readActualExecutionPeriod();
            List associatedExecutionCourses = persistentExecutionCourse
                    .readListbyCurricularCourseAndExecutionPeriod(
                            curricularCourse, executionPeriod);

            ICurriculum curriculum = persistentCurriculum
                    .readCurriculumByCurricularCourse(curricularCourse);
            if (curriculum == null) {
                curriculum = new Curriculum();
                curriculum.setIdInternal(new Integer(0));
                curriculum.setCurricularCourse(curricularCourse);
            }

            //            curriculum.getCurricularCourse().setScopes(activeCurricularCourseScopes);
            //            curriculum.getCurricularCourse().setAssociatedExecutionCourses(associatedExecutionCourses);

            infoCurriculum = createInfoCurriculum(curriculum,
                    persistentExecutionCourse, activeCurricularCourseScopes,
                    associatedExecutionCourses);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoCurriculum;
    }

    private InfoCurriculum createInfoCurriculum(ICurriculum curriculum,
            IPersistentExecutionCourse persistentExecutionCourse,
            List activeCurricularCourseScopes, List associatedExecutionCourses)
            throws ExcepcaoPersistencia {
        
        //CLONER
        InfoCurriculum infoCurriculum = InfoCurriculumWithInfoCurricularCourse.newInfoFromDomain(curriculum);
        //InfoCurriculum infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
        //infoCurriculum.setIdInternal(curriculum.getIdInternal());

        List scopes = new ArrayList();

        CollectionUtils.collect(activeCurricularCourseScopes,
                new Transformer() {
                    public Object transform(Object arg0) {
                        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                        
                        //CLONER
                        return InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear
                        .newInfoFromDomain(curricularCourseScope);
                        //return Cloner
                        //        .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
                        
                    }
                }, scopes);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List infoExecutionCourses = new ArrayList();
        //        List executionCourses = associatedExecutionCourses;
        Iterator iterExecutionCourses = associatedExecutionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses
                    .next();
            
            //CLONER
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(executionCourse);
            //InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner
            //        .get(executionCourse);
            infoExecutionCourse.setHasSite(persistentExecutionCourse
                    .readSite(executionCourse.getIdInternal()));
            infoExecutionCourses.add(infoExecutionCourse);
        }
        infoCurriculum.getInfoCurricularCourse()
                .setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}