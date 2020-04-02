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
package org.fenixedu.academic.ui.faces.bean.teacher.evaluation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.util.MessageResources;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.EvaluationConfiguration;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.FinalEvaluation;
import org.fenixedu.academic.domain.FinalMark;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.Mark;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.WrittenEvaluationEnrolment;
import org.fenixedu.academic.domain.WrittenTest;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.InfoRoom;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.GOPSendMessageService;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceMultipleException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.resourceAllocationManager.exams.CreateWrittenEvaluation;
import org.fenixedu.academic.service.services.resourceAllocationManager.exams.DeleteWrittenEvaluation;
import org.fenixedu.academic.service.services.resourceAllocationManager.exams.EditWrittenEvaluation;
import org.fenixedu.academic.service.services.teacher.EditWrittenEvaluationEnrolmentPeriod;
import org.fenixedu.academic.service.services.teacher.PublishMarks;
import org.fenixedu.academic.service.services.teacher.TeacherEditWrittenTestRooms;
import org.fenixedu.academic.service.services.teacher.WriteMarks;
import org.fenixedu.academic.service.services.teacher.WriteMarks.AttendsMark;
import org.fenixedu.academic.service.services.teacher.WriteMarks.StudentMark;
import org.fenixedu.academic.service.services.teacher.WrittenEvaluationRoomDistribution;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DateFormatUtil;
import org.fenixedu.academic.util.Season;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EvaluationManagementBackingBean extends FenixBackingBean {

    private static final String FILTER_ALL = "all";

    private static final String ENROLMENT_TYPE_FILTER_NOT_ENROLLED = "not.enrolled";

    protected String executionCourseID;

    private HtmlInputHidden executionCourseIdHidden;

    protected String evaluationID;

    private HtmlInputHidden evaluationIdHidden;

    protected String evaluationTypeClassname;

    protected Integer day;

    protected Integer month;

    protected Integer year;

    protected Integer beginHour;

    protected Integer beginMinute;

    protected Integer endHour;

    protected Integer endMinute;

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

    protected String description;

    private String season;

    protected List<WrittenEvaluationEnrolment> writtenEvaluationEnrolments;

    protected Evaluation evaluation;

    protected String distributeEnroledStudentsOption;

    private boolean resetPosition = false;

    private String publishMarksMessage;

    private Boolean sendSMS;

    private String originPage;

    private String selectedBegin;

    private String selectedEnd;

    protected Map<String, String> marks = new HashMap<>();

    protected String[] attendsIDs;

    protected String submitEvaluationDateTextBoxValue;

    protected HtmlInputText submitEvaluationDateTextBox;

    protected List<FinalMark> alreadySubmitedMarks;

    protected List<Attends> notSubmitedMarks;

    protected Integer[] roomsToDelete;

    protected String[] roomsToAssociate;

    protected Map<String, Boolean> canManageRoomsMap = new HashMap<>();

    protected GradeScale gradeScale;

    protected String enrolmentTypeFilter;

    protected String degreeFilter;

    public EvaluationManagementBackingBean() {
        /*
         * HACK: it's necessary set the executionCourseID for struts menu
         */
        getAndHoldStringParameter("executionCourseID");

        initializeEnrolmentFilter();
        initializeDegreeFilter();
    }

    public String getExecutionCourseID() {
        if (this.executionCourseID == null) {
            if (this.executionCourseIdHidden != null && this.executionCourseIdHidden.getValue() != null) {
                this.executionCourseID = this.executionCourseIdHidden.getValue().toString();
            } else {
                String executionCourseIDString = this.getRequestParameter("executionCourseID");
                if (executionCourseIDString != null && executionCourseIDString.length() > 0) {
                    this.executionCourseID = executionCourseIDString;
                }
            }
        }
        return this.executionCourseID;
    }

    public void setExecutionCourseID(String executionCourseId) {
        this.executionCourseID = executionCourseId;
    }

    public HtmlInputHidden getExecutionCourseIdHidden() {
        if (this.executionCourseIdHidden == null) {
            String executionCourseId = this.getExecutionCourseID();

            this.executionCourseIdHidden = new HtmlInputHidden();
            this.executionCourseIdHidden.setValue(executionCourseId);
            this.executionCourseID = executionCourseId;
        }
        return this.executionCourseIdHidden;
    }

    public void setExecutionCourseIdHidden(HtmlInputHidden executionCourseIdHidden) {
        if (executionCourseIdHidden != null) {
            this.executionCourseID = executionCourseIdHidden.getValue().toString();
            /*
             * HACK: it's necessary set the executionCourseID for struts menu
             */
            setRequestAttribute("executionCourseID", executionCourseIdHidden.getValue());
        }
        this.executionCourseIdHidden = executionCourseIdHidden;
    }

    public String getEvaluationID() {
        if (this.evaluationID == null) {
            if (this.evaluationIdHidden != null && this.evaluationIdHidden.getValue() != null
                    && !this.evaluationIdHidden.getValue().equals("")) {
                this.evaluationID = this.evaluationIdHidden.getValue().toString();
            } else if (this.getRequestParameter("evaluationID") != null && !this.getRequestParameter("evaluationID").equals("")) {
                this.evaluationID = this.getRequestParameter("evaluationID");
            }
        }
        return this.evaluationID;
    }

    public void setEvaluationID(String evaluationId) {
        this.evaluationID = evaluationId;
    }

    public HtmlInputHidden getEvaluationIdHidden() {
        if (this.evaluationIdHidden == null) {
            String evaluationId = this.getEvaluationID();
            this.evaluationIdHidden = new HtmlInputHidden();
            this.evaluationIdHidden.setValue(evaluationId);
        }

        return this.evaluationIdHidden;
    }

    public void setEvaluationIdHidden(HtmlInputHidden evaluationIdHidden) {
        if (evaluationIdHidden != null && evaluationIdHidden.getValue() != null
                && !evaluationIdHidden.getValue().toString().equals("")) {
            this.evaluationID = evaluationIdHidden.getValue().toString();
        }

        this.evaluationIdHidden = evaluationIdHidden;
    }

    public Integer getDay() {
        if (this.day == null && this.getEvaluation() != null) {
            this.day = ((WrittenEvaluation) getEvaluation()).getDay().get(Calendar.DAY_OF_MONTH);
        } else if (this.getRequestParameter("day") != null && !this.getRequestParameter("day").equals("")) {
            this.day = Integer.valueOf(this.getRequestParameter("day"));
        }
        return this.day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        if (this.month == null && this.getEvaluation() != null) {
            this.month = ((WrittenEvaluation) getEvaluation()).getDay().get(Calendar.MONTH) + 1;
        } else if (this.getRequestParameter("month") != null && !this.getRequestParameter("month").equals("")) {
            this.month = Integer.valueOf(this.getRequestParameter("month"));
        }
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        if (this.year == null && this.getEvaluation() != null) {
            this.year = ((WrittenEvaluation) getEvaluation()).getDay().get(Calendar.YEAR);
        } else if (this.getRequestParameter("year") != null && !this.getRequestParameter("year").equals("")) {
            this.year = Integer.valueOf(this.getRequestParameter("year"));
        }
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getBeginHour() {
        if (this.beginHour == null && this.getEvaluation() != null) {
            this.beginHour = ((WrittenEvaluation) getEvaluation()).getBeginning().get(Calendar.HOUR_OF_DAY);
        }
        return this.beginHour;
    }

    public void setBeginHour(Integer beginHour) {
        this.beginHour = beginHour;
    }

    public Integer getBeginMinute() {
        if (this.beginMinute == null && this.getEvaluation() != null) {
            this.beginMinute = ((WrittenEvaluation) getEvaluation()).getBeginning().get(Calendar.MINUTE);
        }
        return this.beginMinute;
    }

    public void setBeginMinute(Integer beginMinute) {
        this.beginMinute = beginMinute;
    }

    public Integer getEndHour() {
        if (this.endHour == null && this.getEvaluation() != null && ((WrittenEvaluation) getEvaluation()).getEnd() != null) {
            this.endHour = ((WrittenEvaluation) getEvaluation()).getEnd().get(Calendar.HOUR_OF_DAY);
        }
        return this.endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() {
        if (this.endMinute == null && this.getEvaluation() != null && ((WrittenEvaluation) getEvaluation()).getEnd() != null) {
            this.endMinute = ((WrittenEvaluation) getEvaluation()).getEnd().get(Calendar.MINUTE);
        }
        return this.endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public String getDescription() {
        if (this.description == null && this.getEvaluation() != null) {
            final Evaluation writtenEvaluation = getEvaluation();
            if (writtenEvaluation instanceof WrittenTest) {
                this.description = ((WrittenTest) writtenEvaluation).getDescription();
            }
        } else if (this.getViewState().getAttribute("description") != null
                && !this.getViewState().getAttribute("description").equals("")) {
            this.description = (String) this.getViewState().getAttribute("description");
        }
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.getViewState().setAttribute("description", description);
    }

    public Integer getEnrolmentBeginDay() {
        if (this.enrolmentBeginDay == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay() != null) {
            this.enrolmentBeginDay =
                    ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay().get(Calendar.DAY_OF_MONTH);
        }
        return this.enrolmentBeginDay;
    }

    public void setEnrolmentBeginDay(Integer enrolmentBeginDay) {
        this.enrolmentBeginDay = enrolmentBeginDay;
    }

    public Integer getEnrolmentBeginHour() {
        if (this.enrolmentBeginHour == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime() != null) {
            this.enrolmentBeginHour =
                    ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime().get(Calendar.HOUR_OF_DAY);
        }
        return this.enrolmentBeginHour;
    }

    public void setEnrolmentBeginHour(Integer enrolmentBeginHour) {
        this.enrolmentBeginHour = enrolmentBeginHour;
    }

    public Integer getEnrolmentBeginMinute() {
        if (this.enrolmentBeginMinute == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime() != null) {
            this.enrolmentBeginMinute = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime().get(Calendar.MINUTE);
        }
        return this.enrolmentBeginMinute;
    }

    public void setEnrolmentBeginMinute(Integer enrolmentBeginMinute) {
        this.enrolmentBeginMinute = enrolmentBeginMinute;
    }

    public Integer getEnrolmentBeginMonth() {
        if (this.enrolmentBeginMonth == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay() != null) {
            this.enrolmentBeginMonth = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay().get(Calendar.MONTH) + 1;
        }
        return enrolmentBeginMonth;
    }

    public void setEnrolmentBeginMonth(Integer enrolmentBeginMonth) {
        this.enrolmentBeginMonth = enrolmentBeginMonth;
    }

    public Integer getEnrolmentBeginYear() {
        if (this.enrolmentBeginYear == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay() != null) {
            this.enrolmentBeginYear = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay().get(Calendar.YEAR);
        }
        return enrolmentBeginYear;
    }

    public void setEnrolmentBeginYear(Integer enrolmentBeginYear) {
        this.enrolmentBeginYear = enrolmentBeginYear;
    }

    public Integer getEnrolmentEndDay() {
        if (this.enrolmentEndDay == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay() != null) {
            this.enrolmentEndDay = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay().get(Calendar.DAY_OF_MONTH);
        }
        return this.enrolmentEndDay;
    }

    public void setEnrolmentEndDay(Integer enrolmentEndDay) {
        this.enrolmentEndDay = enrolmentEndDay;
    }

    public Integer getEnrolmentEndHour() {
        if (this.enrolmentEndHour == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime() != null) {
            this.enrolmentEndHour = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime().get(Calendar.HOUR_OF_DAY);
        }
        return this.enrolmentEndHour;
    }

    public void setEnrolmentEndHour(Integer enrolmentEndHour) {
        this.enrolmentEndHour = enrolmentEndHour;
    }

    public Integer getEnrolmentEndMinute() {
        if (this.enrolmentEndMinute == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime() != null) {
            this.enrolmentEndMinute = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime().get(Calendar.MINUTE);
        }
        return this.enrolmentEndMinute;
    }

    public void setEnrolmentEndMinute(Integer enrolmentEndMinute) {
        this.enrolmentEndMinute = enrolmentEndMinute;
    }

    public Integer getEnrolmentEndMonth() {
        if (this.enrolmentEndMonth == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay() != null) {
            this.enrolmentEndMonth = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay().get(Calendar.MONTH) + 1;
        }
        return enrolmentEndMonth;
    }

    public void setEnrolmentEndMonth(Integer enrolmentEndMonth) {
        this.enrolmentEndMonth = enrolmentEndMonth;
    }

    public Integer getEnrolmentEndYear() {
        if (this.enrolmentEndYear == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay() != null) {
            this.enrolmentEndYear = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay().get(Calendar.YEAR);
        }
        return enrolmentEndYear;
    }

    public void setEnrolmentEndYear(Integer enrolmentEndYear) {
        this.enrolmentEndYear = enrolmentEndYear;
    }

    public String getDistributeEnroledStudentsOption() {
        return (distributeEnroledStudentsOption == null) ? "true" : this.distributeEnroledStudentsOption;
    }

    public void setDistributeEnroledStudentsOption(String distributeEnroledStudentsOption) {
        this.distributeEnroledStudentsOption = distributeEnroledStudentsOption;
    }

    public String getRoomToChangeID() {
        if (getViewState().getAttribute("roomToChangeID") == null) {
            getViewState().setAttribute("roomToChangeID", getElementKeyFor(getEvaluationRoomsPositions(), 1));
        }
        return (String) getViewState().getAttribute("roomToChangeID");
    }

    public void setRoomToChangeID(String roomToChangeID) {
        getViewState().setAttribute("roomToChangeID", roomToChangeID);
    }

    public Integer getNewRoomPosition() {
        if (getViewState().getAttribute("newRoomPosition") == null || this.resetPosition) {
            getViewState().setAttribute("newRoomPosition", 0);
        }
        return (Integer) getViewState().getAttribute("newRoomPosition");
    }

    public void setNewRoomPosition(Integer newRoomPosition) {
        getViewState().setAttribute("newRoomPosition", newRoomPosition);
    }

    public List<Exam> getExamList() {
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(getExecutionCourseID());

        List<Exam> examsList = new ArrayList(executionCourse.getAssociatedExams());
        examsList.sort(new BeanComparator("dayDate"));
        return examsList;
    }

    public List<WrittenTest> getWrittenTestList() {
        ExecutionCourse executionCourse = getExecutionCourse();
        Teacher teacher = AccessControl.getPerson().getTeacher();

        List<WrittenTest> writtenTestList = new ArrayList();
        for (WrittenTest writtenTest : executionCourse.getAssociatedWrittenTests()) {
            writtenTestList.add(writtenTest);
            canManageRoomsMap.put(writtenTest.getExternalId(), writtenTest.canTeacherChooseRoom(executionCourse, teacher));
        }

        writtenTestList.sort(new BeanComparator("dayDate"));
        return writtenTestList;
    }

    public Evaluation getEvaluation() {
        if (this.evaluation == null) {
            if (this.getEvaluationID() != null) {
                evaluation = FenixFramework.getDomainObject(getEvaluationID());
            } else { // Should not happen
                return null;
            }
        }
        return this.evaluation;
    }

    private Calendar getEnrolmentBegin() {
        Calendar result = Calendar.getInstance();

        result.set(getEnrolmentBeginYear(), getEnrolmentBeginMonth() - 1, getEnrolmentBeginDay(), getEnrolmentBeginHour(),
                getEnrolmentBeginMinute());

        return result;
    }

    private Calendar getEnrolmentEnd() {
        Calendar result = Calendar.getInstance();

        result.set(getEnrolmentEndYear(), getEnrolmentEndMonth() - 1, getEnrolmentEndDay(), getEnrolmentEndHour(),
                getEnrolmentEndMinute());

        return result;
    }

    public String editEvaluationEnrolmentPeriod() {
        Calendar enrolmentBeginDay = getEnrolmentBegin();
        Calendar enrolmentBeginTime = getEnrolmentBegin();
        Calendar enrolmentEndDay = getEnrolmentEnd();
        Calendar enrolmentEndTime = getEnrolmentEnd();

        try {
            EditWrittenEvaluationEnrolmentPeriod.runEditWrittenEvaluationEnrolmentPeriod(getExecutionCourseID(),
                    getEvaluationID(), enrolmentBeginDay.getTime(), enrolmentEndDay.getTime(), enrolmentBeginTime.getTime(),
                    enrolmentEndTime.getTime());
        } catch (Exception e) {
            addErrorMessage(e.getMessage());
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        return this.getEvaluation().getClass().getSimpleName();
    }
    
    public String removeEvaluationEnrolmentPeriod() {
        try {
            EditWrittenEvaluationEnrolmentPeriod.removeEnrolmentPeriod(getExecutionCourseID(), getEvaluationID());
        } catch (Exception e) {
            addErrorMessage(e.getMessage());
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        return this.getEvaluation().getClass().getSimpleName();
    }

    public String editMarks() throws FenixServiceException, IllegalDataAccessException {
        if (getEvaluationID() == null) {
            WriteMarks.writeByAttend(getExecutionCourseID(), getEvaluationID(), buildAttendsMark());
            return "";
        }
        try {
            WriteMarks.writeByAttend(getExecutionCourseID(), getEvaluationID(), buildAttendsMark());
        } catch (FenixServiceMultipleException e) {
            for (DomainException domainException : e.getExceptionList()) {
                addErrorMessage(BundleUtil.getString(Bundle.APPLICATION, domainException.getKey(), domainException.getArgs()));
            }
            return "";
        } catch (IllegalDataAccessException idae) {
            addErrorMessage(BundleUtil.getString(Bundle.APPLICATION, "message.teacher.evaluation.editMarks", ExecutionSemester
                    .readActualExecutionSemester().getExecutionYear().getName(), ExecutionSemester.readActualExecutionSemester()
                    .getName()));
            return "";
        }
        return getEvaluation().getClass().getSimpleName();
    }

    private List<AttendsMark> buildAttendsMark() {
        final List<AttendsMark> result = new ArrayList<>();
        for (Entry<String, String> entry : this.marks.entrySet()) {
            result.add(new AttendsMark(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public List<WrittenEvaluationEnrolment> getWrittenEvaluationEnrolments() {

        if (this.writtenEvaluationEnrolments == null) {
            this.writtenEvaluationEnrolments =
                    new ArrayList(((WrittenEvaluation) getEvaluation()).getWrittenEvaluationEnrolmentsSet());
            this.writtenEvaluationEnrolments.sort(new BeanComparator("student.person.username"));
        }
        return this.writtenEvaluationEnrolments;
    }

    public Calendar getBegin() {
        Calendar result = Calendar.getInstance();

        result.set(this.getYear(), this.getMonth() - 1, this.getDay(), this.getBeginHour(), this.getBeginMinute());

        return result;
    }

    public Calendar getEnd() {
        Calendar result = Calendar.getInstance();

        result.set(this.getYear(), this.getMonth() - 1, this.getDay(), this.getEndHour(), this.getEndMinute());

        return result;
    }
    
    public boolean isEnrolmentPeriodDefined() {
    	return ((WrittenEvaluation)getEvaluation()).getEnrollmentBeginDayDateYearMonthDay() != null;
    }

    public String createWrittenTest() {
        List<String> executionCourseIDs = new ArrayList<>();
        executionCourseIDs.add(this.getExecutionCourseID());

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(getExecutionCourseID());

        final List<String> degreeModuleScopesIDs = getDegreeModuleScopeIDs(executionCourse);

        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;
        try {
            CreateWrittenEvaluation.runCreateWrittenEvaluation(this.getExecutionCourseID(), this.getBegin().getTime(), this
                    .getBegin().getTime(), this.getEnd().getTime(), executionCourseIDs, degreeModuleScopesIDs, null, this
                    .getGradeScale(), season, this.getDescription());

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedException) {
                errorMessage = "message.error.notAuthorized";
            }
            if (e instanceof IllegalDataAccessException) {
                errorMessage = "message.error.notAuthorized.create.evaluation.during.exan.period";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        return WrittenTest.class.getSimpleName();
    }

    public ExecutionCourse getExecutionCourse() {
        return FenixFramework.getDomainObject(getExecutionCourseID());
    }

    public String getHackToStoreExecutionCourse() {
        ExecutionCourse course = getExecutionCourse();
        setRequestAttribute("executionCourse", course);
        return "";
    }

    public Map<String, String> getMarks() {
        final Evaluation evaluation = getEvaluation();
        final ExecutionCourse executionCourse = getExecutionCourse();
        if (executionCourse != null) {
            for (final Attends attends : getExecutionCourseAttends()) {
                final Mark mark = attends.getMarkByEvaluation(evaluation);
                if (mark != null && !marks.containsKey(attends.getExternalId())) {
                    marks.put(attends.getExternalId(), mark.getMark());
                }
            }
        }
        return marks;
    }

    public void setMarks(Map<String, String> marks) {
        this.marks = marks;
    }

    public String loadMarks() throws FenixServiceException, ServletException, IOException {
        final HttpServletRequest httpServletRequest =
                (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        final Part fileItem = httpServletRequest.getPart("theFile");
        InputStream inputStream = null;
        try {
            inputStream = fileItem.getInputStream();
            final Map<String, String> marks = loadMarks(inputStream);

            WriteMarks.writeByStudent(getExecutionCourseID(), getEvaluationID(), buildStudentMarks(marks));

            return "success";

        } catch (FenixServiceMultipleException e) {
            for (DomainException domainException : e.getExceptionList()) {
                addErrorMessage(BundleUtil.getString(Bundle.APPLICATION, domainException.getKey(), domainException.getArgs()));
            }
            return "";
        } catch (IOException e) {
            addErrorMessage(BundleUtil.getString(Bundle.APPLICATION, e.getMessage()));
            return "";
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Nothing to do ...
                }
            }
        }
    }

    private List<StudentMark> buildStudentMarks(Map<String, String> marks) {
        final List<StudentMark> result = new ArrayList<>();
        for (final Entry<String, String> entry : marks.entrySet()) {
            result.add(new StudentMark(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    private Map<String, String> loadMarks(final InputStream inputStream) throws IOException {
        final Map<String, String> marks = new HashMap<>();

        final InputStreamReader input = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        final BufferedReader reader = new BufferedReader(input);

        char[] buffer = new char[4096];
        StringBuilder fileContents = new StringBuilder();
        int i = 0;
        while ((i = reader.read(buffer)) != -1) {
            fileContents.append(buffer, 0, i);
        }

        try {
            final StringTokenizer stringTokenizer = new StringTokenizer(fileContents.toString());
            while (true) {
                String studentNumber = getNextToken(stringTokenizer);
                if (studentNumber == null) {
                    return marks;
                }

                String mark = getNextToken(stringTokenizer);
                if (mark == null) {
                    throw new Exception();
                }
                marks.put(studentNumber, mark);
            }
        } catch (Exception e) {
            throw new IOException("error.file.badFormat");
        }
    }

    private String getNextToken(StringTokenizer stringTokenizer) {
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken().trim();
            if (token.length() > 0) {
                return token;
            }
        }
        return null;
    }

    public String editWrittenTest() throws Exception {
        final List<String> executionCourseIDs = new ArrayList<>();
        executionCourseIDs.add(this.getExecutionCourseID());

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(getExecutionCourseID());

        final List<String> degreeModuleScopesIDs = getDegreeModuleScopeIDs(executionCourse);

        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;

        try {
            EditWrittenEvaluation.runEditWrittenEvaluation(this.getExecutionCourseID(), this.getBegin().getTime(), this
                    .getBegin().getTime(), this.getEnd().getTime(), executionCourseIDs, degreeModuleScopesIDs, null,
                    this.evaluationID, season, this.getDescription(), this.getGradeScale());
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedException) {
                errorMessage = "message.error.notAuthorized";
            }
            if (e instanceof IllegalDataAccessException) {
                errorMessage = "message.error.notAuthorized.create.evaluation.during.exan.period";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        final String originPage = getOriginPage();
        if (originPage != null) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getApplicationContext());
            stringBuilder
                    .append("/resourceAllocationManager/searchWrittenEvaluationsByDate.do?method=returnToSearchPage&amp;page=0&date=");
            stringBuilder.append(DateFormatUtil.format("yyyy/MM/dd", this.getBegin().getTime()));
            stringBuilder.append("&");
            stringBuilder.append(PresentationConstants.EXECUTION_PERIOD_OID);
            stringBuilder.append("=");
            stringBuilder.append(executionCourse.getExecutionPeriod().getExternalId());
            if (selectedBegin != null && selectedBegin.length() > 0 && selectedBegin.equals("true")) {
                stringBuilder.append("selectedBegin=");
                stringBuilder.append(DateFormatUtil.format("HH:mm", this.getBegin().getTime()));
            }
            if (selectedEnd != null && selectedEnd.length() > 0 && selectedEnd.equals("true")) {
                stringBuilder.append("selectedEnd=");
                stringBuilder.append(DateFormatUtil.format("HH:mm", this.getEnd().getTime()));
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect(stringBuilder.toString());
            return originPage;
        } else {
            return this.getEvaluation().getClass().getSimpleName();
        }
    }

    private List<String> getDegreeModuleScopeIDs(ExecutionCourse executionCourse) {
        final List<String> degreeModuleScopesIDs = new ArrayList<>();
        for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            List<DegreeModuleScope> degreeModuleScopes = curricularCourse.getDegreeModuleScopes();
            for (DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
                if (degreeModuleScope.getCurricularSemester().equals(executionCourse.getExecutionPeriod().getSemester())) {
                    degreeModuleScopesIDs.add(degreeModuleScope.getKey());
                }
            }
        }
        return degreeModuleScopesIDs;
    }

    protected String getApplicationContext() {
        return getRequest().getContextPath();
    }

    public String deleteWrittenTest() {
        try {
            DeleteWrittenEvaluation.runDeleteWrittenEvaluation(this.getExecutionCourseID(), this.getEvaluationID());
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

    public List<InfoRoom> getEvaluationRooms() {
        final Space[] result = new Space[getEvaluationRoomsPositions().size()];
        for (final Entry<String, Integer> entry : getEvaluationRoomsPositions().entrySet()) {
            final Space room = getRoom(entry.getKey());
            result[entry.getValue() - 1] = room;
        }
//        return Arrays.asList(result);

        return Arrays.stream(result).map(InfoRoom::new).collect(Collectors.toList());
    }

    private Space getRoom(final String roomID) {
        for (final WrittenEvaluationSpaceOccupation roomOccupation : ((WrittenEvaluation) getEvaluation())
                .getWrittenEvaluationSpaceOccupationsSet()) {
            if (roomOccupation.getRoom().getExternalId().equals(roomID)) {
                return roomOccupation.getRoom();
            }
        }
        return null;
    }

    public Map<String, Integer> getEvaluationRoomsPositions() {
        if (getViewState().getAttribute("evaluationRooms") == null) {
            final Map<String, Integer> evaluationRooms = initializeEvaluationRoomsPositions();
            getViewState().setAttribute("evaluationRooms", evaluationRooms);
        }
        return (Map<String, Integer>) getViewState().getAttribute("evaluationRooms");
    }

    public Integer getExamCapacity(Space space) {
        Optional<Integer> metadata = space.getMetadata("examCapacity");
        return metadata.orElse(0);
    }

    private Map<String, Integer> initializeEvaluationRoomsPositions() {
        final Map<String, Integer> evaluationRooms = new TreeMap();
        WrittenEvaluation writtenEvaluation = (WrittenEvaluation) getEvaluation();

        final List<WrittenEvaluationSpaceOccupation> roomOccupations =
                writtenEvaluation.getWrittenEvaluationSpaceOccupationsSet().stream()
                        .sorted((o1, o2) -> getExamCapacity(o2.getRoom()).compareTo(getExamCapacity(o1.getRoom())))
                        .collect(Collectors.toList());
        int count = 0;
        for (final WrittenEvaluationSpaceOccupation roomOccupation : roomOccupations) {
            evaluationRooms.put(roomOccupation.getRoom().getExternalId(), ++count);
        }
        return evaluationRooms;
    }

    public void changeRoom(ValueChangeEvent valueChangeEvent) {
        this.resetPosition = true;
    }

    public void changePosition(ValueChangeEvent valueChangeEvent) {
        final Integer roomToChangeNewPosition = (Integer) valueChangeEvent.getNewValue();
        if (roomToChangeNewPosition != 0) {
            final Integer roomToChangeOldPosition = getEvaluationRoomsPositions().get(getRoomToChangeID());
            final String elementKey = getElementKeyFor(getEvaluationRoomsPositions(), roomToChangeNewPosition);
            getEvaluationRoomsPositions().put(elementKey, roomToChangeOldPosition);
            getEvaluationRoomsPositions().put(getRoomToChangeID(), roomToChangeNewPosition);
        }
    }

    private String getElementKeyFor(Map<String, Integer> evaluationRooms, Integer roomPosition) {
        for (final Entry<String, Integer> entry : evaluationRooms.entrySet()) {
            if (entry.getValue().equals(roomPosition)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public List<SelectItem> getNames() {
        final List<SelectItem> result =
                new ArrayList<>(((WrittenEvaluation) getEvaluation()).getWrittenEvaluationSpaceOccupationsSet().size());
        for (final WrittenEvaluationSpaceOccupation roomOccupation : ((WrittenEvaluation) getEvaluation())
                .getWrittenEvaluationSpaceOccupationsSet()) {
            result.add(new SelectItem(roomOccupation.getRoom().getExternalId(), (roomOccupation.getRoom()).getName()));
        }
        return result;
    }

    public List<SelectItem> getPositions() {
        final List<SelectItem> result = new ArrayList(getEvaluationRoomsPositions().size());
        result.add(new SelectItem(0, ""));
        for (final Integer value : getEvaluationRoomsPositions().values()) {
            result.add(new SelectItem(value, value.toString()));
        }
        result.sort(new BeanComparator("label"));
        this.resetPosition = true;
        return result;
    }

    public String distributeStudentsByRooms() {
        try {
            final Boolean distributeOnlyEnroledStudents = Boolean.valueOf(this.getDistributeEnroledStudentsOption());
            WrittenEvaluationRoomDistribution.runWrittenEvaluationRoomDistribution(getExecutionCourseID(), getEvaluationID(),
                    getRoomIDs(), distributeOnlyEnroledStudents);
            return "enterShowStudentsEnroled";
        } catch (Exception e) {
            setErrorMessage(e.getMessage());
            return "";
        }
    }

    private List<String> getRoomIDs() {
        final List<InfoRoom> rooms = getEvaluationRooms();
        final List<String> result = new ArrayList(rooms.size());
        for (final InfoRoom room : rooms) {
            result.add(room.getExternalId());
        }
        return result;
    }

    public String checkIfCanDistributeStudentsByRooms() {
        try {
            final Evaluation writtenEvaluation = getEvaluation();
            ((WrittenEvaluation) writtenEvaluation).checkIfCanDistributeStudentsByRooms();
            return "enterDistributeStudentsByRooms";
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }
    }

    public boolean getExistsADistribution() {
        final Evaluation writtenEvaluation = getEvaluation();
        if (writtenEvaluation != null) {
            return !((WrittenEvaluation) writtenEvaluation).getWrittenEvaluationEnrolmentsSet().isEmpty();
        }
        return false;
    }

    public int getNumberOfAttendingStudents() {
        int numberOfAttendingStudents = 0;
        for (final ExecutionCourse executionCourse : getEvaluation().getAssociatedExecutionCoursesSet()) {
            numberOfAttendingStudents += executionCourse.getAttendsSet().size();
        }
        return numberOfAttendingStudents;
    }

    public FinalEvaluation getFinalEvaluation() {
        for (final Evaluation evaluation : getExecutionCourse().getAssociatedEvaluationsSet()) {
            if (evaluation instanceof FinalEvaluation) {
                return (FinalEvaluation) evaluation;
            }
        }
        return null;
    }

    public List<Attends> getExecutionCourseAttends() {
        List<Attends> result;

        Set<Attends> attendsSet = getExecutionCourse().getAttendsSet().stream()
                .filter(a -> a.getEnrolment() == null || !a.getEnrolment().isImpossible())
                .collect(Collectors.toSet());

        String enrolmentTypeFilter = getEnrolmentTypeFilter();
        String degreeFilter = getDegreeFilter();

        if (enrolmentTypeFilter.equals(FILTER_ALL)) {
            result = new ArrayList<>(attendsSet);
        } else if (enrolmentTypeFilter.equals(ENROLMENT_TYPE_FILTER_NOT_ENROLLED)) {
            result = attendsSet.stream()
                    .filter(a -> a.getEnrolment() == null)
                    .collect(Collectors.toList());
        } else {
            EvaluationSeason season = FenixFramework.getDomainObject(enrolmentTypeFilter);
            result = attendsSet.stream()
                    .filter(a -> a.getEnrolment() != null && a.getEnrolment().getEvaluationSeason().equals(season))
                    .collect(Collectors.toList());
        }

        if (!degreeFilter.equals(FILTER_ALL)) {
            Degree degree = FenixFramework.getDomainObject(degreeFilter);
            result = result.stream()
                    .filter(a -> a.getRegistration().getDegree().equals(degree))
                    .collect(Collectors.toList());
        }

        result.sort(Attends.COMPARATOR_BY_STUDENT_NUMBER);
        return result;
    }

    public List<Student> getStudentsWithImpossibleEnrolments() {
        final List<Student> result = new ArrayList<>();
        for (final Attends attends : getExecutionCourse().getAttendsSet()) {
            if (attends.getEnrolment() != null && attends.getEnrolment().isImpossible()) {
                final Student student = attends.getEnrolment().getStudentCurricularPlan().getRegistration().getStudent();
                if (!result.contains(student)) {
                    result.add(student);
                }
            }
        }
        return result;
    }

    public String getPublishMarksMessage() {
        return publishMarksMessage;
    }

    public void setPublishMarksMessage(String publishMarksMessage) {
        this.publishMarksMessage = publishMarksMessage;
    }

    public Boolean getSendSMS() {
        return sendSMS;
    }

    public void setSendSMS(Boolean sendSMS) {
        this.sendSMS = sendSMS;
    }

    public String publishMarks() {
        final MessageResources messages = MessageResources.getMessageResources(Bundle.APPLICATION);
        final String announcementTitle =
                (getPublishMarksMessage() != null && getPublishMarksMessage().length() > 0) ? messages
                        .getMessage("message.publishment") : null;

        try {
            PublishMarks.runPublishMarks(this.getExecutionCourseID(), this.getEvaluationID(), this.getPublishMarksMessage(),
                    this.getSendSMS(), announcementTitle);
        } catch (Exception e) {
            this.setErrorMessage(e.getMessage());
            return "";
        }

        return this.getEvaluation().getClass().getSimpleName();
    }

    public String getEvaluationTypeClassname() {
        String evaluationTypeClassname = (String) this.getViewState().getAttribute("evaluationTypeClassname");
        if (evaluationTypeClassname == null) {
            evaluationTypeClassname = this.getRequestParameter("evaluationTypeClassname");
            if (evaluationTypeClassname != null) {
                setEvaluationTypeClassname(evaluationTypeClassname);
            }
        }
        if (evaluationTypeClassname == null) {
            final Evaluation evaluation = getEvaluation();
            if (evaluation != null) {
                evaluationTypeClassname = evaluation.getClass().getName();
                setEvaluationTypeClassname(evaluationTypeClassname);
            }
        }
        return evaluationTypeClassname;
    }

    public void setEvaluationTypeClassname(String evaluationTypeClassname) {
        this.getViewState().setAttribute("evaluationTypeClassname", evaluationTypeClassname);
    }

    public String getSeason() {
        if (season == null) {
            final Evaluation evaluation = getEvaluation();
            if (evaluation != null && evaluation instanceof Exam) {
                final Exam exam = (Exam) evaluation;
                season = exam.getSeason().toString();
            }
        }
        if (this.getViewState().getAttribute("season") != null && !this.getViewState().getAttribute("season").equals("")) {
            this.season = (String) this.getViewState().getAttribute("season");
        }
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
        this.getViewState().setAttribute("season", season);
    }

    public String getOriginPage() {
        if (originPage == null) {
            originPage = getRequestParameter("originPage");
        }
        return originPage;
    }

    public void setOriginPage(String originPage) {
        this.originPage = originPage;
    }

    public String getSelectedBegin() {
        if (selectedBegin == null) {
            selectedBegin = getRequestParameter("selectedBegin");
        }
        return selectedBegin;
    }

    public void setSelectedBegin(String selectedBegin) {
        this.selectedBegin = selectedBegin;
    }

    public String getSelectedEnd() {
        if (selectedEnd == null) {
            selectedEnd = getRequestParameter("selectedEnd");
        }
        return selectedEnd;
    }

    public void setSelectedEnd(String selectedEnd) {
        this.selectedEnd = selectedEnd;
    }

    // Submit Marks
    public List<FinalMark> getAlreadySubmitedMarks() {
        if (this.alreadySubmitedMarks == null) {
            FinalEvaluation evaluation = (FinalEvaluation) getEvaluation();
            ExecutionCourse executionCourse = getExecutionCourse();
            this.alreadySubmitedMarks = evaluation.getAlreadySubmitedMarks(executionCourse);
            this.alreadySubmitedMarks.sort(new BeanComparator("attend.aluno.number"));
        }
        return this.alreadySubmitedMarks;
    }

    public List<Attends> getNotSubmitedMarks() {
        if (this.notSubmitedMarks == null) {
            FinalEvaluation evaluation = (FinalEvaluation) getEvaluation();
            ExecutionCourse executionCourse = getExecutionCourse();
            this.notSubmitedMarks = evaluation.getNotSubmitedMarkAttends(executionCourse);
            this.notSubmitedMarks.sort(new BeanComparator("aluno.number"));
        }
        return this.notSubmitedMarks;
    }

    public String submitMarks() {
        HttpServletRequest request = getRequest();
        attendsIDs = request.getParameterValues("selectedMarks");
        if (attendsIDs == null || attendsIDs.length == 0) {
            setErrorMessage("error.noStudents.selected");
            return "";
        }
        this.getViewState().setAttribute("attendIDs", attendsIDs);
        return "enterSubmitMarksList2";
    }

    public String getSubmitEvaluationDateTextBoxValue() {
        if (submitEvaluationDateTextBoxValue == null) {
            List<Exam> exams = getExamList();
            if (exams != null && !exams.isEmpty()) {
                Exam exam = exams.get(exams.size() - 1);
                submitEvaluationDateTextBoxValue = exam.getDayDateYearMonthDay().toString("dd/MM/yyyy");
            }
        }
        return submitEvaluationDateTextBoxValue;
    }

    public void setSubmitEvaluationDateTextBoxValue(String submitEvaluationDateTextBoxValue) {
        this.submitEvaluationDateTextBoxValue = submitEvaluationDateTextBoxValue;
    }

    public HtmlInputText getSubmitEvaluationDateTextBox() {
        return submitEvaluationDateTextBox;
    }

    public void setSubmitEvaluationDateTextBox(HtmlInputText submitEvaluationDateTextBox) {
        this.submitEvaluationDateTextBox = submitEvaluationDateTextBox;
    }

    public List<SelectItem> getAvailableRooms() {
        List<SelectItem> result = new ArrayList<>();
        WrittenTest writtenTest = (WrittenTest) getEvaluation();
        Teacher teacher = AccessControl.getPerson().getTeacher();
        for (Space room : writtenTest.getAvailableRooms()) {
            SelectItem selectItem;
            selectItem = new SelectItem(room.getExternalId(), room.getName());
            selectItem.setDisabled(!writtenTest.canTeacherRemoveRoom(getExecutionCourse().getExecutionPeriod(), teacher, room));
            result.add(selectItem);
        }
        return result;
    }

    public String[] getRoomsToAssociate() {
        if (roomsToAssociate == null) {
            List<String> roomIds = new ArrayList<>();
            for (Space room : ((WrittenTest) getEvaluation()).getAssociatedRooms()) {
                roomIds.add(room.getExternalId());
            }
            roomsToAssociate = roomIds.toArray(new String[] {});
        }
        return roomsToAssociate;
    }

    public void setRoomsToAssociate(String[] roomsToAssociate) {
        this.roomsToAssociate = roomsToAssociate;
    }

    public String editEvaluationRooms() throws FenixServiceException {

        TeacherEditWrittenTestRooms.runTeacherEditWrittenTestRooms(getExecutionCourse(), AccessControl.getPerson().getTeacher(),
                (WrittenTest) getEvaluation(), getRooms(getRoomsToAssociate()));
        setRoomsToAssociate(null);

        return "";
    }

    private List<Space> getRooms(String[] roomsToAssociate) {
        List<Space> rooms = new ArrayList<>();
        for (String roomId : roomsToAssociate) {
            Space space = (Space) FenixFramework.getDomainObject(roomId);
            if (space == null) {
                throw new IllegalArgumentException();
            }
            rooms.add(space);
        }
        return rooms;
    }

    public Map<String, Boolean> getCanManageRoomsMap() {
        return canManageRoomsMap;
    }

    public void setCanManageRoomsMap(Map<String, Boolean> canManageRoomsMap) {
        this.canManageRoomsMap = canManageRoomsMap;
    }

    public GradeScale getGradeScale() {
        if (gradeScale == null && this.getEvaluation() != null) {
            this.gradeScale = getEvaluation().getGradeScale();
        }
        return this.gradeScale;
    }

    public void setGradeScale(GradeScale gradeScale) {
        this.gradeScale = gradeScale;
    }

    public List<SelectItem> getGradeScaleOptions() {
        List<GradeScale> scales = GradeScale.getPublicGradeScales();
        List<SelectItem> items = new ArrayList<>();

        for (GradeScale s : scales) {
            items.add(new SelectItem(s, BundleUtil.getString(Bundle.ENUMERATION, s.getName())));
        }

        return items;
    }

    public String getGradeScaleDescription() {
        GradeScale gradeScale = getGradeScale();
        if (gradeScale != null) {
            return getGradeScale().getPossibleValueDescription(getEvaluation().isFinal());
        }
        return "";
    }

    public void exportStudentsEnroledToExcel() throws FenixServiceException {
        exportToExcel();
    }

    private String getFileName(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return (day + "_" + month + "_" + year + "-" + hour + ":" + minutes);
    }

    public void exportToExcel() throws FenixServiceException {
        String filename =
                BundleUtil.getString(Bundle.APPLICATION, "title.enrolments") + "-"
                        + getFileName(Calendar.getInstance().getTime());
        try {
            exportToXls(filename.replace(" ", "_"));
        } catch (IOException e) {
            throw new FenixServiceException();
        }
    }

    private void exportToXls(String filename) throws IOException {
        this.getResponse().setContentType("application/vnd.ms-excel");
        this.getResponse().setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
        ServletOutputStream outputStream = this.getResponse().getOutputStream();

        String spreadSheetName = BundleUtil.getString(Bundle.APPLICATION, "title.enrolments");
        List<Object> headers = getStudentsEnroledListHeaders();
        Spreadsheet spreadsheet = new Spreadsheet(spreadSheetName, headers);

        reportInfo(spreadsheet);

        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        this.getResponse().flushBuffer();
        FacesContext.getCurrentInstance().responseComplete();
    }

    private List<Object> getStudentsEnroledListHeaders() {
        final List<Object> headers = new ArrayList<>();
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.username"));
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.number"));
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.name"));
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.room"));
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.degree.name"));
        return headers;
    }

    private void reportInfo(Spreadsheet spreadsheet) {
        fillStudentsEnroled(spreadsheet);
    }

    private void fillStudentsEnroled(Spreadsheet spreadsheet) {

        for (WrittenEvaluationEnrolment enrolment : getWrittenEvaluationEnrolments()) {
            final Row newRow = spreadsheet.addRow();
            newRow.setCell(enrolment.getStudent().getPerson().getUsername());
            newRow.setCell(enrolment.getStudent().getNumber().toString());
            newRow.setCell(enrolment.getStudent().getPerson().getName());
            newRow.setCell(enrolment.getRoom() != null ? enrolment.getRoom().getName() : "-");
            newRow.setCell(enrolment.getStudent().getDegree().getNameI18N().getContent());
        }
    }

    public boolean getMixedGrades() {
        final Evaluation evaluation = getEvaluation();
        final GradeScale gradeScale = getGradeScale();

        if (gradeScale == null) {
            return false;
        }

        for (String mark : getMarks().values()) {
            if (!gradeScale.isValid(mark, evaluation.getEvaluationType())) {
                return true;
            }
        }
        return false;
    }

    public List<SelectItem> getEnrolmentTypeFilterOptions() {
        List<SelectItem> options = new ArrayList<>();

        options.add(new SelectItem(FILTER_ALL, BundleUtil.getString(Bundle.ENUMERATION, "filter.all")));
        for (EvaluationSeason season : EvaluationConfiguration.getInstance().getEvaluationSeasonSet()) {
            options.add(new SelectItem(season.getExternalId(), season.getName().getContent()));
        }
        options.add(new SelectItem(ENROLMENT_TYPE_FILTER_NOT_ENROLLED, BundleUtil.getString(Bundle.ENUMERATION,
                "filter.not.enrolled")));

        return options;
    }

    public String getEnrolmentTypeFilter() {
        return enrolmentTypeFilter;
    }

    public void setEnrolmentTypeFilter(String filter) {
        enrolmentTypeFilter = filter;
    }

    public List<SelectItem> getDegreeFilterOptions() {
        List<SelectItem> options = getExecutionCourse().getDegreesSortedByDegreeName().stream()
                .map(d -> new SelectItem(d.getExternalId(), d.getPresentationName()))
                .collect(Collectors.toList());
        options.add(0, new SelectItem(FILTER_ALL, BundleUtil.getString(Bundle.ENUMERATION, "filter.all")));

        return options;
    }

    public boolean hasDegreeFilter() {
        return getExecutionCourse().getDegreesSortedByDegreeName().size() > 1;
    }

    public String getDegreeFilter() {
        return degreeFilter;
    }

    public void setDegreeFilter(String filter) {
        degreeFilter = filter;
    }

    public String filterByEnrolmentType() {
        marks.clear();
        return null;
    }

    private UIComponent findComponent(UIComponent c, String id) {
        if (id.equals(c.getId())) {
            return c;
        }

        Iterator<UIComponent> childIt = c.getFacetsAndChildren();
        while (childIt.hasNext()) {
            UIComponent found = findComponent(childIt.next(), id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private void initializeEnrolmentFilter() {
        Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        UIComponent filterComponent = findComponent(FacesContext.getCurrentInstance().getViewRoot(), "enrolmentFilter");

        if (filterComponent != null) {
            String clientId = filterComponent.getClientId(FacesContext.getCurrentInstance());
            enrolmentTypeFilter = (String) map.get(clientId);
        } else {
            enrolmentTypeFilter = FILTER_ALL;
        }
    }

    private void initializeDegreeFilter() {
        Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        UIComponent filterComponent = findComponent(FacesContext.getCurrentInstance().getViewRoot(), "degreeFilter");

        if (filterComponent != null) {
            String clientId = filterComponent.getClientId(FacesContext.getCurrentInstance());
            degreeFilter = (String) map.get(clientId);
        } else {
            degreeFilter = FILTER_ALL;
        }
    }

    @Atomic
    public String sendEmailRequestRoom() {
        GOPSendMessageService.requestRoom((WrittenTest) getEvaluation());
        return null;
    }
}
