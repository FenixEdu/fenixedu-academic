package ServidorApresentacao.formbeans.assiduousness;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import Dominio.IStrategyJustificacoes;
import Dominio.Justificacao;
import Dominio.ParamJustificacao;
import Dominio.SuporteStrategyJustificacoes;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class LerDadosJustificacaoForm extends ActionForm {
    private String _numMecanografico = null;

    private String _siglaJustificacao = null;

    private String _horaInicio = null;

    private String _minutosInicio = null;

    private String _diaInicio = null;

    private String _mesInicio = null;

    private String _anoInicio = null;

    private String _horaFim = null;

    private String _minutosFim = null;

    private String _diaFim = null;

    private String _mesFim = null;

    private String _anoFim = null;

    private String _observacao = null;

    private List _listaSiglasJustificacao = new ArrayList();

    public String getNumMecanografico() {
        return (_numMecanografico);
    }

    public String getSiglaJustificacao() {
        return (_siglaJustificacao);
    }

    public String getHoraInicio() {
        return (_horaInicio);
    }

    public String getMinutosInicio() {
        return (_minutosInicio);
    }

    public String getDiaInicio() {
        return (_diaInicio);
    }

    public String getMesInicio() {
        return (_mesInicio);
    }

    public String getAnoInicio() {
        return (_anoInicio);
    }

    public String getHoraFim() {
        return (_horaFim);
    }

    public String getMinutosFim() {
        return (_minutosFim);
    }

    public String getDiaFim() {
        return (_diaFim);
    }

    public String getMesFim() {
        return (_mesFim);
    }

    public String getAnoFim() {
        return (_anoFim);
    }

    public String getObservacao() {
        return (_observacao);
    }

    public List getListaSiglasJustificacao() {
        return (_listaSiglasJustificacao);
    }

    public void setNumMecanografico(String numMecanografico) {
        _numMecanografico = numMecanografico;
    }

    public void setSiglaJustificacao(String siglaJustificacao) {
        _siglaJustificacao = siglaJustificacao;
    }

    public void setHoraInicio(String horaInicio) {
        _horaInicio = horaInicio;
    }

    public void setMinutosInicio(String minutosInicio) {
        _minutosInicio = minutosInicio;
    }

    public void setDiaInicio(String diaInicio) {
        _diaInicio = diaInicio;
    }

    public void setMesInicio(String mesInicio) {
        _mesInicio = mesInicio;
    }

    public void setAnoInicio(String anoInicio) {
        _anoInicio = anoInicio;
    }

    public void setHoraFim(String horaFim) {
        _horaFim = horaFim;
    }

    public void setMinutosFim(String minutosFim) {
        _minutosFim = minutosFim;
    }

    public void setDiaFim(String diaFim) {
        _diaFim = diaFim;
    }

    public void setMesFim(String mesFim) {
        _mesFim = mesFim;
    }

    public void setAnoFim(String anoFim) {
        _anoFim = anoFim;
    }

    public void setObservacao(String observacao) {
        _observacao = observacao;
    }

    public void setListaSiglasJustificacao(List listaSiglasJustificacao) {
        _listaSiglasJustificacao = listaSiglasJustificacao;
    }

    public void setForm(List listaSiglasJustificacao, String sigla, Justificacao justificacao,
            ParamJustificacao paramJustificacao, int numeroMecanografico) {
        this.setListaSiglasJustificacao(listaSiglasJustificacao);
        if (sigla == null) {
            this.setSiglaJustificacao(paramJustificacao.getSigla());
        } else {
            setSiglaJustificacao(sigla);
        }
        IStrategyJustificacoes justificacaoStrategy = SuporteStrategyJustificacoes.getInstance()
                .callStrategy((paramJustificacao.getTipo()));
        justificacaoStrategy.setLerDadosJustificacaoForm(justificacao, this);

        this.setNumMecanografico(String.valueOf(numeroMecanografico));

    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();
        List listaJustificacoes = (ArrayList) session.getAttribute("listaTiposJustificacao");
        List listaSiglas = (ArrayList) session.getAttribute("listaSiglasJustificacao");
        int indiceLista = listaSiglas.indexOf(getSiglaJustificacao());

        // validacao dos outros campos
        IStrategyJustificacoes justificacaoStrategy = SuporteStrategyJustificacoes.getInstance()
                .callStrategy(((ParamJustificacao) listaJustificacoes.get(indiceLista)).getTipo());
        errors = justificacaoStrategy.validateFormJustificacao(this);

        session.setAttribute("tipoJustificacao", ((ParamJustificacao) listaJustificacoes
                .get(indiceLista)).getTipo());
        return errors;
    }
}