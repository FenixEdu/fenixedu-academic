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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
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
        IPersistentExecutionCourse persistentExecutionCourse = suportePersistente
                .getIPersistentExecutionCourse();
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, infoExecutionCourse.getIdInternal());

        List result = executionCourse.responsibleFors();

        List infoResult = new ArrayList();
        if (result != null) {
            Iterator iter = result.iterator();
            while (iter.hasNext()) {
                IProfessorship responsibleFor = (IProfessorship) iter.next();
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
        IPersistentExecutionCourse persistentExecutionCourse = suportePersistente
                .getIPersistentExecutionCourse();
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);

        List result = executionCourse.responsibleFors();

        List infoResult = new ArrayList();
        if (result != null) {
            Iterator iter = result.iterator();
            while (iter.hasNext()) {
                IProfessorship responsibleFor = (IProfessorship) iter.next();
                ITeacher teacher = responsibleFor.getTeacher();
                InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                infoResult.add(infoTeacher);
            }
            return infoResult;
        }
        return result;
    }
}