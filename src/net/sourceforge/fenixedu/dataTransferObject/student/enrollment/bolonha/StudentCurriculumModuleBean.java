package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public abstract class StudentCurriculumModuleBean implements Serializable {

    private DomainReference<CurriculumModule> curriculumModule;
    
    public StudentCurriculumModuleBean(final CurriculumModule curriculumModule) {
	super();
	setCurriculumModule(curriculumModule);
    }

    public CurriculumModule getCurriculumModule() {
	return (this.curriculumModule != null) ? this.curriculumModule.getObject() : null;
    }

    private void setCurriculumModule(CurriculumModule curriculumModule) {
	this.curriculumModule = (curriculumModule != null) ? new DomainReference<CurriculumModule>(
		curriculumModule) : null;
    }

}
