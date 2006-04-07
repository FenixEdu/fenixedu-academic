package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class OtherService extends OtherService_Base {
    
    public OtherService(TeacherService teacherService, Double credits, String reason) {
        super();
        if(teacherService == null || credits == null || reason == null){
            throw new DomainException("arguments can't be null");
        }
        
        setTeacherService(teacherService);
        setCredits(credits);
        setReason(reason);
    }
    
    public void delete(){
        removeTeacherService();
        super.delete();
    }
    
}
