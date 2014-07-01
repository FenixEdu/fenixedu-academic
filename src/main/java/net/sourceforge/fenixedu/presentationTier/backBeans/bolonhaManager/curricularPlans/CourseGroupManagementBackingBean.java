/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixframework.FenixFramework;

public class CourseGroupManagementBackingBean extends CurricularCourseManagementBackingBean {

    private String name = null;
    private String nameEn = null;
    private String courseGroupID;
    private List<SelectItem> courseGroups = null;

    public String getParentCourseGroupID() {
        return getAndHoldStringParameter("parentCourseGroupID");
    }

    @Override
    public String getCourseGroupID() {
        return (this.courseGroupID != null) ? this.courseGroupID : getAndHoldStringParameter("courseGroupID");
    }

    @Override
    public void setCourseGroupID(String courseGroupID) {
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
                    getFinalEndExecutionPeriodID());
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "courseGroupEdited"));
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
            return "<p class=\"mtop25\">" + BundleUtil.getString(Bundle.BOLONHA, "branchType") + ": "
                    + "<em><strong>"
                    + ((BranchCourseGroup) getCourseGroup()).getBranchType().getDescription(I18N.getLocale())
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

}
