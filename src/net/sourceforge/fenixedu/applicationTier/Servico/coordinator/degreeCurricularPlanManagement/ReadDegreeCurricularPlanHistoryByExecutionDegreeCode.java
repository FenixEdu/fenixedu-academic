package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 10/Nov/2003
 *  
 */
public class ReadDegreeCurricularPlanHistoryByExecutionDegreeCode implements IService {

    public InfoDegreeCurricularPlan run(Integer executionDegreeCode) throws FenixServiceException {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();

            if (executionDegreeCode == null) {
                throw new FenixServiceException("nullDegree");
            }

            IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, executionDegreeCode);

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

    private InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(IExecutionDegree executionDegree,
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