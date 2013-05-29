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
