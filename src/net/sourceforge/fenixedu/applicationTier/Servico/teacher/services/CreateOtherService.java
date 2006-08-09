package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateOtherService extends Service {

    public void run(Integer teacherID, Integer executionPeriodID, Double credits, String reason)
            throws ExcepcaoPersistencia {

        Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
        
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if(teacherService == null){
            teacherService = new TeacherService(teacher,executionPeriod);
        }
        
        new OtherService(teacherService,credits,reason);
    }
    
}
