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
package org.fenixedu.academic.report.candidacy.erasmus;

import java.util.Locale;
import java.util.TreeSet;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.academic.util.FenixStringTools;
import org.fenixedu.academic.util.MultiLanguageString;
import org.jsoup.helper.StringUtil;

public class LearningAgreementDocument extends FenixReport {

    MobilityIndividualApplicationProcess process;

    static final protected char END_CHAR = ' ';
    static final protected int LINE_LENGTH = 70;
    static final protected String LINE_BREAK = "\n";
    static final protected String YEAR_TEXT = "Y.";
    static final protected String SEMESTER_TEXT = "Sem.";

    public LearningAgreementDocument(MobilityIndividualApplicationProcess process) {
        this.process = process;
        fillReport();
    }

    public LearningAgreementDocument(MobilityIndividualApplicationProcess process, Locale locale) {
        super(locale);
        this.process = process;
        fillReport();
    }

    @Override
    protected void fillReport() {
        addParameter("mobilityProgram", process.getMobilityProgram().getName().getContent(MultiLanguageString.en));
        addParameter("academicYear", process.getCandidacyExecutionInterval().getName());
        addParameter("studentName", process.getPersonalDetails().getName());
        addParameter("sendingInstitution", process.getCandidacy().getMobilityStudentData().getSelectedOpening()
                .getMobilityAgreement().getUniversityUnit().getNameI18n().getContent());

        addParameter("desiredEnrollments", getChosenSubjectsInformation());
    }

    private String getChosenSubjectsInformation() {
        StringBuilder result = new StringBuilder();

        for (CurricularCourse course : process.getCandidacy().getCurricularCoursesSet()) {
            String yearAndSemester = buildYearAndSemester(course);

            result.append(
                    FenixStringTools.multipleLineRightPadWithSuffix(course.getNameI18N().getContent(MultiLanguageString.en)
                            + " (" + course.getDegree().getSigla() + ")" + yearAndSemester, LINE_LENGTH, END_CHAR, course
                            .getEctsCredits().toString())).append(LINE_BREAK);
        }

        return result.toString();
    }

    private String buildYearAndSemester(CurricularCourse course) {
        String year = "";
        String semester = "";

        if (course.getParentContextsSet().size() > 1
                && ((MobilityApplicationProcess) process.getCandidacyProcess()).getForSemester().equals(
                        ErasmusApplyForSemesterType.SECOND_SEMESTER)) {
            TreeSet<Integer> years = new TreeSet<Integer>();
            for (Context context : course.getParentContextsSet()) {
                years.add(context.getCurricularYear());
            }
            if (years.size() == 1) {
                year = YEAR_TEXT + years.iterator().next();
            }

            semester = SEMESTER_TEXT + "2";
        } else if (course.getParentContextsSet().size() == 1) {
            Context context = course.getParentContextsSet().iterator().next();
            year = YEAR_TEXT + context.getCurricularYear();
            semester = SEMESTER_TEXT + (context.containsSemester(1) ? "1" : "2");
        } else {
            TreeSet<Integer> years = new TreeSet<Integer>();
            TreeSet<Integer> semesters = new TreeSet<Integer>();
            for (Context context : course.getParentContextsSet()) {
                years.add(context.getCurricularYear());
                semesters.add(context.containsSemester(1) ? 1 : 2);
            }
            if (years.size() == 1) {
                year = YEAR_TEXT + years.iterator().next();
            }
            if (semesters.size() == 1) {
                semester = SEMESTER_TEXT + semesters.iterator().next();
            }
        }

        String yearAndSemester = year + (StringUtil.isBlank(semester) || StringUtil.isBlank(year) ? "" : " - ") + semester;
        yearAndSemester = (StringUtil.isBlank(yearAndSemester) ? "" : " - ") + yearAndSemester;
        return yearAndSemester;
    }

    @Override
    public String getReportFileName() {
        return "learning_agreement_" + process.getCandidacy().getPersonalDetails().getDocumentIdNumber();
    }

}
