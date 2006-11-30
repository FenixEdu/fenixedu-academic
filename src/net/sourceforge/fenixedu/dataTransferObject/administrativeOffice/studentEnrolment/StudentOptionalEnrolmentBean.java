package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class StudentOptionalEnrolmentBean implements Serializable{

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;
    private DomainReference<ExecutionPeriod> executionPeriod;
    private DomainReference<CurriculumGroup> curriculumGroup;
    private DomainReference<Context> context;
    private DegreeType degreeType;
    private DomainReference<Degree> degree;
    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;
    
    public StudentOptionalEnrolmentBean() {
	
    }
    
    public StudentOptionalEnrolmentBean(StudentCurricularPlan studentCurricularPlan, 
	    ExecutionPeriod executionPeriod, CurriculumGroup curriculumGroup, Context context) {
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionPeriod);
	setCurriculumGroup(curriculumGroup);
	setContext(context);
    }
    
    public StudentCurricularPlan getStudentCurricularPlan() {
        return (this.studentCurricularPlan == null) ? null : this.studentCurricularPlan.getObject();
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(studentCurricularPlan) : null; 
    }

    public ExecutionPeriod getExecutionPeriod() {
    	return (this.executionPeriod == null) ? null : this.executionPeriod.getObject();
    }
    
    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
    	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(executionPeriod) : null;
    }
    
    public CurriculumGroup getCurriculumGroup() {
    	return (this.curriculumGroup == null) ? null : this.curriculumGroup.getObject();
    }
    
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
    	this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(curriculumGroup) : null;
    }

    public Context getContex() {
    	return (this.context == null) ? null : this.context.getObject();
    }
    
    public void setContext(Context context) {
    	this.context = (context != null) ? new DomainReference<Context>(context) : null;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }
    
    public Degree getDegree() {
    	return (this.degree == null) ? null : this.degree.getObject();
    }
    
    public void setDegree(Degree degree) {
    	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }
    
    public DegreeCurricularPlan getDegreeCurricularPlan() {
    	return (this.degreeCurricularPlan == null) ? null : this.degreeCurricularPlan.getObject();
    }
    
    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
    	this.degreeCurricularPlan = (degreeCurricularPlan != null) ? new DomainReference<DegreeCurricularPlan>(degreeCurricularPlan) : null;
    }


}
