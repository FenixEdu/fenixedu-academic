package net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.domain.Horario;
import net.sourceforge.fenixedu.domain.IStrategyHorarios;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.SuporteStrategyHorarios;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class AssociarHorarioForm extends ActionForm {
    private String _nome = null;

    private String _numMecanografico = null;

    private String _inicioExpedienteHoras = null;

    private String _inicioExpedienteMinutos = null;

    private String _fimExpedienteHoras = null;

    private String _fimExpedienteMinutos = null;

    private String _diaAnteriorExpediente = null;

    private String _diaSeguinteExpediente = null;

    private String _inicioRefeicaoHoras = null;

    private String _inicioRefeicaoMinutos = null;

    private String _fimRefeicaoHoras = null;

    private String _fimRefeicaoMinutos = null;

    private String _diaAnteriorRefeicao = null;

    private String _diaSeguinteRefeicao = null;

    private String _intervaloMinimoHoras = null;

    private String _intervaloMinimoMinutos = null;

    private String _descontoObrigatorioHoras = null;

    private String _descontoObrigatorioMinutos = null;

    private String _inicioHN1Horas = null;

    private String _inicioHN1Minutos = null;

    private String _fimHN1Horas = null;

    private String _fimHN1Minutos = null;

    private String _diaAnteriorHN1 = null;

    private String _diaSeguinteHN1 = null;

    private String _inicioHN2Horas = null;

    private String _inicioHN2Minutos = null;

    private String _fimHN2Horas = null;

    private String _fimHN2Minutos = null;

    private String _diaAnteriorHN2 = null;

    private String _diaSeguinteHN2 = null;

    private String _inicioPF1Horas = null;

    private String _inicioPF1Minutos = null;

    private String _fimPF1Horas = null;

    private String _fimPF1Minutos = null;

    private String _diaAnteriorPF1 = null;

    private String _diaSeguintePF1 = null;

    private String _inicioPF2Horas = null;

    private String _inicioPF2Minutos = null;

    private String _fimPF2Horas = null;

    private String _fimPF2Minutos = null;

    private String _diaAnteriorPF2 = null;

    private String _diaSeguintePF2 = null;

    private String _diaCumprir = null;

    private String _mesCumprir = null;

    private String _anoCumprir = null;

    private String _diaInicio = null;

    private String _mesInicio = null;

    private String _anoInicio = null;

    private String _diaFim = null;

    private String _mesFim = null;

    private String _anoFim = null;

    private String _duracaoSemanal = null;

    private String _modalidade = null;

    private String _sigla = null;

    private String _numDias = null;

    private String _posicao = null;

    private String _trabalhoConsecutivoHoras = null;

    private String _trabalhoConsecutivoMinutos = null;

    private List _listaModalidades = new ArrayList();

    private String[] _regime = null;

    private List _listaRegimes = new ArrayList();

    private boolean _excepcaoHorario = false;

    /* construtores */
    public AssociarHorarioForm() {
    }

    /* Constroi um Horário para ser mostrado */
    //	public AssociarHorarioForm(Horario horario, List listaRegimes) {
    public AssociarHorarioForm getFormHorarioPreenchido(Horario horario, List listaRegimes,
            String alterar) {
        Calendar calendario = Calendar.getInstance();

        String preenchimento = new String("--");
        if (alterar != null) {
            preenchimento = "";
        }

        setNumDias(String.valueOf(horario.getNumDias()));
        setPosicao(String.valueOf(horario.getPosicao()));

        if (horario.getChaveHorarioTipo() != 0) {
            // é um horário tipo ou um horário da BD oracle
            setSigla(horario.getSigla());
        } else {
            // é um horário personalizado
            setSigla("semSigla");
        }

        setListaRegimes(listaRegimes);

        setDuracaoSemanal(String.valueOf(horario.getDuracaoSemanal()));
        setModalidade(horario.getModalidade());

        if (horario.getInicioHN1() != null) {
            calendario.setTimeInMillis(horario.getInicioHN1().getTime());
            setInicioHN1Horas(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
            if (calendario.get(Calendar.MINUTE) < 10) {
                setInicioHN1Minutos("0" + String.valueOf(calendario.get(Calendar.MINUTE)));
            } else {
                setInicioHN1Minutos(String.valueOf(calendario.get(Calendar.MINUTE)));
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Anterior
                setDiaAnteriorHN1("A");
            }
        } else {
            setInicioHN1Horas(preenchimento);
            setInicioHN1Minutos(preenchimento);
        }

        if (horario.getFimHN1() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getFimHN1().getTime());
            setFimHN1Horas(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
            if (calendario.get(Calendar.MINUTE) < 10) {
                setFimHN1Minutos("0" + String.valueOf(calendario.get(Calendar.MINUTE)));
            } else {
                setFimHN1Minutos(String.valueOf(calendario.get(Calendar.MINUTE)));
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Seguinte
                setDiaSeguinteHN1("S");
            }
        } else {
            setFimHN1Horas(preenchimento);
            setFimHN1Minutos(preenchimento);
        }
        if (horario.getInicioHN2() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getInicioHN2().getTime());
            setInicioHN2Horas(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
            if (calendario.get(Calendar.MINUTE) < 10) {
                setInicioHN2Minutos("0" + String.valueOf(calendario.get(Calendar.MINUTE)));
            } else {
                setInicioHN2Minutos(String.valueOf(calendario.get(Calendar.MINUTE)));
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Anterior
                setDiaAnteriorHN2("A");
            }
        } else {
            setInicioHN2Horas(preenchimento);
            setInicioHN2Minutos(preenchimento);
        }

        if (horario.getFimHN2() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getFimHN2().getTime());
            setFimHN2Horas(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
            if (calendario.get(Calendar.MINUTE) < 10) {
                setFimHN2Minutos("0" + String.valueOf(calendario.get(Calendar.MINUTE)));
            } else {
                setFimHN2Minutos(String.valueOf(calendario.get(Calendar.MINUTE)));
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Seguinte
                setDiaSeguinteHN2("S");
            }
        } else {
            setFimHN2Horas(preenchimento);
            setFimHN2Minutos(preenchimento);
        }

        if (horario.getInicioPF1() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getInicioPF1().getTime());
            setInicioPF1Horas(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
            if (calendario.get(Calendar.MINUTE) < 10) {
                setInicioPF1Minutos("0" + String.valueOf(calendario.get(Calendar.MINUTE)));
            } else {
                setInicioPF1Minutos(String.valueOf(calendario.get(Calendar.MINUTE)));
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Anterior
                setDiaAnteriorPF1("A");
            }
        } else {
            setInicioPF1Horas(preenchimento);
            setInicioPF1Minutos(preenchimento);
        }

        if (horario.getFimPF1() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getFimPF1().getTime());
            setFimPF1Horas(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
            if (calendario.get(Calendar.MINUTE) < 10) {
                setFimPF1Minutos("0" + String.valueOf(calendario.get(Calendar.MINUTE)));
            } else {
                setFimPF1Minutos(String.valueOf(calendario.get(Calendar.MINUTE)));
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Seguinte
                setDiaSeguintePF1("S");
            }
        } else {
            setFimPF1Horas(preenchimento);
            setFimPF1Minutos(preenchimento);
        }

        if (horario.getInicioPF2() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getInicioPF2().getTime());
            setInicioPF2Horas(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
            if (calendario.get(Calendar.MINUTE) < 10) {
                setInicioPF2Minutos("0" + String.valueOf(calendario.get(Calendar.MINUTE)));
            } else {
                setInicioPF2Minutos(String.valueOf(calendario.get(Calendar.MINUTE)));
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Anterior
                setDiaAnteriorPF2("A");
            }
        } else {
            setInicioPF2Horas(preenchimento);
            setInicioPF2Minutos(preenchimento);
        }

        if (horario.getFimPF2() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getFimPF2().getTime());
            setFimPF2Horas(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
            if (calendario.get(Calendar.MINUTE) < 10) {
                setFimPF2Minutos("0" + String.valueOf(calendario.get(Calendar.MINUTE)));
            } else {
                setFimPF2Minutos(String.valueOf(calendario.get(Calendar.MINUTE)));
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Seguinte
                setDiaSeguintePF2("S");
            }
        } else {
            setFimPF2Horas(preenchimento);
            setFimPF2Minutos(preenchimento);
        }

        if (horario.getInicioExpediente() != null) {
            calendario.setTimeInMillis((horario.getInicioExpediente()).getTime());
            setInicioExpedienteHoras((new Integer(calendario.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendario.get(Calendar.MINUTE) < 10) {
                setInicioExpedienteMinutos("0"
                        + (new Integer(calendario.get(Calendar.MINUTE))).toString());
            } else {
                setInicioExpedienteMinutos((new Integer(calendario.get(Calendar.MINUTE))).toString());
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Anterior
                setDiaAnteriorExpediente("A");
            }
        } else {
            setInicioExpedienteHoras(preenchimento);
            setInicioExpedienteMinutos(preenchimento);
        }

        if (horario.getFimExpediente() != null) {
            calendario.clear();
            calendario.setTimeInMillis((horario.getFimExpediente()).getTime());
            setFimExpedienteHoras((new Integer(calendario.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendario.get(Calendar.MINUTE) < 10) {
                setFimExpedienteMinutos("0" + (new Integer(calendario.get(Calendar.MINUTE))).toString());
            } else {
                setFimExpedienteMinutos((new Integer(calendario.get(Calendar.MINUTE))).toString());
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Seguinte
                setDiaSeguinteExpediente("S");
            }
        } else {
            setFimExpedienteHoras(preenchimento);
            setFimExpedienteMinutos(preenchimento);
        }

        //intervalo e dados de refeicao
        if ((horario.getInicioRefeicao() != null) && (horario.getFimRefeicao() != null)) {
            calendario.clear();
            calendario.setTimeInMillis((horario.getInicioRefeicao()).getTime());
            setInicioRefeicaoHoras((new Integer(calendario.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendario.get(Calendar.MINUTE) < 10) {
                setInicioRefeicaoMinutos("0" + (new Integer(calendario.get(Calendar.MINUTE))).toString());
            } else {
                setInicioRefeicaoMinutos((new Integer(calendario.get(Calendar.MINUTE))).toString());
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Seguinte
                setDiaAnteriorRefeicao("A");
            }

            calendario.clear();
            calendario.setTimeInMillis((horario.getFimRefeicao()).getTime());
            setFimRefeicaoHoras((new Integer(calendario.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendario.get(Calendar.MINUTE) < 10) {
                setFimRefeicaoMinutos("0" + (new Integer(calendario.get(Calendar.MINUTE))).toString());
            } else {
                setFimRefeicaoMinutos((new Integer(calendario.get(Calendar.MINUTE))).toString());
            }
            if (calendario.get(Calendar.DAY_OF_MONTH) != 1) {
                //contagem no Dia Seguinte
                setDiaSeguinteRefeicao("S");
            }
        } else {
            setInicioRefeicaoHoras(preenchimento);
            setInicioRefeicaoMinutos(preenchimento);
            setFimRefeicaoHoras(preenchimento);
            setFimRefeicaoMinutos(preenchimento);
        }

        //		if (horario.getIntervaloMinimoRefeicao() != null) {
        //			calendario.clear();
        //			calendario.setTimeInMillis((horario.getIntervaloMinimoRefeicao()).getTime());
        //
        //			calendario.add(Calendar.HOUR_OF_DAY, -1);
        //
        //			setIntervaloMinimoHoras((new
        // Integer(calendario.get(Calendar.HOUR_OF_DAY))).toString());
        //			if (calendario.get(Calendar.MINUTE) < 10) {
        //				setIntervaloMinimoMinutos("0" + (new
        // Integer(calendario.get(Calendar.MINUTE))).toString());
        //			} else {
        //				setIntervaloMinimoMinutos((new
        // Integer(calendario.get(Calendar.MINUTE))).toString());
        //			}
        //		} else {
        //			setIntervaloMinimoHoras(preenchimento);
        //			setIntervaloMinimoMinutos(preenchimento);
        //		}
        //		if (horario.getDescontoObrigatorioRefeicao() != null) {
        //			calendario.clear();
        //			calendario.setTimeInMillis((horario.getDescontoObrigatorioRefeicao()).getTime());
        //
        //			calendario.add(Calendar.HOUR_OF_DAY, -1);
        //
        //			setDescontoObrigatorioHoras((new
        // Integer(calendario.get(Calendar.HOUR_OF_DAY))).toString());
        //			if (calendario.get(Calendar.MINUTE) < 10) {
        //				setDescontoObrigatorioMinutos("0" + (new
        // Integer(calendario.get(Calendar.MINUTE))).toString());
        //			} else {
        //				setDescontoObrigatorioMinutos((new
        // Integer(calendario.get(Calendar.MINUTE))).toString());
        //			}
        //		} else {
        //			setDescontoObrigatorioHoras(preenchimento);
        //			setDescontoObrigatorioMinutos(preenchimento);
        //		}
        //
        //		if (horario.getTrabalhoConsecutivo() != null) {
        //			calendario.clear();
        //			calendario.setTimeInMillis(horario.getTrabalhoConsecutivo().getTime());
        //			calendario.add(Calendar.HOUR_OF_DAY, -1);
        //			setTrabalhoConsecutivoHoras((new
        // Integer(calendario.get(Calendar.HOUR_OF_DAY))).toString());
        //			if (calendario.get(Calendar.MINUTE) < 10) {
        //				setTrabalhoConsecutivoMinutos("0" + (new
        // Integer(calendario.get(Calendar.MINUTE))).toString());
        //			} else {
        //				setTrabalhoConsecutivoMinutos((new
        // Integer(calendario.get(Calendar.MINUTE))).toString());
        //			}
        //		} else {
        //			setTrabalhoConsecutivoHoras(preenchimento);
        //			setTrabalhoConsecutivoMinutos(preenchimento);
        //		}

        if (horario.getDataCumprir() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getDataCumprir().getTime());

            setAnoCumprir(String.valueOf(calendario.get(Calendar.YEAR)));
            if ((calendario.get(Calendar.MONTH) + 1) < 10) {
                setMesCumprir("0" + String.valueOf(calendario.get(Calendar.MONTH) + 1));
            } else {
                setMesCumprir(String.valueOf(calendario.get(Calendar.MONTH) + 1));
            }
            if ((calendario.get(Calendar.DAY_OF_MONTH)) < 10) {
                setDiaCumprir("0" + String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
            } else {
                setDiaCumprir(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
            }
        } else {
            setAnoCumprir(preenchimento + preenchimento);
            setMesCumprir(preenchimento);
            setDiaCumprir(preenchimento);
        }

        if (horario.getDataInicio() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getDataInicio().getTime());

            setAnoInicio(String.valueOf(calendario.get(Calendar.YEAR)));
            if ((calendario.get(Calendar.MONTH) + 1) < 10) {
                setMesInicio("0" + String.valueOf(calendario.get(Calendar.MONTH) + 1));
            } else {
                setMesInicio(String.valueOf(calendario.get(Calendar.MONTH) + 1));
            }
            if ((calendario.get(Calendar.DAY_OF_MONTH)) < 10) {
                setDiaInicio("0" + String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
            } else {
                setDiaInicio(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
            }
        } else {
            setAnoInicio(preenchimento + preenchimento);
            setMesInicio(preenchimento);
            setDiaInicio(preenchimento);
        }

        if (horario.getDataFim() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getDataFim().getTime());

            setAnoFim(String.valueOf(calendario.get(Calendar.YEAR)));
            if ((calendario.get(Calendar.MONTH) + 1) < 10) {
                setMesFim("0" + String.valueOf(calendario.get(Calendar.MONTH) + 1));
            } else {
                setMesFim(String.valueOf(calendario.get(Calendar.MONTH) + 1));
            }
            if ((calendario.get(Calendar.DAY_OF_MONTH)) < 10) {
                setDiaFim("0" + String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
            } else {
                setDiaFim(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
            }
        } else {
            setAnoFim(preenchimento + preenchimento);
            setMesFim(preenchimento);
            setDiaFim(preenchimento);
        }
        return this;
    }

    public void setDuracoesHorario(Horario horario, boolean vemBaseDados) {
        Calendar calendario = Calendar.getInstance();
        String preenchimento = "--";

        if (horario.getIntervaloMinimoRefeicao() != null) {
            calendario.clear();
            calendario.setTimeInMillis((horario.getIntervaloMinimoRefeicao()).getTime());
            if (vemBaseDados) {
                calendario.add(Calendar.HOUR_OF_DAY, -1);
            }

            setIntervaloMinimoHoras((new Integer(calendario.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendario.get(Calendar.MINUTE) < 10) {
                setIntervaloMinimoMinutos("0"
                        + (new Integer(calendario.get(Calendar.MINUTE))).toString());
            } else {
                setIntervaloMinimoMinutos((new Integer(calendario.get(Calendar.MINUTE))).toString());
            }
        } else {
            setIntervaloMinimoHoras(preenchimento);
            setIntervaloMinimoMinutos(preenchimento);
        }
        if (horario.getDescontoObrigatorioRefeicao() != null) {
            calendario.clear();
            calendario.setTimeInMillis((horario.getDescontoObrigatorioRefeicao()).getTime());
            if (vemBaseDados) {
                calendario.add(Calendar.HOUR_OF_DAY, -1);
            }
            setDescontoObrigatorioHoras((new Integer(calendario.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendario.get(Calendar.MINUTE) < 10) {
                setDescontoObrigatorioMinutos("0"
                        + (new Integer(calendario.get(Calendar.MINUTE))).toString());
            } else {
                setDescontoObrigatorioMinutos((new Integer(calendario.get(Calendar.MINUTE))).toString());
            }
        } else {
            setDescontoObrigatorioHoras(preenchimento);
            setDescontoObrigatorioMinutos(preenchimento);
        }

        if (horario.getTrabalhoConsecutivo() != null) {
            calendario.clear();
            calendario.setTimeInMillis(horario.getTrabalhoConsecutivo().getTime());
            if (vemBaseDados) {
                calendario.add(Calendar.HOUR_OF_DAY, -1);
            }
            setTrabalhoConsecutivoHoras((new Integer(calendario.get(Calendar.HOUR_OF_DAY))).toString());
            if (calendario.get(Calendar.MINUTE) < 10) {
                setTrabalhoConsecutivoMinutos("0"
                        + (new Integer(calendario.get(Calendar.MINUTE))).toString());
            } else {
                setTrabalhoConsecutivoMinutos((new Integer(calendario.get(Calendar.MINUTE))).toString());
            }
        } else {
            setTrabalhoConsecutivoHoras(preenchimento);
            setTrabalhoConsecutivoMinutos(preenchimento);
        }
    } /* setDuracoesHorario */

    public String getTrabalhoConsecutivoHoras() {
        return _trabalhoConsecutivoHoras;
    }

    public void setTrabalhoConsecutivoHoras(String consecutivo) {
        _trabalhoConsecutivoHoras = consecutivo;
    }

    public String getTrabalhoConsecutivoMinutos() {
        return _trabalhoConsecutivoMinutos;
    }

    public void setTrabalhoConsecutivoMinutos(String consecutivo) {
        _trabalhoConsecutivoMinutos = consecutivo;
    }

    public String getNome() {
        return (_nome);
    }

    public String getNumMecanografico() {
        return (_numMecanografico);
    }

    public String getInicioExpedienteHoras() {
        return (_inicioExpedienteHoras);
    }

    public String getInicioExpedienteMinutos() {
        return (_inicioExpedienteMinutos);
    }

    public String getFimExpedienteHoras() {
        return (_fimExpedienteHoras);
    }

    public String getFimExpedienteMinutos() {
        return (_fimExpedienteMinutos);
    }

    public String getDiaAnteriorExpediente() {
        return (_diaAnteriorExpediente);
    }

    public String getDiaSeguinteExpediente() {
        return (_diaSeguinteExpediente);
    }

    public String getInicioRefeicaoHoras() {
        return _inicioRefeicaoHoras;
    }

    public String getInicioRefeicaoMinutos() {
        return _inicioRefeicaoMinutos;
    }

    public String getFimRefeicaoHoras() {
        return _fimRefeicaoHoras;
    }

    public String getFimRefeicaoMinutos() {
        return _fimRefeicaoMinutos;
    }

    public String getDiaAnteriorRefeicao() {
        return _diaAnteriorRefeicao;
    }

    public String getDiaSeguinteRefeicao() {
        return _diaSeguinteRefeicao;
    }

    public String getIntervaloMinimoHoras() {
        return _intervaloMinimoHoras;
    }

    public String getIntervaloMinimoMinutos() {
        return _intervaloMinimoMinutos;
    }

    public String getDescontoObrigatorioHoras() {
        return _descontoObrigatorioHoras;
    }

    public String getDescontoObrigatorioMinutos() {
        return _descontoObrigatorioMinutos;
    }

    public String getInicioHN1Horas() {
        return (_inicioHN1Horas);
    }

    public String getInicioHN1Minutos() {
        return (_inicioHN1Minutos);
    }

    public String getFimHN1Horas() {
        return (_fimHN1Horas);
    }

    public String getFimHN1Minutos() {
        return (_fimHN1Minutos);
    }

    public String getInicioHN2Horas() {
        return (_inicioHN2Horas);
    }

    public String getInicioHN2Minutos() {
        return (_inicioHN2Minutos);
    }

    public String getFimHN2Horas() {
        return (_fimHN2Horas);
    }

    public String getFimHN2Minutos() {
        return (_fimHN2Minutos);
    }

    public String getDiaAnteriorHN1() {
        return _diaAnteriorHN1;
    }

    public String getDiaAnteriorHN2() {
        return _diaAnteriorHN2;
    }

    public String getDiaSeguinteHN1() {
        return _diaSeguinteHN1;
    }

    public String getDiaSeguinteHN2() {
        return _diaSeguinteHN2;
    }

    public String getInicioPF1Horas() {
        return (_inicioPF1Horas);
    }

    public String getInicioPF1Minutos() {
        return (_inicioPF1Minutos);
    }

    public String getFimPF1Horas() {
        return (_fimPF1Horas);
    }

    public String getFimPF1Minutos() {
        return (_fimPF1Minutos);
    }

    public String getInicioPF2Horas() {
        return (_inicioPF2Horas);
    }

    public String getInicioPF2Minutos() {
        return (_inicioPF2Minutos);
    }

    public String getFimPF2Horas() {
        return (_fimPF2Horas);
    }

    public String getFimPF2Minutos() {
        return (_fimPF2Minutos);
    }

    public String getDiaAnteriorPF1() {
        return _diaAnteriorPF1;
    }

    public String getDiaAnteriorPF2() {
        return _diaAnteriorPF2;
    }

    public String getDiaSeguintePF1() {
        return _diaSeguintePF1;
    }

    public String getDiaSeguintePF2() {
        return _diaSeguintePF2;
    }

    public String getDiaCumprir() {
        return (_diaCumprir);
    }

    public String getMesCumprir() {
        return (_mesCumprir);
    }

    public String getAnoCumprir() {
        return (_anoCumprir);
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

    public String getDuracaoSemanal() {
        return (_duracaoSemanal);
    }

    public String getModalidade() {
        return (_modalidade);
    }

    public String getSigla() {
        return (_sigla);
    }

    public String getNumDias() {
        return _numDias;
    }

    public String getPosicao() {
        return _posicao;
    }

    public List getListaModalidades() {
        return (_listaModalidades);
    }

    public String[] getRegime() {
        return (_regime);
    }

    public List getListaRegimes() {
        return (_listaRegimes);
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

    public void setInicioExpedienteHoras(String inicioExpedienteHoras) {
        _inicioExpedienteHoras = inicioExpedienteHoras;
    }

    public void setInicioExpedienteMinutos(String inicioExpedienteMinutos) {
        _inicioExpedienteMinutos = inicioExpedienteMinutos;
    }

    public void setFimExpedienteHoras(String fimExpedienteHoras) {
        _fimExpedienteHoras = fimExpedienteHoras;
    }

    public void setFimExpedienteMinutos(String fimExpedienteMinutos) {
        _fimExpedienteMinutos = fimExpedienteMinutos;
    }

    public void setDiaAnteriorExpediente(String diaAnteriorExpediente) {
        _diaAnteriorExpediente = diaAnteriorExpediente;
    }

    public void setDiaSeguinteExpediente(String diaSeguinteExpediente) {
        _diaSeguinteExpediente = diaSeguinteExpediente;
    }

    public void setInicioRefeicaoHoras(String _inicioRefeicaoHoras) {
        this._inicioRefeicaoHoras = _inicioRefeicaoHoras;
    }

    public void setInicioRefeicaoMinutos(String _inicioRefeicaoMinutos) {
        this._inicioRefeicaoMinutos = _inicioRefeicaoMinutos;
    }

    public void setFimRefeicaoHoras(String _fimRefeicaoHoras) {
        this._fimRefeicaoHoras = _fimRefeicaoHoras;
    }

    public void setFimRefeicaoMinutos(String _fimRefeicaoMinutos) {
        this._fimRefeicaoMinutos = _fimRefeicaoMinutos;
    }

    public void setDiaAnteriorRefeicao(String diaAnteriorRefeicao) {
        _diaAnteriorRefeicao = diaAnteriorRefeicao;
    }

    public void setDiaSeguinteRefeicao(String diaSeguinteRefeicao) {
        _diaSeguinteRefeicao = diaSeguinteRefeicao;
    }

    public void setIntervaloMinimoHoras(String _intervaloMinimoHoras) {
        this._intervaloMinimoHoras = _intervaloMinimoHoras;
    }

    public void setIntervaloMinimoMinutos(String _intervaloMinimoMinutos) {
        this._intervaloMinimoMinutos = _intervaloMinimoMinutos;
    }

    public void setDescontoObrigatorioHoras(String _descontoObrigatorioHoras) {
        this._descontoObrigatorioHoras = _descontoObrigatorioHoras;
    }

    public void setDescontoObrigatorioMinutos(String _descontoObrigatorioMinutos) {
        this._descontoObrigatorioMinutos = _descontoObrigatorioMinutos;
    }

    public void setInicioHN1Horas(String inicioHN1Horas) {
        _inicioHN1Horas = inicioHN1Horas;
    }

    public void setInicioHN1Minutos(String inicioHN1Minutos) {
        _inicioHN1Minutos = inicioHN1Minutos;
    }

    public void setFimHN1Horas(String fimHN1Horas) {
        _fimHN1Horas = fimHN1Horas;
    }

    public void setFimHN1Minutos(String fimHN1Minutos) {
        _fimHN1Minutos = fimHN1Minutos;
    }

    public void setInicioHN2Horas(String inicioHN2Horas) {
        _inicioHN2Horas = inicioHN2Horas;
    }

    public void setInicioHN2Minutos(String inicioHN2Minutos) {
        _inicioHN2Minutos = inicioHN2Minutos;
    }

    public void setFimHN2Horas(String fimHN2Horas) {
        _fimHN2Horas = fimHN2Horas;
    }

    public void setFimHN2Minutos(String fimHN2Minutos) {
        _fimHN2Minutos = fimHN2Minutos;
    }

    public void setDiaAnteriorHN1(String diaAnteriorHN1) {
        _diaAnteriorHN1 = diaAnteriorHN1;
    }

    public void setDiaAnteriorHN2(String diaAnteriorHN2) {
        _diaAnteriorHN2 = diaAnteriorHN2;
    }

    public void setDiaSeguinteHN1(String diaSeguinteHN1) {
        _diaSeguinteHN1 = diaSeguinteHN1;
    }

    public void setDiaSeguinteHN2(String diaSeguinteHN2) {
        _diaSeguinteHN2 = diaSeguinteHN2;
    }

    public void setInicioPF1Horas(String inicioPF1Horas) {
        _inicioPF1Horas = inicioPF1Horas;
    }

    public void setInicioPF1Minutos(String inicioPF1Minutos) {
        _inicioPF1Minutos = inicioPF1Minutos;
    }

    public void setFimPF1Horas(String fimPF1Horas) {
        _fimPF1Horas = fimPF1Horas;
    }

    public void setFimPF1Minutos(String fimPF1Minutos) {
        _fimPF1Minutos = fimPF1Minutos;
    }

    public void setInicioPF2Horas(String inicioPF2Horas) {
        _inicioPF2Horas = inicioPF2Horas;
    }

    public void setInicioPF2Minutos(String inicioPF2Minutos) {
        _inicioPF2Minutos = inicioPF2Minutos;
    }

    public void setFimPF2Horas(String fimPF2Horas) {
        _fimPF2Horas = fimPF2Horas;
    }

    public void setFimPF2Minutos(String fimPF2Minutos) {
        _fimPF2Minutos = fimPF2Minutos;
    }

    public void setDiaAnteriorPF1(String diaAnteriorPF1) {
        _diaAnteriorPF1 = diaAnteriorPF1;
    }

    public void setDiaAnteriorPF2(String diaAnteriorPF2) {
        _diaAnteriorPF2 = diaAnteriorPF2;
    }

    public void setDiaSeguintePF1(String diaSeguintePF1) {
        _diaSeguintePF1 = diaSeguintePF1;
    }

    public void setDiaSeguintePF2(String diaSeguintePF2) {
        _diaSeguintePF2 = diaSeguintePF2;
    }

    public void setDiaCumprir(String diaCumprir) {
        _diaCumprir = diaCumprir;
    }

    public void setMesCumprir(String mesCumprir) {
        _mesCumprir = mesCumprir;
    }

    public void setAnoCumprir(String anoCumprir) {
        _anoCumprir = anoCumprir;
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

    public void setDuracaoSemanal(String duracaoSemanal) {
        _duracaoSemanal = duracaoSemanal;
    }

    public void setModalidade(String modalidade) {
        _modalidade = modalidade;
    }

    public void setSigla(String sigla) {
        _sigla = sigla;
    }

    public void setNumDias(String numDias) {
        _numDias = numDias;
    }

    public void setPosicao(String posicao) {
        _posicao = posicao;
    }

    public void setListaModalidades(List listaModalidades) {
        _listaModalidades = listaModalidades;
    }

    public void setRegime(String[] regime) {
        _regime = regime;
    }

    public void setListaRegimes(List listaRegimes) {
        _listaRegimes = listaRegimes;
    }

    public void setExcepcaoHorario(boolean excepcaoHorario) {
        _excepcaoHorario = excepcaoHorario;
    }

    public void setForm(List modalidades, List regimes, List listaRegimesHorarioAlterar) {
        setListaModalidades(modalidades);
        setListaRegimes(regimes);

        //		testes para o caso de alteraçao de horario que so pode ocorrer logo
        // apos a associacao
        if (getDiaInicio() == null) {
            Calendar agora = Calendar.getInstance();
            setDiaInicio((new Integer(agora.get(Calendar.DAY_OF_MONTH))).toString());
            setMesInicio((new Integer(agora.get(Calendar.MONTH) + 1)).toString());
            setAnoInicio((new Integer(agora.get(Calendar.YEAR))).toString());
        }
        if (listaRegimesHorarioAlterar != null) {

            //			setRegime((String[])listaRegimesHorarioAlterar.toArray());
            Iterator iterRegimes = listaRegimesHorarioAlterar.iterator();
            int i = 0;
            _regime = new String[listaRegimesHorarioAlterar.size()];
            while (iterRegimes.hasNext()) {
                String regime = (String) iterRegimes.next();
                _regime[i++] = regime;
            }
        }
    }

    public void setForm(Locale locale, Person pessoa, Funcionario funcionario, Horario horario,
            List listaRegime, boolean isExcepcaoHorario, String alterar) {
        IStrategyHorarios horarioStrategy = SuporteStrategyHorarios.getInstance().callStrategy(
                getModalidade());
        horarioStrategy.setFormAssociarHorarioConfirmar(locale, this, pessoa, funcionario, horario,
                (ArrayList) listaRegime, isExcepcaoHorario, alterar);
    } /* setForm */

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        _numDias = "";
        _posicao = "";

        _numMecanografico = "";

        _inicioExpedienteHoras = "";
        _inicioExpedienteMinutos = "";
        _fimExpedienteHoras = "";
        _fimExpedienteMinutos = "";

        _diaAnteriorExpediente = "";
        _diaSeguinteExpediente = "";

        _inicioHN1Horas = "";
        _inicioHN1Minutos = "";
        _fimHN1Horas = "";
        _fimHN1Minutos = "";

        _inicioHN2Horas = "";
        _inicioHN2Minutos = "";
        _fimHN2Horas = "";
        _fimHN2Minutos = "";

        _inicioPF1Horas = "";
        _inicioPF1Minutos = "";
        _fimPF1Horas = "";
        _fimPF1Minutos = "";

        _inicioPF2Horas = "";
        _inicioPF2Minutos = "";
        _fimPF2Horas = "";
        _fimPF2Minutos = "";

        _diaFim = "";
        _mesFim = "";
        _anoFim = "";

        _duracaoSemanal = "";

        _excepcaoHorario = false;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        if (mapping.getPath().equals(new String("/associarHorario"))) {
            if (request.getParameter("numMecanografico") != null) {

                if ((request.getParameter("numMecanografico")).length() < 1) {
                    errors.add("numMecanografico", new ActionError("error.numero.obrigatorio"));
                    return errors;
                }
                try {
                    (new Integer(getNumMecanografico())).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError("error.numero.naoInteiro"));
                    return errors;
                }

            }
        } else if (mapping.getPath().equals(new String("/adicionarHorarioRotacao"))) {
            if ((request.getParameter("numDias") != null) && (request.getParameter("posicao") != null)) {
                if (((request.getParameter("numDias")).length() < 1)
                        || ((request.getParameter("posicao")).length() < 1)) {
                    errors.add("numero", new ActionError("error.numero.obrigatorio"));
                    return errors;
                }
                int numDias = 0;
                int posicao = 0;
                try {
                    numDias = (new Integer(getNumDias())).intValue();
                    posicao = (new Integer(getPosicao())).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError("error.numero.naoInteiro"));
                    return errors;
                }

                // testa a posição e a duração da nova rotação

                List rotacaoHorario = new ArrayList();
                if ((rotacaoHorario = (ArrayList) session.getAttribute("rotacaoHorario")) != null) {
                    List novaLista = (List) ((ArrayList) rotacaoHorario).clone();
                    Horario horario = null;
                    int posMaxPrimeiroHorario = 0;

                    // se for uma alteracao de horario a validacao das posicoes
                    // nao pode contar com o horario anterior
                    if (request.getParameter("alterar") != null) {
                        novaLista.remove(Integer.valueOf(request.getParameter("indiceRotacao"))
                                .intValue());
                    }
                    ListIterator iterador = novaLista.listIterator();

                    if (iterador.hasNext()) {
                        horario = (Horario) iterador.next();

                        if ((posicao >= horario.getPosicao())
                                && (posicao <= (horario.getPosicao() + horario.getNumDias() - 1))) {
                            errors.add("numero", new ActionError("error.rotacaoHorario"));
                            return errors;
                        }

                        if ((posicao >= posMaxPrimeiroHorario) && (posicao < horario.getPosicao())) {
                            if (numDias >= (horario.getPosicao() - posMaxPrimeiroHorario)) {
                                errors.add("rotacao", new ActionError("error.rotacaoHorario"));
                                return errors;
                            }
                            /* dia de descanso entre a rotacao dos horários */
                            if (((posicao - posMaxPrimeiroHorario) == 1)
                                    || (((horario.getPosicao()) - (posicao + numDias - 1)) == 1)) {
                                errors.add("dia de descanso", new ActionError(
                                        "error.rotacaoHorario.diaDescanso"));
                                return errors;
                            }

                        }

                        posMaxPrimeiroHorario = horario.getPosicao() + horario.getNumDias() - 1;
                    }

                    while (iterador.hasNext()) {
                        horario = (Horario) iterador.next();

                        if ((posicao >= horario.getPosicao())
                                && (posicao <= (horario.getPosicao() + horario.getNumDias() - 1))) {
                            errors.add("rotacao", new ActionError("error.rotacaoHorario"));
                            return errors;
                        }

                        if ((posicao > posMaxPrimeiroHorario) && (posicao < horario.getPosicao())) {
                            if (numDias >= (horario.getPosicao() - posMaxPrimeiroHorario)) {
                                errors.add("rotacao", new ActionError("error.rotacaoHorario"));
                                return errors;
                            }
                            /* dia de descanso entre a rotacao dos horários */
                            if (((posicao - posMaxPrimeiroHorario) == 1)
                                    || (((horario.getPosicao()) - (posicao + numDias - 1)) == 1)) {
                                errors.add("dia de descanso", new ActionError(
                                        "error.rotacaoHorario.diaDescanso"));
                                return errors;
                            }

                        }
                        posMaxPrimeiroHorario = horario.getPosicao() + horario.getNumDias() - 1;
                    }

                    if (posicao > posMaxPrimeiroHorario) {
                        // dia de descanso entre a rotacao dos horários
                        if ((posicao - posMaxPrimeiroHorario) == 1) {
                            errors.add("dia de descanso", new ActionError(
                                    "error.rotacaoHorario.diaDescanso"));
                            return errors;
                        }
                    }
                }

            }
        }

        // validacao dos outros campos
        IStrategyHorarios horario = SuporteStrategyHorarios.getInstance().callStrategy(getModalidade());
        errors = horario.validateAssociarHorario(this);

        return errors;
    } /* validate */
}