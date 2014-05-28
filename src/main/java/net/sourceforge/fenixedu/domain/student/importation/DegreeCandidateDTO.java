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
/**
 * 
 */
package net.sourceforge.fenixedu.domain.student.importation;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.loaders.IFileLine;
import pt.utl.ist.fenix.tools.util.PhoneUtil;

public class DegreeCandidateDTO implements IFileLine {

    private static final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "PT"));
        DATE_FORMAT.setLenient(false);
    }

    private String degreeCode;

    private String documentIdNumber;

    private String documentCheckDigit;

    private String name;

    private String address;

    private String areaCode;

    private String areaOfAreaCode;

    private String phoneNumber;

    private Gender gender;

    private YearMonthDay dateOfBirth;

    private String contigent;

    private Ingression ingression;

    private Integer placingOption;

    private String highSchoolFinalGrade;

    private Double entryGrade;

    private String highSchoolName;

    private AcademicalInstitutionType highSchoolType;

    private String highSchoolDegreeDesignation;

    private EntryPhase entryPhase;

    private String istUniversity;

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        printField(result, "Degree Code", this.degreeCode);
        printField(result, "Document ID", this.documentIdNumber);
        printField(result, "Document Check Digit", this.documentCheckDigit);
        printField(result, "Name", this.name);
        printField(result, "Address", this.address);
        printField(result, "Area Code", this.areaCode);
        printField(result, "Area of Area Code", this.areaOfAreaCode);
        printField(result, "Phone", this.phoneNumber);
        printField(result, "Gender ", this.gender.name());
        printField(result, "Date Of Birth", this.dateOfBirth.toString("dd-MM-yyyy"));
        printField(result, "Contigent", this.contigent);
        printField(result, "Ingression", this.ingression.name());
        printField(result, "Placing Option", this.placingOption.toString());
        printField(result, "Highschool Final Grade", this.highSchoolFinalGrade);
        printField(result, "Entry Grade", this.entryGrade.toString());
        printField(result, "Highschool Name", this.highSchoolName);
        printField(result, "Highschool Type", this.highSchoolType.name());
        printField(result, "Degree Designation", this.highSchoolDegreeDesignation);
        printField(result, "Entry Phase", this.entryPhase.toString());
        printField(result, "Ist University", this.istUniversity);

        return result.toString();

    }

    private void printField(final StringBuilder result, final String name, final String value) {
        result.append(name).append(":").append(value).append("\n");
    }

    public DegreeCandidateDTO() {

    }

    /**
     * <pre>
     * 
     * EstabCol(0)	CursoCol (cods old)(1)	NumBI(2)	LocBI(3)	Descr loc BI(4)	Check Digit(5)	Nome(6)	Morada1(7)	
     * Morada2(8)	Codpos(9)	Codpos3(10)	CodLocal(11)	Telefone(12)	Sexo(13)	DataNasc(dd-MMM-yy)(14)	
     * Conting(15)	PrefCol (op ingresso)(16)	EtapCol(17)	Media12(18)	NotaCand(19)	cod_escola_sec(20)	
     * escola_sec(21)	tipo_estab_sec(22)	curso_secundario(23)
     * 
     * </pre>
     */

    @Override
    public boolean fillWithFileLineData(String dataLine) {

        if (StringUtils.isEmpty(dataLine.trim()) || dataLine.startsWith("#")) {
            return false;
        }

        final String[] fields = dataLine.split("\t");
        this.degreeCode = fields[1].trim();
        this.documentIdNumber = fields[2].trim();
        this.documentCheckDigit = fields[5].trim();
        this.name = fields[6].trim();
        this.address = fields[7].trim() + " " + fields[8].trim();
        this.areaCode = fields[9].trim() + "-" + fields[10].trim();
        this.areaOfAreaCode = fields[11].trim();
        this.phoneNumber = fields[12].trim();
        this.gender = String2Gender.convert(fields[13].trim());
        this.dateOfBirth = parseDate(fields[14].trim());
        this.contigent = fields[15].trim();
        this.ingression = DgesBaseProcess.CONTINGENT_TO_INGRESSION_CONVERSION.get(this.contigent);
        this.placingOption = Integer.valueOf(fields[16].trim());
        this.highSchoolFinalGrade = new BigDecimal(fields[18].trim()).divide(BigDecimal.valueOf(10)).toPlainString();
        this.entryGrade = new BigDecimal(fields[19].trim().replace(',', '.')).doubleValue();
        this.highSchoolName = fields[21].trim();
        this.highSchoolType = parseHighSchoolType(fields[22].trim());
        this.highSchoolDegreeDesignation = fields[23].trim();

        return true;
    }

    private AcademicalInstitutionType parseHighSchoolType(final String value) {
        if (value.equals("PRI")) {
            return AcademicalInstitutionType.PRIVATE_HIGH_SCHOOL;
        } else if (value.equals("PUB")) {
            return AcademicalInstitutionType.PUBLIC_HIGH_SCHOOL;
        } else {
            throw new RuntimeException("Unexpected high school type");
        }

    }

    private YearMonthDay parseDate(final String value) {
        try {
            return YearMonthDay.fromDateFields(DATE_FORMAT.parse(value));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUniqueKey() {
        return this.documentIdNumber;
    }

    public String getDegreeCode() {
        return degreeCode;
    }

    public void setDegreeCode(String degreeCode) {
        this.degreeCode = degreeCode;
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public String getDocumentCheckDigit() {
        return documentCheckDigit;
    }

    public void setDocumentCheckDigit(String documentCheckDigit) {
        this.documentCheckDigit = documentCheckDigit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaOfAreaCode() {
        return areaOfAreaCode;
    }

    public void setAreaOfAreaCode(String areaOfAreaCode) {
        this.areaOfAreaCode = areaOfAreaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public YearMonthDay getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(YearMonthDay dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContigent() {
        return contigent;
    }

    public void setContigent(String contigent) {
        this.contigent = contigent;
    }

    public Ingression getIngression() {
        return ingression;
    }

    public void setIngression(Ingression ingression) {
        this.ingression = ingression;
    }

    public Integer getPlacingOption() {
        return placingOption;
    }

    public void setPlacingOption(Integer placingOption) {
        this.placingOption = placingOption;
    }

    public String getHighSchoolFinalGrade() {
        return highSchoolFinalGrade;
    }

    public void setHighSchoolFinalGrade(String highSchoolFinalGrade) {
        this.highSchoolFinalGrade = highSchoolFinalGrade;
    }

    public Double getEntryGrade() {
        return entryGrade;
    }

    public void setEntryGrade(Double entryGrade) {
        this.entryGrade = entryGrade;
    }

    public String getHighSchoolName() {
        return highSchoolName;
    }

    public void setHighSchoolName(String highSchoolName) {
        this.highSchoolName = highSchoolName;
    }

    public AcademicalInstitutionType getHighSchoolType() {
        return highSchoolType;
    }

    public void setHighSchoolType(AcademicalInstitutionType highSchoolType) {
        this.highSchoolType = highSchoolType;
    }

    public String getHighSchoolDegreeDesignation() {
        return highSchoolDegreeDesignation;
    }

    public void setHighSchoolDegreeDesignation(String highSchoolDegreeDesignation) {
        this.highSchoolDegreeDesignation = highSchoolDegreeDesignation;
    }

    public EntryPhase getEntryPhase() {
        return entryPhase;
    }

    public void setEntryPhase(EntryPhase entryPhase) {
        this.entryPhase = entryPhase;
    }

    public String getIstUniversity() {
        return istUniversity;
    }

    public void setIstUniversity(String istUniversity) {
        this.istUniversity = istUniversity;
    }

    public Person getMatchingPerson() throws MatchingPersonException {
        Collection<Person> persons = Person.readByDocumentIdNumber(getDocumentIdNumber());

        if (persons.isEmpty()) {
            throw new NotFoundPersonException();
        }

        if (persons.size() > 1) {
            throw new TooManyMatchedPersonsException();
        }

        final Person person = persons.iterator().next();

        if (person.getDateOfBirthYearMonthDay() != null && person.getDateOfBirthYearMonthDay().equals(getDateOfBirth())) {
            return person;
        }

        if (person.getName().equals(getName())) {
            return person;
        }

        throw new NotFoundPersonException();
    }

    public Person createPerson() {
        final Person person = new Person(getName(), getGender(), getDocumentIdNumber(), IDDocumentType.IDENTITY_CARD);

        person.setMaritalStatus(MaritalStatus.SINGLE);
        person.setDateOfBirthYearMonthDay(getDateOfBirth());
        person.setIdentificationDocumentSeriesNumber(getDocumentCheckDigit());

        final PhysicalAddress createPhysicalAddress =
                PhysicalAddress.createPhysicalAddress(person, new PhysicalAddressData(getAddress(), getAreaCode(),
                        getAreaOfAreaCode(), null), PartyContactType.PERSONAL, true);
        createPhysicalAddress.setValid();

        if (PhoneUtil.isMobileNumber(getPhoneNumber())) {
            final MobilePhone createMobilePhone =
                    MobilePhone.createMobilePhone(person, getPhoneNumber(), PartyContactType.PERSONAL, true);
            createMobilePhone.setValid();
        } else {
            final Phone createPhone = Phone.createPhone(person, getPhoneNumber(), PartyContactType.PERSONAL, true);
            createPhone.setValid();
        }

        return person;
    }

    public ExecutionDegree getExecutionDegree(final ExecutionYear executionYear, final Space campus) {
        return ExecutionDegree.readByDegreeCodeAndExecutionYearAndCampus(getDegreeCode(), executionYear, campus);
    }

    public static abstract class MatchingPersonException extends Exception {

        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

    }

    public static class NotFoundPersonException extends MatchingPersonException {

        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

    }

    public static class TooManyMatchedPersonsException extends MatchingPersonException {

        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

    }
}