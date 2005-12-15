package net.sourceforge.fenixedu.presentationTier.backBeans.departmentMember;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.department.CompetenceCourseStatisticsDTO;
import net.sourceforge.fenixedu.dataTransferObject.department.DegreeCourseStatisticsDTO;
import net.sourceforge.fenixedu.dataTransferObject.department.ExecutionCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

/**
 * 
 * @author pcma
 * 
 */
public class CourseStatistics extends FenixBackingBean {
    private List<CompetenceCourseStatisticsDTO> competenceCourses;

    private List<DegreeCourseStatisticsDTO> degreeCourses;

    private List<ExecutionCourseStatisticsDTO> executionCourses;

    private List<SelectItem> executionYears;

    private ICompetenceCourse competenceCourse;

    public IDepartment getDepartment() {
        return getUserView().getPerson().getEmployee().getDepartmentWorkingPlace();
    }

    public Integer getCompetenceCourseId() throws FenixFilterException, FenixServiceException {
        Integer competenceCourseId = (Integer) this.getViewState().getAttribute("competenceCourseId");
        return competenceCourseId;
    }

    public void setCompetenceCourseId(Integer competenceCourseId) {
        this.getViewState().setAttribute("competenceCourseId", competenceCourseId);
    }

    public Integer getDegreeId() throws FenixFilterException, FenixServiceException {
        Integer degreeId = (Integer) this.getViewState().getAttribute("degreeId");
        return degreeId;
    }

    public void setDegreeId(Integer degreeId) {
        this.getViewState().setAttribute("degreeId", degreeId);
    }

    public Integer getExecutionYearId() throws FenixFilterException, FenixServiceException {
        Integer executionYearId = (Integer) this.getViewState().getAttribute("executionYearId");

        if (executionYearId == null) {
            executionYearId = (Integer) getRequestAttribute("executionYearId");

            if (executionYearId == null) {
                executionYearId = (Integer) this.getExecutionYears().get(this.executionYears.size() - 1)
                        .getValue();
            }

            this.getViewState().setAttribute("executionYearId", executionYearId);
        }

        return executionYearId;
    }

    public void setExecutionYearId(Integer executionYearId) {
        this.getViewState().setAttribute("executionYearId", executionYearId);
        setRequestAttribute("executionYearId", executionYearId);
    }

    public void onExecutionYearChangeForCompetenceCourses(ValueChangeEvent valueChangeEvent)
            throws FenixFilterException, FenixServiceException {
        setExecutionYearId((Integer) valueChangeEvent.getNewValue());
        loadCompetenceCourses();
    }

    public void onExecutionYearChangeForDegreeCourses(ValueChangeEvent valueChangeEvent)
            throws FenixFilterException, FenixServiceException {
        setExecutionYearId((Integer) valueChangeEvent.getNewValue());
        loadDegreeCourses();
    }

    public void onExecutionYearChangeForExecutionCourses(ValueChangeEvent valueChangeEvent)
            throws FenixFilterException, FenixServiceException {
        setExecutionYearId((Integer) valueChangeEvent.getNewValue());
        loadExecutionCourses();
    }

    public List<SelectItem> getExecutionYears() throws FenixFilterException, FenixServiceException {
        if (this.executionYears == null) {
            Object[] args = {};
            List<InfoExecutionYear> executionYearsList = (List<InfoExecutionYear>) ServiceUtils
                    .executeService(getUserView(), "ReadNotClosedExecutionYears", args);
            List<SelectItem> result = new ArrayList<SelectItem>();
            for (InfoExecutionYear executionYear : executionYearsList) {
                result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));
            }
            this.executionYears = result;
        }
        return this.executionYears;
    }

    private void loadCompetenceCourses() throws FenixFilterException, FenixServiceException {
        Integer departmentID = getUserView().getPerson().getEmployee().getDepartmentWorkingPlace()
                .getIdInternal();
        Object args[] = { departmentID, this.getExecutionYearId() };
        competenceCourses = (List<CompetenceCourseStatisticsDTO>) ServiceUtils.executeService(
                getUserView(), "CreateCompetenceCourseStatistics", args);
    }

    public List<CompetenceCourseStatisticsDTO> getCompetenceCourses() throws FenixFilterException,
            FenixServiceException {
        if (competenceCourses == null) {
            loadCompetenceCourses();
        }

        return competenceCourses;
    }

    private void loadDegreeCourses() throws FenixFilterException, FenixServiceException {
        degreeCourses = (List<DegreeCourseStatisticsDTO>) ServiceUtils.executeService(getUserView(),
                "CreateDegreeCourseStatistics", new Object[] { getCompetenceCourseId(),
                        getExecutionYearId() });
    }

    public List<DegreeCourseStatisticsDTO> getDegreeCourses() throws FenixFilterException,
            FenixServiceException {
        if (degreeCourses == null) {
            loadDegreeCourses();
        }

        return degreeCourses;
    }

    private void loadExecutionCourses() throws FenixFilterException, FenixServiceException {
        executionCourses = (List<ExecutionCourseStatisticsDTO>) ServiceUtils.executeService(
                getUserView(), "CreateExecutionCourseStatistics", new Object[] {
                        this.getCompetenceCourseId(), this.getDegreeId(), getExecutionYearId() });
    }

    public List<ExecutionCourseStatisticsDTO> getExecutionCourses() throws FenixFilterException,
            FenixServiceException {
        if (executionCourses == null) {
            loadExecutionCourses();
        }

        return executionCourses;
    }

    public void onCompetenceCourseSelect(ActionEvent event) throws FenixFilterException,
            FenixServiceException {

        int competenceCourseId = Integer.parseInt(getRequestParameter("competenceCourseId"));
        setCompetenceCourseId(competenceCourseId);
    }

    public void onDegreeCourseSelect(ActionEvent event) throws FenixFilterException,
            FenixServiceException {
        int degreeId = Integer.parseInt(getRequestParameter("degreeId"));
        setDegreeId(degreeId);
    }

    public ICompetenceCourse getCompetenceCourse() throws FenixFilterException, FenixServiceException {
        if (competenceCourse == null) {
            competenceCourse = (ICompetenceCourse) ServiceUtils.executeService(getUserView(),
                    "ReadDomainObject", new Object[] { CompetenceCourse.class,
                            this.getCompetenceCourseId() });
        }

        return competenceCourse;
    }
}
