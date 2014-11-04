/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Oct 19, 2005
 *  by jdnf
 */
package org.fenixedu.academic.ui.faces.bean.coordinator.evaluation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.service.services.commons.ReadCurrentExecutionPeriod;
import org.fenixedu.academic.service.services.commons.ReadExecutionPeriodsByDegreeCurricularPlan;
import org.fenixedu.academic.service.services.coordinator.ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;

import pt.ist.fenixframework.FenixFramework;

public class CoordinatorEvaluationManagementBackingBean extends FenixBackingBean {

    private String degreeCurricularPlanID;
    private HtmlInputHidden degreeCurricularPlanIdHidden;
    private String executionPeriodID;
    private String curricularYearID;
    private HtmlInputHidden executionPeriodIdHidden;
    private HtmlInputHidden curricularYearIdHidden;
    private ExecutionSemester executionSemester;
    private String executionCourseID;
    private HtmlInputHidden executionCourseIdHidden;
    private List<ExecutionCourse> executionCourses;
    private List<SelectItem> executionPeriodsLabels;
    private List<SelectItem> curricularYearsLabels;
    private String evaluationID;
    private HtmlInputHidden evaluationIdHidden;
    protected Evaluation evaluation;
    private Integer day;
    private Integer month;
    private Integer year;
    private HtmlInputHidden dayHidden;
    private HtmlInputHidden monthHidden;
    private HtmlInputHidden yearHidden;
    private String evaluationType;
    private HtmlInputHidden evaluationTypeHidden;
    private ExecutionDegree executionDegree;

    public HtmlInputHidden getDegreeCurricularPlanIdHidden() {
        if (this.degreeCurricularPlanIdHidden == null) {
            final String degreeCurricularPlanId = this.getDegreeCurricularPlanID();
            this.degreeCurricularPlanIdHidden = new HtmlInputHidden();
            this.degreeCurricularPlanIdHidden.setValue(degreeCurricularPlanId);
        }
        return degreeCurricularPlanIdHidden;
    }

    public void setDegreeCurricularPlanIdHidden(HtmlInputHidden degreeCurricularPlanIdHidden) {
        if (degreeCurricularPlanIdHidden != null) {
            setDegreeCurricularPlanID(degreeCurricularPlanIdHidden.getValue().toString());
        }
        this.degreeCurricularPlanIdHidden = degreeCurricularPlanIdHidden;
    }

