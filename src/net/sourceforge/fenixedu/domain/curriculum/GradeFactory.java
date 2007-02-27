package net.sourceforge.fenixedu.domain.curriculum;

import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

class Grade implements IGrade {
    private Object grade;

    private GradeType gradeType;

    public Grade(int grade) {
        initNumeric(grade);
    }

    public Grade(String grade) {
        if (grade == null || grade.equals("") || grade.equals(GradeScale.NA)) {
            this.grade = GradeScale.NA;
            this.gradeType = GradeType.GRADENA;
        } else if (StringUtils.isNumeric(grade)) {
            Integer numericGrade = Integer.parseInt((String) grade);
            initNumeric(numericGrade);
        } else if (grade.equals(GradeScale.AP)) {
            this.grade = grade;
            this.gradeType = GradeType.GRADEAP;
        } else {
            this.grade = GradeScale.RE;
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

    
    //very important: don't change this
    public int compareTo(IGrade o) {
        if(this.getGradeType() == o.getGradeType()) {
            if(this.getGradeType() == GradeType.GRADEFIVE || this.getGradeType() == GradeType.GRADETWENTY) {
                Integer grade1 = (Integer) this.getGrade();
                Integer grade2 = (Integer) o.getGrade();
                return grade1.compareTo(grade2);
            } else {
                return 0;
            }
        }
        if(this.getGradeType() == GradeType.GRADENA || this.getGradeType() == GradeType.GRADERE) {
            return 1;
        }
        if(o.getGradeType() == GradeType.GRADENA || o.getGradeType() == GradeType.GRADERE) {
            return -1;
        }
        
        throw new DomainException("error.grade.different.grade.types");
    }

}

public class GradeFactory {
    private static GradeFactory instance = new GradeFactory();

    private IGrade[] flyWeight;

    private GradeFactory() {
        flyWeight = new IGrade[24];

        flyWeight[21] = new Grade(GradeScale.RE);
        flyWeight[22] = new Grade(GradeScale.NA);
        flyWeight[23] = new Grade(GradeScale.AP);

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
        if (key == null || key.equals("") || key.equals(GradeScale.NA)) {
            return 22;
        }

        if (key.equals(GradeScale.RE)) {
            return 21;
        }

        if (key.equals(GradeScale.AP)) {
            return 23;
        }

        return Integer.parseInt(key);
    }

    public static GradeFactory getInstance() {
        return instance;
    }
}
