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

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.FenixFramework;

public class CycleCourseGroupInformationManagementBackingBean extends CurricularCourseManagementBackingBean {

    private String courseGroupID;
    private String informationExecutionYearId;

    private String graduatedTitle;
    private String graduatedTitleEn;

    private String informationId;
    private String editGraduatedTitle;
    private String editGraduatedTitleEn;

    private String editInformationExecutionYearId;

    public CycleCourseGroup getCourseGroup(String courseGroupID) {
        return (CycleCourseGroup) FenixFramework.getDomainObject(courseGroupID);
    }

    public List<CycleCourseGroupInformation> getCycleCourseGroupInformationList() {
        CycleCourseGroup courseGroup = getCourseGroup(getCourseGroupID());
        return courseGroup.getCycleCourseGroupInformationOrderedByExecutionYear();
    }

    public String createCourseGroupInformation() {
        try {
            CycleCourseGroup courseGroup = getCourseGroup(getCourseGroupID());
            courseGroup.createCycleCourseGroupInformation(getInformationExecutionYear(), getGraduatedTitle(),
                    getGraduatedTitleEn());

            this.addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "cycleCourseGroupInformationAdded"));

            setInformationExecutionYearId(null);
            setGraduatedTitle("");
            setGraduatedTitleEn("");

            return "editCurricularPlanStructure";
        } catch (DomainException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
            return "";
        }
    }

    public String prepareEditCourseGroupInformation() {
        CycleCourseGroupInformation information = getInformation();
        setEditGraduatedTitle(information.getGraduatedTitlePt());
        setEditGraduatedTitleEn(information.getGraduatedTitleEn());
        setEditInformationExecutionYearId(information.getExecutionYear().getExternalId());

        return "";
    }

    public String editCourseGroupInformation() {
        try {
            CycleCourseGroupInformation information = getInformation();
            information.edit(getEditInformationExecutionYear(), getEditGraduatedTitle(), getEditGraduatedTitleEn());

            this.addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "cycleCourseGroupInformationEdit"));

            setEditGraduatedTitle("");
            setEditGraduatedTitleEn("");
            setEditInformationExecutionYearId(null);

            return "editCurricularPlanStructure";
        } catch (DomainException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
            return "";
        }
    }

    /* GETTERS AND SETTERS */

    public String getGraduatedTitle() {
        return graduatedTitle;
    }

    public void setGraduatedTitle(String graduatedTitle) {
        this.graduatedTitle = graduatedTitle;
    }

    public String getGraduatedTitleEn() {
        return graduatedTitleEn;
    }

    public void setGraduatedTitleEn(String graduatedTitleEn) {
        this.graduatedTitleEn = graduatedTitleEn;
    }

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

    public String getEditGraduatedTitle() {
        return editGraduatedTitle;
    }

    public void setEditGraduatedTitle(String editGraduatedTitle) {
        this.editGraduatedTitle = editGraduatedTitle;
    }

    public String getEditGraduatedTitleEn() {
        return editGraduatedTitleEn;
    }

    public void setEditGraduatedTitleEn(String editGraduatedTitleEn) {
        this.editGraduatedTitleEn = editGraduatedTitleEn;
    }

    public String getEditInformationExecutionYearId() {
        return editInformationExecutionYearId;
    }

    public void setEditInformationExecutionYearId(String editExecutionYearId) {
        this.editInformationExecutionYearId = editExecutionYearId;
    }

    public ExecutionYear getEditInformationExecutionYear() {
        return FenixFramework.getDomainObject(getEditInformationExecutionYearId());
    }

}
