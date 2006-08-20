package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentKind;
import net.sourceforge.fenixedu.domain.student.StudentType;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class InfoStudentKind extends InfoObject {

	private final StudentKind studentKind;

    public InfoStudentKind(final StudentKind studentKind) {
    	this.studentKind = studentKind;
    }

    public static InfoStudentKind newInfoFromDomain(final StudentKind studentKind) {
    	return studentKind == null ? null : new InfoStudentKind(studentKind);
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoStudentKind && studentKind == ((InfoStudentKind) obj).studentKind;
    }

    public String toString() {
    	return studentKind.toString();
    }

    public StudentType getStudentType() {
        return studentKind.getStudentType();
    }

    public Integer getMaxCoursesToEnrol() {
        return studentKind.getMaxCoursesToEnrol();
    }

    public Integer getMaxNACToEnrol() {
        return studentKind.getMaxNACToEnrol();
    }

    public Integer getMinCoursesToEnrol() {
        return studentKind.getMinCoursesToEnrol();
    }

	@Override
	public Integer getIdInternal() {
		return studentKind.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}
