package net.sourceforge.fenixedu.presentationTier.backBeans.departmentMember.teacherService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.UISelectItems;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodsByExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYearByID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadPreviousExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.teacherService.ReadTeacherServiceDistributionByCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.teacherService.ReadTeacherServiceDistributionByTeachers;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByCourseDTO.ExecutionCourseDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByTeachersDTO.TeacherDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.fenix.tools.util.i18n.Language;

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
        String executionYearIDString = this.getRequestParameter("selectedExecutionYearID");

        if (executionYearIDString != null) {
            this.selectedExecutionYearID = this.getRequestParameter("selectedExecutionYearID");
        } else {
            if (this.selectedExecutionYearID == null) {
                List<SelectItem> executionYearItems = (List<SelectItem>) this.getExecutionYearItems().getValue();
                this.selectedExecutionYearID = (String) executionYearItems.get(0).getValue();
            }

        }

        return this.selectedExecutionYearID;
    }

    public Integer getSelectedExecutionPeriodID() {

        String executionPeriodIDString = this.getRequestParameter("selectedExecutionPeriodID");

        if (executionPeriodIDString != null) {
            selectedExecutionPeriodID = Integer.valueOf(this.getRequestParameter("selectedExecutionPeriodID"));
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
        if (teacherServiceDTO == null) {
            loadDistributionServiceData();
        }
        return teacherServiceDTO;
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

        ResourceBundle rb = ResourceBundle.getBundle("resources.DepartmentMemberResources", Language.getLocale());

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(BOTH_SEMESTERS_ID, rb.getString("label.teacherService.bothSemesters")));
        result.add(new SelectItem(FIRST_SEMESTER_ID, rb.getString("label.teacherService.firstSemester")));
        result.add(new SelectItem(SECOND_SEMESTER_ID, rb.getString("label.teacherService.secondSemester")));

        return result;

    }

    public String getDepartmentName() {
        Person person = getUserView().getPerson();
        Department currentWorkingDepartment =
                person.getEmployee() != null ? person.getEmployee().getCurrentDepartmentWorkingPlace() : null;
        return currentWorkingDepartment == null ? null : currentWorkingDepartment.getRealName();
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
                ReadTeacherServiceDistributionByTeachers.runReadTeacherServiceDistributionByTeachers(getUserView().getPerson()
                        .getEmployee().getCurrentDepartmentWorkingPlace().getExternalId(), ExecutionPeriodsIDs);

    }

    private void loadDistributionServiceDataByCourse() throws FenixServiceException {

        List<String> ExecutionPeriodsIDs = buildExecutionPeriodsIDsList();

        Object[] args =
                { getUserView().getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getExternalId(), ExecutionPeriodsIDs };

        this.executionCourseServiceDTO =
                ReadTeacherServiceDistributionByCourse.runReadTeacherServiceDistributionByCourse(getUserView().getPerson()
                        .getEmployee().getCurrentDepartmentWorkingPlace().getExternalId(), ExecutionPeriodsIDs);

    }

    private List<String> buildExecutionPeriodsIDsList() throws FenixServiceException {
        List<InfoExecutionPeriod> executionPeriods = ReadExecutionPeriodsByExecutionYear.run(this.getSelectedExecutionYearID());

        Collections.sort(executionPeriods, new BeanComparator("beginDate"));

        InfoExecutionPeriod previousExecutionPeriod = ReadPreviousExecutionPeriod.run(executionPeriods.get(0).getExternalId());

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
                returnList.add(periodsIDsList.get(0));
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

        ResourceBundle rb = ResourceBundle.getBundle("resources.DepartmentMemberResources", Language.getLocale());

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(VIEW_COURSE_INFORMATION, rb.getString("label.teacherService.viewCourseInfo")));
        result.add(new SelectItem(VIEW_STUDENTS_ENROLMENTS, rb.getString("label.teacherService.viewStudentsEnrolments")));
        result.add(new SelectItem(VIEW_HOURS_PER_SHIFT, rb.getString("label.teacherService.viewHoursPerShift")));
        result.add(new SelectItem(VIEW_STUDENTS_PER_SHIFT, rb.getString("label.teacherService.viewStudentsPerShift")));

        return result;
    }

    private List<SelectItem> getByTeacherViewOptions() throws FenixServiceException {

        ResourceBundle rb = ResourceBundle.getBundle("resources.DepartmentMemberResources", Language.getLocale());
        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(VIEW_CREDITS_INFORMATION, rb.getString("label.teacherService.viewCreditsInfo")));

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