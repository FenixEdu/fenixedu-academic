/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CourseGroupManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle bolonhaResources = getResourceBundle("resources/BolonhaManagerResources");
    private final ResourceBundle domainResources = getResourceBundle("resources/DomainExceptionResources");
    private final Integer NO_SELECTION = 0;    
    
    private String name = null;
    private Integer courseGroupID;

    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
    }

    public Integer getParentCourseGroupID() {
        return getAndHoldIntegerParameter("parentCourseGroupID");
    }

    public Integer getCourseGroupID() {
        return (this.courseGroupID != null) ? this.courseGroupID : getAndHoldIntegerParameter("courseGroupID");
    }

    public void setCourseGroupID(Integer courseGroupID) {
        this.courseGroupID = courseGroupID;
    }
    
    public String getName() throws FenixFilterException, FenixServiceException {
        return (name == null && getCourseGroupID() != null) ? getCourseGroup(getCourseGroupID()).getName() : name;    
    }

    public String getParentName() throws FenixFilterException, FenixServiceException {
        return (getParentCourseGroupID() != null) ? getCourseGroup(getParentCourseGroupID()).getName() : null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException,
            FenixServiceException {
        return (DegreeCurricularPlan) readDomainObject(DegreeCurricularPlan.class, getDegreeCurricularPlanID());
    }

    public CourseGroup getCourseGroup(Integer courseGroupID) throws FenixFilterException, FenixServiceException {
        return (CourseGroup) readDomainObject(CourseGroup.class, courseGroupID);
    }

    public String createCourseGroup() throws FenixFilterException {
        try {
            final Object args[] = { getParentCourseGroupID(), getName() };
            ServiceUtils.executeService(getUserView(), "CreateCourseGroup", args);
            addInfoMessage(bolonhaResources.getString("courseGroupCreated"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }

    public String editCourseGroup() throws FenixFilterException {
        try {
            final Object args[] = { getCourseGroupID(), getName() };
            ServiceUtils.executeService(getUserView(), "EditCourseGroup", args);
            addInfoMessage(bolonhaResources.getString("courseGroupEdited"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }

    public String deleteCourseGroup() throws FenixFilterException {
        try {
            final Object args[] = { getCourseGroupID() };
            ServiceUtils.executeService(getUserView(), "DeleteCourseGroup", args);
            addInfoMessage(bolonhaResources.getString("courseGroupDeleted"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }
    
    public String addContext() {
        try {
            checkCourseGroup();
            Object args[] = { getCourseGroup(getCourseGroupID()), getCourseGroup(getParentCourseGroupID()) };
            ServiceUtils.executeService(getUserView(), "AddContextToDegreeModule", args);
        } catch (FenixActionException e) {
            this.addErrorMessage(bolonhaResources.getString(e.getMessage()));
            return "editCurricularPlanStructure";
        } catch (FenixFilterException e) {
            this.addErrorMessage(bolonhaResources.getString("error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (FenixServiceException e) {
            this.addErrorMessage(bolonhaResources.getString(e.getMessage()));
            return "editCurricularPlanStructure";
        } catch (DomainException e) {
            this.addErrorMessage(domainResources.getString(e.getMessage()));
            return "editCurricularPlanStructure";
        } catch (Exception e) {
            this.addErrorMessage(bolonhaResources.getString("general.error"));
            return "editCurricularPlanStructure";
        }        
        addInfoMessage(bolonhaResources.getString("courseGroupAssociated"));
        return "editCurricularPlanStructure";
    }
    
    private void checkCourseGroup() throws FenixFilterException, FenixServiceException, FenixActionException {
        if (getCourseGroupID() == null || getCourseGroupID().equals(this.NO_SELECTION)) {
            throw new FenixActionException("error.mustChooseACourseGroup");
        }
    }

}
