package ServidorApresentacao.formbeans.assiduousness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import Dominio.CostCenter;
import Dominio.NonTeacherEmployee;
import Dominio.Funcionario;
import Dominio.Horario;
import Dominio.Person;
import Dominio.StatusAssiduidade;
import constants.assiduousness.Constants;

/**
 * @author Fernanda Quitério & Tania Pousão
 */
public class ConsultarFuncionarioMostrarForm extends ActionForm {

    private String _nome = null;

    private String _morada = null;

    private String _localidade = null;

    private String _codigoPostal = null;

    private String _telefone = null;

    private String _email = null;

    private String _numMecanografico = null;

    private String _statusAssiduidade = null;

    private String _siglaCentroCusto = null;

    private String _descricaoCentroCusto = null;

    /*
     * private String _inicioExpedienteHoras = null; private String
     * _inicioExpedienteMinutos = null; private String _fimExpedienteHoras =
     * null; private String _fimExpedienteMinutos = null;
     * 
     * private String _diaAnteriorExpediente = null; private String
     * _diaSeguinteExpediente = null;
     * 
     * private String _inicioRefeicaoHoras = null; private String
     * _inicioRefeicaoMinutos = null; private String _fimRefeicaoHoras = null;
     * private String _fimRefeicaoMinutos = null;
     * 
     * private String _diaAnteriorRefeicao = null; private String
     * _diaSeguinteRefeicao = null;
     * 
     * private String _intervaloMinimoHoras = null; private String
     * _intervaloMinimoMinutos = null; private String _descontoObrigatorioHoras =
     * null; private String _descontoObrigatorioMinutos = null;
     * 
     * private String _inicioHN1Horas = null; private String _inicioHN1Minutos =
     * null; private String _fimHN1Horas = null; private String _fimHN1Minutos =
     * null;
     * 
     * private String _diaAnteriorHN1 = null; private String _diaSeguinteHN1 =
     * null;
     * 
     * private String _inicioHN2Horas = null; private String _inicioHN2Minutos =
     * null; private String _fimHN2Horas = null; private String _fimHN2Minutos =
     * null;
     * 
     * private String _diaAnteriorHN2 = null; private String _diaSeguinteHN2 =
     * null;
     * 
     * private String _inicioPF1Horas = null; private String _inicioPF1Minutos =
     * null; private String _fimPF1Horas = null; private String _fimPF1Minutos =
     * null;
     * 
     * private String _diaAnteriorPF1 = null; private String _diaSeguintePF1 =
     * null;
     * 
     * private String _inicioPF2Horas = null; private String _inicioPF2Minutos =
     * null; private String _fimPF2Horas = null; private String _fimPF2Minutos =
     * null;
     * 
     * private String _diaAnteriorPF2 = null; private String _diaSeguintePF2 =
     * null;
     * 
     * private String _diaInicio = null; private String _mesInicio = null;
     * private String _anoInicio = null;
     * 
     * private String _diaFim = null; private String _mesFim = null;
     */

    private String _diaInicioEscolha = null;

    private String _mesInicioEscolha = null;

    private String _anoInicioEscolha = null;

    private Timestamp _dataInicioEscolha = null;

    private String _diaFimEscolha = null;

    private String _mesFimEscolha = null;

    private String _anoFimEscolha = null;

    private Timestamp _dataFimEscolha = null;

    /*
     * private String _duracaoSemanal = null; private String _sigla = null;
     * private String _modalidade = null;
     */
    private List _listaEscolhas = new ArrayList();

    private String _escolha = null;

    private List _rotacaoHorario = null;

    public String getNome() {

        return (_nome);
    }

    public String getMorada() {

        return (_morada);
    }

    public String getLocalidade() {

        return (_localidade);
    }

    public String getCodigoPostal() {

        return (_codigoPostal);
    }

    public String getTelefone() {

        return (_telefone);
    }

    public String getEmail() {

        return (_email);
    }

    public String getNumMecanografico() {

        return (_numMecanografico);
    }

    public String getStatusAssiduidade() {

        return _statusAssiduidade;
    }

    public String getSiglaCentroCusto() {

        return (_siglaCentroCusto);
    }

    public String getDescricaoCentroCusto() {

        return (_descricaoCentroCusto);
    }

    public String getDiaInicioEscolha() {

        return (_diaInicioEscolha);
    }

    public String getMesInicioEscolha() {

        return (_mesInicioEscolha);
    }

    public String getAnoInicioEscolha() {

        return (_anoInicioEscolha);
    }

    public Timestamp getDataInicioEscolha() {

        return (_dataInicioEscolha);
    }

    public String getDiaFimEscolha() {

        return (_diaFimEscolha);
    }

    public String getMesFimEscolha() {

        return (_mesFimEscolha);
    }

