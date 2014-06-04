/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator.evaluation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams.CreateWrittenEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams.DeleteWrittenEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams.EditWrittenEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteEvaluation;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.util.MessageResources;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class CoordinatorEvaluationsBackingBean extends FenixBackingBean {

    private static final MessageResources messages = MessageResources.getMessageResources(Bundle.DEGREE);

    private static final Locale locale = new Locale("pt", "PT");

    // These are context variables
    private String degreeCurricularPlanID = null;

    private String executionPeriodID = null;

    private String curricularYearID = null;

    // These are context variables for creating evaluations
    private String evaluationType = null;

    private String executionCourseID = null;

    // These variables hold the necessary information for creating/editing the
    // evaluations
    private String name = null;

    private String begin = null;

    private String end = null;

    private String description = null;

    private String date = null;

    private String beginTime = null;

    private String endTime = null;

    private String evaluationID = null;

    private Evaluation evalution;

    private Boolean onlineSubmissionsAllowed = null;

    private Integer maxSubmissionsToKeep = null;

    private String groupingID = null;

    private List<SelectItem> executionCourseGroupings = null;

    private Evaluation evaluation;

    public String getDegreeCurricularPlanID() {
        DegreeCoordinatorIndex.setCoordinatorContext(getRequest());
        return (degreeCurricularPlanID == null) ? degreeCurricularPlanID = getAndHoldStringParameter("degreeCurricularPlanID") : degreeCurricularPlanID;
    }

    public String getExecutionPeriodID() throws FenixServiceException {
        if (executionPeriodID == null) {
            executionPeriodID = getAndHoldStringParameter("executionPeriodID");
            if (executionPeriodID == null) {
                final ExecutionSemester executionSemester = getCurrentExecutionPeriod();
                executionPeriodID = executionSemester.getExternalId();
            }
        }
        return executionPeriodID;
    }

    public void setExecutionPeriodID(final String executionPeriodID) {
        this.executionPeriodID = executionPeriodID;
    }

    public String getCurricularYearID() {
        return (curricularYearID == null) ? curricularYearID = getAndHoldStringParameter("curricularYearID") : curricularYearID;
    }

    public void setCurricularYearID(String curricularYearID) {
        this.curricularYearID = curricularYearID;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        final String degreeCurricularPlanID = getDegreeCurricularPlanID();
        return FenixFramework.getDomainObject(degreeCurricularPlanID);
    }

    private ExecutionSemester getCurrentExecutionPeriod() throws FenixServiceException {
        ExecutionSemester lastExecutionPeriod = null;
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
                if (executionSemester.isCurrent()) {
                    return executionSemester;
                } else if (lastExecutionPeriod == null || executionSemester.isAfter(lastExecutionPeriod)) {
                    lastExecutionPeriod = executionSemester;
                }
            }
        }
        return lastExecutionPeriod;
    }

    public List<SelectItem> getExecutionPeriodSelectItems() throws FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final TreeSet<ExecutionSemester> executionPeriods = new TreeSet<ExecutionSemester>();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
                executionPeriods.add(executionSemester);
            }
        }
        final List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (final ExecutionSemester executionSemester : executionPeriods) {
            final SelectItem selectItem = new SelectItem();
            selectItem.setLabel(executionSemester.getName() + " - " + executionSemester.getExecutionYear().getYear());
            selectItem.setValue(executionSemester.getExternalId());
            selectItems.add(selectItem);
        }
        return selectItems;
    }

    public List<SelectItem> getCurricularYearSelectItems() throws FenixServiceException {
        final List<SelectItem> selectItems = new ArrayList<SelectItem>();
        selectItems.add(new SelectItem("", messages.getMessage(locale, "public.curricular.years.all")));
        for (int i = 1; i <= 5; i++) {
            selectItems.add(new SelectItem(Integer.valueOf(i), String.valueOf(i)));
        }
        return selectItems;
    }

    public ExecutionSemester getExecutionPeriod() throws FenixServiceException {
        final String executionPeriodID = getExecutionPeriodID();
        return FenixFramework.getDomainObject(executionPeriodID);
    }

    private ExecutionDegree getExecutionDegree() throws FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final ExecutionSemester executionSemester = getExecutionPeriod();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            if (executionDegree.getExecutionYear() == executionSemester.getExecutionYear()) {
                return executionDegree;
            }
        }
        return null;
    }

    public Date getCalendarBegin() throws FenixServiceException {
        final ExecutionDegree executionDegree = getExecutionDegree();
        final ExecutionSemester executionSemester = getExecutionPeriod();
        if (executionDegree != null) {
            if (executionSemester.getSemester().intValue() == 1) {
                return executionDegree.getPeriodLessonsFirstSemester().getStart();
            } else if (executionSemester.getSemester().intValue() == 2) {
                return executionDegree.getPeriodLessonsSecondSemester().getStart();
            } else {
                return executionSemester.getBeginDate();
            }
        } else {
            return null;
        }
    }

    public Date getCalendarEnd() throws FenixServiceException {
        final ExecutionDegree executionDegree = getExecutionDegree();
        final ExecutionSemester executionSemester = getExecutionPeriod();
        if (executionDegree != null) {
            if (executionSemester.getSemester().intValue() == 1) {
                return executionDegree.getPeriodExamsFirstSemester().getEnd();
            } else if (executionSemester.getSemester().intValue() == 2) {
                return executionDegree.getPeriodExamsSecondSemester().getEnd();
            } else {
                return executionSemester.getEndDate();
            }
        } else {
            return null;
        }
    }

    private static final Comparator executionCourseComparator = new ComparatorChain();

    static {
        ((ComparatorChain) executionCourseComparator).addComparator(new BeanComparator("nome"));
        ((ComparatorChain) executionCourseComparator).addComparator(new BeanComparator("externalId"));
    }

    private static final Comparator evaluationComparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1.getClass() != o2.getClass()) {
                return o1.getClass().getName().compareTo(o2.getClass().getName());
            } else if (o1 instanceof WrittenTest) {
                final WrittenTest writtenTest1 = (WrittenTest) o1;
                final WrittenTest writtenTest2 = (WrittenTest) o2;
                return writtenTest1.getDayDate().compareTo(writtenTest2.getDayDate());
            } else if (o2 instanceof Project) {
                final Project project1 = (Project) o1;
                final Project project2 = (Project) o2;
                return project1.getBegin().compareTo(project2.getBegin());
            } else {
                return -1;
            }
        }
    };

    public List<ExecutionCourse> getExecutionCourses() throws FenixServiceException {
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final CurricularYear curricularYear = getCurricularYear();

        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            if (isActiveInExecutionPeriodAndYear(curricularCourse, executionSemester, curricularYear)) {
                for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                    if (executionCourse.getExecutionPeriod() == executionSemester) {
                        executionCourses.add(executionCourse);
                    }
                }
            }
        }
        return executionCourses;
    }

    public Map<ExecutionCourse, Set<Evaluation>> getExecutionCoursesMap() throws FenixServiceException {
        final Map<ExecutionCourse, Set<Evaluation>> executionCourseEvaluationsMap =
                new TreeMap<ExecutionCourse, Set<Evaluation>>(executionCourseComparator);
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final CurricularYear curricularYear = getCurricularYear();

        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            if (isActiveInExecutionPeriodAndYear(curricularCourse, executionSemester, curricularYear)) {
                for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                    if (executionCourse.getExecutionPeriod() == executionSemester) {
                        final Set<Evaluation> evaluations = new TreeSet<Evaluation>(evaluationComparator);
                        executionCourseEvaluationsMap.put(executionCourse, evaluations);
                        evaluations.addAll(executionCourse.getAssociatedEvaluations());
                    }
                }
            }
        }
        return executionCourseEvaluationsMap;
    }

    public List<CalendarLink> getCalendarLinks() throws FenixServiceException {
        final List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();
        final String evaluationType = getEvaluationType();

        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
                if (evaluationType == null || evaluationType.length() == 0
                        || evaluationType.equals(evaluation.getClass().getName())) {
                    if (evaluation instanceof WrittenTest) {
                        final WrittenTest writtenTest = (WrittenTest) evaluation;
                        constructCalendarLink(calendarLinks, writtenTest, executionCourse);
                    } else if (evaluation instanceof Project) {
                        final Project project = (Project) evaluation;
                        constructCalendarLink(calendarLinks, project, executionCourse);
                    } else if (evaluation instanceof Exam) {
                        final Exam exam = (Exam) evaluation;
                        if (exam.isExamsMapPublished()) {
                            constructEmptyCalendarLink(calendarLinks, exam, executionCourse);
                        }
                    }
                }
            }
        }

        return calendarLinks;
    }

    public List<ExecutionCourse> getExecutionCoursesWithoutEvaluations() throws FenixServiceException {
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            if (!hasNonExamEvaluation(executionCourse)) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    public List<ExecutionCourse> getExecutionCoursesWithEvaluations() throws FenixServiceException {
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            if (hasNonExamEvaluation(executionCourse)) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    private boolean hasNonExamEvaluation(final ExecutionCourse executionCourse) {
        for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
            if (!(evaluation instanceof Exam)) {
                return true;
            }
        }
        return false;
    }

    private boolean isActiveInExecutionPeriodAndYear(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester, final CurricularYear curricularYear) {

        for (final DegreeModuleScope curricularCourseScope : curricularCourse.getDegreeModuleScopes()) {
            if (curricularCourseScope.isActiveForExecutionPeriod(executionSemester)
                    && (curricularYear == null || curricularYear.getYear().equals(curricularCourseScope.getCurricularYear()))) {
                return true;
            }
        }
        return false;
    }

    private CurricularYear getCurricularYear() {
        final String curricularYearID = getCurricularYearID();
        return (curricularYearID != null) ? FenixFramework.<CurricularYear> getDomainObject(curricularYearID) : null;
    }

    private void constructEmptyCalendarLink(final List<CalendarLink> calendarLinks, final WrittenEvaluation writtenEvaluation,
            final ExecutionCourse executionCourse) {
        CalendarLink calendarLink = new CalendarLink(executionCourse, writtenEvaluation, locale);
        // addLinkParameters(calendarLink, executionCourse, writtenEvaluation);
        // addWrittenEvaluationLinkParameters(calendarLink, writtenEvaluation);
        calendarLink.setAsLink(false);
        calendarLinks.add(calendarLink);
    }

    private void constructCalendarLink(final List<CalendarLink> calendarLinks, final WrittenEvaluation writtenEvaluation,
            final ExecutionCourse executionCourse) {
        CalendarLink calendarLink = new CalendarLink(executionCourse, writtenEvaluation, locale);
        addLinkParameters(calendarLink, executionCourse, writtenEvaluation);
        addWrittenEvaluationLinkParameters(calendarLink, writtenEvaluation);
        calendarLinks.add(calendarLink);
    }

    private void constructCalendarLink(final List<CalendarLink> calendarLinks, final Project project,
            final ExecutionCourse executionCourse) {
        final CalendarLink calendarLinkBegin =
                new CalendarLink(executionCourse, project, project.getBegin(), messages.getMessage(locale,
                        "label.evaluation.project.begin"), locale);
        addLinkParameters(calendarLinkBegin, executionCourse, project);
        addProjectLinkParameters(calendarLinkBegin, project);
        calendarLinks.add(calendarLinkBegin);

        final CalendarLink calendarLinkEnd =
                new CalendarLink(executionCourse, project, project.getEnd(), messages.getMessage(locale,
                        "label.evaluation.project.end"), locale);
        addLinkParameters(calendarLinkEnd, executionCourse, project);
        addProjectLinkParameters(calendarLinkEnd, project);
        calendarLinks.add(calendarLinkEnd);
    }

    private void addLinkParameters(final CalendarLink calendarLink, final ExecutionCourse executionCourse,
            final Evaluation evaluation) {
        calendarLink.addLinkParameter("degreeCurricularPlanID", getDegreeCurricularPlanID().toString());
        calendarLink.addLinkParameter("executionPeriodID", executionCourse.getExecutionPeriod().getExternalId().toString());
        calendarLink.addLinkParameter("executionCourseID", executionCourse.getExternalId().toString());
        calendarLink
                .addLinkParameter("curricularYearID", (getCurricularYearID() != null) ? getCurricularYearID().toString() : "");
        calendarLink.addLinkParameter("evaluationID", evaluation.getExternalId().toString());
        calendarLink.addLinkParameter("evaluationType", evaluation.getClass().getName());
    }

    private void addWrittenEvaluationLinkParameters(CalendarLink calendarLink, WrittenEvaluation writtenEvaluation) {
        if (writtenEvaluation instanceof WrittenTest) {
            calendarLink.addLinkParameter("description", ((WrittenTest) writtenEvaluation).getDescription());
        }
        calendarLink.addLinkParameter("date", DateFormatUtil.format("dd/MM/yyyy", writtenEvaluation.getDayDate()));
        calendarLink.addLinkParameter("beginTime", DateFormatUtil.format("HH:mm", writtenEvaluation.getBeginningDate()));
        calendarLink.addLinkParameter("endTime", DateFormatUtil.format("HH:mm", writtenEvaluation.getEndDate()));
    }

    private void addProjectLinkParameters(CalendarLink calendarLinkBegin, Project project) {
        calendarLinkBegin.addLinkParameter("name", project.getName());
        calendarLinkBegin.addLinkParameter("begin", DateFormatUtil.format("dd/MM/yyyy HH:mm", project.getBegin()));
        calendarLinkBegin.addLinkParameter("end", DateFormatUtil.format("dd/MM/yyyy HH:mm", project.getEnd()));
        calendarLinkBegin.addLinkParameter("description", project.getDescription());
    }

    public String getEvaluationType() {
        return (evaluationType == null) ? evaluationType = getAndHoldStringParameter("evaluationType") : evaluationType;
    }

    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }

    public List<SelectItem> getExecutionCourseSelectItems() throws FenixServiceException {
        final List<SelectItem> selectItems = new ArrayList<SelectItem>();
        selectItems.add(new SelectItem("", messages.getMessage(locale, "public.curricular.years.all")));
        final List<ExecutionCourse> executionCourses = getExecutionCourses();
        for (ExecutionCourse executionCourse : executionCourses) {
            selectItems.add(new SelectItem(executionCourse.getExternalId(), executionCourse.getNome()));
        }
        return selectItems;
    }

    public String getExecutionCourseID() {
        return (executionCourseID == null) ? executionCourseID = getAndHoldStringParameter("executionCourseID") : executionCourseID;
    }

    public void setExecutionCourseID(String executionCourseID) {
        this.executionCourseID = executionCourseID;
    }

    public String getDescription() {
        return (description == null) ? description = getAndHoldStringParameter("description") : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return (name == null) ? name = getAndHoldStringParameter("name") : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBegin() {
        if (begin == null) {
            begin = getAndHoldStringParameter("begin");
            if (begin == null) {
                final String parameterString = getRequestParameter("selectedDate");
                if (parameterString != null && parameterString.length() > 0) {
                    begin = parameterString + " 00:00";
                    setRequestAttribute("begin", begin);
                }
            }
        }
        return (begin == null) ? begin = getAndHoldStringParameter("begin") : begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return (end == null) ? end = getAndHoldStringParameter("end") : end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String createWrittenTest() throws FenixServiceException {
        final ExecutionCourse executionCourse = getExecutionCourse();

        final List<String> executionCourseIDs = new ArrayList<String>(1);
        executionCourseIDs.add(this.getExecutionCourseID().toString());

        final List<String> degreeModuleScopeIDs = getDegreeModuleScopeIDs(executionCourse);

        try {
            CreateWrittenEvaluation.runCreateWrittenEvaluation(getExecutionCourseID(),
                    DateFormatUtil.parse("dd/MM/yyyy", getDate()), DateFormatUtil.parse("HH:mm", getBeginTime()),
                    DateFormatUtil.parse("HH:mm", getEndTime()), executionCourseIDs, degreeModuleScopeIDs, null, null, null,
                    getDescription());

        } catch (ParseException ex) {
            setErrorMessage("error.invalid.date");
            return "viewCreationPage";
        }

        return "viewCalendar";
    }

    public String editWrittenTest() throws FenixServiceException {
        final ExecutionCourse executionCourse = getExecutionCourse();

        final List<String> executionCourseIDs = new ArrayList<String>(1);
        executionCourseIDs.add(this.getExecutionCourseID().toString());

        final List<String> degreeModuleScopeIDs = getDegreeModuleScopeIDs(executionCourse);

        try {
            EditWrittenEvaluation.runEditWrittenEvaluation(executionCourse.getExternalId(),
                    DateFormatUtil.parse("dd/MM/yyyy", getDate()), DateFormatUtil.parse("HH:mm", getBeginTime()),
                    DateFormatUtil.parse("HH:mm", getEndTime()), executionCourseIDs, degreeModuleScopeIDs, null,
                    getEvaluationID(), null, getDescription(), null);

        } catch (ParseException ex) {
            setErrorMessage("error.invalid.date");
            return "viewEditPage";
        } catch (NotAuthorizedException ex) {
            setErrorMessage(ex.getMessage());
            return "viewEditPage";
        }

        return "viewCalendar";
    }

    public ExecutionCourse getExecutionCourse() {
        return FenixFramework.getDomainObject(getExecutionCourseID());
    }

    private List<String> getDegreeModuleScopeIDs(final ExecutionCourse executionCourse) {
        final List<String> ids = new ArrayList<String>();
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            List<DegreeModuleScope> degreeModuleScopes = curricularCourse.getDegreeModuleScopes();
            for (DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
                if (degreeModuleScope.getCurricularSemester().equals(executionCourse.getExecutionPeriod().getSemester())) {
                    ids.add(degreeModuleScope.getKey());
                }
            }
        }
        return ids;
    }

    public String getBeginTime() {
        return (beginTime == null) ? beginTime = getAndHoldStringParameter("beginTime") : beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getDate() {
        if (date == null) {
            date = getAndHoldStringParameter("date");
            if (date == null) {
                final String parameterString = getRequestParameter("selectedDate");
                if (parameterString != null && parameterString.length() > 0) {
                    date = parameterString;
                    setRequestAttribute("date", date);
                }
            }
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndTime() {
        return (endTime == null) ? endTime = getAndHoldStringParameter("endTime") : endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEvaluationID() {
        return (evaluationID == null) ? evaluationID = getAndHoldStringParameter("evaluationID") : evaluationID;
    }

    public void setEvaluationID(String evaluationID) {
        this.evaluationID = evaluationID;
    }

    public String deleteEvaluation() throws FenixServiceException {
        final String evaluationType = getEvaluationType();
        if (evaluationType.equals(WrittenEvaluation.class.getName())) {
            try {
                DeleteWrittenEvaluation.runDeleteWrittenEvaluation(getExecutionCourseID(), getEvaluationID());
            } catch (NotAuthorizedException ex) {
                setErrorMessage(ex.getMessage());
                return "viewDeletePage";
            } catch (DomainException ex) {
                setErrorMessage(ex.getKey());
                return "viewDeletePage";
            }
        } else {
            try {
                DeleteEvaluation.runDeleteEvaluation(getExecutionCourseID(), getEvaluationID());
            } catch (DomainException ex) {
                setErrorMessage(ex.getKey());
                return "viewDeletePage";
            }
        }
        return "viewCalendar";
    }

    public List<SelectItem> getExecutionCourseGroupings() throws FenixServiceException {
        if (this.executionCourseGroupings == null) {
            this.executionCourseGroupings = new ArrayList<SelectItem>();

            for (Grouping grouping : getExecutionCourse().getGroupings()) {
                this.executionCourseGroupings.add(new SelectItem(grouping.getExternalId(), grouping.getName()));
            }

        }

        return this.executionCourseGroupings;
    }

    private Evaluation getEvaluation() {
        if (this.evalution == null && getEvaluationID() != null) {
            this.evaluation = FenixFramework.getDomainObject(getEvaluationID());
        }

        return this.evaluation;
    }

    private Project getProject() {
        Evaluation evaluation = getEvaluation();
        if (evaluation != null && evaluation.getEvaluationType().equals(EvaluationType.PROJECT_TYPE)) {
            return (Project) evaluation;
        }

        return null;

    }

    public String getGroupingID() {
        if (this.groupingID == null && getProject() != null) {
            Grouping grouping = getProject().getGrouping();

            this.groupingID = (grouping != null) ? grouping.getExternalId() : null;
        }
        return groupingID;
    }

    public void setGroupingID(String groupingID) {
        this.groupingID = groupingID;
    }

    public Grouping getGrouping() {
        if (getProject() != null) {
            return getProject().getGrouping();
        } else {
            return null;
        }
    }

    public Integer getMaxSubmissionsToKeep() {
        if (this.maxSubmissionsToKeep == null && getProject() != null) {
            this.maxSubmissionsToKeep = getProject().getMaxSubmissionsToKeep();
        }
        return maxSubmissionsToKeep;
    }

    public void setMaxSubmissionsToKeep(Integer maxSubmissionsToKeep) {
        this.maxSubmissionsToKeep = maxSubmissionsToKeep;
    }

    public Boolean getOnlineSubmissionsAllowed() {
        if (this.onlineSubmissionsAllowed == null && getProject() != null) {
            this.onlineSubmissionsAllowed = getProject().getOnlineSubmissionsAllowed();
        }
        return onlineSubmissionsAllowed;
    }

    public void setOnlineSubmissionsAllowed(Boolean onlineSubmissionsAllowed) {
        this.onlineSubmissionsAllowed = onlineSubmissionsAllowed;
    }

    public String returnToCalendar() {
        setEvaluationType("");

        return "viewCalendar";
    }

}
