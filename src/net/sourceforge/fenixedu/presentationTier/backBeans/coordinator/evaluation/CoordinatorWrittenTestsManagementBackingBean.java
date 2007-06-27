/*
 * Created on Oct 26, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator.evaluation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

public class CoordinatorWrittenTestsManagementBackingBean extends
        CoordinatorWrittenTestsInformationBackingBean {
    
    private Integer beginHour;
    private Integer beginMinute;
    private Integer endHour;
    private Integer endMinute;
    private String description;   

    public String createWrittenTest() {
        try {
            final ExecutionCourse executionCourse = getExecutionCourse();
            if (executionCourse == null) {
                this.setErrorMessage("error.noExecutionCourse");
                return "";
            }
            final List<String> executionCourseIDs = new ArrayList<String>(1);
            executionCourseIDs.add(this.getExecutionCourseID().toString());
            final List<String> curricularCourseScopeIDs = getCurricularCourseScopeIDs(executionCourse);
            final List<String> curricularCourseContextIDs = new ArrayList<String>();

            final Object[] args = { this.getExecutionCourseID(), this.getBegin().getTime(), this.getBegin().getTime(),
                    this.getEnd().getTime(), executionCourseIDs, curricularCourseScopeIDs, curricularCourseContextIDs,
                    null, null, this.getDescription() };
            ServiceUtils.executeService(getUserView(), "CreateWrittenEvaluation", args);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }
        return this.showWrittenTestsForExecutionCourses();
    }

    public String editWrittenTest() {
        try {
            final ExecutionCourse executionCourse = getExecutionCourse();
            if (executionCourse == null) {
                this.setErrorMessage("error.noExecutionCourse");
                return "";
            }
            final List<String> executionCourseIDs = new ArrayList<String>(1);
            executionCourseIDs.add(this.getExecutionCourseID().toString());
            final List<String> curricularCourseScopeIDs = getCurricularCourseScopeIDs(executionCourse);
            List<String> curricularCourseContextIDs = new ArrayList<String>();

            final Object[] args = { executionCourse.getIdInternal(), this.getBegin().getTime(), this.getBegin().getTime(),
                    this.getEnd().getTime(), executionCourseIDs, curricularCourseScopeIDs, curricularCourseContextIDs, null,
                    this.getEvaluationID(), null, this.getDescription() };
            ServiceUtils.executeService(getUserView(), "EditWrittenEvaluation", args);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }
        return this.showWrittenTestsForExecutionCourses();
    }

    public String deleteWrittenTest() {
        try {
            final Object args[] = { this.getExecutionCourseID(), this.getEvaluationID() };
            ServiceUtils.executeService(getUserView(), "DeleteWrittenEvaluation", args);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }
        return this.showWrittenTestsForExecutionCourses();
    }

    public String showWrittenTestsForExecutionCourses() {
        setRequestCommonAttributes();
        return "showWrittenTestsForExecutionCourses";
    }

    private List<String> getCurricularCourseScopeIDs(final ExecutionCourse executionCourse) {
        final List<String> curricularCourseScopeIDs = new ArrayList<String>();
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                if (curricularCourseScope.getCurricularSemester().getSemester().equals(
                        executionCourse.getExecutionPeriod().getSemester())) {
                    curricularCourseScopeIDs.add(curricularCourseScope.getIdInternal().toString());
                }
            }
        }
        return curricularCourseScopeIDs;
    }

    private Calendar getBegin() {
        final Calendar result = Calendar.getInstance();
        result.set(this.getYear(), this.getMonth() - 1, this.getDay(), this.getBeginHour(), this
                .getBeginMinute());

        return result;
    }

    private Calendar getEnd() {
        final Calendar result = Calendar.getInstance();
        result.set(this.getYear(), this.getMonth() - 1, this.getDay(), this.getEndHour(), this
                .getEndMinute());

        return result;
    }

    public Integer getBeginHour() {
        if (this.beginHour == null && this.getEvaluation() != null) {
            this.beginHour = ((WrittenEvaluation) getEvaluation()).getBeginning().get(
                    Calendar.HOUR_OF_DAY);
        }
        return this.beginHour;
    }

    public void setBeginHour(Integer beginHour) {
        this.beginHour = beginHour;
    }

    public Integer getBeginMinute() {
        if (this.beginMinute == null && this.getEvaluation() != null) {
            this.beginMinute = ((WrittenEvaluation) getEvaluation()).getBeginning()
                    .get(Calendar.MINUTE);
        }
        return this.beginMinute;
    }

    public void setBeginMinute(Integer beginMinute) {
        this.beginMinute = beginMinute;
    }

    public String getDescription() {
        if (this.description == null && this.getEvaluation() != null) {
            final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) getEvaluation();
            if (writtenEvaluation instanceof WrittenTest) {
                this.description = ((WrittenTest) writtenEvaluation).getDescription();
            }
        }
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEndHour() {
        if (this.endHour == null && this.getEvaluation() != null) {
            this.endHour = ((WrittenEvaluation) getEvaluation()).getEnd().get(Calendar.HOUR_OF_DAY);
        }
        return this.endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() {
        if (this.endMinute == null && this.getEvaluation() != null) {
            this.endMinute = ((WrittenEvaluation) getEvaluation()).getEnd().get(Calendar.MINUTE);
        }
        return this.endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }    
}
