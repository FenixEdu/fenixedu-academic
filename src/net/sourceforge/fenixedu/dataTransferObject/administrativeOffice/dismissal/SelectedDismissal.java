/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class SelectedDismissal implements Serializable {

    private CurriculumGroup curriculumGroup;

    private CurricularCourse curricularCourse;

    public SelectedDismissal(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        setCurriculumGroup(curriculumGroup);
        setCurricularCourse(curricularCourse);
    }

    public CurriculumGroup getCurriculumGroup() {
        return this.curriculumGroup;
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
        this.curriculumGroup = curriculumGroup;
    }

    public CurricularCourse getCurricularCourse() {
        return this.curricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public static String getKey(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        return CurriculumGroup.class.getName() + ":" + curriculumGroup.getIdInternal() + "," + CurricularCourse.class.getName()
                + ":" + curricularCourse.getIdInternal();
    }

    public boolean equals(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        return getCurriculumGroup() == curriculumGroup && getCurricularCourse() == curricularCourse;
    }
}