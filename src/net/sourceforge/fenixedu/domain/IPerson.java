/*
 * IPerson.java
 * 
 * Created on 15 de Outubro de 2002, 15:03
 * @author Lu?s Cruz, Nuno Nunes, Jo?o Luz
 */

package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.util.EstadoCivil;
import net.sourceforge.fenixedu.util.Sexo;
import net.sourceforge.fenixedu.util.TipoDocumentoIdentificacao;

public interface IPerson extends IDomainObject {

    public List getManageableDepartmentCredits();

    public void setManageableDepartmentCredits(List manageableDepartmentCredits);

    public List getAdvisories();

    public void setAdvisories(List advisories);

    public Boolean getAvailableEmail();

    public Boolean getAvailableWebSite();

    public String getCodigoFiscal();

    public String getCodigoPostal();

    public String getConcelhoMorada();

    public String getConcelhoNaturalidade();

    public Date getDataEmissaoDocumentoIdentificacao();

    public Date getDataValidadeDocumentoIdentificacao();

    public String getDistritoMorada();

    public String getDistritoNaturalidade();

    public String getEmail();

    public String getEnderecoWeb();

    public EstadoCivil getEstadoCivil();

    public String getFreguesiaMorada();

    public String getFreguesiaNaturalidade();

    public String getLocalEmissaoDocumentoIdentificacao();

    public String getLocalidade();

    public String getLocalidadeCodigoPostal();

    public String getMorada();

    public String getNacionalidade();

    public Date getNascimento();

    public String getNome();

    public String getNomeMae();

    public String getNomePai();

    public String getNumContribuinte();

    public String getNumeroDocumentoIdentificacao();

    public ICountry getPais();

    public String getPassword();

    public Collection getPersonRoles();

    public String getProfissao();

    public Sexo getSexo();

    public String getTelefone();

    public String getTelemovel();

    public TipoDocumentoIdentificacao getTipoDocumentoIdentificacao();

    public String getUsername();

    public String getWorkPhone();

    public void setAvailableEmail(Boolean availableEmail);

    public void setAvailableWebSite(Boolean availableWebSite);

    public void setCodigoFiscal(String codigoFiscal);

    public void setCodigoPostal(String codigoPostal);

    public void setConcelhoMorada(String concelhoMorada);

    public void setConcelhoNaturalidade(String concelhoNaturalidade);

    public void setDataEmissaoDocumentoIdentificacao(Date dataEmissaoDocumentoIdentificacao);

    public void setDataValidadeDocumentoIdentificacao(Date dataValidadeDocumentoIdentificacao);

    public void setDistritoMorada(String distritoMorada);

    public void setDistritoNaturalidade(String distritoNaturalidade);

    public void setEmail(String email);

    public void setEnderecoWeb(String enderecoWeb);

    public void setEstadoCivil(EstadoCivil estadoCivil);

    public void setFreguesiaMorada(String freguesiaMorada);

    public void setFreguesiaNaturalidade(String freguesiaNaturalidade);

    public void setLocalEmissaoDocumentoIdentificacao(String localEmissaoDocumentoIdentificacao);

    public void setLocalidade(String localidade);

    public void setLocalidadeCodigoPostal(String localidadeCodigoPostal);

    public void setMorada(String morada);

    public void setNacionalidade(String nacionalidade);

    public void setNascimento(Date nascimento);

    public void setNome(String nome);

    public void setNomeMae(String nomeMae);

    public void setNomePai(String nomePai);

    public void setNumContribuinte(String numContribuinte);

    public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao);

    public void setPais(ICountry pais);

    public void setPassword(String password);

    public void setPersonRoles(Collection personRoles);

    public void setProfissao(String profissao);

    public void setSexo(Sexo sexo);

    public void setTelefone(String telefone);

    public void setTelemovel(String telemovel);

    public void setTipoDocumentoIdentificacao(TipoDocumentoIdentificacao tipoDocumentoIdentificacao);

    public void setUsername(String username);

    public void setWorkPhone(String workPhone);

    //Nuno Correia & Ricardo Rodrigues
    public Boolean getAvailablePhoto();

    public void setAvailablePhoto(Boolean availablePhoto);

    public String getSlideName();

}