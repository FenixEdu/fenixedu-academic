package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class StudentExtraEnrolmentBean extends StudentOptionalEnrolmentBean {
    
    private NoCourseGroupCurriculumGroupType groupType;
    
    public StudentExtraEnrolmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod, NoCourseGroupCurriculumGroupType groupType) {
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionPeriod);
	setGroupType(groupType);
    }
    

    public NoCourseGroupCurriculumGroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(NoCourseGroupCurriculumGroupType groupType) {
        this.groupType = groupType;
    }
    
    

}
