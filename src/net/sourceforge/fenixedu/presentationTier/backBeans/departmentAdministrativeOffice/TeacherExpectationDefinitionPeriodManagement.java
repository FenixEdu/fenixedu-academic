package net.sourceforge.fenixedu.presentationTier.backBeans.departmentAdministrativeOffice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.validator.DateValidator;

/**
 * 
 * @author naat
 * 
 */
public class TeacherExpectationDefinitionPeriodManagement extends FenixBackingBean {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private List<SelectItem> executionYears;

    private ITeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod;

    private Integer teacherExpectationDefinitionPeriodID;

    private String startDateString;

    private String endDateString;

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public Integer getTeacherExpectationDefinitionPeriodID() {
        return teacherExpectationDefinitionPeriodID;
    }

    public void setTeacherExpectationDefinitionPeriodID(Integer teacherExpectationDefinitionPeriodID) {
        this.teacherExpectationDefinitionPeriodID = teacherExpectationDefinitionPeriodID;
    }

    public TeacherExpectationDefinitionPeriodManagement() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext()
                .getResponse();

        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Expires", "-1");
        response.addHeader("Pragma", "no-cache");
    }

    public List<SelectItem> getExecutionYears() throws FenixFilterException, FenixServiceException {

        if (this.executionYears == null) {

            List<InfoExecutionYear> executionYears = (List<InfoExecutionYear>) ServiceUtils
                    .executeService(getUserView(), "ReadNotClosedExecutionYears", null);

            List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
            for (InfoExecutionYear executionYear : executionYears) {
                result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));
            }

            this.executionYears = result;
        }

        return this.executionYears;
    }

    public Integer getSelectedExecutionYearID() throws FenixFilterException, FenixServiceException {

        Integer executionYearID = (Integer) this.getViewState().getAttribute("selectedExecutionYearID");

        if (executionYearID == null) {
            if (this.getExecutionYears().size() != 0) {
                executionYearID = (Integer) this.getExecutionYears().get(
                        this.getExecutionYears().size() - 1).getValue();
                this.getViewState().setAttribute("selectedExecutionYearID", executionYearID);
            }
        }

        return executionYearID;
    }

    public void setSelectedExecutionYearID(Integer selectedExecutionYearID) {
        this.getViewState().setAttribute("selectedExecutionYearID", selectedExecutionYearID);
    }

    public ITeacherExpectationDefinitionPeriod getTeacherExpectationDefinitionPeriod()
            throws FenixFilterException, FenixServiceException {

        Integer departmentID = getUserView().getPerson().getEmployee().getDepartmentWorkingPlace()
                .getIdInternal();

        if (this.teacherExpectationDefinitionPeriod == null) {
            if (getSelectedExecutionYearID() != null) {
                this.teacherExpectationDefinitionPeriod = (ITeacherExpectationDefinitionPeriod) ServiceUtils
                        .executeService(
                                getUserView(),
                                "ReadTeacherExpectationDefinitionPeriodByDepartmentIDAndExecutionYearID",
                                new Object[] { departmentID, getSelectedExecutionYearID() });
            }
        }

        return teacherExpectationDefinitionPeriod;
    }

    public void onExecutionYearChange(ValueChangeEvent valueChangeEvent) throws FenixFilterException,
            FenixServiceException {
        this.setSelectedExecutionYearID((Integer) valueChangeEvent.getNewValue());

        this.teacherExpectationDefinitionPeriod = null;

    }

    public String savePeriod() throws FenixFilterException, FenixServiceException, ParseException {

        String mapping = "";

        if (areDatesValid()) {
            Integer departmentID = getUserView().getPerson().getEmployee().getDepartmentWorkingPlace()
                    .getIdInternal();

            ServiceUtils.executeService(getUserView(), "CreateTeacherExpectationDefinitionPeriod",
                    new Object[] { departmentID, getSelectedExecutionYearID(), getStartDate(),
                            getEndDate() });

            mapping = "success";
        }

        return mapping;
    }

    public String updatePeriod() throws FenixFilterException, FenixServiceException, ParseException {
        String mapping = "";

        if (areDatesValid()) {

            ServiceUtils.executeService(getUserView(), "EditTeacherExpectationDefinitionPeriod",
                    new Object[] { getTeacherExpectationDefinitionPeriodID(), getStartDate(),
                            getEndDate() });

            mapping = "success";

        }

        return mapping;
    }

    private boolean areDatesValid() throws ParseException {
        String dateFormat = DATE_FORMAT;
        DateValidator dateValidator = DateValidator.getInstance();

        if (dateValidator.isValid(getStartDateString(), dateFormat, false)
                && dateValidator.isValid(getEndDateString(), dateFormat, false)) {

            Date startDate = getStartDate();
            Date endDate = getEndDate();

            if (endDate.before(startDate) == true) {
                setErrorMessage("error.teacherExpectationDefinitionPeriodManagement.endDateBeforeStartDate");

                return false;
            }

            return true;
        } else {

            setErrorMessage("error.teacherExpectationDefinitionPeriodManagement.invalidDates");

            return false;
        }

    }

    private Date getStartDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date startDate = dateFormat.parse(this.getStartDateString());

        return startDate;
    }

    private Date getEndDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date endDate = dateFormat.parse(this.getEndDateString());

        return endDate;
    }

    public String preparePeriodForEdit() throws FenixFilterException, FenixServiceException {
        ITeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod = getTeacherExpectationDefinitionPeriod();
        Date startDate = teacherExpectationDefinitionPeriod.getStartDate();
        Date endDate = teacherExpectationDefinitionPeriod.getEndDate();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

        this.startDateString = dateFormatter.format(startDate);
        this.endDateString = dateFormatter.format(endDate);
        this.setTeacherExpectationDefinitionPeriodID(teacherExpectationDefinitionPeriod.getIdInternal());

        return "edit";
    }
}
