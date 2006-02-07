/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.bolonhaManager;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

public class CurricularRuleParametersDTO extends DataTranferObject {
    
    private Integer precedenceDegreeModuleID;
    private Integer contextCourseGroupID;
    private CurricularPeriodInfoDTO curricularPeriodInfoDTO;    
    private Double minimumCredits;
    private Double maximumCredits;
    private Integer minimumLimit;
    private Integer maximumLimit;
    
    public CurricularRuleParametersDTO() {

    }
    
    public Integer getContextCourseGroupID() {
        return contextCourseGroupID;
    }
    public void setContextCourseGroupID(Integer contextCourseGroup) {
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
    public Integer getPrecedenceDegreeModuleID() {
        return precedenceDegreeModuleID;
    }
    public void setPrecedenceDegreeModuleID(Integer precedenceDegreeModule) {
        this.precedenceDegreeModuleID = precedenceDegreeModule;
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
}
