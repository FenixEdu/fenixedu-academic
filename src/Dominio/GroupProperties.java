/*
 * Created on 6/Mai/2003
 *
 */
package Dominio;

import java.util.Calendar;

import Util.EnrolmentGroupPolicyType;
import Util.TipoAula;

/**
 * @author asnr and scpo
 *  
 */
public class GroupProperties extends DomainObject implements IGroupProperties {

    private Integer maximumCapacity;

    private Integer minimumCapacity;

    private Integer idealCapacity;

    private EnrolmentGroupPolicyType enrolmentPolicy;

    private Integer groupMaximumNumber;

    private String name;

    private Integer keyExecutionCourse;

    private IExecutionCourse executionCourse;

    private TipoAula shiftType;

    private Calendar enrolmentBeginDay;

    private Calendar enrolmentEndDay;

    private String projectDescription;

    /**
     * Construtor
     */
    public GroupProperties() {
    }

    /**
     * Construtor
     */
    public GroupProperties(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * Construtor
     */
    public GroupProperties(IExecutionCourse executionCourse, String name) {
        this.executionCourse = executionCourse;
        this.name = name;
    }

    /**
     * Construtor
     */
    public GroupProperties(Integer maximumCapacity, Integer minimumCapacity, Integer idealCapacity,
            EnrolmentGroupPolicyType enrolmentPolicy, Integer groupMaximumNumber, String name,
            IExecutionCourse executionCourse, TipoAula shiftType, Calendar enrolmentBeginDay,
            Calendar enrolmentEndDay, String projectDescription) {
        this.maximumCapacity = maximumCapacity;
        this.minimumCapacity = minimumCapacity;
        this.idealCapacity = idealCapacity;
        this.enrolmentPolicy = enrolmentPolicy;
        this.groupMaximumNumber = groupMaximumNumber;
        this.name = name;
        this.executionCourse = executionCourse;
        this.shiftType = shiftType;
        this.enrolmentBeginDay = enrolmentBeginDay;
        this.enrolmentEndDay = enrolmentEndDay;
        this.projectDescription = projectDescription;

    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof IGroupProperties) {
            result = (getExecutionCourse().equals(((IGroupProperties) arg0).getExecutionCourse()))
                    && (getName().equals(((IGroupProperties) arg0).getName()));
        }
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[GROUP";
        result += ", maximumCapacity=" + getMaximumCapacity();
        result += ", minimumCapacity=" + getMinimumCapacity();
        result += ", idealCapacity=" + getIdealCapacity();
        result += ", enrolmentPolicy=" + getEnrolmentPolicy();
        result += ", groupMaximumNumber=" + getGroupMaximumNumber();
        result += ", name=" + getName();
        result += ", executionCourse=" + getExecutionCourse();
        result += ", shiftType=" + getShiftType();
        //result += ", enrolmentBeginDay=" + getEnrolmentBeginDay();
        //result += ", enrolmentEndDay=" + getEnrolmentEndDay();
        result += ", projectDescription=" + getProjectDescription();
        result += "]";
        return result;
    }

    /**
     * @return Integer
     */
    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }

    /**
     * @return Integer
     */
    public Integer getMaximumCapacity() {
        return maximumCapacity;
    }

    /**
     * @return Integer
     */
    public Integer getMinimumCapacity() {
        return minimumCapacity;
    }

    /**
     * @return Integer
     */
    public Integer getIdealCapacity() {
        return idealCapacity;
    }

    /**
     * @return EnrolmentPolicy
     */
    public EnrolmentGroupPolicyType getEnrolmentPolicy() {
        return enrolmentPolicy;
    }

    /**
     * @return Integer
     */
    public Integer getGroupMaximumNumber() {
        return groupMaximumNumber;
    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * @return String
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * @return DisciplinaExecucao
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * @return Tipo Aula
     */
    public TipoAula getShiftType() {
        return shiftType;
    }

    /**
     * @return Calendar
     */
    public Calendar getEnrolmentBeginDay() {
        return enrolmentBeginDay;
    }

    /**
     * @return Calendar
     */
    public Calendar getEnrolmentEndDay() {
        return enrolmentEndDay;
    }

    /**
     * Sets the keyExecutionCourse.
     * 
     * @param keyExecutionCourse
     *            The keyExecutionCourse to set
     */
    public void setKeyExecutionCourse(Integer keyExecutionCourse) {
        this.keyExecutionCourse = keyExecutionCourse;
    }

    /**
     * Sets the maximumCapacity.
     * 
     * @param maximumCapacity
     *            The maximumCapacity to set
     */
    public void setMaximumCapacity(Integer maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    /**
     * Sets the minimumCapacity.
     * 
     * @param minimumCapacity
     *            The minimumCapacity to set
     */
    public void setMinimumCapacity(Integer minimumCapacity) {
        this.minimumCapacity = minimumCapacity;
    }

    /**
     * Sets the idealCapacity.
     * 
     * @param idealCapacity
     *            The idealCapacity to set
     */
    public void setIdealCapacity(Integer idealCapacity) {
        this.idealCapacity = idealCapacity;
    }

    /**
     * Sets the enrolmentPolicy.
     * 
     * @param enrolmentPolicy
     *            The enrolmentPolicy to set
     */
    public void setEnrolmentPolicy(EnrolmentGroupPolicyType enrolmentPolicy) {
        this.enrolmentPolicy = enrolmentPolicy;
    }

    /**
     * Sets the groupMaximumNumber.
     * 
     * @param groupMaximumNumber
     *            The groupMaximumNumber to set
     */
    public void setGroupMaximumNumber(Integer groupMaximumNumber) {
        this.groupMaximumNumber = groupMaximumNumber;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set
     */
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    /**
     * Sets the executionCourse.
     * 
     * @param executionCourse
     *            The executionCourse to set
     */
    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    /**
     * Sets the shiftType.
     * 
     * @param shiftType
     *            The shiftType to set
     */
    public void setShiftType(TipoAula shiftType) {
        this.shiftType = shiftType;
    }

    /**
     * Sets the enrolmentBeginDay.
     * 
     * @param enrolmentBeginDay
     *            The enrolmentBeginDay to set
     */
    public void setEnrolmentBeginDay(Calendar enrolmentBeginDay) {
        this.enrolmentBeginDay = enrolmentBeginDay;
    }

    /**
     * Sets the enrolmentEndDay.
     * 
     * @param enrolmentEndDay
     *            The enrolmentEndDay to set
     */
    public void setEnrolmentEndDay(Calendar enrolmentEndDay) {
        this.enrolmentEndDay = enrolmentEndDay;
    }

}