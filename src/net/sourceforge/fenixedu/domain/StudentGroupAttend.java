/*
 * Created on 08/Mar/2005
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author joaosa & rmalo
 *  
 */
public class StudentGroupAttend extends StudentGroupAttend_Base {

    /**
     * Construtor
     */
    public StudentGroupAttend() {
    }

    /**
     * Construtor
     */
    public StudentGroupAttend(IStudentGroup studentGroup, IAttends attend) {
        super.setStudentGroup(studentGroup);
        super.setAttend(attend);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[STUDENT_GROUP_ATTEND";
        result += ", keyStudentGroup=" + getKeyStudentGroup();
        result += ", studentGroup=" + getStudentGroup();
        result += "]";
        return result;
    }

}
