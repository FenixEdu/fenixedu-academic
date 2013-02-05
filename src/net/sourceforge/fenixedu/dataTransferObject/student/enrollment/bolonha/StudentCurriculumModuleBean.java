package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public abstract class StudentCurriculumModuleBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private CurriculumModule curriculumModule;

    public StudentCurriculumModuleBean(final CurriculumModule curriculumModule) {
        super();
        setCurriculumModule(curriculumModule);
    }

    public CurriculumModule getCurriculumModule() {
        return this.curriculumModule;
    }

    private void setCurriculumModule(CurriculumModule curriculumModule) {
        this.curriculumModule = curriculumModule;
    }

    protected StudentCurricularPlan getStudentCurricularPlan() {
        return curriculumModule.getStudentCurricularPlan();
    }

    protected Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }
}
