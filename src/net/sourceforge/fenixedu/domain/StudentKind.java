package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class StudentKind extends StudentKind_Base {

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "idInternal = " + this.getIdInternal() + "; ";
        result += "studentType = " + this.getStudentType() + "; ";
        result += "minCoursesToEnrol = " + this.getMinCoursesToEnrol() + "; ";
        result += "maxNACToEnrol = " + this.getMaxNACToEnrol() + "; ";
        result += "maxCoursesToEnrol = " + this.getMaxCoursesToEnrol() + "]\n";
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IStudentKind) {
            final IStudentKind studentKind = (IStudentKind) obj;
            return this.getIdInternal().equals(studentKind.getIdInternal());
        }
        return false;
    }

}
