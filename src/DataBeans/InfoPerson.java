/*
 * InfoPerson.java
 *
 * Created on 13 de Dezembro de 2002, 16:13
 */

package DataBeans;

import java.util.Date;
import java.util.Set;

import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

/**
 *
 * @author  tfc130
 */

public class InfoPerson {
    
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
    private InfoCountry pais;
    private Set privilegios;
    
    /* Construtores */
    public InfoPerson() { }

	public InfoPerson(
		String numeroDocumentoIdentificacao,
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
		InfoCountry pais,
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
		setInfoPais(pais);
		setCodigoFiscal(codigoFiscal);
	}


	public java.lang.String getUsername() {
		return username;
	}

	public void setUsername(java.lang.String username) {
		this.username = username;
	}

	public java.lang.String getTelemovel() {
		return telemovel;
	}

	public void setTelemovel(java.lang.String telemovel) {
		this.telemovel = telemovel;
	}

	public java.lang.String getCodigoFiscal() {
		return codigoFiscal;
	}

	public void setCodigoFiscal(java.lang.String codigoFiscal) {
		this.codigoFiscal = codigoFiscal;
	}

	public java.lang.String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(java.lang.String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public java.lang.String getConcelhoMorada() {
		return concelhoMorada;
	}

	public void setConcelhoMorada(java.lang.String concelhoMorada) {
		this.concelhoMorada = concelhoMorada;
	}

	public java.lang.String getConcelhoNaturalidade() {
		return concelhoNaturalidade;
	}

	public void setConcelhoNaturalidade(java.lang.String concelhoNaturalidade) {
		this.concelhoNaturalidade = concelhoNaturalidade;
	}

	public java.util.Date getDataEmissaoDocumentoIdentificacao() {
		return dataEmissaoDocumentoIdentificacao;
	}

	public void setDataEmissaoDocumentoIdentificacao(java.util.Date dataEmissaoDocumentoIdentificacao) {
		this.dataEmissaoDocumentoIdentificacao = dataEmissaoDocumentoIdentificacao;
	}

	public java.util.Date getDataValidadeDocumentoIdentificacao() {
		return dataValidadeDocumentoIdentificacao;
	}

	public void setDataValidadeDocumentoIdentificacao(java.util.Date dataValidadeDocumentoIdentificacao) {
		this.dataValidadeDocumentoIdentificacao =dataValidadeDocumentoIdentificacao;
	}

	public java.lang.String getDistritoMorada() {
		return distritoMorada;
	}
	public void setDistritoMorada(java.lang.String distritoMorada) {
		this.distritoMorada = distritoMorada;
	}
	public java.lang.String getDistritoNaturalidade() {
		return distritoNaturalidade;
	}

	public void setDistritoNaturalidade(java.lang.String distritoNaturalidade) {
		this.distritoNaturalidade = distritoNaturalidade;
	}

	public java.lang.String getEmail() {
		return email;
	}
	
	public void setEmail(java.lang.String email) {
		this.email = email;
	}
	
	public java.lang.String getEnderecoWeb() {
		return enderecoWeb;
	}
	
	public void setEnderecoWeb(java.lang.String enderecoWeb) {
		this.enderecoWeb = enderecoWeb;
	}
	
	public java.lang.String getFreguesiaMorada() {
		return freguesiaMorada;
	}

	public void setFreguesiaMorada(java.lang.String freguesiaMorada) {
		this.freguesiaMorada = freguesiaMorada;
	}

	public java.lang.String getFreguesiaNaturalidade() {
		return freguesiaNaturalidade;
	}

	public void setFreguesiaNaturalidade(java.lang.String freguesiaNaturalidade) {
		this.freguesiaNaturalidade = freguesiaNaturalidade;
	}

	public java.lang.String getLocalEmissaoDocumentoIdentificacao() {
		return localEmissaoDocumentoIdentificacao;
	}

	public void setLocalEmissaoDocumentoIdentificacao(java.lang.String localEmissaoDocumentoIdentificacao) {
		this.localEmissaoDocumentoIdentificacao =localEmissaoDocumentoIdentificacao;
	}

	public java.lang.String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(java.lang.String localidade) {
		this.localidade = localidade;
	}

	public java.lang.String getLocalidadeCodigoPostal() {
		return localidadeCodigoPostal;
	}

	public void setLocalidadeCodigoPostal(java.lang.String localidadeCodigoPostal) {
		this.localidadeCodigoPostal = localidadeCodigoPostal;
	}
	
	public java.lang.String getMorada() {
		return morada;
	}

	public void setMorada(java.lang.String morada) {
		this.morada = morada;
	}

	public java.lang.String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(java.lang.String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public java.util.Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(java.util.Date nascimento) {
		this.nascimento = nascimento;
	}

	public java.lang.String getNome() {
		return nome;
	}

	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}

	public java.lang.String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(java.lang.String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public java.lang.String getNomePai() {
		return nomePai;
	}

	public void setNomePai(java.lang.String nomePai) {
		this.nomePai = nomePai;
	}

	public java.lang.String getNumContribuinte() {
		return numContribuinte;
	}

	public void setNumContribuinte(java.lang.String numContribuinte) {
		this.numContribuinte = numContribuinte;
	}

	public java.lang.String getNumeroDocumentoIdentificacao() {
		return numeroDocumentoIdentificacao;
	}

	public void setNumeroDocumentoIdentificacao(java.lang.String numeroDocumentoIdentificacao) {
		this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
	}
	
	public InfoCountry getInfoPais() {
		return pais;
	}
	
	public void setInfoPais(InfoCountry pais) {
		this.pais = pais;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public java.lang.String getProfissao() {
		return profissao;
	}

	public void setProfissao(java.lang.String profissao) {
		this.profissao = profissao;
	}
	
	public java.lang.String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(java.lang.String telefone) {
		this.telefone = telefone;
	}
	
	public TipoDocumentoIdentificacao getTipoDocumentoIdentificacao() {
		return tipoDocumentoIdentificacao;
	}
	
	public void setTipoDocumentoIdentificacao(TipoDocumentoIdentificacao tipoDocumentoIdentificacao) {
		this.tipoDocumentoIdentificacao = tipoDocumentoIdentificacao;
	}

	public Util.Sexo getSexo() {
		return sexo;
	}
	
	public void setSexo(Util.Sexo sexo) {
		this.sexo = sexo;
	}
	
	public Util.EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}
	
	public void setEstadoCivil(Util.EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	
	public java.util.Set getPrivilegios() {
		return privilegios;
	}
	
	public void setPrivilegios(java.util.Set privilegios) {
		this.privilegios = privilegios;
	}


	public boolean equals(Object o) {
		return ((o instanceof InfoPerson) &&
				(numeroDocumentoIdentificacao.equals(((InfoPerson) o).getNumeroDocumentoIdentificacao())) &&
				(tipoDocumentoIdentificacao	== ((InfoPerson) o).getTipoDocumentoIdentificacao()));
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer("[InfoPerson ,");
		buffer.append("Numero de identificacao=");
		buffer.append(this.getNumeroDocumentoIdentificacao());
		buffer.append(", Tipo documento identificacao=");
		buffer.append(this.getTipoDocumentoIdentificacao().toString());
		buffer.append(",Nome=");
		buffer.append(this.getNome());
		buffer.append("]");
		return buffer.toString();
	}

}