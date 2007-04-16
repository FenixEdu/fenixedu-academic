package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class TeacherServiceNotes extends TeacherServiceNotes_Base {
    
    public TeacherServiceNotes(TeacherService teacherService){
        super();
        if(teacherService == null){
            throw new DomainException("arguments can't be null");
        }
        if(teacherService.getTeacherServiceNotes() != null){            
            throw new DomainException("There is already one Teacher Service Notes, there can't be more");
        }
        setTeacherService(teacherService);        
    }        
    
    public void editNotes(String managementFunctionNote, String serviceExemptionNote,
            String otherNote, String masterDegreeTeachingNote, String functionsAccumulation,
            String thesisNote, RoleType roleType) {
        
        getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
        
        if(managementFunctionNote != null) {
            setManagementFunctionNotes(managementFunctionNote);
        }
        if(serviceExemptionNote != null) {
            setServiceExemptionNotes(serviceExemptionNote);
        }
        if(masterDegreeTeachingNote != null) {
            setMasterDegreeTeachingNotes(masterDegreeTeachingNote);
        }
        if(otherNote != null) {
            setOthersNotes(otherNote);                
        }
        if(functionsAccumulation != null) {
            setFunctionsAccumulation(functionsAccumulation);                
        }
        if(thesisNote != null) {
        	setThesisNote(thesisNote);
        }
    }
}
