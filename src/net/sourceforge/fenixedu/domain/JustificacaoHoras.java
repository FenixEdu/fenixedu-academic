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
public class JustificacaoHoras implements IStrategyJustificacoes {
    public JustificacaoHoras() {
    }

    public void completaListaMarcacoes(Timestamp dataConsulta, Justificacao justificacao,
            List listaMarcacoesPonto) {

        MarcacaoPonto entrada = new MarcacaoPonto(0, new Timestamp(dataConsulta.getTime()
                + justificacao.getHoraInicio().getTime() + 3600 * 1000), 0, 0);
        MarcacaoPonto saida = new MarcacaoPonto(0, new Timestamp(dataConsulta.getTime()
                + justificacao.getHoraFim().getTime() + 3600 * 1000), 0, 0);

        listaMarcacoesPonto.add(entrada);
        listaMarcacoesPonto.add(saida);
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

        calendario.clear();
        calendario.set(1970, 0, 1, new Integer(formJustificacao.getHoraInicio()).intValue(),
                new Integer(formJustificacao.getMinutosInicio()).intValue(), 00);
        justificacao.setHoraInicio(new Time(calendario.getTimeInMillis()));

        calendario.clear();
        calendario.set(1970, 0, 1, new Integer(formJustificacao.getHoraFim()).intValue(), new Integer(
                formJustificacao.getMinutosFim()).intValue(), 00);
        justificacao.setHoraFim(new Time(calendario.getTimeInMillis()));

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

        calendario.clear();
        calendario.set(1970, 0, 1, new Integer(formJustificacao.getHoraInicio()).intValue(),
                new Integer(formJustificacao.getMinutosInicio()).intValue(), 00);
        justificacao.setHoraInicio(new Time(calendario.getTimeInMillis()));

        calendario.clear();
        calendario.set(1970, 0, 1, new Integer(formJustificacao.getHoraFim()).intValue(), new Integer(
                formJustificacao.getMinutosFim()).intValue(), 00);
        justificacao.setHoraFim(new Time(calendario.getTimeInMillis()));

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
        calendario.clear();
        calendario.setTimeInMillis(justificacao.getHoraInicio().getTime());
        listaJustificacoesBody.add(6, FormataCalendar.horasMinutos(calendario));

        calendario.clear();
        calendario.setTimeInMillis(justificacao.getHoraFim().getTime());
        listaJustificacoesBody.add(7, FormataCalendar.horasMinutos(calendario));

    } /* setListaJustificacoesBody */

    public void updateSaldosHorarioVerbeteBody(Justificacao justificacao,
            ParamJustificacao paramJustificacao, Horario horario, List listaRegimes,
            List listaMarcacoesPonto, List listaSaldos) {

        Float duracaoDiaria = new Float(horario.getDuracaoSemanal() / Constants.SEMANA_TRABALHO_FLEXIVEL);
        long saldoJustificacao = justificacao.getHoraFim().getTime()
                - justificacao.getHoraInicio().getTime();

        saldoJustificacao = saldoJustificacao
                - (duracaoDiaria.intValue() * 3600 * 1000 + new Float(
                        (duracaoDiaria.floatValue() - duracaoDiaria.intValue()) * 3600 * 1000)
                        .longValue());

        //Justificacao de horas, mas acaba por funcionar como uma ocorrencia
        //o saldo é superior à duracao diaria e a lista de marcacoes foi
        // completado com a justificacao
        if (saldoJustificacao > 0 && listaMarcacoesPonto.size() == 2) {
            //saldo do horario normal
            listaSaldos.set(0, new Long(0));
            listaSaldos.set(1, new Long(0));
        }
    } /* updateSaldosHorarioVerbeteBody */

