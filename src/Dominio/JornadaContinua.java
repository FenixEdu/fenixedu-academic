package Dominio;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import ServidorApresentacao.formbeans.assiduousness.AssociarHorarioForm;
import ServidorApresentacao.formbeans.assiduousness.AssociarHorarioTipoConfirmarForm;
import ServidorApresentacao.formbeans.assiduousness.AssociarHorarioTipoForm;
import constants.assiduousness.Constants;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class JornadaContinua implements IStrategyHorarios {
    public JornadaContinua() {
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

        int inicioPF1Horas = 0;
        int inicioPF1Minutos = 0;
        int fimPF1Horas = 0;
        int fimPF1Minutos = 0;

        long timeInicioPF1 = 0;
        long timeFimPF1 = 0;
        long timePFPeriodo = 0;

        int diaInicio = 0;
        int mesInicio = 0;
        int anoInicio = 0;
        int diaFim = 0;
        int mesFim = 0;
        int anoFim = 0;

        // Duracao semanal
        if (formHorario.getDuracaoSemanal().length() < 1) {
            errors.add("DuracaoSemanal-HJornadaContinua", new ActionError(
                    "error.duracaoSemanal.obrigatoria"));
        } else {
            try {
                duracaoSemanal = (new Float(formHorario.getDuracaoSemanal())).floatValue();
            } catch (java.lang.NumberFormatException e) {
                errors.add("numero", new ActionError("error.numero.naoInteiro"));
            }
            if (duracaoSemanal != Constants.DURACAO_SEMANAL_JORNADA) {
                errors.add("DuracaoSemanal-HJornadaContnua", new ActionError("error.duracaoSemanal"));
            }
        }

        try {
            if ((formHorario.getInicioExpedienteHoras().length() < 1)
                    || (formHorario.getInicioExpedienteMinutos().length() < 1)
                    || (formHorario.getFimExpedienteHoras().length() < 1)
                    || (formHorario.getFimExpedienteMinutos().length() < 1)) {
                errors.add("Expediente", new ActionError("error.Expediente.obrigatorio"));
            } else {
                try {
                    inicioExpedienteHoras = (new Integer(formHorario.getInicioExpedienteHoras()))
                            .intValue();
                    inicioExpedienteMinutos = (new Integer(formHorario.getInicioExpedienteMinutos()))
                            .intValue();
                    fimExpedienteHoras = (new Integer(formHorario.getFimExpedienteHoras())).intValue();
                    fimExpedienteMinutos = (new Integer(formHorario.getFimExpedienteMinutos()))
                            .intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError("error.numero.naoInteiro"));
                }

                calendar.clear();
                if (formHorario.getDiaAnteriorExpediente().length() < 1) {
                    calendar.set(1970, 0, 1, inicioExpedienteHoras, inicioExpedienteMinutos, 00);
                    timeInicioExpediente = calendar.getTimeInMillis();
                } else {
                    //contagem no dia anterior
                    calendar.set(1969, 11, 31, inicioExpedienteHoras, inicioExpedienteMinutos, 00);
                    timeInicioExpediente = calendar.getTimeInMillis();
                }

                calendar.clear();
                if (formHorario.getDiaSeguinteExpediente().length() < 1) {
                    calendar.set(1970, 0, 1, fimExpedienteHoras, fimExpedienteMinutos, 00);
                    timeFimExpediente = calendar.getTimeInMillis();
                } else {
                    //contagem no dia seguinte
                    calendar.set(1970, 0, 2, fimExpedienteHoras, fimExpedienteMinutos, 00);
                    timeFimExpediente = calendar.getTimeInMillis();
                }

                if (!(timeInicioExpediente < timeFimExpediente)) {
                    errors.add("Expediente", new ActionError("error.Expediente"));
                } else if ((timeInicioExpediente < Constants.EXPEDIENTE_MINIMO)
                        || (timeFimExpediente > Constants.EXPEDIENTE_MAXIMO)) {
                    errors.add("Expediente", new ActionError("error.Expediente"));
                }
            }

            if ((formHorario.getInicioRefeicaoHoras().length() > 0)
                    || (formHorario.getInicioRefeicaoMinutos().length() > 0)
                    || (formHorario.getFimRefeicaoHoras().length() > 0)
                    || (formHorario.getFimRefeicaoMinutos().length() > 0)) {
                errors.add("Intervalo de Refeicao", new ActionError("error.refeicao.naoPermitido"));
            }
            if ((formHorario.getIntervaloMinimoHoras().length() > 0)
                    || (formHorario.getIntervaloMinimoMinutos().length() > 0)
                    || (formHorario.getDescontoObrigatorioHoras().length() > 0)
                    || (formHorario.getDescontoObrigatorioMinutos().length() > 0)) {
                errors.add("Intervalo de Refeicao", new ActionError(
                        "error.refeicao.intervaloEDesconto.nãoPermitidos"));
            }
            if ((formHorario.getTrabalhoConsecutivoHoras().length() > 0)
                    || (formHorario.getTrabalhoConsecutivoMinutos().length() > 0)) {
                errors.add("Trabalho Consecutivo", new ActionError(
                        "error.trabalhoConsecutivo.naoPermitido"));
            }

            // regime
            if (formHorario.getRegime().length < 0) {
                errors.add("regime", new ActionError("error.regime.obrigatorio"));
            } else {
                listaRegime = Arrays.asList(formHorario.getRegime());

                // Horario normal
                if ((formHorario.getInicioHN1Horas().length() < 1)
                        || (formHorario.getInicioHN1Minutos().length() < 1)
                        || (formHorario.getFimHN1Horas().length() < 1)
                        || (formHorario.getFimHN1Minutos().length() < 1)) {
                    errors.add("HorarioNormal", new ActionError("error.HorarioNormal1.obrigatorio"));
                } else {
                    if (!((formHorario.getInicioHN2Horas().length() < 1)
                            || (formHorario.getInicioHN2Minutos().length() < 1)
                            || (formHorario.getFimHN2Horas().length() < 1) || (formHorario
                            .getFimHN2Minutos().length() < 1))) {
                        errors
                                .add("HorarioNormal", new ActionError(
                                        "error.HorarioNormal2.naoPermitido"));
                    }
                    try {
                        inicioHN1Horas = (new Integer(formHorario.getInicioHN1Horas())).intValue();
                        inicioHN1Minutos = (new Integer(formHorario.getInicioHN1Minutos())).intValue();
                        fimHN1Horas = (new Integer(formHorario.getFimHN1Horas())).intValue();
                        fimHN1Minutos = (new Integer(formHorario.getFimHN1Minutos())).intValue();
                    } catch (java.lang.NumberFormatException e) {
                        errors.add("numero", new ActionError("error.numero.naoInteiro"));
                    }

                    calendar.clear();
                    if (formHorario.getDiaAnteriorHN1().length() < 1) {
                        calendar.set(1970, 0, 1, inicioHN1Horas, inicioHN1Minutos, 00);
                    } else {
                        // contagem no dia anterior
                        calendar.set(1969, 11, 31, inicioHN1Horas, inicioHN1Minutos, 00);
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
                            errors.add("ExpedienteHN", new ActionError(
                                    "error.ExpedienteHorarioNormal.inconsistentes"));
                        }
                    }

                    if (timeFimExpediente != 0) {
                        if (!(timeFimExpediente > timeFimHN1)) {
                            errors.add("ExpedienteHN", new ActionError(
                                    "error.ExpedienteHorarioNormal.inconsistentes"));
                        }
                    }

                    if (!(timeInicioHN1 < timeFimHN1)) {
                        errors.add("HN", new ActionError("error.HorarioNormal"));
                    } else {
                        // 1 periodo de trabalho de 6 horas
                        timeHNPeriodo = ((new Float(Constants.DURACAO_SEMANAL_JORNADA
                                / Constants.SEMANA_TRABALHO_JORNADA).intValue() * 3600 * 1000) + new Float(
                                (new Float(Constants.DURACAO_SEMANAL_JORNADA
                                        / Constants.SEMANA_TRABALHO_JORNADA).floatValue() - new Float(
                                        Constants.DURACAO_SEMANAL_JORNADA
                                                / Constants.SEMANA_TRABALHO_JORNADA).intValue()) * 3600 * 1000)
                                .longValue());
                        if (!((timeFimHN1 - timeInicioHN1) == timeHNPeriodo)) {
                            errors.add("HNPeriodo", new ActionError("error.HorarioNormal.duracao"));
                        }
                    }
                }

                // Periodo fixo
                if ((listaRegime.contains(Constants.REGIME_IPF))) {
                    if ((formHorario.getInicioPF1Horas().length() > 0)
                            || (formHorario.getInicioPF1Minutos().length() > 0)
                            || (formHorario.getFimPF1Horas().length() > 0)
                            || (formHorario.getFimPF1Minutos().length() > 0)
                            || (formHorario.getInicioPF2Horas().length() > 0)
                            || (formHorario.getInicioPF2Minutos().length() > 0)
                            || (formHorario.getFimPF2Horas().length() > 0)
                            || (formHorario.getFimPF2Minutos().length() > 0)) {
                        errors.add("periodoFixo", new ActionError("error.PeriodoFixo.naoPermitido"));
                    }
                } else {
                    // sem isencao de periodo fixo
                    if ((formHorario.getInicioPF2Horas().length() > 0)
                            || (formHorario.getInicioPF2Minutos().length() > 0)
                            || (formHorario.getFimPF2Horas().length() > 0)
                            || (formHorario.getFimPF2Minutos().length() > 0)) {
                        errors.add("HorarioNormal", new ActionError("error.PeriodoFixo2.naoPermitido"));
                    }

                    if ((formHorario.getInicioPF1Horas().length() < 1)
                            || (formHorario.getInicioPF1Minutos().length() < 1)
                            || (formHorario.getFimPF1Horas().length() < 1)
                            || (formHorario.getFimPF1Minutos().length() < 1)) {
                        errors.add("HorarioNormal", new ActionError("error.PeriodoFixo1.obrigatorio"));
                    } else {
                        try {
                            inicioPF1Horas = (new Integer(formHorario.getInicioPF1Horas())).intValue();
                            inicioPF1Minutos = (new Integer(formHorario.getInicioPF1Minutos()))
                                    .intValue();
                            fimPF1Horas = (new Integer(formHorario.getFimPF1Horas())).intValue();
                            fimPF1Minutos = (new Integer(formHorario.getFimPF1Minutos())).intValue();
                        } catch (java.lang.NumberFormatException e) {
                            errors.add("numero", new ActionError("error.numero.naoInteiro"));
                        }

                        calendar.clear();
                        if (formHorario.getDiaAnteriorPF1().length() < 1) {
                            calendar.set(1970, 0, 1, inicioPF1Horas, inicioPF1Minutos, 00);
                        } else {
                            // contagem no dia anterior
                            calendar.set(1969, 11, 31, inicioPF1Horas, inicioPF1Minutos, 00);
                        }
                        timeInicioPF1 = calendar.getTimeInMillis();

                        calendar.clear();
                        if (formHorario.getDiaSeguintePF1().length() < 1) {
                            calendar.set(1970, 0, 1, fimPF1Horas, fimPF1Minutos, 00);
                        } else {
                            // contagem no dia seguinte
                            calendar.set(1970, 0, 2, fimPF1Horas, fimPF1Minutos, 00);
                        }
                        timeFimPF1 = calendar.getTimeInMillis();

                        if (!(timeInicioPF1 < timeFimPF1)) {
                            errors.add("PF", new ActionError("error.PeriodoFixo"));
                        } else {
                            // 1 periodos fixo de 4 horas.
                            timePFPeriodo = Constants.PLATAFORMAS_FIXAS_JORNADA;
                            if (!((timeFimPF1 - timeInicioPF1) == timePFPeriodo)) {
                                errors.add("PFPeriodo", new ActionError("error.PeriodoFixo.duracao"));
                            }

                            /*
                             * ATENCAO: no horario da JC pode-se adiar e atrasar
                             * A ENTRADA(MARCACAO DE PONTO) DE UMA HORA
                             */
                            /*
                             * calendar.clear(); calendar.set(1970, 0, 1, 2, 00,
                             * 00); timePFPeriodo = calendar.getTimeInMillis();
                             * 
                             * if((timeInicioPF1 - timeInicioHN1) !=
                             * timePFPeriodo){ errors.add("HNPF", new
                             * ActionError("error.PeriodoFixo.adiamento")); }
                             */

                            if (!((timeInicioHN1 <= timeInicioPF1) && (timeFimPF1 <= timeFimHN1))) {
                                errors.add("HNPF", new ActionError(
                                        "error.HorarioNormalPeriodoFixo.inconsistentes"));
                            }
                        }
                    }
                }
            }

            // Data de validade
            if ((formHorario.getDiaInicio() != null) && (formHorario.getMesInicio() != null)
                    && (formHorario.getAnoInicio() != null) && (formHorario.getDiaFim() != null)
                    && (formHorario.getMesFim() != null) && (formHorario.getAnoFim() != null)) {
                if ((formHorario.getDiaInicio().length() < 1)
                        || (formHorario.getMesInicio().length() < 1)
                        || (formHorario.getAnoInicio().length() < 1)
                        || (formHorario.getDiaFim().length() < 1)
                        || (formHorario.getMesFim().length() < 1)
                        || (formHorario.getAnoFim().length() < 1)) {
                    errors.add("datasValidade", new ActionError("error.dataValidade.obrigatoria"));
                } else {
                    try {
                        diaInicio = (new Integer(formHorario.getDiaInicio())).intValue();
                        mesInicio = (new Integer(formHorario.getMesInicio())).intValue();
                        anoInicio = (new Integer(formHorario.getAnoInicio())).intValue();
                        diaFim = (new Integer(formHorario.getDiaFim())).intValue();
                        mesFim = (new Integer(formHorario.getMesFim())).intValue();
                        anoFim = (new Integer(formHorario.getAnoFim())).intValue();
                    } catch (java.lang.NumberFormatException e) {
                        errors.add("numero", new ActionError("error.numero.naoInteiro"));
                    }

                    calendar.clear();
                    calendar.set(anoInicio, mesInicio - 1, diaInicio, 00, 00, 00);
                    java.util.Date dataInicio = calendar.getTime();

                    calendar.clear();
                    calendar.set(anoFim, mesFim - 1, diaFim, 00, 00, 00);
                    java.util.Date dataFim = calendar.getTime();

                    if (!formHorario.isExcepcaoHorario()) {
                        if (!(dataInicio.getTime() < dataFim.getTime())) {
                            errors
                                    .add("datasValidade", new ActionError(
                                            "error.dataValidade.incorrecta"));
                        }
                    } else {
                        if (!(dataInicio.getTime() <= dataFim.getTime())) {
                            errors
                                    .add("datasValidade", new ActionError(
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

    public ActionErrors validateAssociarHorarioTipo(ActionForm form, HorarioTipo horarioTipo,
            List listaRegimes) {
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
            // Data de validade
            if ((formHorario.getDiaInicio() != null) && (formHorario.getMesInicio() != null)
                    && (formHorario.getAnoInicio() != null) && (formHorario.getDiaFim() != null)
                    && (formHorario.getMesFim() != null) && (formHorario.getAnoFim() != null)) {
                if ((formHorario.getDiaInicio().length() < 1)
                        || (formHorario.getMesInicio().length() < 1)
                        || (formHorario.getAnoInicio().length() < 1)
                        || (formHorario.getDiaFim().length() < 1)
                        || (formHorario.getMesFim().length() < 1)
                        || (formHorario.getAnoFim().length() < 1)) {
                    errors.add("datasValidade", new ActionError("error.dataValidade.obrigatoria"));
                } else {
                    try {
                        diaInicio = (new Integer(formHorario.getDiaInicio())).intValue();
                        mesInicio = (new Integer(formHorario.getMesInicio())).intValue();
                        anoInicio = (new Integer(formHorario.getAnoInicio())).intValue();
                        diaFim = (new Integer(formHorario.getDiaFim())).intValue();
                        mesFim = (new Integer(formHorario.getMesFim())).intValue();
                        anoFim = (new Integer(formHorario.getAnoFim())).intValue();
                    } catch (java.lang.NumberFormatException e) {
                        errors.add("numero", new ActionError("error.numero.naoInteiro"));
                    }

                    calendar.clear();
                    calendar.set(anoInicio, mesInicio - 1, diaInicio, 00, 00, 00);
                    java.util.Date dataInicio = calendar.getTime();

                    calendar.clear();
                    calendar.set(anoFim, mesFim - 1, diaFim, 00, 00, 00);
                    java.util.Date dataFim = calendar.getTime();

                    if (!formHorario.isExcepcaoHorario()) {
                        if (!(dataInicio.getTime() < dataFim.getTime())) {
                            errors
                                    .add("datasValidade", new ActionError(
                                            "error.dataValidade.incorrecta"));
                        }
                    } else {
                        if (!(dataInicio.getTime() <= dataFim.getTime())) {
                            errors
                                    .add("datasValidade", new ActionError(
                                            "error.dataValidade.incorrecta"));
                        }
                    }
                }
            }
        } catch (java.lang.IllegalArgumentException e) {
            errors.add("horasData", new ActionError("error.dataValidade.incorrecta"));
        }
        return errors;
    } /* validateAssociarHorarioTipo */

    public void setHorario(Horario horario, ActionForm form) {
        AssociarHorarioForm formHorario = (AssociarHorarioForm) form;
        Calendar calendar = Calendar.getInstance();
        List listaRegime = null;

        horario.setModalidade(formHorario.getModalidade());
        horario.setDuracaoSemanal((new Float(formHorario.getDuracaoSemanal())).floatValue());

        if ((formHorario.getNumDias() == null) || (formHorario.getPosicao() == null)
                || (formHorario.getNumDias().length() < 1) || (formHorario.getPosicao().length() < 1)) {
            horario.setNumDias(5);
            horario.setPosicao(2);
        } else {
            horario.setNumDias(Integer.valueOf(formHorario.getNumDias()).intValue());
            horario.setPosicao(Integer.valueOf(formHorario.getPosicao()).intValue());
        }

        // Expediente
        calendar.clear();
        if (formHorario.getDiaAnteriorExpediente().length() < 1) {
            calendar.set(1970, 0, 1, (new Integer(formHorario.getInicioExpedienteHoras())).intValue(),
                    (new Integer(formHorario.getInicioExpedienteMinutos())).intValue(), 00);
            horario.setInicioExpediente(new Timestamp(calendar.getTimeInMillis()));
        } else {
            //contagem no dia anterior
            calendar.set(1969, 11, 31, (new Integer(formHorario.getInicioExpedienteHoras())).intValue(),
                    (new Integer(formHorario.getInicioExpedienteMinutos())).intValue(), 00);
            horario.setInicioExpediente(new Timestamp(calendar.getTimeInMillis()));
        }

        calendar.clear();
        if (formHorario.getDiaSeguinteExpediente().length() < 1) {
            calendar.set(1970, 0, 1, (new Integer(formHorario.getFimExpedienteHoras())).intValue(),
                    (new Integer(formHorario.getFimExpedienteMinutos())).intValue(), 00);
            horario.setFimExpediente(new Timestamp(calendar.getTimeInMillis()));

        } else {
            //contagem no dia seguinte
            calendar.set(1970, 0, 2, (new Integer(formHorario.getFimExpedienteHoras())).intValue(),
                    (new Integer(formHorario.getFimExpedienteMinutos())).intValue(), 00);
            horario.setFimExpediente(new Timestamp(calendar.getTimeInMillis()));
        }

        // Periodo do Horario Normal
        calendar.clear();
        if (formHorario.getDiaAnteriorHN1().length() < 1) {
            calendar.set(1970, 0, 1, (new Integer(formHorario.getInicioHN1Horas())).intValue(),
                    (new Integer(formHorario.getInicioHN1Minutos())).intValue(), 00);
        } else {
            //contagem no dia anterior
            calendar.set(1969, 11, 31, (new Integer(formHorario.getInicioHN1Horas())).intValue(),
                    (new Integer(formHorario.getInicioHN1Minutos())).intValue(), 00);
        }
        horario.setInicioHN1(new Timestamp(calendar.getTimeInMillis()));

        calendar.clear();
        if (formHorario.getDiaSeguinteHN1().length() < 1) {
            calendar.set(1970, 0, 1, (new Integer(formHorario.getFimHN1Horas())).intValue(),
                    (new Integer(formHorario.getFimHN1Minutos())).intValue(), 00);
        } else {
            //contagem no dia seguinte
            calendar.set(1970, 0, 2, (new Integer(formHorario.getFimHN1Horas())).intValue(),
                    (new Integer(formHorario.getFimHN1Minutos())).intValue(), 00);
        }
        horario.setFimHN1(new Timestamp(calendar.getTimeInMillis()));

        // Data de Validade
        if ((formHorario.getDiaInicio() != null) && (formHorario.getMesInicio() != null)
                && (formHorario.getAnoInicio() != null)) {
            if ((formHorario.getDiaInicio().length() > 0) && (formHorario.getMesInicio().length() > 0)
                    && (formHorario.getAnoInicio().length() > 0)) {
                calendar.clear();
                calendar.set((new Integer(formHorario.getAnoInicio())).intValue(), (new Integer(
                        formHorario.getMesInicio())).intValue() - 1, (new Integer(formHorario
                        .getDiaInicio())).intValue(), 00, 00, 00);
                horario.setDataInicio(calendar.getTime());
            }
        }
        if ((formHorario.getDiaFim() != null) && (formHorario.getMesFim() != null)
                && (formHorario.getAnoFim() != null)) {
            if ((formHorario.getDiaFim().length() > 0) && (formHorario.getMesFim().length() > 0)
                    && (formHorario.getAnoFim().length() > 0)) {
                calendar.clear();
                calendar.set((new Integer(formHorario.getAnoFim())).intValue(), (new Integer(formHorario
                        .getMesFim())).intValue() - 1,
                        (new Integer(formHorario.getDiaFim())).intValue(), 00, 00, 00);
                horario.setDataFim(calendar.getTime());
            }
        }

        // Periodos Fixos
        listaRegime = Arrays.asList(formHorario.getRegime());
        if (!listaRegime.contains(Constants.REGIME_IPF)) {
            //new String("isencaoPeriodoFixo")
            if (formHorario.getDiaAnteriorPF1().length() < 1) {
                calendar.set(1970, 0, 1, (new Integer(formHorario.getInicioPF1Horas())).intValue(),
                        (new Integer(formHorario.getInicioPF1Minutos())).intValue(), 00);
            } else {
                //contagem no dia anterior
                calendar.set(1969, 11, 31, (new Integer(formHorario.getInicioPF1Horas())).intValue(),
                        (new Integer(formHorario.getInicioPF1Minutos())).intValue(), 00);
            }
            horario.setInicioPF1(new Timestamp(calendar.getTimeInMillis()));

            calendar.clear();
            if (formHorario.getDiaSeguintePF1().length() < 1) {
                calendar.set(1970, 0, 1, (new Integer(formHorario.getFimPF1Horas())).intValue(),
                        (new Integer(formHorario.getFimPF1Minutos())).intValue(), 00);
            } else {
                //contagem no dia seguinte
                calendar.set(1970, 0, 2, (new Integer(formHorario.getFimPF1Horas())).intValue(),
                        (new Integer(formHorario.getFimPF1Minutos())).intValue(), 00);
            }
            horario.setFimPF1(new Timestamp(calendar.getTimeInMillis()));
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
    } /* setHorarioRotativo */

    public void setDatasHorario(Horario horario, ActionForm form) {
        AssociarHorarioTipoForm formHorario = (AssociarHorarioTipoForm) form;

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);

        horario.setNumDias(Constants.NUMDIAS_ROTACAO);
        horario.setPosicao(Constants.INICIO_ROTACAO);

        calendar.clear();
        calendar.set((new Integer(formHorario.getAnoInicio())).intValue(), (new Integer(formHorario
                .getMesInicio())).intValue() - 1, (new Integer(formHorario.getDiaInicio())).intValue(),
                00, 00, 00);
        horario.setDataInicio(calendar.getTime());

        if ((formHorario.getDiaFim().length() > 0) && (formHorario.getMesFim().length() > 0)
                && (formHorario.getAnoFim().length() > 0)) {
            calendar.clear();
            calendar.set((new Integer(formHorario.getAnoFim())).intValue(), (new Integer(formHorario
                    .getMesFim())).intValue() - 1, (new Integer(formHorario.getDiaFim())).intValue(),
                    00, 00, 00);
            horario.setDataFim(calendar.getTime());
        }
    }

    public void setFormAssociarHorarioConfirmar(Locale locale, ActionForm form, Pessoa pessoa,
            Funcionario funcionario, Horario horario, List listaRegime, boolean isExcepcaoHorario,
            String alterar) {
        AssociarHorarioForm formHorario = (AssociarHorarioForm) form;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);

        String preenchimento = "--";
        if (alterar != null) {
            preenchimento = "";
        }

        formHorario.setExcepcaoHorario(isExcepcaoHorario);

        if (pessoa != null) {
            formHorario.setNome(pessoa.getNome());
        }
        if (funcionario != null) {
            formHorario.setNumMecanografico((new Integer(funcionario.getNumeroMecanografico()))
                    .toString());
        }

        formHorario.setDuracaoSemanal((new Integer((new Float(horario.getDuracaoSemanal())).intValue()))
                .toString());
        formHorario.setModalidade(horario.getModalidade());

        formHorario.setListaRegimes(listaRegime);

        calendar.clear();
        calendar.setTimeInMillis((horario.getInicioExpediente()).getTime());
        formHorario.setInicioExpedienteHoras((new Integer(calendar.get(Calendar.HOUR_OF_DAY)))
                .toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            formHorario.setInicioExpedienteMinutos("0"
                    + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            formHorario.setInicioExpedienteMinutos((new Integer(calendar.get(Calendar.MINUTE)))
                    .toString());
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
            //contagem no Dia Anterior
            formHorario.setDiaAnteriorExpediente(Constants.DIA_ANTERIOR);
        }

        calendar.clear();
        calendar.setTimeInMillis((horario.getFimExpediente()).getTime());
        formHorario.setFimExpedienteHoras((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            formHorario.setFimExpedienteMinutos("0"
                    + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            formHorario.setFimExpedienteMinutos((new Integer(calendar.get(Calendar.MINUTE))).toString());
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
            //contagem no Dia Seguinte
            formHorario.setDiaSeguinteExpediente(Constants.DIA_SEGUINTE);
        }

        calendar.clear();
        calendar.setTimeInMillis((horario.getInicioHN1()).getTime());
        formHorario.setInicioHN1Horas((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            formHorario.setInicioHN1Minutos("0"
                    + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            formHorario.setInicioHN1Minutos((new Integer(calendar.get(Calendar.MINUTE))).toString());
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
            //contagem no Dia Anterior
            formHorario.setDiaAnteriorHN1(Constants.DIA_ANTERIOR);
        }

        calendar.clear();
        calendar.setTimeInMillis((horario.getFimHN1()).getTime());
        formHorario.setFimHN1Horas((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            formHorario.setFimHN1Minutos("0" + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            formHorario.setFimHN1Minutos((new Integer(calendar.get(Calendar.MINUTE))).toString());
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
            //contagem no Dia Seguinte
            formHorario.setDiaSeguinteHN1(Constants.DIA_SEGUINTE);
        }

        formHorario.setInicioHN2Horas(preenchimento);
        formHorario.setInicioHN2Minutos(preenchimento);
        formHorario.setFimHN2Horas(preenchimento);
        formHorario.setFimHN2Minutos(preenchimento);

        calendar.clear();
        calendar.setTime(horario.getDataInicio());
        formHorario.setDiaInicio((new Integer(calendar.get(Calendar.DAY_OF_MONTH))).toString());
        formHorario.setMesInicio((new Integer(calendar.get(Calendar.MONTH) + 1)).toString());
        formHorario.setAnoInicio((new Integer(calendar.get(Calendar.YEAR))).toString());

        calendar.clear();
        calendar.setTime(horario.getDataFim());
        formHorario.setDiaFim((new Integer(calendar.get(Calendar.DAY_OF_MONTH))).toString());
        formHorario.setMesFim((new Integer(calendar.get(Calendar.MONTH) + 1)).toString());
        formHorario.setAnoFim((new Integer(calendar.get(Calendar.YEAR))).toString());

        if (!listaRegime.contains(Constants.REGIME_IPF)) {
            //Regime: new String("isencaoPeriodoFixo")
            calendar.clear();
            calendar.setTimeInMillis((horario.getInicioPF1()).getTime());
            formHorario.setInicioPF1Horas((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendar.get(Calendar.MINUTE) < 10) {
                formHorario.setInicioPF1Minutos("0"
                        + (new Integer(calendar.get(Calendar.MINUTE))).toString());
            } else {
                formHorario.setInicioPF1Minutos((new Integer(calendar.get(Calendar.MINUTE))).toString());
            }
            if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Anterior
                formHorario.setDiaAnteriorPF1(Constants.DIA_ANTERIOR);
            }

            calendar.clear();
            calendar.setTimeInMillis((horario.getFimPF1()).getTime());
            formHorario.setFimPF1Horas((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendar.get(Calendar.MINUTE) < 10) {
                formHorario.setFimPF1Minutos("0"
                        + (new Integer(calendar.get(Calendar.MINUTE))).toString());
            } else {
                formHorario.setFimPF1Minutos((new Integer(calendar.get(Calendar.MINUTE))).toString());
            }
            if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Seguinte
                formHorario.setDiaSeguintePF1(Constants.DIA_SEGUINTE);
            }
        } else {
            formHorario.setInicioPF1Horas(preenchimento);
            formHorario.setInicioPF1Minutos(preenchimento);
            formHorario.setFimPF1Horas(preenchimento);
            formHorario.setFimPF1Minutos(preenchimento);
        }

        formHorario.setInicioPF2Horas(preenchimento);
        formHorario.setInicioPF2Minutos(preenchimento);
        formHorario.setFimPF2Horas(preenchimento);
        formHorario.setFimPF2Minutos(preenchimento);

        formHorario.setDescontoObrigatorioHoras(preenchimento);
        formHorario.setDescontoObrigatorioMinutos(preenchimento);
        formHorario.setIntervaloMinimoHoras(preenchimento);
        formHorario.setIntervaloMinimoMinutos(preenchimento);

        formHorario.setTrabalhoConsecutivoHoras(preenchimento);
        formHorario.setTrabalhoConsecutivoMinutos(preenchimento);

        formHorario.setInicioRefeicaoHoras(preenchimento);
        formHorario.setInicioRefeicaoMinutos(preenchimento);
        formHorario.setFimRefeicaoHoras(preenchimento);
        formHorario.setFimRefeicaoMinutos(preenchimento);
    } /* setFormAssociarHorarioConfirmar */

    public void setFormAssociarHorarioTipoConfirmar(Locale locale, ActionForm form, Pessoa pessoa,
            Funcionario funcionario, HorarioTipo horarioTipo, Horario horario, List listaRegime,
            boolean isExcepcaoHorario) {
        AssociarHorarioTipoConfirmarForm formHorario = (AssociarHorarioTipoConfirmarForm) form;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);

        formHorario.setExcepcaoHorario(isExcepcaoHorario);

        formHorario.setNome(pessoa.getNome());
        formHorario.setNumMecanografico((new Integer(funcionario.getNumeroMecanografico())).toString());

        formHorario.setDuracaoSemanal((new Integer((new Float(horarioTipo.getDuracaoSemanal()))
                .intValue())).toString());
        formHorario.setModalidade(horarioTipo.getModalidade());
        formHorario.setSigla(horarioTipo.getSigla());

        formHorario.setListaRegime(listaRegime);

        calendar.clear();
        calendar.setTimeInMillis((horarioTipo.getInicioExpediente()).getTime());
        formHorario.setInicioExpedienteHoras((new Integer(calendar.get(Calendar.HOUR_OF_DAY)))
                .toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            formHorario.setInicioExpedienteMinutos("0"
                    + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            formHorario.setInicioExpedienteMinutos((new Integer(calendar.get(Calendar.MINUTE)))
                    .toString());
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
            //contagem no Dia Anterior
            formHorario.setDiaAnteriorExpediente(Constants.DIA_ANTERIOR);
        }

        calendar.clear();
        calendar.setTimeInMillis((horarioTipo.getFimExpediente()).getTime());
        formHorario.setFimExpedienteHoras((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            formHorario.setFimExpedienteMinutos("0"
                    + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            formHorario.setFimExpedienteMinutos((new Integer(calendar.get(Calendar.MINUTE))).toString());
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
            //contagem no Dia Seguinte
            formHorario.setDiaSeguinteExpediente(Constants.DIA_SEGUINTE);
        }

        calendar.clear();
        calendar.setTimeInMillis((horarioTipo.getInicioHN1()).getTime());
        formHorario.setInicioHN1Horas((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            formHorario.setInicioHN1Minutos("0"
                    + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            formHorario.setInicioHN1Minutos((new Integer(calendar.get(Calendar.MINUTE))).toString());
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
            //contagem no Dia Anterior
            formHorario.setDiaAnteriorHN1(Constants.DIA_ANTERIOR);
        }

        calendar.clear();
        calendar.setTimeInMillis((horarioTipo.getFimHN1()).getTime());
        formHorario.setFimHN1Horas((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            formHorario.setFimHN1Minutos("0" + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            formHorario.setFimHN1Minutos((new Integer(calendar.get(Calendar.MINUTE))).toString());
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
            //contagem no Dia Seguinte
            formHorario.setDiaSeguinteHN1(Constants.DIA_SEGUINTE);
        }

        formHorario.setInicioHN2Horas("--");
        formHorario.setInicioHN2Minutos("--");
        formHorario.setFimHN2Horas("--");
        formHorario.setFimHN2Minutos("--");

        calendar.clear();
        calendar.setTime(horario.getDataInicio());
        formHorario.setDiaInicio((new Integer(calendar.get(Calendar.DAY_OF_MONTH))).toString());
        formHorario.setMesInicio((new Integer(calendar.get(Calendar.MONTH) + 1)).toString());
        formHorario.setAnoInicio((new Integer(calendar.get(Calendar.YEAR))).toString());

        calendar.clear();
        calendar.setTime(horario.getDataFim());
        formHorario.setDiaFim((new Integer(calendar.get(Calendar.DAY_OF_MONTH))).toString());
        formHorario.setMesFim((new Integer(calendar.get(Calendar.MONTH) + 1)).toString());
        formHorario.setAnoFim((new Integer(calendar.get(Calendar.YEAR))).toString());

        if (!listaRegime.contains(Constants.REGIME_IPF)) {
            //Regime: new String("isencaoPeriodoFixo")
            calendar.clear();
            calendar.setTimeInMillis((horarioTipo.getInicioPF1()).getTime());
            formHorario.setInicioPF1Horas((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendar.get(Calendar.MINUTE) < 10) {
                formHorario.setInicioPF1Minutos("0"
                        + (new Integer(calendar.get(Calendar.MINUTE))).toString());
            } else {
                formHorario.setInicioPF1Minutos((new Integer(calendar.get(Calendar.MINUTE))).toString());
            }
            if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Anterior
                formHorario.setDiaAnteriorPF1(Constants.DIA_ANTERIOR);
            }

            calendar.clear();
            calendar.setTimeInMillis((horarioTipo.getFimPF1()).getTime());
            formHorario.setFimPF1Horas((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendar.get(Calendar.MINUTE) < 10) {
                formHorario.setFimPF1Minutos("0"
                        + (new Integer(calendar.get(Calendar.MINUTE))).toString());
            } else {
                formHorario.setFimPF1Minutos((new Integer(calendar.get(Calendar.MINUTE))).toString());
            }
            if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Seguinte
                formHorario.setDiaSeguintePF1(Constants.DIA_SEGUINTE);
            }
        } else {
            formHorario.setInicioPF1Horas("--");
            formHorario.setInicioPF1Minutos("--");
            formHorario.setFimPF1Horas("--");
            formHorario.setFimPF1Minutos("--");
        }

        formHorario.setInicioPF2Horas("--");
        formHorario.setInicioPF2Minutos("--");
        formHorario.setFimPF2Horas("--");
        formHorario.setFimPF2Minutos("--");

        formHorario.setDescontoObrigatorioHoras("--");
        formHorario.setDescontoObrigatorioMinutos("--");
        formHorario.setIntervaloMinimoHoras("--");
        formHorario.setIntervaloMinimoMinutos("--");

        formHorario.setTrabalhoConsecutivoHoras("--");
        formHorario.setTrabalhoConsecutivoMinutos("--");

        formHorario.setInicioRefeicaoHoras("--");
        formHorario.setInicioRefeicaoMinutos("--");
        formHorario.setFimRefeicaoHoras("--");
        formHorario.setFimRefeicaoMinutos("--");
    } /* setFormAssociarHorarioTipoConfirmar */

    public String descricaoHorario(Locale locale, Horario horario, List listaRegimes) {
        String descricao = null;
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.APPLICATION_RESOURCES, locale);
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
        inicioHN1Horas = ((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            inicioHN1Minutos = ("0" + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            inicioHN1Minutos = ((new Integer(calendar.get(Calendar.MINUTE))).toString());
        }

        calendar.clear();
        calendar.setTimeInMillis((horario.getFimHN1()).getTime());
        fimHN1Horas = ((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            fimHN1Minutos = ("0" + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            fimHN1Minutos = ((new Integer(calendar.get(Calendar.MINUTE))).toString());
        }

        descricao = descricao.concat(" " + inicioHN1Horas + ":" + inicioHN1Minutos + " - " + fimHN1Horas
                + ":" + fimHN1Minutos);

        return descricao;
    } /* descricaoHorario */

    public String descricaoHorarioTipo(Locale locale, HorarioTipo horarioTipo, List listaRegimes) {
        String descricao = null;
        Calendar calendar = Calendar.getInstance();
        String inicioPF1Horas = null;
        String inicioPF1Minutos = null;
        String fimPF1Horas = null;
        String fimPF1Minutos = null;

        descricao = new String((new Float(horarioTipo.getDuracaoSemanal())).intValue() + " horas ");

        if (!listaRegimes.contains(Constants.REGIME_IPF)) {
            //Regime: new String("isencaoPeriodoFixo")
            calendar.clear();
            calendar.setTimeInMillis((horarioTipo.getInicioPF1()).getTime());
            inicioPF1Horas = ((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendar.get(Calendar.MINUTE) < 10) {
                inicioPF1Minutos = ("0" + (new Integer(calendar.get(Calendar.MINUTE))).toString());
            } else {
                inicioPF1Minutos = ((new Integer(calendar.get(Calendar.MINUTE))).toString());
            }

            calendar.clear();
            calendar.setTimeInMillis((horarioTipo.getFimPF1()).getTime());
            fimPF1Horas = ((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendar.get(Calendar.MINUTE) < 10) {
                fimPF1Minutos = ("0" + (new Integer(calendar.get(Calendar.MINUTE))).toString());
            } else {
                fimPF1Minutos = ((new Integer(calendar.get(Calendar.MINUTE))).toString());
            }

            descricao = descricao.concat(" (" + inicioPF1Horas + ":" + inicioPF1Minutos + " - "
                    + fimPF1Horas + ":" + fimPF1Minutos + ")");
        }

        ResourceBundle bundle = ResourceBundle.getBundle(Constants.APPLICATION_RESOURCES, locale);
        ListIterator iterListaRegimes = listaRegimes.listIterator();
        String regime = null;
        String auxRegime = null;
        descricao = descricao.concat(" ");
        while (iterListaRegimes.hasNext()) {
            regime = (String) iterListaRegimes.next();
            auxRegime = bundle.getString(regime);
            if (auxRegime != null)
                regime = auxRegime;
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
        inicioHN1Horas = ((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            inicioHN1Minutos = ("0" + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            inicioHN1Minutos = ((new Integer(calendar.get(Calendar.MINUTE))).toString());
        }

        calendar.clear();
        calendar.setTimeInMillis((horarioTipo.getFimHN1()).getTime());
        fimHN1Horas = ((new Integer(calendar.get(Calendar.HOUR_OF_DAY))).toString());
        if (calendar.get(Calendar.MINUTE) < 10) {
            fimHN1Minutos = ("0" + (new Integer(calendar.get(Calendar.MINUTE))).toString());
        } else {
            fimHN1Minutos = ((new Integer(calendar.get(Calendar.MINUTE))).toString());
        }

        periodoTrabalho = inicioHN1Horas + ":" + inicioHN1Minutos + " - " + fimHN1Horas + ":"
                + fimHN1Minutos;

        return periodoTrabalho;
    } /* periodoTrabalhoHorarioTipo */

    public void setSaldosHorarioVerbeteBody(Horario horario, List listaRegimes,
            List listaParamJustificacoes, List listaMarcacoesPonto, List listaSaldos) {
        // saldo do horario normal
        Float duracaoDiaria = new Float(horario.getDuracaoSemanal() / Constants.SEMANA_TRABALHO_JORNADA);
        long saldo = ((Long) listaSaldos.get(0)).longValue()
                - (duracaoDiaria.intValue() * 3600 * 1000 + new Float(
                        (duracaoDiaria.floatValue() - duracaoDiaria.intValue()) * 3600 * 1000)
                        .longValue());

        listaSaldos.set(0, new Long(saldo));

        // saldo das plataformas fixas
        //if(listaRegimes.contains("isencaoPeriodoFixo")){ quando estiver a
        // funcionar a nossa base de dados
        if (horario.getInicioPF1() != null) {
            ListIterator iterador = listaMarcacoesPonto.listIterator();
            MarcacaoPonto entrada = null;
            MarcacaoPonto saida = null;
            long trabalhouPF = 0;

            while (iterador.hasNext()) {
                entrada = (MarcacaoPonto) iterador.next();
                if (iterador.hasNext()) {
                    saida = (MarcacaoPonto) iterador.next();

                    // primeiro periodo fixo
                    if (entrada.getData().getTime() >= horario.getInicioPF1().getTime()
                            && entrada.getData().getTime() <= horario.getFimPF1().getTime()) {
                        trabalhouPF = trabalhouPF
                                + (horario.getFimPF1().getTime() - entrada.getData().getTime());
                    } else if (entrada.getData().getTime() < horario.getInicioPF1().getTime()) {
                        trabalhouPF = trabalhouPF
                                + (horario.getFimPF1().getTime() - horario.getInicioPF1().getTime());
                    }
                    if (saida.getData().getTime() >= horario.getInicioPF1().getTime()
                            && saida.getData().getTime() <= horario.getFimPF1().getTime()) {
                        trabalhouPF = trabalhouPF
                                + (saida.getData().getTime() - horario.getFimPF1().getTime());
                    } else if (saida.getData().getTime() < horario.getInicioPF1().getTime()) {
                        trabalhouPF = trabalhouPF
                                - (horario.getFimPF1().getTime() - horario.getInicioPF1().getTime());
                    }
                }
            }

            long saldoInjust = trabalhouPF
                    - (horario.getFimPF1().getTime() - horario.getInicioPF1().getTime());

            listaSaldos.set(1, new Long(saldoInjust));
        }
    } /* setSaldosHorarioVerbeteBody */

    public long calcularTrabalhoNocturno(Horario horario, MarcacaoPonto entrada, MarcacaoPonto saida) {
        long saldoNocturno = 0;

        // calculo do trabalho nocturno normal para os horario que ultrapassem o
        // inicio do trabalho nocturno
        if (horario.getFimHN1().getTime() >= Constants.INICIO_TRABALHO_NOCTURNO) {
            if (entrada.getData().getTime() >= Constants.INICIO_TRABALHO_NOCTURNO
                    && entrada.getData().getTime() <= Constants.FIM_TRABALHO_NOCTURNO) {
                saldoNocturno = saldoNocturno
                        + (Constants.FIM_TRABALHO_NOCTURNO - entrada.getData().getTime());
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

    public void calcularHorasExtraordinarias(Horario horario, List listaMarcacoesPonto, List listaSaldos) {
        if (listaMarcacoesPonto.size() > 0) {
            ListIterator iteradorMarcacoes = listaMarcacoesPonto.listIterator();
            MarcacaoPonto entrada = null;
            MarcacaoPonto saida = null;
            long fimHorario = 0;
            long saldoExtraordinario = 0;

            long saldoPrimEscalao = 0;
            long saldoSegEscalao = 0;
            long saldoDepoisSegEscalao = 0;

            while (iteradorMarcacoes.hasNext()) {
                entrada = (MarcacaoPonto) iteradorMarcacoes.next();

                if (iteradorMarcacoes.hasNext()) {
                    saida = (MarcacaoPonto) iteradorMarcacoes.next();

                    if (horario.getFimHN1().getTime() > Constants.INICIO_TRABALHO_NOCTURNO) {
                        fimHorario = horario.getFimHN1().getTime();
                    } else {
                        fimHorario = Constants.INICIO_TRABALHO_NOCTURNO;
                    }

                    // calculo do trabalho extraordinário nocturno
                    if (entrada.getData().getTime() >= fimHorario
                            && entrada.getData().getTime() <= Constants.FIM_TRABALHO_NOCTURNO) {
                        saldoExtraordinario = saldoExtraordinario
                                + (Constants.FIM_TRABALHO_NOCTURNO - entrada.getData().getTime());
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
            if (saldoExtraordinario > ((Long) listaSaldos.get(0)).longValue()) {
                saldoExtraordinario = ((Long) listaSaldos.get(0)).longValue();
            }
            //calculo dos escalões do trabalho extraordinário nocturno
            if (saldoExtraordinario <= Constants.PRIMEIRO_ESCALAO) {
                saldoPrimEscalao = saldoPrimEscalao + saldoExtraordinario;
            } else {
                saldoPrimEscalao = saldoPrimEscalao + Constants.PRIMEIRO_ESCALAO;
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
            saldoExtraordinario = ((Long) listaSaldos.get(0)).longValue() - saldoExtraordinario;

            saldoPrimEscalao = 0;
            saldoSegEscalao = 0;
            saldoDepoisSegEscalao = 0;
            //calculo dos escalões do trabalho extraordinário diurno
            if (saldoExtraordinario <= Constants.PRIMEIRO_ESCALAO) {
                saldoPrimEscalao = saldoPrimEscalao + saldoExtraordinario;
            } else {
                saldoPrimEscalao = saldoPrimEscalao + Constants.PRIMEIRO_ESCALAO;
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

    public long limitaTrabalhoSeguido(Horario horario, long entrada, long saida, boolean limita) {
        long saldo = saida - entrada;

        return saldo;
    } /* limitaTrabalhoSeguido */

    public long duracaoPF(Horario horario, List listaRegimes) {
        if (!listaRegimes.contains(Constants.REGIME_IPF)) {
            return Constants.PLATAFORMAS_FIXAS_JORNADA;
        }
        return 0;
    } /* duracaoPF */

    public long duracaoDiaria(Horario horario) {
        return ((new Float(Constants.DURACAO_SEMANAL_JORNADA / Constants.SEMANA_TRABALHO_JORNADA)
                .intValue() * 3600 * 1000) + new Float(
                (new Float(Constants.DURACAO_SEMANAL_JORNADA / Constants.SEMANA_TRABALHO_JORNADA)
                        .floatValue() - new Float(Constants.DURACAO_SEMANAL_JORNADA
                        / Constants.SEMANA_TRABALHO_JORNADA).intValue()) * 3600 * 1000).longValue());
    } /* duracaoDiaria */

    public int mapeamentoFechoMes(Horario horario, List listaRegimes) {
        if (listaRegimes.contains(Constants.REGIME_TE)) {
            return Constants.MAP_JC_TE;
        } else if (listaRegimes.contains(Constants.REGIME_MENORES12)) {
            return Constants.MAP_JC_FILHOS12;
        } else if (listaRegimes.contains(Constants.REGIME_FILHOSDEFICIENTES)) {
            return Constants.MAP_JC_FILHOSDEF;
        } else if (listaRegimes.contains(Constants.REGIME_SERVICO)) {
            return Constants.MAP_JC_FILHOSDEF;
        } else {
            return Constants.MAP_JC_OUTRAS;
        }
    } /* mapeamentoFechoMes */
}