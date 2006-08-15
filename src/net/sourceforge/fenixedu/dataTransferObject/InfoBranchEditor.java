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

    private Integer idInternal;

    public InfoBranchEditor() {
		super(null);
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public BranchType getBranchType() {
		return branchType;
	}

	public void setBranchType(BranchType branchType) {
		this.branchType = branchType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
		return infoDegreeCurricularPlan;
	}

	public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public Integer getSecondaryCredits() {
		return secondaryCredits;
	}

	public void setSecondaryCredits(Integer secondaryCredits) {
		this.secondaryCredits = secondaryCredits;
	}

	public Integer getSpecializationCredits() {
		return specializationCredits;
	}

	public void setSpecializationCredits(Integer specializationCredits) {
		this.specializationCredits = specializationCredits;
	}

	public Integer getIdInternal() {
		return idInternal;
	}

	public void setIdInternal(Integer idInternal) {
		this.idInternal = idInternal;
	}

}
