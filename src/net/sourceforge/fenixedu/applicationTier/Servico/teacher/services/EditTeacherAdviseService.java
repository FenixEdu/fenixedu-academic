/**
 * Nov 28, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditTeacherAdviseService extends Service {

    public void run(Integer teacherID, Integer executionPeriodID, final Integer studentNumber,
            Double percentage, AdviseType adviseType) throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Teacher teacher = (Teacher) persistentSupport.getIPersistentTeacher().readByOID(Teacher.class,
                teacherID);
        ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentSupport
                .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class, executionPeriodID);
        
        List<Student> students = persistentSupport.getIPersistentStudent().readAll();
        Student student = (Student) CollectionUtils.find(students, new Predicate(){
            public boolean evaluate(Object arg0) {
                Student tempStudent = (Student) arg0; 
                return tempStudent.getNumber().equals(studentNumber);
            }});
        
        if(student == null){
            throw new FenixServiceException("errors.invalid.student-number");
        }
        
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if(teacherService == null){
            teacherService = DomainFactory.makeTeacherService(teacher,executionPeriod);
        }
        List<Advise> advises = student.getAdvisesByTeacher(teacher);
        Advise advise = null;
        if(advises == null || advises.isEmpty()){
            advise = DomainFactory.makeAdvise(teacher,student,adviseType,executionPeriod,executionPeriod);
        } else {
            advise = advises.iterator().next();
        }

        TeacherAdviseService teacherAdviseService = advise.getTeacherAdviseServiceByExecutionPeriod(executionPeriod);        
        if(teacherAdviseService == null){
            teacherAdviseService = DomainFactory.makeTeacherAdviseService(teacherService,advise,percentage);
        } else {
            teacherAdviseService.updatePercentage(percentage);
        }
    }
}
