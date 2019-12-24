/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Dec 7, 2005
 */
package org.fenixedu.academic.ui.faces.bean.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.degreeStructure.BranchCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.bolonhaManager.AddContextToCourseGroup;
import org.fenixedu.academic.service.services.bolonhaManager.CreateCourseGroup;
import org.fenixedu.academic.service.services.bolonhaManager.DeleteContextFromDegreeModule;
import org.fenixedu.academic.service.services.bolonhaManager.EditCourseGroup;
import org.fenixedu.academic.service.services.bolonhaManager.OrderDegreeModule;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.CurricularRuleLabelFormatter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

import com.google.common.base.Strings;

import pt.ist.fenixframework.FenixFramework;

public class CourseGroupManagementBackingBean extends CurricularCourseManagementBackingBean {

    private String name = null;
    private String nameEn = null;
    private String courseGroupID;
    private String programConclusionID = null;
    private List<SelectItem> courseGroups = null;
    private Boolean isOptional;

    public String getParentCourseGroupID() {
        return getAndHoldStringParameter("parentCourseGroupID");
    }

    public String getProgramConclusionID() {
        if (this.programConclusionID != null) {
            return this.programConclusionID;
        } else {
            if (getCourseGroup() != null && getCourseGroup().getProgramConclusion() != null) {
                return getCourseGroup().getProgramConclusion().getExternalId();
            }
        }
        return null;
    }

    @Override
    public String getCourseGroupID() {
        return (this.courseGroupID != null) ? this.courseGroupID : getAndHoldStringParameter("courseGroupID");
    }

    @Override
    public void setCourseGroupID(String courseGroupID) {
        this.courseGroupID = courseGroupID;
    }

    public void setProgramConclusionID(String programConclusionID) {
        this.programConclusionID = programConclusionID;
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

    public Boolean getIsOptional() {
        return (isOptional == null && getCourseGroupID() != null) ? getCourseGroup(getCourseGroupID()).getIsOptional() : isOptional;
    }

    public void setIsOptional(Boolean isOptional) {
        this.isOptional = isOptional;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public CourseGroup getCourseGroup(String courseGroupID) {
        return (CourseGroup) FenixFramework.getDomainObject(courseGroupID);
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
    protected ExecutionInterval getMinimumExecutionPeriod() {
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
                    getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID(), getProgramConclusionID());
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "courseGroupCreated"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        }
        return "";
    }

    public String editCourseGroup() {
        try {
            EditCourseGroup.run(getCourseGroupID(), getContextID(), getName(), getNameEn(), getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID(), getIsOptional(), getProgramConclusionID());
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "courseGroupEdited"));
            return "editCurricularPlanStructure";
        } catch (final IllegalDataAccessException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(e.getLocalizedMessage());
        }
        return "";
    }

    public String deleteCourseGroup() {
        try {
            DeleteContextFromDegreeModule.run(getCourseGroupID(), getContextID());
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "courseGroupDeleted"));
            return "editCurricularPlanStructure";
        } catch (final IllegalDataAccessException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        }
        return "";
    }

    @Override
    public String addContext() {
        try {
            checkCourseGroup();
            AddContextToCourseGroup.run(getCourseGroup(getCourseGroupID()), getCourseGroup(getParentCourseGroupID()),
                    getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID());
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "courseGroupAssociated"));
            return "editCurricularPlanStructure";
        } catch (FenixActionException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (FenixServiceException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (DomainException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "general.error"));
            return "editCurricularPlanStructure";
        }
        return "";
    }

    private boolean isBranch() {
        return (this.getCourseGroup() instanceof BranchCourseGroup);
    }

    public String getIfBranchShowType() {
        if (isBranch()) {
            return "<p class=\"mtop25\">" + BundleUtil.getString(Bundle.BOLONHA, "branchType") + ": " + "<em><strong>"
                    + ((BranchCourseGroup) getCourseGroup()).getBranchType().getDescription(I18N.getLocale()) + "</strong></em>"
                    + "</p>";
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
        result.add(0, new SelectItem(this.NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "choose")));
        return result;
    }

    public Integer getPosition() {
        return getAndHoldIntegerParameter("pos");
    }

    public String orderCourseGroup() {
        try {
            OrderDegreeModule.run(getContextID(), getPosition());
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "courseGroupMoved"));
            return "editCurricularPlanStructure";
        } catch (DomainException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "general.error"));
            return "editCurricularPlanStructure";
        }
        return "";
    }

    public List<SelectItem> getProgramConclusionItems() {
        return Bennu.getInstance().getProgramConclusionSet().stream().map(pc -> {
            String name = pc.getName().getContent();
            String description = pc.getDescription().getContent();
            if (!Strings.isNullOrEmpty(description)) {
                name += " - " + description;
            }
            return new SelectItem(pc.getExternalId(), name);
        }).collect(Collectors.toList());

    }
}
