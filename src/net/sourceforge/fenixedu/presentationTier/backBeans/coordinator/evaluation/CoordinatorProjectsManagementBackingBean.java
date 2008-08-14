/*
 * Created on Nov 9, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator.evaluation;

import java.text.ParseException;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class CoordinatorProjectsManagementBackingBean extends CoordinatorProjectsInformationBackingBean {
    protected String name;
    protected String beginDate;
    protected String beginHour;
    protected String endDate;
    protected String endHour;
    protected String description;

    private String getBeginString() {
	return getBeginDate() + " " + getBeginHour();
    }

    private String getEndString() {
	return getEndDate() + " " + getEndHour();
    }

    public String createProject() {
	try {
	    final Object[] args = { getExecutionCourseID(), getName(),
		    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBeginString()),
		    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEndString()), getDescription() };
	    ServiceUtils.executeService("CreateProject", args);
	} catch (final FenixFilterException e) {
	    return "";
	} catch (final FenixServiceException e) {
	    setErrorMessage(e.getMessage());
	    return "";
	} catch (final ParseException e) {
	    setErrorMessage("error.invalidDate");
	    return "";
	}
	return this.showProjectsForExecutionCourses();
    }

    public String editProject() {
	try {
	    final Object[] args = { getExecutionCourseID(), getEvaluationID(), getName(),
		    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBeginString()),
		    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEndString()), getDescription() };
	    ServiceUtils.executeService("EditProject", args);
	} catch (final FenixFilterException e) {
	} catch (final FenixServiceException e) {
	    setErrorMessage(e.getMessage());
	    return "";
	} catch (ParseException e) {
	    setErrorMessage("error.invalidDate");
	    return "";
	}
	return this.showProjectsForExecutionCourses();
    }

    public String deleteProject() {
	try {
	    final Object[] args = { getExecutionCourseID(), getEvaluationID() };
	    ServiceUtils.executeService("DeleteEvaluation", args);
	} catch (FenixFilterException e) {
	} catch (FenixServiceException e) {
	    setErrorMessage(e.getMessage());
	}
	return this.showProjectsForExecutionCourses();
    }

    public Evaluation getEvaluation() {
	try {
	    if (this.evaluation == null && this.getEvaluationID() != null) {
		this.evaluation = rootDomainObject.readEvaluationByOID(getEvaluationID());
	    }
	    return this.evaluation;
	} catch (Exception e) {
	    return null;
	}
    }

    public String showProjectsForExecutionCourses() {
	setRequestCommonAttributes();
	return "showProjectsForExecutionCourses";
    }

    public String getBeginDate() {
	if (this.beginDate == null) {
	    if (this.getEvaluation() != null) {
		this.beginDate = DateFormatUtil.format("dd/MM/yyyy", ((Project) this.getEvaluation()).getBegin());
	    } else if (getDay() != null & getMonth() != null & getYear() != null) {
		this.beginDate = getDay() + "/" + getMonth() + "/" + getYear();
	    }
	}
	return this.beginDate;
    }

    public void setBeginDate(String beginDate) {
	this.beginDate = beginDate;
    }

    public String getEndDate() {
	if (this.endDate == null && this.getEvaluation() != null) {
	    this.endDate = DateFormatUtil.format("dd/MM/yyyy", ((Project) this.getEvaluation()).getEnd());
	}
	return this.endDate;
    }

    public void setEndDate(String endDate) {
	this.endDate = endDate;
    }

    public String getName() {
	if (this.name == null && this.getEvaluation() != null) {
	    this.name = ((Project) this.getEvaluation()).getName();
	}
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	if (this.description == null && this.getEvaluation() != null) {
	    this.description = ((Project) this.getEvaluation()).getDescription();
	}
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getBeginHour() {
	if (this.beginHour == null && this.getEvaluation() != null) {
	    this.beginHour = DateFormatUtil.format("HH:mm", ((Project) this.getEvaluation()).getBegin());
	}
	return this.beginHour;
    }

    public void setBeginHour(String beginHour) {
	this.beginHour = beginHour;
    }

    public String getEndHour() {
	if (this.endHour == null && this.getEvaluation() != null) {
	    this.endHour = DateFormatUtil.format("HH:mm", ((Project) this.getEvaluation()).getEnd());
	}
	return this.endHour;
    }

    public void setEndHour(String endHour) {
	this.endHour = endHour;
    }
}
