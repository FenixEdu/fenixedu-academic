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
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.CreateBranchCourseGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import java.util.Locale;

public class BranchCourseGroupManagementBackingBean extends CourseGroupManagementBackingBean {

    private String branchTypeName;
    private List<SelectItem> branchTypes = null;

    public String getBranchTypeName() {
        return (branchTypeName != null) ? branchTypeName : BranchType.MAJOR.getName();//BundleUtil.getStringFromResourceBundle("resources.BolonhaManagerResources", "choose");
    }

    public void setBranchTypeName(String branchTypeName) {
        this.branchTypeName = branchTypeName;
    }

    public BranchType getBranchType() {
        return BranchType.valueOf(getBranchTypeName());
        /*
        if(branchType == null && getCourseGroupID() != null) {
            CourseGroup group = getCourseGroup(getCourseGroupID());
            return (group instanceof BranchCourseGroup ? ((BranchCourseGroup) group).getBranchType() : branchType);
        }
            return branchType;
            */
    }

    public List<SelectItem> getBranchTypes() {
        return (branchTypes == null) ? (branchTypes = readBranchTypes()) : branchTypes;
    }

    private List<SelectItem> readBranchTypes() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final List<BranchType> entries = new ArrayList<BranchType>(Arrays.asList(BranchType.values()));
        for (BranchType entry : entries) {
            result.add(new SelectItem(entry.name(), entry.getDescription(I18N.getLocale())));
        }
        return result;
    }

    public String createBranchCourseGroup() {
        try {
            CreateBranchCourseGroup.run(getDegreeCurricularPlanID(), getParentCourseGroupID(), getName(), getNameEn(),
                    getBranchType(), getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID());
            addInfoMessage(bolonhaBundle.getString("branchCourseGroupCreated"));
            return "editCurricularPlanStructure";
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        }
        return "";
    }
}
