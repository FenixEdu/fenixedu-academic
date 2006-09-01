package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class InfoPerson extends InfoObject {

    private DomainReference<Person> person;

    public InfoPerson(Person person) {
        this.person = new DomainReference<Person>(person);
    }

    public boolean equals(Object o) {
        return ((o instanceof InfoPerson)
                && (getNumeroDocumentoIdentificacao().equals(((InfoPerson) o)
                        .getNumeroDocumentoIdentificacao())) && (getTipoDocumentoIdentificacao()
                .equals(((InfoPerson) o).getTipoDocumentoIdentificacao())));
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Person :\n");
        result.append("\n  - Identification Document Number : ").append(
                getNumeroDocumentoIdentificacao());
        result.append("\n  - Identification Document Type : ").append(getTipoDocumentoIdentificacao());
        result.append("\n  - Identification Document Issue Place : ").append(
                getLocalEmissaoDocumentoIdentificacao());
        result.append("\n  - Identification Document Issue Date : ").append(
                getDataEmissaoDocumentoIdentificacao());
        result.append("\n  - Identification Document Expiration Date : ").append(
                getDataValidadeDocumentoIdentificacao());
        result.append("\n  - Name : ").append(getNome());
        ;
        result.append("\n  - Birth : ").append(getNascimento());
        result.append("\n  - Father Name : ").append(getNomePai());
        result.append("\n  - Mother Name : ").append(getNomeMae());
        result.append("\n  - Nationality : ").append(getInfoPais().getNationality());
        result.append("\n  - Birth Place Parish : ").append(getFreguesiaNaturalidade());
        result.append("\n  - Birth Place Municipality : ").append(getConcelhoNaturalidade());
        result.append("\n  - Birth Place District : ").append(getDistritoNaturalidade());
        result.append("\n  - Address : ").append(getMorada());
        result.append("\n  - Place : ").append(getLocalidade());
        result.append("\n  - Post Code : ").append(getCodigoPostal());
        result.append("\n  - Address Parish : ").append(getFreguesiaMorada());
        result.append("\n  - Address Municipality : ").append(getConcelhoMorada());
        result.append("\n  - Address District : ").append(getDistritoMorada());
        result.append("\n  - Telephone : ").append(getTelefone());
        result.append("\n  - MobilePhone : ").append(getTelemovel());
        result.append("\n  - E-Mail : ").append(getEmail());
        result.append("\n  - HomePage : ").append(getEnderecoWeb());
        result.append("\n  - Contributor Number : ").append(getNumContribuinte());
        result.append("\n  - Username : ").append(getUsername());
        result.append("\n  - Password : ").append(getPassword());
        result.append("\n  - Occupation : ").append(getProfissao());
        result.append("\n  - Codigo Fiscal : ").append(getCodigoFiscal());
        result.append("\n  - studentList : ").append(getInfoStudentCurricularPlanList());
        return result.toString();
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
        return getPerson().getWebAddress();
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
        return getPerson().getCountry().getNationality();
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

    public String getPassword() {
        return getPerson().getPassword();
    }

    public String getProfissao() {
        return getPerson().getProfession();
    }

    public Gender getSexo() {
        return getPerson().getGender();
    }

    public String getTelefone() {
        return getPerson().getPhone();
    }

    public String getTelemovel() {
        return getPerson().getMobile();
    }

    public IDDocumentType getTipoDocumentoIdentificacao() {
        return getPerson().getIdDocumentType();
    }

    public String getUsername() {
        return getPerson().getUsername();
    }

    public List<InfoAdvisory> getInfoAdvisories() {
        final List<InfoAdvisory> result = new ArrayList<InfoAdvisory>(getPerson().getAdvisoriesCount());
        for (final Advisory advisory : getPerson().getAdvisoriesSet()) {
            result.add(InfoAdvisory.newInfoFromDomain(advisory));
        }
        return result;
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

    public Boolean getAvailablePhoto() {
        return getPerson().getAvailablePhoto();
    }

    public InfoExternalPerson getInfoExternalPerson() {
        return InfoExternalPersonWithPersonAndWLocation.newInfoFromDomain(getPerson()
                .getExternalPerson());
    }

    private List<String> getImportantRoles(Person person, List<String> mainRoles) {

        if (person.getPersonRolesCount() != 0) {
            boolean teacher = false, employee = false;

            for (final Role personRole : person.getPersonRolesSet()) {

                if (personRole.getRoleType() == RoleType.TEACHER) {
                    mainRoles.add("Docente");
                    teacher = true;

                } else if (personRole.getRoleType() == RoleType.STUDENT) {
                    mainRoles.add("Aluno");

                } else if (personRole.getRoleType() == RoleType.GRANT_OWNER) {
                    mainRoles.add("Bolseiro");
                } else if (!teacher && personRole.getRoleType() == RoleType.EMPLOYEE) {
                    employee = true;
                }
            }
            if (employee && !teacher) {
                mainRoles.add("Funcionário");
            }
        }
        return mainRoles;
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
            for (final StudentCurricularPlan studentCurricularPlan : registration
                    .getStudentCurricularPlansSet()) {
                result.add(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));
            }
        }
        return result;
    }

    public InfoTeacher getInfoTeacher() {
        return InfoTeacher.newInfoFromDomain(getPerson().getTeacher());
    }

    public List<String> getMainRoles() {
        return getImportantRoles(getPerson(), new ArrayList());
    }

    public String getIstUsername() {
        return getPerson().getIstUsername();
    }

    @Override
    public Integer getIdInternal() {
        return getPerson().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

    private Person getPerson() {
        return person == null ? null : person.getObject();
    }

}