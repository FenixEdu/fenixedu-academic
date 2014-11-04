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
package org.fenixedu.academic.ui.faces.bean.bolonhaManager.curricularPlans;

import java.util.List;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroupInformation;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.FenixFramework;

public class CycleCourseGroupInformationManagementBackingBean extends CurricularCourseManagementBackingBean {

    private String courseGroupID;
    private String informationExecutionYearId;

    private String informationId;

    private String graduateTitleSuffix;
    private String graduateTitleSuffixEn;

    public CycleCourseGroup getCourseGroup(String courseGroupID) {
        return (CycleCourseGroup) FenixFramework.getDomainObject(courseGroupID);
    }

    public List<CycleCourseGroupInformation> getCycleCourseGroupInformationList() {
        CycleCourseGroup courseGroup = getCourseGroup(getCourseGroupID());
        return courseGroup.getCycleCourseGroupInformationOrderedByExecutionYear();
    }

    public String prepareEditCourseGroupInformation() {
        CycleCourseGroupInformation information = getInformation();
        setInformationExecutionYearId(information.getExecutionYear().getExternalId());
        setInformationId(information.getExternalId());

        setGraduateTitleSuffix(information.getGraduateTitleSuffixDefault());
        setGraduateTitleSuffixEn(information.getGraduateTitleSuffixEn());

        return "";
    }

    public String editCourseGroupInformation() {
        try {
            CycleCourseGroupInformation information = getInformation();

            if (information != null) {
                information.edit(getInformationExecutionYear(), getGraduateTitleSuffix(), getGraduateTitleSuffixEn());
            } else {
                CycleCourseGroup courseGroup = getCourseGroup(getCourseGroupID());
                courseGroup.createCycleCourseGroupInformation(getInformationExecutionYear(), getGraduateTitleSuffix(),
                        getGraduateTitleSuffixEn());
            }

            this.addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "cycleCourseGroupInformationEdit"));

            setGraduateTitleSuffix("");
            setGraduateTitleSuffixEn("");
            setInformationExecutionYearId(null);

            return "editCurricularPlanStructure";
        } catch (DomainException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
            return "";
        }
    }

    /* GETTERS AND SETTERS */

    @Override
    public String getCourseGroupID() {
        return (this.courseGroupID != null) ? this.courseGroupID : getAndHoldStringParameter("courseGroupID");
    }

    @Override
    public void setCourseGroupID(String courseGroupID) {
        this.courseGroupID = courseGroupID;
    }

    public String getInformationExecutionYearId() {
        return informationExecutionYearId != null ? informationExecutionYearId : NO_SELECTION_STRING;
    }

    public void setInformationExecutionYearId(String informationExecutionYearId) {
        this.informationExecutionYearId = informationExecutionYearId;
    }

    public ExecutionYear getInformationExecutionYear() {
        return FenixFramework.getDomainObject(getInformationExecutionYearId());
    }

    public String getInformationId() {
        return informationId != null ? informationId : (informationId = getAndHoldStringParameter("informationId"));
    }

    public CycleCourseGroupInformation getInformation() {
        return getInformationId() != null ? FenixFramework.<CycleCourseGroupInformation> getDomainObject(getInformationId()) : null;
    }

    public void setInformationId(String informationId) {
        this.informationId = informationId;
    }

    public String getGraduateTitleSuffix() {
        return graduateTitleSuffix;
    }

    public void setGraduateTitleSuffix(String graduateTitleSuffix) {
        this.graduateTitleSuffix = graduateTitleSuffix;
    }

    public String getGraduateTitleSuffixEn() {
        return graduateTitleSuffixEn;
    }

    public void setGraduateTitleSuffixEn(String graduateTitleSuffixEn) {
        this.graduateTitleSuffixEn = graduateTitleSuffixEn;
    }
}
