package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import net.sourceforge.fenixedu.domain.Enrolment;

public class StudentCurriculumEnrolmentBean extends StudentCurriculumModuleBean {

    static private final long serialVersionUID = 1L;

    public StudentCurriculumEnrolmentBean(final Enrolment enrolment) {
        super(enrolment);
    }

    @Override
    public Enrolment getCurriculumModule() {
        return (Enrolment) super.getCurriculumModule();
    }

}