    public ActionErrors validateFormJustificacao(IntroduzirJustificacaoForm formJustificacao) {
        /*
         * preenche hora inicio e hora fim preenche data inicio permite haver
         * varios dias a justificar algumas horas isto acontece teoricamente e
         * nao esta testado na exaustao
         */
        ActionErrors errors = new ActionErrors();

        int horaInicio = 0;
        int minutosInicio = 0;
        int diaInicio = 0;
        int mesInicio = 0;
        int anoInicio = 0;
        int horaFim = 0;
        int minutosFim = 0;
        int diaFim = 0;
        int mesFim = 0;
        int anoFim = 0;

        if ((formJustificacao.getHoraInicio().length() < 1)
                || (formJustificacao.getMinutosInicio().length() < 1)
                || (formJustificacao.getDiaInicio().length() < 1)
                || (formJustificacao.getMesInicio().length() < 1)
                || (formJustificacao.getAnoInicio().length() < 1)
                || (formJustificacao.getHoraFim().length() < 1)
                || (formJustificacao.getMinutosFim().length() < 1)) {
            errors.add("dates", new ActionError("error.campos.justificacaoHoras"));
        } else {
            if ((formJustificacao.getDiaFim() != null && formJustificacao.getDiaFim().length() > 0)
                    && (formJustificacao.getMesFim() != null && formJustificacao.getMesFim().length() > 0)
                    && (formJustificacao.getAnoFim() != null && formJustificacao.getAnoFim().length() > 0)) {
                try {
                    diaFim = (new Integer(formJustificacao.getDiaFim())).intValue();
                    mesFim = (new Integer(formJustificacao.getMesFim())).intValue();
                    anoFim = (new Integer(formJustificacao.getAnoFim())).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError("error.numero.naoInteiro"));
                }
            }
            try {
                horaInicio = (new Integer(formJustificacao.getHoraInicio())).intValue();
                minutosInicio = (new Integer(formJustificacao.getMinutosInicio())).intValue();
                diaInicio = (new Integer(formJustificacao.getDiaInicio())).intValue();
                mesInicio = (new Integer(formJustificacao.getMesInicio())).intValue();
                anoInicio = (new Integer(formJustificacao.getAnoInicio())).intValue();
                horaFim = (new Integer(formJustificacao.getHoraFim())).intValue();
                minutosFim = (new Integer(formJustificacao.getMinutosFim())).intValue();
            } catch (java.lang.NumberFormatException e) {
                errors.add("numero", new ActionError("error.numero.naoInteiro"));
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
                calendarInicio.clear();
                calendarInicio.set(1970, 0, 1, horaInicio, minutosInicio, 00);

                calendarFim.clear();
                calendarFim.set(1970, 0, 1, horaFim, minutosFim, 00);

                if (!(calendarInicio.before(calendarFim))) {
                    errors.add("horas", new ActionError("error.intervaloJustificacao.incorrecto"));
                }
            } catch (java.lang.IllegalArgumentException ee) {
                errors.add("datas", new ActionError("error.data.horas"));
            }
        }
        return errors;
    } /* validateFormJustificacao */

    public ActionErrors validateFormJustificacao(LerDadosJustificacaoForm formJustificacao) {
        /*
         * preenche hoea inicio e hora fim preenche data inicio permite haver
         * varios dias a justificar algumas horas isto acontece teoricamente e
         * nao esta testado na exaustao
         */
        ActionErrors errors = new ActionErrors();

        int horaInicio = 0;
        int minutosInicio = 0;
        int diaInicio = 0;
        int mesInicio = 0;
        int anoInicio = 0;
        int horaFim = 0;
        int minutosFim = 0;
        int diaFim = 0;
        int mesFim = 0;
        int anoFim = 0;

        if ((formJustificacao.getHoraInicio().length() < 1)
                || (formJustificacao.getMinutosInicio().length() < 1)
                || (formJustificacao.getDiaInicio().length() < 1)
                || (formJustificacao.getMesInicio().length() < 1)
                || (formJustificacao.getAnoInicio().length() < 1)
                || (formJustificacao.getHoraFim().length() < 1)
                || (formJustificacao.getMinutosFim().length() < 1)) {
            errors.add("dates", new ActionError("error.campos.justificacaoHoras"));
        } else {
            if ((formJustificacao.getDiaFim() != null && formJustificacao.getDiaFim().length() > 0)
                    && (formJustificacao.getMesFim() != null && formJustificacao.getMesFim().length() > 0)
                    && (formJustificacao.getAnoFim() != null && formJustificacao.getAnoFim().length() > 0)) {
                try {
                    diaFim = (new Integer(formJustificacao.getDiaFim())).intValue();
                    mesFim = (new Integer(formJustificacao.getMesFim())).intValue();
                    anoFim = (new Integer(formJustificacao.getAnoFim())).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError("error.numero.naoInteiro"));
                }
            }
            try {
                horaInicio = (new Integer(formJustificacao.getHoraInicio())).intValue();
                minutosInicio = (new Integer(formJustificacao.getMinutosInicio())).intValue();
                diaInicio = (new Integer(formJustificacao.getDiaInicio())).intValue();
                mesInicio = (new Integer(formJustificacao.getMesInicio())).intValue();
                anoInicio = (new Integer(formJustificacao.getAnoInicio())).intValue();
                horaFim = (new Integer(formJustificacao.getHoraFim())).intValue();
                minutosFim = (new Integer(formJustificacao.getMinutosFim())).intValue();
            } catch (java.lang.NumberFormatException e) {
                errors.add("numero", new ActionError("error.numero.naoInteiro"));
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
                calendarInicio.clear();
                calendarInicio.set(1970, 0, 1, horaInicio, minutosInicio, 00);

                calendarFim.clear();
                calendarFim.set(1970, 0, 1, horaFim, minutosFim, 00);

                if (!(calendarInicio.before(calendarFim))) {
                    errors.add("horas", new ActionError("error.intervaloJustificacao.incorrecto"));
                }
            } catch (java.lang.IllegalArgumentException ee) {
                errors.add("datas", new ActionError("error.data.horas"));
            }
        }
        return errors;
    } /* validateFormJustificacao */

    public String formataDuracao(Horario horario, Justificacao justificacao) {

        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);
        calendario.clear();
        calendario.setTimeInMillis(justificacao.getHoraFim().getTime()
                - justificacao.getHoraInicio().getTime());
        calendario.add(Calendar.HOUR_OF_DAY, -1);
        Time diferenca = new Time(calendario.getTimeInMillis());

        String duracao = diferenca.toString().substring(0, diferenca.toString().lastIndexOf(":"));

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

        calendario.clear();
        calendario.setTime(justificacao.getHoraInicio());
        formJustificacao.setHoraInicio(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
        formJustificacao.setMinutosInicio(String.valueOf(calendario.get(Calendar.MINUTE)));

        calendario.clear();
        calendario.setTime(justificacao.getHoraFim());
        formJustificacao.setHoraFim(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
        formJustificacao.setMinutosFim(String.valueOf(calendario.get(Calendar.MINUTE)));

        if (justificacao.getObservacao() == null || justificacao.getObservacao().length() < 1) {
            formJustificacao.setObservacao(String.valueOf(""));
        } else {
            formJustificacao.setObservacao(justificacao.getObservacao());
        }
    } /* setLerDadosJustificacaoForm */
}