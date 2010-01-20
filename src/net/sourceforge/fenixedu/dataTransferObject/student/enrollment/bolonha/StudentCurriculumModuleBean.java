package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public abstract class StudentCurriculumModuleBean implements Serializable {

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

}
