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
package org.fenixedu.academic.ui.faces.bean.sop.evaluation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.MessageResources;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularCourseScope;
import org.fenixedu.academic.domain.CurricularCourseScope.DegreeModuleScopeCurricularCourseScope;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.OccupationPeriod;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.WrittenTest;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.Context.DegreeModuleScopeContext;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.InfoRoom;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.resourceAllocationManager.DefineExamComment;
import org.fenixedu.academic.service.services.resourceAllocationManager.exams.CreateWrittenEvaluation;
import org.fenixedu.academic.service.services.resourceAllocationManager.exams.EditWrittenEvaluation;
import org.fenixedu.academic.ui.faces.bean.teacher.evaluation.EvaluationManagementBackingBean;
import org.fenixedu.academic.ui.faces.components.util.CalendarLink;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DateFormatUtil;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.academic.util.Season;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.FenixFramework;

public class SOPEvaluationManagementBackingBean extends EvaluationManagementBackingBean {

    private static final MessageResources messages = MessageResources.getMessageResources(Bundle.RESOURCE_ALLOCATION);

    private String academicInterval;
    protected HtmlInputHidden academicIntervalHidden;
    protected boolean disableDropDown;
    protected String executionDegreeID;
    protected HtmlInputHidden executionDegreeIdHidden;
    protected Integer curricularYearID;
    protected HtmlInputHidden curricularYearIdHidden;
    protected String curricularYearIDsParameterString;
    protected Integer calendarPeriod;
    protected HtmlInputHidden calendarPeriodHidden;
    private Integer[] curricularYearIDs;
    private final String chooseMessage = messages.getMessage(I18N.getLocale(), "label.choose.message");
    private HtmlInputHidden dayHidden;
    private HtmlInputHidden monthHidden;
    private HtmlInputHidden yearHidden;
    private HtmlInputHidden beginHourHidden;
    private HtmlInputHidden beginMinuteHidden;
    private HtmlInputHidden endHourHidden;
    private HtmlInputHidden endMinuteHidden;
    private Integer orderCriteria;
    private final String labelVacancies = messages.getMessage(I18N.getLocale(), "label.vacancies");
    private List<String> associatedExecutionCourses;

    private Map<String, String> associatedExecutionCoursesNames = new HashMap<String, String>();
    private Map<String, List<SelectItem>> curricularCourseScopesSelectItems = new HashMap<String, List<SelectItem>>();
    private Map<String, List<SelectItem>> curricularCourseContextSelectItems = new HashMap<String, List<SelectItem>>();
    private final Map<String, List<WrittenEvaluation>> writtenEvaluations = new HashMap<String, List<WrittenEvaluation>>();;
    private final Map<String, Integer> writtenEvaluationsMissingPlaces = new HashMap<String, Integer>();
    private final Map<String, String> writtenEvaluationsRooms = new HashMap<String, String>();
    private final Map<String, Integer> executionCoursesEnroledStudents = new HashMap<String, Integer>();

    private String comment;

    // BEGIN academicInterval
    public String getAcademicInterval() {
        hackBecauseJSFareReallyReallyReallyGreatButWeDontKnowAtWhat();
        if (this.academicInterval == null && this.academicIntervalHidden.getValue() != null
                && !this.academicIntervalHidden.getValue().equals("")) {
            this.academicInterval = this.academicIntervalHidden.getValue().toString();
        } else if (this.getRequestAttribute("academicInterval") != null
                && !this.getRequestAttribute("academicInterval").equals("")) {
            this.academicInterval = this.getRequestAttribute("academicInterval").toString();
        } else if (this.getRequestParameter("academicInterval") != null
                && !this.getRequestParameter("academicInterval").equals("")) {
            this.academicInterval = this.getRequestParameter("academicInterval");
            // } else if (this.academicInterval == null) {
            // this.academicInterval = getAcademicIntervalOID();
        }
        return academicInterval;
    }

    private void hackBecauseJSFareReallyReallyReallyGreatButWeDontKnowAtWhat() {
        AcademicInterval academicInterval = null;
        if (getRequestAttribute(PresentationConstants.ACADEMIC_INTERVAL) != null) {
            String academicIntervalStr = (String) getRequestAttribute(PresentationConstants.ACADEMIC_INTERVAL);
            academicInterval = AcademicInterval.getAcademicIntervalFromResumedString(academicIntervalStr);
        } else if (getRequestParameter(PresentationConstants.ACADEMIC_INTERVAL) != null) {
            String academicIntervalStr = getRequestParameter(PresentationConstants.ACADEMIC_INTERVAL);
            if (academicIntervalStr != null && !academicIntervalStr.equals("null")) {
                final String academicIntervalStrArg =
                        academicIntervalStr.indexOf('-') > 0 ? academicIntervalStr.replaceAll("-", "_") : academicIntervalStr;
                academicInterval = AcademicInterval.getAcademicIntervalFromResumedString(academicIntervalStrArg);
            }
        }
        if (academicInterval == null) {
            academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
        }
        setRequestAttribute(PresentationConstants.ACADEMIC_INTERVAL, academicInterval.getResumedRepresentationInStringFormat());
    }

    public String getAcademicIntervalEscapeFriendly() {
        return getAcademicInterval().replaceAll("_", "-");
    }

    public void setAcademicInterval(String academicInterval) {
        if (academicInterval != null) {
            this.academicIntervalHidden.setValue(academicInterval);
        }
        this.academicInterval = academicInterval;
    }

    public HtmlInputHidden getAcademicIntervalHidden() {
        if (this.academicIntervalHidden == null) {
            this.academicIntervalHidden = new HtmlInputHidden();
            this.academicIntervalHidden.setValue(this.getAcademicInterval());
        }
        return academicIntervalHidden;
    }

    public void setAcademicIntervalHidden(HtmlInputHidden academicIntervalHidden) {
        if (academicIntervalHidden.getValue() != null && !((String) academicIntervalHidden.getValue()).isEmpty()) {
            this.academicInterval = academicIntervalHidden.getValue().toString();
        }
        this.academicIntervalHidden = academicIntervalHidden;
    }

    public String getAcademicIntervalLabel() {
        return getAcademicIntervalFromParameter(getAcademicInterval()).getPathName();
    }

    // END executionPeriod

    // BEGIN disableDropDown
    public boolean getDisableDropDown() {
        if (getAcademicInterval() == null || getAcademicInterval().isEmpty()) {
            this.disableDropDown = Boolean.TRUE;
        } else {
            this.disableDropDown = Boolean.FALSE;
        }
        return disableDropDown;
    }

    public void setDisableDropDown(boolean disableDropDown) {
        this.disableDropDown = disableDropDown;
    }

    // END disableDropDown

    // BEGIN executionDegree
    public String getExecutionDegreeID() {
        if (this.executionDegreeID == null && this.executionDegreeIdHidden.getValue() != null
                && !this.executionDegreeIdHidden.getValue().equals("")) {
            this.executionDegreeID = this.executionDegreeIdHidden.getValue().toString();
        } else if (this.getRequestAttribute("executionDegreeID") != null
                && !this.getRequestAttribute("executionDegreeID").equals("")) {
            this.executionDegreeID = this.getRequestAttribute("executionDegreeID").toString();
        } else if (this.getRequestParameter("executionDegreeID") != null
                && !this.getRequestParameter("executionDegreeID").equals("")) {
            this.executionDegreeID = this.getRequestParameter("executionDegreeID");
        }
        return executionDegreeID;
    }

    public void setExecutionDegreeID(String executionDegreeID) {
        if (executionDegreeID != null) {
            this.executionDegreeIdHidden = new HtmlInputHidden();
            this.executionDegreeIdHidden.setValue(executionDegreeID);
        }
        this.executionDegreeID = executionDegreeID;
    }

    public HtmlInputHidden getExecutionDegreeIdHidden() {
        if (this.executionDegreeIdHidden == null) {
            this.executionDegreeIdHidden = new HtmlInputHidden();
            this.executionDegreeIdHidden.setValue(this.getExecutionDegreeID());
        }
        return executionDegreeIdHidden;
    }

