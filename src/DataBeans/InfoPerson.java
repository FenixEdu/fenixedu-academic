/*
 * InfoPerson.java
 * 
 * Created on 13 de Dezembro de 2002, 16:13
 */

package DataBeans;

import java.util.Date;
import java.util.List;

import Dominio.IPessoa;
import Dominio.Pessoa;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

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

    private String workPhone;

    private String email;

    private Boolean availableEmail;

    private String enderecoWeb;

    private Boolean availableWebSite;

    private String numContribuinte;

    private String profissao;

    private String username;

    private String password;

    private String codigoFiscal;

    private TipoDocumentoIdentificacao tipoDocumentoIdentificacao;

    private Sexo sexo;

    private EstadoCivil estadoCivil;

    private InfoCountry infoPais;

    private List infoAdvisories;

    private Boolean availablePhoto;

    /* Construtores */
    public InfoPerson() {
    }

    public InfoPerson(String numeroDocumentoIdentificacao,
            TipoDocumentoIdentificacao tipoDocumentoIdentificacao,
            String localEmissaoDocumentoIdentificacao, Date dataEmissaoDocumentoIdentificacao,
            Date dataValidadeDocumentoIdentificacao, String nome, Sexo sexo, EstadoCivil estadoCivil,
            Date nascimento, String nomePai, String nomeMae, String nacionalidade,
            String freguesiaNaturalidade, String concelhoNaturalidade, String distritoNaturalidade,
            String morada, String localidade, String codigoPostal, String localidadeCodigoPostal,
            String freguesiaMorada, String concelhoMorada, String distritoMorada, String telefone,
            String telemovel, String email, String enderecoWeb, String numContribuinte,
            String profissao, String username, String password, InfoCountry infoPais, String codigoFiscal) {

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
        result += "\n  - E-Mail : " + email;
        result += "\n  - HomePage : " + enderecoWeb;
        result += "\n  - Contributor Number : " + numContribuinte;
        result += "\n  - Username : " + username;
        result += "\n  - Password : " + password;
        result += "\n  - Occupation : " + profissao;
        result += "\n  - Codigo Fiscal : " + codigoFiscal;
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
     * @return EstadoCivil
     */
    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
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
        return nacionalidade;
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

    /**
     * @return Sexo
     */
    public Sexo getSexo() {
        return sexo;
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
    public TipoDocumentoIdentificacao getTipoDocumentoIdentificacao() {
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
    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
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
     * Sets the nacionalidade.
     * 
     * @param nacionalidade
     *            The nacionalidade to set
     */
    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
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
    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
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
    public void setTipoDocumentoIdentificacao(TipoDocumentoIdentificacao tipoDocumentoIdentificacao) {
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

    public void copyFromDomain(IPessoa person) {
        super.copyFromDomain(person);
        if (person != null) {
            setNome(person.getNome());
            setNumeroDocumentoIdentificacao(person.getNumeroDocumentoIdentificacao());
            setTipoDocumentoIdentificacao(person.getTipoDocumentoIdentificacao());
            setLocalEmissaoDocumentoIdentificacao(person.getLocalEmissaoDocumentoIdentificacao());
            setDataEmissaoDocumentoIdentificacao(person.getDataEmissaoDocumentoIdentificacao());
            setDataValidadeDocumentoIdentificacao(person.getDataValidadeDocumentoIdentificacao());

            setSexo(person.getSexo());
            setEstadoCivil(person.getEstadoCivil());

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
            setNacionalidade(person.getNacionalidade());

            setNascimento(person.getNascimento());
            setNomeMae(person.getNomeMae());
            setNomePai(person.getNomePai());

            setNumContribuinte(person.getNumContribuinte());
            setCodigoFiscal(person.getCodigoFiscal());

            setPassword(person.getPassword());
            setUsername(person.getUsername());

            setAvailablePhoto(person.getAvailablePhoto());

        }
    }

    /**
     * @param pessoa
     * @return
     */
    public static InfoPerson newInfoFromDomain(IPessoa person) {
        InfoPerson infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPerson();
            infoPerson.copyFromDomain(person);
        }
        return infoPerson;
    }

    public void copyToDomain(InfoPerson infoPerson, IPessoa person) {
        super.copyToDomain(infoPerson, person);

        person.setNome(infoPerson.getNome());
        person.setNumeroDocumentoIdentificacao(infoPerson.getNumeroDocumentoIdentificacao());
        person.setTipoDocumentoIdentificacao(infoPerson.getTipoDocumentoIdentificacao());
        person.setLocalEmissaoDocumentoIdentificacao(infoPerson.getLocalEmissaoDocumentoIdentificacao());
        person.setDataEmissaoDocumentoIdentificacao(infoPerson.getDataEmissaoDocumentoIdentificacao());
        person.setDataValidadeDocumentoIdentificacao(infoPerson.getDataValidadeDocumentoIdentificacao());

        person.setSexo(infoPerson.getSexo());
        person.setEstadoCivil(infoPerson.getEstadoCivil());

        person.setEmail(infoPerson.getEmail());
        person.setAvailableEmail(infoPerson.getAvailableEmail());
        person.setEnderecoWeb(infoPerson.getEnderecoWeb());
        person.setAvailableWebSite(infoPerson.getAvailableWebSite());
        person.setTelefone(infoPerson.getTelefone());
        person.setTelemovel(infoPerson.getTelemovel());
        person.setWorkPhone(infoPerson.getWorkPhone());

        person.setProfissao(infoPerson.getProfissao());

        person.setMorada(infoPerson.getMorada());
        person.setCodigoPostal(infoPerson.getCodigoPostal());
        person.setLocalidade(infoPerson.getLocalidade());
        person.setFreguesiaMorada(infoPerson.getFreguesiaMorada());
        person.setConcelhoMorada(infoPerson.getConcelhoMorada());
        person.setDistritoMorada(infoPerson.getDistritoMorada());

        person.setConcelhoNaturalidade(infoPerson.getConcelhoNaturalidade());
        person.setDistritoNaturalidade(infoPerson.getDistritoNaturalidade());
        person.setFreguesiaNaturalidade(infoPerson.getFreguesiaNaturalidade());

        person.setLocalidadeCodigoPostal(infoPerson.getLocalidadeCodigoPostal());
        person.setNacionalidade(infoPerson.getNacionalidade());

        person.setNascimento(infoPerson.getNascimento());
        person.setNomeMae(infoPerson.getNomeMae());
        person.setNomePai(infoPerson.getNomePai());

        person.setNumContribuinte(infoPerson.getNumContribuinte());
        person.setCodigoFiscal(infoPerson.getCodigoFiscal());

        person.setPassword(infoPerson.getPassword());
        person.setUsername(infoPerson.getUsername());

        person.setAvailablePhoto(infoPerson.getAvailablePhoto());
    }

    public static IPessoa newDomainFromInfo(InfoPerson infoPerson) {
        IPessoa person = null;
        if (infoPerson != null) {
            person = new Pessoa();
            infoPerson.copyToDomain(infoPerson, person);
        }
        return person;
    }
}