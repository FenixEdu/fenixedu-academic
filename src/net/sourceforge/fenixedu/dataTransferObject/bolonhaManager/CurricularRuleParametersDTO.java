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
    private Integer minimumInt;
    private Integer maximumInt;
    private Double minimumDouble;
    private Double maximumDouble;
    
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
    public Double getMaximumDouble() {
        return maximumDouble;
    }
    public void setMaximumDouble(Double maxDouble) {
        this.maximumDouble = maxDouble;
    }
    public Integer getMaximumInt() {
        return maximumInt;
    }
    public void setMaximumInt(Integer maxInt) {
        this.maximumInt = maxInt;
    }
    public Double getMinimumDouble() {
        return minimumDouble;
    }
    public void setMinimumDouble(Double minDouble) {
        this.minimumDouble = minDouble;
    }
    public Integer getMinimumInt() {
        return minimumInt;
    }
    public void setMinimumInt(Integer minInt) {
        this.minimumInt = minInt;
    }
    public Integer getPrecedenceDegreeModuleID() {
        return precedenceDegreeModuleID;
    }
    public void setPrecedenceDegreeModuleID(Integer precedenceDegreeModule) {
        this.precedenceDegreeModuleID = precedenceDegreeModule;
    }
    
}