    public String getAnoFimEscolha() {

        return (_anoFimEscolha);
    }

    public Timestamp getDataFimEscolha() {

        return (_dataFimEscolha);
    }

    public List getListaEscolhas() {

        return (_listaEscolhas);
    }

    public String getEscolha() {

        return (_escolha);
    }

    public List getRotacaoHorario() {

        return (_rotacaoHorario);
    }

    public void setNome(String nome) {

        _nome = nome;
    }

    public void setMorada(String morada) {

        _morada = morada;
    }

    public void setLocalidade(String localidade) {

        _localidade = localidade;
    }

    public void setCodigoPostal(String codigoPostal) {

        _codigoPostal = codigoPostal;
    }

    public void setTelefone(String telefone) {

        _telefone = telefone;
    }

    public void setEmail(String email) {

        _email = email;
    }

    public void setNumMecanografico(int numMecanografico) {

        _numMecanografico = (new Integer(numMecanografico)).toString();
    }

    public void setStatusAssiduidade(String _statusAssiduidade) {

        this._statusAssiduidade = _statusAssiduidade;
    }

    public void setDescricaoCentroCusto(String descricaoCentroCusto) {

        _descricaoCentroCusto = descricaoCentroCusto;
    }

    public void setSiglaCentroCusto(String siglaCentroCusto) {

        _siglaCentroCusto = siglaCentroCusto;
    }

    public void setDiaInicioEscolha(String diaInicioEscolha) {

        _diaInicioEscolha = diaInicioEscolha;
    }

    public void setMesInicioEscolha(String mesInicioEscolha) {

        _mesInicioEscolha = mesInicioEscolha;
    }

    public void setAnoInicioEscolha(String anoInicioEscolha) {

        _anoInicioEscolha = anoInicioEscolha;
    }

    public void setDataInicioEscolha(Timestamp dataInicioEscolha) {

        _dataInicioEscolha = dataInicioEscolha;
    }

    public void setDiaFimEscolha(String diaFimEscolha) {

        _diaFimEscolha = diaFimEscolha;
    }

    public void setMesFimEscolha(String mesFimEscolha) {

        _mesFimEscolha = mesFimEscolha;
    }

    public void setAnoFimEscolha(String anoFimEscolha) {

        _anoFimEscolha = anoFimEscolha;
    }

    public void setDataFimEscolha(Timestamp dataFimEscolha) {

        _dataFimEscolha = dataFimEscolha;
    }

    public void setListaEscolhas(List listaEscolhas) {

        _listaEscolhas = listaEscolhas;
    }

    public void setEscolha(String escolha) {

        _escolha = escolha;
    }

    public void setRotacaoHorario(List rotacaoHorario) {

        _rotacaoHorario = rotacaoHorario;
    }

