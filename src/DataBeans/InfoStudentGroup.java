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

    private InfoAttendsSet infoAttendsSet;

    /**
     * Construtor
     */
    public InfoStudentGroup() {
    }

    /**
     * Construtor
     */
    public InfoStudentGroup(Integer groupNumber,
            InfoAttendsSet infoAttendsSet) {

        this.groupNumber = groupNumber;
        this.infoAttendsSet = infoAttendsSet;
    }

    /**
     * Construtor
     */
    public InfoStudentGroup(Integer groupNumber,
            InfoAttendsSet infoAttendsSet, InfoShift infoShift) {

        this.groupNumber = groupNumber;
        this.infoAttendsSet = infoAttendsSet;
        this.infoShift = infoShift;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof InfoStudentGroup) {
            result = (getInfoAttendsSet().equals(((InfoStudentGroup) arg0)
                    .getInfoAttendsSet()))
                    && (getGroupNumber().equals(((InfoStudentGroup) arg0)
                            .getGroupNumber()));
            if (getInfoShift() != null) {
                result = result
                        && (getInfoShift().equals(((InfoStudentGroup) arg0)
                                .getInfoShift()));
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
        result += ", infoAttendsSet=" + getInfoAttendsSet();
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
    public InfoAttendsSet getInfoAttendsSet() {
        return infoAttendsSet;
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
    public void setInfoAttendsSet(InfoAttendsSet infoAttendsSet) {
        this.infoAttendsSet = infoAttendsSet;
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
        
        if(studentGroup != null) {
            setGroupNumber(studentGroup.getGroupNumber());
        }
    }
    
    public static InfoStudentGroup newInfoFromDomain(IStudentGroup studentGroup) {
        InfoStudentGroup infoStudentGroup = null;
        System.out.println("InfoStudentGroup newInfoFromDomain-Inicio ");
        if(studentGroup != null) {
            infoStudentGroup = new InfoStudentGroup();
            infoStudentGroup.copyFromDomain(studentGroup);
        }
        System.out.println("InfoStudentGroup newInfoFromDomain-Fim ");
        return infoStudentGroup;
    }
}