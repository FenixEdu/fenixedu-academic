package Dominio;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

public class Pessoa extends DomainObject implements IPessoa {
    private Boolean availableEmail = Boolean.FALSE;

    private Boolean availableWebSite = Boolean.FALSE;

    private Integer chavePais;

    private String codigoFiscal;

    private String codigoPostal;

    private String concelhoMorada;

    private String concelhoNaturalidade;

    private Date dataEmissaoDocumentoIdentificacao;

    private Date dataValidadeDocumentoIdentificacao;

    private String distritoMorada;

    private String distritoNaturalidade;

    private String email;

    private String enderecoWeb;

    private EstadoCivil estadoCivil;

    private String freguesiaMorada;

    private String freguesiaNaturalidade;

    private String localEmissaoDocumentoIdentificacao;

    private String localidade;

    private String localidadeCodigoPostal;

    private String morada;

    private String nacionalidade;

    private Date nascimento;

    private String nome;

    private String nomeMae;

    private String nomePai;

    private String numContribuinte;

    private String numeroDocumentoIdentificacao;

    private ICountry pais;

    private String password;

    private Collection personRoles;

    private String profissao;

    private Sexo sexo;

    private String telefone;

    private String telemovel;

    private TipoDocumentoIdentificacao tipoDocumentoIdentificacao;

    private String username;

    private String workPhone;

    private List manageableDepartmentCredits;

    private List advisories;

    private Boolean availablePhoto = Boolean.FALSE;

    /**
     * @return
     */
    public List getManageableDepartmentCredits() {
        return this.manageableDepartmentCredits;
    }

    /**
     * @param manageableDepartmentCredits
     */
    public void setManageableDepartmentCredits(List manageableDepartmentCredits) {
        this.manageableDepartmentCredits = manageableDepartmentCredits;
    }

    /* Construtores */

    public Pessoa() {
        //		this.numeroDocumentoIdentificacao = "";
        //		this.tipoDocumentoIdentificacao = null;
        //		this.localEmissaoDocumentoIdentificacao = "";
        //		this.dataEmissaoDocumentoIdentificacao = null;
        //		this.dataValidadeDocumentoIdentificacao = null;
        //		this.nome = "";
        //		this.sexo = null;
        //		this.estadoCivil = null;
        //		this.nascimento = null;
        //		this.nomePai = "";
        //		this.nomeMae = "";
        //		this.nacionalidade = "";
        //		this.freguesiaNaturalidade = "";
        //		this.concelhoNaturalidade = "";
        //		this.distritoNaturalidade = "";
        //		this.morada = "";
        //		this.localidade = "";
        //		this.codigoPostal = "";
        //		this.localidadeCodigoPostal = "";
        //		this.freguesiaMorada = "";
        //		this.concelhoMorada = "";
        //		this.distritoMorada = "";
        //		this.telefone = "";
        //		this.telemovel = "";
        //		this.email = "";
        //		this.enderecoWeb = "";
        //		this.numContribuinte = "";
        //		this.profissao = "";
        //		this.username = "";
        //		this.pais = null;
        //		this.codigoFiscal = "";
    }

    public Pessoa(String username) {
        setUsername(username);
    }

    public Pessoa(TipoDocumentoIdentificacao idDocumentType, String userName) {
        //		this.numeroDocumentoIdentificacao = "";
        this.tipoDocumentoIdentificacao = idDocumentType;
        this.username = userName;
        //		this.localEmissaoDocumentoIdentificacao = "";
        //		this.dataEmissaoDocumentoIdentificacao = null;
        //		this.dataValidadeDocumentoIdentificacao = null;
        //		this.nome = "";
        //		this.sexo = null;
        //		this.estadoCivil = null;
        //		this.nascimento = null;
        //		this.nomePai = "";
        //		this.nomeMae = "";
        //		this.nacionalidade = "";
        //		this.freguesiaNaturalidade = "";
        //		this.concelhoNaturalidade = "";
        //		this.distritoNaturalidade = "";
        //		this.morada = "";
        //		this.localidade = "";
        //		this.codigoPostal = "";
        //		this.localidadeCodigoPostal = "";
        //		this.freguesiaMorada = "";
        //		this.concelhoMorada = "";
        //		this.distritoMorada = "";
        //		this.telefone = "";
        //		this.telemovel = "";
        //		this.email = "";
        //		this.enderecoWeb = "";
        //		this.numContribuinte = "";
        //		this.profissao = "";
        //		this.username = userName;
        //		this.pais = null;
        //		this.codigoFiscal = "";
    }

