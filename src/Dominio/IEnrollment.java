package Dominio;

import java.util.Date;
import java.util.List;

import Util.EnrolmentEvaluationType;
import Util.EnrollmentState;
import Util.enrollment.EnrollmentCondition;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public interface IEnrollment extends IDomainObject {
    public ICurricularCourse getCurricularCourse();

    public IStudentCurricularPlan getStudentCurricularPlan();

    public EnrollmentState getEnrolmentState();

    public IExecutionPeriod getExecutionPeriod();

    public EnrolmentEvaluationType getEnrolmentEvaluationType();

    public List getEvaluations();

    public Date getCreationDate();

    public EnrollmentCondition getCondition();

    public Integer getAccumulatedWeight();

    public void setEnrolmentState(EnrollmentState state);

    public void setCurricularCourse(ICurricularCourse curricularCourse);

    public void setStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan);

    public void setExecutionPeriod(IExecutionPeriod executionPeriod);

    public void setEnrolmentEvaluationType(EnrolmentEvaluationType type);

    public void setEvaluations(List list);

    public void setCreationDate(Date creationDate);

    public void setCondition(EnrollmentCondition condition);

    public void setAccumulatedWeight(Integer accumulatedWeight);

}