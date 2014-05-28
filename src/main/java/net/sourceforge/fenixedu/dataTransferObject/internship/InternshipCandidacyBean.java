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
package net.sourceforge.fenixedu.dataTransferObject.internship;

import java.io.Serializable;
import java.util.Collections;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.internship.InternshipCandidacy;
import net.sourceforge.fenixedu.domain.internship.InternshipCandidacySession;
import net.sourceforge.fenixedu.domain.internship.LanguageKnowledgeLevel;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.person.Gender;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author Pedro Santos (pmrsa)
 */
public class InternshipCandidacyBean implements Serializable, Comparable<InternshipCandidacyBean> {
    private static final long serialVersionUID = -4963748520642734496L;

    public static class UniversitiesProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            InternshipCandidacyBean bean = (InternshipCandidacyBean) source;
            if (bean.getSession() != null) {
                return bean.getSession().getUniversitySet();
            }
            return Collections.emptySet();
        }
    }

    public static class DestinationsProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            InternshipCandidacyBean bean = (InternshipCandidacyBean) source;
            if (bean.getSession() != null) {
                return bean.getSession().getDestinationSet();
            }
            return Collections.emptySet();
        }
    }

    public enum StudentYear {
        FIRST, SECOND;
    }

    private final InternshipCandidacySession session;

    private InternshipCandidacy candidacy;
    private AcademicalInstitutionUnit university;
    private String studentNumber;
    private StudentYear studentYear;
    private String degree;
    private String branch;
    private String name;
    private Gender gender;

    private String street;
    private String area;
    private String areaCode;

    private String email;
    private String telephone;
    private String mobilePhone;

    private LocalDate birthday;
    private String parishOfBirth;
    private Country countryOfBirth;

    private String documentIdNumber;
    private String emissionLocationOfDocumentId;
    private LocalDate emissionDateOfDocumentId;
    private LocalDate expirationDateOfDocumentId;

    private String passportIdNumber;
    private String emissionLocationOfPassport;
    private LocalDate emissionDateOfPassport;
    private LocalDate expirationDateOfPassport;

    private Country firstDestination;
    private Country secondDestination;
    private Country thirdDestination;

    private LanguageKnowledgeLevel english = LanguageKnowledgeLevel.NONE;
    private LanguageKnowledgeLevel french = LanguageKnowledgeLevel.NONE;
    private LanguageKnowledgeLevel spanish = LanguageKnowledgeLevel.NONE;
    private LanguageKnowledgeLevel german = LanguageKnowledgeLevel.NONE;

    private Boolean previousCandidacy;

    public InternshipCandidacyBean(InternshipCandidacySession session) {
        this.session = session;
        setCountryOfBirth(Country.readByTwoLetterCode("PT"));
    }

    public InternshipCandidacyBean(InternshipCandidacy candidacy) {
        this.session = candidacy.getInternshipCandidacySession();
        setCandidacy(candidacy);
        setStudentNumber(candidacy.getStudentNumber());
        setUniversity(candidacy.getUniversity());
        if (candidacy.getStudentYear() == 1) {
            setStudentYear(StudentYear.FIRST);
        } else {
            setStudentYear(StudentYear.SECOND);
        }
        setDegree(candidacy.getDegree());
        setBranch(candidacy.getBranch());
        setName(candidacy.getName());
        setGender(candidacy.getGender());
        setBirthday(candidacy.getBirthday());
        setParishOfBirth(candidacy.getParishOfBirth());
        setCountryOfBirth(candidacy.getCountryOfBirth());

        setDocumentIdNumber(candidacy.getDocumentIdNumber());
        setEmissionLocationOfDocumentId(candidacy.getEmissionLocationOfDocumentId());
        setEmissionDateOfDocumentId(candidacy.getEmissionDateOfDocumentId());
        setExpirationDateOfDocumentId(candidacy.getExpirationDateOfDocumentId());

        setPassportIdNumber(candidacy.getPassportIdNumber());
        setEmissionLocationOfPassport(candidacy.getEmissionLocationOfPassport());
        setEmissionDateOfPassport(candidacy.getEmissionDateOfPassport());
        setExpirationDateOfPassport(candidacy.getExpirationDateOfPassport());

        setStreet(candidacy.getStreet());
        setAreaCode(candidacy.getAreaCode());
        setArea(candidacy.getArea());

        setTelephone(candidacy.getTelephone());
        setMobilePhone(candidacy.getMobilePhone());
        setEmail(candidacy.getEmail());

        setFirstDestination(candidacy.getFirstDestination());
        setSecondDestination(candidacy.getSecondDestination());
        setThirdDestination(candidacy.getThirdDestination());

        setEnglish(candidacy.getEnglish());
        setFrench(candidacy.getFrench());
        setSpanish(candidacy.getSpanish());
        setGerman(candidacy.getGerman());

        setPreviousCandidacy(candidacy.getPreviousCandidacy());
    }

    public InternshipCandidacySession getSession() {
        return session;
    }

    public InternshipCandidacy getCandidacy() {
        return candidacy;
    }

    public void setCandidacy(InternshipCandidacy candidacy) {
        this.candidacy = candidacy;
    }

    public Country getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(Country countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public StudentYear getStudentYear() {
        return studentYear;
    }

    public void setStudentYear(StudentYear studentYear) {
        this.studentYear = studentYear;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getParishOfBirth() {
        return parishOfBirth;
    }

    public void setParishOfBirth(String parishOfBirth) {
        this.parishOfBirth = parishOfBirth;
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public String getEmissionLocationOfDocumentId() {
        return emissionLocationOfDocumentId;
    }

    public void setEmissionLocationOfDocumentId(String emissionLocationOfDocumentId) {
        this.emissionLocationOfDocumentId = emissionLocationOfDocumentId;
    }

    public LocalDate getEmissionDateOfDocumentId() {
        return emissionDateOfDocumentId;
    }

    public void setEmissionDateOfDocumentId(LocalDate emissionDateOfDocumentId) {
        this.emissionDateOfDocumentId = emissionDateOfDocumentId;
    }

    public LocalDate getExpirationDateOfDocumentId() {
        return expirationDateOfDocumentId;
    }

    public void setExpirationDateOfDocumentId(LocalDate expirationDateOfDocumentId) {
        this.expirationDateOfDocumentId = expirationDateOfDocumentId;
    }

    public String getPassportIdNumber() {
        return passportIdNumber;
    }

    public void setPassportIdNumber(String passportIdNumber) {
        this.passportIdNumber = passportIdNumber;
    }

    public String getEmissionLocationOfPassport() {
        return emissionLocationOfPassport;
    }

    public void setEmissionLocationOfPassport(String emissionLocationOfPassport) {
        this.emissionLocationOfPassport = emissionLocationOfPassport;
    }

    public LocalDate getEmissionDateOfPassport() {
        return emissionDateOfPassport;
    }

    public void setEmissionDateOfPassport(LocalDate emissionDateOfPassport) {
        this.emissionDateOfPassport = emissionDateOfPassport;
    }

    public LocalDate getExpirationDateOfPassport() {
        return expirationDateOfPassport;
    }

    public void setExpirationDateOfPassport(LocalDate expirationDateOfPassport) {
        this.expirationDateOfPassport = expirationDateOfPassport;
    }

    public Country getFirstDestination() {
        return firstDestination;
    }

    public void setFirstDestination(Country country) {
        this.firstDestination = country;
    }

    public Country getSecondDestination() {
        return secondDestination;
    }

    public void setSecondDestination(Country country) {
        this.secondDestination = country;
    }

    public Country getThirdDestination() {
        return thirdDestination;
    }

    public void setThirdDestination(Country country) {
        this.thirdDestination = country;
    }

    public LanguageKnowledgeLevel getEnglish() {
        return english;
    }

    public void setEnglish(LanguageKnowledgeLevel english) {
        this.english = english;
    }

    public LanguageKnowledgeLevel getFrench() {
        return french;
    }

    public void setFrench(LanguageKnowledgeLevel french) {
        this.french = french;
    }

    public LanguageKnowledgeLevel getSpanish() {
        return spanish;
    }

    public void setSpanish(LanguageKnowledgeLevel spanish) {
        this.spanish = spanish;
    }

    public LanguageKnowledgeLevel getGerman() {
        return german;
    }

    public void setGerman(LanguageKnowledgeLevel german) {
        this.german = german;
    }

    public Boolean getPreviousCandidacy() {
        return previousCandidacy;
    }

    public void setPreviousCandidacy(Boolean previousCandidacy) {
        this.previousCandidacy = previousCandidacy;
    }

    public AcademicalInstitutionUnit getUniversity() {
        return university;
    }

    public void setUniversity(AcademicalInstitutionUnit university) {
        this.university = university;
    }

    @Override
    public int compareTo(InternshipCandidacyBean other) {
        int order = getCandidacy().getCandidacyDate().compareTo(other.getCandidacy().getCandidacyDate());
        if (order != 0) {
            return order;
        }
        order = getStudentNumber().compareTo(other.getStudentNumber());
        if (order != 0) {
            return order;
        }
        return getName().compareTo(other.getName());
    }
}
