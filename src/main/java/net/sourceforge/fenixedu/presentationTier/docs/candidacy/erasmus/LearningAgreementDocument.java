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
package net.sourceforge.fenixedu.presentationTier.docs.candidacy.erasmus;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.FenixStringTools;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class LearningAgreementDocument extends FenixReport {

    MobilityIndividualApplicationProcess process;

    static final protected char END_CHAR = ' ';
    static final protected int LINE_LENGTH = 70;
    static final protected String LINE_BREAK = "\n";

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

        for (CurricularCourse course : process.getCandidacy().getCurricularCourses()) {
            result.append(
                    FenixStringTools.multipleLineRightPadWithSuffix(course.getNameI18N().getContent(MultiLanguageString.en)
                            + " (" + course.getDegree().getSigla() + ")", LINE_LENGTH, END_CHAR, course.getEctsCredits()
                            .toString())).append(LINE_BREAK);
        }

        return result.toString();
    }

    @Override
    public String getReportFileName() {
        return "learning_agreement_" + process.getCandidacy().getPersonalDetails().getDocumentIdNumber();
    }

}
