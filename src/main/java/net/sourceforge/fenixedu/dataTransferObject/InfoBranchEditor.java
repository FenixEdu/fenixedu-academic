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
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.branch.BranchType;

public class InfoBranchEditor extends InfoBranch {

    private String name;

    private String nameEn;

    private String code;

    private Integer specializationCredits;

    private Integer secondaryCredits;

    private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    private BranchType branchType;

    private String acronym;

    private String externalId;

    public InfoBranchEditor() {
        super(null);
    }

    @Override
    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    @Override
    public BranchType getBranchType() {
        return branchType;
    }

    public void setBranchType(BranchType branchType) {
        this.branchType = branchType;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return infoDegreeCurricularPlan;
    }

    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
        this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public Integer getSecondaryCredits() {
        return secondaryCredits;
    }

    public void setSecondaryCredits(Integer secondaryCredits) {
        this.secondaryCredits = secondaryCredits;
    }

    @Override
    public Integer getSpecializationCredits() {
        return specializationCredits;
    }

    public void setSpecializationCredits(Integer specializationCredits) {
        this.specializationCredits = specializationCredits;
    }

    @Override
    public String getExternalId() {
        return externalId;
    }

    @Override
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

}
