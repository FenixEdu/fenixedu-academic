/*
 * IPessoa.java
 *
 * Created on 15 de Outubro de 2002, 15:03
 */

package Dominio;

import java.util.Set;

import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

public interface IPessoa {
    public Set getPrivilegios();
    public void setPrivilegios(Set privilegios);
    
    public java.lang.String getCodigoFiscal();
    public void setCodigoFiscal(java.lang.String codigoFiscal);
    public java.lang.String getConcelhoMorada();
    public void setConcelhoMorada(java.lang.String concelhoMorada);
    public java.lang.String getTelefone();
    public void setTelefone(java.lang.String telefone);
    public java.lang.String getEnderecoWeb();
    public void setEnderecoWeb(java.lang.String enderecoWeb);
    public java.util.Date getDataEmissaoDocumentoIdentificacao();
    public void setDataEmissaoDocumentoIdentificacao(java.util.Date dataEmissaoDocumentoIdentificacao);
    public java.lang.String getLocalidadeCodigoPostal();
    public void setLocalidadeCodigoPostal(java.lang.String localidadeCodigoPostal);
    public java.lang.String getEmail();
    public void setEmail(java.lang.String email);
    public java.lang.String getDistritoNaturalidade();
    public void setDistritoNaturalidade(java.lang.String distritoNaturalidade);
    public java.lang.String getNomeMae();
    public void setNomeMae(java.lang.String nomeMae);
    public java.lang.String getFreguesiaMorada();
    public void setFreguesiaMorada(java.lang.String freguesiaMorada);
    public java.lang.String getNumeroDocumentoIdentificacao();
    public void setNumeroDocumentoIdentificacao(java.lang.String numeroDocumentoIdentificacao);
    public java.util.Date getNascimento();
    public void setNascimento(java.util.Date nascimento);
    public java.lang.String getNomePai();
    public void setNomePai(java.lang.String nomePai);
    public java.lang.String getNumContribuinte();
    public void setNumContribuinte(java.lang.String numContribuinte);
    public java.util.Date getDataValidadeDocumentoIdentificacao();
    public void setDataValidadeDocumentoIdentificacao(java.util.Date dataValidadeDocumentoIdentificacao);
    public java.lang.String getLocalEmissaoDocumentoIdentificacao();
    public void setLocalEmissaoDocumentoIdentificacao(java.lang.String localEmissaoDocumentoIdentificacao);
    public java.lang.String getDistritoMorada();
    public void setDistritoMorada(java.lang.String distritoMorada);
    public java.lang.String getFreguesiaNaturalidade();
    public void setFreguesiaNaturalidade(java.lang.String freguesiaNaturalidade);
    public java.lang.String getPassword();
    public void setPassword(java.lang.String password);
    public java.lang.String getCodigoPostal();
    public void setCodigoPostal(java.lang.String codigoPostal);
    public java.lang.String getLocalidade();
    public void setLocalidade(java.lang.String localidade);
    public java.lang.String getProfissao();
    public void setProfissao(java.lang.String profissao);
    public java.lang.String getUsername();
    public void setUsername(java.lang.String username);
    public java.lang.String getNome();
    public void setNome(java.lang.String nome);
    public java.lang.String getNacionalidade();
    public void setNacionalidade(java.lang.String nacionalidade);
    public java.lang.String getTelemovel();
    public void setTelemovel(java.lang.String telemovel);
    public java.lang.String getConcelhoNaturalidade();
    public void setConcelhoNaturalidade(java.lang.String concelhoNaturalidade);
    public ICountry getPais();
    public void setPais(ICountry pais);
    public java.lang.String getMorada();
    public void setMorada(java.lang.String morada);
    public TipoDocumentoIdentificacao getTipoDocumentoIdentificacao();
    public void setTipoDocumentoIdentificacao(TipoDocumentoIdentificacao tipoDocumentoIdentificacao);
    public Sexo getSexo();
    public void setSexo(Sexo sexo);
    public EstadoCivil getEstadoCivil();
    public void setEstadoCivil(EstadoCivil estadoCivil);
}