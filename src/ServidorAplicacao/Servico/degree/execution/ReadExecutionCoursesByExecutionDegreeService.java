/*
 * Created on Dec 10, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.degree.execution;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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

            ICursoExecucao executionDegree = (ICursoExecucao) executionDegreeDAO.readByOID(
                    CursoExecucao.class, executionDegreeId);

            if (executionDegree == null) {
                throw new NonExistingExecutionDegree();
            }

            List executionCourseList = executionCourseDAO.readByExecutionDegreeAndExecutionPeriod(
                    executionDegree, executionPeriod);

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