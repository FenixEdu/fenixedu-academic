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
package org.fenixedu.academic.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.fenixedu.academic.domain.curriculum.EnrollmentState;
import org.fenixedu.academic.domain.curriculum.grade.GradeScale;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.commons.i18n.LocalizedString;

public class Grade implements Serializable, Comparable<Grade> {

    private static Grade emptyGrade = new EmptyGrade();

    private static Map<String, Grade> gradeMap = new HashMap<String, Grade>();

    private final String value;

    private final GradeScale gradeScale;
    
    protected Grade() {
        value = null;
        gradeScale = null;
    }

    protected Grade(String value, GradeScale gradeScale) {
        if (EmptyGrade.qualifiesAsEmpty(value)) {
            throw new DomainException("error.grade.invalid.argument");
        }

        if (!gradeScale.belongsTo(value)) {
            throw new DomainException("error.grade.invalid.grade");
        }

        this.value = value.trim().toUpperCase();
        this.gradeScale = gradeScale;
    }

    @Override
    public int compareTo(final Grade otherGrade) {
        return gradeScale.compareGrades(this, otherGrade);
    }

    public BigDecimal getNumericValue() {
        return value == null ? null : new BigDecimal(getValue());
    }

    public String getValue() {
        return value;
    }

    public Integer getIntegerValue() {
        return isNumeric() ? Integer.valueOf(getValue()) : null;
    }

    public GradeScale getGradeScale() {
        return gradeScale;
    }

    public static Grade createGrade(String value, GradeScale gradeScale) {
        if (EmptyGrade.qualifiesAsEmpty(value)) {
            return createEmptyGrade();
        }

        Grade grade = gradeMap.get(exportAsString(gradeScale, value));
        if (grade == null) {
            grade = new Grade(value, gradeScale);
            gradeMap.put(grade.exportAsString(), grade);
        }
        return grade;
    }

    public static Grade createEmptyGrade() {
        return emptyGrade;
    }

    public static Grade importFromString(String string) {
        if (EmptyGrade.qualifiesAsEmpty(string)) {
            return emptyGrade;
        }

        String[] tokens = string.split(":");
        return createGrade(tokens[1], GradeScale.findUniqueByCode(tokens[0]).get());
    }

    @Override
    public String toString() {
        return exportAsString();
    }

    public String exportAsString() {
        return exportAsString(getGradeScale(), getValue());
    }

    private static String exportAsString(GradeScale gradeScale, String value) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gradeScale.getCode());
        stringBuilder.append(":");
        stringBuilder.append(value.trim().toUpperCase());

        return stringBuilder.toString();
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isNumeric() {
        /**
         * 
         * This was the original implementation, but it is very slow...
         * especially when generating reports that have to call this method for
         * practically every enrollment.
         * 
         * try { Double.parseDouble(getValue()); return true; } catch
         * (NumberFormatException e) { return false; }
         * 
         * 
         * This alternative implementation is roughly 30-40x faster! So I
         * suggest you keep this ugly code where it is. Using StringUtils may
         * appear to be nicer, but beware that it will produce different
         * results!
         * 
         */
        if (value.isEmpty()) {
            return false;
        }
        boolean foundSeperator = false;
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            if (c == '.' || c == ',') {
                if (foundSeperator) {
                    return false;
                } else {
                    foundSeperator = true;
                }
            } else if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean isApproved() {
        return getGradeScale().isApproved(this);
    }

    public boolean isNotApproved() {
        return getGradeScale().isNotApproved(this);
    }

    public LocalizedString getExtendedValue() {
        return gradeScale.getExtendedValue(this);
    }

    public EnrollmentState getEnrolmentState() {
        return isApproved() ? EnrollmentState.APROVED : EnrollmentState.NOT_APROVED;
    }

}
