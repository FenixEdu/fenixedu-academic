package ServidorAplicacao.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import DataBeans.InfoDegreeCurricularPlan;
import Dominio.CursoExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 10/Nov/2003
 *  
 */
public class ReadDegreeCurricularPlanHistoryByExecutionDegreeCode implements IService {

    public InfoDegreeCurricularPlan run(Integer executionDegreeCode) throws FenixServiceException {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();

            if (executionDegreeCode == null) {
                throw new FenixServiceException("nullDegree");
            }

            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOID(
                    CursoExecucao.class, executionDegreeCode);

            if (executionDegree == null) {
                throw new NonExistingServiceException();
            }
            IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getCurricularPlan();
            if (degreeCurricularPlan != null) {
                List allCurricularCourses = sp.getIPersistentCurricularCourse()
                        .readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);

                if (allCurricularCourses != null && !allCurricularCourses.isEmpty()) {

                    Iterator iterator = allCurricularCourses.iterator();
                    while (iterator.hasNext()) {
                        ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

                        List curricularCourseScopes = sp.getIPersistentCurricularCourseScope()
                                .readByCurricularCourse(curricularCourse);

                        if (curricularCourseScopes != null) {
                            curricularCourse.setScopes(curricularCourseScopes);
                        }
                    }
                    infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(executionDegree,
                            allCurricularCourses);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoDegreeCurricularPlan;
    }

    private InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(ICursoExecucao executionDegree,
            List allCurricularCourses) {
        //CLONER
        //InfoDegreeCurricularPlan infoDegreeCurricularPlan =
        //  Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(
        //    executionDegree.getCurricularPlan());
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(executionDegree.getCurricularPlan());

        List allInfoCurricularCourses = new ArrayList();

        CollectionUtils.collect(allCurricularCourses, new Transformer() {
            public Object transform(Object arg0) {
                ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                List allInfoCurricularCourseScopes = new ArrayList();
                CollectionUtils.collect(curricularCourse.getScopes(), new Transformer() {
                    public Object transform(Object arg0) {
                        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                        //CLONER
                        //return
                        // Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(
                        //curricularCourseScope);
                        return InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear
                                .newInfoFromDomain(curricularCourseScope);
                    }
                }, allInfoCurricularCourseScopes);

                //CLONER
                //InfoCurricularCourse infoCurricularCourse =
                //    Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                        .newInfoFromDomain(curricularCourse);
                infoCurricularCourse.setInfoScopes(allInfoCurricularCourseScopes);
                return infoCurricularCourse;
            }
        }, allInfoCurricularCourses);

        infoDegreeCurricularPlan.setCurricularCourses(allInfoCurricularCourses);
        return infoDegreeCurricularPlan;
    }
}