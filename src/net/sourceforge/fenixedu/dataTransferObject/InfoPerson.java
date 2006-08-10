/*
 * InfoPerson.java
 * 
 * Created on 13 de Dezembro de 2002, 16:13
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

/**
 * @author tfc130
 */

public class InfoPerson extends InfoObject {

    private String numeroDocumentoIdentificacao;

    private String localEmissaoDocumentoIdentificacao;

    private Date dataEmissaoDocumentoIdentificacao;

    private Date dataValidadeDocumentoIdentificacao;

    private String nome;

    private Date nascimento;

    private String nomePai;

    private String nomeMae;

    private String freguesiaNaturalidade;

    private String concelhoNaturalidade;

    private String distritoNaturalidade;

    private String morada;

    private String localidade;

    private String codigoPostal;

    private String localidadeCodigoPostal;

    private String freguesiaMorada;

    private String concelhoMorada;

    private String distritoMorada;

    private String telefone;

    private String telemovel;

    private String workPhone;

    private String email;

    private Boolean availableEmail;

    private String enderecoWeb;

    private Boolean availableWebSite;

    private String numContribuinte;

    private String profissao;

    private String username;
    
    private String istUsername;

    private String password;

    private String codigoFiscal;

    private IDDocumentType tipoDocumentoIdentificacao;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private InfoCountry infoPais;

    private InfoEmployee infoEmployee;

    private List<InfoStudentCurricularPlan> infoStudentCurricularPlanList;

    private InfoExternalPerson infoExternalPerson;

    private InfoTeacher infoTeacher;

    private List infoAdvisories;
    
    private List mainRoles;

    private Boolean availablePhoto;

    /* Construtores */
    public InfoPerson() {
    }