    /*
     * Construtor sem país Acrescentado por Fernanda Quitério & Tânia Pousão
     * Devido ao JDBC
     */
    public Pessoa(Integer codigoInterno, String numeroDocumentoIdentificacao,
            TipoDocumentoIdentificacao tipoDocumentoIdentificacao,
            String localEmissaoDocumentoIdentificacao, Date dataEmissaoDocumentoIdentificacao,
            Date dataValidadeDocumentoIdentificacao, String nome, Sexo sexo, EstadoCivil estadoCivil,
            Date nascimento, String nomePai, String nomeMae, String nacionalidade,
            String freguesiaNaturalidade, String concelhoNaturalidade, String distritoNaturalidade,
            String morada, String localidade, String codigoPostal, String localidadeCodigoPostal,
            String freguesiaMorada, String concelhoMorada, String distritoMorada, String telefone,
            String telemovel, String email, String enderecoWeb, String numContribuinte,
            String profissao, String username, String password, String codigoFiscal) {
        setIdInternal(codigoInterno);
        setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        setTipoDocumentoIdentificacao(tipoDocumentoIdentificacao);
        setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
        setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);
        setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);
        setNome(nome);
        setSexo(sexo);
        setEstadoCivil(estadoCivil);
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
    public Pessoa(String numeroDocumentoIdentificacao, int tipoDocumentoIdentificacao, String nome,
            String username, String password) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(1970, Calendar.JANUARY, 31, 00, 00, 00);

        setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(tipoDocumentoIdentificacao));
        this.localEmissaoDocumentoIdentificacao = "";
        this.dataEmissaoDocumentoIdentificacao = null;
        this.dataValidadeDocumentoIdentificacao = null;
        setNome(nome);
        this.sexo = null;
        this.estadoCivil = null;
        this.nascimento = null;
        this.nomePai = "";
        this.nomeMae = "";
        this.nacionalidade = "";
        this.freguesiaNaturalidade = "";
        this.concelhoNaturalidade = "";
        this.distritoNaturalidade = "";
        this.morada = "";
        this.localidade = "";
        this.codigoPostal = "";
        this.localidadeCodigoPostal = "";
        this.freguesiaMorada = "";
        this.concelhoMorada = "";
        this.distritoMorada = "";
        this.telefone = "";
        this.telemovel = "";
        this.email = "";
        this.enderecoWeb = "";
        this.numContribuinte = "";
        this.profissao = "";
        setUsername(username);
        setPassword(password);
        this.pais = null;
        this.codigoFiscal = "";
    }

    public Pessoa(String numeroDocumentoIdentificacao,
            TipoDocumentoIdentificacao tipoDocumentoIdentificacao,
            String localEmissaoDocumentoIdentificacao, Date dataEmissaoDocumentoIdentificacao,
            Date dataValidadeDocumentoIdentificacao, String nome, Sexo sexo, EstadoCivil estadoCivil,
            Date nascimento, String nomePai, String nomeMae, String nacionalidade,
            String freguesiaNaturalidade, String concelhoNaturalidade, String distritoNaturalidade,
            String morada, String localidade, String codigoPostal, String localidadeCodigoPostal,
            String freguesiaMorada, String concelhoMorada, String distritoMorada, String telefone,
            String telemovel, String email, String enderecoWeb, String numContribuinte,
            String profissao, String username, String password, ICountry pais, String codigoFiscal) {
        setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        setTipoDocumentoIdentificacao(tipoDocumentoIdentificacao);
        setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
        setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);
        setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);
        setNome(nome);
        setSexo(sexo);
        setEstadoCivil(estadoCivil);
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

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IPessoa) {
            IPessoa person = (IPessoa) obj;
            resultado = (((this.numeroDocumentoIdentificacao.equals(person
                    .getNumeroDocumentoIdentificacao())) && (this.tipoDocumentoIdentificacao
                    .equals(person.getTipoDocumentoIdentificacao()))) || (this.username.equals(person
                    .getUsername())));
        }
        return resultado;
    }

    /**
     * Getter for property chavePais.
     * 
     * @return Value of property chavePais.
     *  
     */
    public java.lang.Integer getChavePais() {
        return chavePais;
    }

    /**
     * Getter for property codigoFiscal.
     * 
     * @return Value of property codigoFiscal.
     *  
     */
    public java.lang.String getCodigoFiscal() {
        return codigoFiscal;
    }

    /**
     * Getter for property codigoPostal.
     * 
     * @return Value of property codigoPostal.
     *  
     */
    public java.lang.String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Getter for property concelhoMorada.
     * 
     * @return Value of property concelhoMorada.
     *  
     */
    public java.lang.String getConcelhoMorada() {
        return concelhoMorada;
    }

    /**
     * Getter for property concelhoNaturalidade.
     * 
     * @return Value of property concelhoNaturalidade.
     *  
     */
    public java.lang.String getConcelhoNaturalidade() {
        return concelhoNaturalidade;
    }

    /**
     * Getter for property dataEmissaoDocumentoIdentificacao.
     * 
     * @return Value of property dataEmissaoDocumentoIdentificacao.
     *  
     */
    public java.util.Date getDataEmissaoDocumentoIdentificacao() {
        return dataEmissaoDocumentoIdentificacao;
    }

    /**
     * Getter for property dataValidadeDocumentoIdentificacao.
     * 
     * @return Value of property dataValidadeDocumentoIdentificacao.
     *  
     */
    public java.util.Date getDataValidadeDocumentoIdentificacao() {
        return dataValidadeDocumentoIdentificacao;
    }

    /**
     * Getter for property distritoMorada.
     * 
     * @return Value of property distritoMorada.
     *  
     */
    public java.lang.String getDistritoMorada() {
        return distritoMorada;
    }

    /**
     * Getter for property distritoNaturalidade.
     * 
     * @return Value of property distritoNaturalidade.
     *  
     */
    public java.lang.String getDistritoNaturalidade() {
        return distritoNaturalidade;
    }

    /**
     * Getter for property email.
     * 
     * @return Value of property email.
     *  
     */
    public java.lang.String getEmail() {
        return email;
    }

    /**
     * Getter for property enderecoWeb.
     * 
     * @return Value of property enderecoWeb.
     *  
     */
    public java.lang.String getEnderecoWeb() {
        return enderecoWeb;
    }

    /**
     * Getter for property estadoCivil.
     * 
     * @return Value of property estadoCivil.
     *  
     */
    public Util.EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    /**
     * Getter for property freguesiaMorada.
     * 
     * @return Value of property freguesiaMorada.
     *  
     */
    public java.lang.String getFreguesiaMorada() {
        return freguesiaMorada;
    }

    /**
     * Getter for property freguesiaNaturalidade.
     * 
     * @return Value of property freguesiaNaturalidade.
     *  
     */
    public java.lang.String getFreguesiaNaturalidade() {
        return freguesiaNaturalidade;
    }

    /**
     * Getter for property localEmissaoDocumentoIdentificacao.
     * 
     * @return Value of property localEmissaoDocumentoIdentificacao.
     *  
     */
    public java.lang.String getLocalEmissaoDocumentoIdentificacao() {
        return localEmissaoDocumentoIdentificacao;
    }

    /**
     * Getter for property localidade.
     * 
     * @return Value of property localidade.
     *  
     */
    public java.lang.String getLocalidade() {
        return localidade;
    }

    /**
     * Getter for property localidadeCodigoPostal.
     * 
     * @return Value of property localidadeCodigoPostal.
     *  
     */
    public java.lang.String getLocalidadeCodigoPostal() {
        return localidadeCodigoPostal;
    }

    /**
     * Getter for property morada.
     * 
     * @return Value of property morada.
     *  
     */
    public java.lang.String getMorada() {
        return morada;
    }

    /**
     * Getter for property nacionalidade.
     * 
     * @return Value of property nacionalidade.
     *  
     */
    public java.lang.String getNacionalidade() {
        return nacionalidade;
    }

    /**
     * Getter for property nascimento.
     * 
     * @return Value of property nascimento.
     *  
     */
    public java.util.Date getNascimento() {
        return nascimento;
    }

    /**
     * Getter for property nome.
     * 
     * @return Value of property nome.
     *  
     */
    public java.lang.String getNome() {
        return nome;
    }

    /**
     * Getter for property nomeMae.
     * 
     * @return Value of property nomeMae.
     *  
     */
    public java.lang.String getNomeMae() {
        return nomeMae;
    }

    /**
     * Getter for property nomePai.
     * 
     * @return Value of property nomePai.
     *  
     */
    public java.lang.String getNomePai() {
        return nomePai;
    }

    /**
     * Getter for property numContribuinte.
     * 
     * @return Value of property numContribuinte.
     *  
     */
    public java.lang.String getNumContribuinte() {
        return numContribuinte;
    }

    /**
     * Getter for property numeroDocumentoIdentificacao.
     * 
     * @return Value of property numeroDocumentoIdentificacao.
     *  
     */
    public java.lang.String getNumeroDocumentoIdentificacao() {
        return numeroDocumentoIdentificacao;
    }

    /**
     * Getter for property pais.
     * 
     * @return Value of property pais.
     *  
     */
    public Dominio.ICountry getPais() {
        return pais;
    }

    /**
     * Getter for property password.
     * 
     * @return Value of property password.
     *  
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return List
     */
    public Collection getPersonRoles() {
        return personRoles;
    }

    /**
     * Getter for property profissao.
     * 
     * @return Value of property profissao.
     *  
     */
    public java.lang.String getProfissao() {
        return profissao;
    }

    /**
     * Getter for property sexo.
     * 
     * @return Value of property sexo.
     *  
     */
    public Util.Sexo getSexo() {
        return sexo;
    }

    /**
     * Getter for property telefone.
     * 
     * @return Value of property telefone.
     *  
     */
    public java.lang.String getTelefone() {
        return telefone;
    }

    /**
     * Getter for property telemovel.
     * 
     * @return Value of property telemovel.
     *  
     */
    public java.lang.String getTelemovel() {
        return telemovel;
    }

    /**
     * Getter for property tipoDocumentoIdentificacao.
     * 
     * @return Value of property tipoDocumentoIdentificacao.
     *  
     */
    public TipoDocumentoIdentificacao getTipoDocumentoIdentificacao() {
        return tipoDocumentoIdentificacao;
    }

    /**
     * Getter for property username.
     * 
     * @return Value of property username.
     *  
     */
    public java.lang.String getUsername() {
        return username;
    }

    /**
     * Setter for property chavePais.
     * 
     * @param chavePais
     *            New value of property chavePais.
     *  
     */
    public void setChavePais(java.lang.Integer chavePais) {
        this.chavePais = chavePais;
    }

    /**
     * Setter for property codigoFiscal.
     * 
     * @param codigoFiscal
     *            New value of property codigoFiscal.
     *  
     */
    public void setCodigoFiscal(java.lang.String codigoFiscal) {
        this.codigoFiscal = codigoFiscal;
    }

    /**
     * Setter for property codigoPostal.
     * 
     * @param codigoPostal
     *            New value of property codigoPostal.
     *  
     */
    public void setCodigoPostal(java.lang.String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Setter for property concelhoMorada.
     * 
     * @param concelhoMorada
     *            New value of property concelhoMorada.
     *  
     */
    public void setConcelhoMorada(java.lang.String concelhoMorada) {
        this.concelhoMorada = concelhoMorada;
    }

    /**
     * Setter for property concelhoNaturalidade.
     * 
     * @param concelhoNaturalidade
     *            New value of property concelhoNaturalidade.
     *  
     */
    public void setConcelhoNaturalidade(java.lang.String concelhoNaturalidade) {
        this.concelhoNaturalidade = concelhoNaturalidade;
    }

    /**
     * Setter for property dataEmissaoDocumentoIdentificacao.
     * 
     * @param dataEmissaoDocumentoIdentificacao
     *            New value of property dataEmissaoDocumentoIdentificacao.
     *  
     */
    public void setDataEmissaoDocumentoIdentificacao(java.util.Date dataEmissaoDocumentoIdentificacao) {
        this.dataEmissaoDocumentoIdentificacao = dataEmissaoDocumentoIdentificacao;
    }

    /**
     * Setter for property dataValidadeDocumentoIdentificacao.
     * 
     * @param dataValidadeDocumentoIdentificacao
     *            New value of property dataValidadeDocumentoIdentificacao.
     *  
     */
    public void setDataValidadeDocumentoIdentificacao(java.util.Date dataValidadeDocumentoIdentificacao) {
        this.dataValidadeDocumentoIdentificacao = dataValidadeDocumentoIdentificacao;
    }

    /**
     * Setter for property distritoMorada.
     * 
     * @param distritoMorada
     *            New value of property distritoMorada.
     *  
     */
    public void setDistritoMorada(java.lang.String distritoMorada) {
        this.distritoMorada = distritoMorada;
    }

    /**
     * Setter for property distritoNaturalidade.
     * 
     * @param distritoNaturalidade
     *            New value of property distritoNaturalidade.
     *  
     */
    public void setDistritoNaturalidade(java.lang.String distritoNaturalidade) {
        this.distritoNaturalidade = distritoNaturalidade;
    }

    /**
     * Setter for property email.
     * 
     * @param email
     *            New value of property email.
     *  
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    /**
     * Setter for property enderecoWeb.
     * 
     * @param enderecoWeb
     *            New value of property enderecoWeb.
     *  
     */
    public void setEnderecoWeb(java.lang.String enderecoWeb) {
        this.enderecoWeb = enderecoWeb;
    }

    /**
     * Setter for property estadoCivil.
     * 
     * @param estadoCivil
     *            New value of property estadoCivil.
     *  
     */
    public void setEstadoCivil(Util.EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    /**
     * Setter for property freguesiaMorada.
     * 
     * @param freguesiaMorada
     *            New value of property freguesiaMorada.
     *  
     */
    public void setFreguesiaMorada(java.lang.String freguesiaMorada) {
        this.freguesiaMorada = freguesiaMorada;
    }

    /**
     * Setter for property freguesiaNaturalidade.
     * 
     * @param freguesiaNaturalidade
     *            New value of property freguesiaNaturalidade.
     *  
     */
    public void setFreguesiaNaturalidade(java.lang.String freguesiaNaturalidade) {
        this.freguesiaNaturalidade = freguesiaNaturalidade;
    }

    /**
     * Setter for property localEmissaoDocumentoIdentificacao.
     * 
     * @param localEmissaoDocumentoIdentificacao
     *            New value of property localEmissaoDocumentoIdentificacao.
     *  
     */
    public void setLocalEmissaoDocumentoIdentificacao(java.lang.String localEmissaoDocumentoIdentificacao) {
        this.localEmissaoDocumentoIdentificacao = localEmissaoDocumentoIdentificacao;
    }

    /**
     * Setter for property localidade.
     * 
     * @param localidade
     *            New value of property localidade.
     *  
     */
    public void setLocalidade(java.lang.String localidade) {
        this.localidade = localidade;
    }

    /**
     * Setter for property localidadeCodigoPostal.
     * 
     * @param localidadeCodigoPostal
     *            New value of property localidadeCodigoPostal.
     *  
     */
    public void setLocalidadeCodigoPostal(java.lang.String localidadeCodigoPostal) {
        this.localidadeCodigoPostal = localidadeCodigoPostal;
    }

    /**
     * Setter for property morada.
     * 
     * @param morada
     *            New value of property morada.
     *  
     */
    public void setMorada(java.lang.String morada) {
        this.morada = morada;
    }

    /**
     * Setter for property nacionalidade.
     * 
     * @param nacionalidade
     *            New value of property nacionalidade.
     *  
     */
    public void setNacionalidade(java.lang.String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    /**
     * Setter for property nascimento.
     * 
     * @param nascimento
     *            New value of property nascimento.
     *  
     */
    public void setNascimento(java.util.Date nascimento) {
        this.nascimento = nascimento;
    }

    /**
     * Setter for property nome.
     * 
     * @param nome
     *            New value of property nome.
     *  
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }

    /**
     * Setter for property nomeMae.
     * 
     * @param nomeMae
     *            New value of property nomeMae.
     *  
     */
    public void setNomeMae(java.lang.String nomeMae) {
        this.nomeMae = nomeMae;
    }

    /**
     * Setter for property nomePai.
     * 
     * @param nomePai
     *            New value of property nomePai.
     *  
     */
    public void setNomePai(java.lang.String nomePai) {
        this.nomePai = nomePai;
    }

    /**
     * Setter for property numContribuinte.
     * 
     * @param numContribuinte
     *            New value of property numContribuinte.
     *  
     */
    public void setNumContribuinte(java.lang.String numContribuinte) {
        this.numContribuinte = numContribuinte;
    }

    /**
     * Setter for property numeroDocumentoIdentificacao.
     * 
     * @param numeroDocumentoIdentificacao
     *            New value of property numeroDocumentoIdentificacao.
     *  
     */
    public void setNumeroDocumentoIdentificacao(java.lang.String numeroDocumentoIdentificacao) {
        this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }

    /**
     * Setter for property pais.
     * 
     * @param pais
     *            New value of property pais.
     *  
     */
    public void setPais(Dominio.ICountry pais) {
        this.pais = pais;
    }

    /**
     * Setter for property password.
     * 
     * @param password
     *            New value of property password.
     *  
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the personRoles.
     * 
     * @param personRoles
     *            The personRoles to set
     */
    public void setPersonRoles(Collection personRoles) {
        this.personRoles = personRoles;
    }

    /**
     * Setter for property profissao.
     * 
     * @param profissao
     *            New value of property profissao.
     *  
     */
    public void setProfissao(java.lang.String profissao) {
        this.profissao = profissao;
    }

    /**
     * Setter for property sexo.
     * 
     * @param sexo
     *            New value of property sexo.
     *  
     */
    public void setSexo(Util.Sexo sexo) {
        this.sexo = sexo;
    }

    /**
     * Setter for property telefone.
     * 
     * @param telefone
     *            New value of property telefone.
     *  
     */
    public void setTelefone(java.lang.String telefone) {
        this.telefone = telefone;
    }

    /**
     * Setter for property telemovel.
     * 
     * @param telemovel
     *            New value of property telemovel.
     *  
     */
    public void setTelemovel(java.lang.String telemovel) {
        this.telemovel = telemovel;
    }

    /**
     * Setter for property tipoDocumentoIdentificacao.
     * 
     * @param tipoDocumentoIdentificacao
     *            New value of property tipoDocumentoIdentificacao.
     *  
     */
    public void setTipoDocumentoIdentificacao(TipoDocumentoIdentificacao tipoDocumentoIdentificacao) {
        this.tipoDocumentoIdentificacao = tipoDocumentoIdentificacao;
    }

    /**
     * Setter for property username.
     * 
     * @param username
     *            New value of property username.
     *  
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public String toString() {
        String result = "Person :\n";
        result += "\n  - Internal Code : " + getIdInternal();
        result += "\n  - Identification Document Number : " + numeroDocumentoIdentificacao;
        result += "\n  - Identification Document Type : " + tipoDocumentoIdentificacao;
        result += "\n  - Identification Document Issue Place : " + localEmissaoDocumentoIdentificacao;
        result += "\n  - Identification Document Issue Date : " + dataEmissaoDocumentoIdentificacao;
        result += "\n  - Identification Document Expiration Date : "
                + dataValidadeDocumentoIdentificacao;
        result += "\n  - Name : " + nome;
        result += "\n  - Sex : " + sexo;
        result += "\n  - Marital Status : " + estadoCivil;
        result += "\n  - Birth : " + nascimento;
        result += "\n  - Father Name : " + nomePai;
        result += "\n  - Mother Name : " + nomeMae;
        result += "\n  - Nationality : " + nacionalidade;
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
        result += "\n  - WorkPhone : " + workPhone;
        result += "\n  - E-Mail : " + email;
        result += "\n  - HomePage : " + enderecoWeb;
        result += "\n  - Contributor Number : " + numContribuinte;
        result += "\n  - Username : " + username;
        result += "\n  - Password : " + password;
        result += "\n  - Occupation : " + profissao;
        result += "\n  - Pais : " + pais;
        result += "\n  - Codigo Fiscal : " + codigoFiscal;
        return result;
    }

    /**
     * @return
     */
    public List getAdvisories() {
        return advisories;
    }

    /**
     * @param list
     */
    public void setAdvisories(List list) {
        advisories = list;
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
        String result = "/photos/person/P" + getIdInternal();
        return result;
    }
}