    public void setExecutionDegreeIdHidden(HtmlInputHidden executionDegreeIdHidden) {
        if (executionDegreeIdHidden != null && executionDegreeIdHidden.getValue() != null
                && !executionDegreeIdHidden.getValue().equals("")) {
            this.executionDegreeID = executionDegreeIdHidden.getValue().toString();
        }
        this.executionDegreeIdHidden = executionDegreeIdHidden;
    }

    public ExecutionDegree getExecutionDegree() {
        return FenixFramework.getDomainObject(this.getExecutionDegreeID());
    }

    public String getExecutionDegreeLabel() {
        return getExecutionDegree().getDegree().getPresentationName(getExecutionDegree().getExecutionYear());
    }

    public Integer getCurricularYearID() {
        if (this.curricularYearID == null && this.curricularYearIdHidden.getValue() != null
                && !this.curricularYearIdHidden.getValue().equals("")) {
            this.curricularYearID = Integer.valueOf(this.curricularYearIdHidden.getValue().toString());
        } else if (this.getRequestAttribute("curricularYearID") != null
                && !this.getRequestAttribute("curricularYearID").equals("")) {
            this.curricularYearID = Integer.valueOf(this.getRequestAttribute("curricularYearID").toString());
        } else if (this.getRequestParameter("curricularYearID") != null
                && !this.getRequestParameter("curricularYearID").equals("")) {
            this.curricularYearID = Integer.valueOf(this.getRequestParameter("curricularYearID"));
        }
        return curricularYearID;
    }

    public Integer getCalendarPeriod() {
        if (this.calendarPeriod == null && this.getCalendarPeriodHidden().getValue() != null
                && !this.calendarPeriodHidden.getValue().equals("")) {
            this.calendarPeriod = Integer.valueOf(this.getCalendarPeriodHidden().getValue().toString());
        } else if (this.getRequestAttribute("calendarPeriod") != null && !this.getRequestAttribute("calendarPeriod").equals("")) {
            this.calendarPeriod = Integer.valueOf(this.getRequestAttribute("calendarPeriod").toString());
        } else if (this.getRequestParameter("calendarPeriod") != null && !this.getRequestParameter("calendarPeriod").equals("")) {
            this.calendarPeriod = Integer.valueOf(this.getRequestParameter("calendarPeriod"));
        }
        return calendarPeriod;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        if (curricularYearID != null) {
            this.curricularYearIdHidden = new HtmlInputHidden();
            this.curricularYearIdHidden.setValue(curricularYearID);
        }
        this.curricularYearID = curricularYearID;
    }

    public void setCalendarPeriod(Integer calendarPeriod) {
        if (calendarPeriod != null) {
            this.calendarPeriodHidden = new HtmlInputHidden();
            this.calendarPeriodHidden.setValue(calendarPeriod);
        }
        this.calendarPeriod = calendarPeriod;
    }

    public HtmlInputHidden getCurricularYearIdHidden() {
        if (this.curricularYearIdHidden == null) {
            this.curricularYearIdHidden = new HtmlInputHidden();
            this.curricularYearIdHidden.setValue(this.getCurricularYearID());
        }
        return curricularYearIdHidden;
    }

    public HtmlInputHidden getCalendarPeriodHidden() {
        if (this.calendarPeriodHidden == null) {
            this.calendarPeriodHidden = new HtmlInputHidden();
            this.calendarPeriodHidden.setValue(this.getCalendarPeriod());
        }
        return calendarPeriodHidden;
    }

    public void setCurricularYearIdHidden(HtmlInputHidden curricularYearIdHidden) {
        if (curricularYearIdHidden != null && curricularYearIdHidden.getValue() != null
                && !curricularYearIdHidden.getValue().equals("")) {
            this.curricularYearID = Integer.valueOf(curricularYearIdHidden.getValue().toString());
        }
        this.curricularYearIdHidden = curricularYearIdHidden;
    }

    public void setCalendarPeriodHidden(HtmlInputHidden calendarPeriodHidden) {
        if (calendarPeriodHidden != null && calendarPeriodHidden.getValue() != null
                && !calendarPeriodHidden.getValue().equals("")) {
            this.calendarPeriod = Integer.valueOf(calendarPeriodHidden.getValue().toString());
        }
        this.calendarPeriodHidden = calendarPeriodHidden;
    }

    public String getCurricularYear() {
        return this.getCurricularYearItems().get(getCurricularYearID()).getLabel();
    }

    // END curricularYear

    // BEGIN day, month, year, hour, minute
    public HtmlInputHidden getDayHidden() throws FenixServiceException {
        if (this.dayHidden == null) {
            this.dayHidden = new HtmlInputHidden();
            this.dayHidden.setValue(this.getDay());
        }
        return dayHidden;
    }

    public void setDayHidden(HtmlInputHidden dayHidden) {
        if (dayHidden.getValue() != null) {
            final String dayValue = dayHidden.getValue().toString();
            if (dayValue.length() > 0) {
                this.day = Integer.valueOf(dayValue);
            }
        }
        this.dayHidden = dayHidden;
    }

    public HtmlInputHidden getMonthHidden() throws FenixServiceException {
        if (this.monthHidden == null) {
            this.monthHidden = new HtmlInputHidden();
            this.monthHidden.setValue(this.getMonth());
        }
        return monthHidden;
    }

    public void setMonthHidden(HtmlInputHidden monthHidden) {
        if (monthHidden.getValue() != null) {
            final String monthValue = monthHidden.getValue().toString();
            if (monthValue.length() > 0) {
                this.month = Integer.valueOf(monthValue);
            }
        }
        this.monthHidden = monthHidden;
    }

    public HtmlInputHidden getYearHidden() throws FenixServiceException {
        if (this.yearHidden == null) {
            this.yearHidden = new HtmlInputHidden();
            this.yearHidden.setValue(this.getYear());
        }
        return yearHidden;
    }

    public void setYearHidden(HtmlInputHidden yearHidden) {
        if (yearHidden.getValue() != null) {
            final String yearValue = yearHidden.getValue().toString();
            if (yearValue.length() > 0) {
                this.year = Integer.valueOf(yearValue);
            }
        }
        this.yearHidden = yearHidden;
    }

    public HtmlInputHidden getBeginHourHidden() throws FenixServiceException {
        if (this.beginHourHidden == null) {
            this.beginHourHidden = new HtmlInputHidden();
            this.beginHourHidden.setValue(this.getBeginHour());
        }
        return beginHourHidden;
    }

    public void setBeginHourHidden(HtmlInputHidden beginHourHidden) {
        if (beginHourHidden.getValue() != null) {
            this.beginHour = Integer.valueOf(beginHourHidden.getValue().toString());
        }
        this.beginHourHidden = beginHourHidden;
    }

    public HtmlInputHidden getBeginMinuteHidden() throws FenixServiceException {
        if (this.beginMinuteHidden == null) {
            this.beginMinuteHidden = new HtmlInputHidden();
            this.beginMinuteHidden.setValue(this.getBeginMinute());
        }
        return beginMinuteHidden;
    }

    public void setBeginMinuteHidden(HtmlInputHidden beginMinuteHidden) {
        if (beginMinuteHidden.getValue() != null) {
            this.beginMinute = Integer.valueOf(beginMinuteHidden.getValue().toString());
        }
        this.beginMinuteHidden = beginMinuteHidden;
    }

    public HtmlInputHidden getEndHourHidden() throws FenixServiceException {
        if (this.endHourHidden == null) {
            this.endHourHidden = new HtmlInputHidden();
            this.endHourHidden.setValue(this.getEndHour());
        }
        return endHourHidden;
    }

    public void setEndHourHidden(HtmlInputHidden endHourHidden) {
        if (endHourHidden.getValue() != null) {
            this.endHour = Integer.valueOf(endHourHidden.getValue().toString());
        }
        this.endHourHidden = endHourHidden;
    }

