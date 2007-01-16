/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class SelectedDismissal implements Serializable {

    private DomainReference<CurriculumGroup> curriculumGroup;
    
    private DomainReference<CurricularCourse> curricularCourse;
    
    public SelectedDismissal(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        setCurriculumGroup(curriculumGroup);
        setCurricularCourse(curricularCourse);
    }
    
    public CurriculumGroup getCurriculumGroup() {
	return (this.curriculumGroup != null) ? this.curriculumGroup.getObject() : null;
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(curriculumGroup) : null;
    }

    public CurricularCourse getCurricularCourse() {
	return (this.curricularCourse != null) ? this.curricularCourse.getObject() : null;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
	this.curricularCourse = (curricularCourse != null) ? new DomainReference<CurricularCourse>(curricularCourse) : null;
    }
    
    public static String getKey(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        return CurriculumGroup.class.getName() + ":" + curriculumGroup.getIdInternal() + "," + CurricularCourse.class.getName() + ":" + curricularCourse.getIdInternal();
    }

    public boolean equals(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        return getCurriculumGroup() == curriculumGroup && getCurricularCourse() == curricularCourse;
    }
}