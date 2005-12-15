package net.sourceforge.fenixedu.domain.curriculum;

import org.apache.commons.lang.StringUtils;

class Grade implements IGrade {
    private Object grade;

    private GradeType gradeType;

    public Grade(int grade) {
        initNumeric(grade);
    }

    public Grade(String grade) {
        if (grade == null || grade.equals("") || grade.equals("NA")) {
            this.grade = "NA";
            this.gradeType = GradeType.GRADENA;
        } else if (StringUtils.isNumeric(grade)) {
            Integer numericGrade = Integer.parseInt((String) grade);
            initNumeric(numericGrade);
        } else if (grade.equals("AP")) {
            this.grade = grade;
            this.gradeType = GradeType.GRADEAP;
        } else {
            this.grade = "RE";
            this.gradeType = GradeType.GRADERE;
        }
    }

    protected void initNumeric(int grade) {
        this.grade = grade;

        if (grade <= 5) {
            this.gradeType = GradeType.GRADEFIVE;
        } else {
            this.gradeType = GradeType.GRADETWENTY;
        }
    }

    public Object getGrade() {
        return grade;
    }

    public GradeType getGradeType() {
        return gradeType;
    }
}

public class GradeFactory {
    private static GradeFactory instance = new GradeFactory();

    private IGrade[] flyWeight;

    private GradeFactory() {
        flyWeight = new IGrade[24];

        flyWeight[21] = new Grade("RE");
        flyWeight[22] = new Grade("NA");
        flyWeight[23] = new Grade("AP");

        for (int i = 0; i < 21; i++) {
            flyWeight[i] = new Grade(i);
        }
    }

    public IGrade getGrade(String key) {
        return flyWeight[getGradePosition(key)];
    }

    public IGrade getGrade(int key) {
        return flyWeight[key];
    }

    private int getGradePosition(String key) {
        if (key == null || key.equals("") || key.equals("NA")) {
            return 22;
        }

        if (key.equals("RE")) {
            return 21;
        }

        if (key.equals("AP")) {
            return 23;
        }

        return Integer.parseInt(key);
    }

    public static GradeFactory getInstance() {
        return instance;
    }
}
