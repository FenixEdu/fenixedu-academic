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
package org.fenixedu.academic.ui.faces.bean.departmentMember.teacherService;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.commons.ReadExecutionPeriodsByExecutionYear;
import org.fenixedu.academic.service.services.commons.ReadExecutionYearByID;
import org.fenixedu.academic.service.services.commons.ReadNotClosedExecutionYears;
import org.fenixedu.academic.service.services.commons.ReadPreviousExecutionPeriod;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.teacher.teacherService.ReadTeacherServiceDistributionByCourse;
import org.fenixedu.academic.service.services.teacher.teacherService.ReadTeacherServiceDistributionByTeachers;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.dto.InfoExecutionYear;
import org.fenixedu.academic.dto.teacher.distribution.DistributionTeacherServicesByCourseDTO.ExecutionCourseDistributionServiceEntryDTO;
import org.fenixedu.academic.dto.teacher.distribution.DistributionTeacherServicesByCourseDTO.TeacherExecutionCourseServiceDTO;
import org.fenixedu.academic.dto.teacher.distribution.DistributionTeacherServicesByTeachersDTO.TeacherDistributionServiceEntryDTO;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.util.Bundle;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

/**
 * 
 * @author amak, jpmsit
 * 
 */
public class ViewTeacherService extends FenixBackingBean {

    private List<TeacherDistributionServiceEntryDTO> teacherServiceDTO;

    private List<ExecutionCourseDistributionServiceEntryDTO> executionCourseServiceDTO;

    private String selectedExecutionYearID;

    private String selectedExecutionYearName;

    private UISelectItems executionYearItems;

    private Integer selectedExecutionPeriodID;

    private UISelectItems executionPeriodsItems;

    private final int BOTH_SEMESTERS_ID = 0;

    private final int FIRST_SEMESTER_ID = 1;

    private final int SECOND_SEMESTER_ID = 2;

    private static final int NUMBER_OF_FIXED_COLUMNS = 4;

    private static final int HOURS_PER_SHIFT_INFORMATION_COLUMNS = 9;

    private static final int STUDENT_ENROLMENT_INFORMATION_COLUMNS = 2;

    private static final int COURSE_INFORMATION_COLUMNS = 4;

    private static final int STUDENTS_PER_SHIFT_INFORMATION_COLUMNS = 9;

    private InfoExecutionYear previousExecutionYear = null;

    public Integer getColumnsCount() {
        int totalColumns = NUMBER_OF_FIXED_COLUMNS;

        totalColumns += (this.getViewCourseInformation() == true) ? COURSE_INFORMATION_COLUMNS : 0;
        totalColumns += (this.getViewHoursPerShift() == true) ? HOURS_PER_SHIFT_INFORMATION_COLUMNS : 0;
        totalColumns += (this.getViewStudentsEnrolments() == true) ? STUDENT_ENROLMENT_INFORMATION_COLUMNS : 0;
        totalColumns += (this.getViewStudentsPerShift() == true) ? STUDENTS_PER_SHIFT_INFORMATION_COLUMNS : 0;

        return totalColumns;

    }

    public UISelectItems getExecutionYearItems() throws FenixServiceException {
        if (this.executionYearItems == null) {
            this.executionYearItems = new UISelectItems();
            executionYearItems.setValue(this.getExecutionYears());
        }

        return executionYearItems;
    }

    public UISelectItems getExecutionPeriodsItems() throws FenixServiceException {
        if (this.executionPeriodsItems == null) {
            this.executionPeriodsItems = new UISelectItems();
            this.executionPeriodsItems.setValue(this.getExecutionPeriods());
        }

        return executionPeriodsItems;
    }

    public void setExecutionYearItems(UISelectItems selectItems) {
        this.executionYearItems = selectItems;
    }

