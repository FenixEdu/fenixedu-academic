/*
 * Created on 22/Jul/2003
 *  
 */
package DataBeans;

import Dominio.IStudentGroup;

/**
 * @author asnr and scpo
 *  
 */

public class InfoStudentGroup extends InfoObject {

    private Integer groupNumber;

    private InfoShift infoShift;

    private InfoGroupProperties infoGroupProperties;

    /**
     * Construtor
     */
    public InfoStudentGroup() {
    }

    /**
     * Construtor
     */
    public InfoStudentGroup(Integer groupNumber, InfoGroupProperties infoGroupProperties) {

        this.groupNumber = groupNumber;
        this.infoGroupProperties = infoGroupProperties;
    }

    /**
     * Construtor
     */
    public InfoStudentGroup(Integer groupNumber, InfoGroupProperties infoGroupProperties,
            InfoShift infoShift) {

        this.groupNumber = groupNumber;
        this.infoGroupProperties = infoGroupProperties;
        this.infoShift = infoShift;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof InfoStudentGroup) {
            result = (getInfoGroupProperties()
                    .equals(((InfoStudentGroup) arg0).getInfoGroupProperties()))
                    && (getGroupNumber().equals(((InfoStudentGroup) arg0).getGroupNumber()));
            if (getInfoShift() != null) {
                result = result && (getInfoShift().equals(((InfoStudentGroup) arg0).getInfoShift()));
            } else if (((InfoStudentGroup) arg0).getInfoShift() != null) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[INFO_STUDENT_GROUP";
        result += ", groupNumber=" + getGroupNumber();
        result += ", infoGroupProperties=" + getInfoGroupProperties();
        result += ", infoShift" + getInfoShift();
        result += "]";
        return result;
    }

    /**
     * @return Integer
     */
    public Integer getGroupNumber() {
        return groupNumber;
    }

    /**
     * @return InfoGroupProperties
     */
    public InfoGroupProperties getInfoGroupProperties() {
        return infoGroupProperties;
    }

    /**
     * @return InfoTurno
     */
    public InfoShift getInfoShift() {
        return infoShift;
    }

    /**
     * Sets the groupNumber.
     * 
     * @param groupNumber
     *            The groupNumber to set
     */
    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    /**
     * Sets the infoGroupProperties.
     * 
     * @param infoGroupProperties
     *            The infoGroupProperties to set
     */
    public void setInfoGroupProperties(InfoGroupProperties infoGroupProperties) {
        this.infoGroupProperties = infoGroupProperties;
    }

    /**
     * Sets the infoShift.
     * 
     * @param infoShift
     *            The infoShift to set
     */
    public void setInfoShift(InfoShift infoShift) {
        this.infoShift = infoShift;
    }

    public void copyFromDomain(IStudentGroup studentGroup) {
        super.copyFromDomain(studentGroup);

        if (studentGroup != null) {
            setGroupNumber(studentGroup.getGroupNumber());
        }
    }

    public static InfoStudentGroup newInfoFromDomain(IStudentGroup studentGroup) {
        InfoStudentGroup infoStudentGroup = null;
        if (studentGroup != null) {
            infoStudentGroup = new InfoStudentGroup();
            infoStudentGroup.copyFromDomain(studentGroup);
        }
        return infoStudentGroup;
    }
}