/*
 * Created on 25/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 *  
 */
public class ReadTeachersByExecutionCourseResponsibility implements IService {

    public List run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentResponsibleFor persistentResponsibleFor = suportePersistente
                .getIPersistentResponsibleFor();
        List result = persistentResponsibleFor
                .readByExecutionCourse(infoExecutionCourse.getIdInternal());

        List infoResult = new ArrayList();
        if (result != null) {
            Iterator iter = result.iterator();
            while (iter.hasNext()) {
                IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
                ITeacher teacher = responsibleFor.getTeacher();
                InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                infoResult.add(infoTeacher);
            }
            return infoResult;
        }

        return result;
    }

    public List run(Integer executionCourseID) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentResponsibleFor persistentResponsibleFor = suportePersistente
                .getIPersistentResponsibleFor();
        List result = persistentResponsibleFor.readByExecutionCourse(executionCourseID);

        List infoResult = new ArrayList();
        if (result != null) {
            Iterator iter = result.iterator();
            while (iter.hasNext()) {
                IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
                ITeacher teacher = responsibleFor.getTeacher();
                InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                infoResult.add(infoTeacher);
            }
            return infoResult;
        }
        return result;
    }
}