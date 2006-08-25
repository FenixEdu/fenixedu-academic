package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class InfoPerson extends InfoObject {
    
    private Person person;

    public InfoPerson(Person person) {
	this.person = person;
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
        result.append("\n  - Identification Document Number : ").append(getNumeroDocumentoIdentificacao());
        result.append("\n  - Identification Document Type : ").append(getTipoDocumentoIdentificacao());
        result.append("\n  - Identification Document Issue Place : ").append(getLocalEmissaoDocumentoIdentificacao());
        result.append("\n  - Identification Document Issue Date : ").append(getDataEmissaoDocumentoIdentificacao());
        result.append("\n  - Identification Document Expiration Date : ").append(getDataValidadeDocumentoIdentificacao());
        result.append("\n  - Name : ").append(getNome());;
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
        return person.getFiscalCode();
    }

    public String getCodigoPostal() {
        return person.getAreaCode();
    }

    public String getConcelhoMorada() {
        return person.getDistrictSubdivisionOfResidence();
    }

    public String getConcelhoNaturalidade() {
        return person.getDistrictSubdivisionOfBirth();
    }

    public Date getDataEmissaoDocumentoIdentificacao() {
        return person.getEmissionDateOfDocumentId();
    }

    public Date getDataValidadeDocumentoIdentificacao() {
        return person.getExpirationDateOfDocumentId();
    }

    public String getDistritoMorada() {
        return person.getDistrictOfResidence();
    }

    public String getDistritoNaturalidade() {
        return person.getDistrictOfBirth();
    }

    public String getEmail() {
        return person.getEmail();
    }

    public String getEnderecoWeb() {
        return person.getWebAddress();
    }

    public MaritalStatus getMaritalStatus() {
        return person.getMaritalStatus();
    }

    public String getFreguesiaMorada() {
        return person.getParishOfResidence();
    }

    public String getFreguesiaNaturalidade() {
        return person.getParishOfBirth();
    }

    public InfoCountry getInfoPais() {
        return InfoCountry.newInfoFromDomain(person.getCountry());
    }

    public String getLocalEmissaoDocumentoIdentificacao() {
        return person.getEmissionLocationOfDocumentId();
    }

    public String getLocalidade() {
        return person.getArea();
    }

    public String getLocalidadeCodigoPostal() {
        return person.getAreaOfAreaCode();
    }

    public String getMorada() {
        return person.getAddress();
    }

    public String getNacionalidade() {
        return person.getCountry().getNationality();
    }

    public Date getNascimento() {
        return person.getDateOfBirth();
    }

    public String getNome() {
        return person.getName();
    }

    public String getNomeMae() {
        return person.getNameOfMother();
    }
    
    public String getNomePai() {
        return person.getNameOfFather();
    }

    public String getNumContribuinte() {
        return person.getSocialSecurityNumber();
    }

    public String getNumeroDocumentoIdentificacao() {
        return person.getDocumentIdNumber();
    }

    public String getPassword() {
        return person.getPassword();
    }

    public String getProfissao() {
        return person.getProfession();
    }

    public Gender getSexo() {
        return person.getGender();
    }

    public String getTelefone() {
        return person.getPhone();
    }

    public String getTelemovel() {
        return person.getMobile();
    }

    public IDDocumentType getTipoDocumentoIdentificacao() {
        return person.getIdDocumentType();
    }

    public String getUsername() {
        return person.getUsername();
    }

    public List<InfoAdvisory> getInfoAdvisories() {
	final List<InfoAdvisory> result = new ArrayList<InfoAdvisory>(person.getAdvisoriesCount());
	for (final Advisory advisory : person.getAdvisoriesSet()) {
	    result.add(InfoAdvisory.newInfoFromDomain(advisory));
	}
        return result;
    }

    public Boolean getAvailableEmail() {
        return person.getAvailableEmail();
    }

    public String getWorkPhone() {
        return person.getWorkPhone();
    }

    public Boolean getAvailableWebSite() {
        return person.getAvailableWebSite();
    }

    public Boolean getAvailablePhoto() {
        return person.getAvailablePhoto();
    }

    public InfoExternalPerson getInfoExternalPerson() {
        return InfoExternalPersonWithPersonAndWLocation.newInfoFromDomain(person.getExternalPerson());
    }

    private List<String> getImportantRoles(Person person, List<String> mainRoles) {        

	if(person.getPersonRolesCount() != 0){
            boolean teacher = false, employee = false;

            for(final Role personRole : person.getPersonRolesSet()){
        	
                if(personRole.getRoleType() == RoleType.TEACHER){                        
                    mainRoles.add("Docente");
                    teacher = true;
                    
                } else if(personRole.getRoleType() == RoleType.STUDENT){
                    mainRoles.add("Aluno");
                    
                } else if(personRole.getRoleType() == RoleType.GRANT_OWNER){
                    mainRoles.add("Bolseiro");
                }
                else if(!teacher && personRole.getRoleType() == RoleType.EMPLOYEE){                
                    employee = true;
                }                
            }
            if(employee && !teacher){
                mainRoles.add("Funcionário");
            }
        }
        return mainRoles;
    }

    public static InfoPerson newInfoFromDomain(Person person) {
	return (person != null) ? new InfoPerson(person) : null; 
    }

    public InfoEmployee getInfoEmployee() {
        return InfoEmployeeWithAll.newInfoFromDomain(person.getEmployee());
    }

    public List<InfoStudentCurricularPlan> getInfoStudentCurricularPlanList() {
	final List<InfoStudentCurricularPlan> result = new ArrayList<InfoStudentCurricularPlan>();
	for (final Registration registration : person.getStudentsSet()) {
	    for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
		result.add(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));
	    }
	}
        return result;
    }

    public InfoTeacher getInfoTeacher() {
        return InfoTeacher.newInfoFromDomain(person.getTeacher());
    }

    public List<String> getMainRoles() {
        return getImportantRoles(person, new ArrayList());
    }

    public String getIstUsername() {
	return person.getIstUsername();
    }
    
    @Override
    public Integer getIdInternal() {
        return person.getIdInternal();
    }
    
    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

}