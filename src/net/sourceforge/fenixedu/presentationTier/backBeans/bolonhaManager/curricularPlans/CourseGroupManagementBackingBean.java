/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

public class CourseGroupManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle bolonhaResources = getResourceBundle("resources/BolonhaManagerResources");
    private final ResourceBundle domainResources = getResourceBundle("resources/DomainExceptionResources");
    private final Integer NO_SELECTION = 0;    
    
    private String name = null;
    private String nameEn = null;
    private Integer courseGroupID;
    public List<SelectItem> courseGroups = null;

    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
    }

    public Integer getParentCourseGroupID() {
        return getAndHoldIntegerParameter("parentCourseGroupID");
    }

    public Integer getContextID() {
        return getAndHoldIntegerParameter("contextID");
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

    public String getNameEn() throws FenixFilterException, FenixServiceException {
        return (nameEn == null && getCourseGroupID() != null) ? getCourseGroup(getCourseGroupID()).getNameEn() : nameEn;    
    }
    
    public String getParentName() throws FenixFilterException, FenixServiceException {
        return (getParentCourseGroupID() != null) ? getCourseGroup(getParentCourseGroupID()).getName() : null;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException,
            FenixServiceException {
        return (DegreeCurricularPlan) readDomainObject(DegreeCurricularPlan.class, getDegreeCurricularPlanID());
    }

    public CourseGroup getCourseGroup(Integer courseGroupID) throws FenixFilterException, FenixServiceException {
        return (CourseGroup) readDomainObject(CourseGroup.class, courseGroupID);
    }

    public List<SelectItem> getCourseGroups() throws FenixFilterException, FenixServiceException {
        return (courseGroups == null) ? (courseGroups = readCourseGroups()) : courseGroups;
    }
    
    public List<String> getRulesLabels() throws FenixFilterException, FenixServiceException {
        final List<String> resultLabels = new ArrayList<String>();
        for (final CurricularRule curricularRule : getCourseGroup(getCourseGroupID()).getParticipatingCurricularRules()) {
            resultLabels.add(CurricularRuleLabelFormatter.getLabel(curricularRule));
        }
        return resultLabels;
    }

    public String createCourseGroup() throws FenixFilterException {
        try {
            final Object args[] = { getDegreeCurricularPlanID(), getParentCourseGroupID(), getName(), getNameEn() };
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
            final Object args[] = { getCourseGroupID(), getName(), getNameEn() };
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
            final Object args[] = { getCourseGroupID(), getContextID() };
            ServiceUtils.executeService(getUserView(), "DeleteContextFromDegreeModule", args);
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
            addInfoMessage(bolonhaResources.getString("courseGroupAssociated"));
            return "editCurricularPlanStructure";
        } catch (FenixActionException e) {
            this.addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (FenixFilterException e) {
            this.addErrorMessage(bolonhaResources.getString("error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (FenixServiceException e) {
            this.addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            this.addErrorMessage(domainResources.getString(e.getMessage()));
        } catch (Exception e) {
            this.addErrorMessage(bolonhaResources.getString("general.error"));
            return "editCurricularPlanStructure";
        }        
        return "";
    }
    
    private void checkCourseGroup() throws FenixFilterException, FenixServiceException, FenixActionException {
        if (getCourseGroupID() == null || getCourseGroupID().equals(this.NO_SELECTION)) {
            throw new FenixActionException("error.mustChooseACourseGroup");
        }
    }

    private List<SelectItem> readCourseGroups() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final DegreeModule degreeModule = getDegreeCurricularPlan().getDegreeModule();
        if (degreeModule instanceof CourseGroup) {
            Set<CourseGroup> allParents = getCourseGroup(getParentCourseGroupID()).getAllParentCourseGroups();
            collectChildCourseGroups(result, (CourseGroup) degreeModule, "", allParents);
        }
        
        result.add(0, new SelectItem(this.NO_SELECTION, bolonhaResources.getString("choose")));

        return result;
    }
    
    private void collectChildCourseGroups(final List<SelectItem> result, final CourseGroup courseGroup, final String previousCourseGroupName, Set<CourseGroup> allParents) throws FenixFilterException, FenixServiceException {
        String currentCourseGroupName = "";
        if (!courseGroup.isRoot()) {
            currentCourseGroupName = previousCourseGroupName + courseGroup.getName();
            if (getCourseGroup(getParentCourseGroupID()) != courseGroup && !allParents.contains(courseGroup)) {
                result.add(new SelectItem(courseGroup.getIdInternal(), currentCourseGroupName));
            }
        }
        for (final Context context : courseGroup.getSortedContextsWithCourseGroups()) {
            collectChildCourseGroups(result, (CourseGroup) context.getDegreeModule(), currentCourseGroupName + ((courseGroup.isRoot()) ? "" : " > "), allParents);
        }
    }
    
    public Integer getPosition() {
        return getAndHoldIntegerParameter("pos");
    }

    public String orderCourseGroup() {
        try {
            Object args[] = { getContextID(), getPosition() };
            ServiceUtils.executeService(getUserView(), "OrderDegreeModule", args);
            addInfoMessage(bolonhaResources.getString("courseGroupMoved"));
            return "editCurricularPlanStructure";
        } catch (FenixFilterException e) {
            this.addErrorMessage(bolonhaResources.getString("error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (DomainException e) {
            this.addErrorMessage(domainResources.getString(e.getMessage()));
        } catch (Exception e) {
            this.addErrorMessage(bolonhaResources.getString("general.error"));
            return "editCurricularPlanStructure";
        }        
        return "";
    }
    
}
