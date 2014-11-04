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
package org.fenixedu.academic.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.person.MaritalStatus;
import org.fenixedu.academic.domain.student.Registration;

public class InfoPerson extends InfoObject {

    private final Person person;

    public InfoPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        return ((o instanceof InfoPerson)
                && (getNumeroDocumentoIdentificacao().equals(((InfoPerson) o).getNumeroDocumentoIdentificacao())) && (getTipoDocumentoIdentificacao()
                    .equals(((InfoPerson) o).getTipoDocumentoIdentificacao())));
    }

    @Override
    public String toString() {
        return getPerson().toString();
    }

    public String getCodigoFiscal() {
        return getPerson().getFiscalCode();
    }

    public String getCodigoPostal() {
        return getPerson().getAreaCode();
    }

    public String getConcelhoMorada() {
        return getPerson().getDistrictSubdivisionOfResidence();
    }

    public String getConcelhoNaturalidade() {
        return getPerson().getDistrictSubdivisionOfBirth();
    }

    public Date getDataEmissaoDocumentoIdentificacao() {
        return getPerson().getEmissionDateOfDocumentId();
    }

    public Date getDataValidadeDocumentoIdentificacao() {
        return getPerson().getExpirationDateOfDocumentId();
    }

    public String getDistritoMorada() {
        return getPerson().getDistrictOfResidence();
    }

    public String getDistritoNaturalidade() {
        return getPerson().getDistrictOfBirth();
    }

    public String getEmail() {
        return getPerson().getEmail();
    }

    public String getEnderecoWeb() {
        return getPerson().getDefaultWebAddressUrl();
    }

    public MaritalStatus getMaritalStatus() {
        return getPerson().getMaritalStatus();
    }

    public String getFreguesiaMorada() {
        return getPerson().getParishOfResidence();
    }

    public String getFreguesiaNaturalidade() {
        return getPerson().getParishOfBirth();
    }

    public InfoCountry getInfoPais() {
        return InfoCountry.newInfoFromDomain(getPerson().getCountry());
    }

    public String getLocalEmissaoDocumentoIdentificacao() {
        return getPerson().getEmissionLocationOfDocumentId();
    }

    public String getLocalidade() {
        return getPerson().getArea();
    }

    public String getLocalidadeCodigoPostal() {
        return getPerson().getAreaOfAreaCode();
    }

    public String getMorada() {
        return getPerson().getAddress();
    }

    public String getNacionalidade() {
        return getPerson().getCountry() != null ? getPerson().getCountry().getNationality() : null;
    }

    public Date getNascimento() {
        return getPerson().getDateOfBirth();
    }

    public String getNome() {
        return getPerson().getName();
    }

    public String getNomeMae() {
        return getPerson().getNameOfMother();
    }

    public String getNomePai() {
        return getPerson().getNameOfFather();
    }

    public String getNumContribuinte() {
        return getPerson().getSocialSecurityNumber();
    }

    public String getNumeroDocumentoIdentificacao() {
        return getPerson().getDocumentIdNumber();
    }

    public String getProfissao() {
        return getPerson().getProfession();
    }

    public Gender getSexo() {
        return getPerson().getGender();
    }

    public String getTelefone() {
        return getPerson().getDefaultPhoneNumber();
    }

    public String getTelemovel() {
        return getPerson().getDefaultMobilePhoneNumber();
    }

    public IDDocumentType getTipoDocumentoIdentificacao() {
        return getPerson().getIdDocumentType();
    }

    public String getUsername() {
        return getPerson().getUsername();
    }

    public Boolean getAvailableEmail() {
        return getPerson().getAvailableEmail();
    }

    public String getWorkPhone() {
        return getPerson().getWorkPhone();
    }

    public Boolean getAvailableWebSite() {
        return getPerson().getAvailableWebSite();
    }

    public static InfoPerson newInfoFromDomain(Person person) {
        return (person != null) ? new InfoPerson(person) : null;
    }

    public List<InfoStudentCurricularPlan> getInfoStudentCurricularPlanList() {
        final List<InfoStudentCurricularPlan> result = new ArrayList<InfoStudentCurricularPlan>();
        for (final Registration registration : getPerson().getStudentsSet()) {
            for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                result.add(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));
            }
        }
        return result;
    }

    public InfoTeacher getInfoTeacher() {
        return InfoTeacher.newInfoFromDomain(getPerson().getTeacher());
    }

    @Override
    public String getExternalId() {
        return getPerson().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    public Person getPerson() {
        return person;
    }

}