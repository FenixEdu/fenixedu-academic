/*
 * Created on Oct 25, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.dto;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

/**
 * @author João Mota
 *
 */
public class SchoolClassDTO extends DataTranferObject {

    private String schoolClassName;
    private Integer schoolClassId;
    private Integer executionPeriodId;
    private String degreeCurricularPlanName;
    private Integer degreeCurricularPlanId;
    private Integer degreeId;
    private String degreeInitials;
    
    /**
     * 
     */
    public SchoolClassDTO() {
        super();
    }

    /**
     * @return Returns the degreeCurricularPlanId.
     */
    public Integer getDegreeCurricularPlanId() {
        return degreeCurricularPlanId;
    }
    /**
     * @param degreeCurricularPlanId The degreeCurricularPlanId to set.
     */
    public void setDegreeCurricularPlanId(Integer degreeCurricularPlanId) {
        this.degreeCurricularPlanId = degreeCurricularPlanId;
    }
    /**
     * @return Returns the degreeCurricularPlanName.
     */
    public String getDegreeCurricularPlanName() {
        return degreeCurricularPlanName;
    }
    /**
     * @param degreeCurricularPlanName The degreeCurricularPlanName to set.
     */
    public void setDegreeCurricularPlanName(String degreeCurricularPlanName) {
        this.degreeCurricularPlanName = degreeCurricularPlanName;
    }
    /**
     * @return Returns the degreeId.
     */
    public Integer getDegreeId() {
        return degreeId;
    }
    /**
     * @param degreeId The degreeId to set.
     */
    public void setDegreeId(Integer degreeId) {
        this.degreeId = degreeId;
    }
    /**
     * @return Returns the degreeInitials.
     */
    public String getDegreeInitials() {
        return degreeInitials;
    }
    /**
     * @param degreeInitials The degreeInitials to set.
     */
    public void setDegreeInitials(String degreeInitials) {
        this.degreeInitials = degreeInitials;
    }
    /**
     * @return Returns the executionPeriodId.
     */
    public Integer getExecutionPeriodId() {
        return executionPeriodId;
    }
    /**
     * @param executionPeriodId The executionPeriodId to set.
     */
    public void setExecutionPeriodId(Integer executionPeriodId) {
        this.executionPeriodId = executionPeriodId;
    }
    /**
     * @return Returns the schoolClassId.
     */
    public Integer getSchoolClassId() {
        return schoolClassId;
    }
    /**
     * @param schoolClassId The schoolClassId to set.
     */
    public void setSchoolClassId(Integer schoolClassId) {
        this.schoolClassId = schoolClassId;
    }
    /**
     * @return Returns the schoolClassName.
     */
    public String getSchoolClassName() {
        return schoolClassName;
    }
    /**
     * @param schoolClassName The schoolClassName to set.
     */
    public void setSchoolClassName(String schoolClassName) {
        this.schoolClassName = schoolClassName;
    }
}
