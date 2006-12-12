package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class TeacherPastService extends TeacherPastService_Base {
    
    public TeacherPastService(TeacherService teacherService, Double credits){
        super();
        if(teacherService == null || credits == null){
            throw new DomainException("arguments can't be null");
        }
        if(countPastServices(teacherService.getTeacher()) >= 1){            
            throw new DomainException("There is already one Past Service, there can't be more");
        }
        setTeacherService(teacherService);
        setCredits(credits);
    }
    
    @Override
    public void setCredits(Double credits) {
	if(credits == null) {
	    throw new DomainException("arguments can't be null");
	}
	super.setCredits(credits);
    }

    private int countPastServices(Teacher teacher) {
        int count = 0;
        for (TeacherService teacherService : teacher.getTeacherServices()) {
            if(teacherService.getPastService() != null){
                count++;
            }
        }
        return count;
    }
}
