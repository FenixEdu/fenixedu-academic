package net.sourceforge.fenixedu.util.enrollment;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.util.FenixValuedEnum;

/**
 * @author dcs-rjao
 * 
 * 2/Abr/2003
 */
public class CurricularCourseEnrollmentType extends FenixValuedEnum implements Serializable {

    public static final int TEMPORARY_TYPE = 1;

    public static final int DEFINITIVE_TYPE = 2;

    public static final int NOT_ALLOWED_TYPE = 3;

    public static final int VALIDATED_TYPE = 4;

    public static final CurricularCourseEnrollmentType TEMPORARY = new CurricularCourseEnrollmentType(
            "curricularCourseEnrollmentType.temporary", CurricularCourseEnrollmentType.TEMPORARY_TYPE);

    public static final CurricularCourseEnrollmentType DEFINITIVE = new CurricularCourseEnrollmentType(
            "curricularCourseEnrollmentType.definitive", CurricularCourseEnrollmentType.DEFINITIVE_TYPE);

    public static final CurricularCourseEnrollmentType NOT_ALLOWED = new CurricularCourseEnrollmentType(
            "curricularCourseEnrollmentType.notAllowed", CurricularCourseEnrollmentType.NOT_ALLOWED_TYPE);

    public static final CurricularCourseEnrollmentType VALIDATED = new CurricularCourseEnrollmentType(
            "curricularCourseEnrollmentType.validated", CurricularCourseEnrollmentType.VALIDATED_TYPE);

    private CurricularCourseEnrollmentType(String name, int value) {
        super(name, value);
    }

    public static CurricularCourseEnrollmentType getEnum(String state) {
        return (CurricularCourseEnrollmentType) getEnum(CurricularCourseEnrollmentType.class, state);
    }

    public static CurricularCourseEnrollmentType getEnum(int state) {
        return (CurricularCourseEnrollmentType) getEnum(CurricularCourseEnrollmentType.class, state);
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
        String result = "Curricular Course Enrollment Type:\n";
        result += "\n  - Curricular Course Enrollment Type : " + this.getName();

        return result;
    }

    public CurricularCourseEnrollmentType and(CurricularCourseEnrollmentType other) {

        int myType = this.getValue();
        int othersType = other.getValue();

        if ((myType == NOT_ALLOWED_TYPE) && (othersType == NOT_ALLOWED_TYPE)) {
            return NOT_ALLOWED;
        } else if ((myType == NOT_ALLOWED_TYPE) && (othersType == TEMPORARY_TYPE)) {
            return NOT_ALLOWED;
        } else if ((myType == NOT_ALLOWED_TYPE) && (othersType == DEFINITIVE_TYPE)) {
            return NOT_ALLOWED;
        } else if ((myType == TEMPORARY_TYPE) && (othersType == NOT_ALLOWED_TYPE)) {
            return NOT_ALLOWED;
        } else if ((myType == TEMPORARY_TYPE) && (othersType == TEMPORARY_TYPE)) {
            return TEMPORARY;
        } else if ((myType == TEMPORARY_TYPE) && (othersType == DEFINITIVE_TYPE)) {
            return TEMPORARY;
        } else if ((myType == DEFINITIVE_TYPE) && (othersType == NOT_ALLOWED_TYPE)) {
            return NOT_ALLOWED;
        } else if ((myType == DEFINITIVE_TYPE) && (othersType == TEMPORARY_TYPE)) {
            return TEMPORARY;
        } else {
            return DEFINITIVE;
        }
    }

    public CurricularCourseEnrollmentType or(CurricularCourseEnrollmentType other) {

        int myType = this.getValue();
        int othersType = other.getValue();

        if ((myType == NOT_ALLOWED_TYPE) && (othersType == NOT_ALLOWED_TYPE)) {
            return NOT_ALLOWED;
        } else if ((myType == NOT_ALLOWED_TYPE) && (othersType == TEMPORARY_TYPE)) {
            return TEMPORARY;
        } else if ((myType == NOT_ALLOWED_TYPE) && (othersType == DEFINITIVE_TYPE)) {
            return DEFINITIVE;
        } else if ((myType == TEMPORARY_TYPE) && (othersType == NOT_ALLOWED_TYPE)) {
            return TEMPORARY;
        } else if ((myType == TEMPORARY_TYPE) && (othersType == TEMPORARY_TYPE)) {
            return TEMPORARY;
        } else if ((myType == TEMPORARY_TYPE) && (othersType == DEFINITIVE_TYPE)) {
            return DEFINITIVE;
        } else if ((myType == DEFINITIVE_TYPE) && (othersType == NOT_ALLOWED_TYPE)) {
            return DEFINITIVE;
        } else if ((myType == DEFINITIVE_TYPE) && (othersType == TEMPORARY_TYPE)) {
            return DEFINITIVE;
        } else {
            return DEFINITIVE;
        }
    }

}