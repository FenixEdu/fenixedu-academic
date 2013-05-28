/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.AddContextToCourseGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.CreateCourseGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.DeleteContextFromDegreeModule;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.EditCourseGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.OrderDegreeModule;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CourseGroupManagementBackingBean extends CurricularCourseManagementBackingBean {

    private String name = null;
    private String nameEn = null;
    private Integer courseGroupID;
    private List<SelectItem> courseGroups = null;

    public Integer getParentCourseGroupID() {
        return getAndHoldIntegerParameter("parentCourseGroupID");
    }

    @Override
    public Integer getCourseGroupID() {
        return (this.courseGroupID != null) ? this.courseGroupID : getAndHoldIntegerParameter("courseGroupID");
    }

    @Override
    public void setCourseGroupID(Integer courseGroupID) {
        this.courseGroupID = courseGroupID;
    }

    @Override
    public String getName() {
        return (name == null && getCourseGroupID() != null) ? getCourseGroup(getCourseGroupID()).getName() : name;
    }

    @Override
    public String getNameEn() {
        return (nameEn == null && getCourseGroupID() != null) ? getCourseGroup(getCourseGroupID()).getNameEn() : nameEn;
    }

    public String getParentName() {
        return (getParentCourseGroupID() != null) ? getCourseGroup(getParentCourseGroupID()).getName() : null;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public CourseGroup getCourseGroup(Integer courseGroupID) {
        return (CourseGroup) AbstractDomainObject.fromExternalId(courseGroupID);
    }

    @Override
    public List<SelectItem> getCourseGroups() {
        return (courseGroups == null) ? (courseGroups = readCourseGroups()) : courseGroups;
    }

    @Override
    public List<String> getRulesLabels() {
        final List<String> resultLabels = new ArrayList<String>();
        for (final CurricularRule curricularRule : getCourseGroup(getCourseGroupID()).getParticipatingCurricularRules()) {
            resultLabels.add(CurricularRuleLabelFormatter.getLabel(curricularRule));
        }
        return resultLabels;
    }

    @Override
    protected ExecutionSemester getMinimumExecutionPeriod() {
        CourseGroup courseGroup = getCourseGroup(getParentCourseGroupID());;
        if (courseGroup == null) {
            final Context context = getContext(getContextID());
            if (context != null) {
                courseGroup = context.getParentCourseGroup();
            }
        }
        return (courseGroup == null) ? null : courseGroup.getMinimumExecutionPeriod();
    }

    public String createCourseGroup() {
        try {
            CreateCourseGroup.run(getDegreeCurricularPlanID(), getParentCourseGroupID(), getName(), getNameEn(),
                    getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID());
            addInfoMessage(bolonhaBundle.getString("courseGroupCreated"));
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
            EditCourseGroup.run(getCourseGroupID(), getContextID(), getName(), getNameEn(), getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID());
            addInfoMessage(bolonhaBundle.getString("courseGroupEdited"));
            return "editCurricularPlanStructure";
        } catch (final IllegalDataAccessException e) {
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
            DeleteContextFromDegreeModule.run(getCourseGroupID(), getContextID());
            addInfoMessage(bolonhaBundle.getString("courseGroupDeleted"));
            return "editCurricularPlanStructure";
        } catch (final IllegalDataAccessException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        }
        return "";
    }

    @Override
    public String addContext() {
        try {
            checkCourseGroup();
            AddContextToCourseGroup.run(getCourseGroup(getCourseGroupID()), getCourseGroup(getParentCourseGroupID()),
                    getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID());
            addInfoMessage(bolonhaBundle.getString("courseGroupAssociated"));
            return "editCurricularPlanStructure";
        } catch (FenixActionException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
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

    private boolean isBranch() {
        return (this.getCourseGroup() instanceof BranchCourseGroup);
    }

    public String getIfBranchShowType() {
        if (isBranch()) {
            return "<p class=\"mtop25\">" + BundleUtil.getMessageFromModuleOrApplication("BolonhaManager", "branchType") + ": "
                    + "<em><strong>"
                    + ((BranchCourseGroup) getCourseGroup()).getBranchType().getDescription(Language.getLocale())
                    + "</strong></em>" + "</p>";
        }
        return "";
    }

    private List<SelectItem> readCourseGroups() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final List<List<DegreeModule>> degreeModulesSet =
                getDegreeCurricularPlan().getDcpDegreeModulesIncludingFullPath(CourseGroup.class, null);
        final Set<CourseGroup> allParents = getCourseGroup(getParentCourseGroupID()).getAllParentCourseGroups();
        for (final List<DegreeModule> degreeModules : degreeModulesSet) {
            final DegreeModule lastDegreeModule = (degreeModules.size() > 0) ? degreeModules.get(degreeModules.size() - 1) : null;
            if (!allParents.contains(lastDegreeModule) && lastDegreeModule != getCourseGroup(getParentCourseGroupID())) {
                final StringBuilder pathName = new StringBuilder();
                for (final DegreeModule degreeModule : degreeModules) {
                    pathName.append((pathName.length() == 0) ? "" : " > ").append(degreeModule.getName());
                }
                result.add(new SelectItem(lastDegreeModule.getExternalId(), pathName.toString()));
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
            OrderDegreeModule.run(getContextID(), getPosition());
            addInfoMessage(bolonhaBundle.getString("courseGroupMoved"));
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
