package net.sourceforge.fenixedu.domain;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness.IntroduzirJustificacaoForm;
import net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness.LerDadosJustificacaoForm;
import net.sourceforge.fenixedu.util.FormataCalendar;
import net.sourceforge.fenixedu.constants.assiduousness.Constants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class Ocorrencia implements IStrategyJustificacoes {
    public Ocorrencia() {
    }

    public void completaListaMarcacoes(Timestamp dataConsulta, Justificacao justificacao,
            List listaMarcacoesPonto) {
    } /* completaListaMarcacoes */

    public void setJustificacao(Justificacao justificacao, IntroduzirJustificacaoForm formJustificacao) {
        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);

        calendario.clear();
        calendario.set((new Integer(formJustificacao.getAnoInicio())).intValue(), (new Integer(
                formJustificacao.getMesInicio())).intValue() - 1, (new Integer(formJustificacao
                .getDiaInicio())).intValue(), 00, 00, 00);
        justificacao.setDiaInicio(calendario.getTime());

        if ((formJustificacao.getDiaFim() != null && formJustificacao.getDiaFim().length() > 0)
                && (formJustificacao.getMesFim() != null && formJustificacao.getMesFim().length() > 0)
                && (formJustificacao.getAnoFim() != null && formJustificacao.getAnoFim().length() > 0)) {
            calendario.clear();
            calendario.set((new Integer(formJustificacao.getAnoFim())).intValue(), (new Integer(
                    formJustificacao.getMesFim())).intValue() - 1, (new Integer(formJustificacao
                    .getDiaFim())).intValue(), 00, 00, 00);
        }
        justificacao.setDiaFim(calendario.getTime());

        if (formJustificacao.getObservacao().length() < 1) {
            justificacao.setObservacao(String.valueOf(""));
        } else {
            justificacao.setObservacao(formJustificacao.getObservacao());
        }
    } /* setJustificacao */

    public void setJustificacao(Justificacao justificacao, LerDadosJustificacaoForm formJustificacao) {
        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);

        calendario.clear();
        calendario.set((new Integer(formJustificacao.getAnoInicio())).intValue(), (new Integer(
                formJustificacao.getMesInicio())).intValue() - 1, (new Integer(formJustificacao
                .getDiaInicio())).intValue(), 00, 00, 00);
        justificacao.setDiaInicio(calendario.getTime());

        if ((formJustificacao.getDiaFim() != null && formJustificacao.getDiaFim().length() > 0)
                && (formJustificacao.getMesFim() != null && formJustificacao.getMesFim().length() > 0)
                && (formJustificacao.getAnoFim() != null && formJustificacao.getAnoFim().length() > 0)) {
            calendario.clear();
            calendario.set((new Integer(formJustificacao.getAnoFim())).intValue(), (new Integer(
                    formJustificacao.getMesFim())).intValue() - 1, (new Integer(formJustificacao
                    .getDiaFim())).intValue(), 00, 00, 00);
        }
        justificacao.setDiaFim(calendario.getTime());

        if (formJustificacao.getObservacao().length() < 1) {
            justificacao.setObservacao(String.valueOf(""));
        } else {
            justificacao.setObservacao(formJustificacao.getObservacao());
        }
    } /* setJustificacao */

    public void setListaJustificacoesBody(ParamJustificacao paramJustificacao,
            Justificacao justificacao, List listaJustificacoesBody) {
        Calendar calendario = Calendar.getInstance();

        listaJustificacoesBody.add(1, paramJustificacao.getSigla());
        listaJustificacoesBody.add(2, paramJustificacao.getDescricao());
        listaJustificacoesBody.add(3, paramJustificacao.getTipo());

        calendario.clear();
        calendario.setTime(justificacao.getDiaInicio());
        listaJustificacoesBody.add(4, FormataCalendar.data(calendario));

        if (justificacao.getDiaFim() != null) {
            calendario.clear();
            calendario.setTime(justificacao.getDiaFim());
            listaJustificacoesBody.add(5, FormataCalendar.data(calendario));
        } else {
            listaJustificacoesBody.add(5, new String("&nbsp;"));
        }
        listaJustificacoesBody.add(6, new String("&nbsp;"));
        listaJustificacoesBody.add(7, new String("&nbsp;"));

    } /* setListaJustificacoesBody */

    public void updateSaldosHorarioVerbeteBody(Justificacao justificacao,
            ParamJustificacao paramJustificacao, Horario horario, List listaRegimes,
            List listaMarcacoesPonto, List listaSaldos) {
        /* justificacoes de ocorrencia justificam o dia todo */
        listaSaldos.set(0, new Long(0));
        listaSaldos.set(1, new Long(0));
    } /* updateSaldosHorarioVerbeteBody */

    public ActionErrors validateFormJustificacao(IntroduzirJustificacaoForm formJustificacao) {
        /*
         * nao preenche horas (justificacao do dia todo) pode preencher as duas
         * datas ou so a inicial
         */
        ActionErrors errors = new ActionErrors();

        int diaInicio = 0;
        int mesInicio = 0;
        int anoInicio = 0;
        int diaFim = 0;
        int mesFim = 0;
        int anoFim = 0;

        if ((formJustificacao.getHoraInicio().length() > 0)
                || (formJustificacao.getMinutosInicio().length() > 0)
                || (formJustificacao.getHoraFim().length() > 0)
                || (formJustificacao.getMinutosFim().length() > 0)) {
            errors.add("intervaloJustificacao", new ActionError(
                    "error.intervaloJustificacao.naoPermitido"));
        } else {
            if ((formJustificacao.getDiaInicio().length() < 1)
                    || (formJustificacao.getMesInicio().length() < 1)
                    || (formJustificacao.getAnoInicio().length() < 1)) {
                errors.add("dates", new ActionError("error.dataInicial.obrigatoria"));
            } else {
                try {
                    diaInicio = (new Integer(formJustificacao.getDiaInicio())).intValue();
                    mesInicio = (new Integer(formJustificacao.getMesInicio())).intValue();
                    anoInicio = (new Integer(formJustificacao.getAnoInicio())).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError("error.numero.naoInteiro"));
                }
                if ((formJustificacao.getDiaFim() != null && formJustificacao.getDiaFim().length() > 0)
                        && (formJustificacao.getMesFim() != null && formJustificacao.getMesFim()
                                .length() > 0)
                        && (formJustificacao.getAnoFim() != null && formJustificacao.getAnoFim()
                                .length() > 0)) {
                    //					errors.add("dates", new
                    // ActionError("error.campos.justificacaoHoras"));
                    //				} else {

                    try {
                        diaFim = (new Integer(formJustificacao.getDiaFim())).intValue();
                        mesFim = (new Integer(formJustificacao.getMesFim())).intValue();
                        anoFim = (new Integer(formJustificacao.getAnoFim())).intValue();
                    } catch (java.lang.NumberFormatException e) {
                        errors.add("numero", new ActionError("error.numero.naoInteiro"));
                    }
                }
                try {
                    Calendar calendarInicio = Calendar.getInstance();
                    Calendar calendarFim = Calendar.getInstance();
                    calendarInicio.setLenient(false);
                    calendarFim.setLenient(false);

                    calendarInicio.clear();
                    calendarInicio.set(anoInicio, mesInicio - 1, diaInicio, 00, 00, 00);

                    if ((formJustificacao.getDiaFim() != null && formJustificacao.getDiaFim().length() > 0)
                            && (formJustificacao.getMesFim() != null && formJustificacao.getMesFim()
                                    .length() > 0)
                            && (formJustificacao.getAnoFim() != null && formJustificacao.getAnoFim()
                                    .length() > 0)) {
                        calendarFim.clear();
                        calendarFim.set(anoFim, mesFim - 1, diaFim, 00, 00, 00);

                        if (!(calendarInicio.getTimeInMillis() <= calendarFim.getTimeInMillis())) {
                            errors.add("datas", new ActionError("error.dataValidade.incorrecta"));
                        }
                    }
                } catch (java.lang.IllegalArgumentException ee) {
                    errors.add("datas", new ActionError("error.dataValidade.incorrecta"));
                }
            }
        }
        return errors;
    } /* validateFormJustificacao */

    public ActionErrors validateFormJustificacao(LerDadosJustificacaoForm formJustificacao) {
        /*
         * nao preenche horas (justificacao do dia todo) pode preencher as duas
         * datas ou so a inicial isto acontece teoricamente e nao esta testado
         * na exaustao
         */
        ActionErrors errors = new ActionErrors();

        int diaInicio = 0;
        int mesInicio = 0;
        int anoInicio = 0;
        int diaFim = 0;
        int mesFim = 0;
        int anoFim = 0;

        if ((formJustificacao.getHoraInicio().length() > 0)
                || (formJustificacao.getMinutosInicio().length() > 0)
                || (formJustificacao.getHoraFim().length() > 0)
                || (formJustificacao.getMinutosFim().length() > 0)) {
            errors.add("intervaloJustificacao", new ActionError(
                    "error.intervaloJustificacao.naoPermitido"));
        } else {
            if ((formJustificacao.getDiaInicio().length() < 1)
                    || (formJustificacao.getMesInicio().length() < 1)
                    || (formJustificacao.getAnoInicio().length() < 1)) {
                errors.add("dates", new ActionError("error.dataInicial.obrigatoria"));
            } else {
                try {
                    diaInicio = (new Integer(formJustificacao.getDiaInicio())).intValue();
                    mesInicio = (new Integer(formJustificacao.getMesInicio())).intValue();
                    anoInicio = (new Integer(formJustificacao.getAnoInicio())).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError("error.numero.naoInteiro"));
                }
                if ((formJustificacao.getDiaFim() != null && formJustificacao.getDiaFim().length() > 0)
                        && (formJustificacao.getMesFim() != null && formJustificacao.getMesFim()
                                .length() > 0)
                        && (formJustificacao.getAnoFim() != null && formJustificacao.getAnoFim()
                                .length() > 0)) {
                    try {
                        diaFim = (new Integer(formJustificacao.getDiaFim())).intValue();
                        mesFim = (new Integer(formJustificacao.getMesFim())).intValue();
                        anoFim = (new Integer(formJustificacao.getAnoFim())).intValue();
                    } catch (java.lang.NumberFormatException e) {
                        errors.add("numero", new ActionError("error.numero.naoInteiro"));
                    }
                }
                try {
                    Calendar calendarInicio = Calendar.getInstance();
                    Calendar calendarFim = Calendar.getInstance();
                    calendarInicio.setLenient(false);
                    calendarFim.setLenient(false);

                    calendarInicio.clear();
                    calendarInicio.set(anoInicio, mesInicio - 1, diaInicio, 00, 00, 00);

                    if ((formJustificacao.getDiaFim() != null && formJustificacao.getDiaFim().length() > 0)
                            && (formJustificacao.getMesFim() != null && formJustificacao.getMesFim()
                                    .length() > 0)
                            && (formJustificacao.getAnoFim() != null && formJustificacao.getAnoFim()
                                    .length() > 0)) {
                        calendarFim.clear();
                        calendarFim.set(anoFim, mesFim - 1, diaFim, 00, 00, 00);

                        if (!(calendarInicio.getTimeInMillis() <= calendarFim.getTimeInMillis())) {
                            errors.add("datas", new ActionError("error.dataValidade.incorrecta"));
                        }
                    }
                } catch (java.lang.IllegalArgumentException ee) {
                    errors.add("datas", new ActionError("error.dataValidade.incorrecta"));
                }
            }
        }
        return errors;
    } /* validateFormJustificacao */

    public String formataDuracao(Horario horario, Justificacao justificacao) {
        String duracao = new String();
        if (!((horario.getSigla() != null) && (horario.getSigla().equals(Constants.DSC)
                || horario.getSigla().equals(Constants.DS) || horario.getSigla().equals(
                Constants.FERIADO)))) {

            IStrategyHorarios tipoHorario = SuporteStrategyHorarios.getInstance().callStrategy(
                    horario.getModalidade());

            Calendar calendario = Calendar.getInstance();
            calendario.setLenient(false);
            calendario.clear();
            calendario.setTimeInMillis(tipoHorario.duracaoDiaria(horario));
            calendario.add(Calendar.HOUR_OF_DAY, -1);
            Time diferenca = new Time(calendario.getTimeInMillis());

            duracao = diferenca.toString().substring(0, diferenca.toString().lastIndexOf(":"));
        } else {
            duracao = "00:00";
        }
        int indice = duracao.length();
        while (indice < Constants.MAX_DURACAO) {
            duracao = "0" + duracao;

            indice++;
        }

        return duracao;
    } /* formataDuracao */

    public void setLerDadosJustificacaoForm(Justificacao justificacao, ActionForm form) {
        LerDadosJustificacaoForm formJustificacao = (LerDadosJustificacaoForm) form;
        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);

        calendario.clear();
        calendario.setTime(justificacao.getDiaInicio());
        formJustificacao.setDiaInicio(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
        formJustificacao.setMesInicio(String.valueOf(calendario.get(Calendar.MONTH) + 1));
        formJustificacao.setAnoInicio(String.valueOf(calendario.get(Calendar.YEAR)));

        calendario.clear();
        calendario.setTime(justificacao.getDiaFim());
        formJustificacao.setDiaFim(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
        formJustificacao.setMesFim(String.valueOf(calendario.get(Calendar.MONTH) + 1));
        formJustificacao.setAnoFim(String.valueOf(calendario.get(Calendar.YEAR)));

        if (justificacao.getObservacao() == null || justificacao.getObservacao().length() < 1) {
            formJustificacao.setObservacao(String.valueOf(""));
        } else {
            formJustificacao.setObservacao(justificacao.getObservacao());
        }
    } /* setLerDadosJustificacaoForm */
}