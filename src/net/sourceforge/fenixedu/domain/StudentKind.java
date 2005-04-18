package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.StudentType;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class StudentKind extends StudentKind_Base {

    private StudentType studentType;

    public StudentKind() {
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IStudentKind) {
            IStudentKind studentType = (IStudentKind) obj;
            resultado = (this.getStudentType().equals(studentType.getStudentType()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "idInternal = " + this.getIdInternal() + "; ";
        result += "studentType = " + this.studentType + "; ";
        result += "minCoursesToEnrol = " + this.getMinCoursesToEnrol() + "; ";
        result += "maxNACToEnrol = " + this.getMaxNACToEnrol() + "; ";
        result += "maxCoursesToEnrol = " + this.getMaxCoursesToEnrol() + "]\n";
        return result;
    }

    public StudentType getStudentType() {
        return studentType;
    }

    public void setStudentType(StudentType studentType) {
        this.studentType = studentType;
    }
}