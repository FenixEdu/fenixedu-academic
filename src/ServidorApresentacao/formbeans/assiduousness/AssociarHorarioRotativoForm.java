package ServidorApresentacao.formbeans.assiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import Dominio.Horario;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class AssociarHorarioRotativoForm extends ActionForm {
    private String _nome = null;

    private String _numMecanografico = null;

    private List _listaHorarios = new ArrayList();

    private String _diaInicio = null;

    private String _mesInicio = null;

    private String _anoInicio = null;

    private String _diaFim = null;

    private String _mesFim = null;

    private String _anoFim = null;

    private boolean _excepcaoHorario = false;

    public AssociarHorarioRotativoForm() {
    }

    public String getNome() {
        return (_nome);
    }

    public String getNumMecanografico() {
        return (_numMecanografico);
    }

    public List getListaHorarios() {
        return (_listaHorarios);
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

    public String getDiaFim() {
        return (_diaFim);
    }

    public String getMesFim() {
        return (_mesFim);
    }

    public String getAnoFim() {
        return (_anoFim);
    }

    public boolean isExcepcaoHorario() {
        return _excepcaoHorario;
    }

    public void setNome(String nome) {
        _nome = nome;
    }

    public void setNumMecanografico(String numMecanografico) {
        _numMecanografico = numMecanografico;
    }

    public void setListaListaHorarios(List listaHorarios) {
        _listaHorarios = listaHorarios;
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

    public void setDiaFim(String diaFim) {
        _diaFim = diaFim;
    }

    public void setMesFim(String mesFim) {
        _mesFim = mesFim;
    }

    public void setAnoFim(String anoFim) {
        _anoFim = anoFim;
    }

    public void setExcepcaoHorario(boolean excepcaoHorario) {
        _excepcaoHorario = excepcaoHorario;
    }

    public void setForm() {
        Calendar agora = Calendar.getInstance();
        setDiaInicio((new Integer(agora.get(Calendar.DAY_OF_MONTH))).toString());
        setMesInicio((new Integer(agora.get(Calendar.MONTH) + 1)).toString());
        setAnoInicio((new Integer(agora.get(Calendar.YEAR))).toString());
    }

    public void setForm(String nome, String numMecanografico, List rotacaoHorario,
            HashMap listaRegimesRotacao, java.util.Date dataInicio, java.util.Date dataFim,
            boolean isExcepcaoHorario) {
        setNome(nome);
        setNumMecanografico(numMecanografico);

        ListIterator iterador = rotacaoHorario.listIterator();
        List listaHorarios = new ArrayList();
        Horario horario = null;
        while (iterador.hasNext()) {
            horario = (Horario) iterador.next();

            // calculo do primeiro dia de cumprimento do horario
            Calendar primeiroDiaHorario = Calendar.getInstance();
            primeiroDiaHorario.setTimeInMillis(horario.getDataInicio().getTime());
            primeiroDiaHorario.set(Calendar.DAY_OF_MONTH, primeiroDiaHorario.get(Calendar.DAY_OF_MONTH)
                    + (horario.getPosicao() - 1));

            horario.setDataCumprir(primeiroDiaHorario.getTime());/*
                                                                  * guarda o dia
                                                                  * de inicio do
                                                                  * cumprimento
                                                                  * do horario
                                                                  */
            AssociarHorarioForm novoHorario = new AssociarHorarioForm();
            novoHorario = novoHorario.getFormHorarioPreenchido(horario, (ArrayList) listaRegimesRotacao
                    .get(new Integer(horario.getPosicao())), null);
            // o horario nao vem da base de dados logo nao necessita de subtrair
            // uma hora
            novoHorario.setDuracoesHorario(horario, false);
            listaHorarios.add(novoHorario);
            //      new AssociarHorarioForm(horario,
            // (ArrayList)listaRegimesRotacao.get(new
            // Integer(horario.getPosicao()))));

        }
        setListaListaHorarios(listaHorarios);

        // data de validade
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(dataInicio);
        setDiaInicio((new Integer(calendario.get(Calendar.DAY_OF_MONTH))).toString());
        setMesInicio((new Integer(calendario.get(Calendar.MONTH) + 1)).toString());
        setAnoInicio((new Integer(calendario.get(Calendar.YEAR))).toString());

        if (dataFim != null) {
            calendario.clear();
            calendario.setTime(dataFim);
            setDiaFim((new Integer(calendario.get(Calendar.DAY_OF_MONTH))).toString());
            setMesFim((new Integer(calendario.get(Calendar.MONTH) + 1)).toString());
            setAnoFim((new Integer(calendario.get(Calendar.YEAR))).toString());
        } else {
            setDiaFim("--");
            setMesFim("--");
            setAnoFim("----");
        }

        setExcepcaoHorario(isExcepcaoHorario);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        _numMecanografico = "";

        _diaFim = "";
        _mesFim = "";
        _anoFim = "";

        _excepcaoHorario = false;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        int diaInicioValidade = 0;
        int mesInicioValidade = 0;
        int anoInicioValidade = 0;
        int diaFimValidade = 0;
        int mesFimValidade = 0;
        int anoFimValidade = 0;

        HttpSession session = request.getSession();
        if (session.getAttribute("rotacaoHorario") == null) {
            errors.add("rotacaoHorario", new ActionError("error.rotacaoHorario.obrigatorio"));
        }

        if (request.getParameter("numMecanografico") != null) {
            if ((request.getParameter("numMecanografico")).length() < 1) {
                errors.add("numMecanografico", new ActionError("error.numero.obrigatorio"));
            } else {
                try {
                    (new Integer(getNumMecanografico())).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError("error.numero.naoInteiro"));
                }
            }
        }

        // data de validade
        Calendar calendar = Calendar.getInstance();
        if ((request.getParameter("diaInicio") != null) || (request.getParameter("mesInicio") != null)
                || (request.getParameter("anoInicio") != null)) {
            if (((request.getParameter("diaInicio")).length() < 1)
                    || ((request.getParameter("mesInicio")).length() < 1)
                    || (request.getParameter("anoInicio").length() < 1)) {
                errors.add("dates", new ActionError("error.dataValidade.obrigatoria"));
            } else {
                try {
                    diaInicioValidade = (new Integer(getDiaInicio())).intValue();
                    mesInicioValidade = (new Integer(getMesInicio())).intValue();
                    anoInicioValidade = (new Integer(getAnoInicio())).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError("error.numero.naoInteiro"));
                }
                calendar.clear();
                calendar.set(anoInicioValidade, mesInicioValidade - 1, diaInicioValidade, 00, 00, 00);
                java.util.Date dataInicio = calendar.getTime();

                if ((request.getParameter("diaFim").length() > 0)
                        && (request.getParameter("mesFim").length() > 0)
                        && (request.getParameter("anoFim").length() > 0)) {
                    try {
                        diaFimValidade = (new Integer(getDiaFim())).intValue();
                        mesFimValidade = (new Integer(getMesFim())).intValue();
                        anoFimValidade = (new Integer(getAnoFim())).intValue();
                    } catch (java.lang.NumberFormatException e) {
                        errors.add("numero", new ActionError("error.numero.naoInteiro"));
                    }

                    calendar.clear();
                    calendar.set(anoFimValidade, mesFimValidade - 1, diaFimValidade, 00, 00, 00);
                    java.util.Date dataFim = calendar.getTime();

                    if (!(dataInicio.getTime() < dataFim.getTime())) {
                        errors.add("datas", new ActionError("error.dataValidade.incorrecta"));
                    }
                }
            }
        }
        return errors;
    }
}