    public InfoPerson(String numeroDocumentoIdentificacao, IDDocumentType tipoDocumentoIdentificacao,
            String localEmissaoDocumentoIdentificacao, Date dataEmissaoDocumentoIdentificacao,
            Date dataValidadeDocumentoIdentificacao, String nome, Gender sex, MaritalStatus estadoCivil,
            Date nascimento, String nomePai, String nomeMae, String freguesiaNaturalidade,
            String concelhoNaturalidade, String distritoNaturalidade, String morada, String localidade,
            String codigoPostal, String localidadeCodigoPostal, String freguesiaMorada,
            String concelhoMorada, String distritoMorada, String telefone, String telemovel,
            String email, String enderecoWeb, String numContribuinte, String profissao, String username, String istUsername,
            String password, InfoCountry infoPais, String codigoFiscal) {

        setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        setTipoDocumentoIdentificacao(tipoDocumentoIdentificacao);
        setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
        setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);
        setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);
        setNome(nome);
        setSexo(sex);
        setMaritalStatus(estadoCivil);
        setNascimento(nascimento);
        setNomePai(nomePai);
        setNomeMae(nomeMae);
        setFreguesiaNaturalidade(freguesiaNaturalidade);
        setConcelhoNaturalidade(concelhoNaturalidade);
        setDistritoNaturalidade(distritoNaturalidade);
        setMorada(morada);
        setLocalidade(localidade);
        setCodigoPostal(codigoPostal);
        setLocalidadeCodigoPostal(localidadeCodigoPostal);
        setFreguesiaMorada(freguesiaMorada);
        setConcelhoMorada(concelhoMorada);
        setDistritoMorada(distritoMorada);
        setTelefone(telefone);
        setTelemovel(telemovel);
        setEmail(email);
        setEnderecoWeb(enderecoWeb);
        setNumContribuinte(numContribuinte);
        setProfissao(profissao);
        setUsername(username);
        setIstUsername(istUsername);
        setPassword(password);
        setInfoPais(infoPais);
        setCodigoFiscal(codigoFiscal);
    }

    public boolean equals(Object o) {
        return ((o instanceof InfoPerson)
                && (numeroDocumentoIdentificacao.equals(((InfoPerson) o)
                        .getNumeroDocumentoIdentificacao())) && (tipoDocumentoIdentificacao
                .equals(((InfoPerson) o).getTipoDocumentoIdentificacao())));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "Person :\n";
        result += "\n  - Identification Document Number : " + numeroDocumentoIdentificacao;
        result += "\n  - Identification Document Type : " + tipoDocumentoIdentificacao;
        result += "\n  - Identification Document Issue Place : " + localEmissaoDocumentoIdentificacao;
        result += "\n  - Identification Document Issue Date : " + dataEmissaoDocumentoIdentificacao;
        result += "\n  - Identification Document Expiration Date : "
                + dataValidadeDocumentoIdentificacao;
        result += "\n  - Name : " + nome;
        result += "\n  - Birth : " + nascimento;
        result += "\n  - Father Name : " + nomePai;
        result += "\n  - Mother Name : " + nomeMae;
        result += "\n  - Nationality : " + getInfoPais().getNationality();
        result += "\n  - Birth Place Parish : " + freguesiaNaturalidade;
        result += "\n  - Birth Place Municipality : " + concelhoNaturalidade;
        result += "\n  - Birth Place District : " + distritoNaturalidade;
        result += "\n  - Address : " + morada;
        result += "\n  - Place : " + localidade;
        result += "\n  - Post Code : " + codigoPostal;
        result += "\n  - Address Parish : " + freguesiaMorada;
        result += "\n  - Address Municipality : " + concelhoMorada;
        result += "\n  - Address District : " + distritoMorada;
        result += "\n  - Telephone : " + telefone;
        result += "\n  - MobilePhone : " + telemovel;
        result += "\n  - E-Mail : " + email;
        result += "\n  - HomePage : " + enderecoWeb;
        result += "\n  - Contributor Number : " + numContribuinte;
        result += "\n  - Username : " + username;
        result += "\n  - Password : " + password;
        result += "\n  - Occupation : " + profissao;
        result += "\n  - Codigo Fiscal : " + codigoFiscal;
        result += "\n  - studentList : " + infoStudentCurricularPlanList;
        return result;
    }

    /**
     * @return String
     */
    public String getCodigoFiscal() {
        return codigoFiscal;
    }

    /**
     * @return String
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * @return String
     */
    public String getConcelhoMorada() {
        return concelhoMorada;
    }

    /**
     * @return String
     */
    public String getConcelhoNaturalidade() {
        return concelhoNaturalidade;
    }

    /**
     * @return Date
     */
    public Date getDataEmissaoDocumentoIdentificacao() {
        return dataEmissaoDocumentoIdentificacao;
    }

    /**
     * @return Date
     */
    public Date getDataValidadeDocumentoIdentificacao() {
        return dataValidadeDocumentoIdentificacao;
    }

    /**
     * @return String
     */
    public String getDistritoMorada() {
        return distritoMorada;
    }

    /**
     * @return String
     */
    public String getDistritoNaturalidade() {
        return distritoNaturalidade;
    }

    /**
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return String
     */
    public String getEnderecoWeb() {
        return enderecoWeb;
    }

    /**
     * @return MaritalStatus
     */
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @return String
     */
    public String getFreguesiaMorada() {
        return freguesiaMorada;
    }

    /**
     * @return String
     */
    public String getFreguesiaNaturalidade() {
        return freguesiaNaturalidade;
    }

    /**
     * @return InfoCountry
     */
    public InfoCountry getInfoPais() {
        return infoPais;
    }

    /**
     * @return String
     */
    public String getLocalEmissaoDocumentoIdentificacao() {
        return localEmissaoDocumentoIdentificacao;
    }

    /**
     * @return String
     */
    public String getLocalidade() {
        return localidade;
    }

    /**
     * @return String
     */
    public String getLocalidadeCodigoPostal() {
        return localidadeCodigoPostal;
    }

    /**
     * @return String
     */
    public String getMorada() {
        return morada;
    }

    /**
     * @return String
     */
    public String getNacionalidade() {
        return this.getInfoPais().getNationality();
    }

    /**
     * @return Date
     */
    public Date getNascimento() {
        return nascimento;
    }

    /**
     * @return String
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return String
     */
    public String getNomeMae() {
        return nomeMae;
    }

    /**
     * @return String
     */
    public String getNomePai() {
        return nomePai;
    }

    /**
     * @return String
     */
    public String getNumContribuinte() {
        return numContribuinte;
    }

    /**
     * @return String
     */
    public String getNumeroDocumentoIdentificacao() {
        return numeroDocumentoIdentificacao;
    }

    /**
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return String
     */
    public String getProfissao() {
        return profissao;
    }

    public Gender getSexo() {
        return gender == Gender.MALE ? Gender.MALE : Gender.FEMALE;
    }

    /**
     * @return String
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @return String
     */
    public String getTelemovel() {
        return telemovel;
    }

    /**
     * @return TipoDocumentoIdentificacao
     */
    public IDDocumentType getTipoDocumentoIdentificacao() {
        return tipoDocumentoIdentificacao;
    }

    /**
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the codigoFiscal.
     * 
     * @param codigoFiscal
     *            The codigoFiscal to set
     */
    public void setCodigoFiscal(String codigoFiscal) {
        this.codigoFiscal = codigoFiscal;
    }

    /**
     * Sets the codigoPostal.
     * 
     * @param codigoPostal
     *            The codigoPostal to set
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Sets the concelhoMorada.
     * 
     * @param concelhoMorada
     *            The concelhoMorada to set
     */
    public void setConcelhoMorada(String concelhoMorada) {
        this.concelhoMorada = concelhoMorada;
    }

    /**
     * Sets the concelhoNaturalidade.
     * 
     * @param concelhoNaturalidade
     *            The concelhoNaturalidade to set
     */
    public void setConcelhoNaturalidade(String concelhoNaturalidade) {
        this.concelhoNaturalidade = concelhoNaturalidade;
    }

    /**
     * Sets the dataEmissaoDocumentoIdentificacao.
     * 
     * @param dataEmissaoDocumentoIdentificacao
     *            The dataEmissaoDocumentoIdentificacao to set
     */
    public void setDataEmissaoDocumentoIdentificacao(Date dataEmissaoDocumentoIdentificacao) {
        this.dataEmissaoDocumentoIdentificacao = dataEmissaoDocumentoIdentificacao;
    }

    /**
     * Sets the dataValidadeDocumentoIdentificacao.
     * 
     * @param dataValidadeDocumentoIdentificacao
     *            The dataValidadeDocumentoIdentificacao to set
     */
    public void setDataValidadeDocumentoIdentificacao(Date dataValidadeDocumentoIdentificacao) {
        this.dataValidadeDocumentoIdentificacao = dataValidadeDocumentoIdentificacao;
    }

    /**
     * Sets the distritoMorada.
     * 
     * @param distritoMorada
     *            The distritoMorada to set
     */
    public void setDistritoMorada(String distritoMorada) {
        this.distritoMorada = distritoMorada;
    }

    /**
     * Sets the distritoNaturalidade.
     * 
     * @param distritoNaturalidade
     *            The distritoNaturalidade to set
     */
    public void setDistritoNaturalidade(String distritoNaturalidade) {
        this.distritoNaturalidade = distritoNaturalidade;
    }

    /**
     * Sets the email.
     * 
     * @param email
     *            The email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the enderecoWeb.
     * 
     * @param enderecoWeb
     *            The enderecoWeb to set
     */
    public void setEnderecoWeb(String enderecoWeb) {
        this.enderecoWeb = enderecoWeb;
    }

    /**
     * Sets the estadoCivil.
     * 
     * @param estadoCivil
     *            The estadoCivil to set
     */
    public void setMaritalStatus(MaritalStatus estadoCivil) {
        this.maritalStatus = estadoCivil;
    }

    /**
     * Sets the freguesiaMorada.
     * 
     * @param freguesiaMorada
     *            The freguesiaMorada to set
     */
    public void setFreguesiaMorada(String freguesiaMorada) {
        this.freguesiaMorada = freguesiaMorada;
    }

    /**
     * Sets the freguesiaNaturalidade.
     * 
     * @param freguesiaNaturalidade
     *            The freguesiaNaturalidade to set
     */
    public void setFreguesiaNaturalidade(String freguesiaNaturalidade) {
        this.freguesiaNaturalidade = freguesiaNaturalidade;
    }

    /**
     * Sets the infoPais.
     * 
     * @param infoPais
     *            The infoPais to set
     */
    public void setInfoPais(InfoCountry infoPais) {
        this.infoPais = infoPais;
    }

    /**
     * Sets the localEmissaoDocumentoIdentificacao.
     * 
     * @param localEmissaoDocumentoIdentificacao
     *            The localEmissaoDocumentoIdentificacao to set
     */
    public void setLocalEmissaoDocumentoIdentificacao(String localEmissaoDocumentoIdentificacao) {
        this.localEmissaoDocumentoIdentificacao = localEmissaoDocumentoIdentificacao;
    }

    /**
     * Sets the localidade.
     * 
     * @param localidade
     *            The localidade to set
     */
    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    /**
     * Sets the localidadeCodigoPostal.
     * 
     * @param localidadeCodigoPostal
     *            The localidadeCodigoPostal to set
     */
    public void setLocalidadeCodigoPostal(String localidadeCodigoPostal) {
        this.localidadeCodigoPostal = localidadeCodigoPostal;
    }

    /**
     * Sets the morada.
     * 
     * @param morada
     *            The morada to set
     */
    public void setMorada(String morada) {
        this.morada = morada;
    }

    /**
     * Sets the nascimento.
     * 
     * @param nascimento
     *            The nascimento to set
     */
    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    /**
     * Sets the nome.
     * 
     * @param nome
     *            The nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Sets the nomeMae.
     * 
     * @param nomeMae
     *            The nomeMae to set
     */
    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    /**
     * Sets the nomePai.
     * 
     * @param nomePai
     *            The nomePai to set
     */
    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    /**
     * Sets the numContribuinte.
     * 
     * @param numContribuinte
     *            The numContribuinte to set
     */
    public void setNumContribuinte(String numContribuinte) {
        this.numContribuinte = numContribuinte;
    }

    /**
     * Sets the numeroDocumentoIdentificacao.
     * 
     * @param numeroDocumentoIdentificacao
     *            The numeroDocumentoIdentificacao to set
     */
    public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao) {
        this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }

    /**
     * Sets the password.
     * 
     * @param password
     *            The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the profissao.
     * 
     * @param profissao
     *            The profissao to set
     */
    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    /**
     * Sets the sexo.
     * 
     * @param sexo
     *            The sexo to set
     */
    public void setSexo(Gender sexo) {
        if (sexo == null) {
            this.gender = null;
        } else if (sexo.equals(Gender.MALE)) {
            this.gender = Gender.MALE;
        } else if (sexo.equals(Gender.FEMALE)) {
            this.gender = Gender.FEMALE;
        }
    }

    /**
     * Sets the telefone.
     * 
     * @param telefone
     *            The telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Sets the telemovel.
     * 
     * @param telemovel
     *            The telemovel to set
     */
    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
    }

    /**
     * Sets the tipoDocumentoIdentificacao.
     * 
     * @param tipoDocumentoIdentificacao
     *            The tipoDocumentoIdentificacao to set
     */
    public void setTipoDocumentoIdentificacao(IDDocumentType tipoDocumentoIdentificacao) {
        this.tipoDocumentoIdentificacao = tipoDocumentoIdentificacao;
    }

    /**
     * Sets the username.
     * 
     * @param username
     *            The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return
     */
    public List getInfoAdvisories() {
        return infoAdvisories;
    }

    /**
     * @param list
     */
    public void setInfoAdvisories(List list) {
        infoAdvisories = list;
    }

    /**
     * @return Returns the availableEmail.
     */
    public Boolean getAvailableEmail() {
        return availableEmail;
    }

    /**
     * @param availableEmail
     *            The availableEmail to set.
     */
    public void setAvailableEmail(Boolean availableEmail) {
        this.availableEmail = availableEmail;
    }

    /**
     * @return Returns the workPhone.
     */
    public String getWorkPhone() {
        return workPhone;
    }

    /**
     * @param workPhone
     *            The workPhone to set.
     */
    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    /**
     * @return Returns the availableHomepage.
     */
    public Boolean getAvailableWebSite() {
        return availableWebSite;
    }

    /**
     * @param availableHomepage
     *            The availableHomepage to set.
     */
    public void setAvailableWebSite(Boolean availableWebSite) {
        this.availableWebSite = availableWebSite;
    }

    /**
     * @return Returns the availableHomepage.
     */
    public Boolean getAvailablePhoto() {
        return availablePhoto;
    }

    /**
     * @param availableHomepage
     *            The availableHomepage to set.
     */
    public void setAvailablePhoto(Boolean availablePhoto) {
        this.availablePhoto = availablePhoto;
    }

    public InfoExternalPerson getInfoExternalPerson() {
        return infoExternalPerson;
    }

    public void setInfoExternalPerson(InfoExternalPerson infoExternalPerson) {
        this.infoExternalPerson = infoExternalPerson;
    }

    public void copyFromDomain(Person person) {
        super.copyFromDomain(person);
        if (person != null) {
            setInfoPais(InfoCountry.newInfoFromDomain(person.getPais()));

            setNome(person.getNome());
            setNumeroDocumentoIdentificacao(person.getNumeroDocumentoIdentificacao());
            setTipoDocumentoIdentificacao(person.getIdDocumentType());
            setLocalEmissaoDocumentoIdentificacao(person.getLocalEmissaoDocumentoIdentificacao());
            setDataEmissaoDocumentoIdentificacao(person.getDataEmissaoDocumentoIdentificacao());
            setDataValidadeDocumentoIdentificacao(person.getDataValidadeDocumentoIdentificacao());

            setSexo(person.getGender());
            setMaritalStatus(person.getMaritalStatus());

            setEmail(person.getEmail());
            setAvailableEmail(person.getAvailableEmail());
            setEnderecoWeb(person.getEnderecoWeb());
            setAvailableWebSite(person.getAvailableWebSite());
            setTelefone(person.getTelefone());
            setTelemovel(person.getTelemovel());
            setWorkPhone(person.getWorkPhone());

            setProfissao(person.getProfissao());

            setMorada(person.getMorada());
            setCodigoPostal(person.getCodigoPostal());
            setLocalidade(person.getLocalidade());
            setFreguesiaMorada(person.getFreguesiaMorada());
            setConcelhoMorada(person.getConcelhoMorada());
            setDistritoMorada(person.getDistritoMorada());

            setConcelhoNaturalidade(person.getConcelhoNaturalidade());
            setDistritoNaturalidade(person.getDistritoNaturalidade());
            setFreguesiaNaturalidade(person.getFreguesiaNaturalidade());

            setLocalidadeCodigoPostal(person.getLocalidadeCodigoPostal());

            setNascimento(person.getNascimento());
            setNomeMae(person.getNomeMae());
            setNomePai(person.getNomePai());

            setNumContribuinte(person.getNumContribuinte());
            setCodigoFiscal(person.getCodigoFiscal());

            setPassword(person.getPassword());
            setUsername(person.getUsername());
            setIstUsername(person.getIstUsername());

            if (person.getExternalPerson() != null) {
                InfoExternalPerson infoExternalPerson = new InfoExternalPerson();
                infoExternalPerson.setIdInternal(person.getExternalPerson().getIdInternal());
                infoExternalPerson.setInfoPerson(this);
                infoExternalPerson.setInfoInstitution(InfoInstitution.newInfoFromDomain(person
                        .getExternalPerson().getInstitutionUnit()));
                setInfoExternalPerson(infoExternalPerson);
            }
            if (person.getTeacher() != null) {
                InfoTeacher infoTeacher = new InfoTeacher();
                infoTeacher.setIdInternal(person.getTeacher().getIdInternal());
                infoTeacher.setTeacherNumber(person.getTeacher().getTeacherNumber());
                infoTeacher.setInfoCategory(InfoCategory.newInfoFromDomain(person.getTeacher()
                        .getCategory()));
                setInfoTeacher(infoTeacher);
            }
            if (person.getEmployee() != null) {
                InfoEmployee infoEmployee = new InfoEmployee();
                infoEmployee.setIdInternal(person.getEmployee().getIdInternal());
                infoEmployee.setEmployeeNumber(person.getEmployee().getEmployeeNumber());

                if (person.getEmployee().getCurrentContract() != null
                        && person.getEmployee().getCurrentContract().getWorkingUnit() != null) {
                    infoEmployee.setWorkingUnit(InfoUnit.newInfoFromDomain(person.getEmployee()
                            .getCurrentContract().getWorkingUnit()));
                }
                if (person.getEmployee().getCurrentContract() != null
                        && person.getEmployee().getCurrentContract().getMailingUnit() != null) {
                    infoEmployee.setMailingUnit(InfoUnit.newInfoFromDomain(person.getEmployee()
                            .getCurrentContract().getMailingUnit()));
                }

                setInfoEmployee(infoEmployee);
            }

            if (person.getStudentsCount() != 0) {

                List<InfoStudentCurricularPlan> infoStudentList = new ArrayList<InfoStudentCurricularPlan>();

                for (Registration student : person.getStudents()) {

                    InfoStudent infoStudent = new InfoStudent();
                    InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
                    InfoDegree infoDegree = new InfoDegree();
                    for (StudentCurricularPlan studentCurricularPlan : student
                            .getStudentCurricularPlans()) {

                        if ((!studentCurricularPlan.getCurrentState().equals(
                                StudentCurricularPlanState.ACTIVE))
                                && (!studentCurricularPlan.getCurrentState().equals(
                                        StudentCurricularPlanState.SCHOOLPARTCONCLUDED))) {

                            continue;
                        }
                        infoStudentCurricularPlan = InfoStudentCurricularPlan
                                .newInfoFromDomain(studentCurricularPlan);

                        infoDegree = InfoDegree.newInfoFromDomain(studentCurricularPlan
                                .getDegreeCurricularPlan().getDegree());
                        infoDegree.setNome(studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                                .getNome());
                        infoDegree.setSigla(studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                                .getSigla());

                        infoStudentCurricularPlan.getInfoDegreeCurricularPlan()
                                .setInfoDegree(infoDegree);
                        infoStudent.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

                        infoStudentList.add(infoStudent.getInfoStudentCurricularPlan());

                    }
                    setInfoStudentCurricularPlanList(infoStudentList);
                }

            }

            setAvailablePhoto(person.getAvailablePhoto());

            setInfoPais(InfoCountry.newInfoFromDomain(person.getPais()));

            setMainRoles(getImportantRoles(person, new ArrayList()));
        }

    }
    
    private List getImportantRoles(Person person, List mainRoles) {        

        if(person.getPersonRolesCount() != 0){
            
            boolean teacher = false, employee = false;
        
            for(Role personRole : person.getPersonRoles()){                    
                RoleType roleType = personRole.getRoleType();
                
                if(roleType.equals(RoleType.TEACHER)){                        
                    mainRoles.add("Docente");
                    teacher = true;
                }
                else if(roleType.equals(RoleType.STUDENT)){
                    mainRoles.add("Aluno");
                }
                else if(roleType.equals(RoleType.GRANT_OWNER)){
                    mainRoles.add("Bolseiro");
                }
                else if(!teacher && roleType.equals(RoleType.EMPLOYEE)){                
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
        InfoPerson infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPerson();
            infoPerson.copyFromDomain(person);
        }
        return infoPerson;
    }

    public InfoEmployee getInfoEmployee() {
        return infoEmployee;
    }

    public void setInfoEmployee(InfoEmployee infoEmployee) {
        this.infoEmployee = infoEmployee;
    }

    public List<InfoStudentCurricularPlan> getInfoStudentCurricularPlanList() {
        return infoStudentCurricularPlanList;
    }

    public void setInfoStudentCurricularPlanList(
            List<InfoStudentCurricularPlan> infoStudentCurricularPlanList) {
        this.infoStudentCurricularPlanList = infoStudentCurricularPlanList;
    }

    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    public List getMainRoles() {
        return mainRoles;
    }

    public void setMainRoles(List mainRoles) {
        this.mainRoles = mainRoles;
    }

	public String getIstUsername() {
		return istUsername;
	}

	public void setIstUsername(String istUsername) {
		this.istUsername = istUsername;
	}
}