package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;

public class Person extends Person_Base {
	private Boolean availableEmail = Boolean.FALSE;
    private Boolean availableWebSite = Boolean.FALSE;
    private Boolean availablePhoto = Boolean.FALSE;

    public Person() {
		super();
        this.setMaritalStatus(MaritalStatus.UNKNOWN);
    }

    public Person(String username) {
        this.setUsername(username);
        this.setMaritalStatus(MaritalStatus.UNKNOWN);
    }

    public Person(IDDocumentType idDocumentType, String userName) {
		this.setIdDocumentType(idDocumentType);
		this.setUsername(userName);
        this.setMaritalStatus(MaritalStatus.UNKNOWN);
    }

    /*
     * Construtor sem país Acrescentado por Fernanda Quitério & Tânia Pousão
     * Devido ao JDBC
     */
    public Person(Integer codigoInterno, String numeroDocumentoIdentificacao,
            IDDocumentType iDDocumentType,
            String localEmissaoDocumentoIdentificacao, Date dataEmissaoDocumentoIdentificacao,
            Date dataValidadeDocumentoIdentificacao, String nome, Gender sex, MaritalStatus estadoCivil,
            Date nascimento, String nomePai, String nomeMae, String nacionalidade,
            String freguesiaNaturalidade, String concelhoNaturalidade, String distritoNaturalidade,
            String morada, String localidade, String codigoPostal, String localidadeCodigoPostal,
            String freguesiaMorada, String concelhoMorada, String distritoMorada, String telefone,
            String telemovel, String email, String enderecoWeb, String numContribuinte,
            String profissao, String username, String password, String codigoFiscal) {
        setIdInternal(codigoInterno);
        setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        setIdDocumentType(iDDocumentType);
        setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
        setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);
        setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);
        setNome(nome);
        setGender(sex);
        setMaritalStatus(estadoCivil);
        setNascimento(nascimento);
        setNomePai(nomePai);
        setNomeMae(nomeMae);
        setNacionalidade(nacionalidade);
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
        setPassword(password);
        setCodigoFiscal(codigoFiscal);
    }

    /*
     * Acrescentado por Fernanda Quitério & Tânia Pousão Devido à aplicacao
     * Assiduidade no usecase Inserir Funcionario
     */
    public Person(String numeroDocumentoIdentificacao, String tipoDocumentoIdentificacao, String nome,
            String username, String password) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(1970, Calendar.JANUARY, 31, 00, 00, 00);

        setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        setIdDocumentType(IDDocumentType.valueOf(tipoDocumentoIdentificacao));
        //setIDDocumentType(new TipoDocumentoIdentificacao(tipoDocumentoIdentificacao));
        setLocalEmissaoDocumentoIdentificacao("");
        setNome(nome);
        setNomePai("");
        setNomeMae("");
        setNacionalidade("");
        setFreguesiaNaturalidade("");
        setConcelhoNaturalidade("");
        setDistritoNaturalidade("");
        setMorada("");
        setLocalidade("");
        setCodigoPostal("");
        setLocalidadeCodigoPostal("");
        setFreguesiaMorada("");
        setConcelhoMorada("");
        setDistritoMorada("");
        setTelefone("");
        setTelemovel("");
        setEmail("");
        setEnderecoWeb("");
        setNumContribuinte("");
        setProfissao("");
        setUsername(username);
        setPassword(password);
        setCodigoFiscal("");
    }

    public Person(String numeroDocumentoIdentificacao,
            IDDocumentType tipoDocumentoIdentificacao,
            String localEmissaoDocumentoIdentificacao, Date dataEmissaoDocumentoIdentificacao,
            Date dataValidadeDocumentoIdentificacao, String nome, Gender sex, MaritalStatus estadoCivil,
            Date nascimento, String nomePai, String nomeMae, String nacionalidade,
            String freguesiaNaturalidade, String concelhoNaturalidade, String distritoNaturalidade,
            String morada, String localidade, String codigoPostal, String localidadeCodigoPostal,
            String freguesiaMorada, String concelhoMorada, String distritoMorada, String telefone,
            String telemovel, String email, String enderecoWeb, String numContribuinte,
            String profissao, String username, String password, ICountry pais, String codigoFiscal) {
        setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        setIdDocumentType(tipoDocumentoIdentificacao);
        setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
        setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);
        setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);
        setNome(nome);
        setGender(sex);
        setMaritalStatus(estadoCivil);
        setNascimento(nascimento);
        setNomePai(nomePai);
        setNomeMae(nomeMae);
        setNacionalidade(nacionalidade);
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
        setPassword(password);
        setPais(pais);
        setCodigoFiscal(codigoFiscal);
    }

    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Person idInternal= ");
        stringBuilder.append(getIdInternal());
        stringBuilder.append(" username= ");
        stringBuilder.append(getUsername());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
    
    public boolean equals(final Object obj) {
        if (obj instanceof IPerson) {
            IPerson person = (IPerson) obj;
            return (((getNumeroDocumentoIdentificacao().equals(person
                    .getNumeroDocumentoIdentificacao())) && (this.getIdDocumentType()
                    .equals(person.getIdDocumentType()))) || (getUsername().equals(person
                    .getUsername())));
        }
        return false;
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
     * @return Returns the availablePhoto.
     */
    public Boolean getAvailablePhoto() {
        return availablePhoto;
    }

    /**
     * @param availablePhoto
     *            The availablePhoto to set.
     */
    public void setAvailablePhoto(Boolean availablePhoto) {
        this.availablePhoto = availablePhoto;
    }

    public String getSlideName() {
        return "/photos/person/P" + getIdInternal();
    }

    public String getSlideNameForCandidateDocuments() {
        return "/candidateDocuments/person/P" + getIdInternal();
    }
}