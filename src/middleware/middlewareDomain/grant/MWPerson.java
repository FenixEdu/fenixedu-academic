/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import java.util.Date;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWPerson extends MWDomainObject
{
    private Integer idInternal;
    private String numeroDocumentoIdentificacao;
    private Integer tipoDocumentoIdentificacao;
    private Date dataEmissaoDocumentoIdentificacao;
    private String localEmissaoDocumentoIdentificacao;
    private Date dataValidadeDocumentoIdentificacao;    
    private Integer estadoCivil;
    private Date nascimento;
    private Integer sexo;
    private String nome;
    private String nomeMae;
    private String nomePai;
    private String nacionalidade;
    private String concelhoNaturalidade;
    private String distritoNaturalidade;
    private String freguesiaNaturalidade;
    private String morada;    
    private String localidade;
    private String codigoPostal;
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
    private String nacionalidadeCompleta;
    private String codigoFiscal;
    
    
	public MWPerson()
	{
		super();
	}

	/**
	 * @return Returns the codigoFiscal.
	 */
	public String getCodigoFiscal()
	{
		return codigoFiscal;
	}

	/**
	 * @param codigoFiscal The codigoFiscal to set.
	 */
	public void setCodigoFiscal(String codigoFiscal)
	{
		this.codigoFiscal = codigoFiscal;
	}

	/**
	 * @return Returns the codigoPostal.
	 */
	public String getCodigoPostal()
	{
		return codigoPostal;
	}

	/**
	 * @param codigoPostal The codigoPostal to set.
	 */
	public void setCodigoPostal(String codigoPostal)
	{
		this.codigoPostal = codigoPostal;
	}

	/**
	 * @return Returns the concelhoMorada.
	 */
	public String getConcelhoMorada()
	{
		return concelhoMorada;
	}

	/**
	 * @param concelhoMorada The concelhoMorada to set.
	 */
	public void setConcelhoMorada(String concelhoMorada)
	{
		this.concelhoMorada = concelhoMorada;
	}

	/**
	 * @return Returns the concelhoNaturalidade.
	 */
	public String getConcelhoNaturalidade()
	{
		return concelhoNaturalidade;
	}

	/**
	 * @param concelhoNaturalidade The concelhoNaturalidade to set.
	 */
	public void setConcelhoNaturalidade(String concelhoNaturalidade)
	{
		this.concelhoNaturalidade = concelhoNaturalidade;
	}

	/**
	 * @return Returns the dataEmissaoDocumentoIdentificacao.
	 */
	public Date getDataEmissaoDocumentoIdentificacao()
	{
		return dataEmissaoDocumentoIdentificacao;
	}

	/**
	 * @param dataEmissaoDocumentoIdentificacao The dataEmissaoDocumentoIdentificacao to set.
	 */
	public void setDataEmissaoDocumentoIdentificacao(Date dataEmissaoDocumentoIdentificacao)
	{
		this.dataEmissaoDocumentoIdentificacao = dataEmissaoDocumentoIdentificacao;
	}

	/**
	 * @return Returns the dataValidadeDocumentoIdentificacao.
	 */
	public Date getDataValidadeDocumentoIdentificacao()
	{
		return dataValidadeDocumentoIdentificacao;
	}

	/**
	 * @param dataValidadeDocumentoIdentificacao The dataValidadeDocumentoIdentificacao to set.
	 */
	public void setDataValidadeDocumentoIdentificacao(Date dataValidadeDocumentoIdentificacao)
	{
		this.dataValidadeDocumentoIdentificacao = dataValidadeDocumentoIdentificacao;
	}

	/**
	 * @return Returns the distritoMorada.
	 */
	public String getDistritoMorada()
	{
		return distritoMorada;
	}

	/**
	 * @param distritoMorada The distritoMorada to set.
	 */
	public void setDistritoMorada(String distritoMorada)
	{
		this.distritoMorada = distritoMorada;
	}

	/**
	 * @return Returns the distritoNaturalidade.
	 */
	public String getDistritoNaturalidade()
	{
		return distritoNaturalidade;
	}

	/**
	 * @param distritoNaturalidade The distritoNaturalidade to set.
	 */
	public void setDistritoNaturalidade(String distritoNaturalidade)
	{
		this.distritoNaturalidade = distritoNaturalidade;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * @return Returns the enderecoWeb.
	 */
	public String getEnderecoWeb()
	{
		return enderecoWeb;
	}

	/**
	 * @param enderecoWeb The enderecoWeb to set.
	 */
	public void setEnderecoWeb(String enderecoWeb)
	{
		this.enderecoWeb = enderecoWeb;
	}

	/**
	 * @return Returns the estadoCivil.
	 */
	public Integer getEstadoCivil()
	{
		return estadoCivil;
	}

	/**
	 * @param estadoCivil The estadoCivil to set.
	 */
	public void setEstadoCivil(Integer estadoCivil)
	{
		this.estadoCivil = estadoCivil;
	}

	/**
	 * @return Returns the freguesiaMorada.
	 */
	public String getFreguesiaMorada()
	{
		return freguesiaMorada;
	}

	/**
	 * @param freguesiaMorada The freguesiaMorada to set.
	 */
	public void setFreguesiaMorada(String freguesiaMorada)
	{
		this.freguesiaMorada = freguesiaMorada;
	}

	/**
	 * @return Returns the freguesiaNaturalidade.
	 */
	public String getFreguesiaNaturalidade()
	{
		return freguesiaNaturalidade;
	}

	/**
	 * @param freguesiaNaturalidade The freguesiaNaturalidade to set.
	 */
	public void setFreguesiaNaturalidade(String freguesiaNaturalidade)
	{
		this.freguesiaNaturalidade = freguesiaNaturalidade;
	}

	/**
	 * @return Returns the idInternal.
	 */
	public Integer getIdInternal()
	{
		return idInternal;
	}

	/**
	 * @param idInternal The idInternal to set.
	 */
	public void setIdInternal(Integer idInternal)
	{
		this.idInternal = idInternal;
	}

	/**
	 * @return Returns the localEmissaoDocumentoIdentificacao.
	 */
	public String getLocalEmissaoDocumentoIdentificacao()
	{
		return localEmissaoDocumentoIdentificacao;
	}

	/**
	 * @param localEmissaoDocumentoIdentificacao The localEmissaoDocumentoIdentificacao to set.
	 */
	public void setLocalEmissaoDocumentoIdentificacao(String localEmissaoDocumentoIdentificacao)
	{
		this.localEmissaoDocumentoIdentificacao = localEmissaoDocumentoIdentificacao;
	}

	/**
	 * @return Returns the localidade.
	 */
	public String getLocalidade()
	{
		return localidade;
	}

	/**
	 * @param localidade The localidade to set.
	 */
	public void setLocalidade(String localidade)
	{
		this.localidade = localidade;
	}

	/**
	 * @return Returns the morada.
	 */
	public String getMorada()
	{
		return morada;
	}

	/**
	 * @param morada The morada to set.
	 */
	public void setMorada(String morada)
	{
		this.morada = morada;
	}

	/**
	 * @return Returns the nacionalidade.
	 */
	public String getNacionalidade()
	{
		return nacionalidade;
	}

	/**
	 * @param nacionalidade The nacionalidade to set.
	 */
	public void setNacionalidade(String nacionalidade)
	{
		this.nacionalidade = nacionalidade;
	}

	/**
	 * @return Returns the nacionalidadeCompleta.
	 */
	public String getNacionalidadeCompleta()
	{
		return nacionalidadeCompleta;
	}

	/**
	 * @param nacionalidadeCompleta The nacionalidadeCompleta to set.
	 */
	public void setNacionalidadeCompleta(String nacionalidadeCompleta)
	{
		this.nacionalidadeCompleta = nacionalidadeCompleta;
	}

	/**
	 * @return Returns the nascimento.
	 */
	public Date getNascimento()
	{
		return nascimento;
	}

	/**
	 * @param nascimento The nascimento to set.
	 */
	public void setNascimento(Date nascimento)
	{
		this.nascimento = nascimento;
	}

	/**
	 * @return Returns the nome.
	 */
	public String getNome()
	{
		return nome;
	}

	/**
	 * @param nome The nome to set.
	 */
	public void setNome(String nome)
	{
		this.nome = nome;
	}

	/**
	 * @return Returns the nomeMae.
	 */
	public String getNomeMae()
	{
		return nomeMae;
	}

	/**
	 * @param nomeMae The nomeMae to set.
	 */
	public void setNomeMae(String nomeMae)
	{
		this.nomeMae = nomeMae;
	}

	/**
	 * @return Returns the nomePai.
	 */
	public String getNomePai()
	{
		return nomePai;
	}

	/**
	 * @param nomePai The nomePai to set.
	 */
	public void setNomePai(String nomePai)
	{
		this.nomePai = nomePai;
	}

	/**
	 * @return Returns the numContribuinte.
	 */
	public String getNumContribuinte()
	{
		return numContribuinte;
	}

	/**
	 * @param numContribuinte The numContribuinte to set.
	 */
	public void setNumContribuinte(String numContribuinte)
	{
		this.numContribuinte = numContribuinte;
	}

	/**
	 * @return Returns the numeroDocumentoIdentificacao.
	 */
	public String getNumeroDocumentoIdentificacao()
	{
		return numeroDocumentoIdentificacao;
	}

	/**
	 * @param numeroDocumentoIdentificacao The numeroDocumentoIdentificacao to set.
	 */
	public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao)
	{
		this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return Returns the profissao.
	 */
	public String getProfissao()
	{
		return profissao;
	}

	/**
	 * @param profissao The profissao to set.
	 */
	public void setProfissao(String profissao)
	{
		this.profissao = profissao;
	}

	/**
	 * @return Returns the sexo.
	 */
	public Integer getSexo()
	{
		return sexo;
	}

	/**
	 * @param sexo The sexo to set.
	 */
	public void setSexo(Integer sexo)
	{
		this.sexo = sexo;
	}

	/**
	 * @return Returns the telefone.
	 */
	public String getTelefone()
	{
		return telefone;
	}

	/**
	 * @param telefone The telefone to set.
	 */
	public void setTelefone(String telefone)
	{
		this.telefone = telefone;
	}

	/**
	 * @return Returns the telemovel.
	 */
	public String getTelemovel()
	{
		return telemovel;
	}

	/**
	 * @param telemovel The telemovel to set.
	 */
	public void setTelemovel(String telemovel)
	{
		this.telemovel = telemovel;
	}

	/**
	 * @return Returns the tipoDocumentoIdentificacao.
	 */
	public Integer getTipoDocumentoIdentificacao()
	{
		return tipoDocumentoIdentificacao;
	}

	/**
	 * @param tipoDocumentoIdentificacao The tipoDocumentoIdentificacao to set.
	 */
	public void setTipoDocumentoIdentificacao(Integer tipoDocumentoIdentificacao)
	{
		this.tipoDocumentoIdentificacao = tipoDocumentoIdentificacao;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

}
