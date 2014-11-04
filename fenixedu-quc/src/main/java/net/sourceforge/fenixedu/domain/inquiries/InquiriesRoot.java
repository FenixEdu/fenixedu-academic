package org.fenixedu.academic.domain.inquiries;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Registration;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public final class InquiriesRoot extends InquiriesRoot_Base {
    public static final List<String> THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES = new ArrayList<String>();

    static {
        THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.add("deargf");
        THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.add("dec");
        THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.add("deft");
        THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.add("deic");
        THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.add("dmat");
        THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.add("cesidb");
    }

    private InquiriesRoot() {
        super();
        setRoot(Bennu.getInstance());
    }

    public static InquiriesRoot getInstance() {
        if (Bennu.getInstance().getInquiriesRoot() == null) {
            return initialize();
        }
        return Bennu.getInstance().getInquiriesRoot();
    }

    @Atomic(mode = TxMode.WRITE)
    private static InquiriesRoot initialize() {
        if (Bennu.getInstance().getInquiriesRoot() == null) {
            return new InquiriesRoot();
        }
        return Bennu.getInstance().getInquiriesRoot();
    }

    public static boolean isAvailableDegreeTypeForInquiries(Registration registration) {
        final DegreeType degreeType = registration.getDegreeType();
        return degreeType == DegreeType.BOLONHA_DEGREE
                || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE
                || degreeType == DegreeType.BOLONHA_MASTER_DEGREE
                || THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES
                        .contains(registration.getDegree().getSigla().toLowerCase());
    }

    public static boolean isMasterDegreeDFAOnly(ExecutionCourse executionCourse) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            DegreeType degreeType = curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType();
            if (!degreeType.equals(DegreeType.MASTER_DEGREE)
                    && !degreeType.equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)
                    && !degreeType.equals(DegreeType.BOLONHA_SPECIALIZATION_DEGREE)
                    && !THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.contains(curricularCourse.getDegreeCurricularPlan()
                            .getDegree().getSigla().toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBolonhaDegreeOrMasterDegreeOrIntegratedMasterDegree(ExecutionCourse executionCourse) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            DegreeType degreeType = curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType();
            if (degreeType.equals(DegreeType.BOLONHA_DEGREE) || degreeType.equals(DegreeType.BOLONHA_MASTER_DEGREE)
                    || degreeType.equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
                return true;
            }
            if (THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.contains(curricularCourse.getDegreeCurricularPlan()
                    .getDegree().getSigla().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean getAvailableForInquiries(ExecutionCourse executionCourse) {
        if (isBolonhaDegreeOrMasterDegreeOrIntegratedMasterDegree(executionCourse)) {
            return executionCourse.getAvailableForInquiries() != null;
        }
        return Boolean.FALSE;
    }

    public static boolean isAvailableForInquiry(ExecutionCourse executionCourse) {
        return getAvailableForInquiries(executionCourse) && executionCourse.hasEnrolmentsInAnyCurricularCourse()
                && !isMasterDegreeDFAOnly(executionCourse);
    }

}
