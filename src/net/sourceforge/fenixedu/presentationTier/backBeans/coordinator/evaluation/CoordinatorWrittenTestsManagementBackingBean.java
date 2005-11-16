/*
 * Created on Oct 26, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator.evaluation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;

public class CoordinatorWrittenTestsManagementBackingBean extends CoordinatorWrittenTestsInformationBackingBean {
    private Integer day;
    private Integer month;
    private Integer year;
    private Integer beginHour;
    private Integer beginMinute;
    private Integer endHour;
    private Integer endMinute;
    private String description;

    private HtmlInputHidden dayHidden;
    private HtmlInputHidden monthHidden;
    private HtmlInputHidden yearHidden;

    private IEvaluation evaluation;

    public List<SelectItem> getExecutionCoursesLabels() {
        final List<SelectItem> result = new ArrayList();
        for (final IExecutionCourse executionCourse : getExecutionCourses()) {
            result.add(new SelectItem(executionCourse.getIdInternal(), executionCourse.getNome()));
        }
        Collections.sort(result, new BeanComparator("label"));
        return result;
    }

    public String createWrittenTest() {
        try {
            final IExecutionCourse executionCourse = getExecutionCourse();
            if (executionCourse == null) {
                this.setErrorMessage("error.noExecutionCourse");
                return "";
            }
            final List<String> executionCourseIDs = new ArrayList<String>(1);
            executionCourseIDs.add(this.getExecutionCourseID().toString());
            final List<String> curricularCourseScopeIDs = getCurricularCourseScopeIDs(executionCourse);

            final Object[] args = { this.getExecutionCourseID(), this.getBegin(), this.getBegin(),
                    this.getEnd(), executionCourseIDs, curricularCourseScopeIDs, null, null,
                    this.getDescription() };
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
            final IExecutionCourse executionCourse = getExecutionCourse();
            if (executionCourse == null) {
                this.setErrorMessage("error.noExecutionCourse");
                return "";
            }
            final List<String> executionCourseIDs = new ArrayList<String>(1);
            executionCourseIDs.add(this.getExecutionCourseID().toString());
            final List<String> curricularCourseScopeIDs = getCurricularCourseScopeIDs(executionCourse);

            final Object[] args = { executionCourse.getIdInternal(), this.getBegin(), this.getBegin(),
                    this.getEnd(), executionCourseIDs, curricularCourseScopeIDs, null,
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

    public String selectExecutionCourse() {
        setRequestCommonAttributes();
        setRequestAttribute("executionCourseID", getExecutionCourseID());
        return "selectExecutionCourse";
    }

    public String showWrittenTestsForExecutionCourses() {
        setRequestCommonAttributes();
        return "showWrittenTestsForExecutionCourses";
    }

    private List<String> getCurricularCourseScopeIDs(final IExecutionCourse executionCourse) {
        final List<String> curricularCourseScopeIDs = new ArrayList<String>();
        for (final ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            for (final ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
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

    public IEvaluation getEvaluation() {
        try {
            if (this.evaluation == null && this.getEvaluationID() != null) {
                final Object[] args = { WrittenEvaluation.class, this.getEvaluationID() };
                this.evaluation = (IEvaluation) ServiceUtils.executeService(null, "ReadDomainObject",
                        args);
            }
            return this.evaluation;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Integer getBeginHour() {
        if (this.beginHour == null && this.getEvaluation() != null) {
            this.beginHour = ((IWrittenEvaluation) getEvaluation()).getBeginning().get(
                    Calendar.HOUR_OF_DAY);
        }
        return this.beginHour;
    }

    public void setBeginHour(Integer beginHour) {
        this.beginHour = beginHour;
    }

    public Integer getBeginMinute() {
        if (this.beginMinute == null && this.getEvaluation() != null) {
            this.beginMinute = ((IWrittenEvaluation) getEvaluation()).getBeginning()
                    .get(Calendar.MINUTE);
        }
        return this.beginMinute;
    }

    public void setBeginMinute(Integer beginMinute) {
        this.beginMinute = beginMinute;
    }

    public String getDescription() {
        if (this.description == null && this.getEvaluation() != null) {
            final IWrittenEvaluation writtenEvaluation = (IWrittenEvaluation) getEvaluation();
            if (writtenEvaluation instanceof IWrittenTest) {
                this.description = ((IWrittenTest) writtenEvaluation).getDescription();
            }
        }
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEndHour() {
        if (this.endHour == null && this.getEvaluation() != null) {
            this.endHour = ((IWrittenEvaluation) getEvaluation()).getEnd().get(Calendar.HOUR_OF_DAY);
        }
        return this.endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() {
        if (this.endMinute == null && this.getEvaluation() != null) {
            this.endMinute = ((IWrittenEvaluation) getEvaluation()).getEnd().get(Calendar.MINUTE);
        }
        return this.endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public Integer getDay() {
        if (this.day == null) {
            if (this.getEvaluation() != null) {
                setDay(((IWrittenEvaluation) this.getEvaluation()).getDay().get(Calendar.DAY_OF_MONTH));
            } else if (this.getRequestParameter("day") != null) {
                setDay(Integer.valueOf(this.getRequestParameter("day")));
            } else if (this.getDayHidden().getValue() != null) {
                setDay(Integer.valueOf(this.getDayHidden().getValue().toString()));
            }
        }
        return this.day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        if (this.month == null) {
            if (this.getEvaluation() != null) {
                setMonth(((IWrittenEvaluation) this.getEvaluation()).getDay().get(Calendar.MONTH) + 1);
            } else if (this.getRequestParameter("month") != null) {
                setMonth(Integer.valueOf(this.getRequestParameter("month")));
            } else if (this.getMonthHidden().getValue() != null) {
                setMonth(Integer.valueOf(this.getMonthHidden().getValue().toString()));
            }
        }
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        if (this.year == null) {
            if (this.getEvaluation() != null) {
                setYear(((IWrittenEvaluation) this.getEvaluation()).getDay().get(Calendar.YEAR));
            } else if (this.getRequestParameter("year") != null) {
                setYear(Integer.valueOf(this.getRequestParameter("year")));
            } else if (this.getYearHidden().getValue() != null) {
                setYear(Integer.valueOf(this.getYearHidden().getValue().toString()));
            }
        }
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
 
    public HtmlInputHidden getDayHidden() {
        if (this.dayHidden == null) {
            this.dayHidden = new HtmlInputHidden();
            this.dayHidden.setValue(this.getDay());
        }
        return this.dayHidden;
    }

    public void setDayHidden(HtmlInputHidden dayHidden) {
        this.dayHidden = dayHidden;
    }

    public HtmlInputHidden getMonthHidden() {
        if (this.monthHidden == null) {
            this.monthHidden = new HtmlInputHidden();
            this.monthHidden.setValue(this.getMonth());
        }
        return this.monthHidden;
    }

    public void setMonthHidden(HtmlInputHidden monthHidden) {
        this.monthHidden = monthHidden;
    }

    public HtmlInputHidden getYearHidden() {
        if (this.yearHidden == null) {
            this.yearHidden = new HtmlInputHidden();
            this.yearHidden.setValue(this.getYear());
        }
        return this.yearHidden;
    }

    public void setYearHidden(HtmlInputHidden yearHidden) {
        this.yearHidden = yearHidden;
    }    
}
