package net.sourceforge.fenixedu.util;

import pt.utl.ist.fenix.tools.util.StringAppender;

public enum WorkingStudentSelectionType {
    WORKING_STUDENT, NOT_WORKING_STUDENT;
    public String getQualifiedName() {
	return StringAppender.append(WorkingStudentSelectionType.class.getSimpleName(), ".", name());
    }

}
