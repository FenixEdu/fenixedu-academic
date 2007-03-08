/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

public class CourseGroupManagementBackingBean extends CurricularCourseManagementBackingBean {
    
    private String name = null;
    private String nameEn = null;
    private String courseGroupTypeValue = null;
    private Integer courseGroupID;
    private List<SelectItem> courseGroups = null;

    public Integer getParentCourseGroupID() {
        return getAndHoldIntegerParameter("parentCourseGroupID");
    }

    public Integer getCourseGroupID() {
        return (this.courseGroupID != null) ? this.courseGroupID : getAndHoldIntegerParameter("courseGroupID");
    }

    public void setCourseGroupID(Integer courseGroupID) {
        this.courseGroupID = courseGroupID;
    }
    
    public String getName() {
        return (name == null && getCourseGroupID() != null) ? getCourseGroup(getCourseGroupID()).getName() : name;    
    }

    public String getNameEn() {
        return (nameEn == null && getCourseGroupID() != null) ? getCourseGroup(getCourseGroupID()).getNameEn() : nameEn;    
    }
    
    public String getCourseGroupTypeValue() {
        return (courseGroupTypeValue == null && getCourseGroupID() != null) ? getCourseGroup(getCourseGroupID()).getCourseGroupType().name() : courseGroupTypeValue;    
    }
    
    private DegreeType getCourseGroupType() {
	return StringUtils.isEmpty(getCourseGroupTypeValue()) ? null : DegreeType.valueOf(getCourseGroupTypeValue());
    }
    
    public List<SelectItem> getCourseGroupTypeValues() {
	final List<SelectItem> result = new ArrayList<SelectItem>();
	final DegreeType degreeType = getDegreeCurricularPlan().getDegree().getDegreeType();
	if (degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
	    result.add(new SelectItem("", " -"));
	    result.add(new SelectItem(DegreeType.BOLONHA_DEGREE.name(), bolonhaBundle.getString("CourseGroupType." + DegreeType.BOLONHA_DEGREE.name())));
	    result.add(new SelectItem(DegreeType.BOLONHA_MASTER_DEGREE.name(), bolonhaBundle.getString("CourseGroupType." + DegreeType.BOLONHA_MASTER_DEGREE.name())));
	}
	return result;
    }
    
    public String getParentName() {
        return (getParentCourseGroupID() != null) ? getCourseGroup(getParentCourseGroupID()).getName() : null;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    
    public void setCourseGroupTypeValue(String courseGroupTypeValue) {
        this.courseGroupTypeValue = courseGroupTypeValue;
    }

    public CourseGroup getCourseGroup(Integer courseGroupID) {
        return (CourseGroup) rootDomainObject.readDegreeModuleByOID(courseGroupID);
    }

    public List<SelectItem> getCourseGroups() {
        return (courseGroups == null) ? (courseGroups = readCourseGroups()) : courseGroups;
    }
    
    public List<String> getRulesLabels() {
        final List<String> resultLabels = new ArrayList<String>();
        for (final CurricularRule curricularRule : getCourseGroup(getCourseGroupID()).getParticipatingCurricularRules()) {
            resultLabels.add(CurricularRuleLabelFormatter.getLabel(curricularRule));
        }
        return resultLabels;
    }
    
    public String createCourseGroup() {
        try {
            final Object args[] = { getDegreeCurricularPlanID(), getParentCourseGroupID(), getName(),
                    getNameEn(), getCourseGroupType(), getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID() };
            ServiceUtils.executeService(getUserView(), "CreateCourseGroup", args);
            addInfoMessage(bolonhaBundle.getString("courseGroupCreated"));
            return "editCurricularPlanStructure";
        } catch (FenixFilterException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        }
        return "";
    }

    public String editCourseGroup() {
        try {
            final Object args[] = { getCourseGroupID(), getContextID(), getName(), getNameEn(),
                    getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID() };
            ServiceUtils.executeService(getUserView(), "EditCourseGroup", args);
            addInfoMessage(bolonhaBundle.getString("courseGroupEdited"));
            return "editCurricularPlanStructure";
        } catch (FenixFilterException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        }
        return "";
    }

    public String deleteCourseGroup() {
        try {
            final Object args[] = { getCourseGroupID(), getContextID() };
            ServiceUtils.executeService(getUserView(), "DeleteContextFromDegreeModule", args);
            addInfoMessage(bolonhaBundle.getString("courseGroupDeleted"));
            return "editCurricularPlanStructure";
        } catch (FenixFilterException e) {
                this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
                return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        }
        return "";
    }
    
    public String addContext() {
        try {
            checkCourseGroup();
            Object args[] = { getCourseGroup(getCourseGroupID()),
                    getCourseGroup(getParentCourseGroupID()), getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID() };
            ServiceUtils.executeService(getUserView(), "AddContextToCourseGroup", args);
            addInfoMessage(bolonhaBundle.getString("courseGroupAssociated"));
            return "editCurricularPlanStructure";
        } catch (FenixActionException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (FenixFilterException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (FenixServiceException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (DomainException e) {
            this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        } catch (Exception e) {
            this.addErrorMessage(bolonhaBundle.getString("general.error"));
            return "editCurricularPlanStructure";
        }        
        return "";
    }
    
    private void checkCourseGroup() throws FenixFilterException, FenixServiceException, FenixActionException {
        if (getCourseGroupID() == null || getCourseGroupID().equals(this.NO_SELECTION)) {
            throw new FenixActionException("error.mustChooseACourseGroup");
        }
    }
    
    private List<SelectItem> readCourseGroups() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final List<List<DegreeModule>> degreeModulesSet = getDegreeCurricularPlan().getDcpDegreeModulesIncludingFullPath(CourseGroup.class, null);
        final Set<CourseGroup> allParents = getCourseGroup(getParentCourseGroupID()).getAllParentCourseGroups();
        for (final List<DegreeModule> degreeModules : degreeModulesSet) {
            final DegreeModule lastDegreeModule = (degreeModules.size() > 0) ? degreeModules.get(degreeModules.size() - 1) : null;
            if (!allParents.contains(lastDegreeModule) && lastDegreeModule != getCourseGroup(getParentCourseGroupID())) {
                final StringBuilder pathName = new StringBuilder();
                for (final DegreeModule degreeModule : degreeModules) {
                    pathName.append((pathName.length() == 0) ? "" : " > ").append(degreeModule.getName());
                }
                result.add(new SelectItem(lastDegreeModule.getIdInternal(), pathName.toString()));
            }
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        return result;
    }
    
    public Integer getPosition() {
        return getAndHoldIntegerParameter("pos");
    }

    public String orderCourseGroup() {
        try {
            Object args[] = { getContextID(), getPosition() };
            ServiceUtils.executeService(getUserView(), "OrderDegreeModule", args);
            addInfoMessage(bolonhaBundle.getString("courseGroupMoved"));
            return "editCurricularPlanStructure";
        } catch (FenixFilterException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (DomainException e) {
            this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        } catch (Exception e) {
            this.addErrorMessage(bolonhaBundle.getString("general.error"));
            return "editCurricularPlanStructure";
        }        
        return "";
    }
    
}
