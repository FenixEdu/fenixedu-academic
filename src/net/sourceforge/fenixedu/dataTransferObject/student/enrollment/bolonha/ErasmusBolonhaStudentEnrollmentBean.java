package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplication;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;

public class ErasmusBolonhaStudentEnrollmentBean extends BolonhaStudentEnrollmentBean {
    
    public static class ErasmusExtraCurricularEnrolmentBean implements Serializable{
	private final CurricularCourse curricularCourse;
	private final Boolean enrol;
	
	public ErasmusExtraCurricularEnrolmentBean(CurricularCourse curricularCourse, Boolean enrol) {
	    this.curricularCourse = curricularCourse;
	    this.enrol = enrol;
	}

	public CurricularCourse getCurricularCourse() {
	    return curricularCourse;
	}

	public Boolean getEnrol() {
	    return enrol;
	}
    }
    
    private MobilityIndividualApplication candidacy;
    private List<ErasmusExtraCurricularEnrolmentBean> extraCurricularEnrolments = null;
    
    public ErasmusBolonhaStudentEnrollmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
	    int[] curricularYears, CurricularRuleLevel curricularRuleLevel, MobilityIndividualApplication candidacy) {
	super(studentCurricularPlan, executionSemester, curricularYears, curricularRuleLevel);
	setCandidacy(candidacy);
    }

    public void setCandidacy(MobilityIndividualApplication candidacy) {
	this.candidacy = candidacy;
    }

    public MobilityIndividualApplication getCandidacy() {
	return candidacy;
    }

    public void setExtraCurricularEnrolments(List<ErasmusExtraCurricularEnrolmentBean> extraCurricularEnrolments) {
	this.extraCurricularEnrolments = extraCurricularEnrolments;
    }

    public List<ErasmusExtraCurricularEnrolmentBean> getExtraCurricularEnrolments() {
	return extraCurricularEnrolments;
    }
}
