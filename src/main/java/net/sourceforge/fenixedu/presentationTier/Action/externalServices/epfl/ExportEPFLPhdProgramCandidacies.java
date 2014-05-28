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
package net.sourceforge.fenixedu.presentationTier.Action.externalServices.epfl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExportEPFLPhdProgramCandidacies {

    public static byte[] run() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, Charset.defaultCharset().name());
        PrintWriter writer = new PrintWriter(outputStreamWriter);

        try {
            writer.println("<?xml version=\"1.0\" encoding=\"" + Charset.defaultCharset().name() + "\" ?>");
            writer.println("<data>");

            List<PhdIndividualProgramProcess> list =
                    PhdIndividualProgramProcess.search(ExecutionYear.readCurrentExecutionYear(),
                            new Predicate<PhdIndividualProgramProcess>() {

                                @Override
                                public boolean eval(PhdIndividualProgramProcess t) {

                                    if (t.getExecutionYear() != ExecutionYear.readCurrentExecutionYear()) {
                                        return false;
                                    }

                                    if (!PhdIndividualProgramCollaborationType.EPFL.equals(t.getCollaborationType())) {
                                        return false;
                                    }

                                    if (!PhdIndividualProgramProcessState.CANDIDACY.equals(t.getActiveState())) {
                                        return false;
                                    }

//                                    if (!t.getCandidacyProcess().getValidatedByCandidate()) {
//                                        return false;
//                                    }

                                    return true;
                                }

                            });
            for (PhdIndividualProgramProcess process : list) {
                writePersonInfo(process, writer);
            }

            writer.println("</data>");
        } finally {
            writer.close();
        }

        return outputStream.toByteArray();
    }

    /**
     * Write each person's personal and process information
     * 
     * @param process
     * @param writer
     */
    private static void writePersonInfo(PhdIndividualProgramProcess process, PrintWriter writer) {
        writer.println(addTabs(1) + "<personne action=\"AUTO\">");
        Person person = process.getPerson();
        writer.println(addTabs(2) + String.format("<nom>%s</nom>", person.getGivenNames()));
        writer.println(addTabs(2) + String.format("<prenom>%s</prenom>", person.getFamilyNames()));
        writer.println(addTabs(2) + String.format("<sexe>%s</sexe>", Gender.MALE.equals(person.getGender()) ? "SEXH" : "SEXF"));
        writer.println(addTabs(2)
                + String.format("<naissance>%s</naissance>", person.getDateOfBirthYearMonthDay().toString("dd.MM.yyyy")));

        writer.println(addTabs(2) + "<detailPersonne action=\"AUTO\">");

        writer.println(addTabs(3) + "<domaine>DOMAINEACADEMIQUE</domaine>");
        writer.println(addTabs(3)
                + String.format("<datePersonne action=\"AUTO\" type=\"TYPE_DATE_ENTREE\">%s</datePersonne>", process
                        .getCandidacyProcess().getCandidacyDate().toString("dd.MM.yyyy")));

        writer.println(addTabs(3)
                + String.format(
                        "<lieuPersonne action=\"AUTO\" type=\"LIEUNAI\" identificationLieu=\"iso\" iso=\"%s\" typeLieu=\"PAYS\">%s</lieuPersonne>",
                        person.getCountry().getCode(), person.getCountry().getCountryNationality().getContent(MultiLanguageString.en)));

        writer.println(addTabs(3)
                + String.format("<lieuPersonne action=\"AUTO\" type=\"LIEUNAIETRA\" "
                        + "typeLieu=\"LOCETRNONCON\" forceTo=\"LOCETRNONCON\">%s</lieuPersonne>",
                        person.getDistrictSubdivisionOfBirth()));

        writer.println(addTabs(3)
                + String.format("<lieuPersonne action=\"AUTO\" type=\"LIEUORI\" identificationLieu=\"iso\" iso=\"%s\" "
                        + "typeLieu=\"PAYS\">%s</lieuPersonne>", person.getCountry().getCode(), person.getCountry()
                        .getLocalizedName().getContent(MultiLanguageString.en)));

        writer.println(addTabs(3) + "<adresse type=\"ADR_ECH\" action=\"AUTO\">");

        writer.println(addTabs(4) + String.format("<ligne n=\"1\">%s</ligne>", person.getAddress()));
        writer.println(addTabs(4)
                + String.format(
                        "<localite typeLieu=\"LOCALITE;LOCETRNONCON\" identificationLieu=\"zip\" zip=\"%s\" b_returnfirst=\"1\">%s</localite>",
                        person.getAreaCode(), person.getArea()));

        if (person.getCountryOfResidence() != null) {
            writer.println(addTabs(4)
                    + String.format("<pays identificationLieu=\"iso\" iso=\"%s\" b_returnfirst=\"1\">%s</pays>", person
                            .getCountryOfResidence().getCode(),
                            person.getCountryOfResidence().getLocalizedName().getContent(MultiLanguageString.en)));
        } else {
            writer.println(addTabs(4) + "<pays identificationLieu=\"iso\" iso=\"\" b_returnfirst=\"1\"></pays>");
        }
        writer.println(addTabs(4) + String.format("<moyen action=\"AUTO\" type=\"EMAIL\">%s</moyen>", person.getEmail()));
        writer.println(addTabs(4) + String.format("<moyen action=\"AUTO\" type=\"PORTABLE\">%s</moyen>", person.getMobile()));

        writer.println(addTabs(3) + "</adresse>");

        writer.println(addTabs(2) + "</detailPersonne>");
        writer.println(addTabs(2) + "<inscription action=\"AUTO\">");

        writer.println(addTabs(3) + "<gps domaine=\"DOMAINEACADEMIQUE\">");

        writer.println(addTabs(4) + "<modelegps>CDOC</modelegps>");
        writer.println(addTabs(4) + "<unite type=\"ACAD\" format=\"LIBELLE\">IST-EPFL</unite>");
        writer.println(addTabs(4) + "<periode type=\"PEDAGO\" format=\"LIBCOU\">Eval sep</periode>");
        writer.println(addTabs(4) + "<periode type=\"ACAD\">2010</periode>");

        writer.println(addTabs(3) + "</gps>");

        writer.println(addTabs(3) + String.format(" <detail type=\"URL_IST-EPFL\">%s</detail>", getUrlForProcess(process)));
        writer.println(addTabs(3) + String.format(" <detail type=\"URL_IST-EPFL_DOCUMENTS\">%s</detail>", getUrlForProcessDocs(process)));
        final Photograph photo = person.getPersonalPhotoEvenIfPending();
        if (photo != null) {
            writer.println(addTabs(3) + String.format(" <detail type=\"URL_IST-EPFL_PHOTO\">%s</detail>", getUrlForPhoto(photo)));
        }

        if (process.getExternalPhdProgram() != null) {
            writer.println(addTabs(3)
                    + String.format("<detail type=\"PDOC_AT_EPFL\" format=\"COURTU\">%s</detail>", process.getExternalPhdProgram()
                            .getAcronym()));
        }

        writer.println(addTabs(3)
                + String.format(" <detail type=\"GPSDOMFOCUS\" conversion=\"IMPORT_IST:GPSDOMFOCUS\">%s</detail> ", process
                        .getPhdProgramFocusArea().getName()));

        writer.println(addTabs(2) + "</inscription>");

        writer.println(addTabs(1) + "</personne>");
    }

    private static String getUrlForProcess(PhdIndividualProgramProcess process) {
        return String.format("https://fenix.ist.utl.pt/phd/epfl/applications/show?process=%s", process
                .getCandidacyProcessHashCode().getValue());
    }

    private static String getUrlForProcessDocs(PhdIndividualProgramProcess process) {
        return String.format("https://fenix.ist.utl.pt/phd/epfl/applications/candidateDocuments?candidateOid=%s", process
                .getCandidacyProcessHashCode().getExternalId());
    }

    private static String getUrlForPhoto(final Photograph photo) {
        return String.format("https://fenix.ist.utl.pt/phd/epfl/applications/photo?photoOid=%s", photo.getExternalId());
    }

    private static String addTabs(int level) {
        String returnString = "";
        for (int i = 1; i <= level; i++) {
            returnString += '\t';
        }
        return returnString;
    }

}
