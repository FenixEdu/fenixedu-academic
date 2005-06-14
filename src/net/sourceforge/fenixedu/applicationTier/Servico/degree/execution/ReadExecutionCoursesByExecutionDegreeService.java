/*
 * Created on Dec 10, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.execution;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadExecutionCoursesByExecutionDegreeService implements IService {

    /**
     * @author jpvl
     */
    public class NonExistingExecutionDegree extends FenixServiceException {

        /**
         *  
         */
        public NonExistingExecutionDegree() {
            super();
        }
    }

    /**
     *  
     */
    public ReadExecutionCoursesByExecutionDegreeService() {
        super();
    }

    public List run(Integer executionDegreeId, Integer executionPeriodId) throws FenixServiceException {
        List infoExecutionCourseList = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = null;

            if (executionPeriodId == null) {
                executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            } else {
                executionPeriod = (IExecutionPeriod) executionCourseDAO.readByOID(ExecutionPeriod.class,
                        executionPeriodId);
            }

            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            IExecutionDegree executionDegree = (IExecutionDegree) executionDegreeDAO.readByOID(
                    ExecutionDegree.class, executionDegreeId);

            if (executionDegree == null) {
                throw new NonExistingExecutionDegree();
            }

            List executionCourseList = executionCourseDAO.readByExecutionDegreeAndExecutionPeriod(
                    executionDegree.getDegreeCurricularPlan().getIdInternal(), executionPeriod.getIdInternal());

            infoExecutionCourseList = (List) CollectionUtils.collect(executionCourseList,
                    new Transformer() {

                        public Object transform(Object input) {
                            IExecutionCourse executionCourse = (IExecutionCourse) input;
                            InfoExecutionCourse infoExecutionCourse;
                            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
                            return infoExecutionCourse;
                        }
                    });
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database", e);
        }
        return infoExecutionCourseList;

    }
}