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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.ThesisSubjectOrder;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Partial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExportPhdIndividualProgramProcessesInHtml {

    private static final Logger logger = LoggerFactory.getLogger(ExportPhdIndividualProgramProcessesInHtml.class);

    // TODO: IST-<Collaboration>: collaboration must be added as argument
    static final private String APPLICATION_NAME = "Application to FCT Doctoral Programmes";
    static final private String APPLICATION_PREFIX_LINK = "";

    static byte[] exportPresentationPage() throws IOException {
        List<PhdProgramPublicCandidacyHashCode> unfocusAreaCandidates = new ArrayList<PhdProgramPublicCandidacyHashCode>();

        final Page page = new Page();
        page.h2(APPLICATION_NAME);

        for (final Entry<PhdProgramFocusArea, Set<PhdProgramPublicCandidacyHashCode>> entry : getApplicants(unfocusAreaCandidates)
                .entrySet()) {
            page.h(3, getFocusAreaTitle(entry), "mtop2");

            page.ulStart();
            for (final PhdProgramPublicCandidacyHashCode code : entry.getValue()) {
                final String url = APPLICATION_PREFIX_LINK + "/phd/epfl/applications/show?process=" + code.getValue();
                page.liStart().link(url, code.getPerson().getName()).liEnd();
            }
            page.ulEnd();
        }

        page.close();

        return page.toByteArray();
    }

    private static String getFocusAreaTitle(final Entry<PhdProgramFocusArea, Set<PhdProgramPublicCandidacyHashCode>> entry) {
        return entry.getKey().getName().getContent() + " (" + entry.getValue().size() + " applications)";
    }

    private static Map<PhdProgramFocusArea, Set<PhdProgramPublicCandidacyHashCode>> getApplicants(
            final List<PhdProgramPublicCandidacyHashCode> unfocusAreaCandidates) {
        final Map<PhdProgramFocusArea, Set<PhdProgramPublicCandidacyHashCode>> candidates =
                new TreeMap<PhdProgramFocusArea, Set<PhdProgramPublicCandidacyHashCode>>(PhdProgramFocusArea.COMPARATOR_BY_NAME);

        for (final PublicCandidacyHashCode hashCode : Bennu.getInstance().getCandidacyHashCodesSet()) {
            if (hashCode.isFromPhdProgram() && hashCode.hasCandidacyProcess()) {

                final PhdProgramPublicCandidacyHashCode phdHashCode = (PhdProgramPublicCandidacyHashCode) hashCode;

                if (phdHashCode.getIndividualProgramProcess().getExecutionYear() != ExecutionYear.readCurrentExecutionYear()) {
                    continue;
                }

                if (!PhdIndividualProgramCollaborationType.EPFL.equals(phdHashCode.getIndividualProgramProcess()
                        .getCollaborationType())) {
                    continue;
                }

                if (!PhdIndividualProgramProcessState.CANDIDACY
                        .equals(phdHashCode.getIndividualProgramProcess().getActiveState())) {
                    continue;
                }

//                if (phdHashCode.getPhdProgramCandidacyProcess().isValidatedByCandidate()) {
                    addCandidate(unfocusAreaCandidates, candidates, phdHashCode);
//                }
            }
        }
        return candidates;
    }

    private static void addCandidate(final List<PhdProgramPublicCandidacyHashCode> unfocusAreaCandidates,
            final Map<PhdProgramFocusArea, Set<PhdProgramPublicCandidacyHashCode>> candidates,
            final PhdProgramPublicCandidacyHashCode hashCode) {

        final PhdProgramFocusArea focusArea = hashCode.getIndividualProgramProcess().getPhdProgramFocusArea();

        if (focusArea == null) {
            unfocusAreaCandidates.add(hashCode);
            return;
        }

        if (!candidates.containsKey(focusArea)) {
            candidates.put(focusArea, new TreeSet<PhdProgramPublicCandidacyHashCode>(
                    new Comparator<PhdProgramPublicCandidacyHashCode>() {

                        @Override
                        public int compare(PhdProgramPublicCandidacyHashCode o1, PhdProgramPublicCandidacyHashCode o2) {
                            return o1.getPerson().getName().compareTo(o2.getPerson().getName());
                        }
                    }));
        }

        candidates.get(focusArea).add(hashCode);
    }

    static byte[] drawCandidatePage(final PhdProgramPublicCandidacyHashCode hashCode) throws IOException {
        final String email = hashCode.getEmail().substring(0, hashCode.getEmail().indexOf("@"));
        final Page page = new Page();

        page.h2(APPLICATION_NAME);
        drawPersonalInformation(page, hashCode, email);
        drawPhdIndividualProgramInformation(page, hashCode);
        drawGuidings(page, hashCode);
        drawQualifications(page, hashCode);
        drawCandidacyReferees(page, hashCode, email);
        drawDocuments(page, hashCode, email);
        drawThesisRanking(page, hashCode, email);

        page.close();
        return page.toByteArray();
    }

    private static void drawPersonalInformation(final Page page, final PhdProgramPublicCandidacyHashCode hashCode,
            final String folderName) throws IOException {

        final Person person = hashCode.getPerson();

        page.h(3, "Personal Information", "mtop2");
        page.tableStart("tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin ");

        page.rowStart("tdbold").headerStartWithStyle("width: 125px;").write("Name:").headerEnd().column(person.getName())
                .rowEnd();
        page.rowStart().header("Gender:").column(person.getGender().toLocalizedString(Locale.ENGLISH)).rowEnd();
        page.rowStart().header("Identity card type:").column(person.getIdDocumentType().getLocalizedName()).rowEnd();
        page.rowStart().header("Identity card #:").column(person.getDocumentIdNumber()).rowEnd();
        page.rowStart().header("Issued by:").column(person.getEmissionLocationOfDocumentId()).rowEnd();
        page.rowStart().header("Fiscal number:").column(string(person.getSocialSecurityNumber())).rowEnd();
        page.rowStart().header("Date of birth:").column(person.getDateOfBirthYearMonthDay().toString("dd/MM/yyyy")).rowEnd();
        page.rowStart().header("Birthplace:").column(person.getDistrictSubdivisionOfBirth()).rowEnd();
        page.rowStart().header("Nationality:").column(person.getCountry().getCountryNationality().getContent()).rowEnd();
        page.rowStart().header("Address:").column(person.getAddress()).rowEnd();
        page.rowStart().header("City:").column(person.getArea()).rowEnd();
        page.rowStart().header("Zip code:").column(person.getAreaCode()).rowEnd();
        page.rowStart().header("Country:")
                .column((person.getCountryOfResidence() != null ? person.getCountryOfResidence().getName() : "-")).rowEnd();
        page.rowStart().header("Phone:").column(person.getDefaultPhoneNumber()).rowEnd();
        page.rowStart().header("Mobile:").column(person.getDefaultMobilePhoneNumber()).rowEnd();
        page.rowStart().header("Email:").column(person.getDefaultEmailAddressValue()).rowEnd();

        page.tableEnd();

        page.h(3, "Photo");
        String photoUrl = APPLICATION_PREFIX_LINK + "/phd/epfl/applications/photo";
        final Photograph photo = person.getPersonalPhotoEvenIfPending();
        if (photo != null) {
            photoUrl += "?photoOid=" + photo.getExternalId();
        }
        page.photo(photoUrl);
    }

    private static void drawPhdIndividualProgramInformation(final Page page, final PhdProgramPublicCandidacyHashCode hashCode)
            throws IOException {
        final PhdIndividualProgramProcess process = hashCode.getIndividualProgramProcess();

        page.h(3, "Application information");
        page.tableStart("tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin ");
        page.rowStart().headerStartWithStyle("width: 125px;").write("Candidacy Date:").headerEnd()
                .column(process.getCandidacyDate().toString("dd/MM/yyyy")).rowEnd();
        page.rowStart().header("Area:").column(process.getPhdProgramFocusArea().getName().getContent()).rowEnd();
        page.rowStart().header(Unit.getInstitutionAcronym() + " Phd Program:")
                .column(process.getPhdProgram().getName().getContent(MultiLanguageString.en)).rowEnd();
        if (process.getExternalPhdProgram() != null) {
            page.rowStart().header("EPFL Phd Program:").column(process.getExternalPhdProgram().getName().getContent(MultiLanguageString.en));
        }
        page.rowStart().header("Title:").column(string(process.getThesisTitle())).rowEnd();
        page.rowStart().header("Collaboration:").column(process.getCollaborationTypeName()).rowEnd();
        page.rowStart().header("Year:").column(process.getExecutionYear().getYear()).rowEnd();
        page.tableEnd();
    }

    private static void drawDocuments(final Page page, final PhdProgramPublicCandidacyHashCode hashCode, final String folderName)
            throws IOException {

        page.h(3, "Documents", "mtop2");

        final PhdIndividualProgramProcess process = hashCode.getIndividualProgramProcess();
        if (!process.getCandidacyProcessDocuments().isEmpty()) {

            final String documentName = folderName + "-documents.zip";
            final String url =
                    APPLICATION_PREFIX_LINK + "/phd/epfl/applications/candidateDocuments?candidateOid="
                            + hashCode.getExternalId();
            page.pStart("mbottom0").link(url, documentName).pEnd();

            page.tableStart("tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin ");
            page.rowStart().header("Document type").header("Upload time").header("Filename").rowEnd();

            for (final PhdProgramProcessDocument document : process.getCandidacyProcessDocuments()) {
                page.rowStart().column(document.getDocumentType().getLocalizedName());
                page.column(document.getUploadTime().toString("dd/MM/yyyy HH:mm"));
                page.column(document.getFilename()).rowEnd();
            }

            page.tableEnd();
        }
    }

    private static void drawThesisRanking(Page page, PhdProgramPublicCandidacyHashCode hashCode, String email) throws IOException {

        page.h(3, "Thesis Rank", "mtop2");
        final PhdIndividualProgramProcess process = hashCode.getIndividualProgramProcess();

        page.tableStart("tstyle2");

        page.rowStart();
        page.header("Rank");
        page.header("Name");
        page.header("Teacher");
        page.header("Description");
        page.rowEnd();

        Collection<ThesisSubjectOrder> thesisSubjectOrders = process.getThesisSubjectOrdersSorted();

        for (ThesisSubjectOrder thesisSubjectOrder : thesisSubjectOrders) {
            page.rowStart();
            page.column(thesisSubjectOrder.getSubjectOrder().toString());
            page.column(thesisSubjectOrder.getThesisSubject().getName().getContent());
            page.column(thesisSubjectOrder.getThesisSubject().hasTeacher() ? thesisSubjectOrder.getThesisSubject().getTeacher()
                    .getPerson().getName() : "");
            page.rowEnd();
        }

        page.tableEnd();
    }

    static byte[] createZip(final PhdProgramPublicCandidacyHashCode hashCode) {

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = null;
        try {
            zip = new ZipOutputStream(outputStream);

            int count = 1;
            for (final PhdProgramProcessDocument document : hashCode.getIndividualProgramProcess().getCandidacyProcessDocuments()) {
                final ZipEntry zipEntry = new ZipEntry(count + "-" + document.getFilename());
                zip.putNextEntry(zipEntry);

                // TODO: use in local context copy(new ByteArrayInputStream(new
                // byte[20]), zip);
                copy(document.getStream(), zip);

                zip.closeEntry();
                count++;
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (zip != null) {
                try {
                    zip.flush();
                    zip.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return outputStream.toByteArray();
    }

    private static void drawCandidacyReferees(final Page page, final PhdProgramPublicCandidacyHashCode hashCode,
            final String folderName) throws IOException {
        final PhdIndividualProgramProcess process = hashCode.getIndividualProgramProcess();
        page.h(3, "Reference letters (referees)", "mtop2");

        if (!process.getPhdCandidacyReferees().isEmpty()) {
            int count = 1;
            for (final PhdCandidacyReferee referee : process.getPhdCandidacyReferees()) {
                page.pStart("mbottom0").strong(String.valueOf(count) + ". ").pEnd();
                drawReferee(page, referee, count, folderName);
                count++;
            }
        } else {
            page.pStart().write("Not defined").pEnd();
        }
    }

    // "displayRefereePage"
    private static void drawReferee(final Page page, final PhdCandidacyReferee referee, final int count, final String folderName)
            throws IOException {

        page.tableStart("tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin ");
        page.rowStart().headerStartWithStyle("width: 125px;").write("Name:").headerEnd().column(referee.getName()).rowEnd();
        page.rowStart().header("Email:").column(referee.getEmail()).rowEnd();
        page.rowStart().header("Institution:").column(referee.getInstitution()).rowEnd();

        if (referee.isLetterAvailable()) {
            page.rowStart().header("Referee form submitted:");
            final String url =
                    APPLICATION_PREFIX_LINK + "/phd/epfl/applications/referee?refereeOid=" + referee.getExternalId()
                            + "&amp;count=" + count;
            page.columnStart().link(url, "Yes").columnEnd().rowEnd();
        } else {
            page.rowStart().header("Referee form submitted:").column("No").rowEnd();
        }

        page.tableEnd();
    }

    static byte[] drawLetter(final PhdCandidacyReferee referee, final int count) throws IOException {
        final Page page = new Page();

        page.h2(APPLICATION_NAME);
        page.h(3, "Applicant", "mtop2");

        candidateInformation(referee, page);
        letterInformation(referee, page);

        page.close();
        return page.toByteArray();
    }

    private static void letterInformation(final PhdCandidacyReferee referee, final Page page) throws IOException {

        final PhdCandidacyRefereeLetter letter = referee.getLetter();

        page.h(3, "Reference Letter", "mtop2");
        page.tableStart("tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin");

        page.rowStart().headerStartWithStyle("width: 200px;").write("How long have you known the applicant?").headerEnd()
                .column(string(letter.getHowLongKnownApplicant()) + " months").rowEnd();
        page.rowStart().header("In what capacity?").column(string(letter.getCapacity())).rowEnd();
        page.rowStart().header("Comparison group:").column(string(letter.getComparisonGroup())).rowEnd();
        page.rowStart().header("Rank in class (if applicable):").column(string(letter.getRankInClass())).rowEnd();
        page.rowStart().header("Academic performance:").column(string(letter.getAcademicPerformance().getLocalizedName()))
                .rowEnd();
        page.rowStart().header("Social and Communication Skills:")
                .column(string(letter.getSocialAndCommunicationSkills().getLocalizedName())).rowEnd();
        page.rowStart().header("Potential to excel in a PhD:").column(string(letter.getPotencialToExcelPhd().getLocalizedName()))
                .rowEnd();

        page.rowStart().header("Recomendation letter:");
        if (letter.hasFile()) {
            page.column(letter.getFile().getDisplayName() + " (file is inside documents zip file)");
        } else {
            page.column("-");
        }
        page.rowEnd();

        page.rowStart().header("Comments:").column(string(letter.getComments())).rowEnd();
        page.rowStart().header("Name:").column(string(letter.getRefereeName())).rowEnd();
        page.rowStart().header("Position/Title:").column(string(letter.getRefereePosition())).rowEnd();
        page.rowStart().header("Institution:").column(string(letter.getRefereeInstitution())).rowEnd();
        page.rowStart().header("Address:").column(string(letter.getRefereeInstitution())).rowEnd();
        page.rowStart().header("City:").column(string(letter.getRefereeCity())).rowEnd();
        page.rowStart().header("Zip code:").column(string(letter.getRefereeZipCode())).rowEnd();
        page.rowStart().header("Country:")
                .column(letter.getRefereeCountry() != null ? letter.getRefereeCountry().getLocalizedName().getContent() : "-")
                .rowEnd();
        page.rowStart().header("Email:").column(string(letter.getRefereeEmail())).rowEnd();

        page.tableEnd();
    }

    private static void candidateInformation(final PhdCandidacyReferee referee, final Page page) throws IOException {
        page.tableStart("tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin ");
        page.rowStart("tdbold").headerStartWithStyle("width: 200px;").write("Name: ").headerEnd()
                .column(referee.getPhdProgramCandidacyProcess().getPerson().getName()).rowEnd();
        page.tableEnd();
    }

    private static void drawQualifications(final Page page, final PhdProgramPublicCandidacyHashCode hashCode) throws IOException {
        final PhdIndividualProgramProcess process = hashCode.getIndividualProgramProcess();
        page.h(3, "Academic Degrees", "mtop2");

        if (!process.getQualifications().isEmpty()) {
            int count = 1;
            for (final Qualification qualification : process.getQualifications()) {
                page.pStart("mbottom0").strong(String.valueOf(count) + ". ").pEnd();
                drawQualification(page, qualification);
                count++;
            }
        } else {
            page.pStart().write("Not defined").pEnd();
        }
    }

    private static void drawQualification(final Page page, final Qualification qualification) throws IOException {
        page.tableStart("tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin ");
        if (qualification != null) {
            final QualificationType type = qualification.getType();
            page.rowStart().header("Type:").column(type == null ? "-" : type.getLocalizedName()).rowEnd();
            final String degree = qualification.getDegree();
            page.rowStart().header("Scientific Field:").column(degree == null ? "-" : degree).rowEnd();
            final String school = qualification.getSchool();
            page.rowStart().header("Institution:").column(school == null ? "-" : school).rowEnd();
            final String mark = qualification.getMark();
            page.rowStart().header("Grade:").column(mark == null ? "-" : mark).rowEnd();
            final Partial attendedBegin = qualification.getAttendedBegin();
            page.rowStart().header("Attended from:").column(attendedBegin == null ? "-" : attendedBegin.toString("MM/yyyy"))
                    .rowEnd();
            final Partial attendedEnd = qualification.getAttendedEnd();
            page.rowStart().header("Attended to:").column(attendedEnd == null ? "-" : attendedEnd.toString("MM/yyyy")).rowEnd();
        }
        page.tableEnd();
    }

    private static void drawGuidings(final Page page, final PhdProgramPublicCandidacyHashCode hashCode) throws IOException {
        final PhdIndividualProgramProcess process = hashCode.getIndividualProgramProcess();
        page.h(3, "Phd supervisors (if applicable)", "mtop2");

        if (process.hasAnyGuidings()) {
            int count = 1;
            for (final PhdParticipant guiding : process.getGuidings()) {
                page.pStart("mbottom0").strong(String.valueOf(count) + ". ").pEnd();
                drawGuiding(page, guiding);
                count++;
            }
        } else {
            page.pStart().write("Not defined").pEnd();
        }
    }

    private static void drawGuiding(final Page page, final PhdParticipant guiding) throws IOException {
        page.tableStart("tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin ");
        page.rowStart().headerStartWithStyle("width: 125px;").write("Name:").headerEnd().column(guiding.getName()).rowEnd();
        page.rowStart().header("Affiliation:").column(guiding.getWorkLocation()).rowEnd();
        page.rowStart().header("Email:").column(guiding.getEmail()).rowEnd();
        page.tableEnd();
    }

    private static String string(final String value) {
        return (value != null) ? value : StringUtils.EMPTY;
    }

    /*
     * do not close output stream
     */
    private static void copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        int BUFFER_SIZE = 1024 * 1024;
        try {
            final byte[] buffer = new byte[BUFFER_SIZE];
            for (int numberOfBytesRead; (numberOfBytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1; outputStream.write(
                    buffer, 0, numberOfBytesRead)) {
                ;
            }
        } finally {
            inputStream.close();
        }

    }

    static private class Page {
        private ByteArrayOutputStream writer = new ByteArrayOutputStream();

        Page() throws IOException {
            write("<xhtml>");
            write("<head>");
            css(APPLICATION_PREFIX_LINK + "/CSS/iststyle.css");
            css(APPLICATION_PREFIX_LINK + "/CSS/webservice.css");
            write("</head>");
            write("<body>");
        }

        public byte[] toByteArray() throws IOException {
            write("</body>");
            write("</xhtml>");
            return writer.toByteArray();
        }

        public Page css(final String url) throws IOException {
            return write(String.format("<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\"  href=\"%s\" />", url));
        }

        public Page h2(final String body) throws IOException {
            return h(2, body);
        }

        public Page h(final int level, final String body) throws IOException {
            return startTag("h" + level).write(body).endTag("h" + level);
        }

        public Page h(final int level, final String body, final String classes) throws IOException {
            return write(String.format("<h%s class=\"%s\">", level, classes)).write(body).endTag("h" + level);
        }

        public Page link(final String path, final String name) throws IOException {
            return write(String.format("<a href='%s'>%s</a>", path, name));
        }

        public Page strong(final String body) throws IOException {
            return startTag("strong").write(body).endTag("strong");
        }

        public Page tableStart(String classes) throws IOException {
            return write(String.format("<table class=\"%s\">", classes));
        }

        public Page rowStart() throws IOException {
            return startTag("tr");
        }

        public Page rowStart(final String classes) throws IOException {
            return write(String.format("<tr class=\"%s\"", classes));
        }

        public Page rowEnd() throws IOException {
            return endTag("tr");
        }

        public Page headerStartWithStyle(final String style) throws IOException {
            return write(String.format("<th style=\"%s\">", style));
        }

        public Page headerEnd() throws IOException {
            return endTag("th");
        }

        public Page header(final String body) throws IOException {
            return write(String.format("<th>%s</th>", body));
        }

        public Page columnStart() throws IOException {
            return startTag("td");
        }

        public Page columnEnd() throws IOException {
            return endTag("td");
        }

        public Page column(final String body) throws IOException {
            return write(String.format("<td>%s</td>", body));
        }

        public Page tableEnd() throws IOException {
            return endTag("table");
        }

        public Page ulStart() throws IOException {
            return startTag("ul");
        }

        public Page ulEnd() throws IOException {
            return endTag("ul");
        }

        public Page liStart() throws IOException {
            return startTag("li");
        }

        public Page liEnd() throws IOException {
            return endTag("li");
        }

        public Page pStart(final String classes) throws IOException {
            return write(String.format("<p class=\"%s\">", classes));
        }

        public Page pStart() throws IOException {
            return startTag("p");
        }

        public Page pEnd() throws IOException {
            return endTag("p");
        }

        public Page photo(final String photoPath) throws IOException {
            return write(String.format("<img src=\"%s\" />", photoPath));
        }

        private Page startTag(final String tagName) throws IOException {
            return write("<" + tagName + ">");
        }

        private Page endTag(final String tagName) throws IOException {
            return write("</" + tagName + ">");
        }

        public Page write(final String value) throws IOException {
            writer.write(value.getBytes(Charset.defaultCharset().name()));
            writer.write("\n".getBytes());
            return this;
        }

        public void close() throws IOException {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

}
