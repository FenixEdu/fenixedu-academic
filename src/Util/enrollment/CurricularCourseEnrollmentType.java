package Util.enrollment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Util.FenixValuedEnum;

/**
 * @author dcs-rjao
 * 
 * 2/Abr/2003
 */
public class CurricularCourseEnrollmentType extends FenixValuedEnum {

    public static final int TEMPORARY_TYPE = 1;

    public static final int DEFINITIVE_TYPE = 2;

    public static final int NOT_ALLOWED_TYPE = 3;

    public static final CurricularCourseEnrollmentType TEMPORARY = new CurricularCourseEnrollmentType(
            "msg.temporary", CurricularCourseEnrollmentType.TEMPORARY_TYPE);

    public static final CurricularCourseEnrollmentType DEFINITIVE = new CurricularCourseEnrollmentType(
            "msg.definitive", CurricularCourseEnrollmentType.DEFINITIVE_TYPE);

    public static final CurricularCourseEnrollmentType NOT_ALLOWED = new CurricularCourseEnrollmentType(
            "msg.notAllowed", CurricularCourseEnrollmentType.NOT_ALLOWED_TYPE);

    private CurricularCourseEnrollmentType(String name, int value) {
        super(name, value);
    }

    public static CurricularCourseEnrollmentType getEnum(String state) {
        return (CurricularCourseEnrollmentType) getEnum(
                CurricularCourseEnrollmentType.class, state);
    }

    public static CurricularCourseEnrollmentType getEnum(int state) {
        return (CurricularCourseEnrollmentType) getEnum(
                CurricularCourseEnrollmentType.class, state);
    }

    public static Map getEnumMap() {
        return getEnumMap(CurricularCourseEnrollmentType.class);
    }

    public static List getEnumList() {
        return getEnumList(CurricularCourseEnrollmentType.class);
    }

    public static Iterator iterator() {
        return iterator(CurricularCourseEnrollmentType.class);
    }

    public String toString() {
        return this.getName();
    }

}