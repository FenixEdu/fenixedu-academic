/*
 * Created on Aug 26, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
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