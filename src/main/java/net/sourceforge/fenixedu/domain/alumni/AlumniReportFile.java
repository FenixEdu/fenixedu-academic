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
package net.sourceforge.fenixedu.domain.alumni;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class AlumniReportFile extends AlumniReportFile_Base {

    private static final Logger logger = LoggerFactory.getLogger(AlumniReportFile.class);

    private final static String NOT_AVAILABLE = "n/a";
    private final static String DATE_FORMAT = "dd/MM/yyyy";

    private final static Locale PT = new Locale("pt", "PT");

    private AlumniReportFile() {
        super();
        setExecutionYear(ExecutionYear.readCurrentExecutionYear());
    }

    private AlumniReportFile(boolean fullReport, boolean onlyRegisteredAlumni) {
        this();

        setFullReport(fullReport);
        setOnlyRegisteredAlumni(onlyRegisteredAlumni);

        if ("fullReport" == null || "fullReport".isEmpty()) {
            throw new DomainException("error.domain.alumni.alumni.report.file.full.report.is.null", null);
        }
        if ("onlyRegisteredAlumni" == null || "onlyRegisteredAlumni".isEmpty()) {
            throw new DomainException("error.domain.alumni.alumni.report.file.only.alumni.is.null", null);
        }
    }

    @Override
    public QueueJobResult execute() throws Exception {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

        Spreadsheet.exportToXLSSheets(byteArrayOS, buildReport());

        final QueueJobResult queueJobResult = new QueueJobResult();
        queueJobResult.setContentType("application/vnd.ms-excel");
        queueJobResult.setContent(byteArrayOS.toByteArray());

        logger.info("Job " + getFilename() + " completed");

        return queueJobResult;
    }

    @Override
    public String getFilename() {
        return getFullReport() ? BundleUtil.getString(Bundle.GEP, "alumni.full.reports.name") : BundleUtil.getString(Bundle.GEP,
                "alumni.partial.reports.name", getRequestDate().toString());
    }

    private List<Spreadsheet> buildReport() {

        final Spreadsheet curriculumData = new Spreadsheet("ALUMNI_CURRICULUM_DATA");
        curriculumData.setHeaders(new String[] { "NOME", "NUMERO_ALUNO", "CURSO", "INICIO", "CONCLUSAO", "DESCRICAO",
                "EMPREGADO ACTUALMENTE" });

        final Spreadsheet personalData = new Spreadsheet("ALUMNI_PERSONAL_DATA");
        personalData.setHeaders(new String[] { "NOME", "NUMERO_ALUNO", "DATA_NASCIMENTO", "MORADA", "COD_POSTAL", "LOCALIDADE",
                "PAIS", "EMAIL_PESSOAL", "EMAIL_ENVIAR", "TELEFONE", "REGISTADO EM" });

        final Spreadsheet jobData = new Spreadsheet("ALUMNI_JOB_DATA");
        jobData.setHeaders(new String[] { "IDENTIFICADOR", "NOME", "NUMERO_ALUNO", "EMPREGADOR", "CIDADE", "PAIS",
                "COD_AREA_NEGOCIO", "AREA_NEGOCIO", "POSICAO", "DATA_INICIO", "DATA_FIM", "TIPO_CONTRATO", "FORMA_COLOCACAO",
                "REMUN_MENSAL_BRUTA", "TIPO_SALARIO", "DATA_ALTERACAO", "DATA_REGISTO" });

        final Spreadsheet formationData = new Spreadsheet("ALUMNI_FORMATION_DATA");
        formationData.setHeaders(new String[] { "IDENTIFICADOR", "NOME", "NUMERO_ALUNO", "TIPO", "GRAU", "INSTITUICAO",
                "COD_AREA_EDUCATIVA", "AREA_EDUCATIVA", "INICIO", "CONCLUSAO", "CREDITOS_ECTS", "NUMERO_HORAS", "DATA_ALTERACAO",
                "DATA_REGISTO" });

        int count = 0;

        for (Person person : Person.readPersonsByRoleType(RoleType.ALUMNI)) {
            if ((++count % 100) == 0) {
                logger.info(String.format("Count %s persons", count));
            }

            if (!person.hasStudent()) {
                continue;
            }

            if (getOnlyRegisteredAlumni() && !person.getStudent().hasAlumni()) {
                continue;
            }

            if (!getFullReport()
                    && (!person.getStudent().hasAlumni() || person.getStudent().getAlumni().getUrlRequestToken() == null)) {
                continue;
            }

            String alumniName = person.getStudent().getName();
            Integer studentNumber = person.getStudent().getNumber();

            addCurriculumDataRow(curriculumData, alumniName, studentNumber, person.getStudent());
            addPersonalDataRow(personalData, alumniName, studentNumber, person, person.getStudent().getAlumni());

            if (person.getStudent().hasAlumni()) {
                for (Job job : person.getStudent().getAlumni().getJobs()) {
                    addJobDataRow(jobData, alumniName, studentNumber, job);
                }
                for (Formation formation : person.getStudent().getAlumni().getFormations()) {
                    addFormationDataRow(formationData, alumniName, studentNumber, formation);
                }
            }

        }

        List<Spreadsheet> sheets = new ArrayList<Spreadsheet>(4);
        sheets.add(curriculumData);
        sheets.add(personalData);
        sheets.add(jobData);
        sheets.add(formationData);
        return sheets;
    }

    private void addCurriculumDataRow(Spreadsheet sheet, String alumniName, Integer studentNumber, Student student) {
        Alumni alumni = student.getAlumni();

        // "NOME", "NUMERO_ALUNO", "CURSO", "INICIO", "CONCLUSAO", "DESCRICAO",
        // "EMPREGADO ACTUALMENTE"
        for (Registration registration : student.getRegistrations()) {
            if (registration.isBolonha()) {
                if (registration.hasConcluded()) {
                    final SortedSet<CycleCurriculumGroup> concludeCycles =
                            new TreeSet<CycleCurriculumGroup>(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
                    concludeCycles.addAll(registration.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops());
                    Row row = sheet.addRow();
                    row.setCell(alumniName);
                    row.setCell(studentNumber);
                    row.setCell(registration.getDegreeName());
                    row.setCell(registration.getStartDate().toString(DATE_FORMAT));
                    final CycleCurriculumGroup lastConcludedCycle = concludeCycles.last();
                    try {
                        row.setCell(lastConcludedCycle.isConclusionProcessed() ? lastConcludedCycle.getConclusionDate().toString(
                                DATE_FORMAT) : lastConcludedCycle.calculateConclusionDate().toString(DATE_FORMAT));
                    } catch (Exception ex) {
                        row.setCell(NOT_AVAILABLE);
                    }
                    row.setCell("Bolonha");
                    row.setCell(alumni != null && alumni.getIsEmployed() != null ? getApp("label." + alumni.getIsEmployed()) : NOT_AVAILABLE);
                }
            } else {
                if (registration.isRegistrationConclusionProcessed()) {
                    Row row = sheet.addRow();
                    row.setCell(alumniName);
                    row.setCell(studentNumber);
                    row.setCell(registration.getDegreeName());
                    row.setCell(registration.getStartDate().toString(DATE_FORMAT));
                    row.setCell(registration.getConclusionDate() != null ? registration.getConclusionDate().toString(DATE_FORMAT) : NOT_AVAILABLE);
                    row.setCell("Pre-Bolonha");
                    row.setCell(alumni != null && alumni.getIsEmployed() != null ? getApp("label." + alumni.getIsEmployed()) : NOT_AVAILABLE);
                }
            }
        }
    }

    private void addPersonalDataRow(Spreadsheet sheet, String alumniName, Integer studentNumber, Person person, Alumni alumni) {
        // "NOME", "NUMERO_ALUNO", "DATA_NASCIMENTO", "MORADA", "COD_POSTAL",
        // "PAIS",
        // "EMAIL_PESSOAL", "EMAIL_ENVIAR"
        // "TELEFONE", "TELEMOVEL", "REGISTERED_WHEN"
        final Row row = sheet.addRow();
        row.setCell(alumniName);
        row.setCell(studentNumber);
        final YearMonthDay dateOfBirth = person.getDateOfBirthYearMonthDay();
        row.setCell(dateOfBirth == null ? NOT_AVAILABLE : dateOfBirth.toString("dd/MM/yyyy"));
        row.setCell(hasLastPersonalAddress(person) ? getLastPersonalAddress(person).getAddress() : NOT_AVAILABLE);
        row.setCell(hasLastPersonalAddress(person) ? getLastPersonalAddress(person).getAreaCode() : NOT_AVAILABLE);
        row.setCell(hasLastPersonalAddress(person) ? getLastPersonalAddress(person).getArea() : NOT_AVAILABLE);
        row.setCell(hasLastPersonalAddress(person) ? getLastPersonalAddress(person).getCountryOfResidenceName() : NOT_AVAILABLE);
        row.setCell(hasPersonalEmail(person) ? getPersonalEmail(person).getValue() : NOT_AVAILABLE);
        row.setCell(hasSendingEmail(person) ? getSendingEmail(person) : NOT_AVAILABLE);
        row.setCell(hasPersonalPhone(person) ? getPersonalPhone(person).getNumber() : NOT_AVAILABLE);
        row.setCell(hasRegisteredWhen(alumni) ? alumni.getRegisteredWhen().toString("yyyy-MM-dd") : NOT_AVAILABLE);
    }

    private boolean hasRegisteredWhen(Alumni alumni) {
        return alumni != null && alumni.getRegisteredWhen() != null;
    }

    public boolean hasLastPersonalAddress(final Person person) {
        return getLastPersonalAddress(person) != null;
    }

    public PhysicalAddress getLastPersonalAddress(final Person person) {
        if (person.getStudent().hasAlumni()) {
            return person.getStudent().getAlumni().getLastPersonalAddress();
        }

        SortedSet<PhysicalAddress> addressSet = new TreeSet<PhysicalAddress>(DomainObjectUtil.COMPARATOR_BY_ID);
        addressSet.addAll(person.getPhysicalAddresses());
        return !addressSet.isEmpty() && addressSet.last() != null ? addressSet.last() : null;
    }

    public EmailAddress getPersonalEmail(final Person person) {
        if (person.getStudent().hasAlumni()) {
            return person.getStudent().getAlumni().getPersonalEmail();
        }

        for (EmailAddress email : person.getEmailAddresses()) {
            if (email.isPersonalType()) {
                return email;
            }
        }
        return null;
    }

    public Boolean hasPersonalEmail(final Person person) {
        return getPersonalEmail(person) != null;
    }

    public Phone getPersonalPhone(final Person person) {
        if (person.hasStudent() && person.getStudent().hasAlumni()) {
            return person.getStudent().getAlumni().getPersonalPhone();
        }

        for (Phone phone : person.getPhones()) {
            if (phone.isPersonalType()) {
                return phone;
            }
        }
        return null;
    }

    public Boolean hasPersonalPhone(final Person person) {
        return getPersonalPhone(person) != null;
    }

    public Boolean hasSendingEmail(final Person person) {
        return getSendingEmail(person) != null;
    }

    public String getSendingEmail(final Person person) {
        return person.getEmailForSendingEmails();
    }

    private String getEnum(final String key) {
        return BundleUtil.getString(Bundle.ENUMERATION, PT, key);
    }

    private String getApp(final String key) {
        return BundleUtil.getString(Bundle.APPLICATION, PT, key);
    }

    private void addJobDataRow(Spreadsheet sheet, String alumniName, Integer studentNumber, Job job) {
        // "IDENTIFICADOR", "NOME", "NUMERO_ALUNO", "EMPREGADOR", "CIDADE",
        // "PAIS",
        // "COD_AREA_NEGOCIO", "AREA_NEGOCIO", "POSICAO", "DATA_INICIO",
        // "DATA_FIM", "FORMA_COLOCACAO", "TIPO_CONTRATO", "SALARIO",
        // "TIPO_SALARIO", "DATA_ALTERACAO", "DATA_REGISTO"
        final Row row = sheet.addRow();
        row.setCell(job.getExternalId());
        row.setCell(alumniName);
        row.setCell(studentNumber);
        row.setCell(job.getEmployerName());
        row.setCell(job.getCity());
        row.setCell(job.getCountry() != null ? job.getCountry().getName() : NOT_AVAILABLE);
        row.setCell(job.getBusinessArea() != null ? job.getBusinessArea().getCode() : NOT_AVAILABLE);
        row.setCell(job.getBusinessArea() != null ? job.getBusinessArea().getDescription().replace(';', '|') : NOT_AVAILABLE);
        row.setCell(job.getPosition());
        row.setCell(job.getBeginDate() != null ? job.getBeginDate().toString(DATE_FORMAT) : NOT_AVAILABLE);
        row.setCell(job.getEndDate() != null ? job.getEndDate().toString(DATE_FORMAT) : NOT_AVAILABLE);
        row.setCell(job.getContractType() != null ? getEnum(job.getContractType().getQualifiedName()) : NOT_AVAILABLE);
        row.setCell(job.getJobApplicationType() != null ? getEnum(job.getJobApplicationType().getQualifiedName()) : NOT_AVAILABLE);
        row.setCell(job.getSalary() != null ? job.getSalary().toString() : NOT_AVAILABLE);
        row.setCell(job.getSalaryType() != null ? getEnum(job.getSalaryType().getQualifiedName()) : NOT_AVAILABLE);
        row.setCell(job.getLastModifiedDate() != null ? job.getLastModifiedDate().toString(DATE_FORMAT) : NOT_AVAILABLE);
        AlumniIdentityCheckRequest lastIdentityRequest = job.getPerson().getStudent().getAlumni().getLastIdentityRequest();
        row.setCell(lastIdentityRequest != null ? lastIdentityRequest.getCreationDateTime().toString(DATE_FORMAT) : NOT_AVAILABLE);
    }

    private void addFormationDataRow(Spreadsheet sheet, String alumniName, Integer studentNumber, Formation formation) {
        // "IDENTIFICADOR", "NOME", "NUMERO_ALUNO", "TIPO", "GRAU",
        // "INSTITUICAO",
        // "COD_AREA_EDUCATIVA",
        // "AREA_EDUCATIVA", "INICIO", "CONCLUSAO", "CREDITOS_ECTS",
        // "NUMERO_HORAS", "DATA_ALTERACAO", "DATA_REGISTO"
        final Row row = sheet.addRow();
        row.setCell(formation.getExternalId());
        row.setCell(alumniName);
        row.setCell(studentNumber);
        row.setCell(formation.getFormationType() != null ? getEnum(formation.getFormationType().getQualifiedName()) : NOT_AVAILABLE);
        row.setCell(formation.getType() != null ? getEnum(formation.getType().getQualifiedName()) : NOT_AVAILABLE);
        row.setCell(formation.getInstitution() != null ? formation.getInstitution().getUnitName().getName() : NOT_AVAILABLE);
        row.setCell(formation.getEducationArea() != null ? formation.getEducationArea().getCode() : NOT_AVAILABLE);
        row.setCell(formation.getEducationArea() != null ? formation.getEducationArea().getDescription().replace(';', '|') : NOT_AVAILABLE);
        row.setCell(formation.getBeginYear());
        row.setCell(formation.getYear());
        row.setCell(formation.getEctsCredits() != null ? formation.getEctsCredits().toString() : NOT_AVAILABLE);
        row.setCell(formation.getFormationHours() != null ? formation.getFormationHours().toString() : NOT_AVAILABLE);
        row.setCell(formation.getLastModificationDateDateTime() != null ? formation.getLastModificationDateDateTime().toString(
                DATE_FORMAT) : NOT_AVAILABLE);
        AlumniIdentityCheckRequest lastIdentityRequest = formation.getPerson().getStudent().getAlumni().getLastIdentityRequest();
        row.setCell(lastIdentityRequest != null ? lastIdentityRequest.getCreationDateTime().toString(DATE_FORMAT) : NOT_AVAILABLE);
    }

    public static AlumniReportFile launchJob(boolean fullReport, boolean onlyRegisteredAlumni) {
        return new AlumniReportFile(fullReport, onlyRegisteredAlumni);
    }

    public static List<AlumniReportFile> readDoneJobs() {
        List<AlumniReportFile> reportFileList = new ArrayList<AlumniReportFile>();

        CollectionUtils.select(ExecutionYear.readCurrentExecutionYear().getAlumniReportFiles(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((AlumniReportFile) arg0).getDone();
            }
        }, reportFileList);

        return reportFileList;
    }

    public static List<AlumniReportFile> readUndoneJobs() {
        return new ArrayList(CollectionUtils.subtract(ExecutionYear.readCurrentExecutionYear().getAlumniReportFiles(),
                readDoneJobs()));
    }

    public static List<AlumniReportFile> readPendingJobs() {
        List<AlumniReportFile> reportFileList = new ArrayList<AlumniReportFile>();

        CollectionUtils.select(ExecutionYear.readCurrentExecutionYear().getAlumniReportFiles(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((AlumniReportFile) arg0).getIsNotDoneAndNotCancelled();
            }
        }, reportFileList);

        return reportFileList;
    }

    public static Boolean canRequestReport() {
        return readPendingJobs().isEmpty();
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
