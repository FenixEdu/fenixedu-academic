package Dominio;
import java.util.Date;
import java.util.Set;

import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

public class Pessoa implements IPessoa {
    
    private Integer codigoInterno;
    private Integer chavePais;

    private String numeroDocumentoIdentificacao;
    private String localEmissaoDocumentoIdentificacao;
    private Date dataEmissaoDocumentoIdentificacao;
    private Date dataValidadeDocumentoIdentificacao;
    private String nome;
    private Date nascimento;
    private String nomePai;
    private String nomeMae;
    private String nacionalidade;
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
    private String email;
    private String enderecoWeb;
    private String numContribuinte;
    private String profissao;
    private String username;
    private String password;
    private String codigoFiscal;
    private TipoDocumentoIdentificacao tipoDocumentoIdentificacao;
    private Sexo sexo;
    private EstadoCivil estadoCivil;
    private ICountry pais;
    private Set privilegios;
    
    /* Construtores */
    
    public Pessoa() {
        this.numeroDocumentoIdentificacao = "";
        this.tipoDocumentoIdentificacao = null;
        this.localEmissaoDocumentoIdentificacao = "";
        this.dataEmissaoDocumentoIdentificacao = null;
        this.dataValidadeDocumentoIdentificacao = null;
        this.nome = "";
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
        this.username = "";
        this.password = "";
        this.pais = null;
        this.codigoFiscal = "";
        this.privilegios = null;
    }
    
