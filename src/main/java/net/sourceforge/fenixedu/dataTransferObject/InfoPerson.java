package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.student.Registration;

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

    public InfoExternalPerson getInfoExternalPerson() {
        return InfoExternalPerson.newInfoFromDomain(getPerson().getExternalContract());
    }

    public Homepage getHomepage() {
        return getPerson().getHomepage();
    }

    public static InfoPerson newInfoFromDomain(Person person) {
        return (person != null) ? new InfoPerson(person) : null;
    }

    public InfoEmployee getInfoEmployee() {
        return InfoEmployee.newInfoFromDomain(getPerson().getEmployee());
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

    public List<String> getMainRoles() {
        return getPerson().getMainRoles();
    }

    public String getIstUsername() {
        return getPerson().getIstUsername();
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