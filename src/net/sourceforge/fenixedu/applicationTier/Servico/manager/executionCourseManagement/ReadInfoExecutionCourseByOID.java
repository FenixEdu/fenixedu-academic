package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadInfoExecutionCourseByOID implements IService {

    public InfoExecutionCourse run(Integer executionCourseOID) throws FenixServiceException {

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionDegree persistentExecutionCourse = sp.getIPersistentExecutionDegree();
            if (executionCourseOID == null) {
                throw new FenixServiceException("nullId");
            }

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseOID);

            List curricularCourses = executionCourse.getAssociatedCurricularCourses();

            List infoCurricularCourses = new ArrayList();

            CollectionUtils.collect(curricularCourses, new Transformer() {
                public Object transform(Object input) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) input;

                    return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                }
            }, infoCurricularCourses);

            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoExecutionCourse;
    }
}