package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;

public class Person extends Person_Base {

    public Person() {
		super();
        this.setMaritalStatus(MaritalStatus.UNKNOWN);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
    }

    public Person(String username) {
        this.setUsername(username);
        this.setMaritalStatus(MaritalStatus.UNKNOWN);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
    }

    public Person(IDDocumentType idDocumentType, String userName) {
		this.setIdDocumentType(idDocumentType);
		this.setUsername(userName);
        this.setMaritalStatus(MaritalStatus.UNKNOWN);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
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
        this.setIdInternal(codigoInterno);
        this.setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        this.setIdDocumentType(iDDocumentType);
        this.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
        this.setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);
        this.setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);
        this.setNome(nome);
        this.setGender(sex);
        this.setMaritalStatus(estadoCivil);
        this.setNascimento(nascimento);
        this.setNomePai(nomePai);
        this.setNomeMae(nomeMae);
        this.setNacionalidade(nacionalidade);
        this.setFreguesiaNaturalidade(freguesiaNaturalidade);
        this.setConcelhoNaturalidade(concelhoNaturalidade);
        this.setDistritoNaturalidade(distritoNaturalidade);
        this.setMorada(morada);
        this.setLocalidade(localidade);
        this.setCodigoPostal(codigoPostal);
        this.setLocalidadeCodigoPostal(localidadeCodigoPostal);
        this.setFreguesiaMorada(freguesiaMorada);
        this.setConcelhoMorada(concelhoMorada);
        this.setDistritoMorada(distritoMorada);
        this.setTelefone(telefone);
        this.setTelemovel(telemovel);
        this.setEmail(email);
        this.setEnderecoWeb(enderecoWeb);
        this.setNumContribuinte(numContribuinte);
        this.setProfissao(profissao);
        this.setUsername(username);
        this.setPassword(password);
        this.setCodigoFiscal(codigoFiscal);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
    }

    /*
     * Acrescentado por Fernanda Quitério & Tânia Pousão Devido à aplicacao
     * Assiduidade no usecase Inserir Funcionario
     */
    public Person(String numeroDocumentoIdentificacao, String tipoDocumentoIdentificacao, String nome,
            String username, String password) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(1970, Calendar.JANUARY, 31, 00, 00, 00);

        this.setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        this.setIdDocumentType(IDDocumentType.valueOf(tipoDocumentoIdentificacao));
        this.setLocalEmissaoDocumentoIdentificacao("");
        this.setNome(nome);
        this.setNomePai("");
        this.setNomeMae("");
        this.setNacionalidade("");
        this.setFreguesiaNaturalidade("");
        this.setConcelhoNaturalidade("");
        this.setDistritoNaturalidade("");
        this.setMorada("");
        this.setLocalidade("");
        this.setCodigoPostal("");
        this.setLocalidadeCodigoPostal("");
        this.setFreguesiaMorada("");
        this.setConcelhoMorada("");
        this.setDistritoMorada("");
        this.setTelefone("");
        this.setTelemovel("");
        this.setEmail("");
        this.setEnderecoWeb("");
        this.setNumContribuinte("");
        this.setProfissao("");
        this.setUsername(username);
        this.setPassword(password);
        this.setCodigoFiscal("");
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
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
        this.setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        this.setIdDocumentType(tipoDocumentoIdentificacao);
        this.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
        this.setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);
        this.setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);
        this.setNome(nome);
        this.setGender(sex);
        this.setMaritalStatus(estadoCivil);
        this.setNascimento(nascimento);
        this.setNomePai(nomePai);
        this.setNomeMae(nomeMae);
        this.setNacionalidade(nacionalidade);
        this.setFreguesiaNaturalidade(freguesiaNaturalidade);
        this.setConcelhoNaturalidade(concelhoNaturalidade);
        this.setDistritoNaturalidade(distritoNaturalidade);
        this.setMorada(morada);
        this.setLocalidade(localidade);
        this.setCodigoPostal(codigoPostal);
        this.setLocalidadeCodigoPostal(localidadeCodigoPostal);
        this.setFreguesiaMorada(freguesiaMorada);
        this.setConcelhoMorada(concelhoMorada);
        this.setDistritoMorada(distritoMorada);
        this.setTelefone(telefone);
        this.setTelemovel(telemovel);
        this.setEmail(email);
        this.setEnderecoWeb(enderecoWeb);
        this.setNumContribuinte(numContribuinte);
        this.setProfissao(profissao);
        this.setUsername(username);
        this.setPassword(password);
        this.setPais(pais);
        this.setCodigoFiscal(codigoFiscal);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
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

    public String getSlideName() {
        return "/photos/person/P" + getIdInternal();
    }

    public String getSlideNameForCandidateDocuments() {
        return "/candidateDocuments/person/P" + getIdInternal();
    }
}