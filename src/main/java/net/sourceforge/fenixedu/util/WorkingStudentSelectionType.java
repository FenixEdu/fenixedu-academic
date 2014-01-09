package net.sourceforge.fenixedu.util;

public enum WorkingStudentSelectionType {
    WORKING_STUDENT, NOT_WORKING_STUDENT;
    public String getQualifiedName() {
        return WorkingStudentSelectionType.class.getSimpleName() + "." + name();
    }

}
