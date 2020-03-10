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
 * Created on Feb 6, 2006
 */
package org.fenixedu.academic.dto.bolonhaManager;

import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.dto.CurricularPeriodInfoDTO;
import org.fenixedu.academic.dto.DataTranferObject;

@SuppressWarnings("serial")
public class CurricularRuleParametersDTO extends DataTranferObject {

    private String selectedDegreeModuleID;
    private String contextCourseGroupID;
    private String selectedDegreeID;
    private String selectedDepartmentUnitID;
    private CurricularPeriodInfoDTO curricularPeriodInfoDTO;
    private Double minimumCredits;
    private Double maximumCredits;
    private Integer minimumLimit;
    private Integer maximumLimit;
    private Double credits;
    private DegreeType degreeType;
    private Boolean even;
    private Boolean optionalsConfiguration;

    public CurricularRuleParametersDTO() {
    }

    public String getContextCourseGroupID() {
        return contextCourseGroupID;
    }

    public void setContextCourseGroupID(String contextCourseGroup) {
        this.contextCourseGroupID = contextCourseGroup;
    }

    public CurricularPeriodInfoDTO getCurricularPeriodInfoDTO() {
        return curricularPeriodInfoDTO;
    }

    public void setCurricularPeriodInfoDTO(CurricularPeriodInfoDTO curricularPeriodInfoDTO) {
        this.curricularPeriodInfoDTO = curricularPeriodInfoDTO;
    }

    public Double getMaximumCredits() {
        return maximumCredits;
    }

    public void setMaximumCredits(Double maxDouble) {
        this.maximumCredits = maxDouble;
    }

    public Double getMinimumCredits() {
        return minimumCredits;
    }

    public void setMinimumCredits(Double minDouble) {
        this.minimumCredits = minDouble;
    }

    public String getSelectedDegreeModuleID() {
        return selectedDegreeModuleID;
    }

    public void setSelectedDegreeModuleID(String precedenceDegreeModule) {
        this.selectedDegreeModuleID = precedenceDegreeModule;
    }

    public Integer getMinimumLimit() {
        return minimumLimit;
    }

    public void setMinimumLimit(Integer minimumLimit) {
        this.minimumLimit = minimumLimit;
    }

    public Integer getMaximumLimit() {
        return maximumLimit;
    }

    public void setMaximumLimit(Integer maximumLimit) {
        this.maximumLimit = maximumLimit;
    }

    public String getSelectedDegreeID() {
        return selectedDegreeID;
    }

    public void setSelectedDegreeID(String selectedDegreeID) {
        this.selectedDegreeID = selectedDegreeID;
    }

    public String getSelectedDepartmentUnitID() {
        return selectedDepartmentUnitID;
    }

    public void setSelectedDepartmentUnitID(String departmentUnitID) {
        this.selectedDepartmentUnitID = departmentUnitID;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public Boolean getEven() {
        return even;
    }

    public void setEven(Boolean even) {
        this.even = even;
    }

    public Boolean getOptionalsConfiguration() {
        return optionalsConfiguration;
    }

    public void setOptionalsConfiguration(final Boolean input) {
        this.optionalsConfiguration = input;
    }

}
