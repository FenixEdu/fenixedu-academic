package org.fenixedu.academic.ui.spring.controller.scientificCouncil;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLoad;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.joda.time.YearMonthDay;

public class DumpCompetenceCourseInformation {

    public static Spreadsheet dumpInformation(final DepartmentUnit departmentUnit) throws Exception {
        final String name = "CompetenceCourses";
        final Spreadsheet spreadsheet = new Spreadsheet(name);
        departmentUnit.getCompetenceCourseStream()
                .forEach(cc -> {
                    final Spreadsheet.Row row = spreadsheet.addRow();
                    row.setCell("CompetenceCoursesId", cc.getExternalId());
                    row.setCell("code", cc.getCode());
                    row.setCell("acronym", cc.getAcronym());
                    row.setCell("creationDateYearMonthDay", print(cc.getCreationDateYearMonthDay()));
                    row.setCell("curricularStage", cc.getCurricularStage().getLocalizedName());
                    row.setCell("name", cc.getName());
                    row.setCell("type", cc.getType().name());
                });
        final Spreadsheet information = spreadsheet.addSpreadsheet("Information");
        departmentUnit.getCompetenceCourseStream()
                .flatMap(cc -> cc.getCompetenceCourseInformationsSet().stream())
                .forEach(info -> {
                    final Spreadsheet.Row row = information.addRow();
                    row.setCell("CompetenceCoursesId", info.getCompetenceCourse().getExternalId());
                    row.setCell("CompetenceCoursesInformationId", info.getExternalId());
                    row.setCell("executionPeriod", info.getExecutionPeriod().getQualifiedName());
                    row.setCell("acronym", info.getAcronym());
                    row.setCell("basic", info.getBasic().toString());
                    row.setCell("competenceCourseLevel", info.getCompetenceCourseLevel().getLocalizedName());
                    row.setCell("evaluationMethod", info.getEvaluationMethod());
                    row.setCell("evaluationMethodEn", info.getEvaluationMethodEn());
                    row.setCell("name", info.getName());
                    row.setCell("nameEn", info.getNameEn());
                    row.setCell("objectives", info.getObjectives());
                    row.setCell("objectivesEn", info.getObjectivesEn());
                    row.setCell("program", info.getProgram());
                    row.setCell("programEn", info.getProgramEn());
                    row.setCell("regime", info.getRegime().getLocalizedName());
                    row.setCell("prerequisites", info.getPrerequisites());
                    row.setCell("prerequisitesEn", info.getPrerequisitesEn());
                    row.setCell("laboratorialComponent", info.getLaboratorialComponent());
                    row.setCell("laboratorialComponentEn", info.getLaboratorialComponentEn());
                    row.setCell("programmingAndComputingComponent", info.getProgrammingAndComputingComponent());
                    row.setCell("programmingAndComputingComponentEn", info.getProgrammingAndComputingComponentEn());
                    row.setCell("crossCompetenceComponent", info.getCrossCompetenceComponent());
                    row.setCell("crossCompetenceComponentEn", info.getCrossCompetenceComponentEn());
                    row.setCell("ethicalPrinciples", info.getEthicalPrinciples());
                    row.setCell("ethicalPrinciplesEn", info.getEthicalPrinciplesEn());
                });
        final Spreadsheet bibliography = information.addSpreadsheet("Bibliography");
        departmentUnit.getCompetenceCourseStream()
                .flatMap(cc -> cc.getCompetenceCourseInformationsSet().stream())
                .forEach(info -> {
                    for (final BibliographicReferences.BibliographicReference reference : info.getBibliographicReferences().getBibliographicReferencesList()) {
                        final Spreadsheet.Row row = bibliography.addRow();
                        row.setCell("CompetenceCoursesId", info.getCompetenceCourse().getExternalId());
                        row.setCell("CompetenceCoursesInformationId", info.getExternalId());
                        row.setCell("type", reference.getType().getName());
                        row.setCell("order", reference.getOrder());
                        row.setCell("title", reference.getTitle());
                        row.setCell("authors", reference.getAuthors());
                        row.setCell("reference", reference.getReference());
                        row.setCell("year", reference.getYear());
                        row.setCell("url", reference.getUrl());
                    }
                });
        final Spreadsheet loads = bibliography.addSpreadsheet("CourseLoads");
        departmentUnit.getCompetenceCourseStream()
                .flatMap(cc -> cc.getCompetenceCourseInformationsSet().stream())
                .forEach(info -> {
                    for (final CompetenceCourseLoad courseLoad : info.getCompetenceCourseLoadsSet()) {
                        final Spreadsheet.Row row = loads.addRow();
                        row.setCell("CompetenceCoursesId", info.getCompetenceCourse().getExternalId());
                        row.setCell("CompetenceCoursesInformationId", info.getExternalId());
                        row.setCell("academicPeriod", courseLoad.getAcademicPeriod().getName());
                        row.setCell("loadOrder", courseLoad.getContactLoad());
                        row.setCell("ectsCredits", courseLoad.getEctsCredits());
                        row.setCell("autonomousWorkHours", courseLoad.getAutonomousWorkHours());
                        row.setCell("theoreticalHours", courseLoad.getTheoreticalHours());
                        row.setCell("problemsHours", courseLoad.getProblemsHours());
                        row.setCell("laboratorialHours", courseLoad.getLaboratorialHours());
                        row.setCell("fieldWorkHours", courseLoad.getFieldWorkHours());
                        row.setCell("seminaryHours", courseLoad.getSeminaryHours());
                        row.setCell("trainingPeriodHours", courseLoad.getTrainingPeriodHours());
                        row.setCell("tutorialOrientationHours", courseLoad.getTutorialOrientationHours());
                    }
                });
        final Spreadsheet changeRequest = loads.addSpreadsheet("ChangeRequest");
        departmentUnit.getCompetenceCourseStream()
                .flatMap(cc -> cc.getCompetenceCourseInformationChangeRequestsSet().stream())
                .forEach(request -> {
                    final Spreadsheet.Row row = changeRequest.addRow();
                    row.setCell("CompetenceCoursesId", request.getCompetenceCourse().getExternalId());
                    row.setCell("CompetenceCoursesChangeRequestId", request.getExternalId());
                    row.setCell("executionPeriod", request.getExecutionPeriod().getQualifiedName());
                    row.setCell("approved", print(request.getApproved()));
                    row.setCell("name", request.getName());
                    row.setCell("nameEn", request.getNameEn());
                    row.setCell("regime", request.getRegime().getLocalizedName());
                    row.setCell("objectives", request.getObjectives());
                    row.setCell("objectivesEn", request.getObjectivesEn());
                    row.setCell("program", request.getProgram());
                    row.setCell("programEn", request.getProgramEn());
                    row.setCell("evaluationMethod", request.getEvaluationMethod());
                    row.setCell("evaluationMethodEn", request.getEvaluationMethodEn());
                    row.setCell("competenceCourseLevel", request.getCompetenceCourseLevel().getLocalizedName());
                    row.setCell("theoreticalHours", request.getTheoreticalHours());
                    row.setCell("problemsHours", request.getProblemsHours());
                    row.setCell("laboratorialHours", request.getLaboratorialHours());
                    row.setCell("seminaryHours", request.getSeminaryHours());
                    row.setCell("fieldWorkHours", request.getFieldWorkHours());
                    row.setCell("trainingPeriodHours", request.getTrainingPeriodHours());
                    row.setCell("tutorialOrientationHours", request.getTutorialOrientationHours());
                    row.setCell("autonomousWorkHours", request.getAutonomousWorkHours());
                    row.setCell("ectsCredits", request.getEctsCredits());
                    row.setCell("secondTheoreticalHours", request.getSecondTheoreticalHours());
                    row.setCell("secondProblemsHours", request.getSecondProblemsHours());
                    row.setCell("secondLaboratorialHours", request.getSecondLaboratorialHours());
                    row.setCell("secondSeminaryHours", request.getSecondSeminaryHours());
                    row.setCell("secondFieldWorkHours", request.getSecondFieldWorkHours());
                    row.setCell("secondTrainingPeriodHours", request.getSecondTrainingPeriodHours());
                    row.setCell("secondTutorialOrientationHours", request.getSecondTutorialOrientationHours());
                    row.setCell("secondAutonomousWorkHours", request.getSecondAutonomousWorkHours());
                    row.setCell("secondEctsCredits", request.getSecondEctsCredits());
                    row.setCell("prerequisites", request.getPrerequisites());
                    row.setCell("prerequisitesEn", request.getPrerequisitesEn());
                    row.setCell("laboratorialComponent", request.getLaboratorialComponent());
                    row.setCell("laboratorialComponentEn", request.getLaboratorialComponentEn());
                    row.setCell("programmingAndComputingComponent", request.getProgrammingAndComputingComponent());
                    row.setCell("programmingAndComputingComponentEn", request.getProgrammingAndComputingComponentEn());
                    row.setCell("crossCompetenceComponent", request.getCrossCompetenceComponent());
                    row.setCell("crossCompetenceComponentEn", request.getCrossCompetenceComponentEn());
                    row.setCell("ethicalPrinciples", request.getEthicalPrinciples());
                    row.setCell("ethicalPrinciplesEn", request.getEthicalPrinciplesEn());
                    row.setCell("justification", request.getJustification());
                });
        final Spreadsheet bibliographyChanges = changeRequest.addSpreadsheet("Bibliography Changes");
        departmentUnit.getCompetenceCourseStream()
                .flatMap(cc -> cc.getCompetenceCourseInformationChangeRequestsSet().stream())
                .forEach(request -> {
                    for (final BibliographicReferences.BibliographicReference reference : request.getBibliographicReferences().getBibliographicReferencesList()) {
                        final Spreadsheet.Row row = bibliographyChanges.addRow();
                        row.setCell("CompetenceCoursesId", request.getCompetenceCourse().getExternalId());
                        row.setCell("CompetenceCoursesChangeRequestId", request.getExternalId());
                        row.setCell("type", reference.getType().getName());
                        row.setCell("order", reference.getOrder());
                        row.setCell("title", reference.getTitle());
                        row.setCell("authors", reference.getAuthors());
                        row.setCell("reference", reference.getReference());
                        row.setCell("year", reference.getYear());
                        row.setCell("url", reference.getUrl());
                    }
                });
        return spreadsheet;
    }

    private static String print(final Boolean b) {
        return b == null ? " " : b.toString();
    }

    private static String print(YearMonthDay yearMonthDay) {
        return yearMonthDay == null ? " " : yearMonthDay.toString("yyyy-MM-dd");
    }

}
