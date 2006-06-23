package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditTeacherServiceNotes extends Service {

    public Boolean run(Integer teacherId, Integer executionPeriodId, String managementFunctionNote, String serviceExemptionNote,
            String otherNote, String masterDegreeTeachingNote, RoleType roleType) throws FenixServiceException, ExcepcaoPersistencia {    
        
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        
        if(teacherService != null) {
            teacherService.editNotes(managementFunctionNote, serviceExemptionNote, otherNote, masterDegreeTeachingNote, roleType);
        }
        
        return Boolean.TRUE;
    }    
}
