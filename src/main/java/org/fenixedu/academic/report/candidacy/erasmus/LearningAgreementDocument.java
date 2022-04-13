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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.report.FenixReport;

import java.util.Locale;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class LearningAgreementDocument extends FenixReport {

    MobilityIndividualApplicationProcess process;

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
        getPayload().addProperty("mobilityProgram", process.getMobilityProgram().getName()
                .getContent(org.fenixedu.academic.util.LocaleUtils.EN));
        getPayload().addProperty("academicYear", process.getCandidacyExecutionInterval().getName());
        getPayload().addProperty("studentName", process.getPersonalDetails().getName());
        getPayload().addProperty("sendingInstitution", process.getCandidacy().getMobilityStudentData().getSelectedOpening()
                .getMobilityAgreement().getUniversityUnit().getNameI18n().getContent());

        getPayload().add("courses", getChosenSubjectsInformation());
    }

    private JsonArray getChosenSubjectsInformation() {
        JsonArray result = new JsonArray();

        process.getCandidacy().getCurricularCoursesSet().stream().map(course -> {
            JsonObject courseInfo = new JsonObject();
            YearSemesterInfo yearAndSemester = getYearAndSemester(course);

            courseInfo.addProperty("title", course.getNameI18N()
                    .getContent(org.fenixedu.academic.util.LocaleUtils.EN));
            courseInfo.addProperty("degree", course.getDegree().getSigla());
            courseInfo.addProperty("year", yearAndSemester.getYear());
            courseInfo.addProperty("semester", yearAndSemester.getSemester());
            courseInfo.addProperty("ects", course.getEctsCredits().toString());

            return courseInfo;
        }).forEach(result::add);

        return result;
    }

    private static class YearSemesterInfo {
        private final String year;
        private final String semester;

        public YearSemesterInfo(String year, String semester) {
            this.year = year;
            this.semester = semester;
        }

        public String getYear() {
            return year;
        }

        public String getSemester() {
            return semester;
        }
    }

    private YearSemesterInfo getYearAndSemester(CurricularCourse course) {
        String year = "";
        String semester = "";

        if (course.getParentContextsSet().size() > 1
                && ((MobilityApplicationProcess) process.getCandidacyProcess()).getForSemester().equals(
                        ErasmusApplyForSemesterType.SECOND_SEMESTER)) {
            TreeSet<Integer> years = course.getParentContextsSet().stream().map(Context::getCurricularYear)
                    .collect(Collectors.toCollection(TreeSet::new));

            if (years.size() == 1) {
                year = years.iterator().next().toString();
            }

            semester = "2";
        } else if (course.getParentContextsSet().size() == 1) {
            Context context = course.getParentContextsSet().iterator().next();
            year = context.getCurricularYear().toString();
            semester = (context.containsSemester(1) ? "1" : "2");
        } else {
            TreeSet<Integer> years = course.getParentContextsSet().stream().map(Context::getCurricularYear)
                    .collect(Collectors.toCollection(TreeSet::new));
            TreeSet<Integer> semesters = course.getParentContextsSet().stream().map(c -> c.containsSemester(1) ? 1 : 2)
                    .collect(Collectors.toCollection(TreeSet::new));

            if (years.size() == 1) {
                year = years.iterator().next().toString();
            }
            if (semesters.size() == 1) {
                semester = semesters.iterator().next().toString();
            }
        }

        return new YearSemesterInfo(year, semester);
    }

    @Override
    public String getReportFileName() {
        return "learning_agreement_" + process.getCandidacy().getPersonalDetails().getDocumentIdNumber();
    }

}