    public void setForm(Date dataInicialConsulta, Date dataFinalConsulta, Person pessoa,
            Funcionario funcionario, StatusAssiduidade statusAssiduidade, CostCenter centroCusto,
            NonTeacherEmployee funcNaoDocente, List rotacaoHorario, HashMap listaRegimesRotacao) {

        _listaEscolhas.clear();
        _listaEscolhas.add(new String("consultar.marcacao"));
        _listaEscolhas.add(new String("consultar.justificacoes"));
        _listaEscolhas.add(new String("consultar.verbete"));

        setEscolha(new String("consultar.verbete"));

        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);
        calendario.clear();
        calendario.setTimeInMillis(dataInicialConsulta.getTime());
        setDiaInicioEscolha(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
        setMesInicioEscolha(String.valueOf(calendario.get(Calendar.MONTH) + 1));
        setAnoInicioEscolha(String.valueOf(calendario.get(Calendar.YEAR)));

        calendario.clear();
        calendario.setTimeInMillis(dataFinalConsulta.getTime());
        setDiaFimEscolha(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
        setMesFimEscolha(String.valueOf(calendario.get(Calendar.MONTH) + 1));
        setAnoFimEscolha(String.valueOf(calendario.get(Calendar.YEAR)));

        setNome(pessoa.getNome());
        setMorada(pessoa.getMorada());
        setLocalidade(pessoa.getLocalidade());
        setCodigoPostal(pessoa.getCodigoPostal());
        setTelefone(pessoa.getTelefone());
        setEmail(pessoa.getEmail());
        setNumMecanografico(funcionario.getNumeroMecanografico());

        if (statusAssiduidade != null) {
            setStatusAssiduidade(statusAssiduidade.getDesignacao());
        }

        if (centroCusto != null) {
            setSiglaCentroCusto(centroCusto.getCode());
            setDescricaoCentroCusto(centroCusto.getDepartament() + "<br>" + centroCusto.getSection1()
                    + "<br>" + centroCusto.getSection2());
        }

        ListIterator iterador = rotacaoHorario.listIterator();
        List listaHorariosDia = new ArrayList();
        while (iterador.hasNext()) {
            Horario horarioDia = (Horario) iterador.next();
            AssociarHorarioForm novoHorario = new AssociarHorarioForm();
            novoHorario = novoHorario.getFormHorarioPreenchido(horarioDia,
                    (ArrayList) listaRegimesRotacao.get(new Integer(horarioDia.getPosicao())), null);
            // o horario vem da base de dados logo tem que se somar uma hora
            novoHorario.setDuracoesHorario(horarioDia, true);
            listaHorariosDia.add(novoHorario);
            //				new AssociarHorarioForm(horarioDia, (ArrayList)
            // listaRegimesRotacao.get(new
            // Integer(horarioDia.getPosicao()))));
            //				novoHorario.getFormHorarioPreenchido(horarioDia, (ArrayList)
            // listaRegimesRotacao.get(new
            // Integer(horarioDia.getPosicao())), null));
        }

        setRotacaoHorario(listaHorariosDia);
    } /* setForm */

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        _diaInicioEscolha = null;
        _mesInicioEscolha = null;
        _anoInicioEscolha = null;
        _diaFimEscolha = null;
        _mesFimEscolha = null;
        _anoFimEscolha = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        int diaInicio = 0;
        int mesInicio = 0;
        int anoInicio = 0;
        int diaFim = 0;
        int mesFim = 0;
        int anoFim = 0;

        try {
            if ((this.getDiaInicioEscolha() == null) || (this.getMesInicioEscolha() == null)
                    || (this.getAnoInicioEscolha() == null) || (this.getDiaFimEscolha() == null)
                    || (this.getMesFimEscolha() == null) || (this.getAnoFimEscolha() == null)) {
                errors.add("dates", new ActionError("error.campos.obrigatorio"));
            } else {
                if ((this.getDiaInicioEscolha().length() < 1)
                        || (this.getMesInicioEscolha().length() < 1)
                        || (this.getAnoInicioEscolha().length() < 1)
                        || (this.getDiaFimEscolha().length() < 1)
                        || (this.getMesFimEscolha().length() < 1)
                        || (this.getAnoFimEscolha().length() < 1)) {
                    errors.add("dates", new ActionError("error.dataConsulta.invalida"));
                } else {
                    try {
                        diaInicio = (new Integer(this.getDiaInicioEscolha())).intValue();
                        mesInicio = (new Integer(this.getMesInicioEscolha())).intValue();
                        anoInicio = (new Integer(this.getAnoInicioEscolha())).intValue();
                        diaFim = (new Integer(this.getDiaFimEscolha())).intValue();
                        mesFim = (new Integer(this.getMesFimEscolha())).intValue();
                        anoFim = (new Integer(this.getAnoFimEscolha())).intValue();
                    } catch (java.lang.NumberFormatException e) {
                        errors.add("numero", new ActionError("error.numero.naoInteiro"));
                    }

                    Calendar calendarInicio = Calendar.getInstance();
                    Calendar calendarFim = Calendar.getInstance();

                    calendarInicio.setLenient(false);
                    calendarInicio.clear();
                    calendarInicio.set(anoInicio, mesInicio - 1, diaInicio, 00, 00, 00);
                    Timestamp dataInicio = new Timestamp(calendarInicio.getTimeInMillis());
                    setDataInicioEscolha(dataInicio);

                    calendarFim.setLenient(false);
                    calendarFim.clear();
                    calendarFim.set(anoFim, mesFim - 1, diaFim, 23, 59, 59);
                    Timestamp dataFim = new Timestamp(calendarFim.getTimeInMillis());
                    setDataFimEscolha(dataFim);

                    Calendar calendarioInicioAplicacao = Calendar.getInstance();
                    calendarioInicioAplicacao.setLenient(false);
                    calendarioInicioAplicacao.clear();
                    calendarioInicioAplicacao.set(2003, Calendar.MAY, 1, 00, 00, 00);

                    if (calendarInicio.before(calendarioInicioAplicacao)) {
                        errors.add("datas", new ActionError("error.dataValidade.antes1Maio"));
                    }

                    if (!(dataInicio.getTime() <= dataFim.getTime())) {
                        errors.add("datas", new ActionError("error.dataValidade.incorrecta"));
                    } else {
                        HttpSession session = request.getSession();
                        session.setAttribute(Constants.INICIO_CONSULTA, new Date(calendarInicio
                                .getTimeInMillis()));
                        session.setAttribute(Constants.FIM_CONSULTA, new Date(calendarFim
                                .getTimeInMillis()));
                    }
                }
            }
        } catch (Exception e) {
            errors.add("horasData", new ActionError("error.dataValidade.incorrecta"));
        }
        return errors;
    }
}