    public String getSelectedExecutionYearID() throws FenixServiceException {
        String executionYearIDString = this.getRequestParameter("teacherServiceForm:selectedExecutionYearID");

        if (executionYearIDString != null) {
            this.selectedExecutionYearID = this.getRequestParameter("teacherServiceForm:selectedExecutionYearID");
        } else {
            if (this.selectedExecutionYearID == null) {
                List<SelectItem> executionYearItems = (List<SelectItem>) this.getExecutionYearItems().getValue();
                this.selectedExecutionYearID = (String) executionYearItems.iterator().next().getValue();
            }

        }

        return this.selectedExecutionYearID;
    }

    public Integer getSelectedExecutionPeriodID() {

        String executionPeriodIDString = this.getRequestParameter("teacherServiceForm:selectedExecutionPeriodID");

        if (executionPeriodIDString != null) {
            selectedExecutionPeriodID = Integer.valueOf(this.getRequestParameter("teacherServiceForm:selectedExecutionPeriodID"));
        } else if (selectedExecutionPeriodID == null) {
            selectedExecutionPeriodID = BOTH_SEMESTERS_ID;
        }

        return selectedExecutionPeriodID;
    }

    public void setSelectedExecutionYearID(String selectedExecutionYearID) throws FenixServiceException {

        this.selectedExecutionYearID = selectedExecutionYearID;
    }

    public String getSelectedExecutionYearName() throws FenixServiceException {

        return selectedExecutionYearName;
    }

    public void setSelectedExecutionYearName(String selectedExecutionYearName) {
        this.selectedExecutionYearName = selectedExecutionYearName;
    }

    public List getTeacherServiceDTO() throws FenixServiceException, ParseException {
        try {
            if (teacherServiceDTO == null) {
                loadDistributionServiceData();
            }
            return teacherServiceDTO;
        } catch (NotAuthorizedException ex1) {
            return null;
        }
    }

    public void setTeacherServiceDTO(List<TeacherDistributionServiceEntryDTO> teacherServiceDTO) {
        this.teacherServiceDTO = teacherServiceDTO;
    }

    public List<ExecutionCourseDistributionServiceEntryDTO> getExecutionCourseServiceDTO() throws FenixServiceException {
        if (executionCourseServiceDTO == null) {
            loadDistributionServiceDataByCourse();
        }
        return executionCourseServiceDTO;
    }

    public void setExecutionCourseServiceDTO(List<ExecutionCourseDistributionServiceEntryDTO> executionCourseServiceDTO) {
        this.executionCourseServiceDTO = executionCourseServiceDTO;
    }

    private List<SelectItem> getExecutionYears() throws FenixServiceException {

        List<InfoExecutionYear> executionYears = ReadNotClosedExecutionYears.run();

        List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
        ExecutionSemester lastExecutionSemester = ExecutionSemester.readActualExecutionSemester();

        for (InfoExecutionYear executionYear : executionYears) {
            if (executionYear.getExecutionYear().isBeforeOrEquals(lastExecutionSemester.getExecutionYear())) {
                result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
            }
        }

        return result;

    }

