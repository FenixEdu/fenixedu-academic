/*
 * Created on 9/Mai/2003
 *
 */
package Dominio;

/**
 * @author asnr and scpo
 *  
 */
public class StudentGroupAttend extends DomainObject implements IStudentGroupAttend {

    private Integer keyAttend;

    private Integer keyStudentGroup;

    private IAttends attend;

    private IStudentGroup studentGroup;

    /**
     * Construtor
     */

    public StudentGroupAttend() {
    }

    /**
     * Construtor
     */
    public StudentGroupAttend(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * Construtor
     */
    public StudentGroupAttend(IStudentGroup studentGroup, IAttends attend) {

        this.studentGroup = studentGroup;
        this.attend = attend;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof IStudentGroupAttend) {
            result = (getStudentGroup().equals(((IStudentGroupAttend) arg0).getStudentGroup()))
                    && (getAttend().equals(((IStudentGroupAttend) arg0).getAttend()));
        }
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[STUDENT_GROUP_ATTEND";
        result += ", keyStudentGroup=" + getKeyStudentGroup();
        result += ", studentGroup=" + getStudentGroup();
        //result += ", attend=" + getAttend();
        result += "]";
        return result;
    }

    /**
     * @return Integer
     */
    public Integer getKeyStudentGroup() {
        return keyStudentGroup;
    }

    /**
     * @return Integer
     */
    public Integer getKeyAttend() {
        return keyAttend;
    }

    /**
     * @return StudentGroup
     */
    public IStudentGroup getStudentGroup() {
        return studentGroup;
    }

    /**
     * @return Attends
     */
    public IAttends getAttend() {
        return attend;
    }

    /**
     * Sets the keyGroup.
     * 
     * @param keyGroup
     *            The keyGroup to set
     */
    public void setKeyStudentGroup(Integer keyStudentGroup) {
        this.keyStudentGroup = keyStudentGroup;
    }

    /**
     * Sets the keyAttend.
     * 
     * @param keyAttend
     *            The keyAttend to set
     */
    public void setKeyAttend(Integer keyAttend) {
        this.keyAttend = keyAttend;
    }

    /**
     * Sets the studentGroup.
     * 
     * @param studentGroup
     *            The studentGroup to set
     */
    public void setStudentGroup(IStudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }

    /**
     * Sets the attend.
     * 
     * @param attend
     *            The attend to set
     */
    public void setAttend(IAttends attend) {
        this.attend = attend;
    }

}