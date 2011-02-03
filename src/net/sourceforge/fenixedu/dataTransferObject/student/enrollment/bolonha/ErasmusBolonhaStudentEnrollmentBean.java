package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacy;
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
    
    private ErasmusIndividualCandidacy candidacy;
    private List<ErasmusExtraCurricularEnrolmentBean> extraCurricularEnrolments = null;
    
    public ErasmusBolonhaStudentEnrollmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
	    int[] curricularYears, CurricularRuleLevel curricularRuleLevel, ErasmusIndividualCandidacy candidacy) {
	super(studentCurricularPlan, executionSemester, curricularYears, curricularRuleLevel);
	setCandidacy(candidacy);
    }

    public void setCandidacy(ErasmusIndividualCandidacy candidacy) {
	this.candidacy = candidacy;
    }

    public ErasmusIndividualCandidacy getCandidacy() {
	return candidacy;
    }

    public void setExtraCurricularEnrolments(List<ErasmusExtraCurricularEnrolmentBean> extraCurricularEnrolments) {
	this.extraCurricularEnrolments = extraCurricularEnrolments;
    }

    public List<ErasmusExtraCurricularEnrolmentBean> getExtraCurricularEnrolments() {
	return extraCurricularEnrolments;
    }
}
