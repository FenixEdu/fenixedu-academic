/*
 * Created on 22/Jul/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IStudentGroupAttend;

/**
 * @author asnr and scpo
 *  
 */
public class InfoStudentGroupAttend extends InfoObject {

    private InfoFrequenta infoAttend;

    private InfoStudentGroup infoStudentGroup;

    /**
     * Construtor
     */

    public InfoStudentGroupAttend() {
    }

    /**
     * Construtor
     */
    public InfoStudentGroupAttend(InfoStudentGroup infoStudentGroup, InfoFrequenta infoAttend) {

        this.infoStudentGroup = infoStudentGroup;
        this.infoAttend = infoAttend;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof InfoStudentGroupAttend) {
            result = (getInfoStudentGroup()
                    .equals(((InfoStudentGroupAttend) arg0).getInfoStudentGroup()))
                    && (getInfoAttend().equals(((InfoStudentGroupAttend) arg0).getInfoAttend()));
        }
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[INFO_STUDENT_GROUP_ATTEND";
        //result += ", infoStudentGroup=" + getInfoStudentGroup();
        //result += ", infoAttend=" + getInfoAttend();
        result += "]";
        return result;
    }

    /**
     * @return InfoStudentGroup
     */
    public InfoStudentGroup getInfoStudentGroup() {
        return infoStudentGroup;
    }

    /**
     * @return InfoFrequenta
     */
    public InfoFrequenta getInfoAttend() {
        return infoAttend;
    }

    /**
     * Sets the infoStudentGroup.
     * 
     * @param infoStudentGroup
     *            The infoStudentGroup to set
     */
    public void setInfoStudentGroup(InfoStudentGroup infoStudentGroup) {
        this.infoStudentGroup = infoStudentGroup;
    }

    /**
     * Sets the infoAttend.
     * 
     * @param infoAttend
     *            The infoAttend to set
     */
    public void setInfoAttend(InfoFrequenta infoAttend) {
        this.infoAttend = infoAttend;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IStudentGroupAttend studentGroupAttend) {
        super.copyFromDomain(studentGroupAttend);
    }

    public static InfoStudentGroupAttend newInfoFromDomain(IStudentGroupAttend studentGroupAttend) {
        InfoStudentGroupAttend infoStudentGroupAttend = null;
        if (studentGroupAttend != null) {
            infoStudentGroupAttend = new InfoStudentGroupAttend();
            infoStudentGroupAttend.copyFromDomain(studentGroupAttend);
        }
        return infoStudentGroupAttend;
    }
}