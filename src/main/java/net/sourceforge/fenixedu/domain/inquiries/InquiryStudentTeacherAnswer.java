package net.sourceforge.fenixedu.domain.inquiries;

public class InquiryStudentTeacherAnswer extends InquiryStudentTeacherAnswer_Base {

    public InquiryStudentTeacherAnswer() {
        super();
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

    @Deprecated
    public boolean hasInquiryCourseAnswer() {
        return getInquiryCourseAnswer() != null;
    }

    @Deprecated
    public boolean hasShiftType() {
        return getShiftType() != null;
    }

}
