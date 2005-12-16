/**
 * Nov 28, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.IAdvise;
import net.sourceforge.fenixedu.domain.teacher.ITeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditTeacherAdviseService implements IService {

    public void run(Integer teacherID, Integer executionPeriodID, final Integer studentNumber,
            Double percentage, AdviseType adviseType) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ITeacher teacher = (ITeacher) persistentSupport.getIPersistentTeacher().readByOID(Teacher.class,
                teacherID);
        IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentSupport
                .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class, executionPeriodID);
        
        List<IStudent> students = persistentSupport.getIPersistentStudent().readAll();
        IStudent student = (IStudent) CollectionUtils.find(students, new Predicate(){
            public boolean evaluate(Object arg0) {
                IStudent tempStudent = (IStudent) arg0; 
                return tempStudent.getNumber().equals(studentNumber);
            }});
        
        ITeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if(teacherService == null){
            teacherService = DomainFactory.makeTeacherService(teacher,executionPeriod);
        }
        List<IAdvise> advises = student.getAdvisesByTeacher(teacher);
        IAdvise advise = null;
        if(advises == null || advises.isEmpty()){
            advise = DomainFactory.makeAdvise(teacher,student,adviseType,executionPeriod,executionPeriod);
        } else {
            advise = advises.iterator().next();
        }

        ITeacherAdviseService teacherAdviseService = advise.getTeacherAdviseServiceByExecutionPeriod(executionPeriod);        
        if(teacherAdviseService == null){
            teacherAdviseService = DomainFactory.makeTeacherAdviseService(teacherService,advise,percentage);
        } else {
            teacherAdviseService.updatePercentage(percentage);
        }
    }
}
