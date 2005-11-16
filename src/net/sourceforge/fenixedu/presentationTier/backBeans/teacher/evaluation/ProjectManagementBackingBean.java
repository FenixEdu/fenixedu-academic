/*
 * Created on Nov 7, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProject;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.commons.beanutils.BeanComparator;

public class ProjectManagementBackingBean extends EvaluationManagementBackingBean {
    protected String name;
    protected String beginProjectDate;
    protected String beginProjectHour;
    protected String endProjectDate;
    protected String endProjectHour;
    protected IProject project;
    protected Integer projectID;
    protected List<IProject> associatedProjects;

    private String getBeginString() {
        return getBeginProjectDate() + " " + getBeginProjectHour();
    }

    private String getEndString() {
        return getEndProjectDate() + " " + getEndProjectHour();
    }

    public String createProject() {
        try {
            final Object[] args = { getExecutionCourseID(), getName(),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBeginString()),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEndString()), getDescription() };
            ServiceUtils.executeService(getUserView(), "CreateProject", args);
        } catch (final FenixFilterException e) {
            return "";
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (final ParseException e) {
            setErrorMessage("error.invalidDate");
            return "";
        }
        return "projectsIndex";
    }

    public String editProject() {
        try {
            final Object[] args = { getExecutionCourseID(), getProjectID(), getName(),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBeginString()),
                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEndString()), getDescription() };
            ServiceUtils.executeService(getUserView(), "EditProject", args);
            setAssociatedProjects(null);
        } catch (final FenixFilterException e) {
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (ParseException e) {
            setErrorMessage("error.invalidDate");
            return "";
        }
        return "projectsIndex";
    }

    public String deleteProject() {
        try {
            final Object[] args = { getExecutionCourseID(), getProjectID() };
            ServiceUtils.executeService(getUserView(), "DeleteEvaluation", args);
            setAssociatedProjects(null);
        } catch (FenixFilterException e) {
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        }
        return "projectsIndex";
    }

    private IProject getProject() {
        if (this.project == null && this.getProjectID() != null) {
            try {
                final Object[] args = { Project.class, getProjectID() };
                this.project = (IProject) ServiceUtils.executeService(null, "ReadDomainObject", args);
            } catch (FenixFilterException e) {
                return null;
            } catch (FenixServiceException e) {
                return null;
            }
        }
        return this.project;
    }

    public List<IProject> getAssociatedProjects() throws FenixFilterException, FenixServiceException {
        if (this.associatedProjects == null) {
            final Object[] args = { ExecutionCourse.class, this.getExecutionCourseID() };
            final IExecutionCourse executionCourse = (IExecutionCourse) ServiceUtils.executeService(
                    null, "ReadDomainObject", args);
            this.associatedProjects = executionCourse.getAssociatedProjects();
            Collections.sort(this.associatedProjects, new BeanComparator("begin"));
        }
        return associatedProjects;
    }

    public void setAssociatedProjects(List<IProject> associatedProjects) {
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

    public String getDescription() {
        if (this.description == null && this.getProject() != null) {
            this.description = this.getProject().getDescription();
        }
        return this.description;
    }

    public Integer getProjectID() {
        if (this.projectID == null) {
            if (this.getRequestParameter("projectID") != null
                    && !this.getRequestParameter("projectID").equals("")) {
                this.projectID = Integer.valueOf(this.getRequestParameter("projectID"));
            }
        }
        return this.projectID;
    }

    public void setProjectID(Integer projectID) {
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
}
