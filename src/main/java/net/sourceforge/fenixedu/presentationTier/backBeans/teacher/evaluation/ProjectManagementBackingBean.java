/*
 * Created on Nov 7, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.CreateProject;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditProject;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Project;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class ProjectManagementBackingBean extends EvaluationManagementBackingBean {
    protected String name;

    protected String beginProjectDate;

    protected String beginProjectHour;

    protected String endProjectDate;

    protected String endProjectHour;

    protected Project project;

    protected String projectID;

    protected List<Project> associatedProjects;

    protected Boolean onlineSubmissionsAllowed;

    protected Integer maxSubmissionsToKeep;

    protected String groupingID;

    protected List<SelectItem> executionCourseGroupings;

    protected List<String> selectedDepartments;

    public ProjectManagementBackingBean() {
        super();
        initSelectedDepartments();
    }

    private void initSelectedDepartments() {
        final Project project = getProject();
        selectedDepartments = new ArrayList<String>();
        if (project != null) {
            for (Department department : project.getDeparments()) {
                selectedDepartments.add(department.getExternalId());
            }
        }
    }

    private String getBeginString() {
        return getBeginProjectDate() + " " + getBeginProjectHour();
    }

    private String getEndString() {
        return getEndProjectDate() + " " + getEndProjectHour();
    }

    public String createProject() {
        try {
            CreateProject.runCreateProject(getExecutionCourseID(), getName(),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBeginString()),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEndString()), getDescription(), getOnlineSubmissionsAllowed(),
                    getMaxSubmissionsToKeep(), getGroupingID(), getGradeScale(), getSelectDepartments());
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (final ParseException e) {
            setErrorMessage("error.invalidDate");
            return "";
        } catch (final DomainException e) {
            setErrorMessage(e.getKey());
            return "";
        }
        return "projectsIndex";
    }

    public String editProject() {
        try {
            EditProject.runEditProject(getExecutionCourseID(), getProjectID(), getName(),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBeginString()),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEndString()), getDescription(), getOnlineSubmissionsAllowed(),
                    getMaxSubmissionsToKeep(), getGroupingID(), getGradeScale(), getSelectDepartments());
            setAssociatedProjects(null);
        } catch (final NotAuthorizedException e) {
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (ParseException e) {
            setErrorMessage("error.invalidDate");
            return "";
        } catch (DomainException e) {
            setErrorMessage(e.getKey());
            return "";
        }
        return "projectsIndex";
    }

    public String deleteProject() {
        try {
            DeleteEvaluation.runDeleteEvaluation(getExecutionCourseID(), getProjectID());
            setAssociatedProjects(null);
        } catch (NotAuthorizedException e) {
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getKey());
        }
        return "projectsIndex";
    }

    private Project getProject() {
        if (this.project == null && this.getProjectID() != null) {
            this.project = (Project) FenixFramework.getDomainObject(getProjectID());
        }
        return this.project;
    }

    public List<Project> getAssociatedProjects() throws FenixServiceException {
        if (this.associatedProjects == null) {
            final ExecutionCourse executionCourse = FenixFramework.getDomainObject(getExecutionCourseID());
            this.associatedProjects = executionCourse.getAssociatedProjects();
            Collections.sort(this.associatedProjects, new BeanComparator("begin"));
        }
        return associatedProjects;
    }

    public void setAssociatedProjects(List<Project> associatedProjects) {
        this.associatedProjects = associatedProjects;
    }

    public String getBeginProjectDate() {
        if (this.beginProjectDate == null && this.getProject() != null) {
            this.beginProjectDate = DateFormatUtil.format("dd/MM/yyyy", getProject().getBegin());
        }
        return this.beginProjectDate;
    }

    public void setBeginProjectDate(String beginProjectDate) {
        this.beginProjectDate = beginProjectDate;
    }

    public String getEndProjectDate() {
        if (this.endProjectDate == null && this.getProject() != null) {
            this.endProjectDate = DateFormatUtil.format("dd/MM/yyyy", getProject().getEnd());
        }
        return this.endProjectDate;
    }

    public void setEndProjectDate(String endProjectDate) {
        this.endProjectDate = endProjectDate;
    }

    public String getName() {
        if (this.name == null && this.getProject() != null) {
            this.name = this.getProject().getName();
        }
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        if (this.description == null && this.getProject() != null) {
            this.description = this.getProject().getDescription();
        }
        return this.description;
    }

    public String getProjectID() {
        if (this.projectID == null) {
            if (this.getRequestParameter("projectID") != null && !this.getRequestParameter("projectID").equals("")) {
                this.projectID = this.getRequestParameter("projectID");
            }
        }
        return this.projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getBeginProjectHour() {
        if (this.beginProjectHour == null && this.getProject() != null) {
            this.beginProjectHour = DateFormatUtil.format("HH:mm", getProject().getBegin());
        }
        return this.beginProjectHour;
    }

    public void setBeginProjectHour(String beginProjectHour) {
        this.beginProjectHour = beginProjectHour;
    }

    public String getEndProjectHour() {
        if (this.endProjectHour == null && this.getProject() != null) {
            this.endProjectHour = DateFormatUtil.format("HH:mm", getProject().getEnd());
        }
        return this.endProjectHour;
    }

    public void setEndProjectHour(String endProjectHour) {
        this.endProjectHour = endProjectHour;
    }

    public String getGroupingID() {
        if (this.groupingID == null && this.getProject() != null) {
            Grouping grouping = this.getProject().getGrouping();

            this.groupingID = (grouping != null) ? grouping.getExternalId() : null;
        }
        return groupingID;
    }

    public void setGroupingID(String groupingID) {
        this.groupingID = groupingID;
    }

    public Integer getMaxSubmissionsToKeep() {
        if (this.maxSubmissionsToKeep == null && this.getProject() != null) {
            this.maxSubmissionsToKeep = this.getProject().getMaxSubmissionsToKeep();
        }
        return maxSubmissionsToKeep;
    }

    public void setMaxSubmissionsToKeep(Integer maxSubmissionsToKeep) {
        this.maxSubmissionsToKeep = maxSubmissionsToKeep;
    }

    public Boolean getOnlineSubmissionsAllowed() {
        if (this.onlineSubmissionsAllowed == null && this.getProject() != null) {
            this.onlineSubmissionsAllowed = this.getProject().getOnlineSubmissionsAllowed();
        }
        return onlineSubmissionsAllowed;
    }

    public void setOnlineSubmissionsAllowed(Boolean onlineSubmissionsAllowed) {
        this.onlineSubmissionsAllowed = onlineSubmissionsAllowed;
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

    @Override
    public GradeScale getGradeScale() {
        if (gradeScale == null && this.getProject() != null) {
            this.gradeScale = getProject().getGradeScale();
        }
        return this.gradeScale;
    }

    public List<SelectItem> getDepartments() {
        List<SelectItem> departments = new ArrayList<SelectItem>();
        for (Department department : Bennu.getInstance().getDepartmentsSet()) {
            final SelectItem e = new SelectItem(department.getExternalId(), department.getName());
            departments.add(e);
        }
        return departments;
    }

    public void setSelectedDepartments(List<String> departments) {
        selectedDepartments = departments;
    }

    public List<Department> getSelectDepartments() {
        List<Department> departments = new ArrayList<Department>();
        for (String departmentExtId : selectedDepartments) {
            departments.add((Department) FenixFramework.getDomainObject(departmentExtId));
        }
        return departments;
    }

    public List<String> getSelectedDepartments() {
        return selectedDepartments;
    }

}