    public HtmlInputHidden getEndMinuteHidden() throws FenixServiceException {
        if (this.endMinuteHidden == null) {
            this.endMinuteHidden = new HtmlInputHidden();
            this.endMinuteHidden.setValue(this.getEndMinute());
        }
        return endMinuteHidden;
    }

    public void setEndMinuteHidden(HtmlInputHidden endMinuteHidden) {
        if (endMinuteHidden.getValue() != null) {
            this.endMinute = Integer.valueOf(endMinuteHidden.getValue().toString());
        }
        this.endMinuteHidden = endMinuteHidden;
    }

    // END day, month, year, hour, minute

    // BEGIN Drop down menu logic
    public List<SelectItem> getAcademicIntervals() throws FenixServiceException {
        List<AcademicInterval> academicIntervals = AcademicInterval.readActiveAcademicIntervals(AcademicPeriod.SEMESTER);
        Collections.sort(academicIntervals, AcademicInterval.COMPARATOR_BY_BEGIN_DATE);

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(0, this.chooseMessage));
        for (AcademicInterval academicInterval : academicIntervals) {
            String label = academicInterval.getPathName();
            result.add(new SelectItem(academicInterval.getResumedRepresentationInStringFormat(), label));
        }

        return result;
    }

    public void enableDropDowns(ValueChangeEvent valueChangeEvent) {
        this.setAcademicInterval(valueChangeEvent.getNewValue().toString());

        if (StringUtils.isEmpty((String) valueChangeEvent.getNewValue())) {
            this.setDisableDropDown(true);
        } else {
            this.setDisableDropDown(false);
        }

        this.setExecutionDegreeID(null);
        this.setCurricularYearID(0);

        return;
    }

    public List<SelectItem> getExecutionDegrees() throws FenixServiceException {
        if (this.getDisableDropDown()) {
            return new ArrayList<SelectItem>();
        }

        List<ExecutionDegree> degrees =
                ExecutionDegree.filterByAcademicInterval(getAcademicIntervalFromParameter(getAcademicInterval()));

        return degrees
                .stream()
                .sorted(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME)
                .map(executionDegree -> new SelectItem(executionDegree.getExternalId(), executionDegree.getDegree().getPresentationName(
                        executionDegree.getExecutionYear()))).collect(Collectors.toList());
    }

    private AcademicInterval getAcademicIntervalFromParameter(String academicInterval) {
        if (academicInterval.contains("-")) {
            return AcademicInterval.getAcademicIntervalFromResumedString(academicInterval.replaceAll("-", ":"));
        }
        return AcademicInterval.getAcademicIntervalFromResumedString(academicInterval);
    }

    public List<SelectItem> getCurricularYearItems() {
        if (this.getDisableDropDown()) {
            return new ArrayList<SelectItem>();
        }

        List<SelectItem> curricularYearItems = new ArrayList<SelectItem>(6);
        curricularYearItems.add(new SelectItem(1, messages.getMessage(I18N.getLocale(), "label.year.first")));
        curricularYearItems.add(new SelectItem(2, messages.getMessage(I18N.getLocale(), "label.year.second")));
        curricularYearItems.add(new SelectItem(3, messages.getMessage(I18N.getLocale(), "label.year.third")));
        curricularYearItems.add(new SelectItem(4, messages.getMessage(I18N.getLocale(), "label.year.fourth")));
        curricularYearItems.add(new SelectItem(5, messages.getMessage(I18N.getLocale(), "label.year.fifth")));

        return curricularYearItems;
    }

    public List<SelectItem> getCalendarPeriodItems() {
        if (this.getDisableDropDown()) {
            return new ArrayList<SelectItem>();
        }

        List<SelectItem> calendarPeriodItems = new ArrayList<SelectItem>(7);

        calendarPeriodItems.add(new SelectItem(0, messages.getMessage(I18N.getLocale(), "label.calendarPeriodItem.all")));
        calendarPeriodItems
                .add(new SelectItem(1, messages.getMessage(I18N.getLocale(), "label.calendarPeriodItem.lesson.period")));
        calendarPeriodItems.add(new SelectItem(2, messages.getMessage(I18N.getLocale(), "label.calendarPeriodItem.exam.period")));

        return calendarPeriodItems;
    }

    public void setNewValueExecutionDegreeID(ValueChangeEvent valueChangeEvent) {
        this.setExecutionDegreeID((String) valueChangeEvent.getNewValue());
    }

    public void setNewValueCurricularYearID(ValueChangeEvent valueChangeEvent) {
        this.setCurricularYearID((Integer) valueChangeEvent.getNewValue());
    }

    public void setNewValueCurricularYearIDs(ValueChangeEvent valueChangeEvent) {
        this.setCurricularYearIDs((Integer[]) valueChangeEvent.getNewValue());
    }

    public Integer[] getCurricularYearIDParameters() {
        final String curricularYearIDsParameterString = getRequestParameter("curricularYearIDsParameterString");
        if (curricularYearIDsParameterString != null && curricularYearIDsParameterString.length() > 0
                && !curricularYearIDsParameterString.equals("null")) {
            final String[] curricularYearIDsParameterStringTokens = curricularYearIDsParameterString.split(",");
            final Integer[] curricularYearIDParameters = new Integer[curricularYearIDsParameterStringTokens.length];
            for (int i = 0; i < curricularYearIDParameters.length; i++) {
                curricularYearIDParameters[i] = Integer.valueOf(curricularYearIDsParameterStringTokens[i]);
            }
            return curricularYearIDParameters;
        }
        return null;
    }

    public String getCurricularYearIDsParameterString() {
        final Integer[] curricularYearIDs = getCurricularYearIDs();
        if (curricularYearIDs != null && curricularYearIDs.length > 0) {
            buildString(curricularYearIDs);
        }
        return curricularYearIDsParameterString;
    }

    private void buildString(final Integer[] curricularYearIDs) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < curricularYearIDs.length; i++) {
            if (i > 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(curricularYearIDs[i].toString());
        }
        this.curricularYearIDsParameterString = stringBuilder.toString();
    }

    public void setCurricularYearIDsParameterString(String curricularYearIDsParameterString) {
        this.curricularYearIDsParameterString = curricularYearIDsParameterString;
    }

    public Integer[] getCurricularYearIDs() {
        if (curricularYearIDs == null) {
            curricularYearIDs = (Integer[]) getViewState().getAttribute("curricularYearIDs");
        }
        if (curricularYearIDs == null) {
            curricularYearIDs = getCurricularYearIDParameters();
        }
        return curricularYearIDs;
    }

    public void setCurricularYearIDs(Integer[] curricularYearIDs) {
        this.curricularYearIDs = curricularYearIDs;
        getViewState().setAttribute("curricularYearIDs", curricularYearIDs);
    }

    public void setNewValueCalendarPeriod(ValueChangeEvent valueChangeEvent) {
        this.setCalendarPeriod((Integer) valueChangeEvent.getNewValue());
    }

    public void setNewValueExecutionCourseID(ValueChangeEvent valueChangeEvent) {
        this.setExecutionCourseID((String) valueChangeEvent.getNewValue());
    }

    public boolean getRenderContextSelection() {
        if (this.getAcademicInterval() == null || this.getExecutionDegreeID() == null || this.getCurricularYearIDs() == null
                || this.getCurricularYearIDs().length <= 0) {
            return false;
        }
        return true;
    }

    public Date[] getWrittenEvaluationsCalendarBegin() {
        final Integer calendarPeriod = getCalendarPeriod();
        final ExecutionDegree executionDegree = getExecutionDegree();

        final int resultSize = calendarPeriod == null || executionDegree == null ? 1 : 2;
        final Date[] result = new Date[resultSize];

        if (calendarPeriod == null || executionDegree == null) {
            result[0] = getAcademicIntervalFromParameter(getAcademicInterval()).getStart().toDate();
        } else {
            int semester =
                    AcademicInterval.getCardinalityOfAcademicInterval(AcademicInterval
                            .getAcademicIntervalFromResumedString(getAcademicInterval()));

            if (calendarPeriod.intValue() == 0) {
                final OccupationPeriod examPeriod = executionDegree.getPeriodExamsSpecialSeason();
                result[0] = getAcademicIntervalFromParameter(getAcademicInterval()).getStart().toDate();
                if (semester == 1 && examPeriod != null) {
                    result[1] = examPeriod.getStart();
                }
            } else if (calendarPeriod.intValue() == 1) {
                final OccupationPeriod occupationPeriod =
                        semester == 1 ? executionDegree.getPeriodLessonsFirstSemester() : executionDegree
                                .getPeriodLessonsSecondSemester();
                result[0] = occupationPeriod.getStart();
            } else if (calendarPeriod.intValue() == 2) {
                final OccupationPeriod occupationPeriod =
                        semester == 1 ? executionDegree.getPeriodExamsFirstSemester() : executionDegree
                                .getPeriodExamsSecondSemester();
                final OccupationPeriod examPeriod = executionDegree.getPeriodExamsSpecialSeason();
                result[0] = occupationPeriod.getStart();
                result[1] = examPeriod != null ? examPeriod.getStart() : null;
            }
        }

        return result;
    }

    public Date[] getWrittenEvaluationsCalendarEnd() {
        final Integer calendarPeriod = getCalendarPeriod();
        final ExecutionDegree executionDegree = getExecutionDegree();

        final int resultSize = calendarPeriod == null || executionDegree == null ? 1 : 2;
        final Date[] result = new Date[resultSize];

        if (calendarPeriod == null || executionDegree == null) {
            result[0] = getAcademicIntervalFromParameter(getAcademicInterval()).getEnd().toDate();
        } else {
            int semester =
                    AcademicInterval.getCardinalityOfAcademicInterval(AcademicInterval
                            .getAcademicIntervalFromResumedString(getAcademicInterval()));

            if (calendarPeriod.intValue() == 0) {
                final OccupationPeriod examPeriod = executionDegree.getPeriodExamsSpecialSeason();
                result[0] = getAcademicIntervalFromParameter(getAcademicInterval()).getEnd().toDate();
                if (semester == 1 && examPeriod != null) {
                    result[1] = examPeriod.getEnd();
                }
            } else if (calendarPeriod.intValue() == 1) {
                final OccupationPeriod occupationPeriod =
                        semester == 1 ? executionDegree.getPeriodLessonsFirstSemester() : executionDegree
                                .getPeriodLessonsSecondSemester();
                result[0] = occupationPeriod.getLastOccupationPeriodOfNestedPeriods().getEnd();
            } else if (calendarPeriod.intValue() == 2) {
                final OccupationPeriod occupationPeriod =
                        semester == 1 ? executionDegree.getPeriodExamsFirstSemester() : executionDegree
                                .getPeriodExamsSecondSemester();
                final OccupationPeriod examPeriod = executionDegree.getPeriodExamsSpecialSeason();
                result[0] = occupationPeriod.getEnd();
                result[1] = examPeriod != null ? examPeriod.getEnd() : null;
            }
        }

        return result;
    }

    public List<CalendarLink> getWrittenTestsCalendarLink() throws FenixServiceException {

        List<CalendarLink> result = new ArrayList<CalendarLink>();
        Integer[] curricularYearIDs = getCurricularYearIDs();

        if (curricularYearIDs != null && getExecutionDegree() != null) {
            List<Integer> curricularYears = Arrays.asList(getCurricularYearIDs());
            DegreeCurricularPlan degreeCurricularPlan = getExecutionDegree().getDegreeCurricularPlan();
            for (final ExecutionCourse executionCourse : getExecutionCourses()) {
                for (final Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
                    if (evaluation instanceof WrittenEvaluation) {
                        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
                        if (writtenEvaluation.hasScopeOrContextFor(curricularYears, degreeCurricularPlan)) {
                            final CalendarLink calendarLink =
                                    new CalendarLink(executionCourse, writtenEvaluation, I18N.getLocale());
                            calendarLink.setLinkParameters(constructLinkParameters(executionCourse, writtenEvaluation));
                            result.add(calendarLink);
                        }
                    }
                }
            }
        }

        return result;
    }

    private Map<String, String> constructLinkParameters(final ExecutionCourse executionCourse,
            final WrittenEvaluation writtenEvaluation) {
        final Map<String, String> linkParameters = new HashMap<String, String>();
        linkParameters.put("executionCourseID", executionCourse.getExternalId().toString());
        linkParameters.put("evaluationID", writtenEvaluation.getExternalId().toString());
        linkParameters.put("academicInterval", this.academicInterval);
        linkParameters.put("executionDegreeID", this.executionDegreeID.toString());
        linkParameters.put("curricularYearIDsParameterString", getCurricularYearIDsParameterString());
        linkParameters.put("evaluationTypeClassname", writtenEvaluation.getClass().getName());
        return linkParameters;
    }

    private List<ExecutionCourse> getExecutionCourses() throws FenixServiceException {
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        Integer[] curricularYears = getCurricularYearIDs();
        if (curricularYears != null) {
            for (final Integer curricularYearID : curricularYears) {
                executionCourses.addAll(ExecutionCourse.filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(
                        getAcademicIntervalFromParameter(getAcademicInterval()), getExecutionDegree().getDegreeCurricularPlan(),
                        CurricularYear.readByYear(curricularYearID), "%"));
            }
        }
        // Integer[] curricularYears = getCurricularYearIDs();
        // if (curricularYears != null) {
        // for (final Integer curricularYearID : curricularYears) {
        // final Object args[] = {
        // this.getExecutionDegree().getDegreeCurricularPlan().getExternalId(),
        // this.getExecutionPeriodID(), curricularYearID };
        // executionCourses.addAll((Collection<ExecutionCourse>)
        // ServiceManagerServiceFactory.executeService(
        // "ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear"
        // , args));
        // }
        // }
        Collections.sort(executionCourses, new BeanComparator("sigla"));
        return executionCourses;
    }

    public List<ExecutionCourseWrittenEvaluationAgregationBean> getExecutionCourseWrittenEvaluationAgregationBeans()
            throws FenixServiceException {

        final List<ExecutionCourseWrittenEvaluationAgregationBean> executionCourseWrittenEvaluationAgregationBean =
                new ArrayList<ExecutionCourseWrittenEvaluationAgregationBean>();
        final Integer[] curricularYears = getCurricularYearIDs();

        if (curricularYears != null && this.getExecutionDegree() != null) {
            final DegreeCurricularPlan degreeCurricularPlan = this.getExecutionDegree().getDegreeCurricularPlan();
            for (final Integer curricularYearID : curricularYears) {
                // final Object args[] = { degreeCurricularPlan.getExternalId(),
                // this.getExecutionPeriodID(), curricularYearID };
                // final Collection<ExecutionCourse> executionCourses =
                // (Collection<ExecutionCourse>) ServiceManagerServiceFactory
                // .executeService(
                // "ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear"
                // , args);
                final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
                executionCourses.addAll(ExecutionCourse.filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(
                        getAcademicIntervalFromParameter(getAcademicInterval()), getExecutionDegree().getDegreeCurricularPlan(),
                        CurricularYear.readByYear(curricularYearID), "%"));

                for (final ExecutionCourse executionCourse : executionCourses) {
                    final Set<WrittenEvaluation> writtenEvaluations =
                            new TreeSet<WrittenEvaluation>(WrittenEvaluation.COMPARATOR_BY_BEGIN_DATE);
                    for (final Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
                        if (evaluation instanceof WrittenEvaluation) {
                            final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
                            for (final DegreeModuleScope scope : writtenEvaluation.getDegreeModuleScopes()) {
                                if (scope.getCurricularYear().intValue() == curricularYearID.intValue()
                                        && degreeCurricularPlan == scope.getCurricularCourse().getDegreeCurricularPlan()) {
                                    writtenEvaluations.add(writtenEvaluation);
                                }
                            }
                        }
                    }
                    if (!writtenEvaluations.isEmpty()) {
                        executionCourseWrittenEvaluationAgregationBean.add(new ExecutionCourseWrittenEvaluationAgregationBean(
                                curricularYearID, executionCourse, writtenEvaluations));
                    }
                }
            }
        }
        Collections.sort(executionCourseWrittenEvaluationAgregationBean,
                ExecutionCourseWrittenEvaluationAgregationBean.COMPARATOR_BY_EXECUTION_COURSE_CODE_AND_CURRICULAR_YEAR);
        return executionCourseWrittenEvaluationAgregationBean;
    }

    public List<ExecutionCourse> getExecutionCoursesWithWrittenEvaluations() throws FenixServiceException {

        writtenEvaluations.clear();
        writtenEvaluationsMissingPlaces.clear();
        writtenEvaluationsRooms.clear();
        executionCoursesEnroledStudents.clear();

        List<ExecutionCourse> executionCoursesWithWrittenEvaluations = new ArrayList<ExecutionCourse>();
        Integer[] curricularYearIDs = getCurricularYearIDs();

        if (curricularYearIDs != null && getExecutionDegree() != null) {

            DegreeCurricularPlan degreeCurricularPlan = getExecutionDegree().getDegreeCurricularPlan();
            List<Integer> curricularYears = Arrays.asList(curricularYearIDs);

            for (final ExecutionCourse executionCourse : getExecutionCourses()) {

                List<WrittenEvaluation> executionCourseWrittenEvaluations = new ArrayList<WrittenEvaluation>();
                for (WrittenEvaluation writtenEvaluation : executionCourse.getAssociatedWrittenEvaluations()) {
                    if (writtenEvaluation.hasScopeOrContextFor(curricularYears, degreeCurricularPlan)) {
                        executionCourseWrittenEvaluations.add(writtenEvaluation);
                    }
                }

                if (!executionCourseWrittenEvaluations.isEmpty()) {
                    Collections.sort(executionCourseWrittenEvaluations, WrittenEvaluation.COMPARATOR_BY_BEGIN_DATE);
                    processWrittenTestAdditionalValues(executionCourse, executionCourseWrittenEvaluations);
                    writtenEvaluations.put(executionCourse.getExternalId(), executionCourseWrittenEvaluations);
                    executionCoursesWithWrittenEvaluations.add(executionCourse);
                }
            }
        }
        return executionCoursesWithWrittenEvaluations;
    }

    private void processWrittenTestAdditionalValues(final ExecutionCourse executionCourse,
            final List<WrittenEvaluation> associatedWrittenEvaluations) {
        for (final WrittenEvaluation writtenTest : associatedWrittenEvaluations) {
            int totalCapacity = 0;
            final StringBuilder buffer = new StringBuilder(20);
            for (final Space room : writtenTest.getAssociatedRooms()) {
                buffer.append(room.getName()).append("; ");
                totalCapacity += room.<Integer> getMetadata("examCapacity").orElse(0);
            }
            if (buffer.length() > 0) {
                buffer.delete(buffer.length() - 2, buffer.length() - 1);
            }
            writtenEvaluationsRooms.put(writtenTest.getExternalId(), buffer.toString());
            int numberOfEnroledStudents = writtenTest.getCountStudentsEnroledAttendingExecutionCourses();
            executionCoursesEnroledStudents.put(writtenTest.getExternalId(), numberOfEnroledStudents);
            writtenEvaluationsMissingPlaces.put(writtenTest.getExternalId(),
                    Integer.valueOf(numberOfEnroledStudents - totalCapacity));
        }
    }

    public List<ExecutionCourse> getExecutionCoursesWithoutWrittenEvaluations() throws FenixServiceException {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        Integer[] curricularYearIDs = getCurricularYearIDs();

        if (curricularYearIDs != null && getExecutionDegree() != null) {
            List<Integer> curricularYears = Arrays.asList(curricularYearIDs);
            DegreeCurricularPlan degreeCurricularPlan = getExecutionDegree().getDegreeCurricularPlan();

            for (final ExecutionCourse executionCourse : getExecutionCourses()) {
                if (executionCourse.getAssociatedWrittenEvaluationsForScopeAndContext(curricularYears, degreeCurricularPlan)
                        .isEmpty()) {
                    result.add(executionCourse);
                }
            }
        }
        return result;
    }

    private static final Comparator<SelectItem> COMPARATOR_BY_LABEL = new Comparator<SelectItem>() {

        @Override
        public int compare(SelectItem o1, SelectItem o2) {
            return o1.getLabel().compareTo(o2.getLabel());
        }

    };

    public List<SelectItem> getExecutionCoursesLabels() throws FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            result.add(new SelectItem(executionCourse.getExternalId(), executionCourse.getNome()));
        }
        Collections.sort(result, COMPARATOR_BY_LABEL);
        result.add(0, new SelectItem(0, this.chooseMessage));
        return result;
    }

    // END Drop down menu logic

    // BEGIN Select Execution Course and Evaluation Type page logic
    public String continueToCreateWrittenEvaluation() throws FenixServiceException {
        if (this.getEvaluationTypeClassname() == null || this.getExecutionCourseID() == null
                || this.getEvaluationTypeClassname().equals("noSelection")) {
            this.setErrorMessage("label.choose.request");
            return "";
        } else {
            return "createWrittenEvaluation";
        }
    }

    public List<SelectItem> getEvaluationTypeClassnameLabels() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem("noSelection", this.chooseMessage));
        result.add(new SelectItem(WrittenTest.class.getName(), messages.getMessage(I18N.getLocale(), "label.test")));
        result.add(new SelectItem(Exam.class.getName(), messages.getMessage(I18N.getLocale(), "label.exam")));
        return result;
    }

    public List<SelectItem> getSeasonLabels() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem("noSelection", this.chooseMessage));
        result.add(new SelectItem(Season.SEASON1_STRING, messages.getMessage(I18N.getLocale(), "property.exam.1stExam")));
        result.add(new SelectItem(Season.SEASON2_STRING, messages.getMessage(I18N.getLocale(), "property.exam.2stExam")));
        result.add(new SelectItem(Season.SPECIAL_SEASON_STRING, messages.getMessage(I18N.getLocale(),
                "property.exam.specialSeasonExam")));
        result.add(new SelectItem(Season.EXTRAORDINARY_SEASON_STRING, messages.getMessage(I18N.getLocale(),
                "property.exam.extraordinarySeasonExam")));
        return result;
    }

    // END Select Execution Course and Evaluation Type page logic

    // BEGIN Associate OldRoom logic
    public Integer getOrderCriteria() {
        if (this.orderCriteria == null) {
            orderCriteria = new Integer(0);
        }
        return orderCriteria;
    }

    public void setOrderCriteria(Integer orderCriteria) {
        this.orderCriteria = orderCriteria;
    }

    public List<SelectItem> getOrderByCriteriaItems() {
        MessageResources messageResources = MessageResources.getMessageResources(Bundle.RESOURCE_ALLOCATION);

        List<SelectItem> orderByCriteriaItems = new ArrayList<SelectItem>(3);
        orderByCriteriaItems.add(new SelectItem(0, messageResources.getMessage("label.capacity")));
        orderByCriteriaItems.add(new SelectItem(1, messageResources.getMessage("property.room.building")));
        orderByCriteriaItems.add(new SelectItem(2, messageResources.getMessage("label.room.type")));

        return orderByCriteriaItems;
    }

    public String[] getChosenRoomsIDs() throws FenixServiceException {
        if (this.getViewState().getAttribute("chosenRoomsIDs") == null && this.getEvaluationID() != null) {
            List<String> associatedRooms = new ArrayList<String>();

            for (Space room : ((WrittenEvaluation) this.getEvaluation()).getAssociatedRooms()) {
                associatedRooms.add(room.getExternalId() + "-" + room.<Integer> getMetadata("examCapacity").orElse(0));
            }

            String[] selectedRooms = {};
            this.setChosenRoomsIDs(associatedRooms.toArray(selectedRooms));
        }

        return (String[]) this.getViewState().getAttribute("chosenRoomsIDs");
    }

    public void setChosenRoomsIDs(String[] chosenRoomsIDs) {
        this.getViewState().setAttribute("chosenRoomsIDs", chosenRoomsIDs);
    }

    public List<SelectItem> getRoomsSelectItems() throws FenixServiceException {

        Calendar examDate = Calendar.getInstance();
        examDate.set(Calendar.YEAR, getYear());
        examDate.set(Calendar.MONTH, getMonth() - 1);
        examDate.set(Calendar.DAY_OF_MONTH, getDay());
        examDate.set(Calendar.SECOND, 0);
        examDate.set(Calendar.MILLISECOND, 0);

        DiaSemana dayOfWeek = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));

        Calendar examStartTime = Calendar.getInstance();
        examStartTime.set(Calendar.HOUR_OF_DAY, getBeginHour());
        examStartTime.set(Calendar.MINUTE, getBeginMinute());
        examStartTime.set(Calendar.SECOND, 0);
        examStartTime.set(Calendar.MILLISECOND, 0);

        Calendar examEndTime = Calendar.getInstance();
        examEndTime.set(Calendar.HOUR_OF_DAY, getEndHour());
        examEndTime.set(Calendar.MINUTE, getEndMinute());
        examEndTime.set(Calendar.SECOND, 0);
        examEndTime.set(Calendar.MILLISECOND, 0);

        List<InfoRoom> availableInfoRoom =
                SpaceUtils.allocatableSpace(YearMonthDay.fromCalendarFields(examDate), YearMonthDay.fromCalendarFields(examDate),
                        HourMinuteSecond.fromCalendarFields(examStartTime), HourMinuteSecond.fromCalendarFields(examEndTime),
                        dayOfWeek, null, null, false);

        if (this.getEvaluationID() != null) {
            for (Space room : ((WrittenEvaluation) this.getEvaluation()).getAssociatedRooms()) {
                InfoRoom associatedRoom = InfoRoom.newInfoFromDomain(room);
                if (!availableInfoRoom.contains(associatedRoom)) {
                    availableInfoRoom.add(associatedRoom);
                }
            }
        }

        if (this.getOrderCriteria() == 0) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator("capacidadeExame")));
            comparatorChain.addComparator(new BeanComparator("nome"));

            Collections.sort(availableInfoRoom, comparatorChain);
        } else if (this.getOrderCriteria() == 1) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator("edificio")));
            comparatorChain.addComparator(new BeanComparator("nome"));

            Collections.sort(availableInfoRoom, comparatorChain);
        } else if (this.getOrderCriteria() == 2) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator("tipo")));
            comparatorChain.addComparator(new BeanComparator("nome"));

            Collections.sort(availableInfoRoom, comparatorChain);
        }

        List<SelectItem> items = new ArrayList<SelectItem>(availableInfoRoom.size());
        for (InfoRoom infoRoom : availableInfoRoom) {
            StringBuilder label = new StringBuilder();
            label.append(infoRoom.getNome());
            label.append("  ( ");
            label.append(infoRoom.getCapacidadeExame());
            label.append(" ");
            label.append(this.labelVacancies);
            label.append(", ");
            label.append(infoRoom.getEdificio());
            label.append(", ");
            label.append(infoRoom.getTipo());
            label.append(" )");

            final String value = getRoomWithExamCapacityString(infoRoom);
            final SelectItem selectItem = new SelectItem(value, label.toString());
            items.add(selectItem);
        }

        return items;
    }

    private String getRoomID(String roomString) {
        return roomString.split("-")[0];
    }

    private String getRoomWithExamCapacityString(InfoRoom infoRoom) {
        return infoRoom.getExternalId() + "-" + infoRoom.getCapacidadeExame();
    }

    public String getAssociatedRooms() throws FenixServiceException {
        StringBuilder result = new StringBuilder();

        if (this.getChosenRoomsIDs() != null && this.getChosenRoomsIDs().length != 0) {
            for (String chosenRoomString : this.getChosenRoomsIDs()) {
                String chosenRoomID = getRoomID(chosenRoomString);
                Space room = (Space) FenixFramework.getDomainObject(chosenRoomID);
                result.append(room.getName());
                result.append("; ");
            }

            if (result.length() > 0) {
                result.delete(result.length() - 2, result.length() - 1);
            }

            return result.toString();
        } else {
            return messages.getMessage(I18N.getLocale(), "label.no.associated.rooms");
        }
    }

    public String associateRoomToWrittenEvaluation() {
        return returnToCreateOrEdit();
    }

    // END Associate OldRoom logic

    // BEGIN Create and Edit logic
    public String createWrittenEvaluation() throws FenixServiceException {

        if (this.getSeason() != null && this.getSeason().equals("noSelection")) {
            this.setErrorMessage("label.choose.request");
            return "";
        }

        List<String> executionCourseIDs = new ArrayList<String>(this.getAssociatedExecutionCourses().size());
        List<String> degreeModuleScopeIDs = new ArrayList<String>();
        List<String> roomsIDs = new ArrayList<String>();

        if (!prepareArguments(executionCourseIDs, degreeModuleScopeIDs, roomsIDs)) {
            return "";
        }

        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;
        try {
            CreateWrittenEvaluation.runCreateWrittenEvaluation(null, this.getBegin().getTime(), this.getBegin().getTime(), this
                    .getEnd().getTime(), executionCourseIDs, degreeModuleScopeIDs, roomsIDs, null, season, this.getDescription());

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        return WrittenTest.class.getSimpleName();
    }

    public boolean prepareArguments(List<String> executionCourseIDs, List<String> degreeModuleScopeIDs, List<String> roomsIDs)
            throws FenixServiceException {

        for (String executionCourseID : this.associatedExecutionCourses) {

            executionCourseIDs.add(executionCourseID.toString());

            if (this.getCurricularCourseScopesToAssociate().get(executionCourseID).length == 0
                    && this.getCurricularCourseContextToAssociate().get(executionCourseID).length == 0) {
                this.setErrorMessage("error.invalidCurricularCourseScope");
                return false;
            }

            for (String curricularCourseScopeId : this.getCurricularCourseScopesToAssociate().get(executionCourseID)) {
                degreeModuleScopeIDs
                        .add(DegreeModuleScope.getKey(curricularCourseScopeId, CurricularCourseScope.class.getName()));
            }
            for (String curricularCourseContextId : this.getCurricularCourseContextToAssociate().get(executionCourseID)) {
                degreeModuleScopeIDs.add(DegreeModuleScope.getKey(curricularCourseContextId, Context.class.getName()));
            }
        }

        if (this.getChosenRoomsIDs() != null) {
            for (String roomIDString : this.getChosenRoomsIDs()) {
                String roomID = getRoomID(roomIDString);
                roomsIDs.add(roomID.toString());
            }
        }
        return true;
    }

    public String editWrittenEvaluation() throws FenixServiceException, IOException {
        if (this.getSeason() != null && this.getSeason().equals("noSelection")) {
            this.setErrorMessage("label.choose.request");
            return "";
        }

        List<String> executionCourseIDs = new ArrayList<String>(this.getAssociatedExecutionCourses().size());
        List<String> degreeModuleScopeIDs = new ArrayList<String>();
        List<String> roomsIDs = new ArrayList<String>();

        if (!prepareArguments(executionCourseIDs, degreeModuleScopeIDs, roomsIDs)) {
            return "";
        }

        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;
        try {
            EditWrittenEvaluation.runEditWrittenEvaluation(null, this.getBegin().getTime(), this.getBegin().getTime(), this
                    .getEnd().getTime(), executionCourseIDs, degreeModuleScopeIDs, roomsIDs, this.evaluationID, season, this
                    .getDescription(), null);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedException) {
                errorMessage = "message.error.notAuthorized";
            }
            if (e instanceof DomainException) {
                final DomainException domainException = (DomainException) e;
                setErrorMessageArguments(domainException.getArgs());
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        final String originPage = getOriginPage();
        if (originPage != null && originPage.length() > 0) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getApplicationContext());
            stringBuilder
                    .append("/resourceAllocationManager/searchWrittenEvaluationsByDate.do?method=returnToSearchPage&amp;page=0&date=");
            stringBuilder.append(DateFormatUtil.format("yyyy/MM/dd", this.getBegin().getTime()));
            if (getSelectedBegin() != null && getSelectedBegin().length() > 0 && getSelectedBegin().equals("true")) {
                stringBuilder.append("&selectedBegin=");
                stringBuilder.append(DateFormatUtil.format("HH:mm", this.getBegin().getTime()));
            }
            if (getSelectedEnd() != null && getSelectedEnd().length() > 0 && getSelectedEnd().equals("true")) {
                stringBuilder.append("&selectedEnd=");
                stringBuilder.append(DateFormatUtil.format("HH:mm", this.getEnd().getTime()));
            }
            stringBuilder.append("&academicInterval=").append(academicInterval);
            String url = stringBuilder.toString();

            String checksum = GenericChecksumRewriter.calculateChecksum(url, getRequest().getSession());
            stringBuilder.append("&");
            stringBuilder.append(GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME);
            stringBuilder.append("=");
            stringBuilder.append(checksum);

            FacesContext.getCurrentInstance().getExternalContext().redirect(stringBuilder.toString());
            return originPage;
        } else {
            return WrittenTest.class.getSimpleName();
            // return this.getEvaluation().getClass().getSimpleName();
        }
    }

    // END Create and Edit logic

    // BEGIN Associate Execution Course and Scopes logic
    public void setAssociatedExecutionCourses(List<String> associatedExecutionCourses) {
        this.associatedExecutionCourses = associatedExecutionCourses;
        this.getViewState().setAttribute("associatedExecutionCourses", associatedExecutionCourses);
    }

    public List<String> getAssociatedExecutionCourses() throws FenixServiceException {

        if (this.getViewState().getAttribute("associatedExecutionCourses") != null) {
            this.associatedExecutionCourses = (List<String>) this.getViewState().getAttribute("associatedExecutionCourses");
        } else if (this.getEvaluationID() != null) {
            List<String> result = new ArrayList<String>();
            for (ExecutionCourse executionCourse : this.getEvaluation().getAssociatedExecutionCoursesSet()) {
                result.add(executionCourse.getExternalId());
                List<String> selectedScopes = new ArrayList<String>();
                List<String> selectedContexts = new ArrayList<String>();
                for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
                    for (DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                        if (degreeModuleScope.isActiveForExecutionPeriod(executionCourse.getExecutionPeriod())) {
                            if (((WrittenEvaluation) this.getEvaluation()).getDegreeModuleScopes().contains(degreeModuleScope)) {
                                if (degreeModuleScope instanceof DegreeModuleScopeCurricularCourseScope) {
                                    selectedScopes.add(degreeModuleScope.getExternalId());
                                } else if (degreeModuleScope instanceof DegreeModuleScopeContext) {
                                    selectedContexts.add(degreeModuleScope.getExternalId());
                                }
                            }
                        }
                    }
                }

                String[] selected = {};
                this.getCurricularCourseScopesToAssociate()
                        .put(executionCourse.getExternalId(), selectedScopes.toArray(selected));
                this.getCurricularCourseContextToAssociate().put(executionCourse.getExternalId(),
                        selectedContexts.toArray(selected));
            }
            this.setAssociatedExecutionCourses(result);
        } else {
            List<String> result = new ArrayList<String>();
            result.add(this.getExecutionCourse().getExternalId());
            this.setAssociatedExecutionCourses(result);
        }

        fillInAuxiliarMaps();
        return this.associatedExecutionCourses;
    }

    private void fillInAuxiliarMaps() throws FenixServiceException {

        for (String executionCourseID : this.associatedExecutionCourses) {

            ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
            this.associatedExecutionCoursesNames.put(executionCourseID, executionCourse.getNome());

            List<SelectItem> items = new ArrayList<SelectItem>();
            List<SelectItem> items2 = new ArrayList<SelectItem>();

            String[] curricularCourseScopesToAssociate =
                    this.getCurricularCourseScopesToAssociate().get(executionCourse.getExternalId());
            String[] curricularCourseContextsToAssociate =
                    this.getCurricularCourseContextToAssociate().get(executionCourse.getExternalId());

            List<String> auxiliarArray = new ArrayList<String>();
            List<String> auxiliarArray2 = new ArrayList<String>();
            if (curricularCourseScopesToAssociate != null) {
                for (String curricularCourseScopeToAssociate : curricularCourseScopesToAssociate) {
                    auxiliarArray.add(curricularCourseScopeToAssociate);
                }
            }
            if (curricularCourseContextsToAssociate != null) {
                for (String curricularCourseContextToAssociate : curricularCourseContextsToAssociate) {
                    auxiliarArray2.add(curricularCourseContextToAssociate);
                }
            }

            for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
                for (DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                    if (degreeModuleScope.isActiveForExecutionPeriod(executionCourse.getExecutionPeriod())) {

                        StringBuilder label = new StringBuilder();
                        label.append(curricularCourse.getDegreeCurricularPlan().getName());
                        label.append(" ").append(degreeModuleScope.getCurricularYear()).append(" � Ano");
                        if (!degreeModuleScope.getBranch().equals("")) {
                            label.append(" ").append(degreeModuleScope.getBranch());
                        }

                        if (degreeModuleScope instanceof DegreeModuleScopeCurricularCourseScope) {
                            items.add(new SelectItem(degreeModuleScope.getExternalId(), label.toString()));
                        } else if (degreeModuleScope instanceof DegreeModuleScopeContext) {
                            items2.add(new SelectItem(degreeModuleScope.getExternalId(), label.toString()));
                        }
                    }
                }
            }

            this.curricularCourseScopesSelectItems.put(executionCourse.getExternalId(), items);
            this.curricularCourseContextSelectItems.put(executionCourse.getExternalId(), items2);

            String[] selected = {};
            this.getCurricularCourseScopesToAssociate().put(executionCourse.getExternalId(), auxiliarArray.toArray(selected));
            this.getCurricularCourseContextToAssociate().put(executionCourse.getExternalId(), auxiliarArray2.toArray(selected));
        }
    }

    public Map<String, List<SelectItem>> getCurricularCourseContextSelectItems() {
        return curricularCourseContextSelectItems;
    }

    public void setCurricularCourseContextSelectItems(Map<String, List<SelectItem>> curricularCourseContextSelectItems) {
        this.curricularCourseContextSelectItems = curricularCourseContextSelectItems;
    }

    public Map<String, List<SelectItem>> getCurricularCourseScopesSelectItems() {
        return curricularCourseScopesSelectItems;
    }

    public void setCurricularCourseScopesSelectItems(Map<String, List<SelectItem>> curricularCourseScopesSelectItems) {
        this.curricularCourseScopesSelectItems = curricularCourseScopesSelectItems;
    }

    public Map<String, String[]> getCurricularCourseScopesToAssociate() {
        if (this.getViewState().getAttribute("curricularCourseScopesToAssociate") == null) {
            this.getViewState().setAttribute("curricularCourseScopesToAssociate", new HashMap<String, String[]>());
        }
        return (Map<String, String[]>) this.getViewState().getAttribute("curricularCourseScopesToAssociate");
    }

    public void setCurricularCourseScopesToAssociate(Map<String, String[]> curricularCourseScopesToAssociate) {
        this.getViewState().setAttribute("curricularCourseScopesToAssociate", curricularCourseScopesToAssociate);
    }

    public Map<String, String[]> getCurricularCourseContextToAssociate() {
        if (this.getViewState().getAttribute("curricularCourseContextToAssociate") == null) {
            this.getViewState().setAttribute("curricularCourseContextToAssociate", new HashMap<String, String[]>());
        }
        return (Map<String, String[]>) this.getViewState().getAttribute("curricularCourseContextToAssociate");
    }

    public void setCurricularCourseContextToAssociate(Map<String, String[]> curricularCourseContextToAssociate) {
        this.getViewState().setAttribute("curricularCourseContextToAssociate", curricularCourseContextToAssociate);
    }

    public String associateExecutionCourse() throws FenixServiceException {
        if (this.getSelectedExecutionDegreeID() == null || this.getSelectedCurricularYearID() == null
                || this.getSelectedExecutionCourseID() == null || this.getSelectedCurricularYearID() == 0) {
            this.setErrorMessage("label.choose.request");
            return "";
        } else {
            List<String> list = this.getAssociatedExecutionCourses();
            String integer = this.getSelectedExecutionCourseID();

            if (!list.contains(integer)) {
                list.add(integer);
            }

            ExecutionCourse executionCourse = FenixFramework.getDomainObject(integer);
            List<String> auxiliarArray = new ArrayList<String>();
            for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
                for (DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                    auxiliarArray.add(degreeModuleScope.getExternalId());
                }
            }

            String[] selected = {};
            this.getCurricularCourseScopesToAssociate().put(executionCourse.getExternalId(), auxiliarArray.toArray(selected));

            return returnToCreateOrEdit();
        }
    }

    public void disassociateExecutionCourse() throws FenixServiceException {
        List<String> associatedExecutionCourses = this.getAssociatedExecutionCourses();
        String executionCourseToDisassociate = this.getRequestParameter("executionCourseToDisassociate");

        associatedExecutionCourses.remove(executionCourseToDisassociate);
        this.setAssociatedExecutionCourses(associatedExecutionCourses);

        this.getCurricularCourseScopesToAssociate().remove(executionCourseToDisassociate);
    }

    public String returnToCreateOrEdit() {
        if (this.getEvaluationID() == null) {
            return "createWrittenTest";
        } else {
            return "editWrittenTest";
        }
    }

    public Map<String, String> getAssociatedExecutionCoursesNames() {
        return associatedExecutionCoursesNames;
    }

    public void setAssociatedExecutionCoursesNames(Map<String, String> associatedExecutionCoursesNames) {
        this.associatedExecutionCoursesNames = associatedExecutionCoursesNames;
    }

    public Map<String, List<WrittenEvaluation>> getWrittenEvaluations() {
        return writtenEvaluations;
    }

    public Map<String, Integer> getWrittenEvaluationsFreeSpace() {
        return writtenEvaluationsMissingPlaces;
    }

    public Map<String, String> getWrittenEvaluationsRooms() {
        return writtenEvaluationsRooms;
    }

    // END Associate Execution Course and Scopes logic

    // BEGIN Execution Course comment logic
    public String getComment() throws FenixServiceException {
        if (this.comment == null && this.getExecutionCourse() != null) {
            this.comment = getExecutionCourse().getComment();
        }
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String commentExecutionCourse() throws FenixServiceException {
        try {

            DefineExamComment.run(this.getExecutionCourse().getSigla(), this.getExecutionCourse().getExecutionPeriod()
                    .getExternalId(), this.getComment());
        } catch (FenixServiceException e) {
            this.setErrorMessage(e.getMessage());
            return "";
        }
        return "writtenEvaluationCalendar";
    }

    // END Execution Course comment logic

    // BEGIN Code to avoid bug in associating execution courses to written
    // evaluation....
    public String getSelectedExecutionDegreeID() {
        String selectedExecutionDegreeID = (String) this.getViewState().getAttribute("selectedExecutionDegreeID");

        if (selectedExecutionDegreeID == null) {
            selectedExecutionDegreeID = this.getExecutionDegreeIdHidden().getValue().toString();
            setSelectedExecutionDegreeID(selectedExecutionDegreeID);
        }

        return selectedExecutionDegreeID;
    }

    public void setSelectedExecutionDegreeID(String selectedExecutionDegreeID) {
        this.getViewState().setAttribute("selectedExecutionDegreeID", selectedExecutionDegreeID);
    }

    public Integer getSelectedCurricularYearID() {
        Integer selectedCurricularYearID = (Integer) this.getViewState().getAttribute("selectedCurricularYearID");

        if (selectedCurricularYearID == null) {
            Integer[] curricularYears = getCurricularYearIDs();
            if (curricularYears != null && curricularYears.length != 0) {
                selectedCurricularYearID = curricularYears[0];
                setSelectedCurricularYearID(selectedCurricularYearID);
            }
        }

        return selectedCurricularYearID;
    }

    public void setSelectedCurricularYearID(Integer selectedCurricularYearID) {
        this.getViewState().setAttribute("selectedCurricularYearID", selectedCurricularYearID);
        curricularYearID = selectedCurricularYearID;
    }

    public String getSelectedExecutionCourseID() {
        String selectedExecutionCourseID = (String) this.getViewState().getAttribute("selectedExecutionCourseID");

        if (selectedExecutionCourseID == null) {
            selectedExecutionCourseID = (String) this.getExecutionCourseIdHidden().getValue();
            setSelectedExecutionCourseID(selectedExecutionCourseID);
        }

        return selectedExecutionCourseID;
    }

    public void setSelectedExecutionCourseID(String selectedExecutionCourseID) {
        this.getViewState().setAttribute("selectedExecutionCourseID", selectedExecutionCourseID);
    }

    public void onExecutionDegreeChanged(ValueChangeEvent valueChangeEvent) {
        setSelectedExecutionDegreeID((String) valueChangeEvent.getNewValue());
    }

    public void onCurricularYearChanged(ValueChangeEvent valueChangeEvent) {
        setSelectedCurricularYearID((Integer) valueChangeEvent.getNewValue());
    }

    public void onExecutionCourseChanged(ValueChangeEvent valueChangeEvent) {
        setSelectedExecutionCourseID((String) valueChangeEvent.getNewValue());
    }

    private List<ExecutionCourse> readExecutionCourses() throws FenixServiceException {
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(this.getSelectedExecutionDegreeID());
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        executionCourses.addAll(ExecutionCourse.filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(
                getAcademicIntervalFromParameter(getAcademicInterval()), executionDegree.getDegreeCurricularPlan(),
                CurricularYear.readByYear(curricularYearID), "%"));
        Collections.sort(executionCourses, new BeanComparator("sigla"));
        return executionCourses;
    }

    public List<SelectItem> getExecutionCoursesItems() throws FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final ExecutionCourse executionCourse : readExecutionCourses()) {
            result.add(new SelectItem(executionCourse.getExternalId(), executionCourse.getNome()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(0, this.chooseMessage));
        return result;
    }

    // END Code to avoid bug in associating execution courses to written
    // evaluation....

    // public Integer getAcademicIntervalOID() {
    // return (academicIntervalOID == null) ? academicIntervalOID =
    // getAndHoldIntegerParameter("academicIntervalOID")
    // : academicIntervalOID;
    // }

    public Map<String, Integer> getExecutionCoursesEnroledStudents() {
        return executionCoursesEnroledStudents;
    }

    public String getSelectedDateString() throws FenixServiceException {
        return new LocalDate(getYear(), getMonth(), getDay()).toString("dd/MM/yyyy");
    }

    public String getSelectedBeginHourString() throws FenixServiceException {
        return new HourMinuteSecond(getBeginHour(), getBeginMinute(), 0).toString("HH:mm");
    }

    public String getSelectedEndHourString() throws FenixServiceException {
        return new HourMinuteSecond(getEndHour(), getEndMinute(), 0).toString("HH:mm");
    }
}