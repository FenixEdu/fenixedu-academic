package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;

public class InfoPersonEditor extends InfoObject {

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

    private InfoCountryEditor infoPais;

    private InfoEmployee infoEmployee;

    private List<InfoStudentCurricularPlan> infoStudentCurricularPlanList;

    private InfoExternalPerson infoExternalPerson;

    private InfoTeacher infoTeacher;

    private List infoAdvisories;

    private Boolean availablePhoto;

    public InfoPersonEditor() {}

    public InfoPersonEditor(String numeroDocumentoIdentificacao, IDDocumentType tipoDocumentoIdentificacao,
	    String localEmissaoDocumentoIdentificacao, Date dataEmissaoDocumentoIdentificacao,
	    Date dataValidadeDocumentoIdentificacao, String nome, Gender sex, MaritalStatus estadoCivil,
	    Date nascimento, String nomePai, String nomeMae, String freguesiaNaturalidade,
	    String concelhoNaturalidade, String distritoNaturalidade, String morada, String localidade,
	    String codigoPostal, String localidadeCodigoPostal, String freguesiaMorada,
	    String concelhoMorada, String distritoMorada, String telefone, String telemovel,
	    String email, String enderecoWeb, String numContribuinte, String profissao, String username,
	    String istUsername, String password, InfoCountryEditor infoPais, String codigoFiscal) {

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

    public String getCodigoFiscal() {
	return codigoFiscal;
    }

    public String getCodigoPostal() {
	return codigoPostal;
    }

    public String getConcelhoMorada() {
	return concelhoMorada;
    }

    public String getConcelhoNaturalidade() {
	return concelhoNaturalidade;
    }

    public Date getDataEmissaoDocumentoIdentificacao() {
	return dataEmissaoDocumentoIdentificacao;
    }

    public Date getDataValidadeDocumentoIdentificacao() {
	return dataValidadeDocumentoIdentificacao;
    }

    public String getDistritoMorada() {
	return distritoMorada;
    }

    public String getDistritoNaturalidade() {
	return distritoNaturalidade;
    }

    public String getEmail() {
	return email;
    }

    public String getEnderecoWeb() {
	return enderecoWeb;
    }

    public MaritalStatus getMaritalStatus() {
	return maritalStatus;
    }

    public String getFreguesiaMorada() {
	return freguesiaMorada;
    }

    public String getFreguesiaNaturalidade() {
	return freguesiaNaturalidade;
    }

    public InfoCountryEditor getInfoPais() {
	return infoPais;
    }

    public String getLocalEmissaoDocumentoIdentificacao() {
	return localEmissaoDocumentoIdentificacao;
    }

    public String getLocalidade() {
	return localidade;
    }

    public String getLocalidadeCodigoPostal() {
	return localidadeCodigoPostal;
    }

    public String getMorada() {
	return morada;
    }

    public String getNacionalidade() {
	return this.getInfoPais().getNationality();
    }

    public Date getNascimento() {
	return nascimento;
    }

    public String getNome() {
	return nome;
    }

    public String getNomeMae() {
	return nomeMae;
    }

    public String getNomePai() {
	return nomePai;
    }

    public String getNumContribuinte() {
	return numContribuinte;
    }

    public String getNumeroDocumentoIdentificacao() {
	return numeroDocumentoIdentificacao;
    }

    public String getPassword() {
	return password;
    }

    public String getProfissao() {
	return profissao;
    }

    /*
     * null gender return MALE
     */
    public Gender getSexo() {
	return gender == Gender.FEMALE ? Gender.FEMALE : Gender.MALE;
    }

    public String getTelefone() {
	return telefone;
    }

    public String getTelemovel() {
	return telemovel;
    }

    public IDDocumentType getTipoDocumentoIdentificacao() {
	return tipoDocumentoIdentificacao;
    }

    public String getUsername() {
	return username;
    }

    public void setCodigoFiscal(String codigoFiscal) {
	this.codigoFiscal = codigoFiscal;
    }

    public void setCodigoPostal(String codigoPostal) {
	this.codigoPostal = codigoPostal;
    }

    public void setConcelhoMorada(String concelhoMorada) {
	this.concelhoMorada = concelhoMorada;
    }

    public void setConcelhoNaturalidade(String concelhoNaturalidade) {
	this.concelhoNaturalidade = concelhoNaturalidade;
    }

    public void setDataEmissaoDocumentoIdentificacao(Date dataEmissaoDocumentoIdentificacao) {
	this.dataEmissaoDocumentoIdentificacao = dataEmissaoDocumentoIdentificacao;
    }

    public void setDataValidadeDocumentoIdentificacao(Date dataValidadeDocumentoIdentificacao) {
	this.dataValidadeDocumentoIdentificacao = dataValidadeDocumentoIdentificacao;
    }

    public void setDistritoMorada(String distritoMorada) {
	this.distritoMorada = distritoMorada;
    }

    public void setDistritoNaturalidade(String distritoNaturalidade) {
	this.distritoNaturalidade = distritoNaturalidade;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public void setEnderecoWeb(String enderecoWeb) {
	this.enderecoWeb = enderecoWeb;
    }

    public void setMaritalStatus(MaritalStatus estadoCivil) {
	this.maritalStatus = estadoCivil;
    }

    public void setFreguesiaMorada(String freguesiaMorada) {
	this.freguesiaMorada = freguesiaMorada;
    }

    public void setFreguesiaNaturalidade(String freguesiaNaturalidade) {
	this.freguesiaNaturalidade = freguesiaNaturalidade;
    }

    public void setInfoPais(InfoCountryEditor infoPais) {
	this.infoPais = infoPais;
    }

    public void setLocalEmissaoDocumentoIdentificacao(String localEmissaoDocumentoIdentificacao) {
	this.localEmissaoDocumentoIdentificacao = localEmissaoDocumentoIdentificacao;
    }

    public void setLocalidade(String localidade) {
	this.localidade = localidade;
    }

    public void setLocalidadeCodigoPostal(String localidadeCodigoPostal) {
	this.localidadeCodigoPostal = localidadeCodigoPostal;
    }

    public void setMorada(String morada) {
	this.morada = morada;
    }

    public void setNascimento(Date nascimento) {
	this.nascimento = nascimento;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public void setNomeMae(String nomeMae) {
	this.nomeMae = nomeMae;
    }

    public void setNomePai(String nomePai) {
	this.nomePai = nomePai;
    }

    public void setNumContribuinte(String numContribuinte) {
	this.numContribuinte = numContribuinte;
    }

    public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao) {
	this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public void setProfissao(String profissao) {
	this.profissao = profissao;
    }

    public void setSexo(Gender sexo) {
	this.gender = sexo;
    }

    public void setTelefone(String telefone) {
	this.telefone = telefone;
    }

    public void setTelemovel(String telemovel) {
	this.telemovel = telemovel;
    }

    public void setTipoDocumentoIdentificacao(IDDocumentType tipoDocumentoIdentificacao) {
	this.tipoDocumentoIdentificacao = tipoDocumentoIdentificacao;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public List getInfoAdvisories() {
	return infoAdvisories;
    }

    public void setInfoAdvisories(List list) {
	infoAdvisories = list;
    }

    public Boolean getAvailableEmail() {
	return availableEmail;
    }

    public void setAvailableEmail(Boolean availableEmail) {
	this.availableEmail = availableEmail;
    }

    public String getWorkPhone() {
	return workPhone;
    }

    public void setWorkPhone(String workPhone) {
	this.workPhone = workPhone;
    }

    public Boolean getAvailableWebSite() {
	return availableWebSite;
    }

    public void setAvailableWebSite(Boolean availableWebSite) {
	this.availableWebSite = availableWebSite;
    }

    public Boolean getAvailablePhoto() {
	return availablePhoto;
    }

    public void setAvailablePhoto(Boolean availablePhoto) {
	this.availablePhoto = availablePhoto;
    }

    public InfoExternalPerson getInfoExternalPerson() {
	return infoExternalPerson;
    }

    public void setInfoExternalPerson(InfoExternalPerson infoExternalPerson) {
	this.infoExternalPerson = infoExternalPerson;
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

    public String getIstUsername() {
	return istUsername;
    }

    public void setIstUsername(String istUsername) {
	this.istUsername = istUsername;
    }
}