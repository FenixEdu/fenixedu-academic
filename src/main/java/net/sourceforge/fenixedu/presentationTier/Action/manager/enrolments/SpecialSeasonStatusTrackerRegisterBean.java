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
package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import java.io.Serializable;
import java.util.Comparator;

public class SpecialSeasonStatusTrackerRegisterBean implements Serializable {

    private static final long serialVersionUID = -7064883960750178974L;

    public static final Comparator<SpecialSeasonStatusTrackerRegisterBean> COMPARATOR_STUDENT_NUMBER =
            new Comparator<SpecialSeasonStatusTrackerRegisterBean>() {

                @Override
                public int compare(final SpecialSeasonStatusTrackerRegisterBean es1,
                        final SpecialSeasonStatusTrackerRegisterBean es2) {
                    final Integer studentNumber1 = es1.getStudentNumber();
                    final Integer studentNumber2 = es2.getStudentNumber();
                    return studentNumber1.compareTo(studentNumber2);
                }

            };

    public static final Comparator<SpecialSeasonStatusTrackerRegisterBean> COMPARATOR_STUDENT_NAME =
            new Comparator<SpecialSeasonStatusTrackerRegisterBean>() {

                @Override
                public int compare(final SpecialSeasonStatusTrackerRegisterBean es1,
                        final SpecialSeasonStatusTrackerRegisterBean es2) {
                    final String studentName1 = es1.getStudentName();
                    final String studentName2 = es2.getStudentName();
                    final int c = studentName1.compareTo(studentName2);
                    return c == 0 ? COMPARATOR_STUDENT_NUMBER.compare(es1, es2) : c;
                }

            };

    public static final Comparator<SpecialSeasonStatusTrackerRegisterBean> COMPARATOR_DEGREE =
            new Comparator<SpecialSeasonStatusTrackerRegisterBean>() {

                @Override
                public int compare(final SpecialSeasonStatusTrackerRegisterBean es1,
                        final SpecialSeasonStatusTrackerRegisterBean es2) {
                    final String degree1 = es1.getDegreeSigla();
                    final String degree2 = es2.getDegreeSigla();
                    final int c = degree1.compareTo(degree2);
                    return c == 0 ? COMPARATOR_STUDENT_NUMBER.compare(es1, es2) : c;
                }

            };

    public static final Comparator<SpecialSeasonStatusTrackerRegisterBean> COMPARATOR_COURSE =
            new Comparator<SpecialSeasonStatusTrackerRegisterBean>() {

                @Override
                public int compare(final SpecialSeasonStatusTrackerRegisterBean es1,
                        final SpecialSeasonStatusTrackerRegisterBean es2) {
                    final String course1 = es1.getCourseName();
                    final String course2 = es2.getCourseName();
                    final int c = course1.compareTo(course2);
                    return c == 0 ? COMPARATOR_STUDENT_NUMBER.compare(es1, es2) : c;
                }

            };

    private Integer studentNumber;
    private String studentName;
    private String degreeSigla;
    private String courseName;

    public SpecialSeasonStatusTrackerRegisterBean(Integer studentNumber, String studentName, String degreeSigla, String courseName) {
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.degreeSigla = degreeSigla;
        this.courseName = courseName;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getDegreeSigla() {
        return degreeSigla;
    }

    public String getCourseName() {
        return courseName;
    }
}
