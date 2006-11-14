package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CurriculumModuleBean implements Serializable{
    
    private List<CurriculumModuleBean> groupsEnroled;
    private List<CurriculumModuleBean> curricularCoursesEnroled;
    
    private List<DegreeModuleToEnrol> groupsToEnrol;
    private List<DegreeModuleToEnrol> curricularCoursesToEnrol;
    
    private DomainReference<CurriculumModule> curriculumModule;
    
    public List<CurriculumModuleBean> getCurricularCoursesEnroled() {
        return curricularCoursesEnroled;
    }
    
    public void setCurricularCoursesEnroled(List<CurriculumModuleBean> curricularCoursesEnroled) {
        this.curricularCoursesEnroled = curricularCoursesEnroled;
    }
    
    public List<DegreeModuleToEnrol> getCurricularCoursesToEnrol() {
        return curricularCoursesToEnrol;
    }
    
    public void setCurricularCoursesToEnrol(List<DegreeModuleToEnrol> curricularCoursesToEnrol) {
        this.curricularCoursesToEnrol = curricularCoursesToEnrol;
    }
    
    public List<CurriculumModuleBean> getGroupsEnroled() {
        return groupsEnroled;
    }
    
    public void setGroupsEnroled(List<CurriculumModuleBean> groupsEnroled) {
        this.groupsEnroled = groupsEnroled;
    }
    
    public List<DegreeModuleToEnrol> getGroupsToEnrol() {
        return groupsToEnrol;
    }
    
    public void setGroupsToEnrol(List<DegreeModuleToEnrol> groupsToEnrol) {
        this.groupsToEnrol = groupsToEnrol;
    }

    public CurriculumModule getCurriculumModule() {
	return (this.curriculumModule == null) ? null : this.curriculumModule.getObject();
    }

    public void setCurriculumModule(CurriculumModule curriculumModule) {
	this.curriculumModule = (curriculumModule != null) ? new DomainReference<CurriculumModule>(curriculumModule) : null;
    }

}