    private List<SelectItem> getExecutionPeriods() throws FenixServiceException {

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(BOTH_SEMESTERS_ID, BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.bothSemesters")));
        result.add(new SelectItem(FIRST_SEMESTER_ID, BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.firstSemester")));
        result.add(new SelectItem(SECOND_SEMESTER_ID, BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.secondSemester")));

        return result;

    }

    public String getDepartmentName() {
        Person person = getUserView().getPerson();
        Department department = person.getEmployee() != null ? person.getEmployee().getCurrentDepartmentWorkingPlace() : null;
        return department == null ? null : department.getRealName();
    }

    public String getTeacherService() throws FenixServiceException, ParseException {

        InfoExecutionYear infoExecutionYear = ReadExecutionYearByID.run(this.getSelectedExecutionYearID());

        this.selectedExecutionYearName = infoExecutionYear.getYear();

        loadDistributionServiceData();
        return "listDistributionTeachersByTeacher";
    }

    public String getTeacherServiceByCourse() throws FenixServiceException {
        loadDistributionServiceDataByCourse();
        return "listDistributionTeachersByCourse";
    }

    private void loadDistributionServiceData() throws FenixServiceException, ParseException {

        List<String> ExecutionPeriodsIDs = buildExecutionPeriodsIDsList();

        this.teacherServiceDTO =
                ReadTeacherServiceDistributionByTeachers.run(getUserView().getPerson().getEmployee()
                        .getCurrentDepartmentWorkingPlace().getExternalId(), ExecutionPeriodsIDs);

    }

    private void loadDistributionServiceDataByCourse() throws FenixServiceException {

        List<String> ExecutionPeriodsIDs = buildExecutionPeriodsIDsList();

        Object[] args =
                { getUserView().getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getExternalId(), ExecutionPeriodsIDs };

        this.executionCourseServiceDTO =
                ReadTeacherServiceDistributionByCourse.runReadTeacherServiceDistributionByCourse(getUserView().getPerson()
                        .getEmployee().getCurrentDepartmentWorkingPlace().getExternalId(), ExecutionPeriodsIDs);

    }

    public void exportTeacherServiceByCourseToXLS() throws FenixServiceException, IOException {
        if (this.executionCourseServiceDTO == null) {
            loadDistributionServiceDataByCourse();
        }

        final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("teacher distribution");
        fillSpreadSheetHeaders(spreadsheet);
        fillSpreadSheedResults(spreadsheet);

        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

        String fileName = "file.xls";
        String contentType = "application/vnd.ms-excel";

        response.reset();
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        OutputStream output = response.getOutputStream();
        spreadsheet.getWorkbook().write(output);

        fc.responseComplete();
    }

    private void fillSpreadSheetHeaders(final StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.newHeaderRow();

        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.name"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.campus"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.degrees"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.curricularYears"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.semester"));

        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.firstTimeEnrolledStudentsNumber"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.secondTimeEnrolledStudentsNumber"));

        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.totalStudentsNumber"));

        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.theoreticalHours"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.praticalHours"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.laboratorialHours"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.theoPratHours"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.seminary"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.problems"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.tutorialOrientation"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.fieldWork"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.trainingPeriod"));

        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.totalHours"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.availability"));

        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.studentsNumberByTheoreticalShift"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.studentsNumberByPraticalShift"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.studentsNumberByLaboratorialShift"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.studentsNumberByTheoPraticalShift"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.studentsNumberBySeminaryShift"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.studentsNumberByProblemsShift"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.studentsNumberByTutorialOrientationShift"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.studentsNumberByFieldWorkShift"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.course.studentsNumberByTrainingPeriodShift"));

    }

    private void fillSpreadSheedResults(final StyledExcelSpreadsheet spreadsheet) {

        for (ExecutionCourseDistributionServiceEntryDTO entry : this.executionCourseServiceDTO) {
            int teacherColumns =
                    NUMBER_OF_FIXED_COLUMNS + HOURS_PER_SHIFT_INFORMATION_COLUMNS + STUDENT_ENROLMENT_INFORMATION_COLUMNS
                            + COURSE_INFORMATION_COLUMNS + STUDENTS_PER_SHIFT_INFORMATION_COLUMNS;
            spreadsheet.newRow();
            spreadsheet.addCell(entry.getExecutionCourseName());
            spreadsheet.addCell(entry.getExecutionCourseCampus());
            spreadsheet.addCell(entry.getExecutionCourseDegreeList());
            spreadsheet.addCell(entry.getCurricularYearListString());
            spreadsheet.addCell(entry.getExecutionCourseSemester());

            spreadsheet.addCell(entry.getExecutionCourseFirstTimeEnrollementStudentNumber());
            spreadsheet.addCell(entry.getExecutionCourseSecondTimeEnrollementStudentNumber());

            spreadsheet.addCell(entry.getExecutionCourseStudentsTotalNumber());

            spreadsheet.addCell(entry.getExecutionCourseTheoreticalHours());
            spreadsheet.addCell(entry.getExecutionCoursePraticalHours());
            spreadsheet.addCell(entry.getExecutionCourseLaboratorialHours());
            spreadsheet.addCell(entry.getExecutionCourseTheoPratHours());
            spreadsheet.addCell(entry.getExecutionCourseSeminaryHours());
            spreadsheet.addCell(entry.getExecutionCourseProblemsHours());
            spreadsheet.addCell(entry.getExecutionCourseTutorialOrientationHours());
            spreadsheet.addCell(entry.getExecutionCourseFieldWorkHours());
            spreadsheet.addCell(entry.getExecutionCourseTrainingPeriodHours());

            spreadsheet.addCell(entry.getExecutionCourseTotalHours());
            spreadsheet.addCell(entry.getExecutionCourseDurationBalance());

            spreadsheet.addCell(entry.getExecutionCourseStudentsNumberByTheoreticalShift());
            spreadsheet.addCell(entry.getExecutionCourseStudentsNumberByPraticalShift());
            spreadsheet.addCell(entry.getExecutionCourseStudentsNumberByLaboratorialShift());
            spreadsheet.addCell(entry.getExecutionCourseStudentsNumberByTheoPraticalShift());
            spreadsheet.addCell(entry.getExecutionCourseStudentsNumberBySeminaryShift());
            spreadsheet.addCell(entry.getExecutionCourseStudentsNumberByProblemsShift());
            spreadsheet.addCell(entry.getExecutionCourseStudentsNumberByTutorialOrientationShift());
            spreadsheet.addCell(entry.getExecutionCourseStudentsNumberByFieldWorkShift());
            spreadsheet.addCell(entry.getExecutionCourseStudentsNumberByTrainingPeriodShift());

            for (TeacherExecutionCourseServiceDTO teacher : entry.getTeacherExecutionCourseServiceList()) {
                spreadsheet.newRow();
                spreadsheet.addCell(teacher.getTeacherUsername());
                spreadsheet.addCell(teacher.getTeacherName());

                PeriodFormatter periodFormatter =
                        new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours().appendSuffix(":")
                                .appendMinutes().toFormatter();
                spreadsheet.addCell(periodFormatter.print(teacher.getTimeSpentByTeacher().toPeriod()));

                if (!teacher.getTeacherOfDepartment()) {
                    spreadsheet.addCell(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.hours"));
                }
            }

        }
    }

    private List<String> buildExecutionPeriodsIDsList() throws FenixServiceException {
        List<InfoExecutionPeriod> executionPeriods = ReadExecutionPeriodsByExecutionYear.run(this.getSelectedExecutionYearID());

        Collections.sort(executionPeriods, new BeanComparator("beginDate"));

        InfoExecutionPeriod previousExecutionPeriod =
                ReadPreviousExecutionPeriod.run(executionPeriods.iterator().next().getExternalId());

        if (previousExecutionPeriod != null) {
            previousExecutionYear = previousExecutionPeriod.getInfoExecutionYear();
        } else {
            previousExecutionYear = null;
        }

        List<String> periodsIDsList = new ArrayList<String>();

        for (InfoExecutionPeriod executionPeriod : executionPeriods) {
            periodsIDsList.add(executionPeriod.getExternalId());
        }

        List<String> returnList = new ArrayList<String>();
        int periodID = getSelectedExecutionPeriodID();

        if ((periodID != BOTH_SEMESTERS_ID) && (periodsIDsList.size() > 1)) {
            if (periodID == FIRST_SEMESTER_ID) {
                returnList.add(periodsIDsList.iterator().next());
            } else {
                returnList.add(periodsIDsList.get(periodsIDsList.size() - 1));
            }
        } else {
            returnList = periodsIDsList;
        }

        return returnList;
    }

    public void onSelectedExecutionYearIDChanged(ValueChangeEvent valueChangeEvent) {
        this.selectedExecutionYearID = (String) valueChangeEvent.getNewValue();
    }

    public void onSelectedExecutionPeriodIDChanged(ValueChangeEvent valueChangeEvent) {
        this.selectedExecutionPeriodID = (Integer) valueChangeEvent.getNewValue();
    }

    public void setExecutionPeriodsItems(UISelectItems executionPeriodsItems) {
        this.executionPeriodsItems = executionPeriodsItems;
    }

    public void setSelectedExecutionPeriodID(Integer selectedExecutionPeriodID) {
        this.selectedExecutionPeriodID = selectedExecutionPeriodID;
    }

    // Opcoes de vista

    private Integer[] selectedViewOptions;

    private UISelectItems viewOptionsItems;

    private final int VIEW_STUDENTS_PER_SHIFT = 3;

    private final int VIEW_HOURS_PER_SHIFT = 4;

    private final int VIEW_COURSE_INFORMATION = 5;

    private final int VIEW_STUDENTS_ENROLMENTS = 6;

    private final int VIEW_CREDITS_INFORMATION = 7;

    public UISelectItems getViewOptionsItems() throws FenixServiceException {
        if (this.viewOptionsItems == null) {
            this.viewOptionsItems = new UISelectItems();
            viewOptionsItems.setValue(this.getViewOptions());
        }

        return viewOptionsItems;
    }

    public UISelectItems getViewByTeacherOptionsItems() throws FenixServiceException {
        if (this.viewOptionsItems == null) {
            this.viewOptionsItems = new UISelectItems();
            viewOptionsItems.setValue(this.getByTeacherViewOptions());
        }

        return viewOptionsItems;
    }

    private List<SelectItem> getViewOptions() throws FenixServiceException {

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(VIEW_COURSE_INFORMATION, BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.viewCourseInfo")));
        result.add(new SelectItem(VIEW_STUDENTS_ENROLMENTS, BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.viewStudentsEnrolments")));
        result.add(new SelectItem(VIEW_HOURS_PER_SHIFT, BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.viewHoursPerShift")));
        result.add(new SelectItem(VIEW_STUDENTS_PER_SHIFT, BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.viewStudentsPerShift")));

        return result;
    }

    private List<SelectItem> getByTeacherViewOptions() throws FenixServiceException {

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(VIEW_CREDITS_INFORMATION, BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                "label.teacherService.viewCreditsInfo")));

        return result;
    }

    public Integer[] getSelectedViewOptions() {
        if (selectedViewOptions == null) {
            selectedViewOptions = new Integer[] {};
        }
        return selectedViewOptions;
    }

    public Integer[] getSelectedViewByTeacherOptions() {
        if (selectedViewOptions == null) {
            selectedViewOptions = new Integer[] { VIEW_CREDITS_INFORMATION };
        }
        return selectedViewOptions;
    }

    public void setSelectedViewOptions(Integer[] selectedViewOptions) {
        this.selectedViewOptions = selectedViewOptions;
    }

    public void setSelectedViewByTeacherOptions(Integer[] selectedViewOptions) {
        this.selectedViewOptions = selectedViewOptions;
    }

    public void setViewOptionsItems(UISelectItems viewOptionsItems) {
        this.viewOptionsItems = viewOptionsItems;
    }

    public void setViewByTeacherOptionsItems(UISelectItems viewOptionsItems) {
        this.viewOptionsItems = viewOptionsItems;
    }

    private boolean isOptionSelected(int option) {
        Integer[] selectedOptions = getSelectedViewByTeacherOptions();

        for (Integer selectedOption : selectedOptions) {
            if (selectedOption == option) {
                return true;
            }
        }
        return false;
    }

    public boolean getViewStudentsPerShift() {
        return isOptionSelected(VIEW_STUDENTS_PER_SHIFT);
    }

    public boolean getViewHoursPerShift() {
        return isOptionSelected(VIEW_HOURS_PER_SHIFT);
    }

    public boolean getViewCourseInformation() {
        return isOptionSelected(VIEW_COURSE_INFORMATION);
    }

    public boolean getViewStudentsEnrolments() {
        return isOptionSelected(VIEW_STUDENTS_ENROLMENTS);
    }

    public boolean getViewCreditsInformation() {
        return isOptionSelected(VIEW_CREDITS_INFORMATION);
    }

    public InfoExecutionYear getPreviousExecutionYear() throws FenixServiceException {
        if (previousExecutionYear == null) {
            buildExecutionPeriodsIDsList();
        }
        return previousExecutionYear;
    }
}