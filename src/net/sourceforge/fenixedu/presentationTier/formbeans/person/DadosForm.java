package net.sourceforge.fenixedu.presentationTier.formbeans.person;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public final class DadosForm extends ActionForm {

    private String action = "Editar";

    private int codigoInterno = 0;

    private String numeroDocumentoIdentificacao = "";

    private String tipoDocumentoIdentificacao = "";

    private String localEmissaoDocumentoIdentificacao = "";

    private int diaValidadeDocumentoIdentificacao = 0;

    private int mesValidadeDocumentoIdentificacao = 0;

    private int anoValidadeDocumentoIdentificacao = 0;

    private String primeiroNome = "";

    private String ultimoNome = "";

    private String nomesMeio = "";

    private String sexo = "masculino";

    private String estadoCivil = "solteiro";

    private int diaNascimento = 0;

    private int mesNascimento = 0;

    private int anoNascimento = 0;

    private String nomePai = "";

    private String nomeMae = "";

    private String freguesiaNaturalidade = "";

    private String concelhoNaturalidade = "";

    private String distritoNaturalidade = "";

    private String morada = "";

    private String localidade = "";

    private String codigoPostal = "";

    private String freguesiaMorada = "";

    private String concelhoMorada = "";

    private String distritoMorada = "";

    private String telefone = "";

    private String telefoneAlternativo = "";

    private String telemovel = "";

    private String telemovelAlternativo = "";

    private String email = "";

    private String emailAlternativo = "";

    private String numContribuinte = "";

    private String codigoFiscal = "";

    private String enderecoInternet = "";

    private String username = "";

    private String password = "";

    private String passwordNova2 = "";

    private List cargos = null;

    private List urls = null;

    public String getAction() {
        return action;
    }

    public int getCodigoInterno() {
        return codigoInterno;
    }

    public String getNumeroDocumentoIdentificacao() {
        return numeroDocumentoIdentificacao;
    }

    public String getTipoDocumentoIdentificacao() {
        return tipoDocumentoIdentificacao;
    }

    public String getLocalEmissaoDocumentoIdentificacao() {
        return localEmissaoDocumentoIdentificacao;
    }

    public int getDiaValidadeDocumentoIdentificacao() {
        return diaValidadeDocumentoIdentificacao;
    }

    public int getMesValidadeDocumentoIdentificacao() {
        return mesValidadeDocumentoIdentificacao;
    }

    public int getAnoValidadeDocumentoIdentificacao() {
        return anoValidadeDocumentoIdentificacao;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public String getNomesMeio() {
        return nomesMeio;
    }

    public String getSexo() {
        return sexo;
    }

    public String getMaritalStatus() {
        return estadoCivil;
    }

    public int getDiaNascimento() {
        return diaNascimento;
    }

    public int getMesNascimento() {
        return mesNascimento;
    }

    public int getAnoNascimento() {
        return anoNascimento;
    }

    public String getNomePai() {
        return nomePai;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public String getFreguesiaNaturalidade() {
        return freguesiaNaturalidade;
    }

    public String getConcelhoNaturalidade() {
        return concelhoNaturalidade;
    }

    public String getDistritoNaturalidade() {
        return distritoNaturalidade;
    }

    public String getMorada() {
        return morada;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public String getFreguesiaMorada() {
        return freguesiaMorada;
    }

    public String getConcelhoMorada() {
        return concelhoMorada;
    }

    public String getDistritoMorada() {
        return distritoMorada;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getTelefoneAlternativo() {
        return telefoneAlternativo;
    }

    public String getTelemovel() {
        return telemovel;
    }

    public String getTelemovelAlternativo() {
        return telemovelAlternativo;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailAlternativo() {
        return emailAlternativo;
    }

    public String getNumContribuinte() {
        return numContribuinte;
    }

    public String getCodigoFiscal() {
        return codigoFiscal;
    }

    public String getEnderecoInternet() {
        return enderecoInternet;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordNova2() {
        return passwordNova2;
    }

    public List getCargos() {
        return cargos;
    }

    public List getUrls() {
        return urls;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setCodigoInterno(int codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao) {
        this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }

    public void setTipoDocumentoIdentificacao(String tipoDocumentoIdentificacao) {
        this.tipoDocumentoIdentificacao = tipoDocumentoIdentificacao;
    }

    public void setLocalEmissaoDocumentoIdentificacao(String localEmissaoDocumentoIdentificacao) {
        this.localEmissaoDocumentoIdentificacao = localEmissaoDocumentoIdentificacao;
    }

    public void setDiaValidadeDocumentoIdentificacao(int diaValidadeDocumentoIdentificacao) {
        this.diaValidadeDocumentoIdentificacao = diaValidadeDocumentoIdentificacao;
    }

    public void setMesValidadeDocumentoIdentificacao(int mesValidadeDocumentoIdentificacao) {
        this.mesValidadeDocumentoIdentificacao = mesValidadeDocumentoIdentificacao;
    }

    public void setAnoValidadeDocumentoIdentificacao(int anoValidadeDocumentoIdentificacao) {
        this.anoValidadeDocumentoIdentificacao = anoValidadeDocumentoIdentificacao;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public void setNomesMeio(String nomesMeio) {
        this.nomesMeio = nomesMeio;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setMaritalStatus(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public void setDiaNascimento(int diaNascimento) {
        this.diaNascimento = diaNascimento;
    }

    public void setMesNascimento(int mesNascimento) {
        this.mesNascimento = mesNascimento;
    }

    public void setAnoNascimento(int anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public void setFreguesiaNaturalidade(String freguesiaNaturalidade) {
        this.freguesiaNaturalidade = freguesiaNaturalidade;
    }

    public void setConcelhoNaturalidade(String concelhoNaturalidade) {
        this.concelhoNaturalidade = concelhoNaturalidade;
    }

    public void setDistritoNaturalidade(String distritoNaturalidade) {
        this.distritoNaturalidade = distritoNaturalidade;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setFreguesiaMorada(String freguesiaMorada) {
        this.freguesiaMorada = freguesiaMorada;
    }

    public void setConcelhoMorada(String concelhoMorada) {
        this.concelhoMorada = concelhoMorada;
    }

    public void setDistritoMorada(String distritoMorada) {
        this.distritoMorada = distritoMorada;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setTelefoneAlternativo(String telefoneAlternativo) {
        this.telefoneAlternativo = telefoneAlternativo;
    }

    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
    }

    public void setTelemovelAlternativo(String telemovelAlternativo) {
        this.telemovelAlternativo = telemovelAlternativo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailAlternativo(String emailAlternativo) {
        this.emailAlternativo = emailAlternativo;
    }

    public void setNumContribuinte(String numContribuinte) {
        this.numContribuinte = numContribuinte;
    }

    public void setCodigoFiscal(String codigoFiscal) {
        this.codigoFiscal = codigoFiscal;
    }

    public void setEnderecoInternet(String enderecoInternet) {
        this.enderecoInternet = enderecoInternet;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordNova2(String passwordNova2) {
        this.passwordNova2 = passwordNova2;
    }

    public void setCargos(List cargos) {
        this.cargos = cargos;
    }

    public void setUrls(List urls) {
        this.urls = urls;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        action = "Editar";
        codigoInterno = 0;
        numeroDocumentoIdentificacao = "";
        tipoDocumentoIdentificacao = "";
        localEmissaoDocumentoIdentificacao = "";
        diaValidadeDocumentoIdentificacao = 0;
        mesValidadeDocumentoIdentificacao = 0;
        anoValidadeDocumentoIdentificacao = 0;
        primeiroNome = "";
        ultimoNome = "";
        nomesMeio = "";
        sexo = "masculino";
        estadoCivil = "solteiro";
        diaNascimento = 0;
        mesNascimento = 0;
        anoNascimento = 0;
        nomePai = "";
        nomeMae = "";
        freguesiaNaturalidade = "";
        concelhoNaturalidade = "";
        distritoNaturalidade = "";
        morada = "";
        localidade = "";
        codigoPostal = "";
        freguesiaMorada = "";
        concelhoMorada = "";
        distritoMorada = "";
        telefone = "";
        telefoneAlternativo = "";
        telemovel = "";
        telemovelAlternativo = "";
        email = "";
        emailAlternativo = "";
        numContribuinte = "";
        codigoFiscal = "";
        enderecoInternet = "";
        username = "";
        password = "";
        passwordNova2 = "";
        cargos = null;
        urls = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if ((username == null) || (username.length() < 1))
            errors.add("username", new ActionError("error.username.obrigatorio"));

        if ("Criar".equals(action)) {
            if ((password == null) || (password.length() < 1) || (passwordNova2 == null)
                    || (passwordNova2.length() < 1))
                errors.add("password", new ActionError("error.password.obrigatoria"));
            if (!(password.equals(passwordNova2)))
                errors.add("passwordNova2", new ActionError("error.password.diferentes"));
        }
        return errors;
    }

    public void setForm(Person pessoa) {
        setCodigoInterno(pessoa.getIdInternal().intValue());
        setNumeroDocumentoIdentificacao(pessoa.getDocumentIdNumber());
        setTipoDocumentoIdentificacao(String.valueOf(pessoa.getIdDocumentType().toString()));
        setLocalEmissaoDocumentoIdentificacao(pessoa.getEmissionLocationOfDocumentId());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pessoa.getExpirationDateOfDocumentId());
        setDiaValidadeDocumentoIdentificacao(calendar.get(Calendar.DAY_OF_MONTH));
        setMesValidadeDocumentoIdentificacao(calendar.get(Calendar.MONTH));
        setAnoValidadeDocumentoIdentificacao(calendar.get(Calendar.YEAR));
        /*
         * setPrimeiroNome(pessoa.getPrimeiroNome());
         * setUltimoNome(pessoa.getUltimoNome());
         * setNomesMeio(pessoa.getNomesMeio()); setSexo(pessoa.getSexo());
         * setMaritalStatus(pessoa.getMaritalStatus());
         */
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(pessoa.getDateOfBirth());
        setDiaNascimento(calendar.get(Calendar.DAY_OF_MONTH));
        setMesNascimento(calendar.get(Calendar.MONTH));
        setAnoNascimento(calendar.get(Calendar.YEAR));
        setNomePai(pessoa.getNameOfFather());
        setNomeMae(pessoa.getNameOfMother());
        setFreguesiaNaturalidade(pessoa.getParishOfBirth());
        setConcelhoNaturalidade(pessoa.getDistrictSubdivisionOfBirth());
        setDistritoNaturalidade(pessoa.getDistrictOfBirth());
        setMorada(pessoa.getAddress());
        setLocalidade(pessoa.getArea());
        setCodigoPostal(pessoa.getAreaCode());
        setFreguesiaMorada(pessoa.getParishOfResidence());
        setConcelhoMorada(pessoa.getDistrictSubdivisionOfResidence());
        setDistritoMorada(pessoa.getDistrictOfResidence());
        setTelefone(pessoa.getPhone());
        setTelemovel(pessoa.getMobile());
        setEmail(pessoa.getEmail());
        setNumContribuinte(pessoa.getSocialSecurityNumber());
        setCodigoFiscal(pessoa.getFiscalCode());
        setEnderecoInternet(pessoa.getWebAddress());
        setUsername(pessoa.getUsername());
        setPassword(pessoa.getPassword());
    }

    public void setPessoa(Person pessoa) {
        if (getMorada() != null)
            pessoa.setMorada(getMorada());
        if (getLocalidade() != null)
            pessoa.setLocalidade(getLocalidade());
        if (getCodigoPostal() != null)
            pessoa.setCodigoPostal(getCodigoPostal());
        if (getFreguesiaMorada() != null)
            pessoa.setFreguesiaMorada(getFreguesiaMorada());
        if (getConcelhoMorada() != null)
            pessoa.setConcelhoMorada(getConcelhoMorada());
        if (getDistritoMorada() != null)
            pessoa.setDistritoMorada(getDistritoMorada());

        if (getTelefone() != null)
            pessoa.setTelefone(getTelefone());
        if (getTelemovel() != null)
            pessoa.setTelemovel(getTelemovel());

        if (getEmail() != null)
            pessoa.setEmail(getEmail());

        if (getEnderecoInternet() != null)
            pessoa.setEnderecoWeb(getEnderecoInternet());

        if ("Criar".equals(getAction())) {
            pessoa.setIdInternal(new Integer(getCodigoInterno()));
            pessoa.setNumeroDocumentoIdentificacao(getNumeroDocumentoIdentificacao());
            pessoa.setIdDocumentType(IDDocumentType.valueOf(getTipoDocumentoIdentificacao()));
            pessoa.setLocalEmissaoDocumentoIdentificacao(getLocalEmissaoDocumentoIdentificacao());

            Calendar calendar = Calendar.getInstance();
            calendar.set(getAnoValidadeDocumentoIdentificacao(), getMesValidadeDocumentoIdentificacao(),
                    getDiaValidadeDocumentoIdentificacao());

            pessoa.setDataValidadeDocumentoIdentificacao(calendar.getTime());
            /*
             * pessoa.setPrimeiroNome(getPrimeiroNome());
             * pessoa.setUltimoNome(getUltimoNome());
             * pessoa.setNomesMeio(getNomesMeio()); pessoa.setSexo(getSexo());
             * pessoa.setMaritalStatus(getMaritalStatus());
             */
            calendar.set(getAnoNascimento(), getMesNascimento(), getDiaNascimento());
            pessoa.setNascimento(calendar.getTime());
            pessoa.setNomePai(getNomePai());
            pessoa.setNomeMae(getNomeMae());
            pessoa.setFreguesiaNaturalidade(getFreguesiaNaturalidade());
            pessoa.setConcelhoNaturalidade(getConcelhoNaturalidade());
            pessoa.setDistritoNaturalidade(getDistritoNaturalidade());
            pessoa.setNumContribuinte(getNumContribuinte());
            pessoa.setCodigoFiscal(getCodigoFiscal());                      
            pessoa.setUsername(RoleType.PERSON);                       
            pessoa.setPassword(getPassword());
        }
    }
}