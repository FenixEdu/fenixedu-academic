package net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation;

import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class EvaluationManagementBackingBean extends FenixBackingBean {

    protected Integer executionCourseID;

    private HtmlInputHidden executionCourseIdHidden;

    protected Integer evaluationID;

    private HtmlInputHidden evaluationIdHidden;

    protected Integer enrolmentBeginDay;

    protected Integer enrolmentBeginMonth;

    protected Integer enrolmentBeginYear;

    protected Integer enrolmentBeginHour;

    protected Integer enrolmentBeginMinute;

    protected Integer enrolmentEndDay;

    protected Integer enrolmentEndMonth;

    protected Integer enrolmentEndYear;

    protected Integer enrolmentEndHour;

    protected Integer enrolmentEndMinute;

    public Integer getExecutionCourseID() {
        if (this.executionCourseID == null) {
            if (this.executionCourseIdHidden != null) {
                this.executionCourseID = Integer.valueOf(this.executionCourseIdHidden.getValue()
                        .toString());
            } else {
                this.executionCourseID = Integer.valueOf(this.getRequestParameter("executionCourseID"));
            }
        }
        return this.executionCourseID;
    }

    public void setExecutionCourseID(Integer executionCourseId) {
        this.executionCourseID = executionCourseId;
    }

    public HtmlInputHidden getExecutionCourseIdHidden() {
        if (this.executionCourseIdHidden == null) {
            Integer executionCourseId = this.getExecutionCourseID();

            this.executionCourseIdHidden = new HtmlInputHidden();
            this.executionCourseIdHidden.setValue(executionCourseId);
        }
        return this.executionCourseIdHidden;
    }

    public void setExecutionCourseIdHidden(HtmlInputHidden executionCourseIdHidden) {
        if (executionCourseIdHidden != null) {
            this.executionCourseID = Integer.valueOf(executionCourseIdHidden.getValue().toString());
        }
        this.executionCourseIdHidden = executionCourseIdHidden;
    }

    public Integer getEvaluationID() {
        if (this.evaluationID == null) {
            if (this.evaluationIdHidden != null) {
                this.evaluationID = Integer.valueOf(this.evaluationIdHidden.getValue().toString());
            } else {
                this.evaluationID = Integer.valueOf(this.getRequestParameter("evaluationID"));
            }
        }
        return this.evaluationID;
    }

    public void setEvaluationID(Integer evaluationId) {
        this.evaluationID = evaluationId;
    }

    public HtmlInputHidden getEvaluationIdHidden() {
        if (this.evaluationIdHidden == null) {
            Integer evaluationId = this.getEvaluationID();

            this.evaluationIdHidden = new HtmlInputHidden();
            this.evaluationIdHidden.setValue(evaluationId);
        }

        return this.evaluationIdHidden;
    }

    public void setEvaluationIdHidden(HtmlInputHidden evaluationIdHidden) {
        if (evaluationIdHidden != null) {
            this.evaluationID = Integer.valueOf(evaluationIdHidden.getValue().toString());
        }

        this.evaluationIdHidden = evaluationIdHidden;
    }

    public Integer getEnrolmentBeginDay() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginDay == null && this.getEvaluation().getEnrollmentBeginDay() != null) {
            this.enrolmentBeginDay = this.getEvaluation().getEnrollmentBeginDay().get(Calendar.DAY_OF_MONTH);
        }
        return this.enrolmentBeginDay;
    }

    public void setEnrolmentBeginDay(Integer enrolmentBeginDay) {
        this.enrolmentBeginDay = enrolmentBeginDay;
    }

    public Integer getEnrolmentBeginHour() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginHour == null && this.getEvaluation().getEnrollmentBeginTime() != null) {
            this.enrolmentBeginHour = this.getEvaluation().getEnrollmentBeginTime().get(Calendar.HOUR_OF_DAY);
        }
        return this.enrolmentBeginHour;
    }

    public void setEnrolmentBeginHour(Integer enrolmentBeginHour) {
        this.enrolmentBeginHour = enrolmentBeginHour;
    }

    public Integer getEnrolmentBeginMinute() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginMinute == null && this.getEvaluation().getEnrollmentBeginTime() != null) {
            this.enrolmentBeginMinute = this.getEvaluation().getEnrollmentBeginTime().get(Calendar.MINUTE);
        }
        return this.enrolmentBeginMinute;
    }

    public void setEnrolmentBeginMinute(Integer enrolmentBeginMinute) {
        this.enrolmentBeginMinute = enrolmentBeginMinute;
    }

    public Integer getEnrolmentBeginMonth() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginMonth == null && this.getEvaluation().getEnrollmentBeginDay() != null) {
            this.enrolmentBeginMonth = this.getEvaluation().getEnrollmentBeginDay().get(Calendar.MONTH) + 1;
        }       
        return enrolmentBeginMonth;
    }

    public void setEnrolmentBeginMonth(Integer enrolmentBeginMonth) {
        this.enrolmentBeginMonth = enrolmentBeginMonth;
    }

    public Integer getEnrolmentBeginYear() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginYear == null && this.getEvaluation().getEnrollmentBeginDay() != null) {
            this.enrolmentBeginYear = this.getEvaluation().getEnrollmentBeginDay().get(Calendar.YEAR);
        }       
        return enrolmentBeginYear;
    }

    public void setEnrolmentBeginYear(Integer enrolmentBeginYear) {
        this.enrolmentBeginYear = enrolmentBeginYear;
    }

    public Integer getEnrolmentEndDay() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndDay == null && this.getEvaluation().getEnrollmentEndDay() != null) {
            this.enrolmentEndDay = this.getEvaluation().getEnrollmentEndDay().get(Calendar.DAY_OF_MONTH);
        }
        return this.enrolmentEndDay;
    }

    public void setEnrolmentEndDay(Integer enrolmentEndDay) {
        this.enrolmentEndDay = enrolmentEndDay;
    }

    public Integer getEnrolmentEndHour() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndHour == null && this.getEvaluation().getEnrollmentEndTime() != null) {
            this.enrolmentEndHour = this.getEvaluation().getEnrollmentEndTime().get(Calendar.HOUR_OF_DAY);
        }
        return this.enrolmentEndHour;
    }

    public void setEnrolmentEndHour(Integer enrolmentEndHour) {
        this.enrolmentEndHour = enrolmentEndHour;
    }

    public Integer getEnrolmentEndMinute() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndMinute == null && this.getEvaluation().getEnrollmentEndTime() != null) {
            this.enrolmentEndMinute = this.getEvaluation().getEnrollmentEndTime().get(Calendar.MINUTE);
        }
        return this.enrolmentEndMinute;
    }

    public void setEnrolmentEndMinute(Integer enrolmentEndMinute) {
        this.enrolmentEndMinute = enrolmentEndMinute;
    }

    public Integer getEnrolmentEndMonth() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndMonth == null && this.getEvaluation().getEnrollmentEndDay() != null) {
            this.enrolmentEndMonth = this.getEvaluation().getEnrollmentEndDay().get(Calendar.MONTH) + 1;
        }       
        return enrolmentEndMonth;
    }

    public void setEnrolmentEndMonth(Integer enrolmentEndMonth) {
        this.enrolmentEndMonth = enrolmentEndMonth;
    }

    public Integer getEnrolmentEndYear() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndYear == null && this.getEvaluation().getEnrollmentEndDay() != null) {
            this.enrolmentEndYear = this.getEvaluation().getEnrollmentEndDay().get(Calendar.YEAR);
        }       
        return enrolmentEndYear;
    }

    public void setEnrolmentEndYear(Integer enrolmentEndYear) {
        this.enrolmentEndYear = enrolmentEndYear;
    }

    public List<IExam> getExamList() {
        final Object[] args = { ExecutionCourse.class, this.getExecutionCourseID() };
        IExecutionCourse executionCourse = null;

        try {
            executionCourse = (IExecutionCourse) ServiceUtils.executeService(null, "ReadDomainObject",
                    args);
        } catch (FenixFilterException e) {
            throw new RuntimeException(e);
        } catch (FenixServiceException e) {
            throw new RuntimeException(e);
        }

        if (executionCourse == null) {
            throw new RuntimeException();
        }

        return executionCourse.getAssociatedExams();
    }

    public IWrittenEvaluation getEvaluation() throws FenixFilterException, FenixServiceException {
        if (this.getEvaluationID() != null) {
            final Object[] args = { WrittenEvaluation.class, this.getEvaluationID() };
            
            return (IWrittenEvaluation) ServiceUtils.executeService(null, "ReadDomainObject", args);
        }
        return null;
    }

    private Calendar getEnrolmentBegin() throws FenixFilterException, FenixServiceException {
        Calendar result = Calendar.getInstance();

        result.set(getEnrolmentBeginYear(), getEnrolmentBeginMonth() - 1, getEnrolmentBeginDay(),
                getEnrolmentBeginHour(), getEnrolmentBeginMinute());

        return result;
    }

    private Calendar getEnrolmentEnd() throws FenixFilterException, FenixServiceException {
        Calendar result = Calendar.getInstance();

        result.set(getEnrolmentEndYear(), getEnrolmentEndMonth() - 1, getEnrolmentEndDay(),
                getEnrolmentEndHour(), getEnrolmentEndMinute());

        return result;
    }

    public String editEvaluationEnrolmentPeriod() throws FenixFilterException, FenixServiceException {
        Calendar enrolmentBeginDay = getEnrolmentBegin();
        Calendar enrolmentBeginTime = getEnrolmentBegin();
        Calendar enrolmentEndDay = getEnrolmentEnd();
        Calendar enrolmentEndTime = getEnrolmentEnd();

        final Object[] args = { getExecutionCourseID(), getEvaluationID(), enrolmentBeginDay,
                enrolmentEndDay, enrolmentBeginTime, enrolmentEndTime };

        try {
            ServiceUtils.executeService(getUserView(), "EditWrittenEvaluationEnrolmentPeriod", args);
        } catch (Exception e) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            
            return "";
        }

        return "backToExamsIndex";
    }

    public String getEvaluationDescription() throws FenixFilterException, FenixServiceException {
        final IWrittenEvaluation writtenEvaluation = getEvaluation();
        if (writtenEvaluation instanceof IExam) {
            return ((IExam) writtenEvaluation).getSeason().toString();
        } else if (writtenEvaluation instanceof IWrittenTest) {
            return ((IWrittenTest) writtenEvaluation).getDescription();
        }
        return "";
    }

}
