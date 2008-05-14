/*
 * Created on Oct 11, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author nmgo
 * @author lmre
 */
public class InfoCurriculumHistoricReport implements Serializable {

    int evaluated = 0;

    int approved = 0;

    Collection<InfoEnrolmentHistoricReport> enrolments;
    
    DomainReference<CurricularCourse> curricularCourse;

    DomainReference<ExecutionSemester> executionSemester;

    public Integer getApproved() {
        return approved;
    }

    public Integer getEvaluated() {
        return evaluated;
    }
    
    public Collection<InfoEnrolmentHistoricReport> getEnrolments() {
        return enrolments;
    }

    public Integer getEnroled() {
        return getEnrolments().size();
    }

    public Integer getRatioApprovedEnroled() {
	return Math.round(((float) getApproved() / (float) getEnroled()) * 100);
    }

    public Integer getRatioApprovedEvaluated() {
	return Math.round(((float) getApproved() / (float) getEvaluated()) * 100);
    }

    public CurricularCourse getCurricularCourse() {
	return this.curricularCourse == null ?  null : this.curricularCourse.getObject();
    }

    private void setCurricularCourse(final CurricularCourse curricularCourse) {
	this.curricularCourse = (curricularCourse == null) ? null : new DomainReference<CurricularCourse>(curricularCourse);

    }

    public ExecutionSemester getExecutionPeriod() {
	return this.executionSemester == null ?  null : this.executionSemester.getObject();
    }

    private void setExecutionPeriod(final ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester == null) ? null : new DomainReference<ExecutionSemester>(executionSemester);

    }

    public Integer getSemester() {
        return getExecutionPeriod().getSemester();
    }

    public ExecutionYear getExecutionYear() {
	return getExecutionPeriod().getExecutionYear();
    }
    
    public InfoCurriculumHistoricReport(final ExecutionSemester executionSemester, final CurricularCourse curricularCourse) {
	setExecutionPeriod(executionSemester);
	setCurricularCourse(curricularCourse);
	
	init();
    }

    private void init() {
	this.enrolments = new TreeSet<InfoEnrolmentHistoricReport>(new BeanComparator("enrolment.studentCurricularPlan.registration.number"));
	for (final Enrolment enrolment : getCurricularCourse().getEnrolmentsByExecutionPeriod(getExecutionPeriod())) {
	    if (!enrolment.isAnnulled()) {
		this.enrolments.add(new InfoEnrolmentHistoricReport(enrolment));
		
		if (enrolment.isEvaluated()) {
		    this.evaluated++;
		    
		    if (enrolment.isEnrolmentStateApproved()) {
			this.approved++;
		    }
		}
	    }
	}
    }

}
