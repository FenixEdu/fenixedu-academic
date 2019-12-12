/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.curriculum;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.GradeScaleEnum;
import org.fenixedu.academic.domain.exceptions.DomainException;

@Deprecated
class Grade implements IGrade {
    private Object gradeValue;

    private GradeType gradeType;

    public Grade(int grade) {
        initNumeric(grade);
    }

    public Grade(String grade) {
        if (grade == null || grade.equals("") || grade.equals(GradeScaleEnum.NA)) {
            this.gradeValue = GradeScaleEnum.NA;
            this.gradeType = GradeType.GRADENA;
        } else if (StringUtils.isNumeric(grade)) {
            Integer numericGrade = Integer.parseInt(grade);
            initNumeric(numericGrade);
        } else if (grade.equals(GradeScaleEnum.AP)) {
            this.gradeValue = grade;
            this.gradeType = GradeType.GRADEAP;
        } else {
            this.gradeValue = GradeScaleEnum.RE;
            this.gradeType = GradeType.GRADERE;
        }
    }

    protected void initNumeric(int grade) {
        this.gradeValue = grade;

        if (grade <= 5) {
            this.gradeType = GradeType.GRADEFIVE;
        } else {
            this.gradeType = GradeType.GRADETWENTY;
        }
    }

    @Override
    public Object getGradeValue() {
        return gradeValue;
    }

    @Override
    public GradeType getGradeType() {
        return gradeType;
    }

    // very important: don't change this
    @Override
    public int compareTo(IGrade o) {
        if (this.getGradeType() == o.getGradeType()) {
            if (this.getGradeType() == GradeType.GRADEFIVE || this.getGradeType() == GradeType.GRADETWENTY) {
                Integer grade1 = (Integer) this.getGradeValue();
                Integer grade2 = (Integer) o.getGradeValue();
                return grade1.compareTo(grade2);
            } else {
                return 0;
            }
        }
        if (this.getGradeType() == GradeType.GRADENA || this.getGradeType() == GradeType.GRADERE) {
            return 1;
        }
        if (o.getGradeType() == GradeType.GRADENA || o.getGradeType() == GradeType.GRADERE) {
            return -1;
        }

        throw new DomainException("error.grade.different.grade.types");
    }

}

@Deprecated
public class GradeFactory {
    private static GradeFactory instance = new GradeFactory();

    private IGrade[] flyWeight;

    private GradeFactory() {
        flyWeight = new IGrade[24];

        flyWeight[21] = new Grade(GradeScaleEnum.RE);
        flyWeight[22] = new Grade(GradeScaleEnum.NA);
        flyWeight[23] = new Grade(GradeScaleEnum.AP);

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
        if (key == null || key.equals("") || key.equals(GradeScaleEnum.NA)) {
            return 22;
        }

        if (key.equals(GradeScaleEnum.RE)) {
            return 21;
        }

        if (key.equals(GradeScaleEnum.AP)) {
            return 23;
        }

        return Integer.parseInt(key);
    }

    public static GradeFactory getInstance() {
        return instance;
    }
}
