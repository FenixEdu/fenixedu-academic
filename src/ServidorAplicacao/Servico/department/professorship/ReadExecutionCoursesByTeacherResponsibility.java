/*
 * Created on Aug 26, 2004
 *
 */
package ServidorAplicacao.Servico.department.professorship;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *  
 */
public class ReadExecutionCoursesByTeacherResponsibility implements IService {

    /**
     *  
     */
    public ReadExecutionCoursesByTeacherResponsibility() {
    }

    public List run(Integer teacherNumber) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
            IPersistentResponsibleFor persistentResponsibleFor = persistentSuport
                    .getIPersistentResponsibleFor();
            List responsibilities = persistentResponsibleFor.readByTeacher(teacher);
            if (responsibilities != null) {
                return (List) CollectionUtils.collect(responsibilities, new Transformer() {

                    public Object transform(Object arg0) {
                        IResponsibleFor responsibleFor = (IResponsibleFor) arg0;
                        return InfoExecutionCourse
                                .newInfoFromDomain(responsibleFor.getExecutionCourse());
                    }
                });
            }
            return new ArrayList();
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}