    public Pessoa(String numeroDocumentoIdentificacao,
    TipoDocumentoIdentificacao tipoDocumentoIdentificacao,
    String localEmissaoDocumentoIdentificacao,
    Date dataEmissaoDocumentoIdentificacao,
    Date dataValidadeDocumentoIdentificacao,
    String nome,
    Sexo sexo,
    EstadoCivil estadoCivil,
    Date nascimento,
    String nomePai,
    String nomeMae,
    String nacionalidade,
    String freguesiaNaturalidade,
    String concelhoNaturalidade,
    String distritoNaturalidade,
    String morada,
    String localidade,
    String codigoPostal,
    String localidadeCodigoPostal,
    String freguesiaMorada,
    String concelhoMorada,
    String distritoMorada,
    String telefone,
    String telemovel,
    String email,
    String enderecoWeb,
    String numContribuinte,
    String profissao,
    String username,
    String password,
    ICountry pais,
    String codigoFiscal) {
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

    public boolean equals(Object o) {
        return
        ((o instanceof Pessoa) &&

        (numeroDocumentoIdentificacao.equals(((Pessoa)o).getNumeroDocumentoIdentificacao())) &&
        (tipoDocumentoIdentificacao == ((Pessoa)o).getTipoDocumentoIdentificacao()));
//        (localEmissaoDocumentoIdentificacao.equals(((Pessoa)o).getLocalEmissaoDocumentoIdentificacao())) &&
//        (dataEmissaoDocumentoIdentificacao.equals(((Pessoa)o).getDataEmissaoDocumentoIdentificacao())) &&
//        (dataValidadeDocumentoIdentificacao.equals(((Pessoa)o).getDataValidadeDocumentoIdentificacao())) &&
//        (nome.equals(((Pessoa)o).getNome())) &&
//        (sexo == ((Pessoa)o).getSexo()) &&
//        (estadoCivil == ((Pessoa)o).getEstadoCivil()) &&
//        (nascimento.equals(((Pessoa)o).getNascimento())) &&
//        (nomePai.equals(((Pessoa)o).getNomePai())) &&
//        (nomeMae.equals(((Pessoa)o).getNomeMae())) &&
//        (nacionalidade.equals(((Pessoa)o).getNacionalidade())) &&
//        (freguesiaNaturalidade.equals(((Pessoa)o).getFreguesiaNaturalidade())) &&
//        (concelhoNaturalidade.equals(((Pessoa)o).getConcelhoNaturalidade())) &&
//        (distritoNaturalidade.equals(((Pessoa)o).getDistritoNaturalidade())) &&
//        (morada.equals(((Pessoa)o).getMorada())) &&
//        (localidade.equals(((Pessoa)o).getLocalidade())) &&
//        (codigoPostal.equals(((Pessoa)o).getCodigoPostal())) &&
//        (localidadeCodigoPostal.equals(((Pessoa)o).getLocalidadeCodigoPostal())) &&
//        (freguesiaMorada.equals(((Pessoa)o).getFreguesiaMorada())) &&
//        (concelhoMorada.equals(((Pessoa)o).getConcelhoMorada())) &&
//        (distritoMorada.equals(((Pessoa)o).getDistritoMorada())) &&
//        (telefone.equals(((Pessoa)o).getTelefone())) &&
//        (telemovel.equals(((Pessoa)o).getTelemovel())) &&
//        (email.equals(((Pessoa)o).getEmail())) &&
//        (enderecoWeb.equals(((Pessoa)o).getEnderecoWeb())) &&
//        (numContribuinte.equals(((Pessoa)o).getNumContribuinte())) &&
//        (profissao.equals(((Pessoa)o).getProfissao())) &&
//        (username.equals(((Pessoa)o).getUsername())) &&
//        (password.equals(((Pessoa)o).getPassword())) &&
//        (chavePais == ((Pessoa)o).getchavePais()) &&
//        (codigoFiscal.equals(((Pessoa)o).getCodigoFiscal())));
    }

    /** Getter for property chavePais.
     * @return Value of property chavePais.
     *
     */
    public java.lang.Integer getChavePais() {
        return chavePais;
    }
    
    /** Setter for property chavePais.
     * @param chavePais New value of property chavePais.
     *
     */
    public void setChavePais(java.lang.Integer chavePais) {
        this.chavePais = chavePais;
    }
    
    /** Getter for property username.
     * @return Value of property username.
     *
     */
    public java.lang.String getUsername() {
        return username;
    }
    
    /** Setter for property username.
     * @param username New value of property username.
     *
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }
    
    /** Getter for property telemovel.
     * @return Value of property telemovel.
     *
     */
    public java.lang.String getTelemovel() {
        return telemovel;
    }
    
    /** Setter for property telemovel.
     * @param telemovel New value of property telemovel.
     *
     */
    public void setTelemovel(java.lang.String telemovel) {
        this.telemovel = telemovel;
    }
    
    /** Getter for property codigoFiscal.
     * @return Value of property codigoFiscal.
     *
     */
    public java.lang.String getCodigoFiscal() {
        return codigoFiscal;
    }
    
    /** Setter for property codigoFiscal.
     * @param codigoFiscal New value of property codigoFiscal.
     *
     */
    public void setCodigoFiscal(java.lang.String codigoFiscal) {
        this.codigoFiscal = codigoFiscal;
    }
    
    /** Getter for property codigoInterno.
     * @return Value of property codigoInterno.
     *
     */
    public java.lang.Integer getCodigoInterno() {
        return codigoInterno;
    }
    
    /** Setter for property codigoInterno.
     * @param codigoInterno New value of property codigoInterno.
     *
     */
    public void setCodigoInterno(java.lang.Integer codigoInterno) {
        this.codigoInterno = codigoInterno;
    }
    
    /** Getter for property codigoPostal.
     * @return Value of property codigoPostal.
     *
     */
    public java.lang.String getCodigoPostal() {
        return codigoPostal;
    }
    
    /** Setter for property codigoPostal.
     * @param codigoPostal New value of property codigoPostal.
     *
     */
    public void setCodigoPostal(java.lang.String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    /** Getter for property concelhoMorada.
     * @return Value of property concelhoMorada.
     *
     */
    public java.lang.String getConcelhoMorada() {
        return concelhoMorada;
    }
    
    /** Setter for property concelhoMorada.
     * @param concelhoMorada New value of property concelhoMorada.
     *
     */
    public void setConcelhoMorada(java.lang.String concelhoMorada) {
        this.concelhoMorada = concelhoMorada;
    }
    
    /** Getter for property concelhoNaturalidade.
     * @return Value of property concelhoNaturalidade.
     *
     */
    public java.lang.String getConcelhoNaturalidade() {
        return concelhoNaturalidade;
    }
    
    /** Setter for property concelhoNaturalidade.
     * @param concelhoNaturalidade New value of property concelhoNaturalidade.
     *
     */
    public void setConcelhoNaturalidade(java.lang.String concelhoNaturalidade) {
        this.concelhoNaturalidade = concelhoNaturalidade;
    }
    
    /** Getter for property dataEmissaoDocumentoIdentificacao.
     * @return Value of property dataEmissaoDocumentoIdentificacao.
     *
     */
    public java.util.Date getDataEmissaoDocumentoIdentificacao() {
        return dataEmissaoDocumentoIdentificacao;
    }
    
    /** Setter for property dataEmissaoDocumentoIdentificacao.
     * @param dataEmissaoDocumentoIdentificacao New value of property dataEmissaoDocumentoIdentificacao.
     *
     */
    public void setDataEmissaoDocumentoIdentificacao(java.util.Date dataEmissaoDocumentoIdentificacao) {
        this.dataEmissaoDocumentoIdentificacao = dataEmissaoDocumentoIdentificacao;
    }
    
    /** Getter for property dataValidadeDocumentoIdentificacao.
     * @return Value of property dataValidadeDocumentoIdentificacao.
     *
     */
    public java.util.Date getDataValidadeDocumentoIdentificacao() {
        return dataValidadeDocumentoIdentificacao;
    }
    
    /** Setter for property dataValidadeDocumentoIdentificacao.
     * @param dataValidadeDocumentoIdentificacao New value of property dataValidadeDocumentoIdentificacao.
     *
     */
    public void setDataValidadeDocumentoIdentificacao(java.util.Date dataValidadeDocumentoIdentificacao) {
        this.dataValidadeDocumentoIdentificacao = dataValidadeDocumentoIdentificacao;
    }
    
    /** Getter for property distritoMorada.
     * @return Value of property distritoMorada.
     *
     */
    public java.lang.String getDistritoMorada() {
        return distritoMorada;
    }
    
    /** Setter for property distritoMorada.
     * @param distritoMorada New value of property distritoMorada.
     *
     */
    public void setDistritoMorada(java.lang.String distritoMorada) {
        this.distritoMorada = distritoMorada;
    }
    
    /** Getter for property distritoNaturalidade.
     * @return Value of property distritoNaturalidade.
     *
     */
    public java.lang.String getDistritoNaturalidade() {
        return distritoNaturalidade;
    }
    
    /** Setter for property distritoNaturalidade.
     * @param distritoNaturalidade New value of property distritoNaturalidade.
     *
     */
    public void setDistritoNaturalidade(java.lang.String distritoNaturalidade) {
        this.distritoNaturalidade = distritoNaturalidade;
    }
    
    /** Getter for property email.
     * @return Value of property email.
     *
     */
    public java.lang.String getEmail() {
        return email;
    }
    
    /** Setter for property email.
     * @param email New value of property email.
     *
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }
    
    /** Getter for property enderecoWeb.
     * @return Value of property enderecoWeb.
     *
     */
    public java.lang.String getEnderecoWeb() {
        return enderecoWeb;
    }
    
    /** Setter for property enderecoWeb.
     * @param enderecoWeb New value of property enderecoWeb.
     *
     */
    public void setEnderecoWeb(java.lang.String enderecoWeb) {
        this.enderecoWeb = enderecoWeb;
    }
    
    /** Getter for property freguesiaMorada.
     * @return Value of property freguesiaMorada.
     *
     */
    public java.lang.String getFreguesiaMorada() {
        return freguesiaMorada;
    }
    
    /** Setter for property freguesiaMorada.
     * @param freguesiaMorada New value of property freguesiaMorada.
     *
     */
    public void setFreguesiaMorada(java.lang.String freguesiaMorada) {
        this.freguesiaMorada = freguesiaMorada;
    }
    
    /** Getter for property freguesiaNaturalidade.
     * @return Value of property freguesiaNaturalidade.
     *
     */
    public java.lang.String getFreguesiaNaturalidade() {
        return freguesiaNaturalidade;
    }
    
    /** Setter for property freguesiaNaturalidade.
     * @param freguesiaNaturalidade New value of property freguesiaNaturalidade.
     *
     */
    public void setFreguesiaNaturalidade(java.lang.String freguesiaNaturalidade) {
        this.freguesiaNaturalidade = freguesiaNaturalidade;
    }
    
    /** Getter for property localEmissaoDocumentoIdentificacao.
     * @return Value of property localEmissaoDocumentoIdentificacao.
     *
     */
    public java.lang.String getLocalEmissaoDocumentoIdentificacao() {
        return localEmissaoDocumentoIdentificacao;
    }
    
    /** Setter for property localEmissaoDocumentoIdentificacao.
     * @param localEmissaoDocumentoIdentificacao New value of property localEmissaoDocumentoIdentificacao.
     *
     */
    public void setLocalEmissaoDocumentoIdentificacao(java.lang.String localEmissaoDocumentoIdentificacao) {
        this.localEmissaoDocumentoIdentificacao = localEmissaoDocumentoIdentificacao;
    }
    
    /** Getter for property localidade.
     * @return Value of property localidade.
     *
     */
    public java.lang.String getLocalidade() {
        return localidade;
    }
    
    /** Setter for property localidade.
     * @param localidade New value of property localidade.
     *
     */
    public void setLocalidade(java.lang.String localidade) {
        this.localidade = localidade;
    }
    
    /** Getter for property localidadeCodigoPostal.
     * @return Value of property localidadeCodigoPostal.
     *
     */
    public java.lang.String getLocalidadeCodigoPostal() {
        return localidadeCodigoPostal;
    }
    
    /** Setter for property localidadeCodigoPostal.
     * @param localidadeCodigoPostal New value of property localidadeCodigoPostal.
     *
     */
    public void setLocalidadeCodigoPostal(java.lang.String localidadeCodigoPostal) {
        this.localidadeCodigoPostal = localidadeCodigoPostal;
    }
    
    /** Getter for property morada.
     * @return Value of property morada.
     *
     */
    public java.lang.String getMorada() {
        return morada;
    }
    
    /** Setter for property morada.
     * @param morada New value of property morada.
     *
     */
    public void setMorada(java.lang.String morada) {
        this.morada = morada;
    }
    
    /** Getter for property nacionalidade.
     * @return Value of property nacionalidade.
     *
     */
    public java.lang.String getNacionalidade() {
        return nacionalidade;
    }
    
    /** Setter for property nacionalidade.
     * @param nacionalidade New value of property nacionalidade.
     *
     */
    public void setNacionalidade(java.lang.String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }
    
    /** Getter for property nascimento.
     * @return Value of property nascimento.
     *
     */
    public java.util.Date getNascimento() {
        return nascimento;
    }
    
    /** Setter for property nascimento.
     * @param nascimento New value of property nascimento.
     *
     */
    public void setNascimento(java.util.Date nascimento) {
        this.nascimento = nascimento;
    }
    
    /** Getter for property nome.
     * @return Value of property nome.
     *
     */
    public java.lang.String getNome() {
        return nome;
    }
    
    /** Setter for property nome.
     * @param nome New value of property nome.
     *
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }
    
    /** Getter for property nomeMae.
     * @return Value of property nomeMae.
     *
     */
    public java.lang.String getNomeMae() {
        return nomeMae;
    }
    
    /** Setter for property nomeMae.
     * @param nomeMae New value of property nomeMae.
     *
     */
    public void setNomeMae(java.lang.String nomeMae) {
        this.nomeMae = nomeMae;
    }
    
    /** Getter for property nomePai.
     * @return Value of property nomePai.
     *
     */
    public java.lang.String getNomePai() {
        return nomePai;
    }
    
    /** Setter for property nomePai.
     * @param nomePai New value of property nomePai.
     *
     */
    public void setNomePai(java.lang.String nomePai) {
        this.nomePai = nomePai;
    }
    
    /** Getter for property numContribuinte.
     * @return Value of property numContribuinte.
     *
     */
    public java.lang.String getNumContribuinte() {
        return numContribuinte;
    }
    
    /** Setter for property numContribuinte.
     * @param numContribuinte New value of property numContribuinte.
     *
     */
    public void setNumContribuinte(java.lang.String numContribuinte) {
        this.numContribuinte = numContribuinte;
    }
    
    /** Getter for property numeroDocumentoIdentificacao.
     * @return Value of property numeroDocumentoIdentificacao.
     *
     */
    public java.lang.String getNumeroDocumentoIdentificacao() {
        return numeroDocumentoIdentificacao;
    }
    
    /** Setter for property numeroDocumentoIdentificacao.
     * @param numeroDocumentoIdentificacao New value of property numeroDocumentoIdentificacao.
     *
     */
    public void setNumeroDocumentoIdentificacao(java.lang.String numeroDocumentoIdentificacao) {
        this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }
    
    /** Getter for property pais.
     * @return Value of property pais.
     *
     */
    public Dominio.ICountry getPais() {
        return pais;
    }
    
    /** Setter for property pais.
     * @param pais New value of property pais.
     *
     */
    public void setPais(Dominio.ICountry pais) {
        this.pais = pais;
    }
    
    /** Getter for property password.
     * @return Value of property password.
     *
     */
    public java.lang.String getPassword() {
        return password;
    }
    
    /** Setter for property password.
     * @param password New value of property password.
     *
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }
    
    /** Getter for property profissao.
     * @return Value of property profissao.
     *
     */
    public java.lang.String getProfissao() {
        return profissao;
    }
    
    /** Setter for property profissao.
     * @param profissao New value of property profissao.
     *
     */
    public void setProfissao(java.lang.String profissao) {
        this.profissao = profissao;
    }
    
    /** Getter for property telefone.
     * @return Value of property telefone.
     *
     */
    public java.lang.String getTelefone() {
        return telefone;
    }
    
    /** Setter for property telefone.
     * @param telefone New value of property telefone.
     *
     */
    public void setTelefone(java.lang.String telefone) {
        this.telefone = telefone;
    }    
    
    /** Getter for property tipoDocumentoIdentificacao.
     * @return Value of property tipoDocumentoIdentificacao.
     *
     */
    public TipoDocumentoIdentificacao getTipoDocumentoIdentificacao() {
        return tipoDocumentoIdentificacao;
    }
    
    /** Setter for property tipoDocumentoIdentificacao.
     * @param tipoDocumentoIdentificacao New value of property tipoDocumentoIdentificacao.
     *
     */
    public void setTipoDocumentoIdentificacao(TipoDocumentoIdentificacao tipoDocumentoIdentificacao) {
        this.tipoDocumentoIdentificacao = tipoDocumentoIdentificacao;
    }
    
    /** Getter for property sexo.
     * @return Value of property sexo.
     *
     */
    public Util.Sexo getSexo() {
        return sexo;
    }
    
    /** Setter for property sexo.
     * @param sexo New value of property sexo.
     *
     */
    public void setSexo(Util.Sexo sexo) {
        this.sexo = sexo;
    }
    
    /** Getter for property estadoCivil.
     * @return Value of property estadoCivil.
     *
     */
    public Util.EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }
    
    /** Setter for property estadoCivil.
     * @param estadoCivil New value of property estadoCivil.
     *
     */
    public void setEstadoCivil(Util.EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
    
    /** Getter for property privilegios.
     * @return Value of property privilegios.
     *
     */
    public java.util.Set getPrivilegios() {
        return privilegios;
    }
    
    /** Setter for property privilegios.
     * @param privilegios New value of property privilegios.
     *
     */
    public void setPrivilegios(java.util.Set privilegios) {
        this.privilegios = privilegios;
    }
    
}