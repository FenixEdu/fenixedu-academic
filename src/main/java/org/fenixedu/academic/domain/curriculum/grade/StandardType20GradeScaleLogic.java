package org.fenixedu.academic.domain.curriculum.grade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScaleEnum.GradeScaleLogic;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class StandardType20GradeScaleLogic implements GradeScaleLogic {

    protected static final List<String> APPROVED_TEXTUAL_GRADES = Arrays.asList("AP");
    protected static final List<String> NOT_APPROVED_TEXTUAL_GRADES = Arrays.asList("ANUL", "D", "F", "NADM", "RE", "SUSPEN");
    private static final List<String> NOT_EVALUATED_TEXTUAL_GRADES = Arrays.asList("NA");

    private static final List<String> TEXTUAL_GRADES = new ArrayList<String>();

    static {
        TEXTUAL_GRADES.addAll(APPROVED_TEXTUAL_GRADES);
        TEXTUAL_GRADES.addAll(NOT_APPROVED_TEXTUAL_GRADES);
        TEXTUAL_GRADES.addAll(NOT_EVALUATED_TEXTUAL_GRADES);
    }
    
    @Override
    public boolean checkNotFinal(final Grade grade) {
        return belongsTo(grade.getValue());
    }

    @Override
    public boolean checkFinal(final Grade grade) {
        return belongsTo(grade.getValue());
    }

    @Override
    public boolean belongsTo(final String value) {
        if (TEXTUAL_GRADES.contains(value)) {
            return true;
        }

        try {
            final double doubleValue = Double.parseDouble(value);
            return doubleValue >= 0 && doubleValue <= 20;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String qualify(final Grade grade) {
        throw new DomainException("GradeScale.unable.to.qualify.given.grade.use.qualitative.scale");
    }

    @Override
    public boolean isNotEvaluated(final Grade grade) {
        final String value = grade.getValue();
        return grade.isEmpty() || NOT_EVALUATED_TEXTUAL_GRADES.contains(value);
    }

    @Override
    public boolean isNotApproved(final Grade grade) {
        final String value = grade.getValue();

        if (NOT_APPROVED_TEXTUAL_GRADES.contains(value) || isNotEvaluated(grade)) {
            return true;
        }

        try {
            return Double.parseDouble(value) < 9.5d;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean isApproved(final Grade grade) {
        final String value = grade.getValue();

        if (APPROVED_TEXTUAL_GRADES.contains(value)) {
            return true;
        }

        try {
            final double doubleValue = Double.parseDouble(value);
            return 9.5d <= doubleValue && doubleValue <= 20;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public int compareGrades(Grade leftGrade, Grade rightGrade) {

        if (rightGrade == null || rightGrade.isEmpty()) {
            return 1;
        }

        if (leftGrade == null || leftGrade.isEmpty()) {
            return -1;
        }

        if (!leftGrade.getGradeScale().equals(rightGrade.getGradeScale())) {
            throw new DomainException("Grade.unsupported.comparassion.of.grades.of.different.scales");
        }

        final boolean isLeftApproved = isApproved(leftGrade);
        final boolean isRightApproved = isApproved(rightGrade);

        if (isLeftApproved && isRightApproved) {
            return compareGradeValues(leftGrade, rightGrade);
        } else if (isLeftApproved) {
            return 1;
        } else if (isRightApproved) {
            return -1;
        } else {
            return compareGradeValues(leftGrade, rightGrade);
        }

    }

    private int compareGradeValues(Grade left, Grade right) {

        final boolean leftIsNumber = NumberUtils.isNumber(left.getValue());
        final boolean rightIsNumber = NumberUtils.isNumber(right.getValue());

        if (leftIsNumber && !rightIsNumber) {
            return 1;
        } else if (!leftIsNumber && rightIsNumber) {
            return -1;
        } else if (leftIsNumber && rightIsNumber) {
            return left.getNumericValue().compareTo(right.getNumericValue());
        }

        return left.getValue().compareTo(right.getValue());
    }

}
