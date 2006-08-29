package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.StudentKind;
import net.sourceforge.fenixedu.domain.student.StudentType;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class InfoStudentKind extends InfoObject {

    private final DomainReference<StudentKind> studentKind;

    public InfoStudentKind(final StudentKind studentKind) {
        this.studentKind = new DomainReference<StudentKind>(studentKind);
    }

    public static InfoStudentKind newInfoFromDomain(final StudentKind studentKind) {
        return studentKind == null ? null : new InfoStudentKind(studentKind);
    }

    public boolean equals(Object obj) {
        return obj instanceof InfoStudentKind && getStudentKind() == ((InfoStudentKind) obj).getStudentKind();
    }

    public String toString() {
        return getStudentKind().toString();
    }

    public StudentType getStudentType() {
        return getStudentKind().getStudentType();
    }

    public Integer getMaxCoursesToEnrol() {
        return getStudentKind().getMaxCoursesToEnrol();
    }

    public Integer getMaxNACToEnrol() {
        return getStudentKind().getMaxNACToEnrol();
    }

    public Integer getMinCoursesToEnrol() {
        return getStudentKind().getMinCoursesToEnrol();
    }

    @Override
    public Integer getIdInternal() {
        return getStudentKind().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

    private StudentKind getStudentKind() {
        return studentKind == null ? null : studentKind.getObject();
    }

}
