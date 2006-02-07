/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.cms.website.executionCourseWebsite;

import org.apache.struts.validator.ValidatorForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/> <br/> Created on
 *         15:20:00,6/Dez/2005
 * @version $Id$
 */
public class ExecutionCourseWebSiteManagementForm extends ValidatorForm {
    private static final long serialVersionUID = 874509136705541900L;

    private String method;

    private String name;

    private String description;

    private Integer executionPeriodID;

    private Integer executionDegreeID;

    private Integer curricularYear;

    private Integer executionCourseID;

    private Integer websiteTypeID;

    public Integer getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(Integer curricularYear) {
        this.curricularYear = curricularYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getExecutionCourseID() {
        return executionCourseID;
    }

    public void setExecutionCourseID(Integer executionCourseID) {
        this.executionCourseID = executionCourseID;
    }

    public Integer getExecutionDegreeID() {
        return executionDegreeID;
    }

    public void setExecutionDegreeID(Integer executionDegreeID) {
        this.executionDegreeID = executionDegreeID;
    }

    public Integer getExecutionPeriodID() {
        return executionPeriodID;
    }

    public void setExecutionPeriodID(Integer executionPeriodID) {
        this.executionPeriodID = executionPeriodID;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWebsiteTypeID() {
        return this.websiteTypeID;
    }

    public void setWebsiteTypeID(Integer websiteTypeID) {
        this.websiteTypeID = websiteTypeID;
    }

}