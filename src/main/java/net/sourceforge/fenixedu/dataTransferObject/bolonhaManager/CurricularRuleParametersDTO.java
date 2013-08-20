/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.bolonhaManager;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

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
    private Integer minimumYear;
    private Integer maximumYear;
    private Double credits;
    private DegreeType degreeType;
    private Boolean even;

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

    public Integer getMaximumYear() {
        return maximumYear;
    }

    public void setMaximumYear(Integer maximumYear) {
        this.maximumYear = maximumYear;
    }

    public Integer getMinimumYear() {
        return minimumYear;
    }

    public void setMinimumYear(Integer minimumYear) {
        this.minimumYear = minimumYear;
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
}
