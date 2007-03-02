package net.sourceforge.fenixedu.presentationTier.backBeans.departmentMember;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.department.CompetenceCourseStatisticsDTO;
import net.sourceforge.fenixedu.dataTransferObject.department.DegreeCourseStatisticsDTO;
import net.sourceforge.fenixedu.dataTransferObject.department.ExecutionCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
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

    private List<SelectItem> executionPeriods;

    private CompetenceCourse competenceCourse;

    public Department getDepartment() {
        return getUserView().getPerson().getTeacher().getLastWorkingDepartment();
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

    public Integer getExecutionPeriodId() throws FenixFilterException, FenixServiceException {
        Integer executionPeriodId = (Integer) this.getViewState().getAttribute("executionYearPeriod");

        if (executionPeriodId == null) {
        	executionPeriodId = (Integer) getRequestAttribute("executionPeriodId");

            if (executionPeriodId == null) {
                InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(
                        getUserView(), "ReadCurrentExecutionPeriod", new Object[] {});

                if (infoExecutionPeriod == null) {
                    executionPeriodId = (Integer) this.getExecutionPeriods().get(
                            this.executionPeriods.size() - 1).getValue();
                } else {
                    executionPeriodId = infoExecutionPeriod.getIdInternal();
                }
            }

            this.getViewState().setAttribute("executionPeriodId", executionPeriodId);
        }

        return executionPeriodId;
    }

    public void setExecutionPeriodId(Integer executionPeriodId) {
        this.getViewState().setAttribute("executionPeriodId", executionPeriodId);
        setRequestAttribute("executionPeriodId", executionPeriodId);
    }

    public void onExecutionPeriodChangeForCompetenceCourses(ValueChangeEvent valueChangeEvent)
            throws FenixFilterException, FenixServiceException {
    	setExecutionPeriodId((Integer) valueChangeEvent.getNewValue());
        loadCompetenceCourses();
    }

    public void onExecutionPeriodChangeForDegreeCourses(ValueChangeEvent valueChangeEvent)
            throws FenixFilterException, FenixServiceException {
        setExecutionPeriodId((Integer) valueChangeEvent.getNewValue());
        loadDegreeCourses();
    }

    public void onExecutionPeriodChangeForExecutionCourses(ValueChangeEvent valueChangeEvent)
            throws FenixFilterException, FenixServiceException {
        setExecutionPeriodId((Integer) valueChangeEvent.getNewValue());
        loadExecutionCourses();
    }

    public List<SelectItem> getExecutionPeriods() throws FenixFilterException, FenixServiceException {
        if (this.executionPeriods == null) {
            Object[] args = {};
            List<InfoExecutionYear> executionYearsList = (List<InfoExecutionYear>) ServiceUtils
                    .executeService(getUserView(), "ReadNotClosedExecutionYears", args);
            List<SelectItem> result = new ArrayList<SelectItem>();
            for (InfoExecutionYear executionYear : executionYearsList) {
            	List<ExecutionPeriod> executionPeriods = rootDomainObject.readExecutionYearByOID(executionYear.getIdInternal()).getExecutionPeriods();
            	for(ExecutionPeriod executionPeriod : executionPeriods) {
            		result.add(new SelectItem(executionPeriod.getIdInternal(), executionPeriod.getExecutionYear().getYear() + " - " + executionPeriod.getName()));
            	}
            }
            this.executionPeriods = result;
        }
        return this.executionPeriods;
    }

    private void loadCompetenceCourses() throws FenixFilterException, FenixServiceException {
        Integer departmentID = getUserView().getPerson().getTeacher().getLastWorkingDepartment().getIdInternal();
        Object args[] = { departmentID, this.getExecutionPeriodId() };
        competenceCourses = (List<CompetenceCourseStatisticsDTO>) ServiceUtils.executeService(
                getUserView(), "ComputeCompetenceCourseStatistics", args);
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
                "ComputeDegreeCourseStatistics", new Object[] { getCompetenceCourseId(),
                        getExecutionPeriodId() });
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
                getUserView(), "ComputeExecutionCourseStatistics", new Object[] {
                        this.getCompetenceCourseId(), this.getDegreeId(), getExecutionPeriodId() });
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

    public CompetenceCourse getCompetenceCourse() throws FenixFilterException, FenixServiceException {
        return competenceCourse == null ? rootDomainObject.readCompetenceCourseByOID(getCompetenceCourseId()) : competenceCourse;
    }
}
