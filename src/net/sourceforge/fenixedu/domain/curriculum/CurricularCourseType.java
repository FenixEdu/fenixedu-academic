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
    
   public String getKeyName() {

        String keyName = null;

        switch (this) {
        case TFC_COURSE:
            keyName = "option.curricularCourse.tfc";
            break;
        case PROJECT_COURSE:
            keyName = "option.curricularCourse.project";
            break;
        case NORMAL_COURSE:
            keyName = "option.curricularCourse.normal";
            break;
        case OPTIONAL_COURSE:
            keyName = "option.curricularCourse.optional";
            break;
        case TRAINING_COURSE:
            keyName = "option.curricularCourse.training";
            break;
        case LABORATORY_COURSE:
            keyName = "option.curricularCourse.laboratory";
            break;
        case M_TYPE_COURSE:
            keyName = "option.curricularCourse.mType";
            break;
        case P_TYPE_COURSE:
            keyName = "option.curricularCourse.pType";
            break;
        case DM_TYPE_COURSE:
            keyName = "option.curricularCourse.dmType";
            break;
        case A_TYPE_COURSE:
            keyName = "option.curricularCourse.aType";
            break;
        case ML_TYPE_COURSE:
            keyName = "option.curricularCourse.mlType";
            break;

        default:
            break;
        }

        return keyName;
    }

}
