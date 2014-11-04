/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class GradeDistribution {
    private static final String A = "A";
    private static final String B = "B";
    private static final String C = "C";
    private static final String D = "D";
    private static final String E = "E";

    private GradeScale gradeScale;
    private Map<Grade, Distribution> gradeDistribution;

    static public GradeDistribution IST_SCALE_20 = new GradeDistribution(GradeScale.TYPE20);
    static public GradeDistribution IST_SCALE_5 = new GradeDistribution(GradeScale.TYPE5);
    static public GradeDistribution ECTS_SCALE_20 = new GradeDistribution(GradeScale.TYPE20);
    static {
        IST_SCALE_20.addDistribution(Grade.createGrade("10", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), D);
        IST_SCALE_20.addDistribution(Grade.createGrade("11", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), D);
        IST_SCALE_20.addDistribution(Grade.createGrade("12", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), D);
        IST_SCALE_20.addDistribution(Grade.createGrade("13", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), D);
        IST_SCALE_20.addDistribution(Grade.createGrade("14", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), C);
        IST_SCALE_20.addDistribution(Grade.createGrade("15", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), C);
        IST_SCALE_20.addDistribution(Grade.createGrade("16", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), B);
        IST_SCALE_20.addDistribution(Grade.createGrade("17", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), B);
        IST_SCALE_20.addDistribution(Grade.createGrade("18", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), A);
        IST_SCALE_20.addDistribution(Grade.createGrade("19", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), A);
        IST_SCALE_20.addDistribution(Grade.createGrade("20", IST_SCALE_20.getGradeScale()), BigDecimal.valueOf(0), A);

        IST_SCALE_5.addDistribution(Grade.createGrade("3", IST_SCALE_5.getGradeScale()), BigDecimal.valueOf(0), C);
        IST_SCALE_5.addDistribution(Grade.createGrade("4", IST_SCALE_5.getGradeScale()), BigDecimal.valueOf(0), B);
        IST_SCALE_5.addDistribution(Grade.createGrade("5", IST_SCALE_5.getGradeScale()), BigDecimal.valueOf(0), A);

        ECTS_SCALE_20.addDistribution(Grade.createGrade("10", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(10), E);
        ECTS_SCALE_20.addDistribution(Grade.createGrade("11", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(10), E);
        ECTS_SCALE_20.addDistribution(Grade.createGrade("12", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(10), D);
        ECTS_SCALE_20.addDistribution(Grade.createGrade("13", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(20), C);
        ECTS_SCALE_20.addDistribution(Grade.createGrade("14", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(30), B);
        ECTS_SCALE_20.addDistribution(Grade.createGrade("15", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(40), B);
        ECTS_SCALE_20.addDistribution(Grade.createGrade("16", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(50), A);
        ECTS_SCALE_20.addDistribution(Grade.createGrade("17", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(60), A);
        ECTS_SCALE_20.addDistribution(Grade.createGrade("18", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(70), A);
        ECTS_SCALE_20.addDistribution(Grade.createGrade("19", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(80), A);
        ECTS_SCALE_20.addDistribution(Grade.createGrade("20", ECTS_SCALE_20.getGradeScale()), BigDecimal.valueOf(90), A);
    }

    private GradeDistribution(final GradeScale gradeScale) {
        this.gradeScale = gradeScale;
        this.gradeDistribution = new ConcurrentHashMap<Grade, Distribution>();
    }

    public GradeScale getGradeScale() {
        return gradeScale;
    }

    public Distribution addDistribution(final Grade grade, final BigDecimal percentage, final String ectsScale) {
        final Distribution result = new Distribution(grade, percentage, ectsScale);
        this.gradeDistribution.put(grade, result);
        return result;
    }

    public Distribution getDistribution(final Integer value) {
        return getDistribution(Grade.createGrade(String.valueOf(value), gradeScale));
    }

    public Distribution getDistribution(final String value) {
        return getDistribution(Grade.createGrade(value, gradeScale));
    }

    public Distribution getDistribution(final Grade grade) {
        if (grade.getGradeScale() != gradeScale) {
            throw new DomainException("GradeDistribution.invalid.grade.scale");
        }

        return gradeDistribution.get(grade);
    }

    public class Distribution {
        private Grade grade;
        private BigDecimal percentage;
        private String scale;

        public Distribution(Grade grade, BigDecimal percentage, String ectsScale) {
            this.grade = grade;
            this.percentage = percentage;
            this.scale = ectsScale;
        }

        public Grade getGrade() {
            return grade;
        }

        public BigDecimal getPercentage() {
            return percentage;
        }

        public String getScale() {
            return scale;
        }
    }

}
