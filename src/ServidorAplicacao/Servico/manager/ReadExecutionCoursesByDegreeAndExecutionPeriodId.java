/*
 * Created on 3/Dez/2003
 *  
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.Degree;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.IDegree;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 3/Dez/2003
 *  
 */
public class ReadExecutionCoursesByDegreeAndExecutionPeriodId implements IService {

    public List run(Integer degreeId, Integer executionPeriodId) throws FenixServiceException {
        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = ps.getIPersistentExecutionPeriod();
            ICursoPersistente persistentDegree = ps.getICursoPersistente();

            final IExecutionPeriod executionPeriod2Compare = (IExecutionPeriod) persistentExecutionPeriod
                    .readByOID(ExecutionPeriod.class, executionPeriodId);
            if (executionPeriod2Compare == null) {
                throw new InvalidArgumentsServiceException();
            }
            IDegree degree = (IDegree) persistentDegree.readByOID(Degree.class, degreeId);
            if (degree == null) {
                throw new InvalidArgumentsServiceException();
            }
            List curricularPlans = degree.getDegreeCurricularPlans();
            Iterator iter = curricularPlans.iterator();
            List curricularCourses = new ArrayList();
            while (iter.hasNext()) {
                IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iter.next();
                if (degreeCurricularPlan.getState().getDegreeState().intValue() == DegreeCurricularPlanState.ACTIVE) {
                    curricularCourses.addAll(degreeCurricularPlan.getCurricularCourses());
                }
            }
            List executionCourses = new ArrayList();
            iter = curricularCourses.iterator();
            while (iter.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                executionCourses.addAll(CollectionUtils.select(curricularCourse
                        .getAssociatedExecutionCourses(), new Predicate() {
                    public boolean evaluate(Object arg0) {
                        IExecutionCourse executionCourse = (IExecutionCourse) arg0;

                        return executionCourse.getExecutionPeriod().equals(executionPeriod2Compare);
                    }
                }));
            }
            List infoExecutionCourses = (List) CollectionUtils.collect(executionCourses,
                    new Transformer() {
                        public Object transform(Object arg0) {
                            return Cloner.get((IExecutionCourse) arg0);
                        }
                    });
            return infoExecutionCourses;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}