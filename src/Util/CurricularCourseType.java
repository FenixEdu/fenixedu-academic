package Util;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class CurricularCourseType extends FenixUtil {

    public static final int NORMAL_COURSE = 1;

    public static final int OPTIONAL_COURSE = 2;

    public static final int PROJECT_COURSE = 3;

    public static final int TFC_COURSE = 4;

    public static final int TRAINING_COURSE = 5;

    public static final int LABORATORY_COURSE = 6;

    public static final int M_TYPE_COURSE = 7;

    public static final int P_TYPE_COURSE = 8;

    public static final int DM_TYPE_COURSE = 9;

    public static final int A_TYPE_COURSE = 10;

    public static final int ML_TYPE_COURSE = 11;

    public static final CurricularCourseType NORMAL_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.NORMAL_COURSE);

    public static final CurricularCourseType OPTIONAL_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.OPTIONAL_COURSE);

    public static final CurricularCourseType PROJECT_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.PROJECT_COURSE);

    public static final CurricularCourseType TFC_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.TFC_COURSE);

    public static final CurricularCourseType TRAINING_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.TRAINING_COURSE);

    public static final CurricularCourseType LABORATORY_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.LABORATORY_COURSE);

    public static final CurricularCourseType M_TYPE_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.M_TYPE_COURSE);

    public static final CurricularCourseType P_TYPE_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.P_TYPE_COURSE);

    public static final CurricularCourseType DM_TYPE_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.DM_TYPE_COURSE);

    public static final CurricularCourseType A_TYPE_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.A_TYPE_COURSE);

    public static final CurricularCourseType ML_TYPE_COURSE_OBJ = new CurricularCourseType(
            CurricularCourseType.ML_TYPE_COURSE);

    private Integer type;

    public CurricularCourseType() {
    }

    public CurricularCourseType(int type) {
        this.type = new Integer(type);
    }

    public CurricularCourseType(Integer type) {
        this.type = type;
    }

    public Integer getCurricularCourseType() {
        return this.type;
    }

    public void setCurricularCourseType(Integer type) {
        this.type = type;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof CurricularCourseType) {
            CurricularCourseType ds = (CurricularCourseType) obj;
            resultado = this.getCurricularCourseType().equals(ds.getCurricularCourseType());
        }
        return resultado;
    }

    //Key name in Application Resources file
    public String getKeyName() {

        int value = this.type.intValue();
        String keyName = null;

        switch (value) {
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

    public String toString() {

        int value = this.type.intValue();
        String valueS = null;

        switch (value) {
        case TFC_COURSE:
            valueS = "TFC_COURSE";
            break;
        case PROJECT_COURSE:
            valueS = "PROJECT_COURSE";
            break;
        case NORMAL_COURSE:
            valueS = "NORMAL_COURSE";
            break;
        case OPTIONAL_COURSE:
            valueS = "OPTIONAL_COURSE";
            break;
        case TRAINING_COURSE:
            valueS = "TRAINING_COURSE";
            break;
        case LABORATORY_COURSE:
            valueS = "LABORATORY_COURSE";
            break;
        case M_TYPE_COURSE:
            valueS = "M_TYPE_COURSE";
            break;
        case P_TYPE_COURSE:
            valueS = "P_TYPE_COURSE";
            break;
        case DM_TYPE_COURSE:
            valueS = "DM_TYPE_COURSE";
            break;
        case A_TYPE_COURSE:
            valueS = "A_TYPE_COURSE";
            break;
        case ML_TYPE_COURSE:
            valueS = "ML_TYPE_COURSE";
            break;

        default:
            break;
        }

        return "[" + this.getClass().getName() + ": " + valueS + "]";
    }

}