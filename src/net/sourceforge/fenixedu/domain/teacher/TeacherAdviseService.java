package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class TeacherAdviseService extends TeacherAdviseService_Base {
    
    public TeacherAdviseService(ITeacherService teacherService, IAdvise advise, Double percentage) {
        super();
        
        if(teacherService == null || advise == null || percentage == null){
            throw new DomainException("arguments can't be null");
        }
        
        checkPercentage(percentage);
        advise.checkPercentageCoherenceWithOtherAdvises(teacherService.getExecutionPeriod(),percentage.doubleValue());
        setTeacherService(teacherService);
        setPercentage(percentage); 
        setAdvise(advise);
    }
    
    public void delete(){
        removeAdvise();
        removeTeacherService();
        deleteDomainObject();
    }
    
    public void updatePercentage(Double percentage) {
        checkPercentage(percentage);
        getAdvise().checkPercentageCoherenceWithOtherAdvises(getTeacherService().getExecutionPeriod(),percentage.doubleValue());
        setPercentage(percentage);
    }
    
    private void checkPercentage(Double percentage) {
        if (percentage == null || percentage > 100 || percentage < 0) {
            throw new DomainException("error.invalid.advise.service.percentage");
        } 
    }    
}
