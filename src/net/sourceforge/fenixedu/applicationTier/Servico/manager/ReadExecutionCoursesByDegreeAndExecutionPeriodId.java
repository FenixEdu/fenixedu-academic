/*
 * Created on 3/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.DegreeCurricularPlanState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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