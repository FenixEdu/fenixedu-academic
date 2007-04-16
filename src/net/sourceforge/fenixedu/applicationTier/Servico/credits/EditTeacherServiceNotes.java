package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceNotes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.lang.StringUtils;

public class EditTeacherServiceNotes extends Service {

    public Boolean run(Integer teacherId, Integer executionPeriodId, String managementFunctionNote, String serviceExemptionNote,
            String otherNote, String masterDegreeTeachingNote, String functionsAccumulation, String thesisNote, RoleType roleType) 
            throws FenixServiceException, ExcepcaoPersistencia {    
        
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        
        if(teacherService == null) {
            teacherService = new TeacherService(teacher, executionPeriod);
        }
         
        TeacherServiceNotes teacherServiceNotes = teacherService.getTeacherServiceNotes();
        if(teacherServiceNotes == null) {
            teacherServiceNotes = new TeacherServiceNotes(teacherService);
        }
        
        teacherServiceNotes.editNotes(managementFunctionNote, serviceExemptionNote, otherNote, masterDegreeTeachingNote,
                functionsAccumulation, thesisNote, roleType);
        
        if(StringUtils.isEmpty(teacherServiceNotes.getManagementFunctionNotes()) && 
                StringUtils.isEmpty(teacherServiceNotes.getServiceExemptionNotes()) &&
                StringUtils.isEmpty(teacherServiceNotes.getOthersNotes()) &&
                StringUtils.isEmpty(teacherServiceNotes.getFunctionsAccumulation()) &&
                StringUtils.isEmpty(teacherServiceNotes.getMasterDegreeTeachingNotes()) &&
                StringUtils.isEmpty(teacherServiceNotes.getThesisNote())) {            
            teacherServiceNotes.delete();
        }
        
        return Boolean.TRUE;
    }    
}
