package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.util.EnrolmentEquivalenceType;

/**
 * @author dcs-rjao
 * 
 * 22/Abr/2003
 */
public class InfoEquivalence extends InfoObject {

    private InfoEnrolment infoEnrolment;

    private InfoEnrolment infoEquivalentEnrolment;

    private EnrolmentEquivalenceType equivalenceType;

    public InfoEquivalence() {
        setInfoEnrolment(null);
        setInfoEquivalentEnrolment(null);
        setEquivalenceType(null);
    }

    public InfoEquivalence(InfoEnrolment enrolment, InfoEnrolment equivalentEnrolment,
            EnrolmentEquivalenceType equivalenceType) {
        setInfoEnrolment(enrolment);
        setInfoEquivalentEnrolment(equivalentEnrolment);
        setEquivalenceType(equivalenceType);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof InfoEquivalence) {
            InfoEquivalence equivalence = (InfoEquivalence) obj;

            resultado = (this.getInfoEnrolment().equals(equivalence.getInfoEnrolment()))
                    && (this.getInfoEquivalentEnrolment().equals(equivalence
                            .getInfoEquivalentEnrolment()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "enrolment = " + this.infoEnrolment + "; ";
        result += "equivalentEnrolment = " + this.infoEquivalentEnrolment + "; ";
        result += "equivalenceType" + this.equivalenceType + "]\n";
        return result;
    }

    /**
     * @return EnrolmentEquivalenceType
     */
    public EnrolmentEquivalenceType getEquivalenceType() {
        return equivalenceType;
    }

    /**
     * @return InfoEnrolment
     */
    public InfoEnrolment getInfoEnrolment() {
        return infoEnrolment;
    }

    /**
     * @return InfoEnrolment
     */
    public InfoEnrolment getInfoEquivalentEnrolment() {
        return infoEquivalentEnrolment;
    }

    /**
     * Sets the equivalenceType.
     * 
     * @param equivalenceType
     *            The equivalenceType to set
     */
    public void setEquivalenceType(EnrolmentEquivalenceType equivalenceType) {
        this.equivalenceType = equivalenceType;
    }

    /**
     * Sets the infoEnrolment.
     * 
     * @param infoEnrolment
     *            The infoEnrolment to set
     */
    public void setInfoEnrolment(InfoEnrolment infoEnrolment) {
        this.infoEnrolment = infoEnrolment;
    }

    /**
     * Sets the infoEquivalentEnrolment.
     * 
     * @param infoEquivalentEnrolment
     *            The infoEquivalentEnrolment to set
     */
    public void setInfoEquivalentEnrolment(InfoEnrolment infoEquivalentEnrolment) {
        this.infoEquivalentEnrolment = infoEquivalentEnrolment;
    }

}