    public String getDegreeCurricularPlanID() {
        if (this.degreeCurricularPlanID == null) {
            if (this.degreeCurricularPlanIdHidden != null) {
                this.degreeCurricularPlanID = this.degreeCurricularPlanIdHidden.getValue().toString();
            } else if (this.getRequestParameter("degreeCurricularPlanID") != null) {
                this.degreeCurricularPlanID = this.getRequestParameter("degreeCurricularPlanID");
            } else if (this.getRequestAttribute("degreeCurricularPlanID") != null) {
                this.degreeCurricularPlanID = this.getRequestAttribute("degreeCurricularPlanID").toString();
            }
        }
        return degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(String degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public List<SelectItem> getExecutionPeriodsLabels() {
        if (this.executionPeriodsLabels == null) {
            this.executionPeriodsLabels = new ArrayList();

            final List<InfoExecutionPeriod> infoExecutionPeriods = getExecutionPeriods();
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator("infoExecutionYear.year")));
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator("semester")));
            Collections.sort(infoExecutionPeriods, comparatorChain);
            for (final InfoExecutionPeriod infoExecutionPeriod : infoExecutionPeriods) {
                final SelectItem selectItem = new SelectItem();
                selectItem.setValue(infoExecutionPeriod.getExternalId());
                selectItem.setLabel(infoExecutionPeriod.getName() + " - " + infoExecutionPeriod.getInfoExecutionYear().getYear());
                this.executionPeriodsLabels.add(selectItem);
            }
        }
        return this.executionPeriodsLabels;
    }

    protected List<InfoExecutionPeriod> getExecutionPeriods() {
        return ReadExecutionPeriodsByDegreeCurricularPlan.run(getDegreeCurricularPlanID());
    }

    public List<SelectItem> getCurricularYearsLabels() {
        if (this.curricularYearsLabels == null) {
            this.curricularYearsLabels = new ArrayList();
            this.curricularYearsLabels.add(new SelectItem(0, "Todos"));
            for (int i = 1; i <= 5; i++) {
                this.curricularYearsLabels.add(new SelectItem(i, i + " º"));
            }
        }
        return this.curricularYearsLabels;
    }

    public ExecutionCourse getExecutionCourse() throws FenixServiceException {
        return FenixFramework.getDomainObject(this.getExecutionCourseID());
    }

    protected List<ExecutionCourse> getExecutionCourses() {
        if (this.executionCourses != null) {
            return this.executionCourses;
        }
        try {
            this.executionCourses =
                    ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear
                            .runReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear(
                                    getDegreeCurricularPlanID(), getExecutionPeriodID(), getCurricularYearID());
            return this.executionCourses;
        } catch (NotAuthorizedException e) {
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        }
        return new ArrayList();
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionSemester == null ? FenixFramework.<ExecutionSemester> getDomainObject(getExecutionCourseID()) : this.executionSemester;
    }

    protected InfoExecutionPeriod getCurrentExecutionPeriod() {
        return ReadCurrentExecutionPeriod.run();
    }

    public List<SelectItem> getExecutionCoursesLabels() {
        final List<SelectItem> result = new ArrayList();
        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            result.add(new SelectItem(executionCourse.getExternalId(), executionCourse.getNome()));
        }
        Collections.sort(result, new BeanComparator("label"));
        return result;
    }

    public Evaluation getEvaluation() {
        try {
            if (this.evaluation == null && this.getEvaluationID() != null) {
                this.evaluation = FenixFramework.getDomainObject(this.getEvaluationID());
            }
            return this.evaluation;
        } catch (Exception e) {
            return null;
        }
    }

    public Date getCalendarBeginDate() {
        Date beginDate = getExecutionPeriod().getBeginDate();
        final ExecutionDegree executionDegree = getExecutionDegree();
        if (executionDegree != null) {
            if (getExecutionPeriod().getSemester().intValue() == 1
                    && executionDegree.getPeriodLessonsFirstSemester().getStart() != null) {
                beginDate = executionDegree.getPeriodLessonsFirstSemester().getStart();
            } else if (getExecutionPeriod().getSemester().intValue() == 2
                    && executionDegree.getPeriodLessonsSecondSemester().getStart() != null) {
                beginDate = executionDegree.getPeriodLessonsSecondSemester().getStart();
            }
        }
        return beginDate;
    }

    public Date getCalendarEndDate() {
        Date endDate = getExecutionPeriod().getEndDate();
        final ExecutionDegree executionDegree = getExecutionDegree();
        if (executionDegree != null) {
            if (getExecutionPeriod().getSemester().intValue() == 1
                    && executionDegree.getPeriodExamsFirstSemester().getEnd() != null) {
                endDate = executionDegree.getPeriodExamsFirstSemester().getEnd();
            } else if (getExecutionPeriod().getSemester().intValue() == 2
                    && executionDegree.getPeriodExamsSecondSemester().getEnd() != null) {
                endDate = executionDegree.getPeriodExamsSecondSemester().getEnd();
            }
        }
        return endDate;
    }

    private ExecutionDegree getExecutionDegree() {
        if (this.executionDegree == null) {
            for (final ExecutionDegree executionDegree : getDegreeCurricularPlan().getExecutionDegreesSet()) {
                if (executionDegree.getExecutionYear() == getExecutionPeriod().getExecutionYear()) {
                    return (this.executionDegree = executionDegree);
                }
            }
        }
        return this.executionDegree;
    }

    private DegreeCurricularPlan getDegreeCurricularPlan() {
        return FenixFramework.getDomainObject(getDegreeCurricularPlanID());
    }

    /**
     * It's necessary to put these attibutes in request for the next back bean
     */
    protected void setRequestCommonAttributes() {
        setRequestAttribute("degreeCurricularPlanID", getDegreeCurricularPlanID());
        setRequestAttribute("executionPeriodID", getExecutionPeriodID());
        setRequestAttribute("curricularYearID", getCurricularYearID());
    }

    public String selectExecutionCourse() {
        setRequestCommonAttributes();
        setRequestAttribute("executionCourseID", getExecutionCourseID());
        setRequestAttribute("day", getDay());
        setRequestAttribute("month", getMonth());
        setRequestAttribute("year", getYear());
        return getEvaluationType();
    }

    public String searchExecutionCourses() {
        clearAttributes();
        return "";
    }

    protected void clearAttributes() {
        this.executionCourses = null;
    }

    public String getExecutionCourseID() {
        if (this.executionCourseID == null) {
            if (this.getExecutionCourseIdHidden().getValue() != null) {
                this.executionCourseID = this.getExecutionCourseIdHidden().getValue().toString();
            } else if (this.getRequestParameter("executionCourseID") != null) {
                this.executionCourseID = this.getRequestParameter("executionCourseID");
            } else if (this.getRequestAttribute("executionCourseID") != null) {
                this.executionCourseID = this.getRequestAttribute("executionCourseID").toString();
            }
        }
        return this.executionCourseID;
    }

    public void setExecutionCourseID(String executionCourseID) {
        if (executionCourseID != null) {
            this.getExecutionCourseIdHidden().setValue(executionCourseID);
        }
        this.executionCourseID = executionCourseID;
    }

    public HtmlInputHidden getExecutionCourseIdHidden() {
        if (this.executionCourseIdHidden == null) {
            this.executionCourseIdHidden = new HtmlInputHidden();
            this.executionCourseIdHidden.setValue(this.getExecutionCourseID());
        }
        return executionCourseIdHidden;
    }

    public void setExecutionCourseIdHidden(HtmlInputHidden executionCourseIdHidden) {
        if (executionCourseIdHidden != null) {
            this.setExecutionCourseID(executionCourseIdHidden.getValue().toString());
        }
        this.executionCourseIdHidden = executionCourseIdHidden;
    }

    public String getExecutionPeriodID() {
        if (this.executionPeriodID == null) {
            if (this.getExecutionPeriodIdHidden().getValue() != null) {
                this.executionPeriodID = this.getExecutionPeriodIdHidden().getValue().toString();
            } else if (this.getRequestParameter("executionPeriodID") != null) {
                this.executionPeriodID = this.getRequestParameter("executionPeriodID");
            } else if (this.getRequestAttribute("executionPeriodID") != null) {
                this.executionPeriodID = this.getRequestAttribute("executionPeriodID").toString();
            } else {
                final InfoExecutionPeriod currentExecutionPeriod = getCurrentExecutionPeriod();
                this.executionPeriodID = (currentExecutionPeriod != null) ? currentExecutionPeriod.getExternalId() : null;
            }
        }
        return executionPeriodID;
    }

    public void setExecutionPeriodID(String executionPeriodID) {
        if (executionPeriodID != null) {
            getExecutionPeriodIdHidden().setValue(executionPeriodID);
        }
        this.executionPeriodID = executionPeriodID;
    }

    public String getCurricularYearID() {
        if (this.curricularYearID == null) {
            if (getCurricularYearIdHidden().getValue() != null) {
                this.curricularYearID = getCurricularYearIdHidden().getValue().toString();
            } else if (this.getRequestParameter("curricularYearID") != null) {
                this.curricularYearID = this.getRequestParameter("curricularYearID");
            } else if (this.getRequestAttribute("curricularYearID") != null) {
                this.curricularYearID = this.getRequestAttribute("curricularYearID").toString();
            } else {
                this.curricularYearID = null;
            }
        }
        return curricularYearID;
    }

    public void setCurricularYearID(String curricularYearID) {
        if (curricularYearID != null) {
            getCurricularYearIdHidden().setValue(curricularYearID);
        }
        this.curricularYearID = curricularYearID;
    }

    public HtmlInputHidden getExecutionPeriodIdHidden() {
        if (this.executionPeriodIdHidden == null) {
            this.executionPeriodIdHidden = new HtmlInputHidden();
            this.executionPeriodIdHidden.setValue(getExecutionPeriodID());
        }
        return executionPeriodIdHidden;
    }

    public void setExecutionPeriodIdHidden(HtmlInputHidden executionPeriodIdHidden) {
        if (executionPeriodIdHidden != null) {
            setExecutionPeriodID(executionPeriodIdHidden.getValue().toString());
        }
        this.executionPeriodIdHidden = executionPeriodIdHidden;
    }

    public HtmlInputHidden getCurricularYearIdHidden() {
        if (this.curricularYearIdHidden == null) {
            this.curricularYearIdHidden = new HtmlInputHidden();
            this.curricularYearIdHidden.setValue(getCurricularYearID());
        }
        return curricularYearIdHidden;
    }

    public void setCurricularYearIdHidden(HtmlInputHidden curricularYearIdHidden) {
        if (curricularYearIdHidden != null) {
            setCurricularYearID(curricularYearIdHidden.getValue().toString());
        }
        this.curricularYearIdHidden = curricularYearIdHidden;
    }

    public String getEvaluationID() {
        if (this.evaluationID == null) {
            if (this.getRequestParameter("evaluationID") != null) {
                setEvaluationID(this.getRequestParameter("evaluationID"));
            } else if (this.getEvaluationIdHidden().getValue() != null) {
                setEvaluationID(this.getEvaluationIdHidden().getValue().toString());
            }
        }
        return this.evaluationID;
    }

    public void setEvaluationID(String evaluationID) {
        this.evaluationID = evaluationID;
    }

    public HtmlInputHidden getEvaluationIdHidden() {
        if (this.evaluationIdHidden == null) {
            this.evaluationIdHidden = new HtmlInputHidden();
            this.evaluationIdHidden.setValue(getEvaluationID());
        }
        return evaluationIdHidden;
    }

    public void setEvaluationIdHidden(HtmlInputHidden evaluationIdHidden) {
        if (evaluationIdHidden != null) {
            setEvaluationID(evaluationIdHidden.getValue().toString());
        }
        this.evaluationIdHidden = evaluationIdHidden;
    }

    public Integer getDay() {
        if (this.day == null) {
            if (this.getEvaluation() != null) {
                setDay(((WrittenEvaluation) this.getEvaluation()).getDay().get(Calendar.DAY_OF_MONTH));
            } else if (this.getRequestParameter("day") != null) {
                setDay(Integer.valueOf(this.getRequestParameter("day")));
            } else if (this.getDayHidden().getValue() != null) {
                setDay(Integer.valueOf(this.getDayHidden().getValue().toString()));
            } else if (this.getRequestAttribute("day") != null) {
                setDay(Integer.valueOf(this.getRequestAttribute("day").toString()));
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
                setMonth(((WrittenEvaluation) this.getEvaluation()).getDay().get(Calendar.MONTH) + 1);
            } else if (this.getRequestParameter("month") != null) {
                setMonth(Integer.valueOf(this.getRequestParameter("month")));
            } else if (this.getMonthHidden().getValue() != null) {
                setMonth(Integer.valueOf(this.getMonthHidden().getValue().toString()));
            } else if (this.getRequestAttribute("month") != null) {
                setMonth(Integer.valueOf(this.getRequestAttribute("month").toString()));
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
                setYear(((WrittenEvaluation) this.getEvaluation()).getDay().get(Calendar.YEAR));
            } else if (this.getRequestParameter("year") != null) {
                setYear(Integer.valueOf(this.getRequestParameter("year")));
            } else if (this.getYearHidden().getValue() != null) {
                setYear(Integer.valueOf(this.getYearHidden().getValue().toString()));
            } else if (this.getRequestAttribute("year") != null) {
                setYear(Integer.valueOf(this.getRequestAttribute("year").toString()));
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

    public String getEvaluationType() {
        if (this.evaluationType == null) {
            if (this.getRequestParameter("evaluationType") != null && !this.getRequestParameter("evaluationType").equals("")) {
                this.evaluationType = this.getRequestParameter("evaluationType");
            }
        }
        return this.evaluationType;
    }

    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }

    public HtmlInputHidden getEvaluationTypeHidden() {
        if (this.evaluationTypeHidden == null) {
            this.evaluationTypeHidden = new HtmlInputHidden();
            this.evaluationTypeHidden.setValue(getEvaluationType());
        }
        return this.evaluationTypeHidden;
    }

    public void setEvaluationTypeHidden(HtmlInputHidden evaluationTypeHidden) {
        if (evaluationTypeHidden != null) {
            setEvaluationType(evaluationTypeHidden.getValue().toString());
        }
        this.evaluationTypeHidden = evaluationTypeHidden;
    }
}