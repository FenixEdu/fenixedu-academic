/*
 * Created on 9/Mai/2003
 *
 */
package Dominio;

/**
 * @author asnr and scpo
 */
public class StudentGroup extends DomainObject implements IStudentGroup {

    private Integer groupNumber;

    private Integer keyShift;

    private Integer keyGroupProperties;

    private IGroupProperties groupProperties;

    private ITurno shift;

    /**
     * Construtor
     */
    public StudentGroup() {
    }

    /**
     * Construtor
     */
    public StudentGroup(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * Construtor
     */
    public StudentGroup(Integer groupNumber, IGroupProperties groupProperties) {

        this.groupNumber = groupNumber;
        this.groupProperties = groupProperties;
    }

    /**
     * Construtor
     */
    public StudentGroup(Integer groupNumber, IGroupProperties groupProperties, ITurno shift) {

        this.groupNumber = groupNumber;
        this.groupProperties = groupProperties;
        this.shift = shift;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof IStudentGroup) {
            if (this.getkeyGroupProperties() == null || ((StudentGroup) arg0).keyGroupProperties == null)
                return false;
            result = (getGroupProperties().equals(((IStudentGroup) arg0).getGroupProperties()))
                    && (getGroupNumber().equals(((IStudentGroup) arg0).getGroupNumber()));

        }
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[STUDENT_GROUP";
        result += ", groupNumber=" + getGroupNumber();
        result += ", groupProperties=" + getGroupProperties();
        result += ", shift =" + getShift();
        result += "]";
        return result;
    }

    /**
     * @return Integer
     */
    public Integer getKeyShift() {
        return keyShift;
    }

    /**
     * @return Integer
     */
    public Integer getkeyGroupProperties() {
        return keyGroupProperties;
    }

    /**
     * @return Integer
     */
    public Integer getGroupNumber() {
        return groupNumber;
    }

    /**
     * @return GroupProperties
     */
    public IGroupProperties getGroupProperties() {
        return groupProperties;
    }

    /**
     * @return Turno
     */
    public ITurno getShift() {
        return shift;
    }

    /**
     * Sets the keyShift.
     * 
     * @param keyShift
     *            The keyShift to set
     */
    public void setKeyShift(Integer keyShift) {
        this.keyShift = keyShift;
    }

    /**
     * Sets the groupProperties.
     * 
     * @param groupProperties
     *            The groupProperties to set
     */

    public void setKeyGroupProperties(Integer keyGroupProperties) {
        this.keyGroupProperties = keyGroupProperties;
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
     * Sets the groupProperties.
     * 
     * @param groupProperties
     *            The groupProperties to set
     */
    public void setGroupProperties(IGroupProperties groupProperties) {
        this.groupProperties = groupProperties;
    }

    /**
     * Sets the shift.
     * 
     * @param shift
     *            The shift to set
     */
    public void setShift(ITurno shift) {
        this.shift = shift;
    }
}