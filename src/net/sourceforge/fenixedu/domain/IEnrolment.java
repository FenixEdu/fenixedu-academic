package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.util.enrollment.EnrollmentCondition;

import org.apache.ojb.broker.PersistenceBrokerAware;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public interface IEnrolment extends IDomainObject, PersistenceBrokerAware {
    public ICurricularCourse getCurricularCourse();

    public IStudentCurricularPlan getStudentCurricularPlan();

    public EnrollmentState getEnrollmentState();

    public IExecutionPeriod getExecutionPeriod();

    public EnrolmentEvaluationType getEnrolmentEvaluationType();

    public List getEvaluations();

    public Date getCreationDate();

    public EnrollmentCondition getCondition();

    public Integer getAccumulatedWeight();

    public void setEnrollmentState(EnrollmentState state);

    public void setCurricularCourse(ICurricularCourse curricularCourse);

    public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);

    public void setExecutionPeriod(IExecutionPeriod executionPeriod);

    public void setEnrolmentEvaluationType(EnrolmentEvaluationType type);

    public void setEvaluations(List list);

    public void setCreationDate(Date creationDate);

    public void setCondition(EnrollmentCondition condition);

    public void setAccumulatedWeight(Integer accumulatedWeight);

    public String getCreatedBy();

    public void setCreatedBy(String createdBy);

}