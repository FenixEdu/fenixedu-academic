package net.sourceforge.fenixedu.domain.curriculum;


/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public enum CurricularCourseType {
    
    NORMAL_COURSE,
    
    OPTIONAL_COURSE,
    
    PROJECT_COURSE,
    
    TFC_COURSE,
    
    TRAINING_COURSE,
    
    LABORATORY_COURSE,
    
    M_TYPE_COURSE,
    
    P_TYPE_COURSE,
    
    DM_TYPE_COURSE,
    
    A_TYPE_COURSE,
    
    ML_TYPE_COURSE;
    
    public String getName() {
        return name();
    }
    
}
