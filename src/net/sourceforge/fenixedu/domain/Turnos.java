package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness.AssociarHorarioForm;
import net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness.AssociarHorarioTipoForm;
import net.sourceforge.fenixedu.constants.assiduousness.Constants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;


/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class Turnos implements IStrategyHorarios {

    public Turnos() {
    }

    public ActionErrors validateAssociarHorario(ActionForm form) {
        ActionErrors errors = new ActionErrors();

        AssociarHorarioForm formHorario = (AssociarHorarioForm) form;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);

        List listaRegime = null;

        float duracaoSemanal = 0;

        int inicioExpedienteHoras = 0;
        int inicioExpedienteMinutos = 0;
        int fimExpedienteHoras = 0;
        int fimExpedienteMinutos = 0;

        long timeInicioExpediente = 0;
        long timeFimExpediente = 0;

        int inicioHN1Horas = 0;
        int inicioHN1Minutos = 0;
        int fimHN1Horas = 0;
        int fimHN1Minutos = 0;

        long timeInicioHN1 = 0;
        long timeFimHN1 = 0;

        long timeHNPeriodo = 0;

        int diaInicio = 0;
        int mesInicio = 0;
        int anoInicio = 0;
        int diaFim = 0;
        int mesFim = 0;
        int anoFim = 0;

        try {
            if (formHorario.getDuracaoSemanal().length() < 1) {
                errors.add("DuracaoSemanal-HTurnos", new ActionError(
                        "error.duracaoSemanal.obrigatoria"));
            } else {
                try {
                    duracaoSemanal = (new Float(formHorario.getDuracaoSemanal()))
                            .floatValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError(
                            "error.numero.naoInteiro"));
                }
                if (duracaoSemanal != Constants.DURACAO_SEMANAL_TURNOS) {
                    errors.add("DuracaoSemanal-HTurnos", new ActionError(
                            "error.duracaoSemanal"));
                }
            }

            if ((formHorario.getInicioExpedienteHoras().length() < 1)
                    || (formHorario.getInicioExpedienteMinutos().length() < 1)
                    || (formHorario.getFimExpedienteHoras().length() < 1)
                    || (formHorario.getFimExpedienteMinutos().length() < 1)) {
                errors.add("Expediente", new ActionError(
                        "error.Expediente.obrigatorio"));
            } else {
                try {
                    inicioExpedienteHoras = (new Integer(formHorario
                            .getInicioExpedienteHoras())).intValue();
                    inicioExpedienteMinutos = (new Integer(formHorario
                            .getInicioExpedienteMinutos())).intValue();
                    fimExpedienteHoras = (new Integer(formHorario
                            .getFimExpedienteHoras())).intValue();
                    fimExpedienteMinutos = (new Integer(formHorario
                            .getFimExpedienteMinutos())).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError(
                            "error.numero.naoInteiro"));
                }

                calendar.clear();
                if (formHorario.getDiaAnteriorExpediente().length() < 1) {
                    calendar.set(1970, 0, 1, inicioExpedienteHoras,
                            inicioExpedienteMinutos, 00);
                    timeInicioExpediente = calendar.getTimeInMillis();
                } else {
                    //contagem no dia anterior
                    calendar.set(1969, 11, 31, inicioExpedienteHoras,
                            inicioExpedienteMinutos, 00);
                    timeInicioExpediente = calendar.getTimeInMillis();
                }

                calendar.clear();
                if (formHorario.getDiaSeguinteExpediente().length() < 1) {
                    calendar.set(1970, 0, 1, fimExpedienteHoras,
                            fimExpedienteMinutos, 00);
                    timeFimExpediente = calendar.getTimeInMillis();
                } else {
                    //contagem no dia seguinte
                    calendar.set(1970, 0, 2, fimExpedienteHoras,
                            fimExpedienteMinutos, 00);
                    timeFimExpediente = calendar.getTimeInMillis();
                }

                if (!(timeInicioExpediente < timeFimExpediente)) {
                    errors.add("Expediente",
                            new ActionError("error.Expediente"));
                } else if ((timeInicioExpediente < Constants.EXPEDIENTE_MINIMO)
                        || (timeFimExpediente > Constants.EXPEDIENTE_MAXIMO)) {
                    errors.add("Expediente",
                            new ActionError("error.Expediente"));
                }
            }

            if ((formHorario.getInicioRefeicaoHoras().length() > 0)
                    || (formHorario.getInicioRefeicaoMinutos().length() > 0)
                    || (formHorario.getFimRefeicaoHoras().length() > 0)
                    || (formHorario.getFimRefeicaoMinutos().length() > 0)
                    || (formHorario.getIntervaloMinimoHoras().length() > 0)
                    || (formHorario.getIntervaloMinimoMinutos().length() > 0)
                    || (formHorario.getDescontoObrigatorioHoras().length() > 0)
                    || (formHorario.getDescontoObrigatorioMinutos().length() > 0)) {
                errors.add("Intervalo de Refeicao", new ActionError(
                        "error.refeicao.naoObrigatorio"));
            }
            if ((formHorario.getTrabalhoConsecutivoHoras().length() > 0)
                    || (formHorario.getTrabalhoConsecutivoMinutos().length() > 0)) {
                errors.add("Trabalho Consecutivo", new ActionError(
                        "error.trabalhoConsecutivo.naoPermitido"));
            }

            // Horario normal
            if ((formHorario.getInicioHN1Horas().length() < 1)
                    || (formHorario.getInicioHN1Minutos().length() < 1)
                    || (formHorario.getFimHN1Horas().length() < 1)
                    || (formHorario.getFimHN1Minutos().length() < 1)) {
                errors.add("HorarioNormal", new ActionError(
                        "error.HorarioNormal1.obrigatorio"));
            } else {
                try {
                    inicioHN1Horas = (new Integer(formHorario
                            .getInicioHN1Horas())).intValue();
                    inicioHN1Minutos = (new Integer(formHorario
                            .getInicioHN1Minutos())).intValue();
                    fimHN1Horas = (new Integer(formHorario.getFimHN1Horas()))
                            .intValue();
                    fimHN1Minutos = (new Integer(formHorario.getFimHN1Minutos()))
                            .intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError(
                            "error.numero.naoInteiro"));
                }

                calendar.clear();
                if (formHorario.getDiaAnteriorHN1().length() < 1) {
                    calendar.set(1970, 0, 1, inicioHN1Horas, inicioHN1Minutos,
                            00);
                } else {
                    // contagem no dia anterior
                    calendar.set(1969, 11, 31, inicioHN1Horas,
                            inicioHN1Minutos, 00);
                }
                timeInicioHN1 = calendar.getTimeInMillis();

                calendar.clear();
                if (formHorario.getDiaSeguinteHN1().length() < 1) {
                    calendar.set(1970, 0, 1, fimHN1Horas, fimHN1Minutos, 00);
                } else {
                    // contagem no dia seguinte
                    calendar.set(1970, 0, 2, fimHN1Horas, fimHN1Minutos, 00);
                }
                timeFimHN1 = calendar.getTimeInMillis();

                if (timeInicioExpediente != 0) {
                    if (!(timeInicioExpediente < timeInicioHN1)) {
                        errors
                                .add(
                                        "ExpedienteHN",
                                        new ActionError(
                                                "error.ExpedienteHorarioNormal.inconsistentes"));
                    }
                }

                if (timeFimExpediente != 0) {
                    if (!(timeFimExpediente > timeFimHN1)) {
                        errors
                                .add(
                                        "ExpedienteHN",
                                        new ActionError(
                                                "error.ExpedienteHorarioNormal.inconsistentes"));
                    }
                }

                if (!(timeInicioHN1 < timeFimHN1)) {
                    errors.add("HN", new ActionError("error.HorarioNormal"));
                } else {
                    // 1 periodo de trabalho de 7 horas
                    timeHNPeriodo = ((new Float(
                            Constants.DURACAO_SEMANAL_TURNOS
                                    / Constants.SEMANA_TRABALHO_TURNOS)
                            .intValue() * 3600 * 1000) + new Float((new Float(
                            Constants.DURACAO_SEMANAL_TURNOS
                                    / Constants.SEMANA_TRABALHO_TURNOS)
                            .floatValue() - new Float(
                            Constants.DURACAO_SEMANAL_TURNOS
                                    / Constants.SEMANA_TRABALHO_TURNOS)
                            .intValue()) * 3600 * 1000).longValue());
                    if (!((timeFimHN1 - timeInicioHN1) == timeHNPeriodo)) {
                        errors.add("HNPeriodo", new ActionError(
                                "error.HorarioNormal.duracao"));
                    }
                }
            }

            if ((formHorario.getInicioHN2Horas().length() > 0)
                    || (formHorario.getInicioHN2Minutos().length() > 0)
                    || (formHorario.getFimHN2Horas().length() > 0)
                    || (formHorario.getFimHN2Minutos().length() > 0)) {
                errors.add("HorarioNormal", new ActionError(
                        "error.HorarioNormal2.naoPermitido"));
            }

            // Periodos fixos
            if ((formHorario.getInicioPF1Horas().length() > 0)
                    || (formHorario.getInicioPF1Minutos().length() > 0)
                    || (formHorario.getFimPF1Horas().length() > 0)
                    || (formHorario.getFimPF1Minutos().length() > 0)
                    || (formHorario.getInicioPF2Horas().length() > 0)
                    || (formHorario.getInicioPF2Minutos().length() > 0)
                    || (formHorario.getFimPF2Horas().length() > 0)
                    || (formHorario.getFimPF2Minutos().length() > 0)) {
                errors.add("periodoFixo", new ActionError(
                        "error.PF.desnecessario"));
            }

            // regime
            if (formHorario.getRegime().length < 0) {
                errors.add("regime",
                        new ActionError("error.regime.obrigatorio"));
            } else {
                listaRegime = Arrays.asList(formHorario.getRegime());

                if (listaRegime.contains(Constants.REGIME_NORMAL)) {
                    errors.add("regime", new ActionError(
                            "error.regime.naoPermitido"));
                }

                if (!listaRegime.contains(Constants.REGIME_IPF)) {
                    errors.add("periodoFixo", new ActionError(
                            "error.regime.IPFobrigatorio"));
                }
            }

            // data de validade
            if (!formHorario.isExcepcaoHorario()) {
                if ((formHorario.getDiaInicio() != null)
                        && (formHorario.getMesInicio() != null)
                        && (formHorario.getAnoInicio() != null)) {
                    if ((formHorario.getDiaInicio().length() < 1)
                            || (formHorario.getMesInicio().length() < 1)
                            || (formHorario.getAnoInicio().length() < 1)) {
                        errors.add("dates", new ActionError(
                                "error.dataValidade.obrigatoria"));
                    } else {
                        try {
                            diaInicio = (new Integer(formHorario.getDiaInicio()))
                                    .intValue();
                            mesInicio = (new Integer(formHorario.getMesInicio()))
                                    .intValue();
                            anoInicio = (new Integer(formHorario.getAnoInicio()))
                                    .intValue();
                        } catch (java.lang.NumberFormatException e) {
                            errors.add("numero", new ActionError(
                                    "error.numero.naoInteiro"));
                        }
                        calendar.clear();
                        calendar.set(anoInicio, mesInicio - 1, diaInicio, 00,
                                00, 00);
                        java.util.Date dataInicio = calendar.getTime();

                        if ((formHorario.getDiaFim() != null)
                                && (formHorario.getMesFim() != null)
                                && (formHorario.getAnoFim() != null)) {
                            if ((formHorario.getDiaFim().length() > 0)
                                    && (formHorario.getMesFim().length() > 0)
                                    && (formHorario.getAnoFim().length() > 0)) {
                                try {
                                    diaFim = (new Integer(formHorario
                                            .getDiaFim())).intValue();
                                    mesFim = (new Integer(formHorario
                                            .getMesFim())).intValue();
                                    anoFim = (new Integer(formHorario
                                            .getAnoFim())).intValue();
                                } catch (java.lang.NumberFormatException e) {
                                    errors.add("numero", new ActionError(
                                            "error.numero.naoInteiro"));
                                }
                                calendar.clear();
                                calendar.set(anoFim, mesFim - 1, diaFim, 00,
                                        00, 00);
                                java.util.Date dataFim = calendar.getTime();

                                if (!(dataInicio.getTime() < dataFim.getTime())) {
                                    errors.add("datas", new ActionError(
                                            "error.dataValidade.incorrecta"));
                                }
                            }
                        }
                    }
                }
            } else {
                if ((formHorario.getDiaInicio() != null)
                        && (formHorario.getMesInicio() != null)
                        && (formHorario.getAnoInicio() != null)
                        && (formHorario.getDiaFim() != null)
                        && (formHorario.getMesFim() != null)
                        && (formHorario.getAnoFim() != null)) {
                    if ((formHorario.getDiaInicio().length() < 1)
                            || (formHorario.getMesInicio().length() < 1)
                            || (formHorario.getAnoInicio().length() < 1)
                            || (formHorario.getDiaFim().length() < 1)
                            || (formHorario.getMesFim().length() < 1)
                            || (formHorario.getAnoFim().length() < 1)) {
                        errors.add("dates", new ActionError(
                                "error.dataValidade.obrigatoria"));
                    } else {
                        try {
                            diaInicio = (new Integer(formHorario.getDiaInicio()))
                                    .intValue();
                            mesInicio = (new Integer(formHorario.getMesInicio()))
                                    .intValue();
                            anoInicio = (new Integer(formHorario.getAnoInicio()))
                                    .intValue();
                            diaFim = (new Integer(formHorario.getDiaFim()))
                                    .intValue();
                            mesFim = (new Integer(formHorario.getMesFim()))
                                    .intValue();
                            anoFim = (new Integer(formHorario.getAnoFim()))
                                    .intValue();
                        } catch (java.lang.NumberFormatException e) {
                            errors.add("numero", new ActionError(
                                    "error.numero.naoInteiro"));
                        }

                        Calendar calendarInicio = Calendar.getInstance();
                        Calendar calendarFim = Calendar.getInstance();

                        calendarInicio.clear();
                        calendarInicio.set(anoInicio, mesInicio - 1, diaInicio,
                                00, 00, 00);
                        java.util.Date dataInicio = calendarInicio.getTime();

                        calendarFim.clear();
                        calendarFim.set(anoFim, mesFim - 1, diaFim, 00, 00, 00);
                        java.util.Date dataFim = calendarFim.getTime();

                        if (!(dataInicio.getTime() <= dataFim.getTime())) {
                            errors.add("datas", new ActionError(
                                    "error.dataValidade.incorrecta"));
                        }
                    }
                }
            }
        } catch (java.lang.IllegalArgumentException e) {
            errors.add("horasData", new ActionError("error.data.horas"));
        }
        return errors;
    } /* validateAssociarHorario */

    public ActionErrors validateAssociarHorarioTipo(ActionForm form,
            HorarioTipo horarioTipo, ArrayList listaRegimes) {
        ActionErrors errors = new ActionErrors();

        AssociarHorarioTipoForm formHorario = (AssociarHorarioTipoForm) form;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);

        int diaInicio = 0;
        int mesInicio = 0;
        int anoInicio = 0;
        int diaFim = 0;
        int mesFim = 0;
        int anoFim = 0;

        try {
            // data de validade
            if (!formHorario.isExcepcaoHorario()) {
                if ((formHorario.getDiaInicio() != null)
                        && (formHorario.getMesInicio() != null)
                        && (formHorario.getAnoInicio() != null)) {
                    if ((formHorario.getDiaInicio().length() < 1)
                            || (formHorario.getMesInicio().length() < 1)
                            || (formHorario.getAnoInicio().length() < 1)) {
                        errors.add("dates", new ActionError(
                                "error.dataValidade.obrigatoria"));
                    } else {
                        try {
                            diaInicio = (new Integer(formHorario.getDiaInicio()))
                                    .intValue();
                            mesInicio = (new Integer(formHorario.getMesInicio()))
                                    .intValue();
                            anoInicio = (new Integer(formHorario.getAnoInicio()))
                                    .intValue();
                        } catch (java.lang.NumberFormatException e) {
                            errors.add("numero", new ActionError(
                                    "error.numero.naoInteiro"));
                        }
                        calendar.clear();
                        calendar.set(anoInicio, mesInicio - 1, diaInicio, 00,
                                00, 00);
                        java.util.Date dataInicio = calendar.getTime();

                        if ((formHorario.getDiaFim() != null)
                                && (formHorario.getMesFim() != null)
                                && (formHorario.getAnoFim() != null)) {
                            if ((formHorario.getDiaFim().length() > 0)
                                    && (formHorario.getMesFim().length() > 0)
                                    && (formHorario.getAnoFim().length() > 0)) {
                                try {
                                    diaFim = (new Integer(formHorario
                                            .getDiaFim())).intValue();
                                    mesFim = (new Integer(formHorario
                                            .getMesFim())).intValue();
                                    anoFim = (new Integer(formHorario
                                            .getAnoFim())).intValue();
                                } catch (java.lang.NumberFormatException e) {
                                    errors.add("numero", new ActionError(
                                            "error.numero.naoInteiro"));
                                }

                                calendar.clear();
                                calendar.set(anoFim, mesFim - 1, diaFim, 00,
                                        00, 00);
                                java.util.Date dataFim = calendar.getTime();

                                if (!(dataInicio.getTime() < dataFim.getTime())) {
                                    errors.add("datas", new ActionError(
                                            "error.dataValidade.incorrecta"));
                                }
                            }
                        }
                    }
                }
            } else {
                if ((formHorario.getDiaInicio() != null)
                        && (formHorario.getMesInicio() != null)
                        && (formHorario.getAnoInicio() != null)
                        && (formHorario.getDiaFim() != null)
                        && (formHorario.getMesFim() != null)
                        && (formHorario.getAnoFim() != null)) {
                    if ((formHorario.getDiaInicio().length() < 1)
                            || (formHorario.getMesInicio().length() < 1)
                            || (formHorario.getAnoInicio().length() < 1)
                            || (formHorario.getDiaFim().length() < 1)
                            || (formHorario.getMesFim().length() < 1)
                            || (formHorario.getAnoFim().length() < 1)) {
                        errors.add("dates", new ActionError(
                                "error.dataValidade.obrigatoria"));
                    } else {
                        try {
                            diaInicio = (new Integer(formHorario.getDiaInicio()))
                                    .intValue();
                            mesInicio = (new Integer(formHorario.getMesInicio()))
                                    .intValue();
                            anoInicio = (new Integer(formHorario.getAnoInicio()))
                                    .intValue();
                            diaFim = (new Integer(formHorario.getDiaFim()))
                                    .intValue();
                            mesFim = (new Integer(formHorario.getMesFim()))
                                    .intValue();
                            anoFim = (new Integer(formHorario.getAnoFim()))
                                    .intValue();
                        } catch (java.lang.NumberFormatException e) {
                            errors.add("numero", new ActionError(
                                    "error.numero.naoInteiro"));
                        }

                        Calendar calendarInicio = Calendar.getInstance();
                        Calendar calendarFim = Calendar.getInstance();

                        calendarInicio.clear();
                        calendarInicio.set(anoInicio, mesInicio - 1, diaInicio,
                                00, 00, 00);
                        java.util.Date dataInicio = calendarInicio.getTime();

                        calendarFim.clear();
                        calendarFim.set(anoFim, mesFim - 1, diaFim, 00, 00, 00);
                        java.util.Date dataFim = calendarFim.getTime();

                        if (!(dataInicio.getTime() <= dataFim.getTime())) {
                            errors.add("datas", new ActionError(
                                    "error.dataValidade.incorrecta"));
                        }
                    }
                }
            }
        } catch (java.lang.IllegalArgumentException e) {
            errors.add("horasData", new ActionError(
                    "error.dataValidade.incorrecta"));
        }
        return errors;
    } /* validateAssociarHorarioTipo */

    public void setHorario(Horario horario, ActionForm form) {
        AssociarHorarioForm formHorario = (AssociarHorarioForm) form;
        Calendar calendar = Calendar.getInstance();

        horario.setModalidade(formHorario.getModalidade());
        horario.setDuracaoSemanal((new Float(formHorario.getDuracaoSemanal()))
                .floatValue());

        horario
                .setNumDias(Integer.valueOf(formHorario.getNumDias())
                        .intValue());
        horario
                .setPosicao(Integer.valueOf(formHorario.getPosicao())
                        .intValue());

        // Expediente
        calendar.clear();
        if (formHorario.getDiaAnteriorExpediente().length() < 1) {
            calendar.set(1970, 0, 1, (new Integer(formHorario
                    .getInicioExpedienteHoras())).intValue(), (new Integer(
                    formHorario.getInicioExpedienteMinutos())).intValue(), 00);
            horario.setInicioExpediente(new Timestamp(calendar
                    .getTimeInMillis()));
        } else {
            //contagem no dia anterior
            calendar.set(1936, 11, 31, (new Integer(formHorario
                    .getInicioExpedienteHoras())).intValue(), (new Integer(
                    formHorario.getInicioExpedienteMinutos())).intValue(), 00);
            horario.setInicioExpediente(new Timestamp(calendar
                    .getTimeInMillis()));
        }

        calendar.clear();
        if (formHorario.getDiaSeguinteExpediente().length() < 1) {
            calendar.set(1970, 0, 1, (new Integer(formHorario
                    .getFimExpedienteHoras())).intValue(), (new Integer(
                    formHorario.getFimExpedienteMinutos())).intValue(), 00);
            horario.setFimExpediente(new Timestamp(calendar.getTimeInMillis()));

        } else {
            //contagem no dia seguinte
            calendar.set(1970, 0, 2, (new Integer(formHorario
                    .getFimExpedienteHoras())).intValue(), (new Integer(
                    formHorario.getFimExpedienteMinutos())).intValue(), 00);
            horario.setFimExpediente(new Timestamp(calendar.getTimeInMillis()));
        }

        // Periodos Normais
        calendar.clear();
        if (formHorario.getDiaAnteriorHN1().length() < 1) {
            calendar.set(1970, 0, 1, (new Integer(formHorario
                    .getInicioHN1Horas())).intValue(), (new Integer(formHorario
                    .getInicioHN1Minutos())).intValue(), 00);
        } else {
            //contagem no dia anterior
            calendar.set(1969, 11, 31, (new Integer(formHorario
                    .getInicioHN1Horas())).intValue(), (new Integer(formHorario
                    .getInicioHN1Minutos())).intValue(), 00);
        }
        horario.setInicioHN1(new Timestamp(calendar.getTimeInMillis()));

        calendar.clear();
        if (formHorario.getDiaSeguinteHN1().length() < 1) {
            calendar.set(1970, 0, 1,
                    (new Integer(formHorario.getFimHN1Horas())).intValue(),
                    (new Integer(formHorario.getFimHN1Minutos())).intValue(),
                    00);
        } else {
            //contagem no dia seguinte
            calendar.set(1970, 0, 2,
                    (new Integer(formHorario.getFimHN1Horas())).intValue(),
                    (new Integer(formHorario.getFimHN1Minutos())).intValue(),
                    00);
        }
        horario.setFimHN1(new Timestamp(calendar.getTimeInMillis()));

        // Data de Validade
        if ((formHorario.getDiaInicio() != null)
                && (formHorario.getMesInicio() != null)
                && (formHorario.getAnoInicio() != null)) {
            if ((formHorario.getDiaInicio().length() > 0)
                    && (formHorario.getMesInicio().length() > 0)
                    && (formHorario.getAnoInicio().length() > 0)) {
                calendar.clear();
                calendar.set((new Integer(formHorario.getAnoInicio()))
                        .intValue(), (new Integer(formHorario.getMesInicio()))
                        .intValue() - 1, (new Integer(formHorario
                        .getDiaInicio())).intValue(), 00, 00, 00);
                horario.setDataInicio(calendar.getTime());
            }
        }
        if ((formHorario.getDiaFim() != null)
                && (formHorario.getMesFim() != null)
                && (formHorario.getAnoFim() != null)) {
            if ((formHorario.getDiaFim().length() > 0)
                    && (formHorario.getMesFim().length() > 0)
                    && (formHorario.getAnoFim().length() > 0)) {
                calendar.clear();
                calendar.set((new Integer(formHorario.getAnoFim())).intValue(),
                        (new Integer(formHorario.getMesFim())).intValue() - 1,
                        (new Integer(formHorario.getDiaFim())).intValue(), 00,
                        00, 00);
                horario.setDataFim(calendar.getTime());
            }
        }
    } /* setHorario */

    public void setHorarioTipoRotativo(Horario horario, ActionForm form) {
        AssociarHorarioTipoForm formHorario = (AssociarHorarioTipoForm) form;

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);

        horario.setChaveHorarioTipo(Constants.CHAVE_HTIPO);
        // para revelar que é um horário tipo

        horario.setNumDias(new Integer(formHorario.getNumDias()).intValue());
        horario.setPosicao(new Integer(formHorario.getPosicao()).intValue());

        horario.setSigla(formHorario.getSigla());
    } /* setHorarioTipo */

    public void setDatasHorario(Horario horario, ActionForm form) {
        /* não é necessario */
    } /* setDatasHorario */

    public void setFormAssociarHorarioConfirmar(Locale locale, ActionForm form,
            Person pessoa, Funcionario funcionario, Horario horario,
            ArrayList listaRegime, boolean isExcepcaoHorario, String alterar) {
        /* não é necessario */
    } /* setFormAssociarHorarioConfirmar */

    public void setFormAssociarHorarioTipoConfirmar(Locale locale,
            ActionForm form, Person pessoa, Funcionario funcionario,
            HorarioTipo horarioTipo, Horario horario, ArrayList listaRegime,
            boolean isExcepcaoHorario) {
        /* não é necessario */
    } /* setFormAssociarHorarioTipoConfirmar */

    public String descricaoHorario(Locale locale, Horario horario,
            ArrayList listaRegimes) {
        String descricao = null;
        ResourceBundle bundle = ResourceBundle.getBundle(
                Constants.APPLICATION_RESOURCES, locale);
        Calendar calendar = Calendar.getInstance();

        String inicioHN1Horas = null;
        String inicioHN1Minutos = null;
        String fimHN1Horas = null;
        String fimHN1Minutos = null;

        // modalidade
        descricao = bundle.getString(horario.getModalidade()) + ", ";

        // regimes
        if (listaRegimes != null) {
            ListIterator iterListaRegimes = listaRegimes.listIterator();
            String regime = null;
            String auxRegime = null;
            descricao = descricao.concat(" ");

            while (iterListaRegimes.hasNext()) {
                regime = (String) iterListaRegimes.next();
                auxRegime = bundle.getString(regime);
                if (auxRegime != null) {
                    regime = auxRegime;
                }
                if (!iterListaRegimes.hasNext()) {
                    descricao = descricao.concat(regime);
                } else {
                    descricao = descricao.concat(regime + ", ");
                }
            }
        }

        // Periodos Normais
        calendar.clear();
        calendar.setTimeInMillis((horario.getInicioHN1()).getTime());
        inicioHN1Horas = ((new Integer(calendar.get(Calendar.HOUR_OF_DAY)))
                .toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            inicioHN1Minutos = ("0" + (new Integer(calendar
                    .get(Calendar.MINUTE))).toString());
        } else {
            inicioHN1Minutos = ((new Integer(calendar.get(Calendar.MINUTE)))
                    .toString());
        }

        calendar.clear();
        calendar.setTimeInMillis((horario.getFimHN1()).getTime());
        fimHN1Horas = ((new Integer(calendar.get(Calendar.HOUR_OF_DAY)))
                .toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            fimHN1Minutos = ("0" + (new Integer(calendar.get(Calendar.MINUTE)))
                    .toString());
        } else {
            fimHN1Minutos = ((new Integer(calendar.get(Calendar.MINUTE)))
                    .toString());
        }

        descricao = descricao.concat(" " + inicioHN1Horas + ":"
                + inicioHN1Minutos + " - " + fimHN1Horas + ":" + fimHN1Minutos);

        return descricao;
    } /* descricaoHorario */

    public String descricaoHorarioTipo(Locale locale, HorarioTipo horarioTipo,
            ArrayList listaRegimes) {
        String descricao = null;

        descricao = new String((new Float(horarioTipo.getDuracaoSemanal()))
                .intValue()
                + " horas ");

        ResourceBundle bundle = ResourceBundle.getBundle(
                Constants.APPLICATION_RESOURCES, locale);
        ListIterator iterListaRegimes = listaRegimes.listIterator();
        String regime = null;
        String auxRegime = null;
        descricao = descricao.concat(" ");
        while (iterListaRegimes.hasNext()) {
            regime = (String) iterListaRegimes.next();
            auxRegime = bundle.getString(regime);
            if (auxRegime != null) regime = auxRegime;
            if (!iterListaRegimes.hasNext()) {
                descricao = descricao.concat(regime);
            } else {
                descricao = descricao.concat(regime + ", ");
            }
        }
        return descricao;
    } /* descricaoHorarioTipo */

    public String periodoTrabalhoHorarioTipo(HorarioTipo horarioTipo) {
        String periodoTrabalho = new String();
        Calendar calendar = Calendar.getInstance();
        String inicioHN1Horas = null;
        String inicioHN1Minutos = null;
        String fimHN1Horas = null;
        String fimHN1Minutos = null;

        calendar.clear();
        calendar.setTimeInMillis((horarioTipo.getInicioHN1()).getTime());
        inicioHN1Horas = ((new Integer(calendar.get(Calendar.HOUR_OF_DAY)))
                .toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            inicioHN1Minutos = ("0" + (new Integer(calendar
                    .get(Calendar.MINUTE))).toString());
        } else {
            inicioHN1Minutos = ((new Integer(calendar.get(Calendar.MINUTE)))
                    .toString());
        }

        calendar.clear();
        calendar.setTimeInMillis((horarioTipo.getFimHN1()).getTime());
        fimHN1Horas = ((new Integer(calendar.get(Calendar.HOUR_OF_DAY)))
                .toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            fimHN1Minutos = ("0" + (new Integer(calendar.get(Calendar.MINUTE)))
                    .toString());
        } else {
            fimHN1Minutos = ((new Integer(calendar.get(Calendar.MINUTE)))
                    .toString());
        }

        periodoTrabalho = inicioHN1Horas + ":" + inicioHN1Minutos + " - "
                + fimHN1Horas + ":" + fimHN1Minutos;

        return periodoTrabalho;
    } /* periodoTrabalhoHorarioTipo */

    public void setSaldosHorarioVerbeteBody(Horario horario,
            ArrayList listaRegimes, ArrayList listaParamJustificacoes,
            ArrayList listaMarcacoesPonto, ArrayList listaSaldos) {
        // saldo do horario normal
        Float duracaoDiaria = new Float(horario.getDuracaoSemanal()
                / Constants.SEMANA_TRABALHO_TURNOS);
        long saldo = ((Long) listaSaldos.get(0)).longValue()
                - (duracaoDiaria.intValue() * 3600 * 1000 + new Float(
                        (duracaoDiaria.floatValue() - duracaoDiaria.intValue()) * 3600 * 1000)
                        .longValue());

        listaSaldos.set(0, new Long(saldo));
    } /* setSaldosHorarioVerbeteBody */

    public long calcularTrabalhoNocturno(Horario horario,
            MarcacaoPonto entrada, MarcacaoPonto saida) {
        long saldoNocturno = 0;

        // calculo do trabalho nocturno normal para os horario que ultrapassem
        // o inicio do trabalho nocturno
        if (horario.getFimHN1().getTime() >= Constants.INICIO_TRABALHO_NOCTURNO) {
            if (entrada.getData().getTime() >= Constants.INICIO_TRABALHO_NOCTURNO
                    && entrada.getData().getTime() <= Constants.FIM_TRABALHO_NOCTURNO) {
                saldoNocturno = saldoNocturno
                        + (Constants.FIM_TRABALHO_NOCTURNO - entrada.getData()
                                .getTime());
            } else if (entrada.getData().getTime() < Constants.INICIO_TRABALHO_NOCTURNO) {
                saldoNocturno = saldoNocturno
                        + (Constants.FIM_TRABALHO_NOCTURNO - Constants.INICIO_TRABALHO_NOCTURNO);
            }

            if (saida.getData().getTime() >= Constants.INICIO_TRABALHO_NOCTURNO
                    && saida.getData().getTime() <= Constants.FIM_TRABALHO_NOCTURNO) {
                saldoNocturno = saldoNocturno
                        + (saida.getData().getTime() - Constants.FIM_TRABALHO_NOCTURNO);
            } else if (saida.getData().getTime() < Constants.INICIO_TRABALHO_NOCTURNO) {
                saldoNocturno = saldoNocturno
                        - (Constants.FIM_TRABALHO_NOCTURNO - Constants.INICIO_TRABALHO_NOCTURNO);
            }
        }
        return saldoNocturno;
    } /* calcularTrabalhoNocturno */

    public void calcularHorasExtraordinarias(Horario horario,
            ArrayList listaMarcacoesPonto, ArrayList listaSaldos) {
        if (listaMarcacoesPonto.size() > 0) {
            ListIterator iteradorMarcacoes = listaMarcacoesPonto.listIterator();
            MarcacaoPonto entrada = null;
            MarcacaoPonto saida = null;
            long fimHorario = 0;
            long saldoExtraordinario = 0;

            long saldoPrimEscalao = 0;
            long saldoSegEscalao = 0;
            long saldoDepoisSegEscalao = 0;

            if (horario.getFimHN2().getTime() > Constants.INICIO_TRABALHO_NOCTURNO) {
                fimHorario = horario.getFimHN2().getTime();
            } else {
                fimHorario = encontraInicioTrabalhoNocturno(listaMarcacoesPonto);
            }

            while (iteradorMarcacoes.hasNext()) {
                entrada = (MarcacaoPonto) iteradorMarcacoes.next();

                if (iteradorMarcacoes.hasNext()) {
                    saida = (MarcacaoPonto) iteradorMarcacoes.next();

                    // calculo do trabalho extraordinário nocturno
                    if (entrada.getData().getTime() >= fimHorario
                            && entrada.getData().getTime() <= Constants.FIM_TRABALHO_NOCTURNO) {
                        saldoExtraordinario = saldoExtraordinario
                                + (Constants.FIM_TRABALHO_NOCTURNO - entrada
                                        .getData().getTime());
                    } else if (entrada.getData().getTime() < fimHorario) {
                        saldoExtraordinario = saldoExtraordinario
                                + (Constants.FIM_TRABALHO_NOCTURNO - fimHorario);
                    }
                    if (saida.getData().getTime() >= fimHorario
                            && saida.getData().getTime() <= Constants.FIM_TRABALHO_NOCTURNO) {
                        saldoExtraordinario = saldoExtraordinario
                                + (saida.getData().getTime() - Constants.FIM_TRABALHO_NOCTURNO);
                    } else if (saida.getData().getTime() < fimHorario) {
                        saldoExtraordinario = saldoExtraordinario
                                - (Constants.FIM_TRABALHO_NOCTURNO - fimHorario);
                    }
                }
            }
            Calendar calendario = Calendar.getInstance();
            calendario.setLenient(false);
            calendario.clear();
            calendario.setTimeInMillis(saldoExtraordinario);
            if (saldoExtraordinario > ((Long) listaSaldos.get(0)).longValue()) {
                calendario.clear();
                calendario.setTimeInMillis(((Long) listaSaldos.get(0))
                        .longValue());
                saldoExtraordinario = ((Long) listaSaldos.get(0)).longValue();
            }
            //cálculo dos escalões do trabalho extraordinário nocturno
            if (saldoExtraordinario <= Constants.PRIMEIRO_ESCALAO) {
                saldoPrimEscalao = saldoPrimEscalao + saldoExtraordinario;
            } else {
                saldoPrimEscalao = saldoPrimEscalao
                        + Constants.PRIMEIRO_ESCALAO;
                if (saldoExtraordinario <= Constants.SEGUNDO_ESCALAO) {
                    saldoSegEscalao = saldoSegEscalao
                            + (saldoExtraordinario - Constants.PRIMEIRO_ESCALAO);
                } else {
                    saldoSegEscalao = saldoSegEscalao
                            + (Constants.SEGUNDO_ESCALAO - Constants.PRIMEIRO_ESCALAO);
                    saldoDepoisSegEscalao = saldoDepoisSegEscalao
                            + (saldoExtraordinario - Constants.SEGUNDO_ESCALAO);
                }
            }

            listaSaldos.set(8, new Long(saldoPrimEscalao));
            listaSaldos.set(9, new Long(saldoSegEscalao));
            listaSaldos.set(10, new Long(saldoDepoisSegEscalao));

            //calculo do trabalho extraordinário diurno
            saldoExtraordinario = ((Long) listaSaldos.get(0)).longValue()
                    - saldoExtraordinario;

            saldoPrimEscalao = 0;
            saldoSegEscalao = 0;
            saldoDepoisSegEscalao = 0;
            //calculo dos escalões do trabalho extraordinário diurno
            if (saldoExtraordinario <= Constants.PRIMEIRO_ESCALAO) {
                saldoPrimEscalao = saldoPrimEscalao + saldoExtraordinario;
            } else {
                saldoPrimEscalao = saldoPrimEscalao
                        + Constants.PRIMEIRO_ESCALAO;
                if (saldoExtraordinario <= Constants.SEGUNDO_ESCALAO) {
                    saldoSegEscalao = saldoSegEscalao
                            + (saldoExtraordinario - Constants.PRIMEIRO_ESCALAO);
                } else {
                    saldoSegEscalao = saldoSegEscalao
                            + (Constants.SEGUNDO_ESCALAO - Constants.PRIMEIRO_ESCALAO);
                    saldoDepoisSegEscalao = saldoDepoisSegEscalao
                            + (saldoExtraordinario - Constants.SEGUNDO_ESCALAO);
                }
            }

            listaSaldos.set(2, new Long(saldoPrimEscalao));
            listaSaldos.set(3, new Long(saldoSegEscalao));
            listaSaldos.set(4, new Long(saldoDepoisSegEscalao));
        }
    } /* calcularHorasExtraorinarias */
   
    public long encontraInicioTrabalhoNocturno(List listaMarcacoesPonto) {
        long inicioTrabalhoExtraNocturno = 0;
        MarcacaoPonto entrada = null;
        MarcacaoPonto saida = null;

        ListIterator iteradorMarcacoes = listaMarcacoesPonto.listIterator();
        while (iteradorMarcacoes.hasNext()) {
            entrada = (MarcacaoPonto) iteradorMarcacoes.next();

            if (iteradorMarcacoes.hasNext()) {
                saida = (MarcacaoPonto) iteradorMarcacoes.next();

                if (entrada.getData().getTime() < Constants.INICIO_TRABALHO_NOCTURNO
                        && saida.getData().getTime() >= Constants.INICIO_TRABALHO_NOCTURNO
                        && saida.getData().getTime() < Constants.FIM_TRABALHO_NOCTURNO) {
                    //transição de período diurno para nocturno
                    inicioTrabalhoExtraNocturno = entrada.getData().getTime();
                    while (inicioTrabalhoExtraNocturno < Constants.INICIO_TRABALHO_NOCTURNO) {
                        inicioTrabalhoExtraNocturno = inicioTrabalhoExtraNocturno
                                + Constants.PERIODO_MINIMO;
                    }
                } else {
                    continue;
                }
            }
        }
        return (inicioTrabalhoExtraNocturno==0?Constants.INICIO_TRABALHO_NOCTURNO:inicioTrabalhoExtraNocturno);
    }

    public long limitaTrabalhoSeguido(Horario horario, long entrada,
            long saida, boolean limita) {
        long saldo = saida - entrada;

        return saldo;
    } /* limitaTrabalhoSeguido */

    public long duracaoPF(Horario horario, ArrayList listaRegimes) {
        //não tem periodos fixos
        return 0;
    } /* duracaoPF */

    public long duracaoDiaria(Horario horario) {
        return ((new Float(Constants.DURACAO_SEMANAL_TURNOS
                / Constants.SEMANA_TRABALHO_TURNOS).intValue() * 3600 * 1000) + new Float(
                (new Float(Constants.DURACAO_SEMANAL_TURNOS
                        / Constants.SEMANA_TRABALHO_TURNOS).floatValue() - new Float(
                        Constants.DURACAO_SEMANAL_TURNOS
                                / Constants.SEMANA_TRABALHO_TURNOS).intValue()) * 3600 * 1000)
                .longValue());
    } /* duracaoDiaria */

    public int mapeamentoFechoMes(Horario horario, ArrayList listaRegimes) {
        return Constants.MAP_TURNOS;
    } /* mapeamentoFechoMes */

    public boolean isNocturno(Horario horario) {
        boolean resultado = false;
        if ((horario.getInicioHN1().getTime() >= Constants.INICIO_TRABALHO_NOCTURNO && horario
                .getInicioHN1().getTime() < Constants.FIM_TRABALHO_NOCTURNO)
                || (Constants.INICIO_TRABALHO_NOCTURNO >= horario
                        .getInicioHN1().getTime() && Constants.INICIO_TRABALHO_NOCTURNO < horario
                        .getFimHN1().getTime())) {
            resultado = true;
        }
        return resultado;
    } /* isNocturno */

    public int marcacoesObrigatorias(Horario horario) {
        return 2;
    } /* marcacoesObrigatorias */
}