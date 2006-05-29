package net.sourceforge.fenixedu.domain.student;

public enum StudentType {

    NORMAL(1), WORKING_STUDENT(2), FOREIGN_STUDENT(3), EXTERNAL_STUDENT(4), OTHER(5);

    private int state;

    StudentType(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public static StudentType getStudentTypeByState(int state) {

        switch (state) {

        case 1:
            return NORMAL;
        case 2:
            return WORKING_STUDENT;
        case 3:
            return FOREIGN_STUDENT;
        case 4:
            return EXTERNAL_STUDENT;
        case 5:
            return OTHER;
        }

        return null;
    }

}
